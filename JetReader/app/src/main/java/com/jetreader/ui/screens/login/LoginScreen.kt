package com.jetreader.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jetreader.R
import com.jetreader.ui.common.ui.PasswordInput
import com.jetreader.ui.common.ui.ReaderTextLogo
import com.jetreader.ui.common.ui.TextInput
import com.jetreader.ui.theme.JetReaderTheme

@Composable
fun LoginScreen(loginViewModel: LoginScreenViewModel = hiltViewModel(), navigateHome: () -> Unit) {
    val isSignUp: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ReaderTextLogo()
            UserForm(isSignUp = isSignUp.value, onDone = { email, password ->

                if (isSignUp.value) {
                    loginViewModel.createUserWithEmailAndPassword(
                        email,
                        password,
                        navigateHome = navigateHome
                    )
                } else {
                    loginViewModel.loginUserWithEmailAndPassword(
                        email,
                        password,
                        navigateHome = navigateHome
                    )
                }
            })
            Row(
                Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = if (isSignUp.value) "Login" else "Sign up"
                Text(text = "New user?")
                Text(
                    text = text,
                    Modifier
                        .padding(start = 5.dp)
                        .clickable { isSignUp.value = !isSignUp.value },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun UserForm(
    isSignUp: Boolean,
    onDone: (String, String) -> Unit,
    isLoading: Boolean = false,
) {
    val email: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val password: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val passwordVisible: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    val validInputs: State<Boolean> = remember {
        derivedStateOf { email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty() }
    }

    val modifier = Modifier
        .height(300.dp)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        if (isSignUp) {
            Text(
                text = stringResource(id = R.string.create_account_hint),
                modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            )
        }

        TextInput(
            value = email.value,
            label = "Email",
            onValueChange = { email.value = it },
            onKeyboardAction = { passwordFocusRequest.requestFocus() },
            enabled = !isLoading,
        )

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            value = password.value,
            onValueChange = { password.value = it },
            onKeyboardAction = {
                if (validInputs.value) {
                    onDone(
                        email.value,
                        password.value
                    )
                }
            },
            passwordVisible = passwordVisible.value,
            onPasswordVisibilityClick = { passwordVisible.value = !passwordVisible.value },
            enabled = !isLoading,
        )

        SubmitButton(
            text = if (isSignUp) "Create account" else "Login",
            loading = isLoading,
            validInputs = validInputs.value,
            onClick = {
                onDone(email.value, password.value)
                keyboardController?.hide()
            },
        )
    }
}

@Composable
private fun SubmitButton(
    text: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape,
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(25.dp))
        } else {
            Text(text = text, Modifier.padding(5.dp))
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    JetReaderTheme { LoginScreen(navigateHome = {}) }
}
