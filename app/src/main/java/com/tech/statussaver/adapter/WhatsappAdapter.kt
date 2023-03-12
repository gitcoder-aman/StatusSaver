package com.tech.statussaver.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tech.statussaver.R
import com.tech.statussaver.StatusViewActivity
import com.tech.statussaver.model.WhatsappStatusModel
import com.tech.statussaver.util.InternetConnection
import com.tech.statussaver.util.Util
import org.apache.commons.io.FileUtils
import java.io.File

class WhatsappAdapter(
    private val list: ArrayList<WhatsappStatusModel>,
    private val context: Context?
) : RecyclerView.Adapter<WhatsappAdapter.ViewHolder>() {

    private var saveFilePath:String = Util.RootDirectoryWhatsapp.toString()+"/"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.whatsapp_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemModel:WhatsappStatusModel = list[position]
        if(itemModel.getUri().toString().endsWith(".mp4")){
            holder.playBtn.visibility = View.VISIBLE
        }else{
            holder.playBtn.visibility = View.GONE
        }

        if (context != null) {
            Glide.with(context)
                .load(itemModel.getUri())
                .into(holder.statusImage)
        }

        holder.downloadBtn.setOnClickListener {

            if(InternetConnection.isNetworkAvailable(context!!)){
                Util.createFileFolder()

                val path:String = itemModel.getPath().toString()
                val file:File = File(path)

                //destination file
                val destFile:File = File(saveFilePath)

                FileUtils.copyFileToDirectory(file,destFile)
                Toast.makeText(context, "Saved to : $saveFilePath", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_SHORT).show()
            }

        }

        holder.itemView.setOnClickListener {

            if(itemModel.getUri().toString().contains(".jpg")){
                val intent = Intent(context, StatusViewActivity::class.java)
                intent.putExtra("imageUri",itemModel.getUri().toString())
                context?.startActivity(intent)
            }else if(itemModel.getUri().toString().contains(".mp4")){

            }
        }
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val playBtn:ImageView
         val statusImage:ImageView
         val downloadBtn:AppCompatButton

         init {
             playBtn = view.findViewById(R.id.playBtn)
             statusImage = view.findViewById(R.id.statusImage)
             downloadBtn = view.findViewById(R.id.downloadBtn)
         }

    }

}