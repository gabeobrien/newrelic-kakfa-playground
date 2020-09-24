package newrelic.kafkaplayground.consumer;

import java.util.Properties;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageConsumer {
  
    final static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    
    final static String APPLICATION_MESSAGES_TOPIC = "application-messages";
    
    final static String GROUP_ID = "application-messages-consumer-group";

    public static void main(String[] args) { 
      
      //TODO:  add additional thread pools for background/status messaging.
      ExecutorService executor = Executors.newFixedThreadPool(1);
      
      String containerId = System.getenv("HOSTNAME");
    
      Properties props = new Properties();
      ApplicationMessagesLoop aml = new ApplicationMessagesLoop(containerId, GROUP_ID, Arrays.asList(APPLICATION_MESSAGES_TOPIC), props);
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