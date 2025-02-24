package com.abedit.moqo_assessment.data.api

import com.abedit.moqo_assessment.API_PAGE_SIZE
import com.abedit.moqo_assessment.data.model.BoundingBox
import com.abedit.moqo_assessment.data.model.DetailedPointOfInterest
import com.abedit.moqo_assessment.data.model.PointOfInterest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class APIService {

    // Fetch list of Points of Interest (10 at a time)
    suspend fun fetchPOIs(
        boundingBox: BoundingBox,
        pageNumber: Int = 1,
        pageSize: Int = API_PAGE_SIZE
    ): List<PointOfInterest> = withContext(Dispatchers.IO) {
        val url = APIServiceHelper.buildUrl(
            params = mapOf(
                "filter[bounding_box]" to """{"ne_lat":${boundingBox.neLat},"ne_lng":${boundingBox.neLng},"sw_lat":${boundingBox.swLat},"sw_lng":${boundingBox.swLng}}""",
                "page[size]" to pageSize.toString(),
                "page[number]" to pageNumber.toString()
            )
        )
        val response = performGetRequest(url)
        APIServiceHelper.parsePoiListResponse(response)
    }

    // For single POI details
    suspend fun fetchPoiDetails(id: String): DetailedPointOfInterest? =
        withContext(Dispatchers.IO) {
            val url = APIServiceHelper.buildUrl(
                params = mapOf(
                    "filter[id]" to id,
                    "extra_fields[pois]" to "image,provider"
                )
            )

            val response = performGetRequest(url)
            APIServiceHelper.parsePoiDetailsResponse(response).firstOrNull()
        }

    // Perform the GET request and return the response as a string
    @Throws(IOException::class)
    private fun performGetRequest(url: URL): String {
        val connection = url.openConnection() as HttpsURLConnection
        return try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            if (connection.responseCode == 200)
                connection.inputStream.bufferedReader().use { it.readText() }
            else
                throw IOException("HTTP error: ${connection.responseCode}")
        } finally {
            connection.disconnect()
        }
    }
}