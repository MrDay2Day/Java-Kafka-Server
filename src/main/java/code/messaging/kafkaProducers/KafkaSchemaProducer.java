package code.messaging.kafkaProducers;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class KafkaSchemaProducer {

    private String bootstrapServers = "localhost:9092,localhost:9093";
    private String schemaRegistryUrl = "http://localhost:8081"; // Replace with your schema registry URL
    private Properties properties;
    private final KafkaProducer<String, GenericRecord> producer;
    private Schema avroSchema;
    private String schemaName = "DemoSchema_001_AVRO";

    public KafkaSchemaProducer() {
        this.properties = getProperties();
        this.producer = new KafkaProducer<>(properties);
        try {
            String schemaString =
                    new String(Files.readAllBytes(Paths.get("src/main/java/code/messaging/schema/" + this.schemaName + ".avsc")));
            JSONObject jsonObject = new JSONObject(schemaString); // Create JSONObject from the string
            String avroSchemaJson = jsonObject.getString("schema"); // Extract the "schema" value
            this.avroSchema = new Schema.Parser().parse(avroSchemaJson); // Parse the schema
        } catch (Exception e) {
            System.err.println("Error loading schema: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error loading schema", e);
        }
    }

    public CompletableFuture<Void> produce(String topic, String key, GenericRecord value, Integer partition) {
        ProducerRecord<String, GenericRecord> record = new ProducerRecord<>(topic, partition, key, value);

        return CompletableFuture.runAsync(() -> {
            try {
                producer.send(record).get();
                System.out.println("Message sent: Key=" + key + ", Partition=" + partition);
            } catch (Exception e) {
                System.err.println("Error during Kafka production: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    public void close() {
        producer.close();
    }

    public Schema getAvroSchema() {
        return avroSchema;
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName()); // Use Avro serializer
        properties.setProperty(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, this.schemaRegistryUrl); // Add schema registry URL
        return properties;
    }

    public static void main(String[] args) {
        KafkaSchemaProducer producer = new KafkaSchemaProducer();

        // Loops to continuously produce messages
        while (true) {
            try {
                //Thread.sleep(100); // Sleep for 1 second

                GenericRecord record = new GenericData.Record(producer.getAvroSchema());
                record.put("info", "Test Info");
                record.put("active", true);
                record.put("textBuffer", ByteBuffer.wrap("Test Buffer".getBytes(StandardCharsets.UTF_8)));
                record.put("data", "Test Data " + System.currentTimeMillis());
                record.put("type", "Test Type");
                record.put("file", null); // Example with null file

                producer.produce("sub-topic", "key1", record, 0).join(); // Produce message
                producer.produce("sub-topic", "key2", record, 1).join(); // Produce message
                producer.produce("sub-topic", "key3", record, 2).join(); // Produce message
                producer.produce("sub-topic", "key4", record, 3).join(); // Produce message
                producer.produce("sub-topic", "key5", record, 4).join(); // Produce message
                producer.produce("sub-topic", "key6", record, 5).join(); // Produce message
                producer.produce("sub-topic", "key6", record, 5).join(); // Produce message
                producer.produce("sub-topic", "key6", record, 5).join(); // Produce message
                producer.produce("sub-topic", "key7", record, 6).join(); // Produce message
                producer.produce("sub-topic", "key8", record, 10).join(); // Produce message

            } catch (Exception e) {
                System.err.println("Kafka Error: " + e.getMessage());
                Thread.currentThread().interrupt();
                producer.close();

                break; // Exit the loop if interrupted
            }
        }
    }
}