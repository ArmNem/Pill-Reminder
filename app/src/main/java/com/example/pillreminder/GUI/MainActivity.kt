package com.example.pillreminder.GUI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.pillreminder.GUI.Fragments.*
import com.example.pillreminder.GUI.Fragments.pills.MyPillsFragment
import com.example.pillreminder.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val myPillsFragment =
        MyPillsFragment()
    private val myRemindersFragment = MyRemindersFragment()
    private val homeFragment = HomeFragment()
    private val newReminderFragment = NewReminderFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(homeFragment)
        button.setOnClickListener { v -> onClickTest(v) }
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_mypills -> replaceFragment(myPillsFragment)
                R.id.nav_myremiders -> replaceFragment(myRemindersFragment)
                R.id.nav_home -> replaceFragment(homeFragment)
                R.id.nav_newreminder -> replaceFragment(newReminderFragment)
                R.id.nav_settings -> replaceFragment(settingsFragment)
            }
            true
        }

    }
    private fun replaceFragment(fragment: Fragment){
        if(fragment != null)
        {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    fun onClickTest(view: View) {
        val intent = Intent(this, Test::class.java)
        startActivity(intent)
    }
}