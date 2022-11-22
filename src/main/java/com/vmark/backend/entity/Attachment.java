package com.vmark.backend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Attachment {
    // Attachment ID
    @JsonProperty("aid")
    private int aid;

    // Attachment name
    @JsonProperty("name")
    private String name;

    // Attachment path
    @JsonProperty("path")
    private String path;

    // Attachment timestamp
    @JsonProperty("timestamp")
    private long timestamp;
}
