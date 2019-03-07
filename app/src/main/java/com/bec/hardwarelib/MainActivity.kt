package com.bec.hardwarelib

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import com.bec.hardwarelibrary.callback.OnScalesReadingReceived
import com.bec.hardwarelibrary.printer.XPrinter.XPrinterSerialPortController
import com.bec.hardwarelibrary.printer.XPrinter.XPrinter
import com.bec.hardwarelibrary.scales.BPS30.BPS30Scales
import com.bec.hardwarelibrary.scales.BPS30.BPS30ScalesController

import com.bec.hardwarelibrary.utils.StringUtils
import kotlinx.android.synthetic.main.activity_main.*
import net.posprinter.posprinterface.IMyBinder
import net.posprinter.posprinterface.ProcessData
import net.posprinter.posprinterface.UiExecute
import net.posprinter.service.PosprinterService
import net.posprinter.utils.DataForSendToPrinterPos80
import net.posprinter.utils.PosPrinterDev
import java.util.*

class MainActivity : AppCompatActivity(), OnScalesReadingReceived {

    private val xPrinterSerialPortController by lazy { XPrinterSerialPortController(serialPortDevice = XPrinter()) }

    private val bpS30ScalesController by lazy {
        BPS30ScalesController(
            serialPortDevice = BPS30Scales(),
            onScalesReadingReceived = this
        )
    }

    /**
     * 打印服务
     */
    private val printerServiceConnection: ServiceConnection by lazy { PrinterServiceConnection() }

    private inner class PrinterServiceConnection : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            printerBinder = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            printerBinder = service as IMyBinder
        }
    }

    private fun connectPrinter() {

        printerBinder?.let { binder ->

            binder
                .connectUsbPort(applicationContext,
                    PosPrinterDev.GetUsbPathNames(applicationContext)?.let { it[0].orEmpty() }
                        ?: "",
                    object : UiExecute {
                        override fun onfailed() {
                            toast("连接失败")
                        }

                        override fun onsucess() {
                            toast("连接成功")
                        }
                    })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //绑定USB打印机服务
        bindService(Intent(this, PosprinterService::class.java), printerServiceConnection, BIND_AUTO_CREATE)

        //串口打印
        btn_open.setOnClickListener {
            xPrinterSerialPortController.connect(object : UiExecute {
                override fun onfailed() {
                    toast("开启失败")
                }

                override fun onsucess() {
                    toast("开启成功")
                }
            })
        }

        btn_close.setOnClickListener {
            xPrinterSerialPortController.disconnect(object : UiExecute {
                override fun onfailed() {
                    toast("关闭失败")
                }

                override fun onsucess() {
                    toast("关闭成功")
                }
            })
        }

        btn_send.setOnClickListener {
            xPrinterSerialPortController.writeDataBySerialPort(ProcessData {

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
            xPrinterSerialPortController.checkLink(object : UiExecute {
                override fun onfailed() {
                    toast("未连接")
                }

                override fun onsucess() {
                    toast("已连接")
                }
            })
        }

        //串口秤
        btn_open_scales.setOnClickListener {
            bpS30ScalesController.open()
        }

        btn_close_scales.setOnClickListener {
            bpS30ScalesController.close()
            tv_weight.text = null
        }

        btn_check_scales_link.setOnClickListener {
            toast("连接：${bpS30ScalesController.enable}")
        }

        //USB打印
        btn_usb_connect.setOnClickListener {
            connectPrinter()
        }

        btn_usb_print.setOnClickListener {

            printerBinder?.let { iMyBinder ->

                iMyBinder.writeDataByYouself(object : UiExecute {
                    override fun onfailed() {
                        toast("打印失败")
                    }

                    override fun onsucess() {
                        toast("打印成功")
                    }
                }) {
                    return@writeDataByYouself ArrayList<ByteArray>().apply {
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
                }
            } ?: toast("服务未启动")
        }

        btn_usb_disconnect.setOnClickListener {

            printerBinder?.let { iMyBinder ->

                iMyBinder.clearBuffer()
                iMyBinder.disconnectCurrentPort(object : UiExecute {
                    override fun onfailed() {
                        toast("断开失败")
                    }

                    override fun onsucess() {
                        toast("断开成功")
                    }
                })
            } ?: toast("服务未启动")
        }

        btn_usb_check.setOnClickListener {

            printerBinder?.checkLinkedState(object : UiExecute {
                override fun onfailed() {
                    toast("未连接")
                }

                override fun onsucess() {
                    toast("已连接")
                }
            }) ?: toast("服务未启动")
        }

    }

    /**
     * 电子秤结果
     * @param reading String
     * @param readingTime Long
     */
    @SuppressLint("SetTextI18n")
    override fun onReceived(reading: String, readingTime: Long) {
        //可选择获取一次读数后关闭串口
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
        unbindService(printerServiceConnection)
        XPrinterSerialPortController.finishAllTask()
        bpS30ScalesController.close()
    }

    companion object {

        const val TAG = "MainActivity"

        var printerBinder: IMyBinder? = null
    }

}
