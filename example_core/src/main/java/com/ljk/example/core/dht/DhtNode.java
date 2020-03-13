package com.ljk.example.core.dht;

import java.util.Arrays;

/**
 * @author xkey  2019/6/27 10:39
 */
public class DhtNode {
    private byte[] id;
    private String ip;
    private int port;

    DhtNode(byte[] id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    /*public DhtNode setIp(String ip) {
        this.ip = ip;
        return this;
    }*/

    public int getPort() {
        return port;
    }

    /*public DhtNode setPort(int port) {
        this.port = port;
        return this;
    }*/

    public byte[] getId() {
        return id;
    }

    /*public DhtNode setId(byte[] id) {
        this.id = id;
        return this;
    }*/

    @Override
    public String toString() {
        return "Node{" +
                "id=" + Arrays.toString(id) +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }


    /*public int compare(DhtNode o1, DhtNode o2) {
        if (o1 == o2) {
            return 0;
        } else if (o1 != null && o2 != null) {
            if (Objects.requireNonNull(SpiderUtils.bytesToHexString(o1.id)).compareTo(Objects.requireNonNull(SpiderUtils.bytesToHexString(o2.id))) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DhtNode node = (DhtNode) o;

        return Arrays.equals(id, node.id);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(id);
    }

    /*public int compareTo(DhtNode o) {
        if (this == o) {
            return 0;
        } else if (o != null) {
            if (Objects.requireNonNull(SpiderUtils.bytesToHexString(id)).compareTo(Objects.requireNonNull(SpiderUtils.bytesToHexString(o.id))) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }*/
}
