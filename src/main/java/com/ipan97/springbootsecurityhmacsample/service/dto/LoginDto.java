package com.ipan97.springbootsecurityhmacsample.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.io.Serializable;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDto implements Serializable {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
