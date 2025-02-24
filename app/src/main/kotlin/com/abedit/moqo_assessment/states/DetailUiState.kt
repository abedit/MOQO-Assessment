package com.abedit.moqo_assessment.states

import com.abedit.moqo_assessment.data.model.DetailedPointOfInterest

//States that are set in the viewmodel when loading the details of a POI
sealed class DetailUiState {
    data object Loading : DetailUiState()
    data class Error(val message: String) : DetailUiState()
    data class Success(val pointOfInterest: DetailedPointOfInterest) : DetailUiState()
}