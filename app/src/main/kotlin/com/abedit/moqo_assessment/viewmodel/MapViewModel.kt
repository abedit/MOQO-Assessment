package com.abedit.moqo_assessment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abedit.moqo_assessment.API_PAGE_SIZE
import com.abedit.moqo_assessment.PoiRepository
import com.abedit.moqo_assessment.data.model.BoundingBox
import com.abedit.moqo_assessment.data.model.PointOfInterest
import com.abedit.moqo_assessment.GERMANY_BOUNDING_BOX
import com.abedit.moqo_assessment.data.model.DetailedPointOfInterest
import com.abedit.moqo_assessment.states.DetailUiState
import com.abedit.moqo_assessment.states.PoiListUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class MapViewModel(private val repo: PoiRepository) : ViewModel() {

    /********** Variables for the Map and list of POIs*/
    // Variable to store the list of POIs
    private val _pois = MutableStateFlow<List<PointOfInterest>>(emptyList())
    private val currentPois: List<PointOfInterest>
        get() = _pois.value

    // Variable to update the UI based on the API state
    private val _poiListUiState =
        MutableStateFlow<PoiListUiState>(PoiListUiState.Loading(currentPois))
    val poiListUiState: StateFlow<PoiListUiState> = _poiListUiState

    private var listFetchJob: Job? = null
    private var currentPage = 1
    private var currentBounds: BoundingBox? = null
    private val debounceTime = 300L
    // Cache for the list of POIs
    private val poiCache = mutableMapOf<String, List<PointOfInterest>>()


    /********** Variables for the detail screen*/
    //variable to update the detail UI based on the API state
    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState
    //save the details in memory
    private val poiDetailsCache = mutableMapOf<String, DetailedPointOfInterest>()

    //API job
    private var fetchJobDetail: Job? = null

    init {
        reloadForNewBounds(GERMANY_BOUNDING_BOX)
    }

    /*************** Functions for the Map and list of POIs *******/
    // Reload the list of POIs based on the new bounds
    private fun reloadForNewBounds(newBounds: BoundingBox) {
        currentBounds = newBounds
        currentPage = 1
        loadPOIs()
    }

    // Fetch the list of POIs from the API
    private fun loadPOIs() {
        val bounds = currentBounds ?: return

        // Cancel previous job if any
        listFetchJob?.cancel()
        _poiListUiState.value = PoiListUiState.Loading(currentPois)

        listFetchJob = viewModelScope.launch {
            try {
                val allPois = mutableListOf<PointOfInterest>()
                var moreResults = true

                while (moreResults) {
                    // Generate a cache key for the current bounds and page
                    val cacheKey = "${bounds.hashCode()} $currentPage"

                    // Check if POIs are already cached
                    val cachedPois = poiCache[cacheKey]
                    if (cachedPois != null) {
                        allPois.addAll(cachedPois)
                        currentPage++
                    } else {
                        // Fetch 10 POIs at a time
                        val result = repo.getPOIs(bounds, currentPage)
                        if (result.isNotEmpty()) {
                            allPois.addAll(result)
                            poiCache[cacheKey] = result // Cache the result
                            currentPage++
                            moreResults = result.size == API_PAGE_SIZE
                        } else {
                            moreResults = false
                        }
                    }
                }

                if (allPois.isNotEmpty()) {
                    // Update the StateFlow with the new POIs
                    _pois.value = allPois
                    _poiListUiState.value = PoiListUiState.Success(allPois)
                } else {
                    _poiListUiState.value =
                        PoiListUiState.Error(poiList = currentPois, message = "No POIs found")
                }
            } catch (e: CancellationException) {
                // Ignore cancellation exceptions
            } catch (e: Exception) {
                _poiListUiState.value =
                    PoiListUiState.Error(poiList = currentPois, message = "Error loading POIs")
            }
        }
    }

    // Debounced function to handle map interactions
    fun onMapBoundsChanged(newBounds: BoundingBox) {
        viewModelScope.launch {
            delay(debounceTime) // Wait a bit for map interactions before loading
            reloadForNewBounds(newBounds)
        }
    }


    /*************** Functions for the detail screen *******/
    fun loadDetails(poiID: String) {
        //cancel previous job if any
        fetchJobDetail?.cancel()

        //inform the ui that we are loading
        _detailUiState.value = DetailUiState.Loading

        //fetch details from API
        fetchJobDetail = viewModelScope.launch {
            try {
                //Check if it exists in the cache
                if (poiDetailsCache.containsKey(poiID) && poiDetailsCache[poiID] != null) {
                    poiDetailsCache[poiID]?.let { mDetailedPoi ->
                        _detailUiState.value = DetailUiState.Success(mDetailedPoi)
                    }
                } else {
                    //get the poiDetails
                    val result = repo.getPoiDetails(poiID)
                    _detailUiState.value = result?.let { mResult ->
                        poiDetailsCache[poiID] = mResult
                        DetailUiState.Success(mResult)
                    } ?: DetailUiState.Error("Failed to load details")
                }

            } catch (e: Exception) {
                //update the ui state on error
                _detailUiState.value = DetailUiState.Error("Failed to load details")
            }
        }
    }
}