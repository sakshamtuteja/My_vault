package com.aryan.vault

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import com.aryan.vault.Modules.UserId
import com.aryan.vault.databinding.ActivityUploadBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUploadBinding
    private lateinit var file:Uri
    private lateinit var fileType:String
    private lateinit var fileName:String
    var userEmail: String? = null
    private lateinit var database : DatabaseReference
    private val storage = FirebaseStorage.getInstance()
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val b:Bundle = intent.extras!!
        fileType= b.getString("UploadType")!!
        file= b.getString("File")!!.toUri()
        if (fileType == "Image") {
            binding.imageView.setImageURI(file)
        }
        else
        {
            binding.imageView.setImageResource(R.drawable.pdf)
        }







        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            userEmail = user.email
        } else {
            // No user is signed in
        }
        val id= userEmail?.substring(0, userEmail!!.indexOf('@'))



        binding.button4.setOnClickListener{
            if (binding.editTextTextPersonName4.text.toString().isNotEmpty() )
            {
                fileName = binding.editTextTextPersonName4.text.toString()
            }
            else {
                fileName= Date().time.toString()
            }
            uploadFile(file,id!!)
           /* database = FirebaseDatabase.getInstance().getReference()
            val usera= UserId(fileName, url)

            //val key=database.child("$id").child("PDF").push().key
            if (fileType == "PDF") {

                database.child("$id").child("PDF").setValue(usera)
            }

            else if (fileType == "Image") {
                database.child("$id").child("IMAGES").push().setValue(usera)
            }
            else{
                Toast.makeText(this,"not supported file", Toast.LENGTH_SHORT).show()
            }*/
finish()
        }


    }
    private fun uploadFile(uri: Uri,id:String) {
        if (fileType=="PDF") {
            val storageRef = storage.reference.child("files/PDF/$fileName")
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show()
                    // taking the public url
                    storageRef.downloadUrl.addOnSuccessListener {
                        url=it.toString()
                        database = FirebaseDatabase.getInstance().getReference()
                        val usera= UserId(fileName, url)
                        database.child("$id").child("PDF").push().setValue(usera)
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show()
                }
        }
        else{
            val storageRef = storage.reference.child("files/Images/$fileName")
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show()
                    // taking the public url
                    storageRef.downloadUrl.addOnSuccessListener {
                       url=it.toString()
                        database = FirebaseDatabase.getInstance().getReference()
                        val usera= UserId(fileName, url)
                        database.child("$id").child("IMAGES").push().setValue(usera)
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show()
                }
        }

        }
    }
