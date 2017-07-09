package com.like.common.util

import android.content.Context
import android.content.Intent
import android.util.Log
import java.io.IOException
import java.net.*


class UDPClient(val context: Context) : Runnable {
    companion object {
        private val udpPort = 6000
        private val hostIp = "192.168.1.102"
        private val socket: DatagramSocket = DatagramSocket()
    }

    var udpLife = true //udp生命线程
    private val msgRcv = ByteArray(1024) //接收消息

    //发送消息
    fun send(msgSend: String): String {
        var hostAddress: InetAddress? = null

        try {
            hostAddress = InetAddress.getByName(hostIp)
        } catch (e: UnknownHostException) {
            Log.i("udpClient", "未找到服务器")
            e.printStackTrace()
        }

        /*        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            Log.i("udpClient","建立发送数据报失败");
            e.printStackTrace();
        }*/

        val packetSend = DatagramPacket(msgSend.toByteArray(), msgSend.toByteArray().size, hostAddress, udpPort)

        try {
            socket.send(packetSend)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("udpClient", "发送失败")
        }

        //   socket.close();
        return msgSend
    }

    override fun run() {
        try {
            socket.soTimeout = 3000
        } catch (e: SocketException) {
            Log.i("udpClient", "建立接收数据报失败")
            e.printStackTrace()
        }

        val packetRcv = DatagramPacket(msgRcv, msgRcv.size)
        while (udpLife) {
            try {
                Log.i("udpClient", "UDP监听")
                socket.receive(packetRcv)
                val RcvMsg = String(packetRcv.data, packetRcv.offset, packetRcv.length)
                //将收到的消息发给主界面
                val RcvIntent = Intent()
                RcvIntent.action = "udpRcvMsg"
                RcvIntent.putExtra("udpRcvMsg", RcvMsg)
                context.sendBroadcast(RcvIntent)

                Log.i("Rcv", RcvMsg)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        Log.i("udpClient", "UDP监听关闭")
        socket.close()
    }

}