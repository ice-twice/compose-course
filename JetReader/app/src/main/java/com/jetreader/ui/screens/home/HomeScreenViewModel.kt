package com.jetreader.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetreader.data.BooksRepository
import com.jetreader.data.remote.app.AppBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val booksRepository: BooksRepository) :
    ViewModel() {

    val addedBooks: MutableStateFlow<List<AppBook>> = MutableStateFlow(emptyList())
    val readingNowBooks: MutableStateFlow<List<AppBook>> = MutableStateFlow(emptyList())

    fun getAllAppBooks() {
        viewModelScope.launch {

            booksRepository.getAllAppBooks()
                .onSuccess { allBooks ->
                    addedBooks.value = allBooks.filter {
                        it.startedReading == null && it.finishedReading == null
                    }
                    readingNowBooks.value = allBooks.filter {
                        it.startedReading != null && it.finishedReading == null
                    }
                }
                .onFailure {
                    Log.d(TAG, "Error occurred while loading app books. e: $it")
                }
        }
    }

    private companion object {
        private val TAG = HomeScreenViewModel::class.simpleName
    }
}
