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

        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        //Ouverture de l'url sur clique du texte
        val appBug = view.findViewById<View>(R.id.app_bug) as TextView
        appBug.setOnClickListener {
            val uri = Uri.parse("https://github.com/PST-Esieabot/Esieabot-Mobile-App/issues")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        // Change la langue de l'appli

        val appLanguage = view.findViewById<TextView>(R.id.app_language)
        appLanguage.setOnClickListener {
            // TODO: Changer la langue de l'appli
            Toast.makeText(context, "En cours de développement", Toast.LENGTH_SHORT).show()
        }

        // Recupère le numéro de version de l'application
        // Aller dans "AndroidManifest.xml" pour changer le numéro de version
        val myVersionName = BuildConfig.VERSION_NAME
        val versionName = view.findViewById<TextView>(R.id.app_versionName)
        versionName.text = myVersionName

        return view
    }
}