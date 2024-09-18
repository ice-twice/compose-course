package com.jetreader.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.jetreader.ui.nav.ReaderNavigation
import com.jetreader.ui.theme.JetReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReaderApp()
        }
    }
}

@Composable
fun ReaderApp() {
    JetReaderTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ReaderNavigation()
            }
        }
    }
}

@Composable
fun TextShimmerEffect() {
    val fontSize: TextUnit = 48.sp
    val fontSizePx: Float = with(LocalDensity.current) { fontSize.toPx() }
    val fontSizeDoublePx: Float = fontSizePx * 2


    val infiniteTransition: InfiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = fontSizeDoublePx,
        animationSpec = infiniteRepeatable(
            tween(
                1000,
                easing = LinearEasing
            )
        )
    )

    Text(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .wrapContentHeight(align = Alignment.CenterVertically),
        textAlign = TextAlign.Center,
        text = "Text shimmer effect",
        style = TextStyle(
            fontSize = fontSize,
            brush = Brush.linearGradient(
                listOf(Color(0xFF77D4D5), Color(0xFFBC87D7), Color(0xFFFF8495)),
                start = Offset(offset, offset),
                end = Offset(offset + fontSizePx, offset + fontSizePx),
                tileMode = TileMode.Mirror
            )
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun ReaderAppPreview() {
    ReaderApp()
}

@Preview(showBackground = true)
@Composable
fun TextShimmerEffectPreview() {
    JetReaderTheme {
        TextShimmerEffect()
    }
}
