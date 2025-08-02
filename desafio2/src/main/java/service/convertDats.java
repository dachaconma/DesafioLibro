package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class convertDats implements IConvierteDatos {

    // Trae clase de dependencia jackson databind. Definir en archivo POM
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase){
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
