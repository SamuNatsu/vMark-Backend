package com.vmark.backend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Item {
    // Item ID
    @JsonProperty("iid")
    private int iid;

    // Item name
    @JsonProperty("name")
    private String name;

    // Item price
    @JsonProperty("price")
    private float price;

    // Item sale price
    @JsonProperty("sale")
    private Float sale;

    // Item description (HTML)
    @JsonProperty("desc")
    private String description;

    // Item remains count
    @JsonProperty("remain")
    private int remain;
}
