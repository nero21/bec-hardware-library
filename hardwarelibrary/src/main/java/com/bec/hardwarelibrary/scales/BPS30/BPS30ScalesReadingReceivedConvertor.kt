package com.bec.hardwarelibrary.scales.BPS30

import android.util.Log
import com.bec.hardwarelibrary.callback.OnScalesReadingReceived
import com.bec.hardwarelibrary.callback.OnSerialPortReceived
import com.bec.hardwarelibrary.utils.Utils

/**
 * BPS15/30串口电子秤的读数解析器
 * Created by 李卓鹏 on 2019/3/6 0006.
 */
class BPS30ScalesReadingReceivedConvertor(private val onScalesReadingReceived: OnScalesReadingReceived) : OnSerialPortReceived {

    override fun onReceived(byteArray: ByteArray) {

        try {

            if (byteArray.isEmpty()) {
                onScalesReadingReceived.onError(Throwable("电子秤读数为空"))
                return
            }

            byteArray.forEachIndexed { i, byte ->

                if (byte.toInt() == 0x1 && i + 1 < byteArray.size && byteArray[i + 1].toInt() == 0x2 && i + 16 < byteArray.size && byteArray[i + 13].toInt() == 0x3 && byteArray[i + 14].toInt() == 0x4) {

                    val response = ByteArray(16)

                    System.arraycopy(byteArray, i, response, 0, response.size)

                    if (response.isEmpty()) {
                        onScalesReadingReceived.onError(Throwable("电子秤读数解析异常"))
                        return@forEachIndexed
                    }

                    when (Utils.checkLegal(response)) {

                        Utils.RIGHT_STATUS -> {

                            var weight = String(response, 4, 6)

                            if (!weight.isEmpty()) {
                                weight = weight.replaceFirst("^0*".toRegex(), "")
                                if (weight.startsWith(".")) {
                                    weight = "0$weight"
                                }
                            }

                            onScalesReadingReceived.onReceived(
                                    reading = weight,
                                    readingTime = System.currentTimeMillis()
                            )
                        }
                        Utils.WRONG_STATUS -> {
                            onScalesReadingReceived.onError(Throwable("电子秤读数校验错误，请重试"))
                        }
                        Utils.ILLEGAL_DATA -> {
                            onScalesReadingReceived.onError(Throwable("电子秤读数格式不符，请连接正确设备"))
                        }
                        Utils.BELOW_ZERO -> {
                            onScalesReadingReceived.onError(Throwable("电子秤读数小于零，请重新校正电子秤"))
                        }
                        else -> {
                            onScalesReadingReceived.onError(Throwable("获取电子秤读数无效，请重试"))
                        }
                    }

                    return@forEachIndexed
                }
            }

        } catch (t: Throwable) {
            t.printStackTrace()
            onScalesReadingReceived.onError(throwable = t)
        }
    }

    companion object {

        const val TAG = "BPS30ScalesConvertor"

    }

}