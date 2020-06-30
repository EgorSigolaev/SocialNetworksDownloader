package com.egorsigolaev.downloader

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.commit451.youtubeextractor.Stream
import io.reactivex.schedulers.Schedulers

class YoutubeDownloader(val context: Context) : Downloader {

    var ytExtractor: YouTubeExtractor? = null
    var extractor = com.commit451.youtubeextractor.YouTubeExtractor.Builder().build()

    interface SNYoutubeListener {
        fun onLoadFailure(e: Exception)
        fun onVideosLoaded(videos: List<YoutubeVideo>)
    }

    fun getVideoUrl2(videoUrl: String, listener: SNYoutubeListener){
        val uri: Uri = Uri.parse(videoUrl)
        val videoId: String = uri.getQueryParameter("v") ?: throw java.lang.Exception("invalid video url")
        val disposable = extractor.extract(videoId)
            .subscribeOn(Schedulers.io())
            .subscribe({
                val videoUrl = it.streams.filterIsInstance<Stream.VideoStream>().max()?.url
                val videos = mutableListOf<YoutubeVideo>()
                for(video in it.thumbnails){
                    val ytVideo = YoutubeVideo(video.quality, it.title, video.url)
                    videos.add(ytVideo)
                }
                listener.onVideosLoaded(videos)
            }, {
                val message = it.message
                val localizedMessage = it.localizedMessage
                listener.onLoadFailure(java.lang.Exception(it))
            })
    }


    fun getVideoUrl(videoUrl: String, listener: SNYoutubeListener) {
        ytExtractor?.cancel(true)
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