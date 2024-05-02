package dev.ran.personify.repository

import dev.ran.personify.model.Results
import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("/api")
    fun getPerson(): Call<Results>
}
