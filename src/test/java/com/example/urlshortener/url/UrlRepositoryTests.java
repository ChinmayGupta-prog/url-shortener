package com.example.urlshortener.url;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UrlRepositoryTests {

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void savesAndFindsUrlByShortCode() {
        Instant expiresAt = Instant.parse("2030-01-01T00:00:00Z");
        Url url = new Url("day2test", "https://example.com/long-path", expiresAt);

        urlRepository.saveAndFlush(url);

        Url found = urlRepository.findByShortCode("day2test").orElseThrow();
        assertThat(found.getId()).isNotNull();
        assertThat(found.getLongUrl()).isEqualTo("https://example.com/long-path");
        assertThat(found.getCreatedAt()).isNotNull();
        assertThat(found.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(found.getClickCount()).isZero();
    }
}
