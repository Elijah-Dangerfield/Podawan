package com.dangerfield.features.auth.internal

import javax.inject.Inject

class IsValidEmail @Inject constructor() {

    operator fun invoke(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        return emailRegex.matches(email)
    }
}