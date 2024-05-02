package dev.ran.personify.repository

import dev.ran.personify.model.Results
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonServices {
    private val baseUrl = "https://randomuser.me"
    private val personApi: API

    init {
        personApi = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
    }

    fun getPersonData(): Call<Results> {
        return personApi.getPerson()
    }
}