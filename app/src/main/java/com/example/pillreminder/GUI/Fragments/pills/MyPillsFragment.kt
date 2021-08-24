package com.example.pillreminder.GUI.Fragments.pills

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pillreminder.data.BEPill
import com.example.pillreminder.Models.PillRepoInDB
import com.example.pillreminder.R
import kotlinx.android.synthetic.main.fragment_my_pills.*


class MyPillsFragment : Fragment() {
    private var mContext:  Context =
    private val ARG_CAUGHT = "myFragment_caught"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        PillRepoInDB.initialize(mContext)
        //setupDataObserver()
        return inflater.inflate(R.layout.fragment_my_pills, container, false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
   private fun newInstance(): MyPillsFragment {
        val args: Bundle = Bundle()
       //args.putSerializable(ARG_CAUGHT, caught)
        val fragment =
            MyPillsFragment()
        fragment.arguments = args
        return fragment
    }
    private fun insertTestData() {
        val mRep = PillRepoInDB.get()
        mRep.insert(
            BEPill(
                0,
                "BluePill",
                "1",
                "medicine",
                "Take 1 pill a day"
            )
        )
        mRep.insert(
            BEPill(
                0,
                "RedPill",
                "2",
                "vitamin",
                "Take this vitamin before breakfast"
            )
        )
    }
    private fun setupDataObserver() {
        val newcontext = newInstance()
        val mRep = PillRepoInDB.get()
        val nameObserver = Observer<List<BEPill>> { pills ->
            //cache = persons;
            val asStrings = pills.map { p -> "${p.id}, ${p.name}" }
            val adapter: ListAdapter = ArrayAdapter(
                mContext,
                android.R.layout.simple_list_item_1,
                asStrings.toTypedArray()
            )
            pillList.adapter = adapter
            Log.d("abc", "Listview updated")
        }
        mRep.getAllLiveData().observe(viewLifecycleOwner, nameObserver)
        //pillList.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ -> onListItemClick(pos)}
    }
  /* fun onListItemClick(pos: Int) {
        // val id = cache!![pos].id
        //Toast.makeText(this, "You have clicked $name at position $pos", Toast.LENGTH_LONG).show()
        val mRep = PillRepoInDB.get()
        val friend = mRep.getByPos(pos)
        if (friend != null)
        {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("friend",friend)
            intent.putExtra("id", friend.id )
            /*
            intent.putExtra("name", friend.name )
            intent.putExtra("phone", friend.phone)
            intent.putExtra("favorite", friend.isFavorite)
            intent.putExtra("email",friend.email)
            intent.putExtra("source",friend.source)*/
            startActivity(intent)
        }
    }*/
}