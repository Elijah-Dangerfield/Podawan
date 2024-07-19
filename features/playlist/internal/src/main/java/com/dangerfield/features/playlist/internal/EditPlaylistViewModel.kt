package com.dangerfield.features.playlist.internal

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dangerfield.features.playlist.MapToPlaylistWithEpisodes
import com.dangerfield.features.playlist.PlaylistRepository
import com.dangerfield.features.playlist.playlistIdArgument
import com.dangerfield.libraries.coreflowroutines.AppScope
import com.dangerfield.libraries.coreflowroutines.DispatcherProvider
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.navigation.navArgument
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.MapEpisodesToDisplayable
import com.dangerfield.libraries.ui.FieldState
import com.dangerfield.libraries.ui.FieldState.Idle
import com.dangerfield.libraries.ui.FieldState.Invalid
import com.dangerfield.libraries.ui.FieldState.Valid
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import podawan.core.eitherWay
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class EditPlaylistViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playlistRepository: PlaylistRepository,
    private val mapEpisodesToDisplayable: MapEpisodesToDisplayable,
    private val mapToPlaylistWithEpisodes: MapToPlaylistWithEpisodes,
    @ApplicationContext private val applicationContext: Context,
    @AppScope private val appScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : SEAViewModel<EditPlaylistViewModel.State, EditPlaylistViewModel.Event, EditPlaylistViewModel.Action>(
    savedStateHandle = savedStateHandle,
    initialStateArg = State()
) {

    private val playlistId = savedStateHandle.navArgument<Int>(playlistIdArgument)

    private var startingPlaylistData: PlaylistData? = null

    private var photosToDelete = mutableSetOf<String>()

    init {
        takeAction(Action.Load)
    }

    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.UpdateName -> action.updateName()
            is Action.UpdateDescription -> action.updateDescription()
            is Action.Save -> action.save()
            is Action.Load -> action.load()
            is Action.UpdateImageUri -> action.updateImageUri()
        }
    }

    private suspend fun Action.UpdateImageUri.updateImageUri() {

        // when the photo changes, mark the previous one as needing deletion
        state.heroImageUrl?.let { photosToDelete.add(it) }

        val path = withContext(dispatcherProvider.io) {
            saveImageToLocalFile(imageUri)
        }

        photosToDelete.remove(path)

        updateState {
            it.copy(heroImageUrl = path)
        }
    }

    //TODO okay so if a user comes in and picks 30 photos and then backs out and doesnt save then
    // we have 30 photos on the device that arent being used. I need to make some cleanup
    private fun removePhotosToDelete() {
        appScope.launch {
            photosToDelete.mapNotNull {
                File(it).takeIf { f -> f.isFile }
            }.forEach {
                it.delete()
            }
        }
    }

    private fun saveImageToLocalFile(imageUri: Uri): String? {
        val inputStream =
            applicationContext.contentResolver.openInputStream(imageUri) ?: return null
        val file = File(
            applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "${System.currentTimeMillis()}.jpg"
        )
        val outputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }

    private suspend fun Action.Load.load() {
        if (playlistId == null) {
            sendEvent(Event.LoadFailed("Playlist ID is null"))
            return
        }

        updateState { it.copy(isLoading = true) }

        playlistRepository.getPlaylist(playlistId)
            .map { playlist ->
                startingPlaylistData = PlaylistData(
                    name = playlist.name,
                    description = playlist.description,
                    url = playlist.imageUrl
                )

                val playlistWithEpisodes = mapToPlaylistWithEpisodes(playlist)
                val episodes = mapEpisodesToDisplayable(playlistWithEpisodes.episodes).getOrThrow()
                    .toPersistentList()

                updateState {
                    it.copy(
                        nameFieldState = Valid(playlist.name),
                        descriptionFieldState = Valid(playlist.description),
                        episodes = episodes,
                        isLoading = false,
                        heroImageUrl = playlist.imageUrl
                    )
                }
            }
            .onFailure {
                sendEvent(Event.LoadFailed(it.javaClass.simpleName))
                updateState { it.copy(isLoading = false) }
            }
    }

    private suspend fun Action.Save.save() {

        removePhotosToDelete()

        if (playlistId == null) {
            sendEvent(Event.SaveFailed("Playlist ID is null"))
            return
        }

        updateState { it.copy(isLoadingSave = true) }

        playlistRepository.updatePlaylist(
            id = playlistId,
            name = state.nameFieldState.value.orEmpty(),
            description = state.descriptionFieldState.value.orEmpty(),
            imageUrl = state.heroImageUrl
        )
            .onSuccess {
                sendEvent(Event.SaveSuccess)
            }
            .onFailure {
                sendEvent(Event.SaveFailed(it.javaClass.simpleName))
            }
            .eitherWay {
                updateState { it.copy(isLoadingSave = false) }
            }
    }

    private suspend fun Action.UpdateDescription.updateDescription() {
        updateState { it.copy(descriptionFieldState = Valid(description.take(150))) }
    }

    private suspend fun Action.UpdateName.updateName() {
        // eagerly validate, but debounce the invalidation
        val safeName = name.take(100)

        if (safeName.length >= 3) {
            updateState { it.copy(nameFieldState = Valid(safeName)) }
        } else {
            // negative validaitons are debounced
            updateState { it.copy(nameFieldState = Idle(safeName)) }
        }

        updateStateDebounced(duration = 1.seconds) {
            if (safeName.length >= 3) {
                // no update needed, positive validations arent debounced
                it
            } else {
                it.copy(
                    nameFieldState = Invalid(
                        safeName,
                        "Playlist names must be at least 3 characters long"
                    )
                )
            }
        }
    }

    override suspend fun mapEachState(state: State): State {
        val name = state.nameFieldState.value.orEmpty()
        val description = state.descriptionFieldState.value.orEmpty()
        val hasMadeChanges =
            startingPlaylistData != PlaylistData(name, description, state.heroImageUrl)

        return state.copy(
            isFormValid = state.nameFieldState is Valid && state.descriptionFieldState is Valid,
            hasMadeChanges = hasMadeChanges
        )
    }

    @Immutable
    data class State(
        val nameFieldState: FieldState<String> = Idle(""),
        val descriptionFieldState: FieldState<String> = Idle(""),
        val episodes: ImmutableList<DisplayableEpisode> = persistentListOf(),
        val isFormValid: Boolean = false,
        val isLoading: Boolean = false,
        val isLoadingSave: Boolean = false,
        val heroImageUrl: String? = null,
        val hasMadeChanges: Boolean = false
    )

    sealed class Action {

        data object Load : Action()
        data class UpdateName(val name: String) : Action()
        data class UpdateDescription(val description: String) : Action()

        data class UpdateImageUri(val imageUri: Uri) : Action()

        object Save : Action()
    }

    sealed class Event {
        class SaveFailed(val errorClass: String?) : Event()

        class LoadFailed(val errorClass: String?) : Event()

        object SaveSuccess : Event()
    }

    data class PlaylistData(
        val name: String,
        val description: String,
        val url: String?
    )
}