package com.bec.hardwarelibrary.common

/**
 * Created by 李卓鹏 on 2019/3/5 0005.
 */
abstract class SerialPortDevice {

    //设备名称
    public var deviceName: String = ""

    //波特率
    public abstract fun baudRate(): Int

    //串口号
    public abstract fun serialPort(): String

}