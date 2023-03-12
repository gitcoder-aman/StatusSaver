package com.tech.statussaver

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.tech.statussaver.Insta.InstagramVideo
import com.tech.statussaver.databinding.ActivityInstagramBinding
import com.tech.statussaver.util.InternetConnection

class InstagramActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInstagramBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstagramBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            this.finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.pasteBtn.setOnClickListener {
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val item: ClipData.Item? = clipboard.primaryClip?.getItemAt(0)
            val textItem: String = item?.text.toString()

            binding.instaUrl.setText(textItem)
        }

        binding.downloadBtn.setOnClickListener {

            val url: String = binding.instaUrl.text.toString()
            if (InternetConnection.isNetworkAvailable(this)) {
                InstagramVideo.downloadVideo(this, url)
            }else{
                Toast.makeText(this, resources.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        this.finishAffinity()
        startActivity(Intent(this, MainActivity::class.java))
    }

}