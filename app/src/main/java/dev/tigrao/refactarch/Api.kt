package dev.tigrao.refactarch

import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("breed/{race}/images")
    suspend fun fetchByBreed(@Path("race") race: String): BreedResponse
}