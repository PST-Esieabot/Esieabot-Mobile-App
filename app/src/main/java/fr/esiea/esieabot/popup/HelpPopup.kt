package fr.esiea.esieabot.popup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import fr.esiea.esieabot.Constants
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R

class HelpPopup(private val context: MainActivity) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.popup_help)

        val sharedPref = context.getSharedPreferences(Constants.FIRSTTIME, Context.MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean(Constants.FIRSTTIME, true)
        val titleText = findViewById<TextView>(R.id.popup_title)

        if (isFirstTime) {
            titleText.text = context.getString(R.string.popup_first_connection)
        }

        // Ferme la popup en cas d'appuis
        findViewById<ImageView>(R.id.popup_btn_close).setOnClickListener {
            dismiss()
        }

        findViewById<Button>(R.id.btn_understand).setOnClickListener {

            with (sharedPref.edit()) {
                putBoolean(Constants.FIRSTTIME, false)
                apply()
            }
            dismiss()
        }
    }
}