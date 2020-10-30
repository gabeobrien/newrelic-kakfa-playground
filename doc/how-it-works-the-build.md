# How it Works - The Build

![How it Works - The Build](./doc/kafka_playground_build.png)

The diagram above shows the flow of control in creating, provisioning, and configuring the infrastructure and applications in the cluster.

In summary:

- Everything is managed from a "control node" by applying Terraform configurations and executing Ansible playbooks.  It exists independently of the cluster itself.  This could be your local laptop, an EC2 instance in your account, or any node that can access both the AWS api and the resources that it creates.  More info in the "Bulding Your Cluster" document.
- A Terraform configuration is applied to your AWS account that will create:
    - A security group that by default has port 22 exposed to the world
    - A group of EC2 instances that will run Zookeeper
    - A group of EC2 instances that will run Kafka
    - A group of EC2 instances that will run Docker Swarm
- Following, Ansible playbooks are run to:
 - Install the New Relic Infrastructure Agent
 - Install Zookeeper
 - Install Kafka
 - Install Docker
 - Create a Docker Swarm
 - Build and deploy the producer and consumer applications to the Swarm
 - Deploy a loadgen container to the Swarm