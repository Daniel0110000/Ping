package com.daniel.ping.domain.utilities

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.daniel.ping.R

fun downloadFile(
    urlFile: String,
    fileName: String,
    fileSize: String,
    context: Context
){
    val request = DownloadManager.Request(Uri.parse(urlFile))
    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        .setTitle(fileName)
        .setDescription("${fileSize.toDouble() / 1024} MB")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)

    Toast.makeText(context, context.getString(R.string.downloadMessage), Toast.LENGTH_SHORT).show()

}