package com.example.user.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Map;

@Configuration
public class KafkaConfig {
  @Bean
  public ProducerFactory<String,String> producerFactory(){
    return new DefaultKafkaProducerFactory<>(Map.of(
      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092",
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
    ));
  }
  @Bean
  public KafkaTemplate<String,String> kafkaTemplate(){ return new KafkaTemplate<>(producerFactory()); }
}
