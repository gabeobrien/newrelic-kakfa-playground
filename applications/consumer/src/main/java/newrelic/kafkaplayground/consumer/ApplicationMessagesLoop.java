package newrelic.kafkaplayground.consumer;

import java.util.Properties;
import java.util.List;
import java.time.Duration;
import java.nio.charset.StandardCharsets;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.header.Header;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

public class ApplicationMessagesLoop implements Runnable {
  
  private final KafkaConsumer<String, String> consumer;
  private final List<String> topics;
  private final String id;
  
  @Trace(dispatcher = true)
  private static void processMessage(ConsumerRecord<String, String> rec){
    Iterable<Header> headers = rec.headers().headers("newrelic");
    for(Header header: headers) {
      NewRelic.getAgent().getTransaction().acceptDistributedTracePayload(new String(header.value(), StandardCharsets.UTF_8));
    }
  }

  public ApplicationMessagesLoop(String id, String groupId, List<String> topics, Properties consumerProperites) {
    this.id = id;
    this.topics = topics;
    this.consumer = new KafkaConsumer<>(consumerProperites);
  }
 
  @Override
  public void run() {
    try {
      consumer.subscribe(topics);

      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
        for (ConsumerRecord<String, String> record : records) {

        }
      }
    } catch (WakeupException e) {
      // ignore for shutdown 
    } finally {
      consumer.close();
    }
  }

  public void shutdown() {
    consumer.wakeup();
  }
}