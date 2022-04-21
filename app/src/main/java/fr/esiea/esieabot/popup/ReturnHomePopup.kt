package fr.esiea.esieabot.popup

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ProgressBar
import fr.esiea.esieabot.Constants
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R
import fr.esiea.esieabot.model.ReturnHomeModel


class ReturnHomePopup(private val context: MainActivity, private val buffer: ArrayList<ReturnHomeModel>) : Dialog(context) {
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.popup_return_home)

        progressBar = findViewById(R.id.pb_return_home)
        progressBar.max = buffer.size
        progressBar.progress = 0

        returnHome(buffer)
    }

    private fun returnHome(buffer: ArrayList<ReturnHomeModel>) {
        for (item in buffer.reversed()) {
            when(item.action) {
                Constants.FORWARDS -> {
                    context.write(Constants.BACKWARDS)
                }
                Constants.BACKWARDS -> {
                    context.write(Constants.FORWARDS)
                }
                Constants.LEFT -> {
                    context.write(Constants.RIGHT)
                }
                Constants.RIGHT -> {
                    context.write(Constants.LEFT)
                }
            }
            progressBar.incrementProgressBy(1)

            Thread.sleep(item.duration)
            context.write(Constants.STOP)
            Thread.sleep(1000)
        }

        context.write(Constants.STOP)

        buffer.clear()
        dismiss()
    }
}
