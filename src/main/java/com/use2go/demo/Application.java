package com.use2go.demo;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.use2go.demo.model.AppConstants;
import com.use2go.demo.model.DataMessage;
import com.use2go.demo.service.DataConsumer;
import com.use2go.demo.service.DataProducer;
import com.use2go.demo.service.EsperService;
import com.use2go.demo.service.impl.DataConsumerImpl;
import com.use2go.demo.service.impl.DataProducerImpl;
import com.use2go.demo.service.impl.EsperServiceImpl;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
	@EnableKafkaStreams
	protected class KafkaConfig {

		@Bean
		public NewTopic topicDemoIn() {
			return new NewTopic(AppConstants.TOPIC_IN, 1, (short) 1);
		}

		@Bean
		public NewTopic topicDemoOut() {
			return new NewTopic(AppConstants.TOPIC_OUT, 1, (short) 1);
		}

		@Bean
		public DataConsumer dataConsumer() {
			return new DataConsumerImpl();
		}

		@Bean
		public DataProducer dataProducer(KafkaTemplate<String, DataMessage> kafkaTemplate) {
			return new DataProducerImpl(kafkaTemplate);
		}

		@Bean
		public KStream<String, DataMessage> processingStream(StreamsBuilder builder, EsperService esperService) {
			EPRuntime runtime = esperService.createRuntime(new ClassPathResource("demo.txt"), DataMessage.class,
					(newEvents, oldEvents, stmt, rntm) -> {
						for (EventBean eventBean : newEvents) {
							Object o = eventBean.getUnderlying();
							System.out.println("EVENT: " + o);
						}
					});
			var keySerde = Serdes.String();
			var valueSerde = new JsonSerde<>(DataMessage.class);
			KStream<String, DataMessage> stream = builder.stream(AppConstants.TOPIC_IN, Consumed.with(keySerde, valueSerde));
//			stream.print(Printed.<String, DataMessage>toSysOut().withLabel("Stream"));
			stream.mapValues(value -> {
				esperService.processMessage(runtime, value);
				return value;
			}).to(AppConstants.TOPIC_OUT, Produced.with(keySerde, valueSerde));
			return stream;
		}

		@Bean
		public EsperService esperService() {
			return new EsperServiceImpl();
		}

	}

}
