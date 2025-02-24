package com.abedit.moqo_assessment.viewmodel

import com.abedit.moqo_assessment.PoiRepository
import com.abedit.moqo_assessment.data.model.BoundingBox
import com.abedit.moqo_assessment.data.model.DetailedPointOfInterest
import com.abedit.moqo_assessment.data.model.PointOfInterest
import com.abedit.moqo_assessment.states.DetailUiState
import com.abedit.moqo_assessment.states.PoiListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MapViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    private lateinit var mockRepo: PoiRepository
    private lateinit var viewModel: MapViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        mockRepo = mock(PoiRepository::class.java)
        viewModel = MapViewModel(mockRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        assertEquals(PoiListUiState.Loading(emptyList()), viewModel.poiListUiState.value)
    }


    @Test
    fun `loadPOIs should update poiListUiState with Success when POIs are fetched`() = testScope.runTest {
        val boundingBox = BoundingBox(neLat = 1.0, neLng = 1.0, swLat = 0.0, swLng = 0.0)
        val pois = listOf(
            PointOfInterest(id = "1", name = "POI 1", lat = 0.5, lng = 0.5, vehicleType = "Car"),
            PointOfInterest(id = "2", name = "POI 2", lat = 0.6, lng = 0.6, vehicleType = "Bike")
        )

        whenever(mockRepo.getPOIs(boundingBox, 1)).thenReturn(pois)
        viewModel.onMapBoundsChanged(boundingBox)
        advanceUntilIdle()
        val uiState = viewModel.poiListUiState.value
        assertEquals(PoiListUiState.Success(pois), uiState)
    }

    @Test
    fun `loadPOIs should update poiListUiState with Error when repository throws an exception`() = testScope.runTest {
        val boundingBox = BoundingBox(neLat = 1.0, neLng = 1.0, swLat = 0.0, swLng = 0.0)

        whenever(mockRepo.getPOIs(boundingBox, 1)).thenThrow(RuntimeException("API error"))
        viewModel.onMapBoundsChanged(boundingBox)

        advanceUntilIdle()
        val uiState = viewModel.poiListUiState.first()
        assert(uiState is PoiListUiState.Error)
        assertEquals("Error loading POIs", (uiState as PoiListUiState.Error).message)
    }

    @Test
    fun `onMapBoundsChanged should reload POIs with new bounds`() = testScope.runTest {
        val newBounds = BoundingBox(neLat = 2.0, neLng = 2.0, swLat = 1.0, swLng = 1.0)
        val pois = listOf(
            PointOfInterest(id = "1", name = "POI 1", lat = 1.5, lng = 1.5, vehicleType = "Car")
        )

        whenever(mockRepo.getPOIs(newBounds, 1)).thenReturn(pois)
        viewModel.onMapBoundsChanged(newBounds)
        advanceUntilIdle()
        val uiState = viewModel.poiListUiState.first()
        assertEquals(PoiListUiState.Success(pois), uiState)
    }

    @Test
    fun `loadDetails should update detailUiState with Success when details are fetched`() = testScope.runTest {
        val poiId = "1"
        val detailedPoi = DetailedPointOfInterest(
            id = "1",
            name = "POI 1",
            lat = 0.5,
            lng = 0.5,
            vehicleType = "Car",
            image = null,
            provider = null
        )

        whenever(mockRepo.getPoiDetails(poiId)).thenReturn(detailedPoi)
        viewModel.loadDetails(poiId)
        advanceUntilIdle()
        val uiState = viewModel.detailUiState.first()
        assertEquals(DetailUiState.Success(detailedPoi), uiState)
    }

    @Test
    fun `loadDetails should update detailUiState with Error when repository throws an exception`() = testScope.runTest {
        val poiId = "1"
        whenever(mockRepo.getPoiDetails(poiId)).thenThrow(RuntimeException("API error"))
        viewModel.loadDetails(poiId)
        advanceUntilIdle()
        val uiState = viewModel.detailUiState.first()
        assertEquals(DetailUiState.Error("Failed to load details"), uiState)
    }

    @Test
    fun `loadDetails should use cached details if available`() = testScope.runTest {
        val poiId = "1"
        val cachedPoi = DetailedPointOfInterest(
            id = "1",
            name = "POI 1",
            lat = 0.5,
            lng = 0.5,
            vehicleType = "Car",
            image = null,
            provider = null
        )

        whenever(mockRepo.getPoiDetails(poiId)).thenReturn(cachedPoi)
        viewModel.loadDetails(poiId)
        advanceUntilIdle()
        viewModel.loadDetails(poiId)
        advanceUntilIdle()
        val uiState = viewModel.detailUiState.first()
        assertEquals(DetailUiState.Success(cachedPoi), uiState)
    }
}