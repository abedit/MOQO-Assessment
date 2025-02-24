package com.abedit.moqo_assessment.data.model

interface POI {
    val id: String
    val lat: Double
    val lng: Double
    val name: String
    val vehicleType: String?
}

data class PointOfInterest( //for the list of points on the map
    override val id: String,
    override val lat: Double,
    override val lng: Double,
    override val name: String,
    override val vehicleType: String?
) : POI


data class DetailedPointOfInterest( // for the details screen
    override val id: String,
    override val lat: Double,
    override val lng: Double,
    override val name: String,
    override val vehicleType: String?,
    val image: ImageData?,
    val provider: ProviderData?
) : POI {

    data class ImageData(
        val url: String?,
        val thumbUrl: String?,
        val mediumUrl: String?
    )

    data class ProviderData(
        val name: String,
        val image: ImageData?
    )
}