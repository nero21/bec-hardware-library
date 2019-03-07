package com.bec.hardwarelibrary.printer

import android.os.AsyncTask
import com.bec.hardwarelibrary.callback.OnSerialPortReceived
import com.bec.hardwarelibrary.common.SerialPortController
import com.bec.hardwarelibrary.common.SerialPortDevice
import net.posprinter.asynncTask.PosAsynncTask
import net.posprinter.posprinterface.BackgroundInit
import net.posprinter.posprinterface.ProcessData
import net.posprinter.posprinterface.UiExecute
import java.io.IOException

/**
 * Created by 李卓鹏 on 2019/3/6 0006.
 */
class PrinterSerialPortController(serialPortDevice: SerialPortDevice) : SerialPortController(serialPortDevice = serialPortDevice) {

    override var onSerialPortReceived: OnSerialPortReceived? = null

    /**
     * 连接打印机
     * @param uiExecute UiExecute
     */
    public fun connect(uiExecute: UiExecute) {

        add(posAsynncTask = PosAsynncTask(uiExecute, BackgroundInit {

            open()

            return@BackgroundInit enable

        }).apply {
            executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, *arrayOfNulls<Void>(0))
        })
    }

    /**
     * 向串口打印机发送数据
     * @param processData ProcessData
     * @param uiExecute UiExecute
     */
    public fun writeDataBySerialPort(processData: ProcessData, uiExecute: UiExecute) {

        add(posAsynncTask = PosAsynncTask(uiExecute, BackgroundInit {

            if (!enable) return@BackgroundInit false

            val list: List<ByteArray> = processData.processDataBeforeSend()

            for (i in list.indices) {
                send(list[i])
            }

            return@BackgroundInit enable

        }).apply {
            executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, *arrayOfNulls<Void>(0))
        })
    }

    public fun checkLink(uiExecute: UiExecute) {

        add(posAsynncTask = PosAsynncTask(uiExecute, BackgroundInit {

            return@BackgroundInit enable

        }).apply {
            executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, *arrayOfNulls<Void>(0))
        })

    }

    public fun disconnect(uiExecute: UiExecute) {

        add(posAsynncTask = PosAsynncTask(uiExecute, BackgroundInit {

            close()

            return@BackgroundInit !enable

        }).apply {
            executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, *arrayOfNulls<Void>(0))
        })

    }

    companion object {

        const val TAG = "PrinterSerialPortController"

        //保存所有的后台任务
        private val taskList by lazy { ArrayList<PosAsynncTask>() }

        private fun add(posAsynncTask: PosAsynncTask) {
            taskList.add(posAsynncTask)
        }

        //结束所有后台任务
        fun finishAllTask() {

            while (taskList.isNotEmpty()) {

                if (!taskList.first().isCancelled) {
                    taskList.first().cancel(true)
                }
                taskList.removeAt(0)
            }
        }
    }

}