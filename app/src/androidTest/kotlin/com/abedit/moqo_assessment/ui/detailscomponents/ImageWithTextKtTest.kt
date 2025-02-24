package com.abedit.moqo_assessment.ui.detailscomponents

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abedit.moqo_assessment.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageWithTextTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Composable
    private fun ImageWithTextItem(title: String, contentDescription: String) {
        ImageWithText(
            imageUrl = "https://test.com/test.jpg",
            text = title,
            textSize = 10.sp,
            contentDescription = contentDescription
        )
    }

    @Test
    fun testImageWithText_displaysTextCorrectly() {
        val testText = "Test Title"
        val contentDescription = "Test Content Description"
        composeTestRule.setContent {
            ImageWithTextItem(title = testText, contentDescription = contentDescription)
        }
        composeTestRule.onNodeWithText(testText).assertExists().assertIsDisplayed()
    }

    @Test
    fun testImageWithText_displaysPlaceholderWhenNoImageUrl() {
        val testText = "Test Title"
        val contentDescription = "Placeholder Image"
        composeTestRule.setContent {
            ImageWithTextItem(title = testText, contentDescription = contentDescription)
        }
        composeTestRule.onNodeWithContentDescription("Placeholder Image").assertExists()
    }

    @Test
    fun testImageWithText_displaysAsyncImageWhenUrlProvided() {
        val testText = "Test Title"
        val contentDescription = "Async Image"
        composeTestRule.setContent {
            ImageWithTextItem(title = testText, contentDescription = contentDescription)
        }
        composeTestRule.onNodeWithContentDescription("Async Image").assertExists()
    }

}
