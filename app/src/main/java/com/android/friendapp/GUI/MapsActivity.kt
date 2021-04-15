package com.android.friendapp.GUI

import android.Manifest
import android.R.attr.src
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.android.friendapp.Model.BEFriend
import com.android.friendapp.Model.FriendRepositoryinDB
import com.android.friendapp.Model.observeOnce
import com.android.friendapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    /**
     * saves an instances of the google maps class
     */
    private lateinit var mMap: GoogleMap

    /**
     * keep track on the last clicked marker
     */
    private var mLastIndex = -1

    /**
     * Sets up the activity when it is started
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap)  {
        mMap = googleMap
        if (intent.extras == null)
        {
            val mRep = FriendRepositoryinDB.get()
            val friendObserver = Observer<List<BEFriend>>{ friends ->
                val list = friends;
                var i = 2
                for (friend: BEFriend in list)
                {
                    if (friend.location !== null && friend.location != "")
                    {
                        val unsplitlocation = friend.location
                        val delim = ","
                        val splitlocation = unsplitlocation!!.split(delim)
                        val freindlatitude = splitlocation[0]
                        val freindlongitude = splitlocation[1]
                        val freindlatitudeFloat = freindlatitude.toDouble()
                        val freindlongitudeFloat = freindlongitude.toDouble()
                        val location = LatLng(freindlatitudeFloat, freindlongitudeFloat)
                        val marker = MarkerOptions().position(location).title("${friend.id}, ${friend.name}")
                        val locationMarker = mMap.addMarker(marker)
                        locationMarker.tag = i
                        i++

                    }
                }
            }

            mRep.getAll().observe(this, friendObserver)
            mMap.setOnMarkerClickListener(this)

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return
            }

            val myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val myLatLng: LatLng
            if(myLocation != null)
            {
                myLatLng = LatLng(myLocation.latitude, myLocation.longitude)
                val myMarker = MarkerOptions().position(myLatLng).title("My Location")
                val myLocationMarker = mMap.addMarker(myMarker)
                myLocationMarker.tag = 1
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng))
            }

        }
        else
        {
            mMap.setOnMarkerClickListener(this)

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return
            }

            val myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val myLatLng: LatLng
            if(myLocation != null)
            {
                myLatLng = LatLng(myLocation.latitude, myLocation.longitude)
                val myMarker = MarkerOptions().position(myLatLng).title("My Location")
                val myLocationMarker = mMap.addMarker(myMarker)
                myLocationMarker.tag = 1
            }

            val friend = intent.extras!!.getSerializable("friend") as BEFriend
            val unsplitlocation = friend.location
            val delim = ","
            val splitlocation = unsplitlocation!!.split(delim)
            val freindlatitude = splitlocation[0]
            val freindlongtitude = splitlocation[1]
            val friendLatLng = LatLng(freindlatitude.toDouble(), freindlongtitude.toDouble())
            val friendMarker = MarkerOptions().position(friendLatLng).title("${friend.id}, ${friend.name}")
            val friendLocationMarker = mMap.addMarker(friendMarker)
            friendLocationMarker.tag = 2
            mMap.moveCamera(CameraUpdateFactory.newLatLng(friendLatLng))


        }






        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        //setupZoomlevels()
    }

   /* private fun setupZoomlevels() {
        spinnerZoomLevel.adapter =
            ArrayAdapter.createFromResource(
                this,
                R.array.zoomlevels,
                android.R.layout.simple_spinner_dropdown_item
            )
    }*/

    /**
     *  goes back to the previous activity
     */
    fun onClickBack(view: View) { finish() }

    /**
     * handles what happens when a user clicks a marker on the map
     * when a marker is clicked the first time wil show the info of the marker
     * if the same marker is clicked twice it will open the detail activity for the friend linked to the marker
     */
    override fun onMarkerClick(p0: Marker?): Boolean {

        if (p0 != null)
        {

                var index = p0.tag as Int
                if(mLastIndex != index)
                {
                    p0.showInfoWindow()
                    mLastIndex = index
                }
                else
                {
                    if (p0.tag != 1)
                    {
                        p0.hideInfoWindow()
                        mLastIndex = -1
                        val mRep = FriendRepositoryinDB.get()
                        val valueOnList = p0.title?.substring(0, p0.title.indexOf(","))
                        val id = valueOnList?.toInt()
                        val friendObserver = Observer<BEFriend> { friend ->
                            if (friend != null)
                            {
                                val intent = Intent(this, DetailActivity::class.java)
                                intent.putExtra("friend", friend)

                                startActivity(intent)

                            }

                        }

                        if (id != null)
                        {
                            mRep.getById(id).observeOnce(this, friendObserver)
                            return true
                        }
                        return false
                    }

                }


        }
        return false


    }
}