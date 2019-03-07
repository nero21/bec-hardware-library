package com.bec.hardwarelibrary.scales.BPS30

import com.bec.hardwarelibrary.common.SerialPortDevice

/**
 * Created by 李卓鹏 on 2019/3/6 0006.
 */
class BPS30Scales : SerialPortDevice() {

    init {
        deviceName = "佰伦斯BPS15/30电子计价秤"
    }

    override fun baudRate(): Int = 9600

    override fun serialPort(): String = "/dev/ttyS3"
}