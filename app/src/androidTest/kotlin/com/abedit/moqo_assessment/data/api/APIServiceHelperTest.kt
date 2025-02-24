package com.abedit.moqo_assessment.data.api


import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL

@RunWith(AndroidJUnit4::class)
class APIServiceHelperTest {

    @Test
    fun buildUrl_testBuildUrlReturnsCorrectUrl() {
        val params = mapOf(
            "filter[bounding_box]" to """{"ne_lat":1.0,"ne_lng":1.0,"sw_lat":0.0,"sw_lng":0.0}""",
            "page[size]" to "10",
            "page[number]" to "1"
        )

        val url: URL = APIServiceHelper.buildUrl(params)
        val expectedUrl =
            "https://prerelease.moqo.de/api/graph/discovery/pois?filter%5Bbounding_box%5D=%7B%22ne_lat%22%3A1.0%2C%22ne_lng%22%3A1.0%2C%22sw_lat%22%3A0.0%2C%22sw_lng%22%3A0.0%7D&page%5Bsize%5D=10&page%5Bnumber%5D=1"
        assertEquals(expectedUrl, url.toString())
    }

    @Test
    fun parsePoiListResponse_testParsePoiListResponseReturnsCorrectList() {
        val json = """
            {
                "data": [
                    {
                        "id": "1",
                        "lat": 0.5,
                        "lng": 0.5,
                        "name": "POI 1",
                        "vehicle_type": "Car"
                    }
                ]
            }
        """.trimIndent()

        val result = APIServiceHelper.parsePoiListResponse(json)
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals(0.5, result[0].lat, 0.0)
        assertEquals(0.5, result[0].lng, 0.0)
        assertEquals("POI 1", result[0].name)
        assertEquals("Car", result[0].vehicleType)
    }

    @Test
    fun parsePoiListResponse_testParsePoiListResponseHandlesNullVehicleType() {
        val json = """
            {
                "data": [
                    {
                        "id": "1",
                        "lat": 0.5,
                        "lng": 0.5,
                        "name": "POI 1",
                        "vehicle_type": "null"
                    }
                ]
            }
        """.trimIndent()

        val result = APIServiceHelper.parsePoiListResponse(json)
        assertEquals(1, result.size)
        assertEquals("POI 1", result[0].name)
        assertEquals(null, result[0].vehicleType)
    }

    @Test
    fun parsePoiDetailsResponse_testParsePoiDetailsResponseReturnsCorrectList() {
        val json = """
            {
                "data": [
                    {
                        "id": "1",
                        "lat": 0.5,
                        "lng": 0.5,
                        "name": "POI 1",
                        "vehicle_type": "Car",
                        "image": {
                            "url": "https://example.com/image.jpg",
                            "thumb_url": "https://example.com/image_thumb.jpg",
                            "medium_url": "https://example.com/image_medium.jpg"
                        },
                        "provider": {
                            "name": "Provider 1",
                            "image": {
                                "url": "https://example.com/provider.jpg",
                                "thumb_url": "https://example.com/provider_thumb.jpg",
                                "medium_url": "https://example.com/provider_medium.jpg"
                            }
                        }
                    }
                ]
            }
        """.trimIndent()

        val result = APIServiceHelper.parsePoiDetailsResponse(json)
        assertEquals(1, result.size)
        assertEquals("POI 1", result[0].name)
        assertEquals("Car", result[0].vehicleType)
        assertEquals("https://example.com/image.jpg", result[0].image?.url)
        assertEquals("Provider 1", result[0].provider?.name)
        assertEquals("https://example.com/provider.jpg", result[0].provider?.image?.url)
    }

    @Test
    fun parsePoiDetailsResponse_testParsePoiDetailsResponseHandlesNullFields() {
        val json = """
            {
                "data": [
                    {
                        "id": "1",
                        "lat": 0.5,
                        "lng": 0.5,
                        "name": "POI 1",
                        "vehicle_type": "null",
                        "image": null,
                        "provider": null
                    }
                ]
            }
        """.trimIndent()

        val result = APIServiceHelper.parsePoiDetailsResponse(json)
        assertEquals(1, result.size)
        assertEquals("POI 1", result[0].name)
        assertEquals(null, result[0].vehicleType)
        assertEquals(null, result[0].image)
        assertEquals(null, result[0].provider)
    }
}


