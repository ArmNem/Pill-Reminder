package com.example.pillreminder.GUI.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pillreminder.R


/**
 * A simple [Fragment] subclass.
 * Use the [MyRemindersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyRemindersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_reminders, container, false)
    }

}