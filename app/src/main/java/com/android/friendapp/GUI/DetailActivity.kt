package com.android.friendapp.GUI

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.friendapp.Model.BEFriend
import com.android.friendapp.Model.Friends
import com.android.friendapp.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var friend:BEFriend
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (intent.extras != null) {
            val extras: Bundle = intent.extras!!
            /*val name = extras.getString("name")
            val phone = extras.getString("phone")
            val favorite = extras.getBoolean("favorite")*/
            friend = extras.getSerializable("friend") as BEFriend
            tvName.setText(friend.name)
            tvPhone.setText(friend.phone)
            imgFavorite.setImageResource(if (friend.isFavorite) R.drawable.ok else R.drawable.notok)
            imgFavorite.setOnClickListener{ v -> onClickFavorite()}
        }
        else
        {
            Log.d("xyz", "system error: intent.extras for detailactivity is null!!!!")
        }
    }

    fun onClickBack(view: View) { finish() }
    fun onClickSave(view: View) {
        if(!(tvName.text.isBlank() || tvPhone.text.isBlank()))
        {
            val friendToUpdateIndex = Friends.mFriends.indexOf(Friends.mFriends.find { v -> v.id == friend.id  })
            friend.name = tvName.text.toString()
            friend.phone = tvPhone.text.toString()
            Friends.getAll()[friendToUpdateIndex] = friend
            Log.d("xyz", "Delete ${friend.id.toString()}")
            finish()
        }
        else
        {
            Toast.makeText(
                this,
                "Friend cant have blank name or phone number",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    fun onClickDelete(view: View) {
        val friendToRemove = Friends.mFriends.find { v -> v.id == friend.id  }
        val yoink = Friends.mFriends.remove(friendToRemove)
        Log.d("xyz", "Delete $yoink")
        finish()
    }

    fun onClickFavorite(){
        if(friend.isFavorite)
        {
            friend.isFavorite = false
            imgFavorite.setImageResource(R.drawable.notok)
        }
        else
        {
            friend.isFavorite = true
            imgFavorite.setImageResource(R.drawable.ok)
        }
    }


}