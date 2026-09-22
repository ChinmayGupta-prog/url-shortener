package com.example.urlshortener.url;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "urls")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urls_id_generator")
    @SequenceGenerator(name = "urls_id_generator", sequenceName = "urls_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "short_code", nullable = false, length = 16)
    private String shortCode;

    @Column(name = "long_url", nullable = false, columnDefinition = "TEXT")
    private String longUrl;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "click_count", nullable = false)
    private long clickCount;

    public Url(String shortCode, String longUrl, Instant expiresAt) {
        this.shortCode = shortCode;
        this.longUrl = longUrl;
        this.expiresAt = expiresAt;
    }

    @PrePersist
    void setCreationTime() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
