package fr.esiea.esieabot.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R
import fr.esiea.esieabot.adapter.DevicesAdapter
import fr.esiea.esieabot.model.DevicesModel

class DevicesListFragment(private val context: MainActivity): Fragment(), DevicesAdapter.OnItemClickListener {

    private val pairedDevicesList = arrayListOf<DevicesModel>()
    private val newDevicesList = arrayListOf<DevicesModel>()
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var list_new_devices : RecyclerView

    @SuppressLint("MissingPermission")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.devices_list, container, false)

        // Ferme le fragment sur clique de la croix
        val close = view.findViewById<ImageView>(R.id.btn_close)
        close.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Remplie la liste avec les périphériques appairés
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            pairedDevicesList.add(DevicesModel(deviceName, deviceHardwareAddress))
        }
        if(pairedDevicesList.size == 0) {
            Toast.makeText(requireContext(), getString(R.string.toast_no_paired_device_found), Toast.LENGTH_SHORT).show()
            defaultList(pairedDevicesList)
        }

        // Remplie la liste des appareils disponibles
        bluetoothAdapter?.startDiscovery()
        requireActivity().registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        if(newDevicesList.size == 0) {
            defaultList(newDevicesList)
        }

        // Charge les listes
        val list_paired_devices = view.findViewById<RecyclerView>(R.id.rv_paired_devices)
        list_new_devices = view.findViewById<RecyclerView>(R.id.rv_new_devices)

        list_paired_devices.adapter = DevicesAdapter(context, pairedDevicesList, this)
        list_new_devices.adapter = DevicesAdapter(context, newDevicesList, this)

        return view
    }

    override fun onItemClick(position: Int) {

        val clickedItem = pairedDevicesList[position]

        context.startConnection(clickedItem.deviceName, clickedItem.deviceAddress)

        requireActivity().supportFragmentManager.popBackStack()
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission", "NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address

                    // Met a jour la liste avec les nouveaux appareils connectés
                    newDevicesList.add(DevicesModel(deviceName!!, deviceHardwareAddress!!))
                    list_new_devices.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun defaultList(list: ArrayList<DevicesModel>) {
        list.add(
            DevicesModel(
                context.getString(R.string.deviceList_no_device_found),
                ""
            ))
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        bluetoothAdapter?.cancelDiscovery()
        requireActivity().unregisterReceiver(receiver)
    }
}