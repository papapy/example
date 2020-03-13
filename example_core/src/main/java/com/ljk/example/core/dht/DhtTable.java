package com.ljk.example.core.dht;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xkey  2019/6/27 10:40
 */
public class DhtTable {
    /**
     * 路由表的id
     */
    private static byte[] id;

    /**
     * 路由表内的桶
     */
    private static List<DhtBucket> buckets;

    /**
     * 桶最大最小值列表
     */
    private static List<BigDecimal> bucketsFlagList;

    static {
        id = SpiderUtils.buildNodeId();
        buckets = new ArrayList<>();
        bucketsFlagList = new ArrayList<>();
        DhtBucket bucket = new DhtBucket(new BigDecimal(SpiderConstant.NODE_ID_MIN), new BigDecimal(SpiderConstant.NODE_ID_MAX));
        buckets.add(bucket);
    }

    private DhtTable() {

    }

    public static void appendNode(DhtNode node) {
        if (Arrays.equals(id, node.getId())) {
            return;
        }
        int index = getBucketIndex(node.getId());
        DhtBucket bucket = buckets.get(index);
        try {
            bucket.appendNode(node);
            System.out.println(node);
        } catch (Exception e) {
            if (!bucket.nodeIdInRange(node.getId())) {
                return;
            }
            spilt_bucket(index);
            appendNode(node);
        }
    }

    public static List<DhtNode> findCloseNodes(byte[] targetId) {
        List<DhtNode> nodes = new ArrayList<>();
        if (buckets.size() == 0) {
            return nodes;
        }
        int index = getBucketIndex(targetId);
        nodes = buckets.get(index).getNodes();
        int smallBucketIndex = index - 1;
        int bigBucketIndex = index + 1;
        while (nodes.size() < SpiderConstant.BUCKET_NODE_SPACE && (smallBucketIndex > 0 || bigBucketIndex < buckets.size())) {
            if (smallBucketIndex > 0) {
                nodes = SpiderUtils.extendArray(nodes, buckets.get(smallBucketIndex).getNodes());
            }
            if (bigBucketIndex <= buckets.size()) {
                nodes = SpiderUtils.extendArray(nodes, buckets.get(bigBucketIndex).getNodes());
            }
            smallBucketIndex--;
            bigBucketIndex++;
        }
//        Collections.sort(nodes);
        return nodes;
    }

    private static void spilt_bucket(int index) {
        DhtBucket oldBucket = buckets.get(index);
        BigDecimal point = oldBucket.getMax().subtract((oldBucket.getMax().subtract(oldBucket.getMin())).divide(new BigDecimal(2))).setScale(0, BigDecimal.ROUND_DOWN);
        DhtBucket newBucket = new DhtBucket(new BigDecimal(point.toString()).add(new BigDecimal("1")), oldBucket.getMax());
        oldBucket.setMax(new BigDecimal(point.toString()));
        for (DhtNode node : oldBucket.getNodes()) {
            if (newBucket.nodeIdInRange(node.getId())) {
                newBucket.getNodes().add(node);
            }
        }
        for (DhtNode node : newBucket.getNodes()) {
            oldBucket.getNodes().remove(node);
        }
        buckets.add(index, newBucket);
    }

    private static int getBucketIndex(byte[] id) {
        for (DhtBucket bucket : buckets) {
            if (bucket.nodeIdInRange(id)) {
                return buckets.indexOf(bucket);
            }
        }
        return buckets.size() - 1;
    }

    public static byte[] getId() {
        return id;
    }

    public static void setId(byte[] id) {
        DhtTable.id = id;
    }

    public static List<DhtBucket> getBuckets() {
        return buckets;
    }

    public static void setBuckets(List<DhtBucket> buckets) {
        DhtTable.buckets = buckets;
    }

    public static List<BigDecimal> getBucketsFlagList() {
        return bucketsFlagList;
    }

    public static void setBucketsFlagList(List<BigDecimal> bucketsFlagList) {
        DhtTable.bucketsFlagList = bucketsFlagList;
    }
}
