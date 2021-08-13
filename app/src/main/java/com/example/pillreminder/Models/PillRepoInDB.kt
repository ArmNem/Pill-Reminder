package com.example.pillreminder.Models

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import java.util.concurrent.Executors

class PillRepoInDB {

    private val database: PillDatabase

    private val personDao : PillDAO

    private lateinit var cache: List<BEPill>

    private constructor(context: Context) {

        database = Room.databaseBuilder(context.applicationContext,
            PillDatabase::class.java,
            "person-database").fallbackToDestructiveMigration().build()

        personDao = database.pillDao()

        val updateCacheObserver = Observer<List<BEPill>>{ persons ->
            cache = persons;

        }
        getAllLiveData().observe(context as LifecycleOwner, updateCacheObserver)
    }

    fun getAllLiveData(): LiveData<List<BEPill>> = personDao.getAll()


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
        executor.execute{ personDao.insert(p) }
    }

    fun update(p: BEPill) {
        executor.execute { personDao.update(p) }
    }

    fun delete(p: BEPill) {
        executor.execute { personDao.delete(p) }
    }

    fun clear() {
        executor.execute { personDao.deleteAll() }
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