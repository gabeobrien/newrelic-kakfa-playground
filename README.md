Notes:

ansible:
on amazon linux, install from pip:  sudo pip install ansible

install sansible kafka: ansible-galaxy install sansible.kafka
install sansible zookeeper: ansible-galaxy install sansible.zookeeper

ansible need to use --private-key flag
cd ansible_playbooks
ansible-playbook -i ../terraform/kafka-cluster/output/ansible_hosts.yml --private-key=your_key ./setup-kafka-and-zookeeper.yml 