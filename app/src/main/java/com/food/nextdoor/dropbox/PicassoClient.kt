package com.food.nextdoor.dropbox

import android.content.Context
import com.squareup.picasso.Picasso
import com.dropbox.core.v2.DbxClientV2
import com.jakewharton.picasso.OkHttp3Downloader


object PicassoClient {
    var picasso: Picasso? = null
        private set

    fun init(context: Context, dbxClient: DbxClientV2) {

        // Configure picasso to know about special thumbnail requests
        picasso = Picasso.Builder(context)
            .downloader(OkHttp3Downloader(context))
            .addRequestHandler(ImagePiccassoRequestHadler(dbxClient))
            .build()
    }
}