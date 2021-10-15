package com.example.pillreminder.GUI

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.example.pillreminder.GUI.Fragments.pills.MyPillsFragment
import com.example.pillreminder.GUI.Fragments.reminders.MyRemindersFragment
import com.example.pillreminder.GUI.Fragments.reminders.AddEditReminderFragment
import com.example.pillreminder.R
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {
    private lateinit var navController: NavController
    private var myPillsFragment = MyPillsFragment()
    private var newReminderFragment = AddEditReminderFragment()
    private var myRemindersFragment = MyRemindersFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        setupActionBarWithNavController(navController)
        bottom_navigation.setupWithNavController(navController)
        //setupViewPager(navHostFragment)
        /*val mViewPager = supportFragmentManager.findFragmentById(R.id.viewpager) as ViewPager
        navController = mViewPager.findNavController()*/
        //setupActionBarWithNavController(navController)
        //replaceFragment(myPillsFragment)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it?.itemId) {
                R.id.myPillsFragment -> {

                   // navHostFragment.setCurrentItem(0)
                    replaceFragment(myPillsFragment)
                    Log.d("NAV", "Current fragment is: " + it.itemId)
                    true
                }
                R.id.myRemindersFragment -> {
                    //navHostFragment.setCurrentItem(1)
                    replaceFragment(myRemindersFragment)
                    Log.d("NAV", "Current fragment is: " + it.itemId)
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.replace(R.id.nav_host_fragment, fragment)
            //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.commitAllowingStateLoss()
        } else {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.replace(R.id.nav_host_fragment, fragment)
            //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.commitAllowingStateLoss()
        }
    }


}

const val ADD_PILL_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_PILL_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val EDIT_REMINDERTIME_RESULT_CANCEL = Activity.RESULT_CANCELED