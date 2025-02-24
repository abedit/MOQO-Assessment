package com.abedit.moqo_assessment.ui.sharedcomponents

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.abedit.moqo_assessment.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

//Load an image from a URL, otherwise load the placeholder
@Composable
fun AsyncImage(
    imageURL: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    placeholder: Painter = painterResource(R.drawable.placeholder)
) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(imageURL) {
        if (imageURL.isNullOrEmpty()) {
            bitmap = null
            isLoading = false
            return@LaunchedEffect
        }

        isLoading = true
        bitmap = withContext(Dispatchers.IO) {
            try {
                URL(imageURL).openStream().use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: IOException) {
                null
            }
        }
        isLoading = false
    }

    Image(
        painter = bitmap?.let { mBitmap ->
            BitmapPainter(mBitmap.asImageBitmap())
        } ?: placeholder,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
private fun DefaultPreview_placeholder() {
    AsyncImage(imageURL = null, modifier = Modifier, contentDescription = "", placeholder = painterResource(R.drawable.placeholder))
}
