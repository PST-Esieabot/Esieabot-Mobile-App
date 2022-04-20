package fr.esiea.esieabot.popup

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R

class LoadingPopup(private val context: Activity, private val deviceName: String) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.popup_loading)

        val device = findViewById<TextView>(R.id.popup_loading_to)
        device.text = context.getString(R.string.popup_connecting_to, deviceName)
    }
}