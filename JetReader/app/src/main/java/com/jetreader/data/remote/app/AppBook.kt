package com.jetreader.data.remote.app

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

// TODO create a representation of this class in domain
data class AppBook(
    @Exclude
    var id: String = "",
    var title: String = "",
    var authors: String = "",
    var description: String? = null,
    var categories: String? = null,
    @get:PropertyName("image_link")
    @set:PropertyName("image_link")
    var imageLink: String? = null,
    @get:PropertyName("published_date")
    @set:PropertyName("published_date")
    var publishedDate: String = "",
    var pageCount: String = "",
    @get:PropertyName("google_book_id")
    @set:PropertyName("google_book_id")
    var googleBookId: String = "",
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String = "",
    var notes: String? = null,
    var rating: Int = 0,
    @get:PropertyName("started_reading_at")
    @set:PropertyName("started_reading_at")
    var startedReading: Timestamp? = null,
    @get:PropertyName("finished_reading_at")
    @set:PropertyName("finished_reading_at")
    var finishedReading: Timestamp? = null,
)
