package com.like.common.sample

import android.databinding.DataBindingUtil
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.common.sample.databinding.ActivityUdpBinding
import com.like.common.util.RxBusTag
import com.like.common.util.UDPClient
import com.like.rxbus.annotations.RxBusSubscribe

class UdpActivity : BaseActivity() {
    private val client: UDPClient = UDPClient(6000)

    private val udpRcvStrBuf = StringBuffer()
    private val udpSendStrBuf = StringBuffer()

    private val mBinding: ActivityUdpBinding by lazy {
        DataBindingUtil.setContentView<ActivityUdpBinding>(this, R.layout.activity_udp)
    }

    override fun getViewModel(): BaseViewModel? {
        mBinding.btnSend.isEnabled = false
        mBinding.btnCleanRecv.setOnClickListener {
            udpRcvStrBuf.delete(0, udpRcvStrBuf.length)
            mBinding.txtRecv.text = udpRcvStrBuf.toString()
        }
        mBinding.btnUdpConn.setOnClickListener {
            client.start()
            mBinding.btnUdpClose.isEnabled = true
            mBinding.btnUdpConn.isEnabled = false
            mBinding.btnSend.isEnabled = true
        }
        mBinding.btnSend.setOnClickListener {
            if (mBinding.editSend.text.toString() !== "") {
                client.send(mBinding.editSend.text.toString())
                udpSendStrBuf.append(mBinding.editSend.text.toString())
                mBinding.txtSend.text = udpSendStrBuf.toString()
            }
        }
        mBinding.btnUdpClose.setOnClickListener {
            client.close()
            mBinding.btnUdpConn.isEnabled = true
            mBinding.btnUdpClose.isEnabled = false
            mBinding.btnSend.isEnabled = false
        }
        return null
    }

    @RxBusSubscribe(RxBusTag.TAG_UDP_RECEIVE_SUCCESS)
    fun receiveUdpMessage(msgRcv: String) {
        udpRcvStrBuf.append(msgRcv)
        mBinding.txtRecv.text = udpRcvStrBuf.toString()
    }

}