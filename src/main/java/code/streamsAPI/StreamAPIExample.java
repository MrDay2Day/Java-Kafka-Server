package code.streamsAPI;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class StreamAPIExample {

    private static final String INPUT_TOPIC = "d2d-temp";
    private static final String OUTPUT_TOPIC = "messages";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Producer
        produceMessages();

        // Kafka Streams
        processMessages();
    }

    private static void produceMessages() throws InterruptedException, ExecutionException {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass());

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)) {
            for (int i = 0; i < 10; i++) {
                String key = "key-" + i;
                String value = "value-" + i;
                producer.send(new ProducerRecord<>(INPUT_TOPIC, key, value)).get();
                System.out.println("Produced: " + key + " - " + value);
                Thread.sleep(100);
            }
        }
    }

    private static void processMessages() {
        Properties streamsProps = new Properties();
        streamsProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-app");
        streamsProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        streamsProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        streamsProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        streamsProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream(INPUT_TOPIC);

        source.mapValues(value -> "Processed: " + value)
                .to(OUTPUT_TOPIC);

        KafkaStreams streams = new KafkaStreams(builder.build(), streamsProps);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
