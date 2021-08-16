package com.example.pillreminder.GUI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pillreminder.GUI.Fragments.*
import com.example.pillreminder.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val myPillsFragment = MyPillsFragment()
    private val myRemindersFragment = MyRemindersFragment()
    private val homeFragment = HomeFragment()
    private val newReminderFragment = NewReminderFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(homeFragment)

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
}