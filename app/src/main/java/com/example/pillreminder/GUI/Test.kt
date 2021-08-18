package com.example.pillreminder.GUI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.lifecycle.Observer
import com.example.pillreminder.Models.BEPill
import com.example.pillreminder.Models.PillRepoInDB
import com.example.pillreminder.R
import kotlinx.android.synthetic.main.activity_test.*

class Test : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        PillRepoInDB.initialize(this)
        //insertTestData()
        setupDataObserver()
    }
    private fun insertTestData() {
        val mRep = PillRepoInDB.get()
        mRep.insert(BEPill(0,"BluePill", "1", "medicine", "Take 1 pill a day"))
        mRep.insert(BEPill(0,"RedPill", "2", "vitamin", "Take this vitamin before breakfast"))
    }
    //var cache: List<BEFriend>? = null;
    private fun setupDataObserver() {
        val mRep = PillRepoInDB.get()
        val nameObserver = Observer<List<BEPill>> { persons ->
            //cache = persons;
            val asStrings = persons.map { p -> "${p.id}, ${p.name}" }
            val adapter: ListAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                asStrings.toTypedArray()
            )
            pillList.adapter = adapter
            Log.d("abc", "Listview updated")
        }
        mRep.getAllLiveData().observe(this, nameObserver)
        pillList.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ -> onListItemClick(pos)}
    }
    fun onListItemClick(pos: Int) {
        // val id = cache!![pos].id
        //Toast.makeText(this, "You have clicked $name at position $pos", Toast.LENGTH_LONG).show()
        val mRep = PillRepoInDB.get()
        val friend = mRep.getByPos(pos)
        if (friend != null)
        {
            /*val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("friend",friend)
            intent.putExtra("id", friend.id )*/
            /*
            intent.putExtra("name", friend.name )
            intent.putExtra("phone", friend.phone)
            intent.putExtra("favorite", friend.isFavorite)
            intent.putExtra("email",friend.email)
            intent.putExtra("source",friend.source)*/
            startActivity(intent)
        }
}
}