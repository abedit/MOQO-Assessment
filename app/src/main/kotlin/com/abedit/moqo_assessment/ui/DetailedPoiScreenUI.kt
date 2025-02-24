package com.abedit.moqo_assessment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abedit.moqo_assessment.R
import com.abedit.moqo_assessment.data.model.DetailedPointOfInterest
import com.abedit.moqo_assessment.states.DetailUiState
import com.abedit.moqo_assessment.ui.detailscomponents.ImageWithText
import com.abedit.moqo_assessment.ui.detailscomponents.SectionWithValue
import com.abedit.moqo_assessment.ui.sharedcomponents.AsyncImage
import kotlin.math.max


@Composable
fun DetailedPoiScreenUI(
    detailUiState: DetailUiState
) {

    val topRoundedCornerShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, topRoundedCornerShape)

    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            .background(Color.LightGray, topRoundedCornerShape)
        ) {
            Box(modifier = Modifier
                .background(Color.White, RoundedCornerShape(30.dp))
                .fillMaxWidth(0.15f)
                .height(3.dp)
                .align(Alignment.Center)
            )
        }


        when (detailUiState) {
            is DetailUiState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally).padding(vertical = 30.dp))
            }

            is DetailUiState.Error -> {
                Text(
                    text = stringResource(R.string.error_message, detailUiState.message),
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 30.dp)
                )
            }

            is DetailUiState.Success -> {
                POIDetailsContent(poi = detailUiState.pointOfInterest)

            }
        }

    }

}

@Composable
private fun POIDetailsContent(poi: DetailedPointOfInterest) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(15.dp)
    ) {

        //POI name and icon
        ImageWithText(
            imageUrl = poi.image?.url,
            text = poi.name,
            textSize = 20.sp,
            contentDescription = stringResource(R.string.poi_image_content_description),
            maxLines = 4
        )

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Spacer(Modifier)

            SectionWithValue(
                title = stringResource(R.string.vehicle_type),
                data = poi.vehicleType ?: stringResource(R.string.unknown_vehicle_type)
            )

            // show images in a row
            val images = listOfNotNull(
                poi.image?.thumbUrl,
                poi.image?.mediumUrl,
                poi.image?.url
            ).distinct()

            LazyRow {
                items(images) { imageUrl ->
                    AsyncImage(
                        imageURL = imageUrl,
                        contentDescription = stringResource(id = R.string.poi_image_content_description),
                        modifier = Modifier.size(120.dp).padding(end = 5.dp)
                    )
                }
            }

            //Provider data section
            poi.provider?.let { provider ->
                SectionWithValue(title = stringResource(R.string.provider), data = null)

                provider.image?.url?.let { providerImageUrl ->

                    ImageWithText(
                        imageUrl = providerImageUrl,
                        text = provider.name,
                        textSize = 18.sp,
                        contentDescription = stringResource(R.string.provider_logo),
                        maxLines = 4
                    )

                }
            } ?: SectionWithValue(
                stringResource(R.string.provider),
                stringResource(R.string.unknown_provider)
            )
        }
    }
}


@Preview
@Composable
private fun DefaultPreview_Success() {
    val detailedPoint = DetailedPointOfInterest(
        id = "1",
        name = "Test POI",
        lat = 52.520008,
        lng = 13.404954,
        vehicleType = "Car",
        image = null,
        provider = null
    )
    DetailedPoiScreenUI(
        detailUiState = DetailUiState.Success(detailedPoint)
    )
}

@Preview
@Composable
private fun DefaultPreview_Error() {
    DetailedPoiScreenUI(
        detailUiState = DetailUiState.Error("Failed to get details")
    )
}


@Preview
@Composable
private fun DefaultPreview_Loading() {
    DetailedPoiScreenUI(
        detailUiState = DetailUiState.Loading
    )
}