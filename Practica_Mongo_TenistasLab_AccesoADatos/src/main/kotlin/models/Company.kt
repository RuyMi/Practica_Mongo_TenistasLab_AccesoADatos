package models

import kotlinx.serialization.Serializable

/**
 *
 *
 * @property bs
 * @property catchPhrase
 * @property name
 */

@Serializable
data class Company(
    val bs: String,
    val catchPhrase: String,
    val name: String
)