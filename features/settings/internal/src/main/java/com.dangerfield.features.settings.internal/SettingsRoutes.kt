package com.dangerfield.features.settings.internal

import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.route

fun Router.navigateToAbout() {
    navigate(aboutRoute.noArgRoute())
}

fun Router.navigateToContactUs() {
    navigate(contactUsRoute.noArgRoute())
}

fun Router.navigateToReferral() {
    navigate(referralCode.noArgRoute())
}

val aboutRoute = route("about")


val referralCode = route("referralCode")

val contactUsRoute = route("contact_us")