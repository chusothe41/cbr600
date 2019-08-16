package com.example.cbr600

import android.bluetooth.BluetoothSocket
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothAdapter
import android.util.Log
import java.io.IOException
import java.util.UUID

class AcceptThread(uuid: UUID, mBluetoothAdapter: BluetoothAdapter) : Thread() {

    private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
        mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("cbr600", uuid)
    }

    override fun run() {
        // Keep listening until exception occurs or a socket is returned.
        var shouldLoop = true
        while (shouldLoop) {
            val socket: BluetoothSocket? = try {
                mmServerSocket?.accept()
            } catch (e: IOException) {
                //Log.e("Debug", "Socket's accept() method failed", e)
                e.printStackTrace()
                shouldLoop = false
                null
            }
            socket?.also {
                //manageMyConnectedSocket(it)
                mmServerSocket?.close()
                shouldLoop = false
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    fun cancel() {
        try {
            mmServerSocket?.close()
        } catch (e: IOException) {
            //Log.e("Debug", "Could not close the connect socket", e)
            e.printStackTrace()
        }
    }
}

