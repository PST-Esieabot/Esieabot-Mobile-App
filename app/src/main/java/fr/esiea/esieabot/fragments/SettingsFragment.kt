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
import fr.esiea.esieabot.MainActivity

class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        //Ouverture de l'url sur click du texte
        val appBug = view.findViewById<View>(R.id.app_bug) as TextView
        appBug.setOnClickListener {

            val uri = Uri.parse("https://github.com/PST-Esieabot/Esieabot-Mobile-App/issues")

           // val intent = Intent(activity, MainActivity.class)
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
        return view
    }
}