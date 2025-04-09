package code.messaging.kafkaConsumers;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class KafkaDynamicConsumer {

    private final String bootstrapServers;
    private final String groupId;
    private final String topic;
    private final Properties properties;
    private final KafkaConsumer<String, GenericRecord> consumer;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Duration pollDuration;

    private final String schemaRegistryUrl; // Replace with your schema registry URL

    public KafkaDynamicConsumer(String bootstrapServers, String groupId, String topic,
                                Duration pollDuration, String schemaRegistryUrl, Properties additionalConfig) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.topic = topic;
        this.pollDuration = pollDuration;
        this.properties = getProperties();
        this.schemaRegistryUrl = schemaRegistryUrl;

        // Apply any additional configuration if provided
        if (additionalConfig != null) {
            this.properties.putAll(additionalConfig);
        }

        this.consumer = new KafkaConsumer<>(properties);

        // Add shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public KafkaDynamicConsumer(String groupId, String topic) {
        this("localhost:9092,localhost:9093", groupId, topic,
                Duration.ofMillis(100), "http://localhost:8081", null);
    }

    public void listen(Consumer<ConsumerRecord<String, GenericRecord>> callback) {
        try {
            consumer.subscribe(Collections.singletonList(this.topic));

            while (!closed.get()) {
                try {
                    ConsumerRecords<String, GenericRecord> records = consumer.poll(pollDuration);

                    for (ConsumerRecord<String, GenericRecord> record : records) {
                        try {
                            callback.accept(record);
                        } catch (Exception e) {
                            // Handle callback processing errors
                            System.err.println("Error processing record: " + e.getMessage());
                            // Depending on your error handling strategy, you might:
                            // - continue processing other records
                            // - commit offset before the problematic record
                            // - send to a dead letter queue
                            // - etc.
                        }
                    }

                    // Option to add explicit commit if auto-commit is disabled
                    // consumer.commitSync();
                } catch (Exception e) {
                    if (!closed.get()) {
                        System.err.println("Error during poll: " + e.getMessage());
                    }
                }
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) throw e;
        } finally {
            consumer.close();
        }
    }

    public void close() {
        closed.set(true);
        consumer.wakeup();
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName()); // Use Avro deserializer
        properties.setProperty(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, this.schemaRegistryUrl); // Add schema registry URL
        properties.setProperty(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "false"); // Use GenericRecord

        // Consider explicitly setting these important properties
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return properties;
    }
}