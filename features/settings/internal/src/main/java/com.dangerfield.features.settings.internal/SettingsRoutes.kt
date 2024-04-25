package com.dangerfield.features.settings.internal

import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.navigateToAbout() {
    navigate(aboutRoute.fill())
}

fun Router.navigateToContactUs() {
    navigate(contactUsRoute.fill())
}

fun Router.navigateToReferral() {
    navigate(referralCode.fill())
}

val aboutRoute = route("about")


val referralCode = route("referralCode")

val contactUsRoute = route("contact_us")