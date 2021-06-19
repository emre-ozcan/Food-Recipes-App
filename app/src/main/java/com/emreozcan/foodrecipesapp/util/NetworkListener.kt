package com.emreozcan.foodrecipesapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkListener: ConnectivityManager.NetworkCallback() {

    private val isNetworkAvaible = MutableStateFlow(false)

    fun checkNetwork(context: Context): MutableStateFlow<Boolean>{

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected = false

        connectivityManager.allNetworks.forEach { network ->

            val networkCapability = connectivityManager.getNetworkCapabilities(network)

            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
                    isConnected = true
                    return@forEach
                }
            }
        }

        isNetworkAvaible.value = isConnected

        return isNetworkAvaible

    }


    override fun onAvailable(network: Network) {
        isNetworkAvaible.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvaible.value = false
    }
}