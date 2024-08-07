package com.dangerfield.libraries.session

import kotlinx.coroutines.flow.Flow
import podawan.core.Catching

interface UserRepository {
    fun getUserFlow(): Flow<User>

    suspend fun login(email: String, password: String): Catching<LoginResult>

    suspend fun signup(email: String, password: String): Catching<SignupResult>

    fun logout(): Catching<Unit>
}