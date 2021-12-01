package fr.esiea.esieabot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.esiea.esieabot.fragments.home_fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, home_fragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}