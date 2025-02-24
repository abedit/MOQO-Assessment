package com.abedit.moqo_assessment.ui.mapcomponents

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.abedit.moqo_assessment.data.model.PointOfInterest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.test.assertTrue

class PoiCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPoiCard_click() {
        var cardClicked = false
        val poi = PointOfInterest(id = "1", lat = 34.0, lng = 35.0, name = "POI 1", vehicleType = "Car")

        composeTestRule.setContent {
            PoiCard(
                poi = poi,
                onCardClick = {
                    cardClicked = true
                }
            )
        }

        composeTestRule.onNodeWithText("POI 1").performClick()
        assertTrue(cardClicked)
    }

    @Test
    fun testPoiCard_display() {
        val poi = PointOfInterest(id = "1", lat = 34.0, lng = 35.0, name = "POI 1", vehicleType = "Car")
        composeTestRule.setContent {
            PoiCard(
                poi = poi,
                onCardClick = {}
            )
        }
        composeTestRule.onNodeWithText("POI 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Car").assertIsDisplayed()
    }

    @Test
    fun testPoiCard_withNullVehicleType() {
        val poi = PointOfInterest(id = "1", lat = 34.0, lng = 35.0, name = "POI 1", vehicleType = null)
        composeTestRule.setContent {
            PoiCard(
                poi = poi,
                onCardClick = {}
            )
        }
        composeTestRule.onNodeWithText("POI 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Car").assertDoesNotExist()
    }
}
