package com.android.friendapp.Model

import androidx.lifecycle.LiveData
import androidx.room.*
//This class defines all DB operations, also an interface.
@Dao
interface FriendDao {
    //Gets all BEFriend objects.
    @Query("SELECT * from BEFriend order by id")
    fun getAll(): LiveData<List<BEFriend>>

    //Gets all Names, alphabetical order.
    @Query("SELECT name from BEFriend order by name")
    fun getAllNames(): LiveData<List<String>>

    //Gets specific object by the given ID.
    @Query("SELECT * from BEFriend where id = (:id)")
    fun getById(id: Int): LiveData<BEFriend>

    //Inserts the given object.
    @Insert
    fun insert(p: BEFriend)

    //Updates the given object.
    @Update
    fun update(p: BEFriend)

    //Deletes the given object.
    @Delete
    fun delete(p: BEFriend)

    //Deletes everything.
    @Query("DELETE from BEFriend")
    fun deleteAll()
}