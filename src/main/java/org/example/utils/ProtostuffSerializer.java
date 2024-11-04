package org.example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

public class ProtostuffSerializer {
    public <T> byte[] serialize(final T source) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(source);
        } catch (final Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public <T> T deserialize(final byte[] bytes) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (bytes != null && bytes.length > 0) {
                return mapper.readValue(bytes, new TypeReference<T>() {});
            }
        } catch (final Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        return null;
    }

    public <T> T deserialize(String jsonStr, Class<T> targetClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            if (StringUtils.hasText(jsonStr)) {
                return mapper.readValue(jsonStr, targetClass);
            }
        } catch (final Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        return null;
    }

}
