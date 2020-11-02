# Builiding your cluster

## How it works

![How it Works - The Build](./images/kafka_playground_build.png)

The diagram above shows the flow of control in creating, provisioning, and configuring the infrastructure and applications in the cluster.

In summary:

- Everything is managed from a "control node" by applying Terraform configurations and executing Ansible playbooks.  It exists independently of the cluster itself.  This could be your local laptop, an EC2 instance in your account, or any host that can access both the AWS api and the resources that it creates.  More info in the "Bulding Your Cluster" document.
- A Terraform configuration is applied to your AWS account that will create the following resources:
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

 ## Choosing and Setting Up a Control Node

As mentioned previously, all of the configuration, provisioning, and deployment is done from a single host that must meet all of the following criteria (detailed instructions following):
- Have this repository cloned to it
- Terraform installed
- Ansible installed
- AWS credentials configured
- Have access to the private key of an [EC2 key pair](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html) that will be used to access the cluster via SSH.
- Be network accessible to the AWS API
- Be network accessible to port 22 of the nodes created in the cluster (by default, the security group created for the cluster opens port 22 to the world)

Generally, a good choice would be either your laptop or an EC2 instance that exists in the same VPC where you will be creating the cluster.  Due to some [Ansible constraints](http://blog.rolpdog.com/2020/03/why-no-ansible-controller-for-windows.html), this should be a host running *nix or MacOS.

Follow the instructions below to set up and configure your control node:

### Step 1: Clone this repository
Navigate to the [project homepage](https://github.com/newrelic-experimental/newrelic-kafka-playground) and use git to clone the repository.

### Step 2: Install the Terraform CLI
Follow the instructions in the Terrafrom tutorial to [install the Terraform CLI](https://learn.hashicorp.com/tutorials/terraform/install-cli).

### Step 3: Install Ansible
There are several ways to install Ansible, which are outlined in the [documentation](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html).
However, the most foolproof way is to [install via `pip` within a Python virtual environment](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html#virtual-environments) running Python version 3.6 or above.

If you are receiving errors about missing or mismatched Python packages, particulary related to OpenSSL and/or GnuPG when installing packages on the remote hosts, your Ansible installation and the underlying Python version are most likely the culprit.  Your best path forward from here would be to reinstall it in a virtual environment.

### Step 4: Configure/Verify your AWS credentials
The Terraform CLI uses the AWS API to create all of the infrastructure in the cluster.  Therefore, it requires the proper credentails to execute those API requests on your behalf.  View the [documentation for the Terraform AWS Provider](https://registry.terraform.io/providers/hashicorp/aws/latest/docs) to understand the various methods you can use to provide your credentials.

If you have previouly [configured the AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-quickstart.html), you should already have a credentials file in `~/.aws/credentials`.  Terraform will automatically read a profile from there.

### Step 5: Configure/Create/Verify your AWS Keypair
After the infrastructure has been created by Terraform, we'll use Ansible to install and configure all of the software on each instance.  This is done by connecting to each host via SSH which requires the private key portion of an [EC2 key pair](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html) to be present on the control node.  Because the infrastructure is being created via automation (as opposed to using the UI), the key pair must exist before you attempt to create the infrastructure.  Either choose an existing key pair, or create one specifically for this cluster.  Be sure you have downloaded the private key `.pem` file, and make note of the key pair name.