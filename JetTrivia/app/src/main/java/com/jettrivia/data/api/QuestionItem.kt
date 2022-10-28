package com.jettrivia.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionItem(
    @SerialName("answer")
    val answer: String,
    @SerialName("category")
    val category: String,
    @SerialName("choices")
    val choices: List<String>,
    @SerialName("question")
    val question: String
)