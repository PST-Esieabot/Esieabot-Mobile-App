package fr.esiea.esieabot

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.esiea.esieabot.bluetooth.BluetoothTask
import fr.esiea.esieabot.fragments.ControlFragment
import fr.esiea.esieabot.fragments.HomeFragment
import fr.esiea.esieabot.fragments.SettingsFragment
import fr.esiea.esieabot.model.FragmentModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 2
    private val REQUEST_ENABLE_BT = 1
    private var bluetoothTask: BluetoothTask? = null

    private val viewModel : FragmentModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(HomeFragment(this))

        checkPermission()
        setUpBT()

        bluetoothTask = BluetoothTask(this, handler)

        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.home_page -> {
                    loadFragment(HomeFragment(this))
                    return@setOnItemSelectedListener true
                }
                R.id.control_page -> {
                    loadFragment(ControlFragment(this))
                    return@setOnItemSelectedListener true
                }
                R.id.settings_page -> {
                    loadFragment(SettingsFragment(this))
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }

        val sharedPref = this.getSharedPreferences(Constants.FIRSTTIME, Context.MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean(Constants.FIRSTTIME, true)

        if(isFirstTime) {
            HelpPopup(this).show()
        }

        val btnHelp = findViewById<ImageView>(R.id.btn_help)
        btnHelp.setOnClickListener {
            HelpPopup(this).show()
        }
    }

    fun startConnection(name: String, address: String) {
        bluetoothTask!!.connect(address)
    }

    @SuppressLint("MissingPermission")
    fun write(s: String) {
        if(bluetoothTask?.getState() != bluetoothTask?.STATE_CONNECTED) {
            Toast.makeText(this, getString(R.string.toast_device_not_connected), Toast.LENGTH_SHORT).show()
            return
        }

        bluetoothTask?.write(s.toByteArray())
    }

    private fun loadFragment(fragment: Fragment) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
    }

    @SuppressLint("MissingPermission")
    private fun setUpBT(){

        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        // Verifie que le bluetooth est disponible
        if (bluetoothAdapter == null) {
            Toast.makeText(this, getString(R.string.toast_bluetooth_not_supported), Toast.LENGTH_SHORT).show()
        }
        // Demande a l'utilisateur d'activer le bluetooth si il ne l'est pas
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    @SuppressLint("InlinedApi")
    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH), REQUEST_CODE)
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_CODE)
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN), REQUEST_CODE)
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_ADMIN), REQUEST_CODE)
        }
    }

    private val handler: Handler =  @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when(msg.what) {
                Constants.MESSAGE_READ -> if(bluetoothTask?.getState() == bluetoothTask?.STATE_CONNECTED){
                    val readBuf = msg.obj as ByteArray
                    val readMessage = String(readBuf, 0, msg.arg1)

                    viewModel.deviceIP = readMessage
                }
                Constants.MESSAGE_DEVICE_NAME -> {
                    val deviceName = msg.data.getString(Constants.DEVICE_NAME)
                    viewModel.deviceName = deviceName.toString()
                }
            }
        }
    }

    fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration

        config.locale = locale
        config.setLayoutDirection(locale)

        resources.updateConfiguration(config, resources.displayMetrics)

        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun message(s: String)
    {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}