package com.jetreader.ui.screens.update

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.jetreader.data.BooksRepository
import com.jetreader.data.remote.app.AppBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateAppBookViewModel @Inject constructor(private val booksRepository: BooksRepository) :
    ViewModel() {

    val appBook: MutableStateFlow<AppBook?> = MutableStateFlow(null)
    private val _navigateBack = Channel<Unit>()
    val navigateBack = _navigateBack.consumeAsFlow()

    fun init(bookId: String) {
        booksRepository.subscribeAppBook(bookId)
            .onEach {
                appBook.value = it
            }
            .launchIn(viewModelScope)
    }

    fun updateBookNote(noteText: String) {
        appBook.value?.let {
            updateBook(it.copy(notes = noteText))
        }
    }

    fun onStartReading() {
        appBook.value?.let {
            updateBook(it.copy(startedReading = Timestamp.now()))
        }
    }

    fun onFinishReading() {
        appBook.value?.let {
            updateBook(it.copy(finishedReading = Timestamp.now()))
        }
    }

    fun onRate(rating: Int) {
        appBook.value?.let {
            updateBook(it.copy(rating = rating))
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            appBook.value?.let { appBook ->
                booksRepository.removeAppBook(appBook)
                    .onFailure {
                        Log.d(TAG, "Error occurred while updating app book. e: $it")
                    }
                    .onSuccess {
                        _navigateBack.send(Unit)
                    }
            }

        }
    }

    private fun updateBook(appBook: AppBook) {
        viewModelScope.launch {
            booksRepository.updateAppBook(appBook)
                .onFailure {
                    Log.d(TAG, "Error occurred while updating app book. e: $it")
                }
        }
    }

    private companion object {
        private val TAG = UpdateAppBookViewModel::class.simpleName
    }
}
