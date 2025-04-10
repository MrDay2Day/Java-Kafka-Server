package code.streamsAPI;

import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.KStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class ProcessSchemaStream {

    private static final String INPUT_TOPIC = "d2d-temp";
    private static final String OUTPUT_TOPIC = "sub-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093";
    private static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CountDownLatch latch = new CountDownLatch(4);

        new Thread(() -> processMessages(1, latch)).start();
        new Thread(() -> processMessages(2, latch)).start();
        new Thread(() -> processMessages(3, latch)).start();
        new Thread(() -> processMessages(4, latch)).start();

        latch.await();
    }

    private static void processMessages(int streamCount, CountDownLatch latch) {
        Properties streamsProps = new Properties();
        streamsProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-app-" + streamCount);
        streamsProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        streamsProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        streamsProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        streamsProps.put("schema.registry.url", SCHEMA_REGISTRY_URL);
        streamsProps.put(StreamsConfig.CLIENT_ID_CONFIG, "client-" + streamCount);
        // Add the error handler configuration:
        streamsProps.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);

        Map<String, String> serdeConfig = new HashMap<>();
        serdeConfig.put("schema.registry.url", SCHEMA_REGISTRY_URL);

        GenericAvroSerde genericAvroSerde = new GenericAvroSerde();
        genericAvroSerde.configure(serdeConfig, false);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, GenericRecord> source = builder.stream(INPUT_TOPIC);

        source.mapValues(value -> {
            try {
                Thread.sleep(100);
                System.out.println("Stream " + streamCount + " processing: " + value.toString());
                return value;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).to(OUTPUT_TOPIC);

        KafkaStreams streams = new KafkaStreams(builder.build(), streamsProps);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            streams.close();
            latch.countDown();
        }));

        streams.start();
    }
}