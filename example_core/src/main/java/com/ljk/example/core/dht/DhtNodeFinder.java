package com.ljk.example.core.dht;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author xkey  2019/6/27 9:44
 */
public class DhtNodeFinder implements Runnable {

    /**
     * DHT网络客户端
     */
    private DhtClient dhtClient;

    /**
     * 要寻找的node的Id
     */
    private byte[] nodeTargetId;

    /**
     * @see Runnable#run()
     */
    public void run() {
        try {
            List<DhtNode> nodeList = SpiderUtils.getNodesInfo(dhtClient.findNodeOnDHT(nodeTargetId));
            for (DhtNode node : nodeList) {
                DhtTable.appendNode(node);
                System.out.println(node);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public DhtNodeFinder(String ipAdress, int port, byte[] nodeTargetId) {
        this.dhtClient = new DhtClient(ipAdress, port);
        this.nodeTargetId = nodeTargetId;
    }

/*    public byte[] getNodeTargetId() {
        return nodeTargetId;
    }

    public DhtNodeFinder setNodeTargetId(byte[] nodeTargetId) {
        this.nodeTargetId = nodeTargetId;
        return this;
    }*/
}
