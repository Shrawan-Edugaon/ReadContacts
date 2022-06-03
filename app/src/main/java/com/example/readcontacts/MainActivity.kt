package com.example.readcontacts

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val READ_CONTACTS_PERMISSION_CODE = 1
    lateinit var contacts:ArrayList<String>
    lateinit var btnReadContacts:Button
    lateinit var phoneContacts:Button
    lateinit var phoneContactsList:ListView
    lateinit var cursor:Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnReadContacts = findViewById(R.id.readContactsButton)

        phoneContactsList = findViewById(R.id.contactList)

        btnReadContacts.setOnClickListener {

            checkPermission()
        }
    }

    private fun checkPermission() {

        val checkPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)
        if (checkPermission == PackageManager.PERMISSION_GRANTED)
        {
            phoneContactsDetails()
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS),READ_CONTACTS_PERMISSION_CODE)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            READ_CONTACTS_PERMISSION_CODE ->{
                if (grantResults.size >=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    phoneContactsDetails()
                }
                else
                {
                    Toast.makeText(applicationContext,"You don't have permission",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


//    @SuppressLint("Range")
    private fun phoneContactsDetails() {
        cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.Contacts.DISPLAY_NAME + "ASC")!!

        contacts = ArrayList()

        while (cursor.moveToNext())
        {
            val contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            contacts.add("Name:$contactName \n PhoneNo :$phoneNumber")
        }
    cursor.close()

    val ContactsAdapter = ArrayAdapter<String>(this@MainActivity,android.R.layout.simple_list_item_1,contacts)
    phoneContactsList.adapter = ContactsAdapter
    }


}