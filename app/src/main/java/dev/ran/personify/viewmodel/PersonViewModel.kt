package dev.ran.personify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.ran.personify.model.Results
import dev.ran.personify.repository.PersonServices
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class PersonViewModel : ViewModel() {
    private val personServices = PersonServices()

    private val personData = MutableLiveData<Results>()
    val livePerson: LiveData<Results> get() = personData
    val errorPerson = MutableLiveData<Boolean>()

    fun refreshPerson() {
        getPersonData()
    }

    private fun getPersonData() {
        personServices.getPersonData().enqueue(object : Callback<Results> {
            override fun onResponse(call: Call<Results>, response: Response<Results>) {
                if(response.isSuccessful){
                    val personLists : Results = response.body()!!
                    personData.value = personLists
                }else{
                    errorPerson.value = false
                }
            }
            override fun onFailure(call: Call<Results>, t: Throwable) {
                errorPerson.value = true
            }
        })
    }
}