package com.vmark.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {
    @JsonProperty("uid")
    private int uid;

    @JsonProperty("account")
    private String account;

    @JsonProperty("name")
    private String name;

    @JsonIgnore
    private String password;

    @JsonProperty("privilege")
    private short privilege;
}
