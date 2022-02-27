package fr.esiea.esieabot.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.esiea.esieabot.R
import android.widget.TextView
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import fr.esiea.esieabot.BuildConfig

class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        //Ouverture de l'url sur clique du texte
        val appBug = view.findViewById<TextView>(R.id.tv_report_bug)
        appBug.setOnClickListener {
            val uri = Uri.parse("https://github.com/PST-Esieabot/Esieabot-Mobile-App/issues")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        // Change la langue de l'appli
        val appLanguage = view.findViewById<TextView>(R.id.tv_language)
        appLanguage.setOnClickListener {
            // TODO: Changer la langue de l'appli
            Toast.makeText(context, getString(R.string.toast_work_in_progress), Toast.LENGTH_SHORT).show()
        }

        // Recupère le numéro de version de l'application
        // Aller dans "AndroidManifest.xml" pour changer le numéro de version
        val myVersionName = BuildConfig.VERSION_NAME
        val versionName = view.findViewById<TextView>(R.id.tv_app_version_name)
        versionName.text = myVersionName

        return view
    }
}