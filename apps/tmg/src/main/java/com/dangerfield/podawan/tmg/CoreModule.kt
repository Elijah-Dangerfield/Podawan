package com.dangerfield.podawan.tmg

import android.content.Context
import com.dangerfield.libraries.ui.color.Colors
import com.dangerfield.podawan.apps.tmg.config.TmgAppConfiguration
import com.dangerfield.podawan.apps.tmg.config.TmgColors
import com.dangerfield.podawan.tmg.BuildConfig.VERSION_CODE
import com.dangerfield.podawan.tmg.BuildConfig.VERSION_NAME
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
            app = App.TMG,
            buildType = when {
                BuildConfig.IS_QA -> BuildType.QA
                BuildConfig.DEBUG -> BuildType.DEBUG
                else -> BuildType.RELEASE
            }
        )

    @Provides
    fun providesAppConfiguration(
        @ApplicationContext context: Context
    ): AppConfiguration = TmgAppConfiguration(context)

    @Provides
    fun providesColors(): Colors = TmgColors
}
