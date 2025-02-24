package com.abedit.moqo_assessment.helperfunctions

import com.abedit.moqo_assessment.data.model.BoundingBox
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsTest {
    @Test
    fun `toLatLngBounds should convert BoundingBox to LatLngBounds`() {
        val boundingBox = BoundingBox(
            neLat = 1.0,
            neLng = 1.0,
            swLat = 0.0,
            swLng = 0.0
        )

        val latLngBounds = boundingBox.toLatLngBounds()
        val expectedSouthwest = LatLng(0.0, 0.0)
        val expectedNortheast = LatLng(1.0, 1.0)
        assertEquals(expectedSouthwest, latLngBounds.southwest)
        assertEquals(expectedNortheast, latLngBounds.northeast)
    }

    @Test
    fun `toLatLngBounds should handle negative coordinates`() {
        val boundingBox = BoundingBox(
            neLat = -1.0,
            neLng = -1.0,
            swLat = -2.0,
            swLng = -2.0
        )

        val latLngBounds = boundingBox.toLatLngBounds()
        val expectedSouthwest = LatLng(-2.0, -2.0)
        val expectedNortheast = LatLng(-1.0, -1.0)
        assertEquals(expectedSouthwest, latLngBounds.southwest)
        assertEquals(expectedNortheast, latLngBounds.northeast)
    }

    @Test
    fun `toLatLngBounds should handle zero coordinates`() {
        val boundingBox = BoundingBox(
            neLat = 0.0,
            neLng = 0.0,
            swLat = 0.0,
            swLng = 0.0
        )

        val latLngBounds = boundingBox.toLatLngBounds()
        val expectedSouthwest = LatLng(0.0, 0.0)
        val expectedNortheast = LatLng(0.0, 0.0)
        assertEquals(expectedSouthwest, latLngBounds.southwest)
        assertEquals(expectedNortheast, latLngBounds.northeast)
    }
}