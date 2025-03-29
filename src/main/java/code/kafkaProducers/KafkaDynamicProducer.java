package code.kafkaProducers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class KafkaDynamicProducer {

    private String bootstrapServers = "localhost:9092,localhost:9093";
    private Properties properties;
    private final KafkaProducer<String, String> producer;

    public KafkaDynamicProducer() {
        this.properties = getProperties();
        this.producer = new KafkaProducer<>(properties);
    }

    public CompletableFuture<Void> produce(String topic, String key, String value, Integer partition) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, key, value);

        return CompletableFuture.runAsync(() -> {
            try {
                producer.send(record).get(); // Use .get() to wait for completion
                System.out.println("Message sent: Key=" + key + ", Value=" + value + ", Partition=" + partition); // Print partition
            } catch (Exception e) {
                System.err.println("Error during Kafka production: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e); // Propagate the exception
            }
        });
    }

    public void close() {
        producer.close();
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public static void main(String[] args) {
        KafkaDynamicProducer producer = new KafkaDynamicProducer();

        CompletableFuture.allOf(
                        producer.produce("ping", "key1", "hello world", 0),
                        producer.produce("ping", "key2", "another message", 1),
                        producer.produce("ping", "key3", "yet another message", 2)
                ).thenRun(producer::close)
                .join(); // Wait for all messages to be sent and close the producer
    }
}