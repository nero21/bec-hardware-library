package com.bec.hardwarelibrary.printer

import android.os.AsyncTask
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
class PrinterSerialPortController {

    private var serialPortController: SerialPortController? = null

    /**
     * 连接打印机
     * @param serialPortDevice SerialPortDevice
     * @param uiExecute UiExecute
     */
    public fun connect(serialPortDevice: SerialPortDevice, uiExecute: UiExecute) {

        add(posAsynncTask = PosAsynncTask(uiExecute, BackgroundInit {

            serialPortController = SerialPortController(serialPortDevice = serialPortDevice)

            serialPortController?.open() ?: return@BackgroundInit false

            return@BackgroundInit serialPortController?.enable ?: false

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

            val list: List<ByteArray> = processData.processDataBeforeSend()

            return@BackgroundInit serialPortController?.let {

                if (!it.enable) return@let false

                for (i in list.indices) {
                    it.send(list[i])
                }

                it.enable

            } ?: return@BackgroundInit false

        }).apply {
            executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, *arrayOfNulls<Void>(0))
        })
    }

    public fun checkLink(uiExecute: UiExecute) {

        add(posAsynncTask = PosAsynncTask(uiExecute, BackgroundInit {

            return@BackgroundInit serialPortController?.enable ?: false

        }).apply {
            executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, *arrayOfNulls<Void>(0))
        })

    }

    public fun disconnect(uiExecute: UiExecute) {

        add(posAsynncTask = PosAsynncTask(uiExecute, BackgroundInit {

            return@BackgroundInit serialPortController?.let {
                it.close()
                true
            } ?: return@BackgroundInit false

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