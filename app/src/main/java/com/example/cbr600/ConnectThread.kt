package com.example.cbr600

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.UUID
import android.os.Handler

class ConnectThread(uuid: UUID, mmDevice: BluetoothDevice, private val mBluetoothAdapter: BluetoothAdapter, private val mHideHandler: Handler) : Thread() {

    private val mmSocket: BluetoothSocket?

    init {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        var tmp: BluetoothSocket? = null

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = mmDevice.createRfcommSocketToServiceRecord(uuid)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mmSocket = tmp
        Log.d("Debug", "ConnectThread iniciado")
    }

    override fun run() {
        // Cancel discovery because it will slow down the connection
        Log.d("Debug", "1")
        mBluetoothAdapter.cancelDiscovery()
        Log.d("Debug", "2")
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            Log.d("Debug", "3")
            mmSocket!!.connect()
            Log.d("Debug", "4")
        } catch (connectException: IOException) {
            connectException.printStackTrace()
            Log.d("Debug", "5")
            // Unable to connect; close the socket and get out
            try {
                mmSocket!!.close()
            } catch (closeException: IOException) {
            }

            return
        } catch (e: Exception){
            Log.d("Debug", "?")
        }

        // Do work to manage the connection (in a separate thread)
        if(mmSocket != null) {
            val connectedThread = ConnectedThread(mmSocket, mHideHandler)
            connectedThread.start()
        }
    }

    /** Will cancel an in-progress connection, and close the socket  */
    fun cancel() {
        try {
            mmSocket!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}