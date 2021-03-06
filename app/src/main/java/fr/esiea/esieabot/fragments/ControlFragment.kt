package fr.esiea.esieabot.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import fr.esiea.esieabot.Constants
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R
import fr.esiea.esieabot.model.FragmentModel
import fr.esiea.esieabot.model.ReturnHomeModel
import fr.esiea.esieabot.popup.HelpConnectPopup
import kotlin.collections.ArrayList


class ControlFragment(private val context: MainActivity) : Fragment() {

    private val viewModel : FragmentModel by activityViewModels()
    private val returnHomeList = arrayListOf<ReturnHomeModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_control, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvDeviceIP = view.findViewById<TextView>(R.id.tv_device_IP)
        val btnForwards = view.findViewById<ImageButton>(R.id.btn_forwards)
        val btnBackwards = view.findViewById<ImageButton>(R.id.btn_backwards)
        val btnLeft = view.findViewById<ImageButton>(R.id.btn_left)
        val btnRight = view.findViewById<ImageButton>(R.id.btn_right)
        val btnStop = view.findViewById<ImageButton>(R.id.btn_stop)
        val cameraView = view.findViewById<WebView>(R.id.wv_camera)
        val cameraHiddenView = view.findViewById<View>(R.id.v_camera_hidden)
        val cameraStatus = view.findViewById<TextView>(R.id.tv_camera_status)

        val btnReturnHome = view.findViewById<ImageButton>(R.id.btn_return_home)
        val btnRectange = view.findViewById<ImageButton>(R.id.btn_rectangle)
        val btnScan = view.findViewById<ImageButton>(R.id.btn_scan)
        val btnUltrasoundActivation = view.findViewById<ToggleButton>(R.id.btn_ultrasoundActivation)

        // Met a jour le pourcentage de batterie
        updateBatteryLevel(view)

        // Met a jour l'addresse IP de l'appareil connect??
        if(viewModel.deviceIP != Constants.DEVICE_IP)
            loadCamera(viewModel.deviceIP, cameraView, tvDeviceIP, cameraHiddenView, cameraStatus)

        cameraStatus.setOnClickListener {
            HelpConnectPopup(context).show()
        }

        var start = 0L
        var keyPressedDuration: Long

        btnForwards.setOnTouchListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    context.write(Constants.FORWARDS)
                    start = System.currentTimeMillis()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    context.write(Constants.STOP)

                    keyPressedDuration = (System.currentTimeMillis() - start)
                    returnHomeList.add(ReturnHomeModel(Constants.FORWARDS, keyPressedDuration))
                }
            }
            false
        }

        btnBackwards.setOnTouchListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    context.write(Constants.BACKWARDS)
                    start = System.currentTimeMillis()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    context.write(Constants.STOP)

                    keyPressedDuration = (System.currentTimeMillis() - start)
                    returnHomeList.add(ReturnHomeModel(Constants.BACKWARDS, keyPressedDuration))
                }
            }
            false
        }

        btnLeft.setOnTouchListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    context.write(Constants.LEFT)
                    start = System.currentTimeMillis()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    context.write(Constants.STOP)

                    keyPressedDuration = (System.currentTimeMillis() - start)
                    returnHomeList.add(ReturnHomeModel(Constants.LEFT, keyPressedDuration))
                }
            }
            false
        }

        btnRight.setOnTouchListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    context.write(Constants.RIGHT)
                    start = System.currentTimeMillis()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    context.write(Constants.STOP)

                    keyPressedDuration = (System.currentTimeMillis() - start)
                    returnHomeList.add(ReturnHomeModel(Constants.RIGHT, keyPressedDuration))
                }
            }
            false
        }

        btnStop.setOnClickListener {
            context.write(Constants.STOP)
        }

        // TODO : Lancer le Dialog pendant le chargement du retour a la maison
        btnReturnHome.setOnClickListener {
            //ReturnHomePopup(context, returnHomeList).show()
            returnHome(returnHomeList)
        }

        btnUltrasoundActivation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                context.write(Constants.ULTRASOUND_ON)
                Toast.makeText(requireContext(), getString(R.string.control_ultrasound_activated), Toast.LENGTH_SHORT).show()
            } else {
                context.write(Constants.ULTRASOUND_OFF)
                Toast.makeText(requireContext(), getString(R.string.control_ultrasound_off), Toast.LENGTH_SHORT).show()
            }
        }

        btnRectange.setOnClickListener {
            for (i in 1..4) {
                context.write(Constants.FORWARDS)
                Thread.sleep(1000)
                context.write(Constants.RIGHT)
                Thread.sleep(500)
            }
        }

        btnScan.setOnClickListener {
            context.write(Constants.SCAN)
            Toast.makeText(requireContext(), getString(R.string.control_scan), Toast.LENGTH_SHORT).show()
        }
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

            Thread.sleep(item.duration)
            context.write(Constants.STOP)
            Thread.sleep(1000)
        }

        context.write(Constants.STOP)
        buffer.clear()
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

    @SuppressLint("SetTextI18n")
    private fun loadCamera(IP: String, camera: WebView, ipAddress: TextView, cameraHiddenView: View, cameraStatus: TextView) {
        ipAddress.text = viewModel.deviceIP + " IP"
        camera.loadUrl(IP)

        camera.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                cameraStatus.visibility = View.VISIBLE
                cameraHiddenView.visibility = View.VISIBLE
                camera.visibility = View.INVISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                cameraStatus.visibility = View.INVISIBLE
                cameraHiddenView.visibility = View.INVISIBLE
                camera.visibility = View.VISIBLE
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                cameraStatus.visibility = View.VISIBLE
                cameraHiddenView.visibility = View.VISIBLE
                camera.visibility = View.INVISIBLE
                super.onReceivedError(view, request, error)
            }
        }
    }
}