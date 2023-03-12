package com.tech.statussaver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide

class StatusViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_view)

        val imageView = findViewById<ImageView>(R.id.viewImage)
        val backArrow = findViewById<ImageView>(R.id.backArrow)

        val imgUri: String? = intent.getStringExtra("imageUri")

        Log.d("@@@@",imgUri.toString())
        Glide.with(this).load(imgUri).into(imageView)

        backArrow.setOnClickListener {
            this.finish()
            startActivity(Intent(this,WhatsappActivity::class.java))
        }
    }
    override fun onBackPressed() {
        this.finish()
        startActivity(Intent(this,WhatsappActivity::class.java))
    }
}