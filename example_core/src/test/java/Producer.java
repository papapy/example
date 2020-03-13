import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;

/**
 * @author xkey  2019/9/29 14:14
 */
public class Producer {
    public final static String TOPIC = "testTopic";

    public static void main(String[] args) {
        Properties p = new Properties();
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.24.170.194:9092");//kafka地址，多个地址用逗号分割
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(p);

        try {
            for(int index = 0; index < 1; index++) {
                String msg = "Hello," + new Random().nextInt(100);
                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, msg);
                kafkaProducer.send(record, (recordMetadata, e) -> {
                    if(null != e) {
                        e.printStackTrace();
                    } else {
                        System.out.println("消息发送成功:" + msg);
                    }
                });
            }
        } finally {
            kafkaProducer.close();
        }
    }
}
