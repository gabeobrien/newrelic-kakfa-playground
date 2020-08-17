resource "aws_instance" "zookeeper" {
    ami = var.amis[var.aws_region]
    instance_type = var.zookeeper_instance_type
    associate_public_ip_address = true
    key_name = var.key_name
    
    count = var.zookeeper_num_nodes
    
    tags = {
      Name = "newrelic-kafka-playground-zookeeper-${count.index}"
    }
}

resource "aws_instance" "brokers" {
    ami = var.amis[var.aws_region]
    instance_type = var.kafka_instance_type
    associate_public_ip_address = true
    key_name = var.key_name
    
    count = var.kafka_num_brokers
    
    tags = {
      Name = "newrelic-kafka-playground-broker-${count.index}"
      broker_id = "${count.index}"
    }
}