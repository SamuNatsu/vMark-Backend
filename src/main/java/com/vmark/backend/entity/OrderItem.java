package com.vmark.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderItem {
    @JsonIgnore
    private int oid;

    @JsonProperty("iid")
    private int iid;

    @JsonProperty("price")
    private int price;

    @JsonProperty("count")
    private int count;
}
