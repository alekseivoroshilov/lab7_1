package com.example.lab7_1

import android.app.DownloadManager
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {

    companion object {
        const val broadcastActionKey = "com.example.lab_7_1.PIC_DOWNLOAD"
        const val broadcastMessageKey = "broadcastMessageKey"

        const val picUrl = "https://sun9-14.userapi.com/c205616/v205616479/4386/JksgL3F-SxA.jpg"
        const val fileName = "lab_7_1"
    }

    class PictureDownloader : IntentService("PictureDownloader") {
        override fun onHandleIntent(intent: Intent?) {
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(intent!!.getStringExtra(Intent.EXTRA_TEXT))
            val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            val request = DownloadManager.Request(uri).apply {
                setTitle("PictureDownloader_Title")
                setDescription("Downloading")
                setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                )
                setDestinationUri(Uri.fromFile(file))

            }
            downloadManager.enqueue(request)

            val responseIntent = Intent().apply {
                action = broadcastActionKey
                addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                putExtra(broadcastMessageKey, file.absolutePath)
            }
            sendBroadcast(responseIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, PictureDownloader::class.java)
            .putExtra(Intent.EXTRA_TEXT, picUrl)

        findViewById<Button>(R.id.button).setOnClickListener {
            startService(intent)
        }
    }
}
