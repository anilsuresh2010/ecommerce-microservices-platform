package com.ecommerce.product_service.kafka;


import com.ecommerce.product_service.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

        private final KafkaTemplate<String, String> kafkaTemplate;
        private final ObjectMapper objectMapper;

        public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
            this.objectMapper = new ObjectMapper();
        }

        public void sendProductCreatedEvent(Product product) throws JsonProcessingException {
            ProductEvent event = new ProductEvent(product.getId(), product.getName(), product.getDescription(), product.getPrice());
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("product-events", message);
        }
    }

