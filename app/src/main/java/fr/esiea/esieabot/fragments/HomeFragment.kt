package fr.esiea.esieabot.fragments

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import fr.esiea.esieabot.Constants
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R
import fr.esiea.esieabot.bluetooth.DevicesListFragment
import fr.esiea.esieabot.model.FragmentModel


class HomeFragment(private val context: MainActivity) : Fragment() {

    private val viewModel : FragmentModel by activityViewModels()
    private lateinit var deviceIP: String
    private lateinit var deviceName: String

    private lateinit var connectedDevice: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceIP = viewModel.deviceIP
        deviceName = viewModel.deviceName

        // Lance la liste d'appareil pour se connecter
        val connect = view.findViewById<TextView>(R.id.tv_click_to_connect)
        connect.setOnClickListener{

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.devices_list_container, DevicesListFragment(context))
            transaction.addToBackStack(null)
            transaction.commit()
        }

        requireActivity().registerReceiver(connectedReceiver, IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED))
        requireActivity().registerReceiver(disconnectedReceiver, IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED))

        connectedDevice = view.findViewById(R.id.tv_connected_device_name)

        if(viewModel.deviceName != Constants.DEVICE_NAME)
            connectedDevice.text = getString(R.string.home_connected_to, viewModel.deviceName)

    }

    private val connectedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent) {
            if (BluetoothDevice.ACTION_ACL_CONNECTED == intent.action) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                viewModel.deviceName = device?.name.toString()
                connectedDevice.text = getString(R.string.home_connected_to, device!!.name)
            }
        }
    }
    private val disconnectedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent) {
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED == intent.action) {
                connectedDevice.text = getString(R.string.home_robot_non_connected)
                viewModel.deviceName = Constants.DEVICE_NAME
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(connectedReceiver)
        requireActivity().unregisterReceiver(disconnectedReceiver)
    }
}