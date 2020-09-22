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

@WebServlet(name = "MessageServlet", urlPatterns = {"/message"}, loadOnStartup = 1) 
public class MessageServlet extends HttpServlet {
    
    static String topicName;
    
    static {
        topicName = System.getenv("KAFKA_TOPIC_NAME");
        if (topicName == null) {
            throw new RuntimeException("Kakfa topic name was null");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        KafkaProducer producer = (KafkaProducer) request.getServletContext().getAttribute("kafkaProducer");
    
        String userId = (String) request.getAttribute("userId");
        String messageId = UUID.randomUUID().toString();
        
        String payload = String.format("{ \"userId\": %s, \"messageId\": %s}", userId, messageId);
        
        ProducerRecord record = new ProducerRecord<String, String>(topicName, userId, payload);
        
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(payload);
        out.flush();
    }
}