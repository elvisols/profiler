package com.lvs.profiler.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Configuration
public class KafkaConfig {

    @Bean
    public StreamsConfig getStreamsConfig() {
        return new StreamsConfig(getProperties());
    }

    @Bean
    public StreamsBuilder getStreamsBuilder() {
        return new StreamsBuilder();
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "profiler_application");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "profiler_group");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "profiler_driver_client");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "1");
        props.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "10000");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        return props;
    }


}
