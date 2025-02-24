package com.abedit.moqo_assessment.helperfunctions

import com.abedit.moqo_assessment.data.model.BoundingBox
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

fun BoundingBox.toLatLngBounds() = LatLngBounds(
    LatLng(swLat, swLng),
    LatLng(neLat, neLng)
)
