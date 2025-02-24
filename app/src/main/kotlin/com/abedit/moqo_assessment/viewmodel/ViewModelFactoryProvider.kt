package com.abedit.moqo_assessment.viewmodel

import com.abedit.moqo_assessment.PoiRepository
import com.abedit.moqo_assessment.data.api.APIService
import com.abedit.moqo_assessment.viewmodel.factories.MapViewModelFactory

//To create an instance of a view model, the view model factory is needed
// and the latter needs a repository.
//This object is to make creating view model instance in the activity more elegant
object ViewModelFactoryProvider {
    private val poiApi by lazy { APIService() }
    val poiRepository by lazy { PoiRepository(poiApi) }

    //returns the factory for the map view model
    fun mapViewModelFactory(): MapViewModelFactory {
        return MapViewModelFactory(poiRepository)
    }
}

