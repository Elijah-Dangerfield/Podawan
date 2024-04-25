package com.dangerfield.features.auth

import com.dangerfield.libraries.navigation.route

val loginRoute = route("login") {
    isTopLevelAlways(true)
}