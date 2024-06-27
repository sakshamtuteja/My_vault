package com.aryan.vault

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_show)
        val imageShow : ImageView = findViewById(R.id.imageShow)

        val url : String? = intent.getStringExtra("URL").toString()
        Glide.with(this).load(url).into(imageShow)
    }
}