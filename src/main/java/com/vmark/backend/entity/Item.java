package com.vmark.backend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Item {
    @JsonProperty("iid")
    private int iid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private float price;

    @JsonProperty("sale")
    private Float sale;

    @JsonProperty("desc")
    private String description;

    @JsonProperty("remain")
    private int remain;
}
