package com.android.friendapp.GUI

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.android.friendapp.Model.BEFriend
import com.android.friendapp.Model.FriendRepositoryinDB
import com.android.friendapp.Model.Friends
import com.android.friendapp.R
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.File
import java.text.SimpleDateFormat
import com.android.friendapp.Model.observeOnce
import java.util.*
import androidx.lifecycle.Observer
import kotlin.math.*

class DetailActivity : AppCompatActivity() {

    val PHONE_NO = "12345678"
    val TAG = "xyz"
    private val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE = 101
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 102

    var mFile: File? = null
    var overLocation: String = ""

    private lateinit var friend:BEFriend

    //Grabs all information for a specific friend when created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        checkPermissions()
        if (intent.extras != null) {
            val extras: Bundle = intent.extras!!
            /*val name = extras.getString("name")
            val phone = extras.getString("phone")
            val favorite = extras.getBoolean("favorite")*/
            friend = extras.getSerializable("friend") as BEFriend
            tvName.setText(friend.name)
            tvPhone.setText(friend.phone)
            tvEmail.setText(friend.email)
            tvLocation.setText(friend.location)
            dveditTextDate.setText(friend.birthday)
            tvAddress.setText(friend.Address)
            
            if (friend.location != null && friend.location != "")
            {
                //unsplitlocation is the full location of the given friend.
                //delim is the delimiting symbol.
                //splitlocation is the split location derived from unsplit location and the delimiter.
                //freindlatitude is the latitude of the friend.
                //freindlongtitude is the longitude of the friend.
                //freindlatitudeFloat is freindlatitude converted to double.
                //freindlongitudeFloat is freindlongtitude converted to double.
                //Latrounded is the rounded value derived from freindlatitudeFloat.
                //Longrounded is the rounded value derived from freindlongitudeFloat.

                overLocation = friend.location!!
                val unsplitlocation = friend.location
                val delim = ","
                val splitlocation = unsplitlocation!!.split(delim)
                val freindlatitude = splitlocation[0]
                val freindlongtitude = splitlocation[1]
                val freindlatitudeFloat = freindlatitude.toDouble()
                val freindlongitudeFloat = freindlongtitude.toDouble()
                val Latrounded = String.format("%.3f", freindlatitudeFloat)
                val Longrounded = String.format("%.3f", freindlongitudeFloat)
                tvLocation.setText(" ${Latrounded}, ${Longrounded}")
            }

            imgFavorite.setImageResource(if (friend.isFavorite) R.drawable.ok else R.drawable.notok)
            imgFavorite.setOnClickListener{ v -> onClickFavorite()}

            if(friend.pictureFile != null)
            {
                val mImage = findViewById<ImageView>(R.id.imgView)
                val File = File(friend.pictureFile!!)
                mFile = File
                showImageFromFile(mImage, File)
            }


        }
        else
        {
            Log.d("xyz", "system error: intent.extras for detailactivity is null!!!!")
        }
    }

    //Goes back to previous screen after closing the current window.
    fun onClickBack(view: View) { finish() }

    //Saves all the current information to the fitting places, first checks if there is any missing information.
    fun onClickSave(view: View) {
        val mRep = FriendRepositoryinDB.get()
        if(!(tvName.text.isBlank() || tvPhone.text.isBlank() || tvLocation.text.isBlank()))
        {
            //val friendToUpdateIndex = Friends.mFriends.indexOf(Friends.mFriends.find { v -> v.id == friend.id  })
            friend.name = tvName.text.toString()
            friend.phone = tvPhone.text.toString()
            friend.email = tvEmail.text.toString()
            friend.url = tvUrl.text.toString()
            friend.pictureFile = mFile?.absolutePath

            friend.location = tvLocation.text.toString()
            friend.birthday = dveditTextDate.text.toString()
            friend.location = overLocation
            friend.Address = tvAddress.text.toString()
            //Friends.getAll()[friendToUpdateIndex] = friend

            if(friend.id == 0) {
                mRep.insert(friend)
            }
            else{
                mRep.update(friend)
            }

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

    //Deletes selected entry.
    fun onClickDelete(view: View) {
        val mRep = FriendRepositoryinDB.get()
        /*val friendToRemove = Friends.mFriends.find { v -> v.id == friend.id  }
        val yoink = Friends.mFriends.remove(friendToRemove)*/

        mRep.delete(friend)
        //Log.d("xyz", "Delete $yoink")
        finish()
    }

    //Favorites the selected entity, unfavorites if selected is already a favorite.
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

    //Opens the selected URL in a browser
    fun onClickBrowser(view: View) {

        //url is the TvURL converted to string.
        //i is the given intent.

        val url = tvUrl.text.toString()
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    //Opens the default email client with the chosen recipient input into the client.
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

    //Calls the selected entry.
    fun onClickCall(view: View) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + tvPhone.text.toString())
        startActivity(intent)
    }

    //Gets the location for the selected entry.
    @SuppressLint("MissingPermission")
    fun onClickGetLocation(view: View) {
        if (!isPermissionGiven()) {
            tvLocation.setText("No permission given")
            return
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        // The type of location is Location? - it can be null... handle cases

        if (location != null) {
            overLocation = "${location.latitude}, ${location.longitude}"
            val Latrounded = String.format("%.3f", location.latitude)
            val Longrounded = String.format("%.3f", location.longitude)
            tvLocation.setText(" ${Latrounded}, ${Longrounded}")
        } else
            tvLocation.setText("Location = null")
    }

    //Checks if permission is granted to use the various required parts.
    private fun isPermissionGiven(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return permissions.all { p -> checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED}
        }
        return true
    }


    //Starts the standard SMS client.
    private fun startSMSActivity() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("sms:" + tvPhone.text.toString())
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...")
        startActivity(sendIntent)
    }


    //Calls the startSMSActivity method.
    fun onClickSms(view: View) {
        startSMSActivity()
    }

    val permissions = mutableListOf<String>()

    //Checks all permissions.
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        if ( ! isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ) permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if ( ! isGranted(Manifest.permission.CAMERA) ) permissions.add(Manifest.permission.CAMERA)
        if ( ! isGranted(Manifest.permission.ACCESS_FINE_LOCATION) ) permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        if ( ! isGranted(Manifest.permission.ACCESS_COARSE_LOCATION) ) permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permissions.size > 0)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)

    }

    //Checks if the permissions are granted.
    private fun isGranted(permission: String): Boolean =
            ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    fun onClickpicture(view: View) {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)



            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP)




    }

    // show the image [bmap] in the imageview [img] - and put meta data in [txt]
    private fun showImageFromBitmap(img: ImageView, bmap: Bitmap) {
        img.setImageBitmap(bmap)
        //img.setLayoutParams(RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        //img.setBackgroundColor(Color.RED)


    }

    //Grabs the new image and inputs it into the app.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val mImage = findViewById<ImageView>(R.id.imgView)
        when (requestCode) {

            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE ->
                if (resultCode == RESULT_OK)
                    showImageFromFile(mImage, mFile!!)
                else handleOther(resultCode)

            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP ->
                if (resultCode == RESULT_OK) {
                    val extras = data!!.extras
                    val imageBitmap = extras!!["data"] as Bitmap
                    showImageFromBitmap(mImage, imageBitmap)
                } else handleOther(resultCode)
        }
    }

    //Handles cancelling the camera or if the image is not taken.
    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }

    // show the image allocated in [f] in imageview [img]. Show meta data in [txt]
    private fun showImageFromFile(img: ImageView,  f: File) {
        img.setImageURI(Uri.fromFile(f))
        img.setBackgroundColor(Color.RED)
        //mImage.setRotation(90);

    }

    //Creates a file to save an image, checks whether or not it can.
    fun onTakeByFile(view: View) {
        mFile = getOutputMediaFile("Camera01") // create a file to save the image

        if (mFile == null) {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show()
            return
        }


        // create Intent to take a picture
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val applicationId = "com.android.friendapp"
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                this,
                "${applicationId}.provider",  //use your app signature + ".provider"
                mFile!!))

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE)
        } else Log.d(TAG, "camera app could NOT be started")

    }

    // return a new file with a timestamp name in a folder named [folder] in
    // the external directory for pictures.
    // Return null if the file cannot be created
    private fun getOutputMediaFile(folder: String): File? {
        // in an emulated device you can see the external files in /sdcard/Android/data/<your app>.
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory")
                return null
            }
        }

        // Create a media file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val postfix = "jpg"
        val prefix = "IMG"
        return File(mediaStorageDir.path +
                File.separator + prefix +
                "_" + timeStamp + "." + postfix)
    }

    //Clicks the distance button on selected friend, gives distance in meter.
    @SuppressLint("MissingPermission")
     fun onClickDistance(view: View){
        val unsplitlocation = overLocation
        if(!unsplitlocation.isEmpty()){
            val delim = ","
            val splitlocation = unsplitlocation.split(delim)
            Log.d(TAG, splitlocation.toString())
            val freindlatitude = splitlocation[0]

            Log.d(TAG, splitlocation[0])
            Log.d(TAG, splitlocation[1])
            val freindlongitude  = splitlocation[1]

            //convert to float
            val freindlatitudeFloat = freindlatitude.toFloatOrNull()
            val freindlongitudeFloat = freindlongitude.toFloatOrNull()

            if(freindlatitudeFloat == null || freindlongitudeFloat == null ){

                return
            }

            if (!isPermissionGiven()) {
                tvLocation.setText("No permission given")
                return
            }

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            val latitude = location?.latitude
            val longitude = location?.longitude



            val R = 6371e3; // metres
            val φ1 = freindlatitudeFloat * Math.PI/180; // φ, λ in radians
            val φ2 = (latitude?.times(Math.PI) ?: 0.0 ) /180;
            val Δφ = ((latitude?.minus(freindlatitudeFloat))?.times(Math.PI) ?: 0.0) /180;
            var Δλ = 0.0
            if (longitude != null) {
                 Δλ = (longitude-freindlongitudeFloat) * Math.PI/180
            }

            val a = sin(Δφ/2) * sin(Δφ/2) +
                    cos(φ1) * cos(φ2) *
                    sin(Δλ/2) * sin(Δλ/2)
            val c = 2 * atan2(sqrt(a), sqrt(1-a));

            val d = R * c; // in metres

            tvDistance.text = d.toString()





        }


    }

    fun onClickShow(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("friend", friend)
        startActivity(intent)
    }

}




