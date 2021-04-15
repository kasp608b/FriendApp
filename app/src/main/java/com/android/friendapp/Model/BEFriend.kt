package com.android.friendapp.Model

import android.net.Uri
import java.io.File
import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

//Friend business entity, marked as a database room entity, and ID is auto generate-able, and entity is serializable. Data types marked with "?" is nullable.
@Entity
class BEFriend(  @PrimaryKey(autoGenerate = true) var id:Int,
                 var name: String,
                 var phone: String,
                 var isFavorite: Boolean,
                 var email: String,
                 var url: String,
                 var pictureFile: String?,
                 var location: String? ,
                 var birthday: String? ) : Serializable {
}