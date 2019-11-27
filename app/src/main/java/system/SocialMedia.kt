package system

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Parcelable
import system.Utility.Companion.SHARE_BY_CHOOSER_NEW


class SocialMedia {

    fun shareWithWhatsApp(activity: Activity, strMessage: String, linkUrl: String) {
        val pm = activity.packageManager
        try {
            val waIntent = Intent(Intent.ACTION_SEND)
            waIntent.type = "text/plain"
            waIntent.setPackage(Utility.WHATS_APP_PACKAGE_NAME)
            waIntent.putExtra(Intent.EXTRA_TEXT, "$strMessage $linkUrl")
            activity.startActivity(waIntent)
        } catch (e: PackageManager.NameNotFoundException) {
            Utility.createCustomDialog(activity,  "whatsapp not found",true)
        }
    }

    // Open whatsApp if installed otherwise it opens share chooser windows
    fun shareWIthWhatsAppOrChooser(activity: Activity, strMessage: String, linkUrl: String) {
        val pm = activity.packageManager
        val waIntent = Intent(Intent.ACTION_SEND)

        try {
            waIntent.type = "text/plain"
            waIntent.putExtra(Intent.EXTRA_TEXT, "$strMessage $linkUrl")

            //Check if package exists or not. If not then code in catch block will be called
            val info = pm.getPackageInfo(Utility.WHATS_APP_PACKAGE_NAME, PackageManager.GET_META_DATA).toString()

            if (info.contains(Utility.WHATS_APP_PACKAGE_NAME)) {
                waIntent.setPackage(Utility.WHATS_APP_PACKAGE_NAME)
                //activity.startActivity(waIntent)
                activity.startActivityForResult(waIntent,Utility.SHARE_BY_CHOOSER)
            }

        } catch (e: PackageManager.NameNotFoundException) {
           //activity.startActivity(Intent.createChooser(waIntent, "Soumen"))
            activity.startActivityForResult(Intent.createChooser(waIntent, "SHARE...."), Utility.SHARE_BY_CHOOSER)
        }
    }





    fun shareWIthWhatsAppOrChooserResult(activity: Activity, strMessage: String, linkUrl: String) {

        val targetedShareIntents = ArrayList<Intent>()
        val shareIntent = Intent(android.content.Intent.ACTION_SEND)
        shareIntent.type = "text/plain"

        // Set title and text to share when the user selects an option.
        shareIntent.putExtra(Intent.EXTRA_TITLE, strMessage )
        shareIntent.putExtra(Intent.EXTRA_TEXT, linkUrl )

        val infos= activity.packageManager.queryIntentActivities(shareIntent,  0)

        if (!infos.isEmpty()) {
            for (info in infos) {
                val targetedShare = Intent(android.content.Intent.ACTION_SEND)
                targetedShare.type = "text/plain"
                targetedShare.setPackage(info.activityInfo.packageName.toLowerCase())
                targetedShareIntents.add(targetedShare)
            }

            // Then show the ACTION_PICK_ACTIVITY to let the user select it
            val intentPick = Intent()
            intentPick.action = Intent.ACTION_PICK_ACTIVITY

            // Set the title of the dialog
            intentPick.putExtra(Intent.EXTRA_TITLE,"Soumen Dialog" )
            intentPick.putExtra(Intent.EXTRA_INTENT, shareIntent)
            intentPick.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray())

            // Call StartActivityForResult so we can get the app name selected by the user
            activity.startActivityForResult(intentPick, SHARE_BY_CHOOSER_NEW)
        }
    }

    private fun shareByExcludingPackage(activity: Activity) {
        // get available share intents
        val packageToBeFiltered = Utility.WHATS_APP_PACKAGE_NAME
        val targets = ArrayList<Intent>()
        val template = Intent(Intent.ACTION_SEND)
        template.setType("text/plain")
        val candidates = activity.packageManager.queryIntentActivities(template, 0)
        // filter package here
        for (candidate in candidates) {
            val packageName = candidate.activityInfo.packageName
            if (packageName != packageToBeFiltered) {
                val target = Intent(android.content.Intent.ACTION_SEND)
                target.setType("text/plain")
                target.putExtra(Intent.EXTRA_TEXT, "Text to share")
                target.setPackage(packageName)
                targets.add(target)
            }
        }
        if (!targets.isEmpty()) {
            val chooser = Intent.createChooser(targets.get(0), "Share dialog title goes here")
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(arrayOf<Parcelable>()))
            activity.startActivity(chooser)
        }
    }

}