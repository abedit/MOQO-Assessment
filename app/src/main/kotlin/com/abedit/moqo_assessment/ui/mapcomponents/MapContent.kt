package com.abedit.moqo_assessment.ui.mapcomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abedit.moqo_assessment.GERMANY_BOUNDING_BOX
import com.abedit.moqo_assessment.R
import com.abedit.moqo_assessment.data.model.BoundingBox
import com.abedit.moqo_assessment.data.model.PointOfInterest
import com.abedit.moqo_assessment.helperfunctions.toLatLngBounds
import com.abedit.moqo_assessment.states.PoiListUiState
import com.abedit.moqo_assessment.testPois
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun MapContent(
    pois: List<PointOfInterest>,
    poiListUiState: PoiListUiState,
    selectedPois: List<PointOfInterest>,
    updateSelectedPois: (List<PointOfInterest>) -> Unit,
    updateClickedPoiID: (String) -> Unit,
    onMapBoundsChanged: (BoundingBox) -> Unit
) {

    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val cameraPositionState = rememberCameraPositionState()
    // group POI by latitude and longitude
    val poiGroups = pois.groupBy { "${it.lat},${it.lng}" }

    // Track selected marker
    var selectedMarkerId by remember { mutableStateOf<String?>(null) }

    // Move camera to Germany when map opens
    LaunchedEffect(Unit) {
        cameraPositionState.move(
            CameraUpdateFactory.newLatLngBounds(GERMANY_BOUNDING_BOX.toLatLngBounds(), 50)
        )
    }

    // Detect map movement and reload if bounds change
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position }
            .debounce(500)
            .collectLatest {
                val bounds = cameraPositionState.projection?.visibleRegion?.latLngBounds
                if (bounds != null) {
                    val boundingBox = BoundingBox(
                        neLat = bounds.northeast.latitude,
                        neLng = bounds.northeast.longitude,
                        swLat = bounds.southwest.latitude,
                        swLng = bounds.southwest.longitude
                    )
                    onMapBoundsChanged.invoke(boundingBox)
                }
            }
    }

    // Clear selected POIs and Marker when POI list is loading
    LaunchedEffect(poiListUiState is PoiListUiState.Loading) {
        updateSelectedPois.invoke(emptyList())
        selectedMarkerId = ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = navigationBarHeight)
    ) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
            properties = MapProperties(mapType = MapType.NORMAL),
            onMapClick = {
                // Hide POI cards when clicking on an empty area of the map
                updateSelectedPois.invoke(emptyList())
                updateClickedPoiID.invoke("")
                selectedMarkerId = null
            }
        ) {
            // Render POIs as markers
            poiGroups.forEach { (_, pois) ->
                val firstPoi = pois.first()

                val isSelected = selectedMarkerId == firstPoi.id
                MapMarker(
                    latLng = LatLng(firstPoi.lat, firstPoi.lng),
                    name = firstPoi.name,
                    vehicleType = firstPoi.vehicleType ?: "",
                    badgeCount = pois.size,
                    markerIconResourceId = if (isSelected) R.drawable.marker_background_selected else R.drawable.marker_background,
                    onMarkerClicked = {
                        updateSelectedPois.invoke(pois)
                        selectedMarkerId = firstPoi.id
                        updateClickedPoiID.invoke("")
                        true
                    },
                    zIndex = if (isSelected) 10f else 0f,
                    poiID = firstPoi.id
                )
            }
        }

        // Show cards at the bottom of the screen if a marker is selected
        if (selectedPois.isNotEmpty()) {
            PoiCardList(
                modifier = Modifier.align(Alignment.BottomCenter),
                selectedPois = selectedPois,
                onPoiClick = { poiID ->
                    updateClickedPoiID.invoke(poiID)
                }
            )
        }


        LoadingBottomPill(
            poiListUiState = poiListUiState,
            poisCount = pois.size
        )
    }
}

@Preview
@Composable
private fun DefaultPreview_Success() {
    MapContent(
        pois = testPois,
        poiListUiState = PoiListUiState.Success(testPois),
        selectedPois = emptyList(),
        updateSelectedPois = {},
        updateClickedPoiID = {},
        onMapBoundsChanged = {}
    )
}

@Preview
@Composable
private fun DefaultPreview_Loading() {
    MapContent(
        pois = testPois,
        poiListUiState = PoiListUiState.Loading(emptyList()),
        selectedPois = emptyList(),
        updateSelectedPois = {},
        updateClickedPoiID = {},
        onMapBoundsChanged = {}
    )
}