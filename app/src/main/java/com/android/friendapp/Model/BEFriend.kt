package com.android.friendapp.Model

import android.net.Uri
import java.io.File
import java.io.Serializable

class BEFriend(var id:Int, var name: String, var phone: String, var isFavorite: Boolean, var email: String, var url: String, var pictureFile: File?) : Serializable {
}