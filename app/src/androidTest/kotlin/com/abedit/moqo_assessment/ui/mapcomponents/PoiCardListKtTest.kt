package com.abedit.moqo_assessment.ui.mapcomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.abedit.moqo_assessment.data.model.PointOfInterest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalTestApi
class PoiCardListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val samplePois = listOf(
        PointOfInterest(id = "1", lat = 0.0, lng = 0.0, name = "POI 1", vehicleType = "Car"),
        PointOfInterest(id = "2", lat = 0.0, lng = 0.0, name = "POI 2", vehicleType = "Bike"),
        PointOfInterest(id = "3", lat = 0.0, lng = 0.0, name = "POI 3", vehicleType = "Bus")
    )

    @Test
    fun poiCards_areDisplayedCorrectly() {
        composeTestRule.setContent {
            PoiCardList(
                modifier = Modifier,
                selectedPois = samplePois,
                onPoiClick = {}
            )
        }

        composeTestRule.onNodeWithText(samplePois.first().name).assertIsDisplayed()
    }

    @Test
    fun clickingOnPoiCard_triggersOnClick() {
        var clickedPoiId: String? = null

        composeTestRule.setContent {
            PoiCardList(
                modifier = Modifier,
                selectedPois = samplePois,
                onPoiClick = { clickedPoiId = it }
            )
        }

        composeTestRule.onNodeWithText(samplePois[0].name).performClick()
        assertEquals(samplePois[0].id, clickedPoiId)
    }

    @Test
    fun poiList_scrollsToFirstItemWhenUpdated() {
        composeTestRule.setContent {
            var pois by remember { mutableStateOf(samplePois) }

            Column {
                PoiCardList(
                    modifier = Modifier,
                    selectedPois = pois,
                    onPoiClick = {}
                )
                Button(onClick = { pois = listOf(samplePois[2]) }) {
                    Text("Update POIs")
                }
            }
        }

        composeTestRule.onNodeWithText(samplePois[1].name).performScrollTo()
        composeTestRule.onNodeWithText("Update POIs").performClick()
        composeTestRule.onNodeWithText(samplePois[2].name).assertIsDisplayed()
    }
}
