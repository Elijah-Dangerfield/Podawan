package com.dangerfield.libraries.app.startup

import android.animation.ValueAnimator
import android.app.Activity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import podawan.core.BuildInfo
import podawan.core.Catching
import timber.log.Timber
import javax.inject.Inject

/**
 * Custom handler for the native splash screen API.
 * This class immediately sets keepOnScreenCondition to false so that we can run our own
 * custom animation using setOnExitAnimationListener.
 */
class SplashScreenBuilder @Inject constructor(
    private val buildInfo: BuildInfo
) {
    private var keepOnScreenCondition: () -> Boolean = { true }
    private var showLoadingCondition: () -> Boolean = { true }
    private lateinit var internalSplashScreen: androidx.core.splashscreen.SplashScreen

    fun keepOnScreenWhile(condition: () -> Boolean): SplashScreenBuilder {
        this.keepOnScreenCondition = condition
        return this
    }

    fun showLoadingWhen(condition: () -> Boolean): SplashScreenBuilder {
        this.showLoadingCondition = condition
        return this
    }

    @Suppress("MagicNumber")
    fun build(activity: Activity) = Catching {
        internalSplashScreen = activity.installSplashScreen()

        if (buildInfo.deviceName.contains("OnePlus", ignoreCase = true)) {
            // oneplus devices have issues with splash screens setOnExitAnimationListener which
            // we rely on for manually animating the splash screen
            internalSplashScreen.setKeepOnScreenCondition(keepOnScreenCondition)
        } else {
            // relies onsetOnExitAnimationListener to keep on screen
            internalSplashScreen.setKeepOnScreenCondition { false }

            internalSplashScreen.setOnExitAnimationListener { splashScreenViewProvider ->

                Catching {

                    val animator = splashScreenViewProvider.startIconPulse()

                    (activity as? LifecycleOwner)?.lifecycleScope?.launch {
                        var isSplashScreenUp = keepOnScreenCondition()
                        while (isSplashScreenUp) {
                            delay(500)
                            isSplashScreenUp = keepOnScreenCondition()
                        }

                        animator?.pause()
                        splashScreenViewProvider.remove()
                    } ?: run {
                        splashScreenViewProvider.remove()
                        Timber.e("SplashScreenBuilder: Activity is not a lifecycle owner, splash screen will be removed")
                    }
                }.onFailure {
                    splashScreenViewProvider.remove()
                }
            }
        }
    }.onFailure { Timber.e("SplashScreenBuilder: Failed to build splash screen. Exception caught: $it")}

    @Suppress("MagicNumber", "TooGenericExceptionCaught")
    private fun SplashScreenViewProvider.startIconPulse(): ValueAnimator? = try {
        val animator = ValueAnimator.ofFloat(0.8f, 1.2f) // Scale values from 0.8x to 1.2x
        animator.addUpdateListener { animation ->
            try {
                if (iconView != null) {
                    val scale = animation.animatedValue as Float
                    iconView.scaleX = scale
                    iconView.scaleY = scale
                }
            } catch (t: Throwable) {
                Timber.e("SplashScreenBuilder: icon view was null. Exception caught: $t")
            }
        }
        animator.duration = 1500 // duration to complete one pulse cycle
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE // Reverses the animation at the end of each cycle
        animator.start()
        animator
    } catch (t: Throwable) {
        Timber.e("SplashScreenBuilder: Failed to start icon pulse. Exception caught: $t")
        null
    }

}


