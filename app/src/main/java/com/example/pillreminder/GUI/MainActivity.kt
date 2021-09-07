package com.example.pillreminder.GUI

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.pillreminder.GUI.Fragments.pills.MyPillsFragment
import com.example.pillreminder.GUI.Fragments.reminders.MyRemindersFragment
import com.example.pillreminder.GUI.Fragments.reminders.AddEditReminderFragment
import com.example.pillreminder.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val myPillsFragment = MyPillsFragment()
    private val newReminderFragment = AddEditReminderFragment()
    private val myRemindersFragment = MyRemindersFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)
        replaceFragment(myPillsFragment)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_mypills -> replaceFragment(myPillsFragment)
                R.id.nav_myremiders -> replaceFragment(myRemindersFragment)
                R.id.nav_newreminder -> replaceFragment(newReminderFragment)
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.commit()
        }
    }
}

const val ADD_PILL_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_PILL_RESULT_OK = Activity.RESULT_FIRST_USER + 1