package fr.esiea.esieabot

import android.app.Dialog
import android.os.Bundle

class LoadingPopup(private val context: MainActivity, private val deviceName: String) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_loading)
    }
}