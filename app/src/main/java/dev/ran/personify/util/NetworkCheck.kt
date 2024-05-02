package dev.ran.personify.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData

class NetworkCheck (private val context: Context) {

    companion object {
        val networkState = MutableLiveData<Boolean>()
    }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    networkState.postValue(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    networkState.postValue(false)
                }
            })
    }

    fun checkNetwork() {
        val network = connectivityManager.activeNetworkInfo
        val isConnected = network != null && network.isConnected
        networkState.postValue(isConnected)
    }
}