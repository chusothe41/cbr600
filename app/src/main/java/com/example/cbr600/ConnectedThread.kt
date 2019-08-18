package com.example.cbr600

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.util.Log
import android.os.Handler
import android.os.Message
import java.io.*
import java.nio.charset.Charset

class ConnectedThread(private val mmSocket: BluetoothSocket, private val handler: Handler) : Thread() {

    private val mmInStream: InputStream = mmSocket.inputStream
    private val mmOutStream: OutputStream = mmSocket.outputStream
    private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

    companion object {
        val MESSAGE_READ: Int = 0
        val MESSAGE_WRITE: Int = 1
        val MESSAGE_TOAST: Int = 2
    }

    override fun run() {
        val bufferedReader = BufferedReader(InputStreamReader(mmInStream))
        var lineStr: String

        while (true) {
            lineStr = bufferedReader.readLine()
            if(lineStr == null){
                break
            }

            // Send the obtained bytes to the UI activity.
            val readMsg = handler.obtainMessage(MESSAGE_READ, lineStr)
            readMsg.sendToTarget()
        }
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


