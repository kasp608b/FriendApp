package com.android.friendapp.GUI

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.friendapp.Model.BEFriend
import com.android.friendapp.Model.Friends
import com.android.friendapp.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    val PHONE_NO = "12345678"
    val TAG = "xyz"
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
            tvEmail.setText(friend.email)
            tvUrl.setText(friend.url)
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
            friend.email = tvEmail.text.toString()
            friend.url = tvUrl.text.toString()
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

    fun onClickBrowser(view: View) {
        val url = tvUrl.text.toString()
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
    fun onClickEmail(view: View) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "plain/text"
        val receivers = arrayOf(tvEmail.toString())
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test")
        emailIntent.putExtra(Intent.EXTRA_TEXT,
            "Hej, Hope that it is ok, Best Regards android...;-)")
        startActivity(emailIntent)
    }
    fun onClickCall(view: View) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + tvPhone.text.toString())
        startActivity(intent)
    }


    private fun startSMSActivity() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("sms:" + tvPhone.text.toString())
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...")
        startActivity(sendIntent)
    }



    fun onClickSms(view: View) {
        startSMSActivity()
    }


}