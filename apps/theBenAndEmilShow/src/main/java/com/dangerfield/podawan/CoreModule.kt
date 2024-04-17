package com.dangerfield.podawan

import android.content.Context
import android.content.SharedPreferences
import com.dangerfield.libraries.coreflowroutines.AppScope
import com.dangerfield.libraries.coreflowroutines.DispatcherProvider
import com.dangerfield.podawan.BuildConfig
import com.dangerfield.podawan.BuildConfig.VERSION_CODE
import com.dangerfield.podawan.BuildConfig.VERSION_NAME
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import oddoneout.core.AppConfiguration
import podawan.core.BuildInfo
import podawan.core.BuildType
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    fun provideBuildInfo(@ApplicationContext context: Context): BuildInfo =
        BuildInfo(
            versionCode = VERSION_CODE,
            versionName = VERSION_NAME,
            packageName = context.packageName,
            buildType = when {
                BuildConfig.IS_QA -> BuildType.QA
                BuildConfig.DEBUG -> BuildType.DEBUG
                else -> BuildType.RELEASE
            }
        )

    @Provides
    fun providesAppConfiguration(): AppConfiguration {
        return object: AppConfiguration {
            override val rssFeedLink: String = "https://feeds.megaphone.fm/QCD4626915194"
        }
    }
}
