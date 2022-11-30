package dev.tigrao.refactarch

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedResponse(
    val message: List<String>
)