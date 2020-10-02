package com.ipan97.springbootsecurityhmacsample.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * @author Ipan Taufik Rahman
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Security security = new Security();

    @Getter
    @Setter
    public static class Security {

        private final Authentication authentication = new Authentication();

        public Authentication getAuthentication() {
            return authentication;
        }

        @Getter
        @Setter
        public static class Authentication {
            private final Jwt jwt = new Jwt();

            private final Hmac hmac = new Hmac();

            @Getter
            @Setter
            public static class Jwt {
                private String secret = null;

                private String base64Secret = null;

                private long tokenValidityInSeconds = 1800; // 0.5 hour

                private long tokenValidityInSecondsForRememberMe = 2592000; // 30 hours
            }

            @Getter
            @Setter
            public static class Hmac {
                private String secret;

                private String base64Secret;
            }
        }

        @Getter
        @Setter
        public static class RememberMe {
            @NotNull
            private String key = null;
        }
    }
}
