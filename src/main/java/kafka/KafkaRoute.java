package kafka;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.base.Joiner;

/**
 * Created by anatolie.lupacescu on 18/11/2015.
 */
public class KafkaRoute extends RouteBuilder implements Constants {

	@Value("${kafka.host}")
	private String kafkaHost;

	@Value("${kafka.port}")
	private String kafkaPort;

	@Value("${kafka.topic}")
	private String kafkaTopic;

	@Value("${kafka.group}")
	private String kafkaGroup;

	@Value("${kafka.consumer.count}")
	private String kafkaConsumerCount;

	@Value("${zk.host}")
	private String zkHost;

	@Value("${zk.port}")
	private String zkPort;

	@Override
	public void configure() throws Exception {
		from(uri()).to("log:input");
	}

	private String uri() {
		String options = Joiner.on("&").join(concat(KAFKA_TOPIC, kafkaTopic), 
				concat(ZOOKEEPER_HOST, zkHost),
				concat(ZOOKEEPER_PORT, zkPort), 
				concat(KAFKA_GROUP_ID, kafkaGroup),
				concat(KAFKA_CONSUMER_COUNT, kafkaConsumerCount));
		return String.format("%s:%s:%s?%s", KAFKA_SCHEMA, kafkaHost, kafkaPort, options);
	}

	private String concat(String head, String tail) {
		return head + "=" + tail;
	}
}
