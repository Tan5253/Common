package com.like.common.sample

import android.databinding.DataBindingUtil
import android.os.Handler
import android.os.Message
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityUdpBinding
import com.like.common.util.RxBusTag
import com.like.common.util.UDPClient
import com.like.rxbus.annotations.RxBusSubscribe
import java.util.concurrent.Executors

class UdpActivity : BaseActivity() {
    private var client: UDPClient = UDPClient(this)
    private val mHandler = android.os.Handler(Handler.Callback { msg ->
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
        true
    })

    private val udpRcvStrBuf = StringBuffer()
    private val udpSendStrBuf = StringBuffer()

    private val mBinding: ActivityUdpBinding by lazy {
        DataBindingUtil.setContentView<ActivityUdpBinding>(this, R.layout.activity_udp)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding.btnSend.isEnabled = false
        mBinding.btnCleanRecv.setOnClickListener {
            udpRcvStrBuf.delete(0, udpRcvStrBuf.length)
            val message = Message()
            message.what = 3
            mHandler.sendMessage(message)
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
                    mHandler.sendMessage(message)
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

    @RxBusSubscribe(RxBusTag.TAG_UDP_RECEIVE_SUCCESS)
    fun receiveUdpMessage(msgRcv: String) {
        val message = Message()
        message.obj = msgRcv
        message.what = 1
        mHandler.sendMessage(message)
    }

}