package fr.esiea.esieabot.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R

class ControlFragment(private val context: MainActivity) : Fragment() {

    val FORWARDS = "forwards"
    val BACKWARDS = "backwards"
    val LEFT = "left"
    val RIGHT = "right"
    val STOP = "stop"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_control, container, false)

        val btnForwards = view.findViewById<ImageButton>(R.id.btn_forwards)
        btnForwards.setOnClickListener{
            context.write(FORWARDS)
        }
        val btnBackwards = view.findViewById<ImageButton>(R.id.btn_backwards)
        btnBackwards.setOnClickListener{
            context.write(BACKWARDS)
        }
        val btnLeft = view.findViewById<ImageButton>(R.id.btn_left)
        btnLeft.setOnClickListener{
            context.write(LEFT)
        }
        val btnRight = view.findViewById<ImageButton>(R.id.btn_right)
        btnRight.setOnClickListener{
            context.write(RIGHT)
        }
        val btnStop = view.findViewById<ImageButton>(R.id.btn_stop)
        btnStop.setOnClickListener{
            context.write(STOP)
        }

        return view
    }
}