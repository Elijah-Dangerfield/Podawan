package com.dangerfield.libraries.coresession.internal.user

import android.util.Log
import com.dangerfield.libraries.coreflowroutines.AppScope
import com.dangerfield.libraries.coreflowroutines.DispatcherProvider
import com.dangerfield.libraries.coreflowroutines.TriggerFlow
import com.dangerfield.libraries.coreflowroutines.childSupervisorScope
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.dangerfield.libraries.session.LoginResult
import com.dangerfield.libraries.session.User
import com.dangerfield.libraries.session.UserRepository
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import podawan.core.ApplicationStateRepository
import podawan.core.Catching
import podawan.core.awaitCatching
import podawan.core.ignoreValue
import podawan.core.logOnFailure
import se.ansman.dagger.auto.AutoBind
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@AutoBind
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val applicationStateRepository: ApplicationStateRepository,
    @AppScope private val appScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider
) : UserRepository {

    private val firebaseUserFlow: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    private val refreshUserTrigger = TriggerFlow()
    private var authStateListener: FirebaseAuth.AuthStateListener? = null

    private val userFlow = combine(
        firebaseUserFlow,
        refreshUserTrigger
    ) { firebaseUser, _ ->
        User(
            id = firebaseUser?.uid,
            languageCode = Locale.getDefault().language,
        )
    }
        .shareIn(
            appScope,
            SharingStarted.Eagerly,
            replay = 1
        )

    init {
        appScope.childSupervisorScope(dispatcherProvider.io).launch {
            authStateListener = FirebaseAuth.AuthStateListener { auth ->
                Log.d("Elijah", "Got user update")
                val user = auth.currentUser
                firebaseUserFlow.value = user
                if (user != null) {
                    firebaseAnalytics.setUserId(user.uid)
                }
            }.also {
                auth.addAuthStateListener(it)
            }
        }.invokeOnCompletion {
            authStateListener?.let { auth.removeAuthStateListener(it) }
        }

        applicationStateRepository.configurationChanges().collectIn(appScope) {
            refreshUserTrigger.pull()
        }
    }

    override fun getUserFlow(): Flow<User> = userFlow

    override suspend fun login(email: String, password: String): Catching<LoginResult> {
        return auth.signInWithEmailAndPassword(email, password)
            .awaitCatching()
            .fold(
                onSuccess = {
                    if (it.user == null) {
                        Log.d("Elijah", "did not log User in")
                        Catching.success(LoginResult.InvalidCredentials)
                    } else {
                        firebaseUserFlow.value = it.user
                        Log.d("Elijah", "Logged User in")
                        Catching.success(LoginResult.Success)
                    }
                },
                onFailure = {
                    when (it) {
                        is FirebaseAuthInvalidCredentialsException -> Catching.success(LoginResult.InvalidCredentials)
                        else -> Catching.failure(it)
                    }
                }
            )
    }

    override fun logout(): Catching<Unit> =
        Catching {
            auth.signOut()
        }
            .logOnFailure()
            .ignoreValue()
}