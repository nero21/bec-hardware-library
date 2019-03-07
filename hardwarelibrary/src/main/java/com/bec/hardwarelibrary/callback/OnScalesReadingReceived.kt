package com.bec.hardwarelibrary.callback

/**
 * 电子秤读数
 * Created by 李卓鹏 on 2019/3/6 0006.
 */
interface OnScalesReadingReceived {

    fun onReceived(reading: String, readingTime: Long)

    fun onError(throwable: Throwable)

}