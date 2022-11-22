package com.vmark.backend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Category {
    // Category ID
    @JsonProperty("cid")
    private int cid;

    // Category name
    @JsonProperty("name")
    private String name;

    // Category parent
    @JsonProperty("parent")
    private Integer parent;
}
