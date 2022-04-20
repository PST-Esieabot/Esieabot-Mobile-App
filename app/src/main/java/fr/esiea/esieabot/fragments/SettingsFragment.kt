package fr.esiea.esieabot.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.esiea.esieabot.BuildConfig
import fr.esiea.esieabot.Constants
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R
import fr.esiea.esieabot.popup.ConfigureWifiPopup
import org.jetbrains.anko.find
import java.io.BufferedReader
import java.util.*

class SettingsFragment(private val context: MainActivity) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val configureWifi = view.findViewById<Button>(R.id.btn_configure_camera)
        configureWifi.setOnClickListener {
            ConfigureWifiPopup(context).show()
        }

        //Ouverture de l'url sur clique du texte
        val appBug = view.findViewById<TextView>(R.id.tv_report_bug)
        appBug.setOnClickListener {
            val uri = Uri.parse("https://github.com/PST-Esieabot/Esieabot-Mobile-App/issues")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        // Change la langue de l'appli
        val appLanguage = view.findViewById<TextView>(R.id.tv_language)
        appLanguage.setOnClickListener {

            if(Locale.getDefault().language == "en") {
                context.setAppLocale(requireContext(), "fr")
            }
            else {
                context.setAppLocale(requireContext(), "en")
            }
        }

        // Aller dans "AndroidManifest.xml" pour changer le numéro de version
        val versionName = view.findViewById<TextView>(R.id.tv_app_version_name)
        versionName.text = BuildConfig.VERSION_NAME

        return view
    }
}