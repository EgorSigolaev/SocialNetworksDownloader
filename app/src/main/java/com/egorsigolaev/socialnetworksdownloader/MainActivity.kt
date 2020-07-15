package com.egorsigolaev.socialnetworksdownloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.egorsigolaev.downloader.*

class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "LOG_DEBUG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /* YOUTUBE DOWNLOADER */
//        val videoUrl = "https://youtu.be/Pve-NEoL7B8"
//        val downloader = SNDownloader(this, SNDownloader.YOUTUBE).build() as YoutubeDownloader
//        downloader.getVideoUrl(videoUrl, object : YoutubeDownloader.SNYoutubeListener{
//            override fun onLoadFailure(e: Exception) {
//                print(e.message)
//                Log.d(TAG, e.message)
//            }
//
//            override fun onVideosLoaded(videos: List<YoutubeVideo>) {
//                print("Video url: " + videos[0].videoUrl)
//                Log.d(TAG, "Video url: " + videos[0].videoUrl)
//            }
//
//            override fun invalidVideoUrl() {
//                Log.d(TAG, "Invalid video url")
//            }
//
//        })


        /* INSTAGRAM PHOTO DOWNLOADER */
//        val photoUrl = "https://www.instagram.com/p/CBxjJe2h_pr/?igshid=mqu8xjxxcn56"
//        val downloader = SNDownloader(this, SNDownloader.INSTAGRAM).build() as InstagramDownloader
//        downloader.getPhoto(photoUrl, object : InstagramDownloader.SNInstagramPhotoListener{
//            override fun onLoadFailure(e: Exception) {
//                Log.d(TAG, e.message)
//            }
//
//            override fun onPhotoLoaded(photo: InstagramPhoto) {
//                Log.d(TAG, "Url: " + photo.url)
//            }
//
//            override fun invalidPhotoUrl() {
//                Log.d(TAG, "Invalid photo url")
//            }
//
//        })

        /* INSTAGRAM VIDEO DOWNLOADER */
        val videoUrl = "https://www.instagram.com/p/B_yJmO7BSfH/"
        val downloader = SNDownloader(this, SNDownloader.INSTAGRAM).build() as InstagramDownloader
        downloader.getVideo(videoUrl, object : InstagramDownloader.SNInstagramVideoListener{
            override fun onLoadFailure(e: Exception) {
                Log.d(TAG, e.message)
            }

            override fun onVideoLoaded(video: InstagramVideo) {
                Log.d(TAG, "Url: " + video.url)
            }

            override fun invalidVideoUrl() {
                Log.d(TAG, "Invalid photo url")
            }

        })

    }
}
