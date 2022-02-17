package fr.esiea.esieabot.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R
import fr.esiea.esieabot.bluetooth.DevicesListFragment

class HomeFragment(private val context: MainActivity) : Fragment() {

    private lateinit var connectedDevice: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Lance la liste d'appareil pour se connecter
        val connect = view.findViewById<TextView>(R.id.tv_click_to_connect)
        connect.setOnClickListener{

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.devices_list_container, DevicesListFragment(context))
            transaction.addToBackStack(null)
            transaction.commit()
        }

        connectedDevice = view.findViewById(R.id.tv_connected_device_name)

        return view
    }

    fun updateDevice(deviceName: String) {
        connectedDevice.text = getString(R.string.home_connected_to) + deviceName
    }
}