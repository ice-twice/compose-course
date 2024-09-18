package com.jetreader.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jetreader.ui.nav.ReaderScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _navigateNextScreen = Channel<ReaderScreen>()
    val navigateNextScreen = _navigateNextScreen.receiveAsFlow()

    init {
        viewModelScope.launch {
            if (auth.currentUser == null) {
                ReaderScreen.LoginScreen
            } else {
                ReaderScreen.HomeScreen
            }
                .let {
                    _navigateNextScreen.send(it)
                }
        }
    }
}
