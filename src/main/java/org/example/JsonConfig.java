//package org.example;
//
//import cn.hutool.json.JSONNull;
//import com.fasterxml.jackson.core.JacksonException;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//
//import java.io.IOException;
//
//@Configuration
//public class JsonConfig {
//    @Bean
//    public ObjectMapper objectMapper() {
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(JSONNull.class, new JsonSerializer<JSONNull>(){
//            @Override
//            public void serialize(JSONNull jsonNull, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//                jsonGenerator.writeNull();
//            }
//        });
//        simpleModule.addDeserializer(JSONNull.class, new JsonDeserializer<JSONNull>() {
//            @Override
//            public JSONNull deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
//                return null;
//            }
//        });
//        return new ObjectMapper().registerModule(simpleModule);
//    }
//
//
//}
