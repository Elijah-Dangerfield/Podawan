package com.dangerfield.libraries.session

sealed class LoginResult {
    object Success : LoginResult()
    object InvalidCredentials : LoginResult()
}

sealed class SignupResult {
    object Success : SignupResult()
    object BadPassword : SignupResult()
    object EmailInUse : SignupResult()
}