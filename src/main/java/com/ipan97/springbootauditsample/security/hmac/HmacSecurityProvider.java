package com.ipan97.springbootauditsample.security.hmac;

import com.ipan97.springbootauditsample.config.ApplicationProperties;
import com.ipan97.springbootauditsample.security.WrappedRequest;
import com.ipan97.springbootauditsample.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;

@Component
@Slf4j
public class HmacSecurityProvider {

    private final ApplicationProperties applicationProperties;

    public HmacSecurityProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public boolean verifyHmac(WrappedRequest request) throws HmacException, IOException {
        String jwt = JwtUtils.resolveToken(request);

        if (jwt == null || jwt.isEmpty()) {
            throw new HmacException("The Jwt is missing from the '" + HmacUtils.AUTHENTICATION + "'header");
        }

        String signatureClient = request.getHeader(HmacUtils.X_PK_SIGNATURE);
        if (signatureClient == null || signatureClient.isEmpty()) {
            throw new HmacException("The signature is missing from the '" + HmacUtils.X_PK_SIGNATURE + "' header");
        }

        //Get Timestamp header
        String timestampHeader = request.getHeader(HmacUtils.X_PK_TIMESTAMP);

        if (timestampHeader == null || timestampHeader.isEmpty()) {
            throw new HmacException("The timestamp is missing from the '" + HmacUtils.X_PK_TIMESTAMP + "' header");
        }

        String hmacSecret = applicationProperties.getSecurity().getAuthentication().getHmac().getBase64Secret();

        Assert.notNull(hmacSecret, "Secret key cannot be null");

        String message = request.getBody();

        //Digest are calculated using a public shared secret
        String signatureServer = HmacUtils.sign(request, hmacSecret, jwt, message, timestampHeader);
        if (log.isDebugEnabled()) {
            log.debug("HMAC JWT: {}", jwt);
            log.debug("HMAC Signature server: {}", signatureServer);
            log.debug("HMAC Signature client: {}", signatureClient);
        }
        if (signatureClient.equals(signatureServer)) {
            return true;
        } else {
            throw new HmacException("Signature are not matching!");
        }
    }
}
