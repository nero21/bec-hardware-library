package com.bec.hardwarelibrary.scales.BPS30

import com.bec.hardwarelibrary.callback.OnScalesReadingReceived
import com.bec.hardwarelibrary.callback.OnSerialPortReceived
import com.bec.hardwarelibrary.common.SerialPortController

/**
 * Created by 李卓鹏 on 2019/3/6 0006.
 */
class BPS30ScalesController(onScalesReadingReceived: OnScalesReadingReceived) : SerialPortController(BPS30Scales()) {

    override var onSerialPortReceived: OnSerialPortReceived? = BPS30ScalesReadingReceivedConvertor(onScalesReadingReceived = onScalesReadingReceived)

    override fun setBufferSize(): Int = 128

}