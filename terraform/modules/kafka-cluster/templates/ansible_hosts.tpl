all:
    zookeepernodes:
        %{~ for node in zookeeper_nodes ~}
        ${node.tags["Name"]}:
            ansible_host: ${node.public_ip}
            public_ip: ${node.public_ip}
            private_ip: ${node.private_ip}
        %{~ endfor ~}
    kafkabrokers:
        %{~ for node in kafka_brokers ~}
        ${node.tags["Name"]}:
            ansible_host: ${node.public_ip}
            public_ip: ${node.public_ip}
            private_ip: ${node.private_ip}
        %{~ endfor ~}