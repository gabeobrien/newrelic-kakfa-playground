package newrelic.kafkaplayground.producer;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.kafka.clients.producer.KafkaProducer;

@WebServlet(name = "DefaultServlet", urlPatterns = {""}, loadOnStartup = 1) 
public class DefaultServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.getWriter().print("Hello, World!");  
    }
}