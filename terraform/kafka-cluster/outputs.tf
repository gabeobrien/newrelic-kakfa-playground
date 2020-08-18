output "zookeeper_nodes" {
    value = module.kafka_cluster.zookeeper_nodes
}

output "kafka_brokers" {
    value = module.kafka_cluster.kafka_brokers
}
