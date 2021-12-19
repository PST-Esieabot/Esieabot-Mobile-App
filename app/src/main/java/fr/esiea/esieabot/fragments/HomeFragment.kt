package fr.esiea.esieabot.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.esiea.esieabot.R
import android.content.Intent
import fr.esiea.esieabot.ConnectDeviceActivity

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val connectDevice = view.findViewById<View>(R.id.app_connectDevice) as TextView
        connectDevice.setOnClickListener {
            val intent = Intent(context, ConnectDeviceActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}