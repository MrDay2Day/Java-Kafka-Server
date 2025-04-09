package code.streamsAPI;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class SendStream {

    private static final String INPUT_TOPIC = "d2d-temp";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Producer
        produceMessages();

    }

    private static void produceMessages() throws InterruptedException, ExecutionException {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass());

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)) {
            int i = 0;
            while (true) {
                i++;
                String key = "key-" + i;
                String value = "value-" + i;
                producer.send(new ProducerRecord<>(INPUT_TOPIC, key, value)).get();
                System.out.println("Produced: " + key + " - " + value);
                //Thread.sleep(100);
            }
        }
    }


}
