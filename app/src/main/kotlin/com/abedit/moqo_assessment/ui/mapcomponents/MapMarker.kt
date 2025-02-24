package com.abedit.moqo_assessment.ui.mapcomponents

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.abedit.moqo_assessment.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapMarker(
    latLng: LatLng,
    poiID: String,
    name: String,
    vehicleType: String,
    badgeCount: Int,
    markerIconResourceId: Int?,
    onMarkerClicked: (Marker) -> Boolean,
    zIndex: Float = 0.0f,
) {
    val customIcon = markerIconResourceId?.let { resId ->
        bitmapDescriptor(
            context = LocalContext.current,
            vectorResId = resId,
            badgeCount = badgeCount
        )
    }

    Marker(
        zIndex = zIndex,
        state = MarkerState(position = latLng),
        title = name,
        snippet = vehicleType,
        icon = customIcon,
        onClick = onMarkerClicked,
        tag = poiID
    )

}

//Custom marker icon with a badge count drawn dynamically using a canvas
fun bitmapDescriptor(
    context: Context,
    vectorResId: Int,
    badgeCount: Int
): BitmapDescriptor? {

    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

    // create a bitmap
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    drawable.draw(canvas)

    //Draw a badge count on the top right of the marker
    if (badgeCount > 1) {
        val circlePaint = Paint().apply {
            color = android.graphics.Color.BLUE
            isAntiAlias = true
        }

        // Draw the circle background for the count
        val radius = 20f
        val badgeX = drawable.intrinsicWidth - radius
        val badgeY = radius
        canvas.drawCircle(badgeX, badgeY, radius, circlePaint)

        val textPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 30f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        // Draw the count text on top of the circle
        canvas.drawText(
            badgeCount.toString(),
            badgeX,
            badgeY + (textPaint.textSize) - 20,
            textPaint
        )
    }


    return BitmapDescriptorFactory.fromBitmap(bitmap)
}


@Preview
@Composable
private fun DefaultPreview() {
    MapMarker(
        latLng = LatLng(0.0, 0.0),
        "1","Audi", "car",
        badgeCount = 3,
        markerIconResourceId = R.drawable.marker_background,
        onMarkerClicked = { true }
    )
}