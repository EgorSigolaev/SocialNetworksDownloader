package com.egorsigolaev.downloader

import android.content.Context
import java.lang.Exception

class SNDownloader(val context: Context, val socialNetwork: Int) {

    companion object{
        const val TIKTOK = 0
        const val YOUTUBE = 1
        const val INSTAGRAM = 2
    }

    fun build(): Downloader{
        when(socialNetwork){
            YOUTUBE -> return YoutubeDownloader(context)
            INSTAGRAM -> return InstagramDownloader()
            else -> throw Exception("social network not specified")
        }
    }


}