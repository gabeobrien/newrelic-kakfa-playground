package newrelic.kafkaplayground.producer;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.kafka.clients.producer.KafkaProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

@WebListener
public class ConfigListener implements ServletContextListener {
    
    final Logger logger = LoggerFactory.getLogger(ConfigListener.class);
    
    final String defaultPropsPath = "/var/webapp/config/producer.default.properties";
    final String overridePropsPath = "/var/webapp/config/producer.override.properties";
    
    public void contextInitialized(ServletContextEvent event) {
            Properties producerProperties = new Properties();
        try{
            FileInputStream defaultProps = new FileInputStream(defaultPropsPath); 
            producerProperties.load(defaultProps);
            defaultProps.close();
            logger.info("loaded default props");
        } catch (FileNotFoundException notFound) {
            logger.error(String.format("Unable to find %s, using default configuration", defaultPropsPath));
        } catch (IOException ioe) {
            logger.warn(ioe.getMessage());
        }
        try {
            FileInputStream overrideProps = new FileInputStream(overridePropsPath);
            producerProperties.load(overrideProps);
            overrideProps.close();
            logger.info("loaded override props");
        } catch (FileNotFoundException notFound) {
            logger.warn(String.format("Unable to find %s, using default configuration", overridePropsPath));
        } catch (IOException ioe) {
            logger.warn(ioe.getMessage());
        }
        
        logger.info(String.format("Using producer configuration:\n%s", producerProperties.toString()));
        
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties);
        
        event.getServletContext().setAttribute("kafkaProducer", producer);
        
        logger.info("added kafkaProducer to ServletContext");
        
        // print logback internal state
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }
    public void contextDestroyed(ServletContextEvent event) {
    }
}