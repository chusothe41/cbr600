package com.example.cbr600

import android.bluetooth.BluetoothSocket
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothAdapter
import android.util.Log
import java.io.IOException
import java.util.UUID

class AcceptThread(uuid: UUID, mBluetoothAdapter: BluetoothAdapter) : Thread() {

    private var mmServerSocket: BluetoothServerSocket? = null

    init {
        try {
            mmServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("cbr600", uuid)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        Log.d("Debug", "1")
        var socket: BluetoothSocket?
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                Log.d("Debug", "2")
                socket = mmServerSocket!!.accept()
            } catch (e: IOException) {
                Log.d("Debug", "3")
                e.printStackTrace()
                break
            }

            // If a connection was accepted
            if (socket != null) {
                Log.d("Debug", "4")
                // Do work to manage the connection (in a separate thread)
                mmServerSocket!!.close()
                break
            }
        }
    }

    fun cancel() {
        try {
            mmServerSocket!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
