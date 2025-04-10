package code.messaging.schema;

import org.apache.avro.Schema;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class SchemaDownloader {

    public static void main(String[] args) throws Exception {
        int schemaId = 7;
        String schemaName = "DemoSchema_001_AVRO";
        String schemaRegistryUrl = "http://localhost:8081/schemas/ids/" + schemaId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(schemaRegistryUrl))
                .header("Accept", "application/vnd.schemaregistry.v1+json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String schemaJson = response.body();
            Files.write(Paths.get("src/main/java/code/schema/"+ schemaName + ".avsc"), schemaJson.getBytes());
            System.out.println("Schema downloaded to schema.avsc");
        } else {
            System.err.println("Failed to download schema: " + response.statusCode() + " - " + response.body());
        }
    }

    public static Schema getSchema(String schemaName) {
            try {
                String schemaString =
                        new String(Files.readAllBytes(Paths.get("src/main/java/code/messaging/schema/" + schemaName + ".avsc")));
                JSONObject jsonObject = new JSONObject(schemaString); // Create JSONObject from the string
                String avroSchemaJson = jsonObject.getString("schema"); // Extract the "schema" value
                Schema schema = new Schema.Parser().parse(avroSchemaJson);
                // Print the schema for debugging
                System.out.println("Schema loaded: " + schema.toString(true));
                return schema;
            } catch (Exception e) {
                System.err.println("Error loading schema: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error loading schema", e);
            }
    }
}