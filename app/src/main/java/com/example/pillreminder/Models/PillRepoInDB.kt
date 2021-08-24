package com.example.pillreminder.Models

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import com.example.pillreminder.data.BEPill
import com.example.pillreminder.data.PillDAO
import com.example.pillreminder.data.PillDatabase
import java.util.concurrent.Executors

class PillRepoInDB {

    private val database: PillDatabase

    private val pillDao : PillDAO

    private lateinit var cache: List<BEPill>

    private constructor(context: Context) {

        database = Room.databaseBuilder(context.applicationContext,
            PillDatabase::class.java,
            "pill-database").fallbackToDestructiveMigration().build()

        pillDao = database.pillDao()

        val updateCacheObserver = Observer<List<BEPill>>{ pills ->
            cache = pills;

        }
        getAllLiveData().observe(context as LifecycleOwner, updateCacheObserver)
    }

    fun getAllLiveData(): LiveData<List<BEPill>> = pillDao.getAll()


    fun getById(id: Int): BEPill? {
        cache.forEach { p -> if (p.id == id) return p; }
        return null;
    }

    fun getByPos(pos: Int): BEPill? {
        if (pos < cache.size)
            return cache[pos]
        return null
    }
    fun filter(text: String): List<BEPill>?{
        cache.forEach { p -> if (p.name.contains(text)) return p as List<BEPill> ; }
        return null;
    }


    private val executor = Executors.newSingleThreadExecutor()

    fun insert(p: BEPill) {
        executor.execute{ pillDao.insert(p) }
    }

    fun update(p: BEPill) {
        executor.execute { pillDao.update(p) }
    }

    fun delete(p: BEPill) {
        executor.execute { pillDao.delete(p) }
    }

    fun clear() {
        executor.execute { pillDao.deleteAll() }
    }


    companion object {
        private var Instance: PillRepoInDB? = null

        fun initialize(context: Context) {
            if (Instance == null)
                Instance = PillRepoInDB(context)
        }

        fun get(): PillRepoInDB {
            if (Instance != null) return Instance!!
            throw IllegalStateException("Person repo not initialized")
        }
    }
}