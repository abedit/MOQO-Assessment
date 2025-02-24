package com.abedit.moqo_assessment.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abedit.moqo_assessment.data.model.BoundingBox
import com.abedit.moqo_assessment.data.model.PointOfInterest
import com.abedit.moqo_assessment.states.DetailUiState
import com.abedit.moqo_assessment.states.PoiListUiState
import com.abedit.moqo_assessment.testPois
import com.abedit.moqo_assessment.ui.mapcomponents.MapContent
import com.abedit.moqo_assessment.ui.theme.MOQOAssessmentTheme
import com.abedit.moqo_assessment.viewmodel.MapViewModel
import com.abedit.moqo_assessment.viewmodel.ViewModelFactoryProvider

@Composable
fun POIMapViewUI(
    viewModel: MapViewModel,
) {
    val poiListUiState by viewModel.poiListUiState.collectAsState()
    val detailUiState by viewModel.detailUiState.collectAsState()


    POIMapViewUI(
        poiListUiState = poiListUiState,
        detailUiState = detailUiState,
        onMapBoundsChanged = { newBounds ->
            viewModel.onMapBoundsChanged(newBounds)
        },
        onLoadDetails = { poiID ->
            viewModel.loadDetails(poiID)
        }
    )
}

@Composable
private fun POIMapViewUI(
    poiListUiState: PoiListUiState,
    detailUiState: DetailUiState,
    onMapBoundsChanged: (BoundingBox) -> Unit,
    onLoadDetails: (String) -> Unit
) {

    MOQOAssessmentTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MapScreen(
                poiListUiState = poiListUiState,
                detailUiState = detailUiState,
                onMapBoundsChanged = onMapBoundsChanged,
                onLoadDetails = onLoadDetails
            )
        }
    }
}


@Composable
fun MapScreen(
    poiListUiState: PoiListUiState,
    detailUiState: DetailUiState,
    onMapBoundsChanged: (BoundingBox) -> Unit,
    onLoadDetails: (String) -> Unit
) {
    val pois = poiListUiState.poiList
    var selectedPois by remember { mutableStateOf<List<PointOfInterest>>(emptyList()) }
    var clickedPoiID by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { true }
    )

    LaunchedEffect(clickedPoiID) {
        if (clickedPoiID.isNotEmpty()) {
            onLoadDetails.invoke(clickedPoiID) //load the details for the POI by ID
            sheetState.show()
        }
        else
            sheetState.hide()
    }

    LaunchedEffect(sheetState) {
        snapshotFlow { sheetState.currentValue }
            .collect { state ->
                if (state == ModalBottomSheetValue.Hidden) {
                    clickedPoiID = ""
                }
            }
    }


    // Bottom sheet for the details screen
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            // Bottom sheet content (POI details)
            if (clickedPoiID.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                ) {
                    DetailedPoiScreenUI(
                        detailUiState = detailUiState
                    )
                }
            }
        }
    ) {
        MapContent(
            pois = pois,
            poiListUiState = poiListUiState,
            selectedPois = selectedPois,
            updateSelectedPois = { newSelectedPois ->
                selectedPois = newSelectedPois
            },
            updateClickedPoiID = { newClickedPoiID ->
                clickedPoiID = newClickedPoiID
            },
            onMapBoundsChanged = onMapBoundsChanged
        )

    }
}





@Preview
@Composable
private fun DefaultPreview_Success() {
    POIMapViewUI(
        poiListUiState = PoiListUiState.Success(testPois),
        detailUiState = DetailUiState.Loading,
        onMapBoundsChanged = {},
        onLoadDetails = {}
    )
}

@Preview
@Composable
private fun DefaultPreview_Loading() {
    POIMapViewUI(
        poiListUiState = PoiListUiState.Loading(emptyList()),
        detailUiState = DetailUiState.Loading,
        onMapBoundsChanged = {},
        onLoadDetails = {}
    )
}