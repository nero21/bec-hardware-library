package com.bec.hardwarelibrary.callback

/**
 * 串口读数
 * Created by 李卓鹏 on 2019/3/6 0006.
 */
interface OnSerialPortReceived {
    fun onReceived(byteArray: ByteArray)
}