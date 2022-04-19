package fr.esiea.esieabot.popup

import android.app.Dialog
import android.os.Bundle
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R

class LoadingPopup(private val context: MainActivity, private val deviceName: String) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_loading)
    }
}