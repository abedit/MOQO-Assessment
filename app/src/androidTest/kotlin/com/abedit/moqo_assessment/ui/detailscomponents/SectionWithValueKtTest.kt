package com.abedit.moqo_assessment.ui.detailscomponents

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abedit.moqo_assessment.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class SectionWithValueTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSectionWithValue_displaysTitleCorrectly() {
        val title = "Section Title"
        composeTestRule.setContent {
            SectionWithValue(title = title, data = "Sample Data")
        }
        composeTestRule.onNodeWithText(title).assertExists().assertIsDisplayed()
    }

    @Test
    fun testSectionWithValue_displaysDataCorrectly() {
        val title = "Category"
        val data = "example text"
        composeTestRule.setContent {
            SectionWithValue(title = title, data = data)
        }
        composeTestRule.onNodeWithText(data.replaceFirstChar { it.uppercase(Locale.getDefault()) })
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testSectionWithValue_hidesDataWhenNull() {
        val title = "Category"
        composeTestRule.setContent {
            SectionWithValue(title = title, data = null)
        }
        composeTestRule.onNodeWithText(title).assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("null").assertDoesNotExist()
    }

    @Test
    fun testSectionWithValue_hidesDataWhenEmpty() {
        val title = "Category"
        composeTestRule.setContent {
            SectionWithValue(title = title, data = "")
        }
        composeTestRule.onNodeWithText(title).assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("").assertDoesNotExist()
    }
}
