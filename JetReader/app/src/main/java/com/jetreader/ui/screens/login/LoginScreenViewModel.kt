package com.jetreader.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jetreader.domain.common.runSuspendCatching
import com.jetreader.domain.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    private var _loading = false

    fun loginUserWithEmailAndPassword(email: String, password: String, navigateHome: () -> Unit) {
        if (_loading) return
        _loading = true
        viewModelScope.launch {
            runSuspendCatching {
                auth.signInWithEmailAndPassword(email.trim(), password.trim()).await()
                navigateHome()
            }.onFailure {
                Log.d("LoginScreenViewModel", "loginUserWithEmailAndPassword: $it")
            }
            _loading = false
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String, navigateHome: () -> Unit) {
        if (_loading) return
        _loading = true

        viewModelScope.launch {
            runSuspendCatching {
                val authResult: AuthResult =
                    auth.createUserWithEmailAndPassword(email.trim(), password.trim()).await()
                val displayName = authResult.user?.email?.split('@')?.get(0)
                createUser(displayName)
                navigateHome()
            }.onFailure {
                Log.d("LoginScreenViewModel", "createUserWithEmailAndPassword: $it")
            }
        }
        _loading = false
    }

    private suspend fun createUser(displayName: String?) {
        val user = User(
            id = null,
            userId = auth.currentUser?.uid.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = ":)",
            profession = "Fashion is my profession"
        )
        // TODO move to data
        firestore
            .collection("users")
            .add(user.toFirestoreDataFormat())
            .await()
    }
}
