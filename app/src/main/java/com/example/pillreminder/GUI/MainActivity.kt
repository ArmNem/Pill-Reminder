package com.example.pillreminder.GUI

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
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
class MainActivity() : AppCompatActivity (){
    private lateinit var navController: NavController
    private var myPillsFragment = MyPillsFragment()
    private var newReminderFragment = AddEditReminderFragment()
    private var myRemindersFragment = MyRemindersFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*if (savedInstanceState != null) {
            myPillsFragment = getSupportFragmentManager().getFragment(savedInstanceState, "MyPillsFragment") as MyPillsFragment
            newReminderFragment = getSupportFragmentManager().getFragment(savedInstanceState, "AddEditReminderFragment") as AddEditReminderFragment
            myRemindersFragment = getSupportFragmentManager().getFragment(savedInstanceState, "MyRemindersFragment") as MyRemindersFragment
        }*/
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)
        //replaceFragment(myPillsFragment)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_mypills -> replaceFragment(myPillsFragment)
                R.id.nav_myremiders -> replaceFragment(myRemindersFragment)
                R.id.nav_newreminder -> replaceFragment(newReminderFragment)
            }
            true
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.add(R.id.nav_host_fragment, myPillsFragment)
        transaction.add(R.id.nav_host_fragment, newReminderFragment)
        transaction.add(R.id.nav_host_fragment, myRemindersFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.commitAllowingStateLoss()

        }
    }
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        getSupportFragmentManager().putFragment(outState, "MyPillsFragment", MyPillsFragment())
        getSupportFragmentManager().putFragment(outState, "AddEditReminderFragment", AddEditReminderFragment())
        getSupportFragmentManager().putFragment(outState, "MyRemindersFragment", MyRemindersFragment())
    }
}

const val ADD_PILL_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_PILL_RESULT_OK = Activity.RESULT_FIRST_USER + 1