package com.ljk.example.core.dht;

/**
 * @author xkey  2019/6/27 10:49
 */
public class SpiderConstant {
    /** Node ID最小值 */
    final static String NODE_ID_MIN = "0";

    /** Node ID最大值 */
    final static String NODE_ID_MAX = "1461501637330902918203684832716283019655932542975";

    /** Bucket内Node空间大小 */
    final static Integer BUCKET_NODE_SPACE = 8;

    /** Node数据传输大小 */
    final static Integer NODE_INFO_LENGTH_ON_DHT = 26;

    /** Node信息中ID末值索引 */
    final static Integer NODE_INFO_ID_LAST_INDEX = 19;

    /** Node信息中IP末值索引 */
    final static Integer NODE_INFO_IP_LAST_INDEX = 23;

    /** Node信息中PORT末值索引 */
    final static Integer NODE_INFO_PORT_LAST_INDEX = 25;
}
