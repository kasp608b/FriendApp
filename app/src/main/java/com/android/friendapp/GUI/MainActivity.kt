package com.android.friendapp.GUI

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.friendapp.Model.BEFriend
import com.android.friendapp.Model.FriendRepositoryinDB
import com.android.friendapp.Model.observeOnce
import com.android.friendapp.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity()  {
    /**
     * list of friends
     */
    var cache: List<BEFriend>? = null;


    /**
     * sets up the detail on start up and initializes the database
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FriendRepositoryinDB.initialize(this)
        refresh()
    }


    /**
     * Sets up the the detail activity for the chosen friend and starts the detail activity
     */
    fun onListItemClick(view: View) {
        val mRep = FriendRepositoryinDB.get()

        val friend = view.tag as BEFriend

                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("friend", friend)

                startActivity(intent)
    }

    /**
     * Refreshes the friend list by comparing the cache's list with the databases list of friends
     */
    private fun refresh(){
        val mRep = FriendRepositoryinDB.get()
        val nameObserver = Observer<List<BEFriend>>{ persons ->
            cache = persons;
            val asArray = persons.toTypedArray()
            val adapter: ListAdapter = FriendAdapter(
                    this,
                    asArray
            )
            friendList.adapter = adapter
        }
        mRep.getAll().observe(this, nameObserver)

        friendList.onItemClickListener = AdapterView.OnItemClickListener { _, view, pos, _ -> onListItemClick(view)}

       // mRep.insert(BEFriend(0,"jake","444444'",false,"b@hotmail.com"  , "https://www.msn.com/da-dk/" , null ))
    }

    /**
     * Sets up the activity view for creating a new friend and starts it
     */
     fun onClickCreate(view: View){
         val intent = Intent(this, DetailActivity::class.java)
         val friend = BEFriend(0, "DefaultName", "DefaultPhoneNumber", false, "DefaultEmail@hotmail.com", "https://www.msn.com/da-dk/", null, null ,"day/month/year", null)

         /* val mRep = FriendRepositoryinDB.get()
         mRep.insert(friend)*/

         //Friends.getAll().add(friend)
         //Friends.nextId++
         intent.putExtra("friend", friend)
         /*intent.putExtra("name", friend.name )
         intent.putExtra("phone", friend.phone)
         intent.putExtra("favorite", friend.isFavorite)*/
         startActivity(intent)
    }

    /**
     * launches the map activity
     */
    fun onClickMap(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    /**
     * refreshes the main activity each time it starts
     */
    override fun onStart() {
        super.onStart()
        refresh()
    }

    internal class FriendAdapter(context: Context,
                                 private val friends: Array<BEFriend>
    ) : ArrayAdapter<BEFriend>(context, 0, friends)
    {
        private val colours = intArrayOf(
                Color.parseColor("#AAAAAA"),
                Color.parseColor("#CCCCCC")
        )

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.cell_extended, null)

            }
            val resView: View = v1!!
            resView.setBackgroundColor(colours[position % colours.size])
            val f = friends[position]
            val nameView = resView.findViewById<TextView>(R.id.tvNameExt)
            val favoriteView = resView.findViewById<ImageView>(R.id.imgFavoriteExt)
            val pictureView = resView.findViewById<ImageView>(R.id.FriendPicture)
            nameView.text = f.name

            if(f.pictureFile != null)
            {
                val File = File(f.pictureFile!!)
                showImageFromFile(pictureView, File)
            }
            resView.tag = f

            favoriteView.setImageResource(if (f.isFavorite) R.drawable.ok else R.drawable.notok)
            return resView
        }

        // show the image allocated in [f] in imageview [img]. Show meta data in [txt]
        private fun showImageFromFile(img: ImageView,  f: File) {
            img.setImageURI(Uri.fromFile(f))
            img.setBackgroundColor(Color.RED)
            //mImage.setRotation(90);

        }
    }



}