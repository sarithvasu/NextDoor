package system

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.provider.Settings.Global.getString
import androidx.appcompat.app.AlertDialog
import com.food.nextdoor.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.ref.WeakReference


class SaveImageHelper(
    var alertDialogWeakReference: WeakReference<AlertDialog>,
    var contentResolverWeakReference: WeakReference<ContentResolver>,
    var name: String,
    var disc: String,
    private var context: Context
) : Target {


    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

    }

    override fun onBitmapFailed(errorDrawable: Drawable?) {

    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        var alertDialog: AlertDialog = alertDialogWeakReference.get()!!
        var contentResolver: ContentResolver = contentResolverWeakReference.get()!!
        MediaStore.Images.Media.insertImage(contentResolver,bitmap,name,disc)
        alertDialog.dismiss()
        var intent:Intent= Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
        //context.startActivity(Intent.createChooser(intent,"VIEW_PICTURE"))

    }
}


