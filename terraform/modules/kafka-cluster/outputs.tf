# resource "local_file" "kafka_cluster_ansible_hosts" {
#     filename = var.ansible_inventory_path
#     content = <<-EOF
#     [zookeeper]
#     %{ for host in aws_instance.zookeeper.* ~}
#     zookeeper_${host.id} ansible_host=${host.private_ip}
#     %{ endfor ~} 
#     EOF
# }

output "zookeeper_nodes" {
    value = aws_instance.zookeeper_nodes
}

output "kafka_brokers" {
    value = aws_instance.kafka_brokers
}


resource "local_file" "ansible_hosts" {
    filename = "./output/ansible_hosts.yml"
    content = templatefile(
        "${path.module}/templates/ansible_hosts.tpl",
        {
            new_relic_license_key = var.new_relic_license_key
            zookeeper_nodes = aws_instance.zookeeper_nodes
            kafka_brokers = aws_instance.kafka_brokers
            project_name = var.project_name
        }
    )
}