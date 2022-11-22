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

    // Item category ID
    @JsonProperty("cid")
    private int cid;

    // Item price
    @JsonProperty("price")
    private int price;

    // Item remains
    @JsonProperty("remain")
    private int remain;

    // Item showcase attachment ID
    @JsonProperty("aid")
    private Integer aid;

    // Item sale price
    @JsonProperty("sale")
    private Integer sale;

    // Item description (HTML)
    @JsonProperty("desc")
    private String description;
}
