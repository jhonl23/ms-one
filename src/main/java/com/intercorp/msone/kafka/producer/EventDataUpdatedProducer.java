package com.intercorp.msone.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intercorp.msone.dto.RootDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.UUID;

@Component
public class EventDataUpdatedProducer {

    @Value("${spring.kafka.topic3}")
    String topicName;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public ListenableFuture<SendResult<String, String>> sendMessage(RootDTO rootDTO) throws JsonProcessingException {

        String key = UUID.randomUUID().toString();
        String value = objectMapper.writeValueAsString(rootDTO);

        ProducerRecord<String, String> producerRecord = buildProducerRecord(key, value, topicName);
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(producerRecord);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {

                try {
                    handleSuccess(key, value, result);
                    System.out.println("SUCCESS SEND MESSAGE MS-ONE TO MS-BATCH...");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

        });

        return listenableFuture;
    }

    private ProducerRecord<String, String> buildProducerRecord(String key, String value, String topic) {
        List<Header> recordHeaders = List.of(new RecordHeader("deposit-subscription-source", "scanner".getBytes()));

        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

    private void handleFailure(String key, String value, Throwable ex) {
        //log.error("Error: send message and the error is " + ex.getMessage());

        try {
        } catch (Throwable throwable) {
         //   log.error("Error on OnFailure "+ throwable.getMessage());
        }

    }

    private void handleSuccess(String key, String value, SendResult<String, String> result) throws JsonMappingException, JsonProcessingException {
     //   log.info("Message Sent Successfully for the key " + key + " and the value is "+ value + "partition is " + result.getRecordMetadata().partition());
    }

}
