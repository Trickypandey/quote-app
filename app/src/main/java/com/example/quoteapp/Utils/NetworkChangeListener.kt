package com.example.quoteapp.Utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

class NetworkChangeListener(private val context: Context)  {

        private var isRegistered = false
        var connected=false
        private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                connected = isNetworkAvailable()
            }
        }

        fun start() {

            if (!isRegistered) {

                val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                context.registerReceiver(networkChangeReceiver, intentFilter)
                isRegistered = true

            }
        }

        fun stop() {
            if (isRegistered) {

                context.unregisterReceiver(networkChangeReceiver)
                isRegistered = false

            }
        }

        private fun isNetworkAvailable(): Boolean {

            val network = connectivityManager.activeNetwork

            if (network != null) {

                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

            }

            return false
        }


}