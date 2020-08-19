all:
    vars:
        ansible_user: ubuntu
    children:
        zookeepernodes:
            hosts:
                %{~ for node in zookeeper_nodes ~}
                ${node.tags["Name"]}:
                    ansible_host: ${node.public_ip}
                    public_ip: ${node.public_ip}
                    private_ip: ${node.private_ip}
                %{~ endfor ~}
        kafkabrokers:
            hosts:
                %{~ for node in kafka_brokers ~}
                ${node.tags["Name"]}:
                    ansible_host: ${node.public_ip}
                    public_ip: ${node.public_ip}
                    private_ip: ${node.private_ip}
                    broker_id: ${node.tags["broker_id"]}
                %{~ endfor ~}