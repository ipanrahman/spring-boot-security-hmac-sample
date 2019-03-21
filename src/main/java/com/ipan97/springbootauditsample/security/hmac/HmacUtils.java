package com.ipan97.springbootauditsample.security.hmac;

import com.ipan97.springbootauditsample.security.WrappedRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public final class HmacUtils {
    public static final String HMAC_SHA_256 = "HmacSHA256";
    public static final String AUTHENTICATION = "Authentication";
    public static final String ENCODING_CLAIM_PROPERTY = "l-lev";

    public static final String X_PK_SIGNATURE = "X-PK-Signature";
    public static final String X_PK_KEY = "X-PK-KEY";
    public static final String X_PK_TIMESTAMP = "X-PK-Timestamp";

    public static String sign(WrappedRequest request, String secret, String accessToken, String body, String timestamp) throws HmacException, IOException {
        String message;
        if (!"GET".equals(request.getMethod())) {
            message = request.getMethod()
                    .concat(":")
                    .concat(resolveRequestPath(request))
                    .concat(":")
                    .concat(accessToken)
                    .concat(":")
                    .concat(Hex.encodeHexString(sha256(canocalized(body))))
                    .concat(":")
                    .concat(timestamp);

        } else {
            message = request.getMethod()
                    .concat(":")
                    .concat(resolveRequestPath(request))
                    .concat(":")
                    .concat(accessToken)
                    .concat(":")
                    .concat(timestamp);
        }
        log.info("HMAC Message Server {}", message);
        return hmacSha256(secret, message);
    }

    private static String resolveRequestPath(WrappedRequest request) throws IOException {
        String path = request.getServletPath();
        if (request.getQueryString() != null) {
            path += "?" + URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8.displayName());
        }
        return path;
    }

    public static byte[] sha256(String value) {
        return DigestUtils.sha256(value);
    }

    public static String hmacSha256(String secret, String message) throws HmacException {
        String digest;
        try {
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA_256);
            Mac mac = Mac.getInstance(HMAC_SHA_256);
            mac.init(key);
            byte[] bytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (Exception e) {
            log.debug("Error while encoding request with hmac");
            log.info("Error while encoding request with hmac {}", e);
            throw new HmacException("Error while encoding request with hmac");
        }
        return digest;
    }

    private static String canocalized(String json) {
        return json.replaceAll(" ", "")
                .replaceAll("\r", "")
                .replaceAll("\n", "")
                .replaceAll("\t", "");
    }
}
