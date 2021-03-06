package com.ljk.example.core.dht;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xkey  2019/6/27 10:44
 */
public class SpiderUtils {
    private static MessageDigest messageDigest;
    private static Random random;
    private static Pattern pattern = Pattern.compile("5:nodes(.*)", Pattern.DOTALL);

    private final static char[] DIGITS = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        random = new Random(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }

    public static String toUnsignedString(BigDecimal bigDecimal, int shift) {
        BigDecimal divisor = new BigDecimal(shift);
        Deque<Character> numberDeque = new ArrayDeque<Character>();
        do {
            BigDecimal[] ba = bigDecimal.divideAndRemainder(divisor);
            bigDecimal = ba[0];
            numberDeque.addFirst(DIGITS[ba[1].intValue()]);
        } while (bigDecimal.compareTo(BigDecimal.ZERO) > 0);
        StringBuilder builder = new StringBuilder();
        for (Character character : numberDeque) {
            builder.append(character);
        }
        return builder.toString();
    }

    public static byte[] buildNodeId() {
        byte[] bytes = new byte[20];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) random.nextInt(256);
        }
        messageDigest.update(bytes);
        return messageDigest.digest();
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static <T> List<T> extendArray(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<T>();
        for (T t : list1) {
            result.add(t);
        }
        for (T t : list2) {
            result.add(t);
        }
        return result;
    }

    public static List<DhtNode> getNodesInfo(String src) throws UnsupportedEncodingException {
        List<DhtNode> result = new ArrayList<>();
        if (src != null) {
            Matcher matcher = pattern.matcher(src);
            String nodesInfoStr = null;
            if (matcher.find()) {
                nodesInfoStr = matcher.group(1);
            }
            if (nodesInfoStr != null) {
                int start = nodesInfoStr.indexOf(":");
                int nodesInfoLength = Integer.parseInt(nodesInfoStr.substring(0, start));
                byte[] nodesInfo = new byte[nodesInfoLength];
                nodesInfoStr = nodesInfoStr.substring(start + 1);
                try {
                    byte[] nodesInfoSrc = nodesInfoStr.getBytes(StandardCharsets.UTF_8);
                    for (int i = 0; i < nodesInfoLength; i++) {
                        nodesInfo[i] = nodesInfoSrc[i];
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < nodesInfoLength / SpiderConstant.NODE_INFO_LENGTH_ON_DHT; i++) {
                    DhtNode node;
                    byte[] nodeId = new byte[20];
                    String nodeIp;
                    byte[] nodeIpBytes = new byte[4];
                    int nodePort;
                    byte[] nodePortBytes = new byte[2];
                    for (int j = i * SpiderConstant.NODE_INFO_LENGTH_ON_DHT; j < (i + 1) * SpiderConstant.NODE_INFO_LENGTH_ON_DHT; j++) {
                        if (j % SpiderConstant.NODE_INFO_LENGTH_ON_DHT <= SpiderConstant.NODE_INFO_ID_LAST_INDEX) {
                            nodeId[j % SpiderConstant.NODE_INFO_LENGTH_ON_DHT] = nodesInfo[j];
                        }
                        if (SpiderConstant.NODE_INFO_ID_LAST_INDEX < j % SpiderConstant.NODE_INFO_LENGTH_ON_DHT &&
                                j % SpiderConstant.NODE_INFO_LENGTH_ON_DHT <= SpiderConstant.NODE_INFO_IP_LAST_INDEX) {
                            nodeIpBytes[j % SpiderConstant.NODE_INFO_LENGTH_ON_DHT - SpiderConstant.NODE_INFO_ID_LAST_INDEX - 1] = nodesInfo[j];
                        }
                        if (SpiderConstant.NODE_INFO_IP_LAST_INDEX < j % SpiderConstant.NODE_INFO_LENGTH_ON_DHT &&
                                j % SpiderConstant.NODE_INFO_LENGTH_ON_DHT <= SpiderConstant.NODE_INFO_PORT_LAST_INDEX) {
                            nodePortBytes[j % SpiderConstant.NODE_INFO_LENGTH_ON_DHT - SpiderConstant.NODE_INFO_IP_LAST_INDEX - 1] = nodesInfo[j];
                        }
                    }
                    long ip_temp = Long.parseLong(bytesToHexString(nodeIpBytes), 16);
                    nodeIp = long2IpAdress(ip_temp);
                    nodePort = Integer.parseInt(bytesToHexString(nodePortBytes), 16);
                    node = new DhtNode(nodeId, nodeIp, nodePort);
                    result.add(node);
                }
            }
        }
        return result;
    }

    public static String long2IpAdress(long src) {
        long l = 256 * 256 * 256;
        StringBuffer stringBuffer = new StringBuffer();
        while (l > 0) {
            stringBuffer.append(src / l).append(".");
            src = src % l;
            l /= 256;
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    public static <T> ArrayList<T> removeSameInArray(List<T> list) {
        return new ArrayList<T>(new LinkedHashSet<T>(list));
    }
}
