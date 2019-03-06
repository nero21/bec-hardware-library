package com.bec.hardwarelibrary.common

import android_serialport_api.SerialPort
import com.bec.hardwarelibrary.callback.OnSerialPortReceived
import com.bec.hardwarelibrary.utils.FuncUtil
import java.io.*

/**
 * Created by 李卓鹏 on 2019/3/5 0005.
 */
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class SerialPortController(private val serialPortDevice: SerialPortDevice) : java.lang.Object() {

    var enable: Boolean = false

    private var serialPort: SerialPort? = null

    private var inputStream: InputStream? = null

    private var outputStream: OutputStream? = null

    private val readThread by lazy { ReadThread() }

    public var onSerialPortReceived: OnSerialPortReceived? = null

    private var byteLoopData: ByteArray? = null

    public fun setTextLooperData(sendText: String) {
        byteLoopData = sendText.toByteArray()
    }

    public fun setHexLooperData(sendHex: String) {
        byteLoopData = FuncUtil.HexToByteArr(sendHex)
    }

    public fun open() {

        val device = File(serialPortDevice.serialPort())

        if (!device.exists()) {
            throw FileNotFoundException("当前设备不存在")
        }

        serialPort = SerialPort(device, serialPortDevice.baudRate(), 0).apply {
            this@SerialPortController.inputStream = inputStream
            this@SerialPortController.outputStream = outputStream
        }

        readThread.start()

        enable = true
    }

    public fun close() {

        readThread.interrupt()

        serialPort?.close()
        serialPort = null

        enable = false
    }

    public fun send(sendArray: ByteArray) {
        try {
            outputStream?.write(sendArray)
        } catch (t: Throwable) {
            t.printStackTrace()
            throw t
        }
    }

    public fun sendHex(sendHex: String) {
        send(FuncUtil.HexToByteArr(sendHex))
    }

    public fun sendTxt(sendTxt: String) {
        send(sendTxt.toByteArray())
    }

    inner class ReadThread : Thread() {

        override fun run() {
            super.run()

            while (!isInterrupted) {

                try {
                    val buffer = ByteArray(512)

                    val size = inputStream?.read(buffer) ?: -1

                    if (size > 0) {
                        onSerialPortReceived?.onReceived(buffer)
                    }

                } catch (t: Throwable) {
                    t.printStackTrace()
                    return
                }
            }
        }
    }

    companion object {

        const val TAG = "SerialPortController"

        var delay: Long = 500L
    }

}