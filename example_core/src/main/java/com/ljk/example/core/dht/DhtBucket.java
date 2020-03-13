package com.ljk.example.core.dht;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author xkey  2019/6/27 10:41
 */
public class DhtBucket {
    /**
     * 最小值
     */
    private BigDecimal min;

    /**
     * 最大值
     */
    private BigDecimal max;

    /**
     * 桶中节点列表
     */
    private List<DhtNode> nodes;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastAccessedTime;

    /**
     * 构造函数
     *
     * @param min 桶中节点hash最小值
     * @param max 桶中节点hash最大值
     */
    public DhtBucket(BigDecimal min, BigDecimal max) {
        this.min = min;
        this.max = max;
        DhtTable.getBucketsFlagList().add(min);
        DhtTable.getBucketsFlagList().add(max);
        DhtTable.setBucketsFlagList(SpiderUtils.removeSameInArray(DhtTable.getBucketsFlagList()));
        Collections.sort(DhtTable.getBucketsFlagList());
        nodes = new ArrayList<>();
        lastAccessedTime = LocalDateTime.now();
    }

    /**
     * 判断节点是否处于桶范围
     *
     * @param id 节点id
     * @return 节点是否处于桶范围
     */
    public boolean nodeIdInRange(byte[] id) {
        String hexId = SpiderUtils.bytesToHexString(id);
        return SpiderUtils.toUnsignedString(min, 16).compareTo(hexId) <= 0 && SpiderUtils.toUnsignedString(max, 16).compareTo(hexId) >= 0;
    }

    /**
     * 添加节点
     *
     * @param node 节点
     */
    public void appendNode(DhtNode node) {
        assert node.getId().length == 20;
        if (nodes.size() < 8) {
            for (DhtNode myNode : nodes) {
                if (Arrays.equals(myNode.getId(), node.getId())) {
                    nodes.remove(myNode);
                    break;
                }
            }
            nodes.add(node);
            lastAccessedTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("BucketFull");
        }
    }

    /**
     * method for get min
     */
    public BigDecimal getMin() {
        return min;
    }

    /**
     * method for set min
     */
    public void setMin(BigDecimal min) {
        this.min = min;
    }

    /**
     * method for get max
     */
    public BigDecimal getMax() {
        return max;
    }

    /**
     * method for set max
     */
    public void setMax(BigDecimal max) {
        this.max = max;
    }

    /**
     * method for get nodes
     */
    public List<DhtNode> getNodes() {
        return nodes;
    }

    /**
     * method for set nodes
     */
    public void setNodes(List<DhtNode> nodes) {
        this.nodes = nodes;
    }

    /**
     * method for get lastAccessedTime
     */
    public LocalDateTime getlastAccessedTime() {
        return lastAccessedTime;
    }

    /**
     * method for set lastAccessedTime
     */
    public void setLastAccessedTime(LocalDateTime lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }
}
