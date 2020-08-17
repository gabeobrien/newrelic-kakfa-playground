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
    value = aws_instance.zookeeper
}

output "kafka_brokers" {
    value = aws_instance.brokers
}