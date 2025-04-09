package code.messaging.kafkaProducers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaSimpleProducer {

    public void produce(String topic, String key, String value) {

        String bootstrapServers = "localhost:9092,localhost:9093"; // Replace with your Kafka broker address

        // Create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create producer
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {

            // Create a producer record
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

            // Send data - asynchronous
            producer.send(record);

            // flush and close producer
            producer.flush();
            System.out.println("Message sent: Key=" + key + ", Value=" + value);
        } catch (Exception e) {
            System.err.println("Error during Kafka production: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        KafkaSimpleProducer producer = new KafkaSimpleProducer();
        producer.produce("ping", "key1", "hello world");
        producer.produce("ping", "key2", "another message");
    }
}