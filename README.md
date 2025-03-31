# Java Kafka Examples

A Java Maven Kafka example with both simple and advanced configurations provides a valuable learning experience for developers working with Apache Kafka. The simple version, utilizing basic string serialization, demonstrates the fundamental concepts of producing and consuming messages. This allows users to grasp the core mechanics of Kafka without the added complexity of schema management. It serves as an excellent starting point for understanding topics, partitions, producers, and consumers in a straightforward manner.

The advanced version, incorporating Avro schema registry, showcases a more robust and scalable approach to Kafka development. By integrating Avro for data serialization and the schema registry for schema management, the example highlights best practices for handling complex data structures in a distributed environment. This version emphasizes the importance of schema evolution, data consistency, and efficient data serialization, which are critical for building reliable and maintainable Kafka-based applications. It demonstrates how to leverage the schema registry to decouple producers and consumers, enabling independent evolution of data schemas while maintaining compatibility.

```
Kafka Server/
├── .idea/
├── src/
│   └── main/
│       ├── java/
│       │   └── code/
│       │       ├── Main.java
│       │       ├── code.kafkaConsumers/
│       │       │   ├── KafkaDynamicConsumer.java
│       │       │   ├── KafkaSchemaConsumer.java
│       │       │   └── KafkaSimpleConsumer.java
│       │       ├── code.kafkaProducers/
│       │       │   ├── KafkaDynamicProducer.java
│       │       │   ├── KafkaSchemaProducer.java
│       │       │   └── KafkaSimpleProducer.java
│       │       └── code.schema/
│       │           ├── [[SCHEMA_NAME]].avsc << (Schemas are downloaded from registry)
│       │           ├── SchemaDownloadCreateClass.java
│       │           └── SchemaDownloader.java
│       └── resources/
│           └── application.properties
├── test/
├── target/
│   ├── .gitignore
│   └── pom.xml
├── External Libraries/
└── README.md
```
