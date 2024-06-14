package com.dicoding.capstone.cocodiag.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.dicoding.capstone.cocodiag.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ConnectivityRepository(context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnected = MutableStateFlow(false)
    val isConnected: Flow<Boolean> = _isConnected

    init {
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                _isConnected.value = true
            }

            override fun onLost(network: android.net.Network) {
                _isConnected.value = false
            }
        })
    }

    companion object {
        @Volatile
        private var instance: ConnectivityRepository? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: ConnectivityRepository(context)
            }.also { instance = it }
    }
}