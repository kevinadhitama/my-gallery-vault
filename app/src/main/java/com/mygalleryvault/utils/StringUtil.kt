package com.mygalleryvault.utils

import android.text.format.DateUtils
import java.util.*

/**
 * Create by kevin.adhitama pm 4/28/2021.
 */
object StringUtil {
    fun getRelativeTime(timeCreated: Long): String {
        return DateUtils.getRelativeTimeSpanString(
            timeCreated,
            Calendar.getInstance().timeInMillis,
            DateUtils.HOUR_IN_MILLIS
        ).toString()
    }
}