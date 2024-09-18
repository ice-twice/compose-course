package com.jetreader.ui.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetreader.data.BooksRepository
import com.jetreader.domain.book.SearchBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBookDetailsViewModel @Inject constructor(private val booksRepository: BooksRepository) :
    ViewModel() {

    val searchBook: MutableStateFlow<SearchBook?> = MutableStateFlow(null)
    val isLoading = MutableStateFlow(false)
    private val _navigateBack = Channel<Unit>()
    val navigateBack = _navigateBack.consumeAsFlow()

    fun getSearchBook(bookId: String) {
        if (isLoading.value) return
        isLoading.value = true

        viewModelScope.launch {

            booksRepository.getSearchBookDetails(bookId)
                .onSuccess { searchBook.value = it }
                .onFailure {
                    Log.d(TAG, "Error occurred while loading a book. e: $it")
                }
            isLoading.value = false
        }
    }

    fun onSave(searchBook: SearchBook) {
        viewModelScope.launch {

            booksRepository.saveAppBook(searchBook)
                .onFailure { Log.e(TAG, "Error occurred while saving a book. e: $it") }
                .onSuccess { _navigateBack.send(Unit) }
        }
    }

    private companion object {
        private val TAG = SearchBookDetailsViewModel::class.simpleName
    }
}
