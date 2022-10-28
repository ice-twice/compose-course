package com.jettrivia.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jettrivia.domain.Question
import com.jettrivia.ui.theme.AppColors

@Composable
fun Questions(viewModel: QuestionViewModel) {
    val viewState = viewModel.viewState.value
    if (viewState.loading) {
        CircularProgressIndicator()
    } else {
        QuestionDisplay(
            viewState.correctAnswersCount,
            viewState.questionsSize,
            viewState.questionIndex,
            viewState.question,
            viewState.selectedChoiceState,
            { selectedChoiceIndex -> viewModel.onSelectChoice(selectedChoiceIndex) }
        ) { viewModel.onNextQuestion() }
    }
}

@Composable
fun QuestionDisplay(
    correctAnswersCount: Int,
    questionsSize: Int,
    questionIndex: Int,
    question: Question?,
    selectedChoiceState: QuestionViewModel.SelectedChoiceState?,
    onSelectChoice: (Int) -> Unit,
    onNextQuestion: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColors.darkPurple
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (questionIndex > 3) {
                ShowProgress(questionIndex, questionIndex / questionsSize.toFloat())
            }
            QuestionTracker(questionIndex, questionsSize, correctAnswersCount)
            DrawDottedLine()
            question?.let { question ->
                Text(
                    text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxHeight(0.3f),
                    fontSize = 17.sp,
                    color = AppColors.offWhite,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
                question.choices.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.offDarkPurple,
                                        AppColors.offDarkPurple
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedChoiceState?.index == index,
                            onClick = { onSelectChoice(index) },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (selectedChoiceState?.isCorrect == true && selectedChoiceState.index == index) {
                                    Color.Green.copy(alpha = 0.2f)
                                } else {
                                    Color.Red.copy(alpha = 0.2f)
                                }
                            )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = if (selectedChoiceState?.isCorrect == true && selectedChoiceState.index == index) {
                                        Color.Green
                                    } else if (selectedChoiceState?.isCorrect == false && selectedChoiceState.index == index) {
                                        Color.Red.copy(alpha = 0.2f)
                                    } else {
                                        AppColors.offWhite
                                    },
                                    fontSize = 17.sp
                                ),
                            ) {
                                append(answerText)
                            }
                        }
                        Text(annotatedString, modifier = Modifier.padding(6.dp))
                    }
                }
            }
            Button(
                onClick = { onNextQuestion() },
                modifier = Modifier
                    .padding(3.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                shape = RoundedCornerShape(34.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.lightBlue)
            ) {
                Text(
                    text = "Next",
                    modifier = Modifier.padding(4.dp),
                    color = AppColors.offWhite,
                    fontSize = 17.sp
                )
            }
        }
    }
}

@Composable
fun ShowProgress(score: Int, progress: Float) {
    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.lightPurple,
                        AppColors.lightPurple
                    )
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {},
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .fillMaxWidth(progress)
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFFF95075),
                            Color(0xFFBE6BE5)
                        )
                    )
                ),
            enabled = false,
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Text(
                text = score.toString(),
                color = AppColors.offWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QuestionTracker(counter: Int, outOf: Int, correctAnswersCount: Int) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(
                style = SpanStyle(
                    color = AppColors.lightGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 27.sp
                )
            ) {
                append("Question $counter/")
                withStyle(
                    style = SpanStyle(
                        color = AppColors.lightGray,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                ) {
                    append("$outOf")
                    append("          Correct: $correctAnswersCount")
                }
            }
        }
    }, modifier = Modifier.padding(20.dp))
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = AppColors.lightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}