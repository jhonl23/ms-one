package com.intercorp.msone.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intercorp.msone.dto.RootDTO;
import com.intercorp.msone.kafka.producer.EventDataUpdatedProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventBatchConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventDataUpdatedProducer eventDataUpdatedProducer;

    @KafkaListener(topics = "${spring.kafka.topic1}")
    public void listenForEventBatch(String message) throws Exception {

        try {

            /** STEP (3) **/
            RootDTO rootDTO = objectMapper.readValue(message, RootDTO.class);

            System.out.println("MENSAJE RECIBIDO EN MS-ONE: ");
            System.out.println(rootDTO.toString());

            /**
             * Agregar dato a la columna last update
             */
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            rootDTO.setLast_update("MICROSERVICES_PROCESS_01_"+dateTimeFormatter.format(LocalDateTime.now()));

            System.out.println("PREPARE MESSAGE SEND MS-ONE TO MS-BATCH: ");
            System.out.println(rootDTO);

            /** STEP (4)
             * Emitir mensaje a MS-BATCH con lista actualizada
             */
            this.sendMessageEventBatchConsumer(rootDTO);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageEventBatchConsumer(RootDTO rootDTO) {
        try {
            eventDataUpdatedProducer.sendMessage(rootDTO);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
