package com.abedit.moqo_assessment.ui.mapcomponents

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abedit.moqo_assessment.data.model.PointOfInterest
import com.abedit.moqo_assessment.states.PoiListUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoadingBottomPillTest {

    private val testPoiList = listOf(
        PointOfInterest(
            id = "1",
            lat = 34.0,
            lng = 35.0,
            name = "Test POI 1",
            vehicleType = "Car"
        ),
        PointOfInterest(
            id = "2",
            lat = 34.5,
            lng = 35.5,
            name = "Test POI 2",
            vehicleType = "Bike"
        )
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingStateShowsProgressIndicator() {
        val loadingState = PoiListUiState.Loading(poiList = testPoiList)
        composeTestRule.setContent {
            LoadingBottomPill(poiListUiState = loadingState, poisCount = testPoiList.size)
        }
        composeTestRule.onNodeWithTag("loadingProgressIndicator").assertExists()
    }

    @Test
    fun testErrorStateShowsErrorMessage() {
        val errorState = PoiListUiState.Error(
            poiList = testPoiList,
            message = "Error loading POIs"
        )
        composeTestRule.setContent {
            LoadingBottomPill(poiListUiState = errorState, poisCount = testPoiList.size)
        }

        composeTestRule.onNodeWithText("Error loading POIs").assertExists()
    }

    @Test
    fun testSuccessStateShowsCorrectPoiCountMessage() {
        val successState = PoiListUiState.Success(poiList = testPoiList)
        composeTestRule.setContent {
            LoadingBottomPill(poiListUiState = successState, poisCount = testPoiList.size)
        }
        composeTestRule.onNodeWithText("2 points").assertExists()
    }

    @Test
    fun testSuccessStateShowsCorrectPluralMessageForMultiplePois() {
        val successState = PoiListUiState.Success(poiList = testPoiList)
        composeTestRule.setContent {
            LoadingBottomPill(poiListUiState = successState, poisCount = 5)
        }
        composeTestRule.onNodeWithText("5 points").assertExists()
    }
}
