package com.dangerfield.features.playlist.internal

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.ui.FieldState
import com.dangerfield.libraries.ui.FieldState.Idle
import com.dangerfield.libraries.ui.FieldState.Invalid
import com.dangerfield.libraries.ui.FieldState.Valid
import com.dangerfield.libraries.ui.isValid
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class NewPlaylistViewModel @Inject constructor(
    private val playlistRepository: com.dangerfield.features.playlist.PlaylistRepository,
    savedStateHandle: SavedStateHandle,
) : SEAViewModel<NewPlaylistViewModel.State, NewPlaylistViewModel.Event, NewPlaylistViewModel.Action>(
    savedStateHandle = savedStateHandle,
    initialStateArg = State()
) {

    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.UpdatePlaylistName -> action.updatePlaylistName()
            is Action.Create -> action.create()
        }
    }

    private suspend fun Action.UpdatePlaylistName.updatePlaylistName() {
        clearFormError()
        val name = playlistName.take(100)
        // eagerly validate, but debounce the invalidation
        if (name.isValidPlaylistName()) {
            updateState { it.copy(playlistNameFieldState = Valid(name)) }
        } else {
            updateState { it.copy(playlistNameFieldState = Idle(name)) }
        }

        updateStateDebounced(duration = 1.seconds) {
            when {
                name.isEmpty() -> it.copy(playlistNameFieldState = Idle(name))
                name.isValidPlaylistName() -> it.copy(playlistNameFieldState = Valid(name))
                else -> {
                    it.copy(playlistNameFieldState = Invalid(name, "Playlists must be at least 3 characters long"))
                }
            }
        }
    }


    private suspend fun Action.Create.create() {
        val name = state.playlistNameFieldState.value
        if (!state.playlistNameFieldState.isValid() || name == null) {
            Timber.d("Log inForm is not valid when submitting")
        } else {
            updateState { it.copy(isLoading = true) }
            playlistRepository.createPlaylist(name)
                .onSuccess { id ->
                    updateState { it.copy(isLoading = false) }
                    sendEvent(Event.PlaylistCreated(id))
                }
                .onFailure { error ->
                    updateState { it.copy(isLoading = false) }
                    sendEvent(Event.CreateFailed(error::class.simpleName))
                }
        }
    }

    private fun clearFormError() {
        if (state.playlistNameFieldState is FieldState.Error) {
            takeAction(Action.UpdatePlaylistName(state.playlistNameFieldState.value.orEmpty()))
        }
    }

    private fun String.isValidPlaylistName(): Boolean {
        // TODO prevent duplicate playlist names
        return this.length >= 3
    }


    @Immutable
    data class State(
        val playlistNameFieldState: FieldState<String> = Idle(""),
        val isLoading: Boolean = false
    )

    sealed class Action {
        data class UpdatePlaylistName(val playlistName: String) : Action()
        object Create : Action()
    }

    sealed class Event {
        class CreateFailed(val errorClass: String?) : Event()
        class PlaylistCreated(val id: Int, ) : Event()
    }
}