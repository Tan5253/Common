package com.like.common.sample

import android.databinding.DataBindingUtil
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivitySocketBinding
import com.like.common.util.RxBusTag
import com.like.common.util.TCPClient
import com.like.common.util.UDPClient
import com.like.rxbus.annotations.RxBusSubscribe

class SocketActivity : BaseActivity() {
    private val udpClient: UDPClient = UDPClient(10000)
    private val tcpClient: TCPClient = TCPClient(8000)

    private val udpRcvStrBuf = StringBuffer()
    private val udpSendStrBuf = StringBuffer()

    private val mBinding: ActivitySocketBinding by lazy {
        DataBindingUtil.setContentView<ActivitySocketBinding>(this, R.layout.activity_socket)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding.btnSend.isEnabled = false
        mBinding.btnCleanRecv.setOnClickListener {
            udpRcvStrBuf.delete(0, udpRcvStrBuf.length)
            mBinding.txtRecv.text = udpRcvStrBuf.toString()
        }
        mBinding.btnUdpConn.setOnClickListener {
            udpClient.start()
            mBinding.btnUdpClose.isEnabled = true
            mBinding.btnUdpConn.isEnabled = false
            mBinding.btnSend.isEnabled = true
        }
        mBinding.btnSend.setOnClickListener {
            if (mBinding.editSend.text.toString() !== "") {
                tcpClient.send(mBinding.editSend.text.toString())
                udpSendStrBuf.append(mBinding.editSend.text.toString())
                mBinding.txtSend.text = udpSendStrBuf.toString()
            }
        }
        mBinding.btnUdpClose.setOnClickListener {
            udpClient.close()
            tcpClient.close()
            mBinding.btnUdpConn.isEnabled = true
            mBinding.btnUdpClose.isEnabled = false
            mBinding.btnSend.isEnabled = false
        }
        return null
    }

    @RxBusSubscribe(RxBusTag.TAG_UDP_RECEIVE_SUCCESS)
    fun udpReceivedMessage(udpMessage: UDPClient.UDPMessage) {
        udpRcvStrBuf.append("服务器ip地址：$udpMessage\n")
        mBinding.txtRecv.text = udpRcvStrBuf.toString()
        udpClient.close()
        tcpClient.close()
        tcpClient.start(udpMessage.ip)
    }

    @RxBusSubscribe(RxBusTag.TAG_TCP_RECEIVE_SUCCESS)
    fun tcpReceivedMessage(message: String) {
        udpRcvStrBuf.append(message)
        mBinding.txtRecv.text = udpRcvStrBuf.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        udpClient.close()
        tcpClient.close()
    }

}