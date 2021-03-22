package com.android.friendapp.Model
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.concurrent.Executors

class FriendRepositoryinDB private constructor(private val context: Context) {


    private val database: FriendDatabase = Room.databaseBuilder(context.applicationContext,
        FriendDatabase::class.java,
        "friend-database").build()


    private val friendDao = database.friendDao()

    fun getAll(): LiveData<List<BEFriend>> = friendDao.getAll()

    fun getAllNames(): LiveData<List<String>> = friendDao.getAllNames()

    fun getById(id: Int) = friendDao.getById(id)

    private val executor = Executors.newSingleThreadExecutor()

    fun insert(p: BEFriend) {
        executor.execute{ friendDao.insert(p) }
    }

    fun update(p: BEFriend) {
        executor.execute { friendDao.update(p) }
    }

    fun delete(p: BEFriend) {
        executor.execute { friendDao.delete(p) }
    }

    fun clear() {
        executor.execute { friendDao.deleteAll() }
    }


    companion object {
        private var Instance: FriendRepositoryinDB? = null

        fun initialize(context: Context) {
            if (Instance == null)
                Instance = FriendRepositoryinDB(context)
        }

        fun get(): FriendRepositoryinDB {
            if (Instance != null) return Instance!!
            throw IllegalStateException("Person repo not initialized")
        }
    }
}