package com.dangerfield.podawan.theBenAndEmilShow

import android.content.Context
import com.dangerfield.libraries.ui.color.Colors
import com.dangerfield.podawan.apps.theBenAndEmilShow.config.BenAndEmilShowColors
import com.dangerfield.podawan.apps.theBenAndEmilShow.config.TheBenAndEmilShowAppConfiguration
import com.dangerfield.podawan.theBenAndEmilShow.BuildConfig.VERSION_CODE
import com.dangerfield.podawan.theBenAndEmilShow.BuildConfig.VERSION_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import oddoneout.core.AppConfiguration
import podawan.core.App
import podawan.core.BuildInfo
import podawan.core.BuildType

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    fun provideBuildInfo(@ApplicationContext context: Context): BuildInfo =
        BuildInfo(
            versionCode = VERSION_CODE,
            versionName = VERSION_NAME,
            packageName = context.packageName,
            app  = App.TheBenAndEmilShow,
            buildType = when {
                BuildConfig.IS_QA -> BuildType.QA
                BuildConfig.DEBUG -> BuildType.DEBUG
                else -> BuildType.RELEASE
            }
        )

    @Provides
    fun providesAppConfiguration(
        @ApplicationContext context: Context
    ): AppConfiguration = TheBenAndEmilShowAppConfiguration(context)

    @Provides
    fun providesColors(): Colors = BenAndEmilShowColors
}
