package system

import java.util.regex.Pattern

class Validation {
    internal var letter = Pattern.compile("[a-zA-z]")
    internal var digit = Pattern.compile("[0-9]")
    internal var special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]")
    internal var eight = Pattern.compile(".{8,16}")


    fun isValidEmail(mStrEmail: String): Boolean {
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(mStrEmail)
        return matcher.matches()
    }

    fun isValidPassword(mStrPassword: String): Boolean {
        val hasLetter = letter.matcher(mStrPassword)
        val hasDigit = digit.matcher(mStrPassword)
        val hasSpecial = special.matcher(mStrPassword)
        val hasEight = eight.matcher(mStrPassword)
        return (hasLetter.find() || hasDigit.find() || hasSpecial.find()) && hasEight.matches()
    }

    fun isValidFullName(mStrName: String): Boolean {
        val mStrName_pattern = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}\$"
        val pattern = Pattern.compile(mStrName_pattern)
        val matcher = pattern.matcher(mStrName)
        return matcher.matches()
    }
    fun isValidDocName(mStrName: String): Boolean {
        val mStrName_pattern = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}\$"
        val pattern = Pattern.compile(mStrName_pattern)
        val matcher = pattern.matcher(mStrName)
        return matcher.matches()
    }




    fun isValidPhone(mStrPhone: String): Boolean {
        val mStrPhone_pattern = "[0-9]{10}"
        val pattern = Pattern.compile(mStrPhone_pattern)
        val matcher = pattern.matcher(mStrPhone)
        return matcher.matches()
    }
    fun isValidPin(mStrPhone: String): Boolean {
        val mStrPhone_pattern = "[0-9]{6}"
        val pattern = Pattern.compile(mStrPhone_pattern)
        val matcher = pattern.matcher(mStrPhone)
        return matcher.matches()
    }

    fun isValidZip(mStrZip: String): Boolean {
        return if (mStrZip == "00000" && mStrZip == " ") {
            true
        } else false
    }
    fun isValid250Character(mStr:String):Boolean{
        val mStrPhone_pattern = "[A-Za-z0-9'\\.\\-\\s\\,]{3,250}"
        val pattern = Pattern.compile(mStrPhone_pattern)
        val matcher = pattern.matcher(mStr)
        return matcher.matches()
    }

    fun isValidCode(mStrcode: String): Boolean {
        val mStrName_pattern = "[0-9]{3,6}"
        val pattern = Pattern.compile(mStrName_pattern)
        if (mStrcode == "") {
            return true
        } else {
            val matcher = pattern.matcher(mStrcode)
            return matcher.matches()
        }
    }
    fun isValidDishPrice(mStrcode: String): Boolean {
        val mStrName_pattern = "[0-9]{1,4}"
        val pattern = Pattern.compile(mStrName_pattern)
        if (mStrcode == "") {
            return true
        } else {
            val matcher = pattern.matcher(mStrcode)
            return matcher.matches()
        }
    }

    fun isValidAddress(mStrAddress: String): Boolean {
        val mStrName_pattern = "[A-Za-z0-9'\\.\\-\\s\\,]{3,100}"
        val pattern = Pattern.compile(mStrName_pattern)
        val matcher = pattern.matcher(mStrAddress)
        return matcher.matches()
    }
}