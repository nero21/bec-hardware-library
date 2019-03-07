package com.bec.hardwarelibrary.scales.BPS30

import com.bec.hardwarelibrary.callback.OnScalesReadingReceived
import com.bec.hardwarelibrary.callback.OnSerialPortReceived
import com.bec.hardwarelibrary.common.SerialPortController
import com.bec.hardwarelibrary.common.SerialPortDevice

/**
 * Created by 李卓鹏 on 2019/3/6 0006.
 */
class BPS30ScalesController(serialPortDevice: SerialPortDevice, onScalesReadingReceived: OnScalesReadingReceived) : SerialPortController(serialPortDevice = serialPortDevice) {

    override var onSerialPortReceived: OnSerialPortReceived? = BPS30ScalesReadingReceivedConvertor(onScalesReadingReceived = onScalesReadingReceived)

    override fun setBufferSize(): Int = 128

}