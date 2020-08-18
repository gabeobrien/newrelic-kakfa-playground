variable "aws_region" {
    type = string
}

variable "vpc_id" {
    type = string
    default = ""
}

# variable "amis" {
#   type = map
#   default = {
#     "ap-northeast-1" = "ami-0f310fced6141e627"
#     "ap-northeast-2" = "ami-01288945bd24ed49a"
#     "ap-south-1" = "ami-0470e33cd681b2476"
#     "ap-southeast-1" = "ami-0ec225b5e01ccb706"
#     "ap-southeast-2" = "ami-0970010f37c4f9c8d"
#     "ca-central-1" = "ami-054362537f5132ce2"
#     "eu-central-1" = "ami-076431be05aaf8080"
#     "eu-north-1" = "ami-0b7a46b4bd694e8a6"
#     "eu-west-1" = "ami-06ce3edf0cff21f07"
#     "eu-west-2" = "ami-01a6e31ac994bbc09"
#     "eu-west-3" = "ami-00077e3fed5089981"
#     "sa-east-1" = "ami-003449ffb2605a74c"
#     "us-east-1" = "ami-0323c3dd2da7fb37d"
#     "us-east-2" = "ami-0f7919c33c90f5b58"
#     "us-west-1" = "ami-06fcc1f0bc2c8943f"
#     "us-west-2" = "ami-0d6621c01e8c2de2c"
#   }
# }

variable "zookeeper_instance_type" {
    type = string
}

variable "zookeeper_num_nodes" {
    type = number
}

variable "kafka_instance_type" {
    type = string
}

variable "kafka_num_brokers" {
    type = number
}

variable "key_name" {
    type = string
    description = "The name of the key pair to associate with the EC2 instances"
}