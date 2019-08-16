package com.example.cbr600

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import android.os.Handler
import android.os.Message

/*class MyBluetoothService(
    // handler that gets info from Bluetooth service
    private val handler: Handler) {

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }

                // Send the obtained bytes to the UI activity.
                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, numBytes, -1,
                    mmBuffer)
                readMsg.sendToTarget()
            }
        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }

            // Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                MESSAGE_WRITE, -1, -1, mmBuffer)
            writtenMsg.sendToTarget()
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }
}*/

class ConnectedThread(private val mmSocket: BluetoothSocket, private val handler: Handler) : Thread() {

    private val mmInStream: InputStream = mmSocket.inputStream
    private val mmOutStream: OutputStream = mmSocket.outputStream
    private val mmBuffer: ByteArray = ByteArray(2048) // mmBuffer store for the stream

    companion object {
        val MESSAGE_READ: Int = 0
        val MESSAGE_WRITE: Int = 1
        val MESSAGE_TOAST: Int = 2
    }

    override fun run() {
        var numBytes = 0// bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            // Read from the InputStream.
            numBytes = try {
                mmInStream.read(mmBuffer)
            } catch (e: IOException) {
                //Log.d(TAG, "Input stream was disconnected", e)
                e.printStackTrace()
                break
            }

            Log.d("Debug", "" + mmBuffer.toString())

            // Send the obtained bytes to the UI activity.
            val readMsg = handler.obtainMessage(MESSAGE_READ, numBytes, -1, mmBuffer)
            readMsg.sendToTarget()
        }

        /*while (numBytes != -1) {

            numBytes = try {
                mmInStream.read()
            } catch (e: IOException) {
                //Log.d(TAG, "Input stream was disconnected", e)
                e.printStackTrace()
                break
            }

            Log.d("Debug", "" + numBytes as Char)
        }*/
    }

    // Call this from the main activity to send data to the remote device.
    fun write(bytes: ByteArray) {
        try {
            mmOutStream.write(bytes)
        } catch (e: IOException) {
            //Log.e(TAG, "Error occurred when sending data", e)
            e.printStackTrace()

            // Send a failure message back to the activity.
            val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
            val bundle = Bundle().apply {
                putString("toast", "Couldn't send data to the other device")
            }
            writeErrorMsg.data = bundle
            handler.sendMessage(writeErrorMsg)
            return
        }

        // Share the sent message with the UI activity.
        val writtenMsg = handler.obtainMessage(MESSAGE_WRITE, -1, -1, mmBuffer)
        writtenMsg.sendToTarget()
    }

    // Call this method from the main activity to shut down the connection.
    fun cancel() {
        try {
            mmSocket.close()
        } catch (e: IOException) {
            //Log.e(TAG, "Could not close the connect socket", e)
            e.printStackTrace()
        }
    }
}


