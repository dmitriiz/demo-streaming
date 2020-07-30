package com.use2go.demo;

import com.use2go.demo.model.AppConstants;
import com.use2go.demo.model.DataMessage;
import com.use2go.demo.service.DataConsumer;
import com.use2go.demo.service.DataProducer;
import com.use2go.demo.service.impl.DataConsumerImpl;
import com.use2go.demo.service.impl.DataProducerImpl;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.Topic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
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
		public KStream<String, DataMessage> processingStream(StreamsBuilder builder) {
//			Serde<String> keySerde = Serdes.String();
//			Serde<DataMessage> valueSerde = Serdes.serdeFrom();
			KStream<String, DataMessage> stream = builder.stream(AppConstants.TOPIC_IN/*, Consumed.with(keySerde, valueSerde)*/);
//			stream.print(Printed.<String, DataMessage>toSysOut().withLabel("Stream"));
			stream.to(AppConstants.TOPIC_OUT);
			return stream;
		}

	}

}
