package com.bec.hardwarelib

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bec.hardwarelibrary.callback.OnScalesReadingReceived
import com.bec.hardwarelibrary.common.SerialPortController
import com.bec.hardwarelibrary.printer.PrinterSerialPortController
import com.bec.hardwarelibrary.printer.XPrinter
import com.bec.hardwarelibrary.scales.BPS30.BPS30ScalesController
import com.bec.hardwarelibrary.utils.StringUtils
import kotlinx.android.synthetic.main.activity_main.*
import net.posprinter.posprinterface.ProcessData
import net.posprinter.posprinterface.UiExecute
import net.posprinter.utils.DataForSendToPrinterPos80
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity(), OnScalesReadingReceived {

    private val printerController: PrinterSerialPortController by lazy { PrinterSerialPortController() }

    private val bpS30ScalesController by lazy { BPS30ScalesController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_open.setOnClickListener {
            printerController.connect(XPrinter(), object : UiExecute {
                override fun onfailed() {
                    toast("开启失败")
                }

                override fun onsucess() {
                    toast("开启成功")
                }
            })
        }

        btn_close.setOnClickListener {
            printerController.disconnect(object : UiExecute {
                override fun onfailed() {
                    toast("关闭失败")
                }

                override fun onsucess() {
                    toast("关闭成功")
                }
            })
        }

        btn_send.setOnClickListener {
            printerController.writeDataBySerialPort(ProcessData {

                return@ProcessData ArrayList<ByteArray>().apply {
                    add(DataForSendToPrinterPos80.initializePrinter())

                    add(StringUtils.strToBytes("abcdefghijklnmopqrst,ABCDEFG,测试测试测试文本，1234567890！@#￥%……&*（）——+"))
                    add(DataForSendToPrinterPos80.printAndFeedLine())
                    add(StringUtils.strToBytes("abcdefghijklnmopqrst,ABCDEFG,测试测试测试文本，1234567890！@#￥%……&*（）——+"))
                    add(DataForSendToPrinterPos80.printAndFeedLine())
                    add(StringUtils.strToBytes("abcdefghijklnmopqrst,ABCDEFG,测试测试测试文本，1234567890！@#￥%……&*（）——+"))
                    add(DataForSendToPrinterPos80.printAndFeedLine())
                    add(StringUtils.strToBytes("abcdefghijklnmopqrst,ABCDEFG,测试测试测试文本，1234567890！@#￥%……&*（）——+"))
                    add(DataForSendToPrinterPos80.printAndFeedLine())
                    add(StringUtils.strToBytes("abcdefghijklnmopqrst,ABCDEFG,测试测试测试文本，1234567890！@#￥%……&*（）——+"))
                    add(DataForSendToPrinterPos80.printAndFeedLine())
                    add(StringUtils.strToBytes("abcdefghijklnmopqrst,ABCDEFG,测试测试测试文本，1234567890！@#￥%……&*（）——+"))
                    add(DataForSendToPrinterPos80.printAndFeedLine())
                    add(StringUtils.strToBytes("abcdefghijklnmopqrst,ABCDEFG,测试测试测试文本，1234567890！@#￥%……&*（）——+"))
                    add(DataForSendToPrinterPos80.printAndFeedLine())

                    add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1))
                    add(DataForSendToPrinterPos80.printAndFeedLine())
                }

            }, object : UiExecute {
                override fun onfailed() {
                    toast("发送失败")
                }

                override fun onsucess() {
                    toast("发送成功")
                }
            })
        }

        btn_check_link.setOnClickListener {
            printerController.checkLink(object : UiExecute {
                override fun onfailed() {
                    toast("未连接")
                }

                override fun onsucess() {
                    toast("已连接")
                }
            })
        }

        btn_open_scales.setOnClickListener {
            bpS30ScalesController.open()
        }

        btn_close_scales.setOnClickListener {
            bpS30ScalesController.close()
        }

        btn_check_link.setOnClickListener {
            toast("连接：${bpS30ScalesController.enable}")
        }
    }

    /**
     * 电子秤结果
     * @param reading String
     * @param readingTime Long
     */
    @SuppressLint("SetTextI18n")
    override fun onReceived(reading: String, readingTime: Long) {
//        bpS30ScalesController.close()
        runOnUiThread {
            tv_weight.text = "读数：$reading kg ，时间：${Date(readingTime).toLocaleString()}"
        }
    }

    /**
     * 电子秤报错
     * @param throwable Throwable
     */
    override fun onError(throwable: Throwable) {
        toast(throwable.toString())
    }

    private fun toast(text: String) {
        runOnUiThread {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PrinterSerialPortController.finishAllTask()
        bpS30ScalesController.close()
    }
}
