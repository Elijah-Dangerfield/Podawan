package com.dangerfield.features.settings.internal.referral

import com.dangerfield.libraries.config.AppConfigMap
import com.dangerfield.libraries.config.Experiment
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class IsReferralFeatureEnabled @Inject constructor(
    private val appConfigMap: AppConfigMap
): Experiment<Boolean>() {
    override val displayName: String = "Referral Feature Enabled"
    override val control: Boolean = false
    override val test: Boolean = true
    override val id: String = "is_referral_feature_enabled"
    override val default: Boolean = false
    override val isDebugOnly: Boolean = true
    override fun resolveValue() = appConfigMap.experiment(this)
}