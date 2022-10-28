package com.jettrivia.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TriviaHome(viewModel: QuestionViewModel = viewModel()) {
    Questions(viewModel)
}
