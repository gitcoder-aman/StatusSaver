package com.tech.alldownloadersaver.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File


class Util {

    companion object {

        var RootDirectoryWhatsapp = File(
            Environment.getExternalStorageDirectory().toString() + "/Download/MyStorySaver/Whatsapp"
        )

        fun createFileFolder() {
            if (!RootDirectoryWhatsapp.exists()) {
                RootDirectoryWhatsapp.mkdir()
            }
        }

        const val RootDirectoryFacebook: String = "/MyStorySaver/Facebook/"
        const val RootDirectoryShareChat: String = "/MyStorySaver/ShareChat/"
        const val RootDirectoryInstagram: String = "/MyStorySaver/Instagram/"
        const val RootDirectoryYoutube: String = "/MyStorySaver/Youtube/"

        //download method
        fun download(
            downloadPath: String,
            destinationPath: String,
            context: Context,
            fileName: String
        ) {

            Toast.makeText(context, "Download Started", Toast.LENGTH_SHORT).show()
            val uri: Uri = Uri.parse(downloadPath)
            val request: DownloadManager.Request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setTitle(fileName)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                destinationPath+fileName)

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            downloadManager!!.enqueue(request)

            Toast.makeText(context, "Saved to:"+Environment.DIRECTORY_DOWNLOADS+destinationPath+fileName, Toast.LENGTH_SHORT).show()
        }
    }


}

