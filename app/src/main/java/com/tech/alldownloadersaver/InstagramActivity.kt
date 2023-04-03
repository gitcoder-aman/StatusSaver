package com.tech.alldownloadersaver

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.tech.alldownloadersaver.Insta.InstagramVideo
import com.tech.alldownloadersaver.api.CommonClassForAPI
import com.tech.alldownloadersaver.databinding.ActivityInstagramBinding
import com.tech.alldownloadersaver.util.InternetConnection


class InstagramActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInstagramBinding
    var commonClassForAPI: CommonClassForAPI? = null
    var activity:InstagramActivity? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstagramBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        commonClassForAPI = CommonClassForAPI.getInstance(this)
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
                if (url != "" && url.contains("instagram.com")) {

                    binding.progressBar.progress = 0
                    var progressBarStatus = 0
                    var dummy: Int = 0
                    binding.progressBar.visibility = View.VISIBLE
                    InstagramVideo.downloadVideo(this, url)


                    Thread(Runnable {
                        // dummy thread mimicking some operation whose progress can be tracked

                        while (progressBarStatus < 100) {
                            // performing some dummy operation
                            try {
                                dummy += 25
                                Thread.sleep(1000)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                            // tracking progress
                            progressBarStatus = dummy

                            // Updating the progress bar
                            binding.progressBar.progress = progressBarStatus
                        }

                    }).start()
                } else {
                    Toast.makeText(this, "Enter the instagram video link", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onBackPressed() {
        this.finishAffinity()
        startActivity(Intent(this, MainActivity::class.java))
    }

}