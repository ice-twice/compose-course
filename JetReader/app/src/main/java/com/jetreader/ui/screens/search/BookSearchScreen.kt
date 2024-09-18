package com.jetreader.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.jetreader.domain.book.SearchBook
import com.jetreader.ui.common.ui.AppBar
import com.jetreader.ui.common.ui.TextInput
import com.jetreader.ui.theme.JetReaderTheme

@Composable
fun BookSearchScreen(
    bookSearchViewModel: BookSearchViewModel = hiltViewModel<BookSearchViewModel>(),
    onBackArrowClick: () -> Unit,
    onBookClick: (String) -> Unit,
) {
    val searchBooks: List<SearchBook> by bookSearchViewModel.books.collectAsStateWithLifecycle()
    val isLoading: Boolean by bookSearchViewModel.isLoading.collectAsStateWithLifecycle()
    BookSearchScreenInternal(
        onBackArrowClick = onBackArrowClick,
        searchBooks = searchBooks,
        onSearch = bookSearchViewModel::getBooks,
        isLoading = isLoading,
        onBookClick = onBookClick,
    )
}

@Composable
fun BookSearchScreenInternal(
    onBackArrowClick: () -> Unit,
    searchBooks: List<SearchBook>,
    onSearch: (String) -> Unit,
    isLoading: Boolean,
    onBookClick: (String) -> Unit,
) {
    Scaffold(topBar = {
        AppBar(
            title = "Search book",
            icon = Icons.AutoMirrored.Default.ArrowBack,
            showProfile = false,
            onBackArrowClick = onBackArrowClick,
        )

    }) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            Column {
                SearchForm(
                    onSearch = onSearch
                )
                Spacer(modifier = Modifier.height(13.dp))
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                BookList(searchBooks = searchBooks, onBookClick = onBookClick)
            }
        }
    }
}

@Composable
private fun SearchForm(
    onSearch: (String) -> Unit,
) {
    Column {
        // TODO keep search query in view model
        val searchQueryState: MutableState<String> = rememberSaveable { mutableStateOf("") }
        val keyboardController: SoftwareKeyboardController? =
            LocalSoftwareKeyboardController.current
        val valid: Boolean = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }

        TextInput(
            value = searchQueryState.value,
            label = "Search",
            onValueChange = { searchQueryState.value = it },
            onKeyboardAction = {
                if (valid) {
                    onSearch(searchQueryState.value.trim())
                    keyboardController?.hide()
                }
            },
        )
    }
}

@Composable
fun BookList(searchBooks: List<SearchBook>, onBookClick: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxHeight(), contentPadding = PaddingValues(16.dp)) {
        items(searchBooks) { book ->
            BookRow(searchBook = book, onBookClick = onBookClick)
        }
    }
}

@Composable
fun BookRow(searchBook: SearchBook, onBookClick: (String) -> Unit) {
    Card(modifier = Modifier
        .clickable { onBookClick(searchBook.id) }
        .fillMaxWidth()
        .height(140.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp)) {
        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {
            Image(
                painter = rememberAsyncImagePainter(model = searchBook.imageLink),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )
            Column {
                Text(text = searchBook.title, overflow = TextOverflow.Ellipsis, maxLines = 2)
                Text(
                    text = "Authors: ${searchBook.authors}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.labelMedium,
                    fontStyle = FontStyle.Italic,
                    maxLines = 2,
                )

                Text(
                    text = "Date: ${searchBook.publishedDate}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.labelMedium,
                    fontStyle = FontStyle.Italic,
                )

                Text(
                    text = "Categories: ${searchBook.categories}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.labelMedium,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}

@Preview
@Composable
private fun BookSearchScreenPreview() {
    JetReaderTheme {
        BookSearchScreenInternal(
            onBackArrowClick = {},
            searchBooks = listOf(
                SearchBook(
                    id = "id1",
                    title = "Title1",
                    authors = "Authors1",
                    notes = null,
                    imageLink = "",
                    publishedDate = "01.02.2222",
                    categories = "Science",
                    pageCount = "123",
                    description = "Description",
                ),
                SearchBook(
                    id = "id2",
                    title = "Title2",
                    authors = "Authors2",
                    notes = null,
                    imageLink = "",
                    publishedDate = "01.02.2222",
                    categories = "Science",
                    pageCount = "123",
                    description = "Description",
                ),
                SearchBook(
                    id = "id3",
                    title = "Title3",
                    authors = "Authors3",
                    notes = null,
                    imageLink = "",
                    publishedDate = "01.02.2222",
                    categories = "Science",
                    pageCount = "123",
                    description = "Description",
                ),
                SearchBook(
                    id = "id4",
                    title = "Title4",
                    authors = "Authors4",
                    notes = null,
                    imageLink = "",
                    publishedDate = "01.02.2222",
                    categories = "Science",
                    pageCount = "123",
                    description = "Description",
                ),
                SearchBook(
                    id = "id5",
                    title = "Title5",
                    authors = "Authors5",
                    notes = null,
                    imageLink = "",
                    publishedDate = "01.02.2222",
                    categories = "Science",
                    pageCount = "123",
                    description = "Description",
                ),
            ),
            onSearch = {},
            isLoading = true,
            onBookClick = {},
        )
    }
}
