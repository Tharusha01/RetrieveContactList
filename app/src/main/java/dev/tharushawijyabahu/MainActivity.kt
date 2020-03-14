package dev.tharushawijyabahu

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    var listView: ListView? = null
    var StoreContacts: ArrayList<String>? = null
    var arrayAdapter: ArrayAdapter<String>? = null
    var button:Button? = null

    var name: String? = null
    var phonenumber:String? = null

    val RequestPermissionCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listview1) as ListView
        button = findViewById(R.id.button1) as Button
        StoreContacts = ArrayList()

        EnableRuntimePermission()

        button!!.setOnClickListener {
            GetContactsIntoArrayList()
            arrayAdapter = ArrayAdapter(
                this@MainActivity,
                R.layout.contact_items_listview,
                R.id.textView, StoreContacts!!
            )
            listView!!.adapter = arrayAdapter
        }

    }

    fun GetContactsIntoArrayList() {
        val cursor : Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                phonenumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                StoreContacts!!.add("$name : $phonenumber")
            }
        }
        cursor?.close()
    }

    fun EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.READ_CONTACTS
            )
        ) {
            Toast.makeText(
                this@MainActivity,
                "CONTACTS permission allows us to Access CONTACTS app",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(
                    Manifest.permission.READ_CONTACTS
                ), RequestPermissionCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RequestPermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this@MainActivity,
                        "Permission Granted, Now your application can access CONTACTS.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Permission Canceled, Now your application cannot access CONTACTS.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}
