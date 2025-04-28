package code.admin;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.acl.*;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.apache.kafka.common.resource.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class KafkaAdmin {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093";
    private static final String TOPIC_NAME = "stream_input_topic";
    private static final int NUM_PARTITIONS = 3;
    private static final short REPLICATION_FACTOR = 2;
    private static final long RETENTION_MS = 7 * 24 * 60 * 60 * 1000L; // 7 days in milliseconds
    private static final int MAX_MESSAGE_SIZE = 1048576; // 1 MB
    private static final String CLEANUP_POLICY = "delete"; // or "compact"

    private final AdminClient adminClient;

    public KafkaAdmin() {
        Properties adminClientConfig = new Properties();
        adminClientConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        this.adminClient = AdminClient.create(adminClientConfig);
    }

    public static void main(String[] args) {
        KafkaAdmin kafkaAdmin = new KafkaAdmin();

        try {
            // Example usage of various methods
            kafkaAdmin.createTopic(TOPIC_NAME, NUM_PARTITIONS, REPLICATION_FACTOR, getDefaultTopicConfig());
            kafkaAdmin.listTopics();
            kafkaAdmin.describeTopic(TOPIC_NAME);
            kafkaAdmin.updateTopicConfig(TOPIC_NAME, "retention.ms", String.valueOf(14 * 24 * 60 * 60 * 1000L));
            kafkaAdmin.updateTopicConfig(TOPIC_NAME, "cleanup.policy", "compact,delete");
            kafkaAdmin.updateTopicPartitions(TOPIC_NAME, 6);

            // ACL Management example
            // kafkaAdmin.createTopicAcl(TOPIC_NAME, "User:alice", AclOperation.READ);
            // kafkaAdmin.createTopicAcl(TOPIC_NAME, "User:bob", AclOperation.WRITE);
            // kafkaAdmin.listAcls();

            // Delete ACL example
            // kafkaAdmin.deleteTopicAcl(TOPIC_NAME, "User:alice", AclOperation.READ);

            // Note: Uncomment to delete the topic
            // kafkaAdmin.deleteTopic(TOPIC_NAME);
        } finally {
            kafkaAdmin.close();
        }
    }

    /**
     * Create a topic with specified configurations
     */
    public void createTopic(String topicName, int numPartitions, short replicationFactor, Map<String, String> configs) {
        NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor)
                .configs(configs);

        try {
            CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singletonList(newTopic));
            createTopicsResult.all().get();
            System.out.println("Topic '" + topicName + "' created successfully.");
        } catch (ExecutionException e) {
            if (e.getCause() instanceof TopicExistsException) {
                System.out.println("Topic '" + topicName + "' already exists.");
            } else {
                System.err.println("Error creating topic: " + e);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while creating topic: " + e);
        }
    }

    /**
     * Delete an existing topic
     */
    public void deleteTopic(String topicName) {
        try {
            DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singletonList(topicName));
            deleteTopicsResult.all().get();
            System.out.println("Topic '" + topicName + "' deleted successfully.");
        } catch (ExecutionException e) {
            if (e.getCause() instanceof UnknownTopicOrPartitionException) {
                System.err.println("Topic '" + topicName + "' does not exist.");
            } else {
                System.err.println("Error deleting topic: " + e);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while deleting topic: " + e);
        }
    }

    /**
     * List all available topics in the Kafka cluster
     */
    public Set<String> listTopics() {
        try {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            Set<String> topicNames = listTopicsResult.names().get();
            System.out.println("Available topics: " + topicNames);
            return topicNames;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while listing topics: " + e);
        } catch (ExecutionException e) {
            System.err.println("Error listing topics: " + e);
        }
        return Collections.emptySet();
    }

    /**
     * Get detailed information about a specific topic
     */
    public void describeTopic(String topicName) {
        try {
            DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singletonList(topicName));
            Map<String, TopicDescription> topicDescriptionMap = describeTopicsResult.allTopicNames().get();

            TopicDescription description = topicDescriptionMap.get(topicName);
            System.out.println("Topic: " + description.name());
            System.out.println("  Internal: " + description.isInternal());
            System.out.println("  Partitions: " + description.partitions().size());

            for (TopicPartitionInfo partitionInfo : description.partitions()) {
                System.out.println("  Partition " + partitionInfo.partition() + ":");
                System.out.println("    Leader: " + partitionInfo.leader().id());
                System.out.println("    Replicas: " + partitionInfo.replicas().stream()
                        .map(node -> String.valueOf(node.id()))
                        .collect(Collectors.joining(", ")));
                System.out.println("    ISRs: " + partitionInfo.isr().stream()
                        .map(node -> String.valueOf(node.id()))
                        .collect(Collectors.joining(", ")));
            }

            // Get configurations as well
            ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
            DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Collections.singletonList(configResource));
            Config config = describeConfigsResult.all().get().get(configResource);

            System.out.println("  Configurations:");
            config.entries().forEach(entry -> {
                if (!entry.isDefault() && !entry.isSensitive()) {
                    System.out.println("    " + entry.name() + ": " + entry.value());
                }
            });

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while describing topic: " + e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof UnknownTopicOrPartitionException) {
                System.err.println("Topic '" + topicName + "' does not exist.");
            } else {
                System.err.println("Error describing topic: " + e);
            }
        }
    }

    /**
     * Update a specific topic configuration
     */
    public void updateTopicConfig(String topicName, String configName, String configValue) {
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
        ConfigEntry configEntry = new ConfigEntry(configName, configValue);

        Map<ConfigResource, Collection<AlterConfigOp>> alterConfigOps = new HashMap<>();
        alterConfigOps.put(configResource, Collections.singletonList(
                new AlterConfigOp(configEntry, AlterConfigOp.OpType.SET)
        ));

        try {
            AlterConfigsResult alterConfigsResult = adminClient.incrementalAlterConfigs(alterConfigOps);
            alterConfigsResult.all().get();
            System.out.println("Configuration '" + configName + "' updated to '" + configValue + "' for topic '" + topicName + "'");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while updating topic configuration: " + e);
        } catch (ExecutionException e) {
            System.err.println("Error updating topic configuration: " + e);
        }
    }

    /**
     * Update multiple topic configurations at once
     */
    public void updateTopicConfigs(String topicName, Map<String, String> configs) {
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);

        Collection<AlterConfigOp> alterConfigOps = configs.entrySet().stream()
                .map(entry -> new AlterConfigOp(
                        new ConfigEntry(entry.getKey(), entry.getValue()),
                        AlterConfigOp.OpType.SET))
                .collect(Collectors.toList());

        Map<ConfigResource, Collection<AlterConfigOp>> configOps = Collections.singletonMap(configResource, alterConfigOps);

        try {
            AlterConfigsResult alterConfigsResult = adminClient.incrementalAlterConfigs(configOps);
            alterConfigsResult.all().get();
            System.out.println("Configurations updated for topic '" + topicName + "'");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while updating topic configurations: " + e);
        } catch (ExecutionException e) {
            System.err.println("Error updating topic configurations: " + e);
        }
    }

    /**
     * Update the number of partitions for a topic
     * Note: Can only increase partitions, not decrease
     */
    public void updateTopicPartitions(String topicName, int newPartitionCount) {
        try {
            // First, get current partition count
            DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singletonList(topicName));
            Map<String, TopicDescription> topicDescriptionMap = describeTopicsResult.allTopicNames().get();
            int currentPartitions = topicDescriptionMap.get(topicName).partitions().size();

            if (newPartitionCount <= currentPartitions) {
                System.err.println("New partition count must be greater than current count (" + currentPartitions + ")");
                return;
            }

            Map<String, NewPartitions> newPartitionsMap = Collections.singletonMap(
                    topicName, NewPartitions.increaseTo(newPartitionCount)
            );

            CreatePartitionsResult createPartitionsResult = adminClient.createPartitions(newPartitionsMap);
            createPartitionsResult.all().get();
            System.out.println("Increased partitions for topic '" + topicName + "' from " + currentPartitions + " to " + newPartitionCount);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while updating partitions: " + e);
        } catch (ExecutionException e) {
            System.err.println("Error updating partitions: " + e);
        }
    }

    /*
     * Create an ACL for a topic
     */
//    public void createTopicAcl(String topicName, String principal, AclOperation operation) {
//        ResourcePattern resourcePattern = new ResourcePattern(
//                ResourceType.TOPIC,
//                topicName,
//                PatternType.LITERAL
//        );
//
//        AccessControlEntry accessControlEntry = new AccessControlEntry(
//                principal,
//                "*",  // any host
//                operation,
//                AclPermissionType.ALLOW
//        );
//
//        AclBinding aclBinding = new AclBinding(resourcePattern, accessControlEntry);
//
//        try {
//            CreateAclsResult createAclsResult = adminClient.createAcls(Collections.singletonList(aclBinding));
//            createAclsResult.all().get();
//            System.out.println("Created ACL for principal '" + principal + "' with operation '" + operation + "' on topic '" + topicName + "'");
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            System.err.println("Interrupted while creating ACL: " + e);
//        } catch (ExecutionException e) {
//            System.err.println("Error creating ACL: " + e);
//        }
//    }

    /*
     * Delete an ACL for a topic
     */
//    public void deleteTopicAcl(String topicName, String principal, AclOperation operation) {
//        AclBindingFilter aclBindingFilter = new AclBindingFilter(
//                new ResourcePatternFilter(ResourceType.TOPIC, topicName, PatternType.LITERAL),
//                new AccessControlEntryFilter(principal, "*", operation, AclPermissionType.ALLOW)
//        );
//
//        try {
//            DeleteAclsResult deleteAclsResult = adminClient.deleteAcls(Collections.singletonList(aclBindingFilter));
//            Collection<AclBinding> deletedAcls = new ArrayList<>();
//
//            for (Map.Entry<AclBindingFilter, KafkaFuture<DeleteAclsResult.FilterResults>> entry : deleteAclsResult.all().get().entrySet()) {
//                DeleteAclsResult.FilterResults results = entry.getValue().get();
//                results.acls().forEach(deletedAcls::add);
//            }
//
//            System.out.println("Deleted " + deletedAcls.size() + " ACLs for topic '" + topicName + "':");
//            deletedAcls.forEach(acl -> System.out.println("  " + acl));
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            System.err.println("Interrupted while deleting ACL: " + e);
//        } catch (ExecutionException e) {
//            System.err.println("Error deleting ACL: " + e);
//        }
//    }


    /**
     * List all ACLs in the cluster
     */
    public void listAcls() {
        AclBindingFilter aclBindingFilter = AclBindingFilter.ANY;

        try {
            DescribeAclsResult describeAclsResult = adminClient.describeAcls(aclBindingFilter);
            Collection<AclBinding> aclBindings = describeAclsResult.values().get();

            System.out.println("Current ACLs:");
            for (AclBinding aclBinding : aclBindings) {
                ResourcePattern resource = aclBinding.pattern();
                AccessControlEntry entry = aclBinding.entry();
                System.out.println("  Resource: Type=" + resource.resourceType() + ", Name=" + resource.name() +
                        ", Principal=" + entry.principal() + ", Operation=" + entry.operation() +
                        ", Permission=" + entry.permissionType());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while listing ACLs: " + e);
        } catch (ExecutionException e) {
            System.err.println("Error listing ACLs: " + e);
        }
    }

    /**
     * Get default topic configurations
     */
    private static Map<String, String> getDefaultTopicConfig() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", String.valueOf(RETENTION_MS));
        configs.put("retention.bytes", String.valueOf(MAX_MESSAGE_SIZE * 100)); // 100 MB
        configs.put("cleanup.policy", CLEANUP_POLICY);
        return configs;
    }

    /**
     * Close the admin client
     */
    public void close() {
        if (adminClient != null) {
            adminClient.close();
            System.out.println("AdminClient closed.");
        }
    }
}