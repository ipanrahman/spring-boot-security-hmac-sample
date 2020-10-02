package com.ipan97.springbootsecurityhmacsample.security.hmac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class HmacToken implements Serializable {
    @JsonProperty("jwt_id")
    private String jwtId;

    @JsonProperty("secret")
    private String secret;

    @JsonProperty("jwt")
    private String jwt;

}
