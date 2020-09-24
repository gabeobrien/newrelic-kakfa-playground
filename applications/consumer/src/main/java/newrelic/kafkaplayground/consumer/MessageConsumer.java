package newrelic.kafkaplayground.consumer;

import java.io.StringReader;
import java.util.Properties;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageConsumer {
  
    final static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    public static void main(String[] args) { 
      
      //TODO:  add additional thread pools for background/status messaging.
      ExecutorService executor = Executors.newFixedThreadPool(1);
      
      String consumerId = System.getenv("CONSUMER_ID") != null ? System.getenv("CONSUMER_ID") : System.getenv("HOSTNAME");
      
      String applicationMessagesTopic = System.getenv("APPLICATION_MESSAGES_TOPIC_NAME");
      
      Properties props = PropertiesLoader.loadProperties(Arrays.asList("/usr/local/app/config/application.consumer.default.properties", "/usr/local/app/config/application.consumer.override.properties"));
      String envProps = System.getenv("APPLICATION_CONSUMER_PROPS");
      if(envProps != null) {
        try {
          props.load(new StringReader(envProps));
        } catch (Exception e) {
          logger.error("Unable to load environment properties", e);
        }
      }
      
      ApplicationMessagesLoop aml = new ApplicationMessagesLoop(consumerId, Arrays.asList(applicationMessagesTopic), props);
      executor.submit(aml);
    
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          aml.shutdown();
          executor.shutdown();
          try {
            executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
          } catch (InterruptedException e) {
            logger.error("Caught exception when shutting down", e);
          }
        }
      });
    }
}