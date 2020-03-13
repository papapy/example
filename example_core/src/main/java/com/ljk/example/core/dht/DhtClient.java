package com.ljk.example.core.dht;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * @author xkey  2019/6/27 9:45
 */
public class DhtClient {
    /**
     * udp发送器
     */
    private DatagramSocket sender = null;

    /**
     * ip地址
     */
    private InetAddress inetAddress = null;
    private int port = -1;
    private DatagramPacket sendPacket;

    public DhtClient(String ipAdress, int port) {
        try {
            sender = new DatagramSocket();
            sender.setSoTimeout(3000);
            this.inetAddress = InetAddress.getByName(ipAdress);
            this.port = port;
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

    private String sendData(String data) {
        if (checkFieldStatus()) {
            sendPacket = new DatagramPacket(data.getBytes(), data.getBytes().length, inetAddress, port);
            try {
                sender.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytes = new byte[10240];
            DatagramPacket recvPacket = new DatagramPacket(bytes, bytes.length);
            try {
                sender.receive(recvPacket);
                return new String(recvPacket.getData(), 0, recvPacket.getLength(), StandardCharsets.UTF_8);
            } catch (SocketTimeoutException e) {
                System.out.println(inetAddress.toString() + ":" + port + " connected time out.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String findNodeOnDHT(byte[] nodeId) {
        String data = "d1:ad2:id20:" + new String(DhtTable.getId()) + "6:target20:" + new String(nodeId) + "1:q9:find_node1:t2:aa1:y1:qe";
        return sendData(data);
    }

    /**
     * 检查客户端状态
     */
    private boolean checkFieldStatus() {
        return sender != null && inetAddress != null && port != -1;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public DhtClient setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
        return this;
    }

    public int getPort() {
        return port;
    }

    public DhtClient setPort(int port) {
        this.port = port;
        return this;
    }

    public DatagramSocket getSender() {
        return sender;
    }

    public DhtClient setSender(DatagramSocket sender) {
        this.sender = sender;
        return this;
    }

    public DatagramPacket getSendPacket() {
        return sendPacket;
    }

    public DhtClient setSendPacket(DatagramPacket sendPacket) {
        this.sendPacket = sendPacket;
        return this;
    }
}
