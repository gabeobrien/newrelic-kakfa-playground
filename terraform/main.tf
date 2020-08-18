terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }
}

provider "aws" {
  profile = var.aws_profile
  region  = var.aws_region
}

module "kafka_cluster" {
    source = "./modules/kafka-cluster"
    aws_region = var.aws_region
    vpc_id = var.vpc_id
    zookeeper_instance_type = var.zookeeper_instance_type
    zookeeper_num_nodes = var.zookeeper_num_nodes
    kafka_instance_type = var.kafka_instance_type
    kafka_num_brokers = var.kafka_num_brokers
    key_name = var.ec2_keypair_name
}