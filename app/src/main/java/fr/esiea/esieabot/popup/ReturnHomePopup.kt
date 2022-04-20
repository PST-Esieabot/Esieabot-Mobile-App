package fr.esiea.esieabot.popup

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ProgressBar
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R

class ReturnHomePopup(private val context: MainActivity) : Dialog(context) {
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.popup_return_home)

        //progressBar = findViewById(R.id.pb_return_home)
    }

    fun incrementProgress() {
        //progressBar.incrementProgressBy(1)
    }

    fun setMaxProgress(max: Int) {
        //progressBar.max = max
    }
}