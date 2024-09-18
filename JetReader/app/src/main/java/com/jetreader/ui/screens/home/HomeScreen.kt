package com.jetreader.ui.screens.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import com.jetreader.data.remote.app.AppBook
import com.jetreader.ui.common.ui.AppBar
import com.jetreader.ui.common.ui.FABContent
import com.jetreader.ui.common.ui.ListCard
import com.jetreader.ui.common.ui.TitleSection
import com.jetreader.ui.theme.JetReaderTheme

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit,
    onAddNewBook: () -> Unit,
    onBookFromReadingActivityClick: (String) -> Unit,
    onBookFromReadingListClick: (String) -> Unit,
) {
    val addedBooks: List<AppBook> by viewModel.addedBooks.collectAsStateWithLifecycle()
    val readingNowBooks: List<AppBook> by viewModel.readingNowBooks.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        AppBar(title = "A.Reader", onLogoutClick = onLogoutClick)
    }, floatingActionButton = {
        FABContent(onClick = onAddNewBook)
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeContent(
                onBookFromReadingListClick = onBookFromReadingListClick,
                addedBooks = addedBooks,
                readingNowBooks = readingNowBooks,
                onBookFromReadingActivityClick = onBookFromReadingActivityClick
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllAppBooks()
    }
}

@Composable
fun HomeContent(
    onBookFromReadingListClick: (String) -> Unit,
    addedBooks: List<AppBook>,
    readingNowBooks: List<AppBook>,
    onBookFromReadingActivityClick: (String) -> Unit
) {
    val isPreview: Boolean = LocalInspectionMode.current

    val currentUserName: String = remember {
        if (!isPreview) {
            // TODO move to view model
            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.currentUser?.email?.split('@')?.get(0) ?: "N/A"
        } else ""
    }

    Column(
        modifier = Modifier
            .padding(2.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.align(alignment = Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleSection(label = "Your reading\nactivity right now.. ")

            Spacer(modifier = Modifier.fillMaxWidth(0.7f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = currentUserName,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                HorizontalDivider()
            }
        }
        ReadingRightNowArea(
            readingNowBooks = readingNowBooks,
            onBookFromReadingActivityClick = onBookFromReadingActivityClick
        )
        TitleSection(label = "Reading list")
        BookListArea(
            addedBooks = addedBooks,
            onBookFromReadingListClick = onBookFromReadingListClick
        )
    }
}

@Composable
fun ReadingRightNowArea(
    readingNowBooks: List<AppBook>,
    onBookFromReadingActivityClick: (String) -> Unit
) {
    if (readingNowBooks.isEmpty()) {
        Text(
            modifier = Modifier.padding(start = 5.dp, top = 23.dp),
            text = "No reading books found. Start reading",
            style = TextStyle(
                color = Color.Red.copy(alpha = 0.4f),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
        )

    } else {
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(280.dp)
                .horizontalScroll(scrollState)
        ) {
            readingNowBooks.forEach {// TODO use LazyRow
                ListCard(
                    appBook = it,
                    isStartedReading = true,
                    onBookClick = onBookFromReadingActivityClick
                )
            }
        }
    }
}

@Composable
fun BookListArea(addedBooks: List<AppBook>, onBookFromReadingListClick: (String) -> Unit) {
    if (addedBooks.isEmpty()) {
        Text(
            modifier = Modifier.padding(start = 5.dp, top = 23.dp),
            text = "No books found. Add a book",
            style = TextStyle(
                color = Color.Red.copy(alpha = 0.4f),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
        )

    } else {
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(280.dp)
                .horizontalScroll(scrollState)
        ) {
            addedBooks.forEach {// TODO use LazyRow
                ListCard(
                    appBook = it,
                    isStartedReading = false,
                    onBookClick = onBookFromReadingListClick
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    JetReaderTheme {
        HomeScreen(
            onLogoutClick = {},
            onBookFromReadingListClick = {},
            onAddNewBook = {},
            onBookFromReadingActivityClick = {})
    }
}


