package com.example.cbr600

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.UUID
import android.os.Handler
import java.lang.Exception
import android.content.ContentValues.TAG



class ConnectThread(uuid: UUID, device: BluetoothDevice, private val bluetoothAdapter: BluetoothAdapter, private val mHideHandler: Handler) : Thread() {

    private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        device.createRfcommSocketToServiceRecord(uuid)
    }

    override fun run() {
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter?.cancelDiscovery()

        /*try {
            mmSocket?.use { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                //manageMyConnectedSocket(socket)
                if (mmSocket != null) {
                    val connectedThread = ConnectedThread(mmSocket!!, mHideHandler)
                    connectedThread.start()
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }*/

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket?.connect()
            if (mmSocket != null) {
                val connectedThread = ConnectedThread(mmSocket!!, mHideHandler)
                connectedThread.start()
            }
        } catch (connectException: IOException) {
            // Unable to connect; close the socket and return.
            connectException.printStackTrace()
            try {
                mmSocket?.close()
            } catch (closeException: IOException) {
                closeException.printStackTrace()
                //Log.e(TAG, "Could not close the client socket", closeException)
            }

            return
        }


    }

    // Closes the client socket and causes the thread to finish.
    fun cancel() {
        try {
            mmSocket?.close()
        } catch (e: IOException) {
            //Log.e("Debug", "Could not close the client socket", e)
            e.printStackTrace()
        }
    }
}
