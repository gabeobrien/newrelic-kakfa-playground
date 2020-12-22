Run with:
```
docker run -it -v $HOME/configs/:/mnt/deployer/configs/ -v $(pwd)/demo-deployer:/usr/local/app/   --entrypoint ruby ghcr.io/newrelic/deployer main.rb -c configs/emittelhammer.docker.local.json -d /usr/local/app/newrelic-kafka-playground.aws.json 
```