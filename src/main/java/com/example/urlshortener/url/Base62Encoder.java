package com.example.urlshortener.url;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    public String encode(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID must be nonnegative");
        }

        if (id == 0) {
            return "0";
        }

        StringBuilder encoded = new StringBuilder();
        long remaining = id;

        while (remaining > 0) {
            int alphabetIndex = (int) (remaining % BASE);
            encoded.append(ALPHABET.charAt(alphabetIndex));
            remaining /= BASE;
        }

        return encoded.reverse().toString();
    }

    public long decode(String shortCode) {
        if (shortCode == null || shortCode.isEmpty()) {
            throw new IllegalArgumentException("Short code must not be null or empty");
        }

        long decoded = 0;

        for (int index = 0; index < shortCode.length(); index++) {
            char character = shortCode.charAt(index);
            int alphabetIndex = ALPHABET.indexOf(character);

            if (alphabetIndex < 0) {
                throw new IllegalArgumentException("Short code contains an invalid character: " + character);
            }

            try {
                decoded = Math.addExact(Math.multiplyExact(decoded, BASE), alphabetIndex);
            } catch (ArithmeticException exception) {
                throw new IllegalArgumentException("Short code exceeds the supported ID range", exception);
            }
        }

        return decoded;
    }
}
