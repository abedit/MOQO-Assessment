package com.abedit.moqo_assessment.ui.detailscomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abedit.moqo_assessment.R
import com.abedit.moqo_assessment.ui.sharedcomponents.AsyncImage

@Composable
fun ImageWithText(
    imageUrl: String?,
    text: String,
    textSize: TextUnit,
    contentDescription: String,
    maxLines: Int = 1
) {
    Row {
        val imageModifier = Modifier
            .size(50.dp)
            .align(Alignment.CenterVertically)

        imageUrl?.let { providerImageUrl ->
            AsyncImage(
                imageURL = providerImageUrl,
                contentDescription = contentDescription,
                modifier = imageModifier
            )
        } ?: Image(
            painter = painterResource(R.drawable.placeholder),
            contentDescription = contentDescription,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )

        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 10.dp),
            maxLines = maxLines,
            fontWeight = FontWeight.Bold,
            fontSize = textSize,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ImageWithText(
        imageUrl = null,
        text = "Provider name",
        textSize = 30.sp,
        "",
        3
    )
}