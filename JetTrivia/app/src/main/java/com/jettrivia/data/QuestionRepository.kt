package com.jettrivia.data

import android.util.Log
import com.jettrivia.data.api.QuestionApi
import com.jettrivia.domain.Question
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {

    suspend fun loadAllQuestion(): Result<List<Question>> = try {
        val questions = api.loadAllQuestions().map {
            Question(
                answer = it.answer,
                category = it.category,
                choices = it.choices,
                question = it.question
            )
        }
        Result.success(questions)
    } catch (e: Exception) {
        Log.e(TAG, "Error occurred while loading questions: $e")
        Result.failure(e) // TODO exception must be converted to some kind of domain exception
    }

    companion object {
        private val TAG = QuestionRepository::class.simpleName
    }
}