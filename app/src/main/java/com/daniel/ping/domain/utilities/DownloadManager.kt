package com.daniel.ping.domain.utilities

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.daniel.ping.R

object DownloadManager{

    /**
     * This function initiates the download of a file or MP3 from the provided URL using Android's DownloadManager
     * @param FMName The name of the file or MP3 to be used for saving the downloaded file
     * @param FMSize The size of the file or MP3 as a String (e.g., "1024 KB")
     * @param context The context of the application, required for accessing system services and displaying Toast messages
     */
    fun downloadFileOrMP3(
        urlFM: String,
        FMName: String,
        FMSize: String,
        context: Context
    ){
        val request = DownloadManager.Request(Uri.parse(urlFM))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(FMName)
            .setDescription("${FMSize.toDouble() / 1024} MB")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FMName)
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(context, context.getString(R.string.downloadMessage), Toast.LENGTH_SHORT).show()

    }
}