variable "aws_region" {
    type = string
}

variable "aws_profile" {
    type = string
    default = "default"
}

variable "zookeeper_instance_type" {
    type = string
    default = "t3.small"
}

variable "zookeeper_num_nodes" {
    type = number
    default = 1
}

variable "kafka_instance_type" {
    type = string
    default = "t3.small"
}

variable "kafka_num_brokers" {
    type = number
    default = 2
}

variable "ec2_keypair_name" {
    type = string
    description = "The name of the keypair that will be associated with the EC2 instances"
}