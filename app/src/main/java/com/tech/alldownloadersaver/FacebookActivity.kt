package com.tech.alldownloadersaver

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tech.alldownloadersaver.util.Util
import com.tech.alldownloadersaver.databinding.ActivityFacebookBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL


open class FacebookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacebookBinding

    companion object {
        var activity = FacebookActivity()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacebookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        binding.backArrow.setOnClickListener {
            this.finish()
            startActivity(Intent(this,MainActivity::class.java))
        }
        binding.pasteBtn.setOnClickListener {
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val item: ClipData.Item? = clipboard.primaryClip?.getItemAt(0)
            val textItem: String = item?.text.toString()

            binding.facebookUrl.setText(textItem)
        }
        binding.downloadBtn.setOnClickListener {
            val url:String = binding.facebookUrl.text.toString()
            Log.d("@@@@",url)

            getFacebookData(url)
        }

    }


    private fun getFacebookData(urlLink:String) {

        if(urlLink==null && urlLink.equals("")){
            return
        }
        val url:URL = URL(urlLink)
        val host= url.host
        if(host.contains("facebook.com")||host.contains("fb.watch")){
            Log.d("@@@@","host Contains")
            callGetFbData().execute(urlLink)
        }else{
            Toast.makeText(this, "Url is Invalid", Toast.LENGTH_SHORT).show()
        }
//        object : FacebookVideo(this, urlLink, false) {
//            override fun onExtractionComplete(facebookFile: FacebookModel) {
//                Log.e("@@@@", "---------------------------------------")
//                Log.e("@@@@", "facebookFile AutherName :: " + facebookFile.author)
//                Log.e("@@@@", "facebookFile FileName :: " + facebookFile.filename)
//                Log.e("@@@@", "facebookFile Ext :: " + facebookFile.ext)
//                Log.e("@@@@", "facebookFile SD :: " + facebookFile.sdUrl)
//                Log.e("@@@@", "facebookFile HD :: " + facebookFile.hdUrl)
//                Log.e("@@@@", "---------------------------------------")
//            }
//
//            override fun onExtractionFail(error: Exception) {
//                Log.e("@@@@", "Error :: " + error.message)
//                error.printStackTrace()
//            }
//        }
    }

    class callGetFbData : AsyncTask<String, Void, Document>() {

       private lateinit var fbDoc:Document
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: String?): Document {
            fbDoc = Jsoup.connect(p0[0]!!).get()

            Log.d("@@@@", "return $fbDoc")
            return fbDoc
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: Document?) {
            Log.d("@@@@", "OnPostExecute")

            val videoUrl: String? = result?.select("meta[property=\"og:video\"]")?.last()?.attr("content")
            val desc: String? = result?.select("meta[property=\"og:description\"]")?.last()?.attr("content")
            val title: String? = result?.select("meta[property=\"og:title\"]")?.last()?.attr("content")
            Log.d("@@@@", "description$desc")
            Log.d("@@@@", "title$title")

            if(!videoUrl.equals("")){
                Log.d("@@@@", "Video URL$videoUrl"+"Not empty")
                if (videoUrl != null) {
                    Log.d("@@@@", "Video URL$videoUrl"+"Not null")
                    Util.download(videoUrl,Util.RootDirectoryFacebook,activity,"facebook "+System.currentTimeMillis()+".mp4")
                }else{
                    Log.d("@@@@", "Video URL$videoUrl null")
                    Toast.makeText(activity, "Video not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}