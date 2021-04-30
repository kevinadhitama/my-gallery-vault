package com.mygalleryvault.utils

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Create by kevin.adhitama pm 4/30/2021.
 */
object PassCodeSharedPreferences {
    private const val passCodeSPName = "PassCode"
    private const val passCodeSPField = "passCode"

    private fun getSP(context: Context): SharedPreferences {
        return context.getSharedPreferences(passCodeSPName, Context.MODE_PRIVATE)
    }

    fun updatePassCode(context: Context, oldPassCode: String = "", newPassCode: String, isFirstTime: Boolean = false) {
        val sp = getSP(context)
        if (isFirstTime || checkPassCode(context, oldPassCode)) {
            with(sp.edit()) {
                putString(passCodeSPField, encrypt(newPassCode))
                apply()
            }
        }
    }

    fun checkPassCode(context: Context, passCode: String): Boolean {
        val sp = getSP(context)
        sp.getString(passCodeSPField, "")?.let {
            if (it.isEmpty()) return false

            val encodedPassCode = encrypt(passCode)
            if (encodedPassCode == "") return false

            return encodedPassCode == it

        }
        return false
    }

    fun hasPassCode(context: Context): Boolean {
        val sp = getSP(context)
        return sp.contains(passCodeSPField)
    }

    private fun encrypt(text: String): String {
        try {
            // Create MD5 Hash
            val s = "MyGalleryVault$text"
            val digest: MessageDigest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest: ByteArray = digest.digest()

            // Create Hex String
            val hexString = StringBuffer()
            for (i in messageDigest.indices) hexString.append(
                Integer.toHexString(
                    0xFF and messageDigest[i]
                        .toInt()
                )
            )
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}