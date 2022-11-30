package com.vmark.backend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Order {
    @JsonProperty("oid")
    private int oid;

    @JsonProperty("uid")
    private int uid;

    @JsonProperty("timestamp")
    private long timestamp;
}
