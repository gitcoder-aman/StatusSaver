package com.tech.statussaver

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tech.statussaver.Insta.InstagramVideo
import com.tech.statussaver.databinding.ActivityShareChatBinding
import com.tech.statussaver.util.InternetConnection
import com.tech.statussaver.util.Util
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL

class ShareChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShareChatBinding

    companion object {
        var activity = ShareChatActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShareChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this

        binding.backArrow.setOnClickListener {
            this.finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.pastBtn.setOnClickListener {
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val item: ClipData.Item? = clipboard.primaryClip?.getItemAt(0)
            val textItem: String = item?.text.toString()

            binding.sharechatUrl.setText(textItem)
        }
        binding.downloadBtn.setOnClickListener {
            val url: String = binding.sharechatUrl.text.toString()
            if (InternetConnection.isNetworkAvailable(this)) {
                if(url!=""){
                    getShareChatData(url)
                }else{
                    Toast.makeText(this, "Enter the snapchat videos link", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this, resources.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getShareChatData(urlLink: String) {
        val url: URL = URL(urlLink)
        val host: String = url.host
        if (host.contains("sharechat")) {
            Log.d("@@@@", "host Contains")
            callGetFbData().execute(urlLink)
        }else{
            Toast.makeText(this, "only share chat video link support", Toast.LENGTH_SHORT).show()
        }

    }

    class callGetFbData : AsyncTask<String, Void, Document>() {

        private lateinit var fbDoc: Document

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: String?): Document {
            fbDoc = Jsoup.connect(p0[0]!!).get()

            Log.d("@@@@", "return $fbDoc")
            return fbDoc
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: Document?) {
            Log.d("@@@@", "OnPostExecute")

            val videoUrl: String? =
                result?.select("meta[property=\"og:video:secure_url\"]")?.last()?.attr("content")
            if (!videoUrl.equals("")) {
                Log.d("@@@@", "Video URL$videoUrl" + "Not empty")
                if (videoUrl != null) {
                    Log.d("@@@@", "Video URL$videoUrl" + "Not null")
                    Util.download(
                        videoUrl,
                        Util.RootDirectoryShareChat,
                        activity,
                        "sharechat " + System.currentTimeMillis() + ".mp4"
                    )
                } else {
                    Log.d("@@@@", "Video URL$videoUrl null")
                    Toast.makeText(activity, "Video not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        this.finishAffinity()
        startActivity(Intent(this, MainActivity::class.java))
    }
}