package com.food.nextdoor.dropbox

import android.net.Uri
import com.dropbox.core.DbxException

import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.ThumbnailFormat
import com.dropbox.core.v2.files.ThumbnailSize
import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestHandler
import okio.Okio
import okio.Source
import java.io.IOException


class ImagePiccassoRequestHadler(private val mDbxClient: DbxClientV2) : RequestHandler() {

    override fun canHandleRequest(data: Request): Boolean {
        return SCHEME == data.uri.scheme && HOST == data.uri.host
    }

    @Throws(IOException::class)
    override fun load(request: Request, networkPolicy: Int): Result? {

        try {
            val downloader = mDbxClient.files().getThumbnailBuilder(request.uri.path!!)
                .withFormat(ThumbnailFormat.JPEG)
                .withSize(ThumbnailSize.W1024H768)
                .start()
                val result :RequestHandler.Result = RequestHandler.Result(downloader.inputStream,Picasso.LoadedFrom.NETWORK)
            return result

        } catch (e: DbxException) {
            throw IOException(e)
        }
        return null
    }

    companion object {

        private val SCHEME = "dropbox"
        private val HOST = "dropbox"

        /**
         * Builds a [Uri] for a Dropbox file thumbnail suitable for handling by this handler
         */
        fun buildPicassoUri(path:String?): Uri {
            return Uri.Builder()
                .scheme(SCHEME)
                .authority(HOST)
                .path(path).build()
        }
    }
}