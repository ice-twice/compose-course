package com.jetnote.data

import com.jetnote.model.Note

class NotesTestDataSource {
    fun loadNotes(): List<Note> {
        return listOf(
            Note(
                title = "Apply these 6 secret techniques to improve compose",
                description = "Believing these 6 myths about compose keeps you from growing"
            ),
            Note(
                title = "Don't waste time! 6 facts until you reach your compose",
                description = "How 6 things will change the way you approach compose"
            ),
            Note(
                title = "Compose awards: 6 reasons why they don't work & what you can do about it",
                description = "Compose doesn't have to be hard. read these 6 tips"
            ),
            Note(
                title = "Compose is your worst enemy. 6 ways to defeat it",
                description = "Compose on a budget: 6 tips from the great depression"
            ),
            Note(
                title = "Knowing these 6 secrets will make your compose look amazing",
                description = "Master the art of compose with these 6 tips"
            ),
            Note(
                title = "My life, my job, my career: how 6 simple compose helped me succeed",
                description = "Take advantage of compose - read these 6 tips"
            ),
            Note(
                title = "The next 6 things you should do for compose success",
                description = "The time is running out! think about these 6 ways to change your compose"
            ),
            Note(
                title = "The 6 best things about compose",
                description = "The 6 biggest compose mistakes you can easily avoid"
            ),
            Note(
                title = "The 6 most successful compose companies in region",
                description = "Think your compose is safe? 6 ways you can lose it today"
            ),
            Note(
                title = "Thinking about compose? 6 reasons why it's time to stop!",
                description = "6 places to get deals on compose"
            )
        )
    }
}