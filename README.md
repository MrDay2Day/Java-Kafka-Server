# Java Kafka Examples

A Java Maven Kafka example with both simple and advanced configurations provides a valuable learning experience for developers working with Apache Kafka. The simple version, utilizing basic string serialization, demonstrates the fundamental concepts of producing and consuming messages. This allows users to grasp the core mechanics of Kafka without the added complexity of schema management. It serves as an excellent starting point for understanding topics, partitions, producers, and consumers in a straightforward manner.

The advanced version, incorporating Avro schema registry, showcases a more robust and scalable approach to Kafka development. By integrating Avro for data serialization and the schema registry for schema management, the example highlights best practices for handling complex data structures in a distributed environment. This version emphasizes the importance of schema evolution, data consistency, and efficient data serialization, which are critical for building reliable and maintainable Kafka-based applications. It demonstrates how to leverage the schema registry to decouple producers and consumers, enabling independent evolution of data schemas while maintaining compatibility.

## Kafka Streams API

The project also includes examples of Kafka Streams API usage, demonstrating how to process and transform data in real-time as it flows through Kafka topics. The Streams API examples showcase:

1. Basic stream processing with transformation operations
2. Parallel processing with multiple stream instances
3. Stream-to-stream transformations
4. Producer and consumer implementations specifically for Streams API

Kafka Streams API provides a simple and powerful way to build real-time applications and microservices that process and analyze data stored in Kafka. The examples illustrate how to configure stream processing applications, handle state, and manage the lifecycle of stream processing tasks.

## Project Structure

```
Kafka Server/
├── .idea/
├── src/
│   └── main/
│       ├── java/
│       │   └── code/
│       │       ├── Main.java
│       │       ├── messaging/
│       │       │   ├── kafkaConsumers/
│       │       │   │   ├── KafkaDynamicConsumer.java
│       │       │   │   ├── KafkaSchemaConsumer.java
│       │       │   │   └── KafkaSimpleConsumer.java
│       │       │   ├── kafkaProducers/
│       │       │   │   ├── KafkaDynamicProducer.java
│       │       │   │   ├── KafkaSchemaProducer.java
│       │       │   │   └── KafkaSimpleProducer.java
│       │       │   └── schema/
│       │       │       ├── [[SCHEMA_NAME]].avsc << (Schemas are downloaded from registry)
│       │       │       ├── SchemaDownloadCreateClass.java
│       │       │       └── SchemaDownloader.java
│       │       └── streamsAPI/
│       │           ├── ProcessStream.java
│       │           ├── SendStream.java
│       │           └── StreamAPIExample.java
│       └── resources/
│           └── application.properties
├── test/
├── target/
│   ├── .gitignore
│   └── pom.xml
├── External Libraries/
└── README.md
```