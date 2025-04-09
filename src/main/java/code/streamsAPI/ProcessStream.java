package code.streamsAPI;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProcessStream {

    private static final String INPUT_TOPIC = "d2d-temp";
    private static final String OUTPUT_TOPIC = "messages";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Kafka Streams - Multiple streams processing from the same topic
        // Each stream will process the same input topic and produce to the same output topic
        //`Kafka will automatically distribute the load across the available instances of the application stream
        processMessages(1);
        processMessages(2);
        processMessages(3);
        processMessages(4);
    }

    private static void processMessages(int streamCount) {
        Properties streamsProps = new Properties();
        streamsProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-app");
        streamsProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        streamsProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        streamsProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        streamsProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream(INPUT_TOPIC);

        source.mapValues(value -> {
                    // Process the value here or pass value to another method that returns a processed value
                    try {
                        Thread.sleep(100);
                        System.out.println("Stream " + streamCount + " processing: " + value);
                        return "Processed: " + value;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                })
                .to(OUTPUT_TOPIC);

        KafkaStreams streams = new KafkaStreams(builder.build(), streamsProps);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
