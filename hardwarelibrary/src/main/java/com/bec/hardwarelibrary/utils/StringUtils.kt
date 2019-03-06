package com.bec.hardwarelibrary.utils

import java.io.UnsupportedEncodingException

/**
 * Created by 李卓鹏 on 2019/3/6 0006.
 */
object StringUtils {

    /**
     * 字符串转byte数组
     */
    fun strToBytes(str: String): ByteArray {

        val data: ByteArray

        try {
            data = String(str.toByteArray(charset("utf-8")), charset("utf-8")).toByteArray(charset("gbk"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return ByteArray(0)
        }

        return data
    }

}