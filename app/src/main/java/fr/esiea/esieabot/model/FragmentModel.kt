package fr.esiea.esieabot.model

import androidx.lifecycle.ViewModel
import fr.esiea.esieabot.Constants

class FragmentModel : ViewModel() {
    var deviceName: String = Constants.DEVICE_NAME
    var deviceIP: String = Constants.DEVICE_IP
}