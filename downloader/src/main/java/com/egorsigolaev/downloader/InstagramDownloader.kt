package com.egorsigolaev.downloader

import android.util.Log
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

    interface SNInstagramVideoListener{
        fun onLoadFailure(e: Exception)
        fun onVideoLoaded(video: InstagramVideo)
        fun invalidVideoUrl()
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

    fun getVideo(videoUrl: String, listener: SNInstagramVideoListener){
        if(!videoUrl.contains("instagram.com/p/")){
            listener.invalidVideoUrl()
            return
        }

        val client = OkHttpClient()
        val urlBuilder = videoUrl.toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("__a", "1")
        val url = urlBuilder.build().toString()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                listener.onLoadFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    print(response.body?.string())
                    val json = response.body!!.string()
                    val jsonObject = JSONObject(json)
                    val videoUrl = ((jsonObject.get("graphql") as JSONObject).get("shortcode_media") as JSONObject).get("video_url").toString()
                    listener.onVideoLoaded(InstagramVideo(videoUrl))
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

data class InstagramVideo(
    val url: String?
)