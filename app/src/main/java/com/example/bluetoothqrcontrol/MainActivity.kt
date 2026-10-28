package com.example.bluetoothqrcontrol

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val requestBluetoothEnable = 4101
    private val requestBluetoothPermission = 4102
    private var pendingEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
        handleDeepLink(intent?.data)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDeepLink(intent.data)
    }

    private fun showHome() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(36, 36, 36, 36)
        }
        root.addView(TextView(this).apply {
            text = "二维码蓝牙控制\n\n从网页按钮打开本 App。开启蓝牙会请求系统确认；关闭蓝牙会跳转到系统设置。"
            textSize = 18f
            gravity = Gravity.CENTER
        })
        root.addView(Button(this).apply {
            text = "打开蓝牙设置"
            setOnClickListener { openBluetoothSettings() }
        })
        setContentView(root)
    }

    private fun handleDeepLink(uri: Uri?) {
        if (uri == null || uri.scheme != "btcontrol") return
        when (uri.host?.lowercase()) {
            "on", "enable" -> requestEnableBluetooth()
            "off", "disable" -> {
                Toast.makeText(this, "请在系统蓝牙设置中关闭蓝牙", Toast.LENGTH_LONG).show()
                openBluetoothSettings()
            }
            else -> Toast.makeText(this, "未知指令。请使用 btcontrol://on 或 btcontrol://off", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestEnableBluetooth() {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            Toast.makeText(this, "此设备不支持蓝牙", Toast.LENGTH_LONG).show()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            pendingEnable = true
            requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), requestBluetoothPermission)
            return
        }
        if (adapter.isEnabled) {
            Toast.makeText(this, "蓝牙已经开启", Toast.LENGTH_SHORT).show()
            return
        }
        // Use the system confirmation UI; no silent Bluetooth toggle.
        startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), requestBluetoothEnable)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestBluetoothPermission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && pendingEnable) {
                pendingEnable = false
                requestEnableBluetooth()
            } else {
                pendingEnable = false
                Toast.makeText(this, "未授予附近设备权限，无法检查蓝牙状态", Toast.LENGTH_LONG).show()
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestBluetoothEnable) {
            val message = if (resultCode == RESULT_OK) "蓝牙开启请求已确认" else "蓝牙开启未确认"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openBluetoothSettings() {
        try {
            startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
        } catch (_: Exception) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }
}
