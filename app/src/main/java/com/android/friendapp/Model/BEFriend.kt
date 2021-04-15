package com.android.friendapp.Model

import android.net.Uri
import java.io.File
import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class BEFriend(  @PrimaryKey(autoGenerate = true) var id:Int,
                 var name: String,
                 var phone: String,
                 var isFavorite: Boolean,
                 var email: String,
                 var url: String,
                 var pictureFile: String?,
                 var location: String? ,
                 var birthday: String? ,
                 var Address: String? ) : Serializable {
}