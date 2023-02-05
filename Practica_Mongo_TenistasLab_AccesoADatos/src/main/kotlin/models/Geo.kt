package models

import kotlinx.serialization.Serializable

/**
 *
 *
 * @property lat
 * @property lng
 */

@Serializable
data class Geo(
    val lat: String,
    val lng: String
)