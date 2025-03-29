package code.schema;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;

public class SchemaDownloadCreateClass {

    public static void main(String[] args) throws Exception {
        int schemaId = 7; // Replace with your schema ID
        String schemaRegistryUrl = "http://localhost:8081/schemas/ids/" + schemaId;
        String outputDirectory = "src/main/java/code/schema"; // Output directory for generated classes

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(schemaRegistryUrl))
                .header("Accept", "application/vnd.schemaregistry.v1+json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String schemaJson = response.body();
            Files.write(Paths.get("src/main/java/code/schema/schema_new.avsc"), schemaJson.getBytes());
            System.out.println("Schema downloaded to schema.avsc");
            generateAvroJavaClass("src/main/java/code/schema/schema_new.avsc", outputDirectory);

        } else {
            System.err.println("Failed to download schema: " + response.statusCode() + " - " + response.body());
        }
    }

    private static void generateAvroJavaClass(String avscFilePath, String outputDirectory) throws IOException, InterruptedException {
        String avroToolsJar = new File(System.getProperty("user.dir"), "target/avro-tools/avro-tools-1.11.3.jar").getAbsolutePath();
        System.out.println("Avro Tools JAR Path: " + avroToolsJar);
        System.out.println("Output Directory: " + outputDirectory); // added line

        // Extract Avro schema from JSON
        String avroSchemaJson = new String(Files.readAllBytes(Paths.get(avscFilePath)));
        JSONObject jsonObject = new JSONObject(avroSchemaJson);
        String avroSchemaString = jsonObject.getString("schema");

        // Create a temporary file with the extracted Avro schema
        File tempFile = File.createTempFile("avro_schema_", ".avsc");
        Files.write(tempFile.toPath(), avroSchemaString.getBytes());

        ProcessBuilder processBuilder = new ProcessBuilder(
                "java",
                "-jar",
                avroToolsJar,
                "compile",
                "schema",
                tempFile.getAbsolutePath(), // Use the temporary file
                outputDirectory
        );
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.err.println(line);
        }

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Avro Java class generated successfully in " + outputDirectory);
        } else {
            System.err.println("Failed to generate Avro Java class. Exit code: " + exitCode);
        }

        // Delete the temporary file
        tempFile.delete();
    }
}