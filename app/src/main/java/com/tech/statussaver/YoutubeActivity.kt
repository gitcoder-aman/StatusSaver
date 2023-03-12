package com.tech.statussaver

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tech.statussaver.databinding.ActivityYoutubeBinding
import com.tech.statussaver.util.InternetConnection
import com.tech.statussaver.util.Util


class YoutubeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityYoutubeBinding
    private lateinit var dialog:BottomSheetDialog
    private lateinit var urlLink:String

    companion object {
        var activity = YoutubeActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYoutubeBinding.inflate(layoutInflater)
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

            binding.youtubeUrl.setText(textItem)
       }

        binding.downloadBtn.setOnClickListener {

            urlLink= binding.youtubeUrl.text.toString()


            if(InternetConnection.isNetworkAvailable(this)) {

                if (urlLink != "") {
                    if (urlLink.contains("yout")) {

                        showBottomSheet(urlLink)
                    }
                    else{
                        Toast.makeText(this, "Only youtube link support", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Enter the youtube videos link", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, resources.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun downloadYoutubeVideo(urlLink: String, formatTag: Int?) {
        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {
                if (ytFiles != null) {
                    Log.d("@@@@","yt not null")

                        val downloadUrl = ytFiles[formatTag!!].url

                        if (downloadUrl != null) {
                            Util.download(
                                downloadUrl,
                                Util.RootDirectoryYoutube,
                                activity,
                                "youtube" + System.currentTimeMillis() + ".mp4"
                            )
                        } else {
                            Log.d("@@@@", "download URL Null")
                            Toast.makeText(
                                this@YoutubeActivity,
                                "something went wrong.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }else{
                    Log.d("@@@@,","ytfile null")
                    Toast.makeText(this@YoutubeActivity, "something went wrong.", Toast.LENGTH_SHORT).show()
                }
            }
        }.extract(urlLink)
    }

    private fun showBottomSheet(urlLink: String) {
        val dialogView = layoutInflater.inflate(R.layout.youtube_bottom_sheet,null)
        dialog = BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
        dialog.show()

        var typeMapList = HashMap<String, Int>()

        typeMapList.clear()
        typeMapList = HashMap(4)
        typeMapList["1080P"] = 137
        typeMapList["720P"] = 22
        typeMapList["360P"] = 18

        val btn1 :Button = dialogView.findViewById(R.id.downloadBtn1) //1080
        val btn2 :Button = dialogView.findViewById(R.id.downloadBtn2) //720
        val btn3 :Button = dialogView.findViewById(R.id.downloadBtn3) //360

        val thumbnailImg :ImageView = dialogView.findViewById(R.id.thumbnailImg)

        Log.d("@@@@",urlLink)
//        https://www.youtube.com/watch?v=WFpqZYHeOnc  43
//        https://youtu.be/4YxwCJ3lqzI
        var videoId:String?=null

        if(urlLink.contains("www.youtube.com")){
             videoId = urlLink.subSequence(32,urlLink.length).toString()
        }else if(urlLink.contains("youtu.be")){
            videoId = urlLink.subSequence(17,urlLink.length).toString()
        }


        Glide.with(this)
            .load("https://img.youtube.com/vi/$videoId/0.jpg")
            .placeholder(R.drawable.ic_launcher_background)
            .into(thumbnailImg)

        btn1.setOnClickListener {
            val formatTag = typeMapList[btn1.text.toString()]
            showProgressBar()
            dialog.dismiss()
            downloadYoutubeVideo(urlLink,formatTag)
        }
        btn2.setOnClickListener {
            val formatTag = typeMapList[btn2.text.toString()]
            showProgressBar()
            dialog.dismiss()
            downloadYoutubeVideo(urlLink,formatTag)
        }
        btn3.setOnClickListener {
            val formatTag = typeMapList[btn3.text.toString()]
            showProgressBar()
            dialog.dismiss()
            downloadYoutubeVideo(urlLink,formatTag)
        }

    }

    private fun showProgressBar() {
        binding.progressBar.progress = 0
        var progressBarStatus = 0
        var dummy:Int = 0
        binding.progressBar.visibility = View.VISIBLE
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
    }

    override fun onBackPressed() {
        this.finishAffinity()
        startActivity(Intent(this,MainActivity::class.java))
    }
}