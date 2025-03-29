package code.kafkaConsumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Consumer; // Import the Consumer interface

public class KafkaDynamicConsumer {

    private String bootstrapServers = "localhost:9092,localhost:9093";
    private String groupId;
    private String topic;

    private Properties properties;

    private final KafkaConsumer<String, String> consumer;


    public KafkaDynamicConsumer(String groupId, String topic) {
        this.groupId = groupId;
        this.topic = topic;
        this.properties = getProperties();
        this.consumer = new KafkaConsumer<>(properties);
    }

    public void Listen(
            Consumer<ConsumerRecord<String, String>> callback /*Accepts a callback*/
    ) {

        // Create consumer
        try (consumer) {

            // Subscribe consumer to our topic(s)
            consumer.subscribe(Arrays.asList(this.topic));

            // Poll for new data
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); // poll every 100 miliseconds.

                for (ConsumerRecord<String, String> record : records) {
                    callback.accept(record); // Invoke the callback with the record
                }
            }
        } catch (Exception e) {
            System.err.println("Error during Kafka consumption: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return properties;
    }
}