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
fun AboutScreen(
    modifier: Modifier = Modifier,
    versionName: String,
    shouldShowConsentFormOption: Boolean,
    onManageConsentClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit,
    onTermsOfServiceClicked: () -> Unit,
    onThirdPartyServicesClicked: () -> Unit ,
    onNavigateBack: () -> Unit,
) {
    Screen(
        modifier = modifier,
        topBar = {
            Header(
                title = dictionaryString(R.string.settings_about_header),
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
                text = dictionaryString(R.string.about_privacyPolicy_label),
                onClick = onPrivacyPolicyClicked,
                trailingIcon = PodawanIcon.ChevronRight(null),
            )

            SettingsOption(
                text = dictionaryString(R.string.about_termsOfService_label),
                onClick = onTermsOfServiceClicked,
                trailingIcon = PodawanIcon.ChevronRight(null),
            )

            SettingsOption(
                text = dictionaryString(R.string.about_thirdPartyServices_label),
                onClick = onThirdPartyServicesClicked,
                trailingIcon = PodawanIcon.ChevronRight(null),
            )

            if (shouldShowConsentFormOption) {
                SettingsOption(
                    text = dictionaryString(R.string.about_consentForm_label),
                    onClick = onManageConsentClicked,
                    trailingIcon = PodawanIcon.ChevronRight(null),
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
        AboutScreen(
            versionName = "X.Y.Z",
            onManageConsentClicked = {},
            onPrivacyPolicyClicked = { -> },
            onTermsOfServiceClicked = { -> },
            onThirdPartyServicesClicked = { -> },
            onNavigateBack = { -> },
            shouldShowConsentFormOption = true
        )
    }
}
