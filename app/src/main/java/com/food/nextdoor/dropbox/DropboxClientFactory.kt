package com.food.nextdoor.dropbox

import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.http.OkHttp3Requestor


object DropboxClientFactory {

    private var sDbxClient: DbxClientV2? = null

    var client: DbxClientV2?=null
        get() {
            checkNotNull(sDbxClient) { "Client not initialized." }
            return sDbxClient
        }

    fun init(accessToken: String):DbxClientV2? {
        if (sDbxClient == null) {
            val requestConfig = DbxRequestConfig.newBuilder("nextdoor")
                .withHttpRequestor(OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
                .build()

            sDbxClient = DbxClientV2(requestConfig, accessToken)
        }
        return sDbxClient
    }
}