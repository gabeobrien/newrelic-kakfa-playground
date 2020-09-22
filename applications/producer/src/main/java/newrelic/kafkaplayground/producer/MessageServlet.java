package newrelic.kafkaplayground.producer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Token;

@WebServlet(name = "MessageServlet", urlPatterns = {"/message"}, loadOnStartup = 1) 
public class MessageServlet extends HttpServlet {
    
    final Logger logger = LoggerFactory.getLogger(MessageServlet.class);
    
    static String topicName;
    
    static {
        topicName = System.getenv("KAFKA_TOPIC_NAME");
        if (topicName == null) {
            throw new RuntimeException("Kakfa topic name was null");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            
        Token transactionToken = NewRelic.getAgent().getTransaction().getToken();
        
        @SuppressWarnings("unchecked")
        KafkaProducer<String, String> producer = (KafkaProducer) request.getServletContext().getAttribute("kafkaProducer");
    
        String userId = (String) request.getAttribute("userId");
        
        NewRelic.addCustomParameter("user.id", userId);
        
        String messageId = UUID.randomUUID().toString();
        
        String payload = String.format("{ \"userId\": %s, \"messageId\": %s}", userId, messageId);
        
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, userId, payload);
        
        
        producer.send(record,
            new Callback() {
                @Trace(async = true)
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    transactionToken.link();
                    if(e != null) {
                        logger.error("Got an error (asynchronously) when sending message {}", messageId, e);
                    } else {
                        // annotate the span with the metadta
                        if (metadata.hasOffset()) {
                            NewRelic.addCustomParameter("kafka.record.offset", metadata.offset());
                        }
                        NewRelic.addCustomParameter("kafka.record.partition", metadata.partition());
                        NewRelic.addCustomParameter("kafka.record.topic", metadata.topic());
                        
                        // only log if the trace is sampled to demonstrate logs-in-context
                        //if (NewRelic.getAgent().getTraceMetadata().isSampled()) {
                            logger.info("Sent message {}", payload);
                        //}
                    }
                }
            });
            
        
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(payload);
        out.flush();
    }
}