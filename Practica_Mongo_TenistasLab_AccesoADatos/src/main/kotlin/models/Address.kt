package models

import kotlinx.serialization.Serializable

/**
 *
 * @property city
 * @property geo
 * @property street
 * @property suite
 * @property zipcode
 */

@Serializable
data class Address(
    val city: String,
    val geo: Geo,
    val street: String,
    val suite: String,
    val zipcode: String
)