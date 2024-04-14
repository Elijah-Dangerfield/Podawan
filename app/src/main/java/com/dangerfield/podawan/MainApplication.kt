package com.dangerfield.podawan

import android.app.Application
import android.content.res.Configuration
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.dangerfield.libraries.coresession.internal.SessionRepository
import com.dangerfield.libraries.logging.RemoteLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import dagger.hilt.android.HiltAndroidApp
import podawan.core.ApplicationStateRepository
import podawan.core.Catching
import podawan.core.logOnFailure
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class PodawanApplication : Application() {

    @Inject
    lateinit var applicationStateRepository: ApplicationStateRepository

    @Inject
    lateinit var firebaseAnalytics: Provider<FirebaseAnalytics>

    @Inject
    lateinit var firebaseFirestore: Provider<FirebaseFirestore>

    private val lifecycle get() = ProcessLifecycleOwner.get().lifecycle

    init {
        if (BuildConfig.DEBUG || BuildConfig.IS_QA) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applicationStateRepository.onConfigurationChanged(newConfig)
    }

    override fun onCreate() {
        super.onCreate()

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                applicationStateRepository.onAppStart()
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                applicationStateRepository.onAppStop()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                Timber.d("Application on destroy")
                applicationStateRepository.onAppDestroyed()
            }
        })

        Timber.plant(RemoteLogger())

        Catching {
            setupFireStore()
        }
            .logOnFailure()
    }

    private fun setupFireStore() {
        val analytics = firebaseAnalytics.get()
        val firestore = firebaseFirestore.get()

        val shouldCollectAnalytics = !BuildConfig.IS_QA

        analytics.setAnalyticsCollectionEnabled(shouldCollectAnalytics)

        analytics.setSessionTimeoutDuration(
            SessionRepository.SESSION_MAXIMUM_TIME_AWAY.inWholeMilliseconds
        )

        firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }
    }
}
