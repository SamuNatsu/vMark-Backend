package com.vmark.backend.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMsg {
    // Object mapper
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Fail message
    public static String failed(String msg) {
        return String.format("{\"status\":\"failed\",\"message\":\"%s\"}", msg);
    }

    // Success message
    public static String success() {
        return "{\"status\":\"success\"}";
    }
    public static String success(Object obj) {
        // Message class
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        class sucMsg {
            @JsonProperty("status")
            public String status = "success";

            @JsonProperty("data")
            public Object data = obj;
        }

        // Return
        try {
            return objectMapper.writeValueAsString(new sucMsg());
        }
        catch (JsonProcessingException e) {
            return "{\"status\":\"success\"}";
        }
    }
}
