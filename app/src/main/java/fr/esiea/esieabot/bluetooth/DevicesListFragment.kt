package fr.esiea.esieabot.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
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
        }

        defaultList(newDevicesList)
        // Charge les listes
        val list_paired_devices = view.findViewById<RecyclerView>(R.id.rv_paired_devices)
        val list_new_devices = view.findViewById<RecyclerView>(R.id.rv_new_devices)

        list_paired_devices.adapter = DevicesAdapter(context, pairedDevicesList, this)
        list_new_devices.adapter = DevicesAdapter(context, newDevicesList, this)

        return view
    }

    override fun onItemClick(position: Int) {

        val clickedItem = pairedDevicesList[position]

        context.startConnection(clickedItem.deviceName, clickedItem.deviceAddress)

        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun defaultList(list: ArrayList<DevicesModel>) {
        list.add(
            DevicesModel(
                "Aucun appareil trouvé",
                ""
            ))
    }
}