package com.abedit.moqo_assessment.ui.detailscomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//Section title and value, value can be null to just show the section title
@Composable
fun SectionWithValue(title: String, data: String?) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray,
            fontSize = 13.sp,
            modifier = Modifier.padding(end = 10.dp)
        )

        if (!data.isNullOrEmpty()) {
            Text(
                text = data.capitalize(Locale.current),
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 18.sp,
            )
        }

    }

}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview_withData() {
    SectionWithValue(title = "Vehicle Type", data = "car")
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview_noData() {
    SectionWithValue(title = "Vehicle Type", data = null)
}