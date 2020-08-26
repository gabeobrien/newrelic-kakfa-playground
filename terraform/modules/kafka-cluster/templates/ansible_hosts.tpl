all:
    vars:
        project_name: ${project_name}
        new_relic_license_key: ${new_relic_license_key}
    children:
        zookeepernodes:
            hosts:
                %{~ for node in zookeeper_nodes ~}
                ${node.tags["Name"]}:
                    ansible_host: ${node.public_ip}
                    public_ip: ${node.public_ip}
                    private_ip: ${node.private_ip}
                    my_id: ${node.tags["my_id"]}
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