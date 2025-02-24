package com.abedit.moqo_assessment.ui.sharedcomponents

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AsyncImageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun testAsyncImage_showsPlaceholderWhileLoading() {
        Dispatchers.setMain(testDispatcher)
        val contentDescription = "Test image"
        composeTestRule.setContent {
            AsyncImage(imageURL = "", contentDescription = contentDescription)
        }
        composeTestRule.onNodeWithContentDescription(contentDescription).assertExists()
    }

}
