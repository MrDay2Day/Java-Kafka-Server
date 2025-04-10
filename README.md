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

## 📄 License

[MIT License](LICENSE)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
