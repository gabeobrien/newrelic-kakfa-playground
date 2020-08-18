data "aws_vpc" "default" {
  default = true
}

data "aws_ami" "amazon_linux_2" {
  owners = ["amazon"]
  most_recent = true

 filter {
   name   = "owner-alias"
   values = ["amazon"]
 }


 filter {
   name   = "name"
   values = ["amzn2-ami-hvm*"]
 }
}

locals {
  vpc_id = var.vpc_id == "" ? data.aws_vpc.default.id : var.vpc_id
}

resource "aws_instance" "zookeeper" {
    ami = data.aws_ami.amazon_linux_2.id
    instance_type = var.zookeeper_instance_type
    associate_public_ip_address = true
    key_name = var.key_name
    vpc_security_group_ids = [aws_security_group.kafka_cluster_sg.id]
    
    count = var.zookeeper_num_nodes
    
    tags = {
      Name = "newrelic-kafka-playground-zookeeper-${count.index}"
    }
}

resource "aws_instance" "brokers" {
    ami = data.aws_ami.amazon_linux_2.id
    instance_type = var.kafka_instance_type
    associate_public_ip_address = true
    key_name = var.key_name
    vpc_security_group_ids = [aws_security_group.kafka_cluster_sg.id]
    
    count = var.kafka_num_brokers
    
    tags = {
      Name = "newrelic-kafka-playground-broker-${count.index}"
      broker_id = "${count.index}"
    }
}
