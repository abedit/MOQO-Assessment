package com.abedit.moqo_assessment.ui.mapcomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abedit.moqo_assessment.R
import com.abedit.moqo_assessment.states.PoiListUiState

@Composable
fun LoadingBottomPill(poiListUiState: PoiListUiState, poisCount: Int) {
    // Show a "pill" on the bottom center to indicate loading or the number of POIs on screen

    val isLoading = poiListUiState is PoiListUiState.Loading
    val messageToShow = if (poiListUiState is PoiListUiState.Error)
        poiListUiState.message
    else
        pluralStringResource(
            id = R.plurals.number_of_points,
            count = poisCount,
            poisCount
        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Black)
                .padding(10.dp)
                .height(25.dp)
                .animateContentSize(), //make sure size change is animated
            contentAlignment = Alignment.Center
        ) {

            AnimatedVisibility(visible = !isLoading) {
                Text(
                    text = messageToShow,
                    color = Color.White,
                    modifier = Modifier.animateContentSize()
                )
            }


            //Show a progress indicator when loading loading
            AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp).testTag("loadingProgressIndicator"),
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview_Loading() {
    LoadingBottomPill(
        poiListUiState = PoiListUiState.Loading(emptyList()),
        poisCount = 10
    )
}

@Preview
@Composable
private fun DefaultPreview_NotLoading() {
    LoadingBottomPill(
        poiListUiState = PoiListUiState.Success(emptyList()),
        poisCount = 10
    )
}

@Preview
@Composable
private fun DefaultPreview_Error() {
    LoadingBottomPill(
        poiListUiState = PoiListUiState.Error(emptyList(), "error message"),
        poisCount = 10
    )
}