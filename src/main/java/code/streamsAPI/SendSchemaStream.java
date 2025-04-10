package code.streamsAPI;

import code.messaging.schema.SchemaDownloader;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class SendSchemaStream {
    private static final String INPUT_TOPIC = "d2d-temp";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Producer
        produceMessages();

    }

    private static void produceMessages() throws InterruptedException, ExecutionException {
        Properties producerProps = new Properties();
        String schemaName = "DemoSchema_001_AVRO";
        String schemaRegistryUrl = "http://localhost:8081";
        Schema schema = SchemaDownloader.getSchema(schemaName);
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName()); // Use Avro serializer
        producerProps.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl); // Add schema registry URL

        try (KafkaProducer<String, GenericRecord> producer = new KafkaProducer<>(producerProps)) {
            int i = 0;
            while (true) {
                i++;

                GenericRecord record = new GenericData.Record(schema);
                record.put("info", "Test Info " + i);
                record.put("active", true);
                record.put("textBuffer", ByteBuffer.wrap(((i * i)  + " Test Buffer").getBytes(StandardCharsets.UTF_8)));
                record.put("data", "Test Data " + System.currentTimeMillis());
                record.put("type", "Test Type " + i);
                record.put("file", null);


                String key = "key-" + i;
                producer.send(new ProducerRecord<>(INPUT_TOPIC, key, record)).get();
                System.out.println("Produced: " + key + " - " + record.toString());
                //Thread.sleep(100);
            }
        }
    }


}
