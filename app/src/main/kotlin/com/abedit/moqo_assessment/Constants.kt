package com.abedit.moqo_assessment

import com.abedit.moqo_assessment.data.model.BoundingBox
import com.abedit.moqo_assessment.data.model.PointOfInterest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

const val API_BASE_URL = "https://prerelease.moqo.de/api/graph/discovery/pois"

val GERMANY_BOUNDING_BOX = BoundingBox(
    neLat = 55.058347,
    neLng = 15.041896,
    swLat = 47.270111,
    swLng = 5.866315
)

const val API_PAGE_SIZE = 10


 val testPois = listOf(
    PointOfInterest("1", 52.520008, 13.404954, "Berlin", "Car"),
    PointOfInterest("2", 48.135124, 11.581981, "Munich", "Bike"),
    PointOfInterest("3", 53.551086, 9.993682, "Hamburg", "Scooter"),
)