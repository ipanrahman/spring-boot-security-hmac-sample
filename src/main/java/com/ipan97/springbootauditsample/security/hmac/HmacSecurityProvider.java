package com.ipan97.springbootauditsample.security.hmac;

import com.ipan97.springbootauditsample.config.ApplicationProperties;
import com.ipan97.springbootauditsample.security.WrappedRequest;
import com.ipan97.springbootauditsample.security.jwt.JwtTokenProvider;
import com.ipan97.springbootauditsample.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
@Slf4j
public class HmacSecurityProvider {

    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationProperties applicationProperties;

    public HmacSecurityProvider(JwtTokenProvider jwtTokenProvider, ApplicationProperties applicationProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.applicationProperties = applicationProperties;
    }

    public boolean verifyHmac(WrappedRequest request) throws HmacException, IOException {
        String jwt = JwtUtils.resolveToken(request);

        if (jwt == null || jwt.isEmpty()) {
            throw new HmacException("The Jwt is missing from the '" + HmacUtils.AUTHENTICATION + "'header");
        }

        String xPKSignatureClient = request.getHeader(HmacUtils.X_PK_SIGNATURE);
        if (xPKSignatureClient == null || xPKSignatureClient.isEmpty()) {
            throw new HmacException("The signature is missing from the '" + HmacUtils.X_PK_SIGNATURE + "' header");
        }

        //Get X-PK-Timestamp header
        String xPKTimestampHeader = request.getHeader(HmacUtils.X_PK_TIMESTAMP);

        if (xPKTimestampHeader == null || xPKTimestampHeader.isEmpty()) {
            throw new HmacException("The timestamp is missing from the '" + HmacUtils.X_PK_TIMESTAMP + "' header");
        }

        String path = request.getServletPath();
        if (request.getQueryString() != null) {
            path += "?" + URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8.displayName());
        }

        String hmacSecret = applicationProperties.getSecurity().getAuthentication().getHmac().getBase64Secret();

        Assert.notNull(hmacSecret, "Secret key cannot be null");

        String message = request.getBody();
        
        //Digest are calculated using a public shared secret
        String digestServer = HmacUtils.sign(request, hmacSecret, jwt, message, xPKTimestampHeader);
        log.info("HMAC JWT: {}", jwt);
        log.info("HMAC url digest: {}", path);
        log.info("HMAC Secret server: {}", hmacSecret);
        log.info("HMAC Signature server: {}", digestServer);
        log.info("HMAC Signature client: {}", xPKSignatureClient);

        if (xPKSignatureClient.equals(digestServer)) {
            log.info("Request is valid, digest are matching");
            return true;
        } else {
            log.info("Server message: " + message);
            throw new HmacException("Digest are not matching! Client: " + xPKSignatureClient + " / Server: " + digestServer);
        }
    }
}
