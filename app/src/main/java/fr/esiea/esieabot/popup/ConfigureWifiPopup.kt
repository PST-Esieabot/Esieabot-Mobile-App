package fr.esiea.esieabot.popup

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R


class ConfigureWifiPopup(private val context: MainActivity) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_configure_wifi)

        findViewById<ImageView>(R.id.popup_btn_close).setOnClickListener {
            dismiss()
        }

        val ssidEditor = findViewById<EditText>(R.id.et_ssid)
        val passwordEditor = findViewById<EditText>(R.id.et_password)

        val SSID = ssidEditor.text
        val password = passwordEditor.text

        findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            val wifi ="w$SSID*$password"
            context.write(wifi)
            dismiss()
        }
    }
}