package bigu.eazybankbackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.password.argon2")
public record Argon2Properties(
        int memory,
        int iterations,
        int parallelism,
        int hashLength
) {
}