package fr.esiea.esieabot.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.esiea.esieabot.BuildConfig
import fr.esiea.esieabot.Constants
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R
import java.util.*

class SettingsFragment(private val context: MainActivity) : Fragment() {
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
        val sharedPref = activity?.getSharedPreferences(Constants.LANG, Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()

        val langPref = sharedPref?.getString(Constants.LANG, "fr")
        val locale = Locale(langPref!!)
        Locale.setDefault(locale)

        val appLanguage = view.findViewById<TextView>(R.id.tv_language)
        appLanguage.setOnClickListener {

            if(Locale.getDefault().language == "en") {
                editor?.putString(Constants.LANG, "fr")?.apply()
                context.setAppLocale(requireContext(), "fr")
            }
            else {
                editor?.putString(Constants.LANG, "en")?.apply()
                context.setAppLocale(requireContext(), "en")
            }

            requireActivity().finish()
            requireActivity().startActivity(Intent(context,  MainActivity::class.java))
            requireActivity().finishAffinity()
        }

        // Aller dans "AndroidManifest.xml" pour changer le numéro de version
        val versionName = view.findViewById<TextView>(R.id.tv_app_version_name)
        versionName.text = BuildConfig.VERSION_NAME

        return view
    }
}