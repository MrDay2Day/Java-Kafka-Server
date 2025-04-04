package code;

import code.kafkaConsumers.KafkaDynamicConsumer;
import code.kafkaConsumers.KafkaSchemaConsumer;
import code.kafkaProducers.KafkaSchemaProducer;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        boolean useSchema = true;

        if(useSchema){
            /* ADVANCE KAFKA LISTENER WITH SCHEMA */
            KafkaSchemaConsumer avroConsumer = new KafkaSchemaConsumer("schema_topic", "sub-topic");

            avroConsumer.Listen((ConsumerRecord<String, GenericRecord> record) -> {
                try {
                    // Process the GenericRecord
                    GenericRecord avroRecord = record.value();

                    // Access Avro fields
                    if (avroRecord != null) {
                        System.out.println("Received message:");
                        System.out.println("Key: " + record.key());
                        System.out.println("Offset: " + record.offset());
                        System.out.println("Partition: " + record.partition());
                        System.out.println("Value: " + avroRecord.toString()); // Print the full record, or access specific fields.
                        // Example of accessing a field called "name"
                        if(avroRecord.get("info") != null){
                            System.out.println("Information: " + avroRecord.get("info").toString());

                            // Creating a new thread to run asyncFunction
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    asyncFunction();
                                }
                            }).start(); // Start the thread (non-blocking)
                        }

                    } else {
                        System.out.println("Received a null record.");
                    }

                    // Add custom logic here
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        } else {
            /* SIMPLE KAFKA LISTENER */
            KafkaDynamicConsumer consumer_1 = new KafkaDynamicConsumer(
            "my-consumer-group",
            "ping");

            consumer_1.Listen((ConsumerRecord<String, String> record) -> {
                // This is the callback function
                System.out.println("Received message:");
                System.out.println("Key: " + record.key());
                System.out.println("Value: " + record.value());
                System.out.println("Partition: " + record.partition() + ", Offset: " + record.offset());
                // Add custom logic to process the Kafka record here
            });
        }
    }

    public static void asyncFunction(){

        try {
            System.out.println("Async function started");

            TimeUnit.SECONDS.sleep(new Random().nextInt(10 - 2 + 1) + 2);

            System.out.println("Async function completed");
        } catch (Exception e) {
            System.err.println("Sleep interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

    }
}