package com.karacasoft.cmpe443carprojectcontroller

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothWriter
import com.github.douglasjunior.bluetoothlowenergylibrary.BluetoothLeService
import java.io.*
import java.util.*

class CarManager(private val bluetoothDevice: BluetoothDevice) {
    private val TAG = "CarManager"


    private var onBluetoothDataRead : ((buffer: ByteArray?, length: Int) -> Unit)? = null

    fun setOnBluetoothDataRead(onDataRead : ((buffer: ByteArray?, length: Int) -> Unit)?) {
        onBluetoothDataRead = onDataRead
    }

    fun init() {
        val service = BluetoothService.getDefaultInstance()

        service.setOnEventCallback(object : BluetoothService.OnBluetoothEventCallback {
            override fun onDataRead(buffer: ByteArray?, length: Int) {
                onBluetoothDataRead?.invoke(buffer, length)
            }

            override fun onStatusChange(status: BluetoothStatus?) {

            }

            override fun onDataWrite(buffer: ByteArray?) {

            }

            override fun onToast(message: String?) {

            }

            override fun onDeviceName(deviceName: String?) {

            }

        })
    }

    fun connect() {
        val service = BluetoothService.getDefaultInstance()
        service.connect(bluetoothDevice)
    }

    fun disconnect() {
        BluetoothService.getDefaultInstance().disconnect()
    }

    fun writeData(data: String) {
        val service = BluetoothService.getDefaultInstance()
        val writer = BluetoothWriter(service)

        writer.write(data + "\r\n")
    }

    companion object CarManager {
        private val uuid = UUID.fromString("0000FFE0-0000-1000-8000-00805f9b34fb")
        private val rx = UUID.fromString("0000FFE1-0000-1000-8000-00805f9b34fb")
        var selectedDevice: BluetoothDevice? = null

        fun initService(ctx: Context) {
            val cfg = BluetoothConfiguration()
            cfg.context = ctx.applicationContext
            cfg.bluetoothServiceClass = BluetoothLeService::class.java
            cfg.bufferSize = 1024
            cfg.characterDelimiter = '\n'
            cfg.deviceName = "Wow"
            cfg.callListenersInMainThread = true

            cfg.uuidService = uuid
            cfg.uuidCharacteristic = rx

            BluetoothService.init(cfg)
        }
    }

}