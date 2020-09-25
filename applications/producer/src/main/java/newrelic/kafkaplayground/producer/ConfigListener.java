package newrelic.kafkaplayground.producer;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.util.Properties;
import java.util.Arrays;
import java.io.StringReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import newrelic.kafkaplayground.common.util.PropertiesLoader;

import org.apache.kafka.clients.producer.KafkaProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

@WebListener
public class ConfigListener implements ServletContextListener {
    
    final Logger logger = LoggerFactory.getLogger(ConfigListener.class);
    
    public void contextInitialized(ServletContextEvent event) {
    
        Properties producerProps = PropertiesLoader.loadProperties(Arrays.asList("/var/webapp/config/producer.default.properties", "/var/webapp/config/producer.override.properties"));
        String envProps = System.getenv("PRODUCER_PROPERTIES");
        if(envProps != null) {
            try {
              producerProps.load(new StringReader(envProps));
            } catch (Exception e) {
              logger.error("Unable to load environment properties", e);
            }
          }
        
        String clientId = System.getenv("CLIENT_ID") != null ? System.getenv("CLIENT_ID") : "producer-" +System.getenv("HOSTNAME");
        producerProps.setProperty("client.id", clientId);
        
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);
        
        event.getServletContext().setAttribute("producerProps", producerProps);
        event.getServletContext().setAttribute("kafkaProducer", producer);
        
        logger.info("added kafkaProducer to ServletContext");
        
        // print logback internal state
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }
    
    public void contextDestroyed(ServletContextEvent event) {
    }
}