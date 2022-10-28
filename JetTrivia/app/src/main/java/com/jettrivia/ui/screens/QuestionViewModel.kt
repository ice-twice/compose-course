package com.jettrivia.ui.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jettrivia.data.QuestionRepository
import com.jettrivia.domain.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class QuestionViewModel @Inject constructor(private val repository: QuestionRepository) :
    ViewModel() {
    val viewState: MutableState<ViewState> = mutableStateOf(ViewState())
    private val questionsFlow = MutableSharedFlow<List<Question>>()
    private val onNextQuestionFlow = MutableSharedFlow<Unit>()
    private val onSelectChoiceFlow = MutableSharedFlow<Int>()

    init {
        questionsFlow.flatMapLatest { questions ->
            if (questions.isEmpty()) {
                viewState.value = viewState.value.copy(questionsListIsEmpty = true)
                emptyFlow()
            } else {
                var questionIndex = UNDEFINED_INDEX
                var correctAnswersCount = 0
                var isCorrectAnswer = false
                onNextQuestionFlow
                    .onStart { emit(Unit) }
                    .flatMapLatest {
                        if (isCorrectAnswer) {
                            correctAnswersCount++
                        }
                        questionIndex++ // TODO handle case when the game is over

                        val question = questions[questionIndex]
                        viewState.value = viewState.value.copy(
                            questionsSize = questions.size,
                            questionIndex = questionIndex,
                            question = question,
                            selectedChoiceState = null,
                            correctAnswersCount = correctAnswersCount
                        )
                        onSelectChoiceFlow.onEach { choiceIndex ->
                            val correctAnswer = question.answer
                            val selectedChoice = question.choices[choiceIndex]
                            isCorrectAnswer = selectedChoice == correctAnswer
                            viewState.value = viewState.value.copy(
                                selectedChoiceState = SelectedChoiceState(
                                    index = choiceIndex,
                                    isCorrect = isCorrectAnswer
                                )
                            )
                        }
                    }
            }
        }.shareIn(viewModelScope, SharingStarted.Eagerly, 0)

        loadAllQuestions()
    }

    private fun loadAllQuestions() {
        viewState.value = viewState.value.copy(loading = true, e = null)
        viewModelScope.launch {
            repository.loadAllQuestion()
                .onSuccess {
                    questionsFlow.emit(it)
                }
                .onFailure { viewState.value = viewState.value.copy(e = it) }
            viewState.value = viewState.value.copy(loading = false)
        }
    }

    fun onSelectChoice(selectedChoiceIndex: Int) {
        viewModelScope.launch {
            onSelectChoiceFlow.emit(selectedChoiceIndex)
        }
    }

    fun onNextQuestion() {
        viewModelScope.launch {
            onNextQuestionFlow.emit(Unit)
        }
    }

    data class ViewState(
        val loading: Boolean = false,
        val e: Throwable? = null,
        val questionsListIsEmpty: Boolean = false,
        val questionsSize: Int = 0,
        val questionIndex: Int = 0,
        val question: Question? = null,
        val selectedChoiceState: SelectedChoiceState? = null,
        val correctAnswersCount: Int = 0
    )

    data class SelectedChoiceState(val index: Int = -1, val isCorrect: Boolean = false)

    private companion object {
        private const val UNDEFINED_INDEX: Int = -1
    }
}