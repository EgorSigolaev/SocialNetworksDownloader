package com.egorsigolaev.downloader

import android.annotation.SuppressLint
import android.content.Context
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile

class YoutubeDownloader(val context: Context) : Downloader {

    var ytExtractor: YouTubeExtractor? = null

    interface SNYoutubeListener {
        fun onLoadFailure(e: Exception)
        fun onVideosLoaded(videos: List<YoutubeVideo>)
        fun invalidVideoUrl()
    }


    fun getVideoUrl(videoUrl: String, listener: SNYoutubeListener) {
        ytExtractor?.cancel(true)
        if (!videoUrl.contains("://youtu.be/") && !videoUrl.contains("youtube.com/watch?v=")){
            listener.invalidVideoUrl()
            return
        }
        ytExtractor = @SuppressLint("StaticFieldLeak")
        object : YouTubeExtractor(context) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if (ytFiles == null) {
                    listener.onLoadFailure(Exception("This video cannot be downloaded"))
                    return
                }

                val videos = mutableListOf<YoutubeVideo>()
                var i = 0
                var itag: Int
                while (i < ytFiles.size()) {
                    itag = ytFiles.keyAt(i)
                    val ytFile = ytFiles[itag]
                    if (ytFile.format.height == -1 || ytFile.format.height >= 360) {
                        videos.add(YoutubeVideo(getResolution(ytFile), getVideoTitle(ytFile, videoMeta?.title), getVideoUrl(ytFile)))
                    }
                    i++
                }

                listener.onVideosLoaded(videos)


            }

        }
        ytExtractor?.extract(videoUrl, true, false)
    }

    private fun getResolution(ytFile: YtFile): String{
        var resolution = if (ytFile.format.height == -1) "Audio " + ytFile.format.audioBitrate + " kbit/s" else ytFile.format.height.toString() + "p"
        resolution += if (ytFile.format.isDashContainer) " dash" else ""
        return resolution
    }

    private fun getVideoTitle(ytFile: YtFile, videoTitle: String?): String?{
        if(videoTitle == null){
            return null
        }
        var fileName =  if (videoTitle.length > 55) {
            videoTitle.substring(0, 55) + "." + ytFile.format.ext
        } else {
            videoTitle + "." + ytFile.format.ext
        }
        fileName = fileName.replace("[\\\\><\"|*?%:#/]".toRegex(), "")
        return fileName
    }

    private fun getVideoUrl(ytFile: YtFile): String{
        return ytFile.url
    }


}

data class YoutubeVideo(
    val resolution: String?,
    val videoTitle: String?,
    val videoUrl: String?
)