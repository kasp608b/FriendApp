package com.android.friendapp.GUI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.lifecycle.Observer
import com.android.friendapp.Model.BEFriend
import com.android.friendapp.Model.FriendRepositoryinDB
import com.android.friendapp.Model.Friends
import com.android.friendapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()  {
    var cache: List<BEFriend>? = null;
    val TAG = "xyz"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FriendRepositoryinDB.initialize(this)
        refresh()
    }


    fun onListItemClick( position: Int ) {
        val mRep = FriendRepositoryinDB.get()




        val intent = Intent(this, DetailActivity::class.java)

        intent.putExtra("friend", cache!![position])

        startActivity(intent)

    }

    private fun refresh(){
        val mRep = FriendRepositoryinDB.get()
        val nameObserver = Observer<List<BEFriend>>{ persons ->
            cache = persons;
            val asStrings = persons.map { p -> "${p.id}, ${p.name}"}
            val adapter: ListAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                asStrings.toTypedArray()
            )
            friendList.adapter = adapter
        }
        mRep.getAll().observe(this, nameObserver)

        friendList.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ -> onListItemClick(pos)}

       // mRep.insert(BEFriend(0,"jake","444444'",false,"b@hotmail.com"  , "https://www.msn.com/da-dk/" , null ))
    }

     fun onClickCreate(view: View){
         val intent = Intent(this, DetailActivity::class.java)
         val friend = BEFriend(0, "DefaultName", "DefaultPhoneNumber", false, "DefaultEmail@hotmail.com", "https://www.msn.com/da-dk/", null)
         val mRep = FriendRepositoryinDB.get()
         mRep.insert(friend)
         //Friends.getAll().add(friend)
         //Friends.nextId++
         intent.putExtra("friend", friend)
         /*intent.putExtra("name", friend.name )
         intent.putExtra("phone", friend.phone)
         intent.putExtra("favorite", friend.isFavorite)*/
         startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        refresh()
    }

}