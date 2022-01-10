package fr.esiea.esieabot.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.esiea.esieabot.R
import android.content.Intent
import fr.esiea.esieabot.bluetooth.DeviceListActivity

// TODO : Ajouter un robot triste si le robot n'est pas connecte

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val connectDevice = view.findViewById<TextView>(R.id.app_connectDevice)
        connectDevice.setOnClickListener {
            val intent = Intent(this@HomeFragment.context, DeviceListActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}