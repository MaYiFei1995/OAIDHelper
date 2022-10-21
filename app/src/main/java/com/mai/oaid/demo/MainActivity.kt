package com.mai.oaid.demo

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mai.oaid.demo.databinding.ActivityMainBinding
import com.mai.oaid.helper.OAIDError
import com.mai.oaid.helper.OAIDHelper
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binder: ActivityMainBinding

    @SuppressLint("MissingPermission", "HardwareIds", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        OAIDHelper.get().useSdk(true).init(application, object : OAIDHelper.InitListener {

            override fun onSuccess(oaid: String?) {
                Log.e("MaiTest", "on init oaid success: $oaid")
                setText("OAID: $oaid")
            }

            override fun onFailure(error: OAIDError?) {
                Log.e("MaiTest", "on init oaid error: $error")
                val str = "<font color=\"#FF0000\">OAID: ${error?.errMsg}</font>"
                setText(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(str)
                    }
                )
            }

        })

        binder.uuid.text = "UUID: ${UUID.randomUUID().toString().replace("-", "")}"

        binder.androidID.text =
            "AndroidID: ${Settings.System.getString(contentResolver, Settings.Secure.ANDROID_ID)}"

        binder.imei.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                "IMEI: NOT_SUPPORT"
            } else {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    try {
                        "IMEI: ${(getSystemService(TELEPHONY_SERVICE) as TelephonyManager).deviceId}"
                    } catch (tr: Throwable) {
                        tr.printStackTrace()
                        "IMEI: error"
                    }
                } else {
                    "IMEI: PERMISSION NOT GRANTED"
                }
            }

        binder.oaid.setOnLongClickListener {
            val text = binder.oaid.text.toString()
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(
                ClipData.newPlainText(
                    "label",
                    text
                )
            )
            share(this, text)
            true
        }
    }

    private fun setText(text: CharSequence) {
        binder.oaid.post {
            binder.oaid.text = text
        }
    }

    /**
     * 调用系统分享
     */
    private fun share(context: Context, adbCmdStr: String? = null) {
        val intent = Intent(Intent.ACTION_SEND)
        if (adbCmdStr != null) {
            intent.putExtra(Intent.EXTRA_TEXT, adbCmdStr)
            intent.type = "text/plain"
        }
        context.startActivity(Intent.createChooser(intent, "发送至..."))
    }

}