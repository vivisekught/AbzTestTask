package com.abz.agency.testtask.core

sealed class ConnectionState {
    data object Unknown: ConnectionState()
    data object Connected: ConnectionState()
    data object Disconnected: ConnectionState()
}