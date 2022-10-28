package com.jettrivia.di

import com.jettrivia.data.QuestionRepository
import com.jettrivia.data.api.QuestionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideQuestionApi(): QuestionApi = QuestionApi.create()

    @Provides
    @Singleton
    fun provideQuestionRepository(questionApi: QuestionApi): QuestionRepository =
        QuestionRepository(questionApi)
}

