package fr.esiea.esieabot.bluetooth

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.*
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import fr.esiea.esieabot.Constants
import fr.esiea.esieabot.R
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothTask(private val activity: Activity, private val handler: Handler){

    val STATE_NONE: Int = 0
    val STATE_LISTEN: Int = 1
    val STATE_CONNECTING: Int = 2
    val STATE_CONNECTED: Int = 3

    private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null
    private var connectionState = STATE_NONE
    private var device: BluetoothDevice? = null

    private var progressDialog: ProgressDialog? = null

    @Synchronized
    fun getState(): Int { return connectionState }

    @SuppressLint("MissingPermission")
    fun connect(address: String) {
        device = bluetoothAdapter?.getRemoteDevice(address)

        progressDialog = ProgressDialog.show(activity, String.format(activity.getString(R.string.progressDialog_connecting_to), device?.name), activity.getString(R.string.progressDialog_loading))

        // arrête les threads s'il sont en cours
        if (connectThread != null) {
                connectThread?.interrupt()
                connectThread = null
        }

        if (connectedThread != null) {
            connectedThread?.interrupt()
            connectedThread = null
        }

        //connectThread = ConnectThread(device) <- Remettre ça si ça beug
        connectThread = device?.let { ConnectThread(it) }
        connectThread?.start()
    }

    // Sources : https://developer.android.com/guide/topics/connectivity/bluetooth/connect-bluetooth-devices
    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        init {
            connectionState = STATE_CONNECTING
        }

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            if(bluetoothAdapter?.isDiscovering == true) {
                bluetoothAdapter.cancelDiscovery()
            }
            try {
                mmSocket?.let { socket ->
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.
                    socket.connect()

                    // The connection attempt succeeded. Perform work associated with
                    // the connection in a separate thread.
                    connected(socket)
                }
            } catch (e: IOException) {
                connectionFailed()
                cancel()
            }
            progressDialog!!.dismiss()
        }

        // Closes the client socket and causes the thread to finish.
        private fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) { }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    fun connected(socket: BluetoothSocket) {

        val tmp = activity.getString(R.string.toast_connected)
        message(tmp)

        // Envoie le nom de l'appareil connecté a la main activity

        // Send the name of the connected device back to the UI Activity
        val msg: Message = handler.obtainMessage(Constants.MESSAGE_DEVICE_NAME)
        val bundle = Bundle()
        bundle.putString(Constants.DEVICE_NAME, device!!.name)
        msg.data = bundle
        handler.sendMessage(msg)


        if (connectThread != null) {
            connectThread?.interrupt()
            connectThread = null
        }
        if (connectedThread != null) {
            connectedThread?.interrupt()
            connectedThread = null
        }

        connectedThread = ConnectedThread(socket)
        connectedThread?.start()
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        init {
            connectionState = STATE_CONNECTED
        }

        override fun run() {
            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    val tmp = activity.getString(R.string.toast_deconnected)
                    message(tmp)
                    connectionLost()
                    break
                }

                // Envoie la valeur lu a la mainActivity
                val readMsg = handler.obtainMessage(
                    Constants.MESSAGE_READ, numBytes, -1,
                    mmBuffer)
                readMsg.sendToTarget()
            }
        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                val tmp = activity.getString(R.string.toast_error_sending_data)
                message(tmp)

                return
            }

        }

        // Call this method from the main activity to shut down the connection.
        private fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) { }
        }
    }

    fun write(bytes: ByteArray) {

        val tmp: ConnectedThread

        synchronized(this) {
            if(connectionState != STATE_CONNECTED) return
            tmp = connectedThread!!
        }

        tmp.write(bytes)
    }

    fun message(s: String) {

        activity.runOnUiThread(Runnable() {
            run {
                Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun connectionLost() {
        val tmp = activity.getString(R.string.toast_connection_lost)
        message(tmp)
        connectionState = STATE_NONE
    }

    private fun connectionFailed() {
        val tmp = activity.getString(R.string.toast_connection_failed)
        message(tmp)
        connectionState = STATE_NONE
    }
}