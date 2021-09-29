package my.nytimes.app.utils

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    //"2021-09-22T22:47:40+0000"

    private val fromFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val fromFormatter2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+0000", Locale.ENGLISH)
    private val toFormatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)

    fun dateFormatChanger(dtStr: String): String {
        val fromdt = fromFormatter.parse(dtStr)
        return toFormatter.format(fromdt ?: Date())
    }

    fun dateFormatChanger2(dtStr: String): String {
        val fromdt = fromFormatter2.parse(dtStr)
        return toFormatter.format(fromdt ?: Date())
    }
}