package com.bec.hardwarelibrary.printer

import com.bec.hardwarelibrary.common.SerialPortDevice

/**
 * Created by 李卓鹏 on 2019/3/6 0006.
 */
class XPrinter : SerialPortDevice() {

    init {
        deviceName = "XPrinter打印机"
    }

    override fun baudRate(): Int = 19200

    override fun serialPort(): String = "/dev/ttyS4"
}