package com.example.huariquehub_mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.huariquehub_mobile.ui.theme.OrangeLight
import com.example.huariquehub_mobile.ui.theme.YellowGreen

/**
 * Imagen de un huarique cargada con Coil. Cae a un placeholder con degradado y
 * emoji 🍽️ cuando no hay `imageUrl`, mientras carga, o si la descarga falla.
 */
@Composable
fun HuariqueImage(
    url: String?,
    modifier: Modifier = Modifier,
    emojiSize: TextUnit = 56.sp
) {
    if (url.isNullOrBlank()) {
        EmojiPlaceholder(modifier, emojiSize)
    } else {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
            loading = { EmojiPlaceholder(Modifier.fillMaxSize(), emojiSize) },
            error = { EmojiPlaceholder(Modifier.fillMaxSize(), emojiSize) }
        )
    }
}

@Composable
private fun EmojiPlaceholder(modifier: Modifier, emojiSize: TextUnit) {
    Box(
        modifier = modifier.background(
            Brush.horizontalGradient(listOf(OrangeLight, YellowGreen.copy(alpha = 0.5f)))
        ),
        contentAlignment = Alignment.Center
    ) {
        Text("🍽️", fontSize = emojiSize)
    }
}
