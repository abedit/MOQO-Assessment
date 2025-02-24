package com.abedit.moqo_assessment

import com.abedit.moqo_assessment.data.api.APIService
import com.abedit.moqo_assessment.data.model.BoundingBox
import com.abedit.moqo_assessment.data.model.DetailedPointOfInterest
import com.abedit.moqo_assessment.data.model.PointOfInterest

class PoiRepository(private val poiApi: APIService) {

    //call the api to get list of POIs
    suspend fun getPOIs(
        boundingBox: BoundingBox, page: Int
    ): List<PointOfInterest> {
        return poiApi.fetchPOIs(
            boundingBox = boundingBox, pageNumber = page
        )
    }

    //call the api to get details about a poi
    suspend fun getPoiDetails(id: String): DetailedPointOfInterest? = poiApi.fetchPoiDetails(id)
}

