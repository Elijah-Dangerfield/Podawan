package com.dangerfield.podawan.theBenAndEmilShow

import com.dangerfield.libraries.app.PodawanApplication
import dagger.hilt.android.HiltAndroidApp
import podawan.core.Catching
import podawan.core.logOnFailure
import timber.log.Timber

@HiltAndroidApp
class Application : PodawanApplication() {

    init {
        if (BuildConfig.DEBUG || BuildConfig.IS_QA
        ) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onCreate() {
        super.onCreate()

        Catching {
            setupFireStore(shouldCollectAnalytics = !BuildConfig.IS_QA)
        }
            .logOnFailure()
    }
}
