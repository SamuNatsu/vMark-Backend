package com.vmark.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {
    // User ID
    @JsonProperty("uid")
    private int uid;

    // User account
    @JsonProperty("account")
    private String account;

    // User name
    @JsonProperty("name")
    private String name;

    // User password
    @JsonIgnore
    private String password;

    // User privilege
    @JsonProperty("privilege")
    private short privilege;
}
