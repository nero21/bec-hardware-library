package com.bec.hardwarelibrary.common

/**
 * Created by 李卓鹏 on 2019/3/6 0006.
 */
class CommonSerialPortDevice(private val baudRate: Int, private val serialPort: String) : SerialPortDevice() {

    init {
        deviceName = "通用串口设备"
    }

    override fun baudRate(): Int = baudRate

    override fun serialPort(): String = serialPort
}