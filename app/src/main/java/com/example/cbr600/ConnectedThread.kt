package com.example.cbr600

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import android.os.Handler
import android.os.Message

class ConnectedThread(private val mmSocket: BluetoothSocket, private val mHideHandler: Handler) : Thread() {
    private val mmInStream: InputStream?
    private val mmOutStream: OutputStream?

    init {
        var tmpIn: InputStream? = null
        var tmpOut: OutputStream? = null

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = mmSocket.inputStream
            tmpOut = mmSocket.outputStream
        } catch (e: IOException) {
        }

        mmInStream = tmpIn
        mmOutStream = tmpOut

        Log.d("Debug", "ConnectedThread iniciado")
    }

    override fun run() {
        val buffer = ByteArray(1024)  // buffer store for the stream
        var bytes: Int // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream!!.read(buffer)
                // Send the obtained bytes to the UI activity
                val message = Message.obtain()
                message.what = 1
                mHideHandler.obtainMessage(message.what, bytes, -1, buffer).sendToTarget()
            } catch (e: IOException) {
                e.printStackTrace()
                break
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    fun write(bytes: ByteArray) {
        try {
            mmOutStream!!.write(bytes)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /* Call this from the main activity to shutdown the connection */
    fun cancel() {
        try {
            mmSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

