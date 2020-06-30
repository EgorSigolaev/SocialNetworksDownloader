package com.egorsigolaev.socialnetworksdownloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.egorsigolaev.downloader.SNDownloader
import com.egorsigolaev.downloader.YoutubeDownloader
import com.egorsigolaev.downloader.YoutubeVideo

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val videoUrl = "http://www.youtube.com/watch?v=n79aphwhpW0"
        //val videoUrl = "https://www.youtube.com/watch?v=GeEn9skH-vE"
      //val videoUrl = "https://youtu.be/n79aphwhpW0"
        //val videoUrl = "https://youtu.be/S44cTOPie-s"

        val downloader = SNDownloader(this, SNDownloader.YOUTUBE).build() as YoutubeDownloader

        downloader.getVideoUrl(videoUrl, object : YoutubeDownloader.SNYoutubeListener{
            override fun onLoadFailure(e: Exception) {
                print(e.message)
                Log.d("LOG_DEBUG", e.message)
            }

            override fun onVideosLoaded(videos: List<YoutubeVideo>) {
                print("Video url: " + videos[0].videoUrl)
                Log.d("LOG_DEBUG", "Video url: " + videos[0].videoUrl)
            }

        })



//        val downloader = SNDownloader(this, SNDownloader.YOUTUBE).build() as YoutubeDownloader
//        downloader.getVideoUrl(videoUrl, object : YoutubeDownloader.SNYoutubeListener{
//            override fun onLoadFailure(e: Exception) {
//                print(e.message)
//                Log.d("LOG_DEBUG", e.message)
//            }
//
//            override fun onVideosLoaded(videos: List<YoutubeVideo>) {
//                print("Video url: " + videos[0].videoUrl)
//                Log.d("LOG_DEBUG", "Video url: " + videos[0].videoUrl)
//            }
//
//        })
    }
}
