package com.abedit.moqo_assessment.data.api

import android.net.Uri
import com.abedit.moqo_assessment.API_BASE_URL
import com.abedit.moqo_assessment.data.model.DetailedPointOfInterest
import com.abedit.moqo_assessment.data.model.PointOfInterest
import org.json.JSONObject
import java.net.URI
import java.net.URL
import java.net.URLEncoder

object APIServiceHelper {

    //Build the URL from the base url and the parameters (depending on the API call
    fun buildUrl(params: Map<String, String>): URL {
        val uri = Uri.parse(API_BASE_URL).buildUpon()
        params.forEach { (key, value) ->
            uri.appendQueryParameter(key, value)
        }
        return URL(uri.build().toString())
    }




    //Parse the response from the API for list of POIs
    fun parsePoiListResponse(json: String): List<PointOfInterest> {
        val jsonObject = JSONObject(json)
        val dataArray = jsonObject.getJSONArray("data")

        return (0 until dataArray.length()).map { index ->
            val item = dataArray.getJSONObject(index)
            val vehicleType = item.optString("vehicle_type")
            PointOfInterest(
                id = item.getString("id"),
                lat = item.getDouble("lat"),
                lng = item.getDouble("lng"),
                name = item.getString("name"),
                vehicleType = if (vehicleType == "null") null else vehicleType
            )
        }
    }



    //Parse the response from the API for more details on a POI
    fun parsePoiDetailsResponse(json: String): List<DetailedPointOfInterest> {
        val jsonObject = JSONObject(json)
        val dataArray = jsonObject.getJSONArray("data")

        return (0 until dataArray.length()).map { index ->
            val item = dataArray.getJSONObject(index)
            val vehicleType = item.optString("vehicle_type")
            DetailedPointOfInterest(
                id = item.getString("id"),
                lat = item.getDouble("lat"),
                lng = item.getDouble("lng"),
                name = item.getString("name"),
                vehicleType = if (vehicleType == "null") null else vehicleType,
                image = parseImageData(item.optJSONObject("image")),
                provider = parseProviderData(item.optJSONObject("provider"))
            )
        }
    }



    //Parse image data object in the json response for the DetailedPointOfInterest
    private fun parseImageData(json: JSONObject?): DetailedPointOfInterest.ImageData? {
        return json?.let {
            DetailedPointOfInterest.ImageData(
                url = it.optString("url"),
                thumbUrl = it.optString("thumb_url"),
                mediumUrl = it.optString("medium_url")
            )
        }
    }



    //Parse the provider data object in the json response for the DetailedPointOfInterest
    private fun parseProviderData(json: JSONObject?): DetailedPointOfInterest.ProviderData? {
        return json?.let {
            DetailedPointOfInterest.ProviderData(
                name = it.getString("name"),
                image = parseImageData(it.optJSONObject("image"))
            )
        }
    }
}