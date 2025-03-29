package code.kafkaConsumers;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.generic.GenericRecord; // Import GenericRecord for Avro
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Consumer;

public class KafkaSchemaConsumer {

    private String bootstrapServers = "localhost:9092,localhost:9093";
    private String schemaRegistryUrl = "http://localhost:8081"; // Replace with your schema registry URL
    private String groupId;
    private String topic;

    private Properties properties;

    private final KafkaConsumer<String, GenericRecord> consumer; // Change value deserializer to GenericRecord

    public KafkaSchemaConsumer(String groupId, String topic) {
        this.groupId = groupId;
        this.topic = topic;
        this.properties = getProperties();
        this.consumer = new KafkaConsumer<>(properties);
    }

    public void Listen(Consumer<ConsumerRecord<String, GenericRecord>> callback) { // Change callback type

        try (consumer) {
            consumer.subscribe(Arrays.asList(this.topic));

            while (true) {
                ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, GenericRecord> record : records) {
                    callback.accept(record);
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
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName()); // Use Avro deserializer
        properties.setProperty(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, this.schemaRegistryUrl); // Add schema registry URL
        properties.setProperty(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "false"); // Use GenericRecord
        return properties;
    }
}