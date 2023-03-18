package com.tech.alldownloadersaver

import android.content.ContentValues
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.tech.alldownloadersaver.api.CommonClassForAPI
import com.tech.alldownloadersaver.databinding.ActivityFacebookBinding
import com.tech.alldownloadersaver.util.Util
import com.tech.alldownloadersaver.util.Util.Companion.RootDirectoryFacebook
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class FacebookActivity : AppCompatActivity() {
   private lateinit var binding: ActivityFacebookBinding
    lateinit var activity: FacebookActivity
    private var commonClassForAPI: CommonClassForAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacebookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        commonClassForAPI = CommonClassForAPI.getInstance(activity)

        binding.downloadBtn.setOnClickListener {
            GetFacebookData()
        }
    }

    private fun GetFacebookData() {
        try {
            val url = URL(binding.facebookUrl.text.toString())
            val host = url.host
            Log.e("initViews: ", host)
            if (host.contains("facebook.com")) {
                Log.d("@@@@","Facebook url right")
                    callGetFacebookData().execute(binding.facebookUrl.text.toString())
            } else {
                Toast.makeText(this, "Enter facebook url only", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    internal inner class callGetFacebookData :
        AsyncTask<String?, Void?, Document?>() {
        var facebookDoc: Document? = null
        override fun doInBackground(vararg p0: String?): Document? {
            try {
                facebookDoc = Jsoup.connect(p0[0]).get()
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d(ContentValues.TAG, "doInBackground: Error")
            }
            Log.d("@@@@", "facebookDoc$facebookDoc")
            return facebookDoc
        }

        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: Document?) {
            try {
                var videoUrl:String = result!!.select("meta[property=\"og:video\"]").last()!!.attr("content")
                Log.d("@@@@", "video$videoUrl")
                Log.e("onPostExecute: ", videoUrl)
                if (videoUrl != "") {
                    try {
                        Log.d("@@@@", "video$videoUrl")

                        Util.download(videoUrl, RootDirectoryFacebook, activity, getFilenameFromURL(videoUrl));

                        videoUrl = ""
                        binding.facebookUrl.setText("")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: NullPointerException) {
                Log.d("@@@@",e.printStackTrace().toString())
                e.printStackTrace()
            }
        }
    }

    fun getFilenameFromURL(url: String?): String {
        return try {
            File(URL(url).path).name + ".mp4"
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            System.currentTimeMillis().toString() + ".mp4"
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        this.finishAffinity()
        startActivity(Intent(this, MainActivity::class.java))
    }
}