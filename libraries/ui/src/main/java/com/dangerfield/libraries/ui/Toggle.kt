package com.dangerfield.libraries.ui

import androidx.compose.runtime.MutableState

fun MutableState<Boolean>.toggle() {
    value = !value
}