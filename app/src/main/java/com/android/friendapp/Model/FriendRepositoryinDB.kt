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

    //Gets the given instance of FriendDao.
    private val friendDao = database.friendDao()

    //Gets all BEFriend objects from FriendDao.
    fun getAll(): LiveData<List<BEFriend>> = friendDao.getAll()

    //Gets all names from FriendDao.
    fun getAllNames(): LiveData<List<String>> = friendDao.getAllNames()

    //Gets an object by ID from FriendDao.
    fun getById(id: Int) = friendDao.getById(id)

    //Executor tracks threads.
    private val executor = Executors.newSingleThreadExecutor()

    //Inserts the given object.
    fun insert(p: BEFriend) {
        executor.execute{ friendDao.insert(p) }
    }

    //Updates the given object.
    fun update(p: BEFriend) {
        executor.execute { friendDao.update(p) }
    }

    //Deletes the given object.
    fun delete(p: BEFriend) {
        executor.execute { friendDao.delete(p) }
    }

    //Deletes everything in the FriendDao.
    fun clear() {
        executor.execute { friendDao.deleteAll() }
    }

    //Tracks whether or not an instance has been created already.
    companion object {
        private var Instance: FriendRepositoryinDB? = null

        //When called, checks if an instance already exists, if not, create one.
        fun initialize(context: Context) {
            if (Instance == null)
                Instance = FriendRepositoryinDB(context)
        }

        //If an instance is not equal to null, return the instance.
        fun get(): FriendRepositoryinDB {
            if (Instance != null) return Instance!!
            throw IllegalStateException("Person repo not initialized")
        }
    }
}