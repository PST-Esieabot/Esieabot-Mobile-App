package fr.esiea.esieabot.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.esiea.esieabot.R
import fr.esiea.esieabot.bluetooth.DeviceListActivity

class ControlFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //val mDeviceAdress = requireArguments().getString(DeviceListActivity.EXTRA_ADDRESS)

        return inflater?.inflate(R.layout.fragment_control, container, false)
    }
}