package com.abedit.moqo_assessment.states

import com.abedit.moqo_assessment.data.model.PointOfInterest

//States that are set in the viewmodel when loading the list of POI
sealed class PoiListUiState {
    //poi list here so that it can be accessed regardless of the state
    abstract val poiList: List<PointOfInterest>

    data class Loading(override val poiList: List<PointOfInterest>) : PoiListUiState()
    data class Error(override val poiList: List<PointOfInterest>, val message: String) : PoiListUiState()
    data class Success(override val poiList: List<PointOfInterest>) : PoiListUiState()
}