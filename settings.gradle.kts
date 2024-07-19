pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Podawan"

include("apps:stuffYouShouldKnow")
include("apps:stuffYouShouldKnow:config")
include("apps:theBenAndEmilShow")
include("apps:theBenAndEmilShow:config")
include("apps:fragmented")
include("apps:fragmented:config")
include("features:auth")
include("features:auth:internal")
include("features:blockingError")
include("features:blockingError:internal")
include("features:consent")
include("features:consent:internal")
include("features:feed")
include("features:feed:internal")
include("features:forcedUpdate")
include("features:forcedUpdate:internal")
include("features:inAppMessaging")
include("features:inAppMessaging:internal")
include("features:library")
include("features:library:internal")
include("features:playback")
include("features:playback:internal")
include("features:playlist")
include("features:playlist:internal")
include("features:qa")
include("features:qa:internal")
include("features:search")
include("features:search:internal")
include("features:settings")
include("features:settings:internal")
include("libraries:analytics")
include("libraries:analytics:internal")
include("libraries:app")
include("libraries:common")
include("libraries:common:internal")
include("libraries:config")
include("libraries:config:internal")
include("libraries:dictionary")
include("libraries:dictionary:internal")
include("libraries:flowroutines")
include("libraries:game")
include("libraries:game:internal")
include("libraries:logging")
include("libraries:logging:internal")
include("libraries:navigation")
include("libraries:navigation:internal")
include("libraries:network")
include("libraries:network:internal")
include("libraries:podcast")
include("libraries:podcast:internal")
include("libraries:podcast:storage")
include("libraries:resources")
include("libraries:resources:internal")
include("libraries:session")
include("libraries:session:internal")
include("libraries:session:storage")
include("libraries:storage")
include("libraries:storage:internal")
include("libraries:test")
include("libraries:test:internal")
include("libraries:ui")
include("libraries:ui:components")
include("libraries:ui:preview")
include("features:playlist:storage")