package system

import android.content.Context
import android.content.SharedPreferences

class Preference(val context: Context) {

    private var sharedRef: SharedPreferences? = null
    private var userBasicInfo: HashMap<String, Int> = HashMap()

    init {
        this.sharedRef = context.getSharedPreferences(Utility.MY_PREFERENCE, Context.MODE_PRIVATE)
    }

     fun saveUserId(userId: Int) {
        val editor = sharedRef!!.edit()
        editor.putInt(Utility.USER_ID, userId)
        editor.apply()
    }


    fun getUserId() : Int = sharedRef!!.getInt(Utility.USER_ID, 0)


    private fun saveUserTypeId(userTypeId: Int) {
        val editor = sharedRef!!.edit()
        editor.putInt(Utility.USER_TYPE_ID, userTypeId)
        editor.apply()
    }

    fun getUserBasicInfo(): HashMap<String, Int> {
        val userId = sharedRef!!.getInt(Utility.USER_ID, -1)
        val userTypeId = sharedRef!!.getInt(Utility.USER_TYPE_ID, -1)

        userBasicInfo[Utility.USER_ID] = userId
        userBasicInfo[Utility.USER_TYPE_ID] =userTypeId

        return userBasicInfo
    }


    fun saveUserBasicInfo(basicInfo: HashMap<String, Int> ) {
        val userId: Int = basicInfo[Utility.USER_ID]!!
        val userTypeId: Int = basicInfo[Utility.USER_TYPE_ID]!!

        val editor = sharedRef!!.edit()
        editor.putInt(Utility.USER_ID, userId)
        editor.putInt(Utility.USER_TYPE_ID, userTypeId)
        editor.apply()
    }

    fun saveAuthToken(accessToken :String){
        val editor = sharedRef!!.edit()
        editor.putString(Utility.ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    fun getUserChefId():Int{
       return  sharedRef!!.getInt(Utility.USER_CHEF_ID, 0)
    }

    fun saveUserChefId(chefId :Int){
        val editor = sharedRef!!.edit()
        editor.putInt(Utility.USER_CHEF_ID, chefId)
        editor.apply()
    }

    fun getAuthToken():String{
        return  sharedRef!!.getString(Utility.ACCESS_TOKEN, "")
    }


}







