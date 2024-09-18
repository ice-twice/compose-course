package com.jetreader.ui.screens.update

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.jetreader.data.remote.app.AppBook
import com.jetreader.ui.common.ui.AppBar
import com.jetreader.ui.common.ui.RoundedButton
import com.jetreader.ui.common.ui.TextInput
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun UpdateAppBookScreen(
    bookId: String,
    viewModel: UpdateAppBookViewModel = hiltViewModel(),
    onBackArrowClick: () -> Unit,
    onBookClick: () -> Unit,
) {
    val appBook: AppBook? by viewModel.appBook.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        AppBar(
            title = "Update books",
            icon = Icons.AutoMirrored.Default.ArrowBack,
            showProfile = false,
            onBackArrowClick = onBackArrowClick,
        )

    }) { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            appBook?.let { book ->
                BookInfo(book, onBookClick = onBookClick)

                BookNotes(
                    defaultValue = book.notes ?: "",
                    onUpdateBookNotes = viewModel::updateBookNote
                )

                StartFinishReadingBlock(
                    book,
                    onStartReading = viewModel::onStartReading,
                    onFinishReading = viewModel::onFinishReading,
                )

                RatingBar(rating = book.rating, onPressRating = viewModel::onRate)

                Spacer(Modifier.padding(bottom = 15.dp))

                RoundedButton(label = "Delete", onClick = viewModel::onDelete)
            }

        }
    }

    val lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(key1 = Unit) {
        viewModel.init(bookId = bookId)

        viewModel.navigateBack
            .flowWithLifecycle(lifecycle)
            .onEach {
                onBackArrowClick()
            }
            .launchIn(this)
    }
}

@Composable
fun BookInfo(appBook: AppBook, onBookClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = CircleShape,
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onBookClick)
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = rememberAsyncImagePainter(model = appBook.imageLink),
                contentDescription = null,
                modifier = Modifier
                    .width(120.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 120.dp, topEnd = 20.dp))
            )

            Column {
                Text(
                    text = appBook.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = appBook.authors,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = appBook.publishedDate,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun BookNotes(
    defaultValue: String,
    onUpdateBookNotes: (String) -> Unit,
) {
    Column {

        // TODO move this logic to view model
        val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextInput(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .height(140.dp),
            value = textFieldValue.value,
            label = "Enter your thoughts",
            onValueChange = { textFieldValue.value = it },
            onKeyboardAction = {
                val noteText: String = textFieldValue.value.trim()

                if (noteText.isNotEmpty()) {
                    onUpdateBookNotes(noteText)
                    keyboardController?.hide()
                }
            },
            placeholder = {
                Text(text = "Enter here...", color = Color.Black.copy(alpha = 0.5f))
            }
        )
    }
}

@Composable
fun StartFinishReadingBlock(
    appBook: AppBook,
    onStartReading: () -> Unit,
    onFinishReading: () -> Unit,
) {
    Row(modifier = Modifier.padding(horizontal = 8.dp)) {

        val startedReading: Boolean = appBook.startedReading != null
        if (!startedReading) {
            TextButton(onClick = onStartReading) {
                Text(text = "Start reading")
            }
        } else {
            Text(
                modifier = Modifier.weight(0.5f),
                text = "Started on: ${appBook.startedReading?.formatDate()}"
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        val finishedReading: Boolean = appBook.finishedReading != null
        if (!finishedReading) {
            TextButton(onClick = onFinishReading) {
                Text(text = "Mark as read")
            }
        } else {
            Text(
                modifier = Modifier.weight(0.5f),
                text = "Finished on: ${appBook.finishedReading?.formatDate()}"
            )
        }
    }
}

@Composable
fun RatingBar(rating: Int, onPressRating: (Int) -> Unit) {
    Text("Rating")

    Row {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier
                    .size(34.dp)
                    .clickable { onPressRating(index + 1) },
                tint = if (index + 1 <= rating) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}

private fun Timestamp.formatDate(): String =
    toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
