data "aws_vpc" "default" {
  default = true
}

data "aws_ami" "ubuntu" {
    most_recent = true

    filter {
        name   = "name"
        values = ["ubuntu/images/hvm-ssd/ubuntu-bionic-18.04-amd64-server-*"]
    }

    filter {
        name   = "virtualization-type"
        values = ["hvm"]
    }

    owners = ["099720109477"] # Canonical
}

locals {
  vpc_id = var.vpc_id == "" ? data.aws_vpc.default.id : var.vpc_id
}

resource "aws_instance" "zookeeper_nodes" {
    ami = data.aws_ami.ubuntu.id
    instance_type = var.zookeeper_instance_type
    associate_public_ip_address = true
    key_name = var.key_name
    vpc_security_group_ids = [aws_security_group.kafka_cluster_sg.id]
    
    count = var.zookeeper_num_nodes
    
    tags = {
      Name = "newrelic-kafka-playground-zookeeper-${count.index}"
      Role = "zookeeper"
    }
}

resource "aws_instance" "kafka_brokers" {
    ami = data.aws_ami.ubuntu.id
    instance_type = var.kafka_instance_type
    associate_public_ip_address = true
    key_name = var.key_name
    vpc_security_group_ids = [aws_security_group.kafka_cluster_sg.id]
    
    count = var.kafka_num_brokers
    
    tags = {
      Name = "newrelic-kafka-playground-broker-${count.index}"
      Role = "kafka broker"
      broker_id = "${count.index}"
    }
}
