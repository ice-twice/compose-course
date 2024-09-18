package com.jetreader.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetreader.data.BooksRepository
import com.jetreader.domain.book.SearchBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val booksRepository: BooksRepository) :
    ViewModel() {

    val books: MutableStateFlow<List<SearchBook>> = MutableStateFlow(emptyList())
    val isLoading = MutableStateFlow(false)

    fun getBooks(query: String) {
        if (isLoading.value || query.isEmpty()) return
        isLoading.value = true
        books.value = emptyList()

        viewModelScope.launch {

            booksRepository.getSearchBooks(query)
                .onSuccess { books.value = it }
                .onFailure {
                    Log.d("BookSearchViewModel", "error: $it")
                }
            isLoading.value = false
        }
    }
}
