package com.dangerfield.libraries.coreflowroutines

import javax.inject.Qualifier

/**
 * Annotation for a provided scope that is tied to the application lifecycle
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AppScope
