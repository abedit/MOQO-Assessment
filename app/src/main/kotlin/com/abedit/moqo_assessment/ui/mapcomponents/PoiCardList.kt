package com.abedit.moqo_assessment.ui.mapcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.abedit.moqo_assessment.data.model.PointOfInterest
import com.abedit.moqo_assessment.testPois

@Composable
fun PoiCardList(
    modifier: Modifier,
    selectedPois: List<PointOfInterest>,
    onPoiClick: (String) -> Unit
) {
    val lazyListState = rememberLazyListState()

    // Reset list index when selectedPois changes
    LaunchedEffect(selectedPois) {
        if (selectedPois.isNotEmpty()) {
            lazyListState.scrollToItem(0)
        }
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(1f)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        state = lazyListState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
        contentPadding = PaddingValues(
            horizontal = 0.dp,
            vertical = 10.dp
        )
    ) {
        items(selectedPois) { poi ->
            PoiCard(
                poi = poi,
                modifier = Modifier
                    // Don't let it take the full width
                    // to make sure the next card is slightly visible
                    // and make the user aware that there's more than 1 card
                    .fillParentMaxWidth(if (selectedPois.size > 1) 0.9f else 1f)
                    .padding(8.dp),
                onCardClick = { poiID ->
                    onPoiClick.invoke(poiID)
                }
            )
        }
    }
}


@Preview
@Composable
private fun DefaultPreview_SinglePOI() {
    PoiCardList(
        modifier = Modifier,
        selectedPois = listOf(testPois.first()),
        onPoiClick = {}
    )
}

@Preview
@Composable
private fun DefaultPreview_MultiplePOIs() {
    PoiCardList(
        modifier = Modifier,
        selectedPois = testPois,
        onPoiClick = {}
    )
}