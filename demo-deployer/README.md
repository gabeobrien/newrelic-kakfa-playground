Run with:
```
docker run -it -v $HOME/configs/:/mnt/deployer/configs/ -v $(pwd):/usr/local/app/ -v $(pwd)/demo-deployer/app_config.yml.local:/mnt/deployer/src/app_config.yml.local ghcr.io/newrelic/deployer /bin/bash -c "ansible-galaxy install newrelic.newrelic-infra; ruby main.rb -c configs/emittelhammer.docker.local.json -d /usr/local/app/demo-deployer/newrelic-kafka-playground.aws.json"
```