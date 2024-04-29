package com.dangerfield.libraries.ui.preview

import android.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.dictionary.Dictionary
import com.dangerfield.libraries.dictionary.LocalDictionary
import com.dangerfield.libraries.ui.LocalAppConfiguration
import com.dangerfield.libraries.ui.LocalAppState
import com.dangerfield.libraries.ui.LocalColors
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.thenIf
import com.dangerfield.podawan.apps.stuffYouShouldKnow.config.StuffYouShouldKnowAppConfiguration
import com.dangerfield.podawan.apps.stuffYouShouldKnow.config.StuffYouShouldKnowColors
import com.dangerfield.podawan.apps.theBenAndEmilShow.config.BenAndEmilShowColors
import com.dangerfield.podawan.apps.theBenAndEmilShow.config.TheBenAndEmilShowAppConfiguration
import com.dangerfield.podawan.apps.tmg.config.TmgAppConfiguration
import com.dangerfield.podawan.apps.tmg.config.TmgColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import podawan.core.App
import podawan.core.AppState
import podawan.core.Catching
import com.dangerfield.libraries.dictionary.applyArgs
import podawan.ui.R

/**
 * A composable that is suitable as the root for any composable preview
 *
 * It will set up the theme and some suitable defaults like a background color.
 */
@Composable
fun Preview(
    modifier: Modifier = Modifier,
    appState: AppState = DefaultAppState,
    app: App = App.TheBenAndEmilShow,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    showBackground: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {

    var context = LocalContext.current
    if (context !is ContextThemeWrapper && context !is ContextThemeWrapper) {
        context = ContextThemeWrapper(
            LocalContext.current,
            R.style.Theme_Spyfall
        )
    }


    val appConfiguration = when (app) {
        App.TMG -> TmgAppConfiguration(context)
        App.TheBenAndEmilShow -> TheBenAndEmilShowAppConfiguration(context)
        App.StuffYouShouldKnow -> StuffYouShouldKnowAppConfiguration(context)
    }

    val colors = when (app) {
        App.TMG -> TmgColors
        App.TheBenAndEmilShow -> BenAndEmilShowColors
        App.StuffYouShouldKnow -> StuffYouShouldKnowColors
    }

    val previewDictionary = object : Dictionary {
        override fun getString(key: Int, args: Map<String, String>): String = Catching {
            context.getText(key).toString().applyArgs(args)
        }.getOrNull() ?: "DNE"

        override fun getOptionalString(key: Int, args: Map<String, String>): String? = Catching {
            context.getText(key).toString().applyArgs(args)
        }.getOrNull()
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalAppConfiguration provides appConfiguration,
        LocalContext provides context,
        LocalDictionary provides previewDictionary,
        LocalAppState provides appState
    ) {
        PodawanTheme{
            Box(
                modifier = modifier
                    .thenIf(showBackground) {
                        background(PodawanTheme.colors.background.color)
                    }
                    .padding(contentPadding)
            ) {
                content()
            }
        }
    }
}

fun loremIpsum(wordCount: Int = 10): String {
    return LoremIpsum(wordCount).values.joinToString(" ")
}
fun loremIpsum(wordCountRange: IntRange = 2..10): String {
    return LoremIpsum(wordCountRange.random()).values.joinToString(" ")
}


val DefaultAppState = object: AppState {
    override val isOffline: StateFlow<Boolean> = MutableStateFlow(false)

    override val isPlayingContent: StateFlow<Boolean> = MutableStateFlow(false)

}