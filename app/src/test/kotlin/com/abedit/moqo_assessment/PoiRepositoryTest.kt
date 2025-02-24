package com.abedit.moqo_assessment

import com.abedit.moqo_assessment.data.api.APIService
import com.abedit.moqo_assessment.data.model.BoundingBox
import com.abedit.moqo_assessment.data.model.DetailedPointOfInterest
import com.abedit.moqo_assessment.data.model.PointOfInterest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class PoiRepositoryTest {

    private lateinit var mockApiService: APIService
    private lateinit var poiRepository: PoiRepository

    @Before
    fun setUp() {
        mockApiService = mock(APIService::class.java)
        poiRepository = PoiRepository(mockApiService)
    }

    @Test
    fun `getPOIs should return list of POIs from API`(): Unit = runBlocking {
        val boundingBox = BoundingBox(neLat = 1.0, neLng = 1.0, swLat = 0.0, swLng = 0.0)
        val page = 1
        val expectedPois = listOf(
            PointOfInterest(id = "1", name = "POI 1", lat = 0.5, lng = 0.5, vehicleType = "car"),
            PointOfInterest(id = "2", name = "POI 2", lat = 0.6, lng = 0.6, vehicleType = "car")
        )

        whenever(mockApiService.fetchPOIs(boundingBox, page)).thenReturn(expectedPois)
        val result = poiRepository.getPOIs(boundingBox, page)

        assertEquals(expectedPois, result)
        verify(mockApiService).fetchPOIs(boundingBox, page)
    }

    @Test
    fun `getPoiDetails should return POI details from API`(): Unit = runBlocking {
        val poiId = "1"
        val expectedPoiDetails = DetailedPointOfInterest(
            id = "1",
            name = "POI 1",
            lat = 0.5,
            lng = 0.5,
            vehicleType = "car",
            image = null,
            provider = null
        )

        whenever(mockApiService.fetchPoiDetails(poiId)).thenReturn(expectedPoiDetails)
        val result = poiRepository.getPoiDetails(poiId)

        assertEquals(expectedPoiDetails, result)
        verify(mockApiService).fetchPoiDetails(poiId)
    }

    @Test
    fun `getPoiDetails should return null if API returns null`(): Unit = runBlocking {
        val poiId = "1"

        whenever(mockApiService.fetchPoiDetails(poiId)).thenReturn(null)
        val result = poiRepository.getPoiDetails(poiId)

        assertEquals(null, result)
        verify(mockApiService).fetchPoiDetails(poiId)
    }
}