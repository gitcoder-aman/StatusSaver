package com.tech.alldownloadersaver.fragment

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tech.alldownloadersaver.adapter.WhatsappAdapter
import com.tech.alldownloadersaver.model.WhatsappStatusModel
import com.tech.alldownloadersaver.databinding.FragmentVideoBinding
import java.io.File
import java.util.*
import kotlin.Comparator

class VideoFragment : Fragment() {

    private lateinit var binding: FragmentVideoBinding
    private lateinit var list: ArrayList<WhatsappStatusModel>
    private lateinit var adapter: WhatsappAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentVideoBinding.inflate(inflater,container,false)

        list = ArrayList()
        getData()

        binding.refreshLayout.setOnRefreshListener {

            list = ArrayList()
            getData()
            binding.refreshLayout.isRefreshing = false
        }
        return binding.root
    }

    private fun getData() {

        var model:WhatsappStatusModel

        try {
            val pm = requireContext().packageManager
            pm.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES)

            val targetPath:String = Environment.getExternalStorageDirectory().absolutePath+"/Whatsapp/Media/.statuses"
            val targetDirector:File = File(targetPath)
            val allFiles: Array<out File>? = targetDirector.listFiles()


            //sort status which status is new come then show status forward  for normal whatsapp
            if (allFiles != null) {
                Arrays.sort<File>(allFiles,
                    Comparator<File> { o1: File, o2: File ->
                        if (o1.lastModified() > o2.lastModified())
                            return@Comparator -1
                        else if (o1.lastModified() < o2.lastModified())
                            return@Comparator +1
                        else return@Comparator 0
                    })
            }

            for (i in allFiles?.indices!!) {

                val file:File = allFiles[i]
                if(Uri.fromFile(file).toString().endsWith(".mp4")){
                    model = WhatsappStatusModel("whats $i",
                        Uri.fromFile(file),
                        allFiles[i].absolutePath,
                        file.name)

                    list.add(model)
                }
            }

        } catch (e: PackageManager.NameNotFoundException) {

            e.printStackTrace()
        }

        try {
            //for Business whatsapp
            val pm = requireContext().packageManager
            pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES)
            val targetPathBusiness:String = Environment.getExternalStorageDirectory().absolutePath+"/Whatsapp/Media/.statuses"
            val targetDirectorBusiness:File = File(targetPathBusiness)
            val allFilesBusiness: Array<out File>? = targetDirectorBusiness.listFiles()

            //sort status which status is new come then show status forward  for normal whatsapp
            if (allFilesBusiness != null) {
                Arrays.sort<File>(allFilesBusiness,
                    Comparator<File> { o1: File, o2: File ->
                        if (o1.lastModified() > o2.lastModified())
                            return@Comparator -1
                        else if (o1.lastModified() < o2.lastModified())
                            return@Comparator +1
                        else return@Comparator 0
                    })
            }

            for (i in allFilesBusiness?.indices!!) {

                val file:File = allFilesBusiness[i]
                if(Uri.fromFile(file).toString().endsWith(".mp4")){
                    model = WhatsappStatusModel("whatsBusiness $i",
                        Uri.fromFile(file),
                        allFilesBusiness[i].absolutePath,
                        file.name)

                    list.add(model)
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {

            e.printStackTrace()
        }
        adapter = WhatsappAdapter(list,context)
        binding.whatsappRecycler.adapter = adapter
    }

}