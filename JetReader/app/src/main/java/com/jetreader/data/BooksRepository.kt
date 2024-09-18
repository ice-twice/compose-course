package com.jetreader.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.jetreader.data.remote.app.AppBook
import com.jetreader.data.remote.search.SearchBooksApi
import com.jetreader.data.remote.search.mapToDomain
import com.jetreader.domain.book.SearchBook
import com.jetreader.domain.common.runSuspendCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// TODO exceptions may be converted to some kind of domain exception
class BooksRepository @Inject constructor(
    private val api: SearchBooksApi,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

    suspend fun getSearchBooks(searchQuery: String): Result<List<SearchBook>> = runSuspendCatching {
        api.getSearchBooks(searchQuery).items.map {
            it.mapToDomain()
        }
    }

    suspend fun getSearchBookDetails(bookId: String): Result<SearchBook> = runSuspendCatching {
        api.getSearchBookInfo(bookId).mapToDomain()
    }

    suspend fun saveAppBook(searchBook: SearchBook): Result<Unit> {
        val appBook = AppBook(
            title = searchBook.title,
            authors = searchBook.authors,
            description = searchBook.description,
            categories = searchBook.categories,
            imageLink = searchBook.imageLink,
            publishedDate = searchBook.publishedDate,
            pageCount = searchBook.pageCount,
            googleBookId = searchBook.id,
            userId = auth.currentUser?.uid!!
        )

        return runSuspendCatching {
            val docReference = firestore.collection("books").add(appBook).await()
            docReference.update("id", docReference.id).await()
        }
    }

    suspend fun getAllAppBooks(): Result<List<AppBook>> = runSuspendCatching {
        firestore.collection("books")
            .whereEqualTo("user_id", auth.currentUser?.uid)
            .get()
            .await()
            .map {
                it.toObject(AppBook::class.java)
            }
    }

    fun subscribeAppBook(bookId: String): Flow<AppBook> =
        firestore.collection("books")
            .document(bookId)
            .dataObjects<AppBook>()
            .filterNotNull()

    suspend fun updateAppBook(appBook: AppBook): Result<Unit> = runSuspendCatching {
        firestore.collection("books").document(appBook.id)
            .update(
                mapOf(
                    "started_reading_at" to appBook.startedReading,
                    "finished_reading_at" to appBook.finishedReading,
                    "notes" to appBook.notes,
                    "rating" to appBook.rating,
                )
            )
            .await()
    }

    suspend fun removeAppBook(appBook: AppBook): Result<Unit> = runSuspendCatching {
        firestore.collection("books").document(appBook.id)
            .delete()
            .await()
    }
}
