package com.bec.hardwarelib

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bec.hardwarelibrary.common.SerialPortController
import com.bec.hardwarelibrary.printer.PrinterSerialPortController
import com.bec.hardwarelibrary.printer.XPrinter
import com.bec.hardwarelibrary.utils.StringUtils
import kotlinx.android.synthetic.main.activity_main.*
import net.posprinter.posprinterface.ProcessData
import net.posprinter.posprinterface.UiExecute
import net.posprinter.utils.DataForSendToPrinterPos80

class MainActivity : AppCompatActivity() {

    private val printerController: PrinterSerialPortController by lazy { PrinterSerialPortController() }

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

    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        PrinterSerialPortController.finishAllTask()
    }
}
