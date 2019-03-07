package com.bec.hardwarelibrary.common

import android.util.Log
import android_serialport_api.SerialPort
import com.bec.hardwarelibrary.callback.OnSerialPortReceived
import com.bec.hardwarelibrary.utils.FuncUtil
import com.bec.hardwarelibrary.utils.Utils
import java.io.*

/**
 * Created by 李卓鹏 on 2019/3/5 0005.
 */
abstract class SerialPortController(private val serialPortDevice: SerialPortDevice) {

    //串口是否可用
    public var enable: Boolean = false
        private set

    //是否展示串口数据
    var showSerialPortData: Boolean = false

    private var serialPort: SerialPort? = null

    private var inputStream: InputStream? = null

    private var outputStream: OutputStream? = null

    private var readThread: ReadThread? = null

    //串口读数
    abstract var onSerialPortReceived: OnSerialPortReceived?

    //设置缓存区大小
    open fun setBufferSize(): Int = 512

    //打开串口，接收数据
    public open fun open() {

        if (serialPort != null) {
            return
        }

        val device = File(serialPortDevice.serialPort())

        if (!device.exists()) {
            throw FileNotFoundException("当前设备不存在")
        }

        serialPort = SerialPort(device, serialPortDevice.baudRate(), 0)

        inputStream = serialPort?.inputStream
        outputStream = serialPort?.outputStream

        onSerialPortReceived?.let {

            readThread = ReadThread()
            readThread?.apply {
                threadRunning = true
                start()
            }

            TAG
        } ?: kotlin.run {
            readThread?.threadRunning = false
            readThread = null
        }

        enable = true
    }

    //关闭串口、读线程
    public open fun close() {

        if (serialPort == null) {
            return
        }

        readThread?.threadRunning = false
        readThread = null

        inputStream?.close()
        inputStream = null
        outputStream?.close()
        outputStream = null

        serialPort?.close()
        serialPort = null

        enable = false
    }

    /**
     * 发送数据
     * @param sendArray ByteArray
     */
    public open fun send(sendArray: ByteArray) {
        try {
            outputStream?.write(sendArray)
        } catch (t: Throwable) {
            t.printStackTrace()
            throw t
        }
    }

    /**
     * 发送16进制
     * @param sendHex String
     */
    public open fun sendHex(sendHex: String) {
        send(FuncUtil.hexToByteArr(sendHex))
    }

    /**
     * 发送字符串
     * @param sendTxt String
     */
    public fun sendTxt(sendTxt: String) {
        send(sendTxt.toByteArray())
    }

    inner class ReadThread : Thread() {

        @Volatile
        var threadRunning = false
            @Synchronized
            set

        override fun run() {
            super.run()

            while (threadRunning) {

                try {
                    val buffer = ByteArray(setBufferSize())

                    val size = inputStream?.read(buffer) ?: -1

                    if (size > 0) {

                        if (showSerialPortData) {
                            Log.i(TAG, buffer.contentToString())
                        }

                        onSerialPortReceived?.onReceived(buffer)
                    }

                    Thread.sleep(50)

                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }
    }

    companion object {

        const val TAG = "SerialPortController"
    }

}