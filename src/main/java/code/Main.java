package code;

import code.messaging.kafkaConsumers.KafkaDynamicConsumer;
import code.messaging.kafkaConsumers.KafkaSchemaConsumer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        /* ADVANCE KAFKA LISTENER WITH SCHEMA */
        KafkaSchemaConsumer avroConsumer = new KafkaSchemaConsumer("schema_topic", "sub-topic");
        avroConsumer.Listen(Main::callBackMethod);

        KafkaDynamicConsumer dynamicConsumer = new KafkaDynamicConsumer("schema_topic", "sub-topic");
        dynamicConsumer.listen(Main::callBackMethod);
    }

    private static void callBackMethod(ConsumerRecord<String, GenericRecord> record) {
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
                            long threadId = Thread.currentThread().getId();
                            asyncFunction(threadId); // Pass the thread ID to asyncFunction
                        }
                    }).start();
                }

            } else {
                System.out.println("Received a null record.");
            }

            // Add custom logic here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void asyncFunction(long threadName) {

        try {
            System.out.println("Async function started");

            TimeUnit.SECONDS.sleep(new Random().nextInt(10 - 2 + 1) + 2);

            System.out.println("Async function completed on Thread: " + threadName);
        } catch (Exception e) {
            System.err.println("Sleep interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

    }
}