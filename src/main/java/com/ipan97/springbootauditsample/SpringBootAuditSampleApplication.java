package com.ipan97.springbootauditsample;

import com.ipan97.springbootauditsample.config.ApplicationProperties;
import com.ipan97.springbootauditsample.domain.Authority;
import com.ipan97.springbootauditsample.domain.User;
import com.ipan97.springbootauditsample.repository.jpa.AuthorityRepository;
import com.ipan97.springbootauditsample.repository.jpa.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@SpringBootApplication
@EnableConfigurationProperties({
        ApplicationProperties.class
})
@Slf4j
public class SpringBootAuditSampleApplication implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public SpringBootAuditSampleApplication(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAuditSampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!authorityRepository.findOneByName("ROLE_USER").isPresent()) {
            Authority authority = new Authority();
            authority.setName("ROLE_USER");
            authority = authorityRepository.save(authority);

            if (!userRepository.findOneWithAuthoritiesByEmail("test@test.com").isPresent()) {
                User user = new User();
                user.setUsername("test");
                user.setEmail("test@test.com");
                user.setPassword(passwordEncoder.encode("test@test.com"));
                user.addAuthority(authority);
                userRepository.save(user);
            }
        }

    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static String generateSecret() throws Exception {
        try {
            return Base64.encodeBase64String(generateToken().getBytes("UTF-8")).replace("\n", "").replace("\r", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new Exception("Cannot encode base64", e);
        }
    }
}
