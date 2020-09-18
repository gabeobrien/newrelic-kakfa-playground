resource "aws_security_group" "kafka_playground_sg" {
    name = "newrelic_kafka_playground_sg"
    description = "Security group for the New Relic Kafka Playground.  Assuming that all ZooKeeper nodes, Kafka Brokers, and application hosts are in the same SG."

    ingress {
        from_port = 22
        to_port = 22
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
        description = "SSH"
    }

    ingress {
        from_port = 2181
        to_port = 2181
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
        description = "ZooKeeper" 
    }
    ingress {
        from_port = 2888
        to_port = 2888
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
        description = "ZooKeeper" 
    }
    ingress {
        from_port = 3888
        to_port = 3888
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
        description = "ZooKeeper" 
    }
    ingress {
        from_port = 9092
        to_port = 9092
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
        description = "Kafka Brokers"
    }
    ingress {
        from_port = 0
        to_port = 65535
        protocol = "tcp"
        self = true
        description = "Internal security group. Allow everything."
    }
    ingress {
        from_port = -1
        to_port = -1
        protocol = "icmp"
        cidr_blocks = ["0.0.0.0/0"] 
    }
    egress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    vpc_id = local.vpc_id

    tags = {
        Name = "newrelic_kafka_playground_sg"
        Project = var.project_name
    }
}