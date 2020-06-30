package com.egorsigolaev.downloader

import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class InstagramDownloader: Downloader {

    interface SNInstagramPhotoListener{
        fun onLoadFailure(e: Exception)
        fun onPhotoLoaded(photo: InstagramPhoto)
        fun invalidPhotoUrl()
    }

    fun getPhoto(photoUrl: String, listener: SNInstagramPhotoListener){
        if(!photoUrl.contains("instagram.com/p/")){
            listener.invalidPhotoUrl()
            return
        }

        val client = OkHttpClient()
        val urlBuilder = "https://api.instagram.com/oembed".toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("url", photoUrl)
        val url = urlBuilder.build().toString()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                listener.onLoadFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    val json = response.body!!.string()
                    val jsonObject = JSONObject(json)
                    val photoUrl = jsonObject.get("thumbnail_url").toString()
                    listener.onPhotoLoaded(InstagramPhoto(photoUrl))
                }else{
                    listener.onLoadFailure(java.lang.Exception("response not successful"))
                }

            }

        })

    }



}

data class InstagramPhoto(
    val url: String?
)