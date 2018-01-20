package br.com.cedran.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtils {

    @SneakyThrows
    public String toJson(Object object) {
        return new ObjectMapper().writeValueAsString(object);
    }
}
