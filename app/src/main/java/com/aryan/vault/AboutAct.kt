package com.aryan.vault

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
private lateinit var logout: Button
private lateinit var img : ImageView
private lateinit var wtext: TextView
var userEmail: String?= null

class AboutAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        img= findViewById(R.id.imageView2)
        img.setImageResource(R.drawable.logo)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            userEmail = user.email
        } else {
            // No user is signed in
        }
        val id= userEmail?.substring(0, userEmail!!.indexOf('@'))

        wtext= findViewById(R.id.welccomeText)
        wtext.setText("Welcome, $id")
       logout = findViewById(R.id.logoutButton)

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            Toast.makeText(this,"Logout Successful",Toast.LENGTH_LONG).show()
        }


    }
}