package com.abedit.moqo_assessment.ui.mapcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abedit.moqo_assessment.data.model.PointOfInterest

@Composable
fun PoiCard(
    poi: PointOfInterest,
    modifier: Modifier = Modifier,
    onCardClick: (String) -> Unit
) {
    // A POI card on the bottom whenever a marker on the map is clicked
    Card(
        modifier = modifier.clickable {
            onCardClick.invoke(poi.id)
        }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(60.dp)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = poi.name,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (poi.vehicleType != null)
                Text(
                    text = poi.vehicleType.capitalize(Locale.current),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    PoiCard(
        poi = PointOfInterest("1", 52.520008, 13.404954, "Berlin", "Car"),
        modifier = Modifier
    ) {}
}

@Preview
@Composable
private fun DefaultPreview_VehicleTypeNull() {
    PoiCard(
        poi = PointOfInterest("1", 52.520008, 13.404954, "Berlin", null),
        modifier = Modifier
    ) {}
}