package fr.esiea.esieabot.popup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R

class HelpConnectPopup(private val context: MainActivity) : Dialog(context){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.popup_help_connect)

        findViewById<ImageView>(R.id.popup_btn_close).setOnClickListener {
            dismiss()
        }
    }
}