package com.abedit.moqo_assessment.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abedit.moqo_assessment.PoiRepository
import com.abedit.moqo_assessment.viewmodel.MapViewModel

class MapViewModelFactory(
    private val repo: PoiRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}