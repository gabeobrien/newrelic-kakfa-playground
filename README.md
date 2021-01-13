[![New Relic Experimental header](https://github.com/newrelic/opensource-website/raw/master/src/images/categories/Experimental.png)](https://opensource.newrelic.com/oss-category/#new-relic-experimental)


# Demo-Deployer Updates

Copy the kafka.json config file from this repo into your demo-deployer/configs directory.  If the kafka.json file was public (not in a branch) we could reference it directly in the 'ruby main.rb' command.

Docker command (update 'PATH_TO_DEPLOYER' to the path of your demo-deployer configs dir):

    docker run --entrypoint /bin/bash  -it -v PATH_TO_DEPLOYER/configs/:/mnt/deployer/configs/ ghcr.io/newrelic/deployer -c /bin/bash

In the container run (if we get this all working we can add the galaxy stuff to the demo-deployer docker image):

     ansible-galaxy install newrelic.newrelic-infra
     ruby main.rb -d configs/kafka.json





# New Relic Kafka Playground

This project contains a set of Terraform configurations and Ansible playbooks that will deploy a complete Kafka cluster (including Zookeeper), as well as a set of producer and consumer applications running in a Docker Swarm cluster.   It is fully configured and instrumented with New Relic.

You'll see how New Relic provides observability into Kafka clusters as well as producer consumer applications in a simulation of a real-world scenario.  It is completely configurable and extensible, allowing you to scale your cluster size and to modify/redeploy the applications to experiment with the features of the New Relic platform and how they relate to messages passed via Kafka streams.

All hosts and containers are instrumented with the [New Relic Infrastructure Agent](https://docs.newrelic.com/docs/infrastructure).  The applications are instrumented with [New Relic APM](https://docs.newrelic.com/docs/apm), and [Distributed Tracing](https://docs.newrelic.com/docs/understand-dependencies/distributed-tracing/get-started/introduction-distributed-tracing) has been enabled through the Kafka topics between producers and consumers.

***NOTE: This project creates several EC2 instances at minimun.  Most likely this will exceed the threshold of AWS's free tier.  Be sure to destroy the resources created by this project (using `terraform destroy`) when you are done using it***

## Prerequisites

- A [New Relic account](https://newrelic.com/signup)
- An AWS account, including:
    - a VPC in the AWS account where you will create the resources.  Your default VPC will work just fine.  If you want to use another VPC, you must create it manually and apply all of the necessary configuration (gateways, et al).
    - an [EC2 Keypair](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html) that will be used by the control node to manage the nodes in the clusters.  _This must be created before you begin building the infrastructure, and you must have the private key (typically a `.pem` file)_
    - An IAM user with adequate permissions, including:
        - creating security groups
        - creating EC2 instances
- A "control node" from where you will execute all of the build-related Terraform and Ansible scripts. See the "Choosing a Control Node" section of the Control Node documentation.

## Getting Started
1. Be sure to review:
    - The prerequisites above
    - How it Works: The Build
    - How it Works: The Application
2. Build and configure your Control Node
3. Create the infrastrucutre using Terraform
4. Install all of the software

## Usage
>[**Optional** - Include more thorough instructions on how to use the software. This section might not be needed if the Getting Started section is enough. Remove this section if it's not needed.]


## Building

>[**Optional** - Include this section if users will need to follow specific instructions to build the software from source. Be sure to include any third party build dependencies that need to be installed separately. Remove this section if it's not needed.]

## Testing

>[**Optional** - Include instructions on how to run tests if we include tests with the codebase. Remove this section if it's not needed.]

## Support

New Relic hosts and moderates an online forum where customers can interact with New Relic employees as well as other customers to get help and share best practices. Like all official New Relic open source projects, there's a related Community topic in the New Relic Explorers Hub. You can find this project's topic/threads here:

>Add the url for the support thread here

## Contributing
We encourage your contributions to improve [project name]! Keep in mind when you submit your pull request, you'll need to sign the CLA via the click-through using CLA-Assistant. You only have to sign the CLA one time per project.
If you have any questions, or to execute our corporate CLA, required if your contribution is on behalf of a company,  please drop us an email at opensource@newrelic.com.

**A note about vulnerabilities**

As noted in our [security policy](../../security/policy), New Relic is committed to the privacy and security of our customers and their data. We believe that providing coordinated disclosure by security researchers and engaging with the security community are important means to achieve our security goals.

If you believe you have found a security vulnerability in this project or any of New Relic's products or websites, we welcome and greatly appreciate you reporting it to New Relic through [HackerOne](https://hackerone.com/newrelic).

## License
[Project Name] is licensed under the [Apache 2.0](http://apache.org/licenses/LICENSE-2.0.txt) License.
>[If applicable: The [project name] also uses source code from third-party libraries. You can find full details on which libraries are used and the terms under which they are licensed in the third-party notices document.]
