package com.abz.agency.testtask.data.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.abz.agency.testtask.core.ConnectionState
import com.abz.agency.testtask.domain.NetworkConnectivityRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NetworkConnectivityRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkConnectivityRepository {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val networkStatus: Flow<ConnectionState> = callbackFlow {
        // Define a callback to handle network connectivity changes
        val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            // Called when a network becomes available
            override fun onAvailable(network: Network) {
                // Emit a "Connected" state when network is available
                trySend(ConnectionState.Connected)
            }

            // Called when no network is available
            override fun onUnavailable() {
                // Emit a "Disconnected" state when no network is available
                trySend(ConnectionState.Disconnected)
            }

            // Called when the network is lost
            override fun onLost(network: Network) {
                // Emit a "Disconnected" state when network is lost
                trySend(ConnectionState.Disconnected)
            }
        }

        // Build a NetworkRequest to listen for changes in network capabilities
        val request = NetworkRequest.Builder()
            // Add capability to check for internet access
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            // Add transport types to check for Wi-Fi and cellular networks
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        // Register the network callback with the ConnectivityManager
        connectivityManager.registerNetworkCallback(request, connectivityCallback)

        // Handle cleanup when the flow is closed
        awaitClose {
            // Unregister the network callback to prevent memory leaks
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
        }
    }.distinctUntilChanged() // Ensure that consecutive identical states are not emitted
        .flowOn(Dispatchers.IO)


}