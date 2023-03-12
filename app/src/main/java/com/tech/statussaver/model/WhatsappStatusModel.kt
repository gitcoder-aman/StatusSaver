package com.tech.statussaver.model

import android.net.Uri

class WhatsappStatusModel(name: String, uri: Uri, path: String, fileName: String) {
    private var name : String?= name
    private var uri : Uri ?= uri
    private var path :String?= path
    private var fileName : String?= fileName

    fun setName(name:String?){
        this.name = name
    }
    fun getName():String?{
        return name
    }
    fun setUri(uri: Uri){
        this.uri = uri
    }
    fun getUri():Uri?{
        return uri
    }
    fun setPath(path: String){
        this.path = path
    }
    fun getPath():String?{
        return path
    }
    fun setFileName(fileName: String){
        this.fileName = fileName
    }
    fun getFileName():String?{
        return fileName
    }

}