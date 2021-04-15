package com.android.friendapp.Model

import androidx.room.Database
import androidx.room.RoomDatabase

//Defines the FriendDatabase, it is a Room Database.
@Database(entities = [BEFriend::class], version=1)
abstract class FriendDatabase : RoomDatabase() {

    //Returns the FriendDao.
    abstract fun friendDao(): FriendDao
}