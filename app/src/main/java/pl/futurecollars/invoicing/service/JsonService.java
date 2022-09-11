package pl.futurecollars.invoicing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class JsonService {

    private ObjectMapper objectMapper;

    {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public String objectAsJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Service error: failed to convert string to JSON", e);
        }
    }

    public <T> T returnJsonAsObject(String json, Class<T> invoice) {
        try {
            return objectMapper.readValue(json, invoice);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Service error: failed to parse JSON", e);
        }

    }

}
