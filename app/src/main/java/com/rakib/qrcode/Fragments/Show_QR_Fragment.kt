package com.rakib.qrcode.Fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rakib.qrcode.Intents
import com.rakib.qrcode.QrCodeDatabase
import com.rakib.qrcode.R

class Show_QR_Fragment : Fragment() {

    private var qrType: String = ""
    private var qrData: String = ""
    private var qrDataType: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        activity?.window?.statusBarColor =  Color.parseColor("#ffffff");

        val bundle = this.arguments

        if (bundle != null) {
            qrType = arguments!!.getString("QrType").toString()
            qrData = arguments!!.getString("QrData").toString()
        }

        var database = QrCodeDatabase(requireContext(), "history")
        database.insertHistory(qrData, qrType)

        var view = inflater.inflate(R.layout.fragment_show__qr, container, false)

        val textQrType: TextView = view.findViewById(R.id.qr_type)
        val textQrData: TextView = view.findViewById(R.id.qr_data)

        val openButton: Button = view.findViewById(R.id.buttonOpen)
        val shareButton: Button = view.findViewById(R.id.buttonShare)

        textQrType.text = qrType
        var i = Intents()

        when (true) {
            qrType == "URL" -> textQrData.text = qrData
            qrType == "SMS" -> textQrData.text = i.editSMS(qrData)
            qrType == "Tel" -> textQrData.text = i.editTel(qrData)
            qrType == "Email" -> textQrData.text = i.editEmail(qrData)
            qrType == "WiFi" -> textQrData.text = i.editWiFi(qrData)
            qrType == "geo" -> textQrData.text = qrData
            else -> {
                textQrData.text = qrData
            }
        }

        openButton.setOnClickListener {
            when (true) {
                qrType == "URL" -> startActivity(i.urlActivity(qrData))
                qrType == "SMS" -> startActivity(i.smsActivity(qrData))
                qrType == "Tel" -> startActivity(i.phoneActivity(qrData))
                qrType == "Email" -> startActivity(i.emailActivity(qrData))
                qrType == "Location" -> startActivity(i.geoActivity(qrData))
                else -> {
                    textQrData.text = qrData
                }
            }
        }

        shareButton.setOnClickListener {
            when (true) {
                qrType == "URL" -> startActivity(i.shareData(qrData))
                qrType == "SMS" -> startActivity(i.shareSMS(qrData))
                qrType == "Tel" -> startActivity(i.sharePhone(qrData))
                qrType == "Email" -> startActivity(i.shareEmail(qrData))
                qrType == "WiFi" -> startActivity(i.shareWifi(qrData))
                else -> {
                    startActivity(i.shareData(qrData)) // 1
                }
            }
        }

        val backButton: ImageButton = view.findViewById(R.id.back_button)

        backButton.setOnClickListener() {
            val fragment = ScanCodeFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame, fragment)
                ?.commit();
        }
        return view
    }

    private fun openURL(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun openPhone(number: String) {
        //val phone = number.replace("tel:", "")
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(number)
        startActivity(intent)
    }

    private fun openEmail(data: String) {
        val msgCode = data.split(":")
        val intent = Intent(Intent.ACTION_SEND)
        val addressees = arrayOf(msgCode[2].split(";")[0])
        intent.putExtra(Intent.EXTRA_EMAIL, addressees)
        intent.putExtra(Intent.EXTRA_SUBJECT, msgCode[3].split(";")[0])
        intent.putExtra(Intent.EXTRA_TEXT, msgCode[4].split(";")[0])
        intent.type = "message/rfc822"
        startActivity(Intent.createChooser(intent, "Send Email using:"));
    }

    private fun openSMS(data: String) {
        val smsCode = data.split(":")
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:" + smsCode[1])
        intent.putExtra("sms_body", smsCode[2]);
        startActivity(intent)
    }

}

