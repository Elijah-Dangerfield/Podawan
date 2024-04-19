package com.dangerfield.features.settings.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.dangerfield.libraries.dictionary.dictionaryString
import com.dangerfield.ui.components.header.Header
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.VerticalSpacerD1200
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.text.Text
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.oddoneoout.features.settings.internal.R

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    versionName: String,
    isQaOptionEnabled: Boolean = false,
    isReferralFeatureEnabled: Boolean,
    onThemeOptionClicked: () -> Unit = { },
    onQaOptionClicked: () -> Unit = { },
    onAboutOptionClicked: () -> Unit = { },
    onNavigateBack: () -> Unit = { },
    onContactUsClicked: () -> Unit = { },
    onReferralClicked: () -> Unit = { }
) {
    Screen(
        modifier = modifier,
        topBar = {
            Header(
                title = dictionaryString(R.string.settings_settingsScreen_header),
                onNavigateBack = onNavigateBack
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = Dimension.D800)
        ) {

            SettingsOption(
                text = dictionaryString(R.string.settings_theme_label),
                onClick = onThemeOptionClicked,
                leadingIcon = PodawanIcon.Theme(null),
            )

            SettingsOption(
                leadingIcon = PodawanIcon.Info(null),
                text = dictionaryString(R.string.settings_about_label),
                onClick = onAboutOptionClicked
            )

            SettingsOption(
                text = dictionaryString(R.string.settings_contactUs_label),
                onClick = onContactUsClicked,
                leadingIcon = PodawanIcon.Chat(null),
            )

            if  (isReferralFeatureEnabled) {
                SettingsOption(
                    text = "Referral",
                    onClick = onReferralClicked,
                    leadingIcon = PodawanIcon.Person(null),
                )
            }

            if (isQaOptionEnabled) {
                SettingsOption(
                    text = dictionaryString(R.string.settings_qaMenu_label),
                    onClick = onQaOptionClicked,
                    leadingIcon = PodawanIcon.Android(null),
                )
            }

            VerticalSpacerD1200()

            Text(
                text = dictionaryString(R.string.settings_madeWithLove_text),
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                typography = PodawanTheme.typography.Body.B700,
                colorResource = PodawanTheme.colors.textDisabled
            )

            Text(
                text = "LocalAppName.current" + dictionaryString(
                    R.string.settings_version_label,
                    "version" to versionName
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                typography = PodawanTheme.typography.Body.B700,
                colorResource = PodawanTheme.colors.textDisabled
            )
        }
    }
}

@Composable
@Preview
private fun PreviewSettingsScreen() {
    com.dangerfield.libraries.ui.preview.Preview {
        SettingsScreen(
            versionName = "X.Y.Z",
            isQaOptionEnabled = true,
            isReferralFeatureEnabled = true
        )
    }
}
