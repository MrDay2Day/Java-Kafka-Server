# Kafka Java Project

A comprehensive Java application demonstrating various Kafka integration patterns using Apache Kafka, Schema Registry, and Kafka Streams API.

## 📋 Overview

This project showcases a robust implementation of Kafka producers and consumers in Java, featuring:

- Simple and advanced Kafka producers/consumers
- Schema Registry integration with Avro schemas
- Dynamic topic subscription
- Kafka Streams API processing
- Multi-threading support
- Asynchronous messaging patterns

## 🔧 Technologies

- **Java 21**
- **Apache Kafka 3.7.1**
- **Confluent Platform 7.5.1**
- **Apache Avro 1.11.4**
- **Kafka Streams API**
- **Maven**

## 🏗️ Project Structure

```
Kafka Server/
├── src/
│   └── main/
│       ├── java/
│       │   └── code/
│       │       ├── Main.java
│       │       ├── messaging/
│       │       │   ├── kafkaConsumers/
│       │       │   │   ├── KafkaDynamicConsumer.java     (Dynamic topic subscription)
│       │       │   │   ├── KafkaSchemaConsumer.java      (Schema registry consumer)
│       │       │   │   └── KafkaSimpleConsumer.java      (Simple consumer implementation)
│       │       │   ├── kafkaProducers/
│       │       │   │   ├── KafkaDynamicProducer.java     (Dynamic topic subscription)
│       │       │   │   ├── KafkaSchemaProducer.java      (Schema registry producer)
│       │       │   │   └── KafkaSimpleProducer.java      (Simple producer implementation)
│       │       │   ├── admin/
│       │       │   │   └── KafkaAdmin.java               (Simple adminnistration implementation)
│       │       │   └── schema/
│       │       │       ├── [[SCHEMA_NAME]].avsc          (Schemas downloaded from registry)
│       │       │       ├── SchemaDownloadCreateClass.java
│       │       │       └── SchemaDownloader.java
│       │       └── streamsAPI/
│       │           ├── ProcessSchemaStream.java
│       │           ├── SendSchemaStream.java
│       │           └── StreamAPIExample.java
│       └── resources/
│           └── application.properties
```

## ✨ Features

### Kafka Admin
- **Topic Creation**: Create topics with specified configurations
- **Topic Deletion**: Delete existing topics
- **Topic Listing**: List all available topics in the Kafka cluster
- **Topic Description**: Get detailed information about a specific topic
- **Topic Configuration**: Update topic configurations
- **Topic Partition Management**: Increase or decrease the number of partitions for a topic
- **Topic Replication Factor Management**: Change the replication factor for a topic
- **Topic Cleanup Policy Management**: Update the cleanup policy for a topic
- **Topic Retention Policy Management**: Change the retention policy for a topic
- **Topic ACL Management**: Manage Access Control Lists (ACLs) for topics

### Kafka Producers

- **Simple Producer**: Basic string key-value message publishing
- **Dynamic Producer**: Flexible producer with partition selection and async completion
- **Schema Producer**: Avro schema-based producer with Schema Registry integration

### Kafka Consumers

- **Simple Consumer**: Basic string message consumer
- **Dynamic Consumer**: Advanced consumer with configurable poll intervals and graceful shutdown
- **Schema Consumer**: Avro schema-based consumer with Schema Registry integration

### Schema Management

- **Schema Downloader**: Downloads Avro schemas from Schema Registry
- **Schema Class Generator**: Generates Java classes from Avro schemas

### Kafka Streams

- **Stream Processing**: Demonstrates Kafka Streams API for message transformation
- **Multi-threaded Streams**: Parallel processing of messages from input to output topics

## 🚀 Getting Started

### Prerequisites

- Java 21
- Maven
- Running Kafka cluster (local or remote)
- Running Schema Registry

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/MrDay2Day/kafka-java-project.git
   cd kafka-java-project
   ```

2. Update Kafka broker addresses and Schema Registry URL in the code if needed:
    - Default broker addresses: `localhost:9092,localhost:9093`
    - Default Schema Registry URL: `http://localhost:8081`

3. Build the project:
   ```bash
   mvn clean package
   ```

## 📝 Usage Examples

### Basic Admin Operations

This module provides administrative capabilities for Apache Kafka using the Kafka Admin Client API. It allows for common Kafka administrative tasks such as creating topics and modifying topic configurations.

#### Features

- Create new Kafka topics with custom configurations
- Alter existing topic configurations (retention policies, cleanup policies, etc.)
- Error handling for common scenarios like duplicate topics

#### Configuration Options

The utility comes with the following default configurations that can be modified in the code:

| Parameter | Default Value | Description |
|-----------|---------------|-------------|
| BOOTSTRAP_SERVERS | localhost:9092,localhost:9093 | Comma-separated list of Kafka broker addresses |
| TOPIC_NAME | stream_input_topic | Name of the topic to create/modify |
| NUM_PARTITIONS | 3 | Number of partitions for the topic |
| REPLICATION_FACTOR | 2 | Replication factor for the topic |
| RETENTION_MS | 7 days | Default message retention period |
| MAX_MESSAGE_SIZE | 1MB | Maximum message size |
| CLEANUP_POLICY | delete | Topic cleanup policy (delete or compact) |

#### Usage

1. Ensure your Kafka cluster is running and accessible
2. Update the configuration variables in the code if needed
3. Run the application:

```bash
java -cp <classpath> code.admin.KafkaAdmin
```

#### Example Operations

#### Creating a Topic

The utility will create a new topic with specified configurations:
- Topic name: stream_input_topic
- Partitions: 3
- Replication factor: 2
- Initial configurations:
   - Retention bytes: 100MB
   - Cleanup policy: delete

#### Modifying Topic Configuration

After creating the topic, the utility modifies:
- Retention period to 14 days
- Changes cleanup policy to "compact"

#### Extending Functionality

You can extend this utility by adding more administrative operations such as:
- Listing all topics
- Describing topic details
- Deleting topics
- Managing consumer groups
- Managing ACLs

Look for the comment "You can add more operations here" in the code to add your own implementations.

#### Error Handling

The utility includes error handling for common scenarios:
- Topics that already exist
- Configuration errors
- Interruption during operations

[Add your license information here]






### Basic Producer-Consumer

```java
// Run a simple producer
KafkaSimpleProducer producer = new KafkaSimpleProducer();
producer.produce("messages", "key1", "hello world");

// Run a simple consumer
KafkaSimpleConsumer consumer = new KafkaSimpleConsumer();
consumer.connect(args);
```

### Avro Schema-based Producer-Consumer

```java
// Producer with Schema Registry
KafkaSchemaProducer schemaProducer = new KafkaSchemaProducer();
GenericRecord record = new GenericData.Record(schemaProducer.getAvroSchema());
record.put("info", "Test Info");
record.put("active", true);
schemaProducer.produce("sub-topic", "key1", record, 0);

// Consumer with Schema Registry
KafkaSchemaConsumer avroConsumer = new KafkaSchemaConsumer("schema_topic", "sub-topic");
avroConsumer.Listen(record -> {
    GenericRecord avroRecord = record.value();
    System.out.println("Information: " + avroRecord.get("info").toString());
});
```

### Kafka Streams Processing

```java
// Initialize and start a Kafka Stream
Properties streamsProps = new Properties();
streamsProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-app");
streamsProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> source = builder.stream(INPUT_TOPIC);

source.mapValues(value -> "Processed: " + value)
        .to(OUTPUT_TOPIC);

KafkaStreams streams = new KafkaStreams(builder.build(), streamsProps);
streams.start();
```


## ⚙️ Configuration

The project uses Maven for dependency management. Key dependencies include:

- kafka-clients
- kafka-streams
- kafka-avro-serializer
- kafka-streams-avro-serde
- avro

## 🛠️ Advanced Features

### Dynamic Topic Subscription

The `KafkaDynamicConsumer` and `KafkaDynamicProducer` classes demonstrate dynamic topic subscription, allowing runtime configuration of topics and partitions.

### Graceful Shutdown

Proper shutdown handling is implemented in consumers and stream processors:

```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    closed.set(true);
    consumer.wakeup();
}));
```

### Multi-threading

The project demonstrates multi-threaded processing using both Java threads and Kafka Streams:

```java
CountDownLatch latch = new CountDownLatch(4);
new Thread(() -> processMessages(1, latch)).start();
new Thread(() -> processMessages(2, latch)).start();
// ...
latch.await();
```

### Asynchronous Processing

Asynchronous handling with CompletableFuture:

```java
CompletableFuture<Void> produce(String topic, String key, String value, Integer partition) {
    return CompletableFuture.runAsync(() -> {
        // Producer logic
    });
}
```


### Multi-Thread Synchronization

This is done using `CountDownLatch` which is part of the Java Concurrency API (specifically, the `java.util.concurrent` package).

The `java.util.concurrent` API in Java is a powerful set of tools designed to simplify and enhance the development of concurrent applications. In essence, it provides developers with high-level abstractions and utilities for managing threads, coordinating their activities, and ensuring thread safety.

```java
import java.util.concurrent.CountDownLatch;
...
CountDownLatch latch = new CountDownLatch(4);
```
The `CountDownLatch` serves as a synchronization mechanism for coordinating multiple threads. Specifically, in the `ProcessSchemaStream` class, it's used to manage the lifecycle of multiple Kafka Streams instances.

```java
CountDownLatch latch = new CountDownLatch(4);

new Thread(() -> processMessages(1, latch)).start();
new Thread(() -> processMessages(2, latch)).start();
new Thread(() -> processMessages(3, latch)).start();
new Thread(() -> processMessages(4, latch)).start();

latch.await();
```

1. **Initialization:** The `CountDownLatch(4)` creates a latch initialized with a count of 4, representing the four threads you're starting.
1. **Thread Management:** Each thread runs the `processMessages()` method with its own ID and a reference to the latch.
1. **Blocking Mechanism:** The `latch.await()` call blocks the main thread until the latch's count reaches zero. This prevents the main program from exiting while your stream processing threads are still active.
1. **Countdown Mechanism:** Inside each thread, you have:

    ```java
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        streams.close();
        latch.countDown();
    }));
    ```
   When each stream is closed (during application shutdown), the `latch.countDown()` decrements the latch count. Once all four streams have been closed and their respective `countDown()` methods called, the latch count reaches zero.
1. **Coordinated Shutdown:** This ensures all streams are properly closed before the application completely terminates, preventing resource leaks and data loss.

This pattern is particularly useful for Kafka Streams applications where you want to ensure all stream processing completes or shuts down properly before your application exits.


## 📄 License

[MIT License](LICENSE)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
