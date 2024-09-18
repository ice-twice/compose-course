package com.jetreader.ui.screens.details

import android.text.Spanned
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.jetreader.domain.book.SearchBook
import com.jetreader.ui.common.ui.AppBar
import com.jetreader.ui.common.ui.RoundedButton
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@Composable
fun SearchBookDetailsScreen(
    bookId: String,
    onBackArrowClick: () -> Unit,
    viewModel: SearchBookDetailsViewModel = hiltViewModel<SearchBookDetailsViewModel>()
) {
    val searchBook: SearchBook? by viewModel.searchBook.collectAsStateWithLifecycle()
    val isLoading: Boolean by viewModel.isLoading.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        AppBar(
            title = "Book details",
            icon = Icons.AutoMirrored.Default.ArrowBack,
            showProfile = false,
            onBackArrowClick = onBackArrowClick,
        )

    }) {
        Surface(
            modifier = Modifier
                .padding(it)
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    ShowBookDetails(
                        searchBook,
                        onSaveClick = viewModel::onSave,
                        onCancelClick = onBackArrowClick,
                    )
                }
            }
        }
    }

    val lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key1 = Unit) {
        viewModel.getSearchBook(bookId)

        viewModel.navigateBack
            .flowWithLifecycle(lifecycle)
            .onEach {
                onBackArrowClick()
            }
            .launchIn(this)
    }
}

@Composable
fun ShowBookDetails(
    searchBook: SearchBook?,
    onSaveClick: (SearchBook) -> Unit,
    onCancelClick: () -> Unit,
) {
    if (searchBook == null) return

    Card(
        modifier = Modifier
            .padding(start = 34.dp, end = 34.dp, bottom = 34.dp, top = 0.dp),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Image(
            painter = rememberAsyncImagePainter(model = searchBook.imageLink),
            contentDescription = null,
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .padding(1.dp)
        )
    }

    Text(
        text = searchBook.title,
        style = MaterialTheme.typography.titleLarge,
    )
    Text(text = "Authors: ${searchBook.authors}")
    Text(text = "Page count: ${searchBook.pageCount}")
    Text(text = "Categories: ${searchBook.categories}")
    Text(text = "Published: ${searchBook.publishedDate}")

    Spacer(modifier = Modifier.height(5.dp))

    val formattedDescription: Spanned = HtmlCompat.fromHtml(
        searchBook.description ?: "",
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )

    val height: Dp = LocalContext.current.resources.displayMetrics.heightPixels.dp.times(0.09f)
    Surface(
        modifier = Modifier
            .height(height)
            .padding(4.dp),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.DarkGray)
    ) {
        LazyColumn(modifier = Modifier.padding(3.dp)) {
            item {
                Text(formattedDescription.toString())
            }
        }
    }

    Row(modifier = Modifier.padding(top = 6.dp)) {
        RoundedButton(label = "Save") {
            onSaveClick(searchBook)
        }
        Spacer(modifier = Modifier.width(25.dp))
        RoundedButton(label = "Cancel", onClick = onCancelClick)
    }
}
