package code;

import code.kafka.KafkaDynamicConsumer;
import code.kafka.KafkaSchemaConsumer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

//        KafkaDynamicConsumer consumer_1 = new KafkaDynamicConsumer(
//                "my-consumer-group",
//                "ping");
//
//        consumer_1.Listen((ConsumerRecord<String, String> record) -> {
//            // This is your callback function
//            System.out.println("Received message:");
//            System.out.println("Key: " + record.key() + ", Value: " + record.value());
//            System.out.println("Partition: " + record.partition() + ", Offset: " + record.offset());
//            // Add your custom logic to process the Kafka message here
//        });


        KafkaSchemaConsumer avroConsumer = new KafkaSchemaConsumer("schema_topic", "sub-topic");

        avroConsumer.Listen((ConsumerRecord<String, GenericRecord> record) -> {
            try {
                // Process the GenericRecord
                GenericRecord avroRecord = record.value();

                // Access Avro fields
                if (avroRecord != null) {
                    System.out.println("Received message:");
                    System.out.println("Key: " + record.key());
                    System.out.println("Value: " + avroRecord.toString()); // Print the full record, or access specific fields.
                    // Example of accessing a field called "name"
                    if(avroRecord.get("info") != null){
                        System.out.println("Information: " + avroRecord.get("info").toString());
                    }
                } else {
                    System.out.println("Received a null record.");
                }

                // Add custom logic here
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}