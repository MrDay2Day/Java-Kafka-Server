package code.kafkaConsumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaSimpleConsumer {

    public void connect(String[] args) {

        String bootstrapServers = "localhost:9092,localhost:9093"; // Replace with your Kafka broker address
        String groupId = "my-consumer-group";
        String topic = "ping";

        // Create consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Create consumer
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {

            // Subscribe consumer to our topic(s)
            consumer.subscribe(Arrays.asList(topic));

            // Poll for new data
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); // poll every 100 miliseconds.

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Key: " + record.key() + ", Value: " + record.value());
                    System.out.println("Partition: " + record.partition() + ", Offset: " + record.offset());
                }
            }
        } catch (Exception e) {
            System.err.println("Error during Kafka consumption: " + e.getMessage());
            e.printStackTrace();
        }
    }
}