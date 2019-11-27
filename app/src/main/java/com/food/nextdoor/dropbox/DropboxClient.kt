package com.food.nextdoor.dropbox

import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.DbxRequestConfig



class DropboxClient {
     companion object {
        fun getClient(ACCESS_TOKEN: String): DbxClientV2 {
            // Create Dropbox client
            val config = DbxRequestConfig("dropbox/sample-app", "en_US")
            return DbxClientV2(config, ACCESS_TOKEN)
        }
    }
}