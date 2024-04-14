package com.dangerfield.libraries.session

sealed class LoginResult {
    object Success : LoginResult()
    object InvalidCredentials : LoginResult()
}