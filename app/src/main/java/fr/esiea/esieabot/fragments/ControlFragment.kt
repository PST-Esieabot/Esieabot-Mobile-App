package fr.esiea.esieabot.fragments

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R


class ControlFragment(private val context: MainActivity) : Fragment() {

    val FORWARDS = "forwards"
    val BACKWARDS = "backwards"
    val LEFT = "left"
    val RIGHT = "right"
    val STOP = "stop"

    lateinit var connectionSpd: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_control, container, false)

        connectionSpd = view.findViewById(R.id.tv_connection_speed)
        requireActivity().registerReceiver(signalReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))

        val btnForwards = view.findViewById<ImageButton>(R.id.btn_forwards)
        btnForwards.setOnTouchListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    context.write(FORWARDS)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    context.write(STOP)
                }
            }
            false
        }

        val btnBackwards = view.findViewById<ImageButton>(R.id.btn_backwards)
        btnBackwards.setOnTouchListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    context.write(BACKWARDS)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    context.write(STOP)
                }
            }
            false
        }

        val btnLeft = view.findViewById<ImageButton>(R.id.btn_left)
        btnLeft.setOnTouchListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    context.write(LEFT)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    context.write(STOP)
                }
            }
            false
        }

        val btnRight = view.findViewById<ImageButton>(R.id.btn_right)
        btnRight.setOnTouchListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    context.write(RIGHT)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    context.write(STOP)
                }
            }
            false
        }

        val btnStop = view.findViewById<ImageButton>(R.id.btn_stop)
        btnStop.setOnClickListener {
            context.write(STOP)
        }

        updateBatteryLevel(view)

        return view
    }

    private val signalReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE).toInt()
                connectionSpd.text = "$rssi ms"
                Toast.makeText(requireContext(), "rrsi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateBatteryLevel(view: View) {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        val batteryPct: Int? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale
        }

        val batteryLvl = view.findViewById<TextView>(R.id.tv_battery_percentage)
        batteryLvl.text = batteryPct.toString() + "%"

    }
}