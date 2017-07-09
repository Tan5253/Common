package com.like.common.sample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Handler
import android.os.Message
import android.util.Log
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityUdpBinding
import com.like.common.util.UDPClient
import java.util.concurrent.Executors


class UdpActivity : BaseActivity() {
    private var client: UDPClient = UDPClient(this)
    private val myHandler = MyHandler()
    private val udpRcvStrBuf = StringBuffer()
    private val udpSendStrBuf = StringBuffer()

    private val mBinding: ActivityUdpBinding by lazy {
        DataBindingUtil.setContentView<ActivityUdpBinding>(this, R.layout.activity_udp)
    }

    private inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    udpRcvStrBuf.append(msg.obj.toString())
                    mBinding.txtRecv.text = udpRcvStrBuf.toString()
                }
                2 -> {
                    udpSendStrBuf.append(msg.obj.toString())
                    mBinding.txtSend.text = udpSendStrBuf.toString()
                }
                3 -> mBinding.txtRecv.text = udpRcvStrBuf.toString()
            }
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.hasExtra("udpRcvMsg")) {
                val message = Message()
                message.obj = intent.getStringExtra("udpRcvMsg")
                message.what = 1
                Log.i("主界面Broadcast", "收到" + message.obj.toString())
                myHandler.sendMessage(message)
            }
        }
    }

    override fun getViewModel(): BaseViewModel? {
        val udpRcvIntentFilter = IntentFilter("udpRcvMsg")
        registerReceiver(broadcastReceiver, udpRcvIntentFilter)
        mBinding.btnSend.isEnabled = false
        mBinding.btnCleanRecv.setOnClickListener {
            udpRcvStrBuf.delete(0, udpRcvStrBuf.length)
            val message = Message()
            message.what = 3
            myHandler.sendMessage(message)
        }
        mBinding.btnUdpConn.setOnClickListener {
            //建立线程池
            val exec = Executors.newCachedThreadPool()
            exec.execute(client)
            mBinding.btnUdpClose.isEnabled = true
            mBinding.btnUdpConn.isEnabled = false
            mBinding.btnSend.isEnabled = true
        }
        mBinding.btnSend.setOnClickListener {
            val thread = Thread(Runnable {
                val message = Message()
                message.what = 2
                if (mBinding.editSend.text.toString() !== "") {
                    client.send(mBinding.editSend.text.toString())
                    message.obj = mBinding.editSend.text.toString()
                    myHandler.sendMessage(message)
                }
            })
            thread.start()
        }
        mBinding.btnUdpClose.setOnClickListener {
            client.udpLife = false
            mBinding.btnUdpConn.isEnabled = true
            mBinding.btnUdpClose.isEnabled = false
            mBinding.btnSend.isEnabled = false
        }
        return null
    }
}