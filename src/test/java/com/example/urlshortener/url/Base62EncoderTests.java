package com.example.urlshortener.url;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Base62EncoderTests {

    private final Base62Encoder encoder = new Base62Encoder();

    @Test
    void encodesBoundaryValuesUsingTheConfiguredAlphabet() {
        assertThat(encoder.encode(0)).isEqualTo("0");
        assertThat(encoder.encode(9)).isEqualTo("9");
        assertThat(encoder.encode(10)).isEqualTo("a");
        assertThat(encoder.encode(35)).isEqualTo("z");
        assertThat(encoder.encode(36)).isEqualTo("A");
        assertThat(encoder.encode(61)).isEqualTo("Z");
        assertThat(encoder.encode(62)).isEqualTo("10");
        assertThat(encoder.encode(3_843)).isEqualTo("ZZ");
        assertThat(encoder.encode(3_844)).isEqualTo("100");
    }

    @ParameterizedTest
    @ValueSource(longs = { 0, 1, 61, 62, 3_843, 3_844, 1_000_000, Long.MAX_VALUE })
    void roundTripsIds(long id) {
        assertThat(encoder.decode(encoder.encode(id))).isEqualTo(id);
    }

    @Test
    void decodesBoundaryValuesUsingTheConfiguredAlphabet() {
        assertThat(encoder.decode("0")).isZero();
        assertThat(encoder.decode("Z")).isEqualTo(61);
        assertThat(encoder.decode("10")).isEqualTo(62);
    }

    @Test
    void rejectsNegativeIds() {
        assertThatThrownBy(() -> encoder.encode(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID must be nonnegative");
    }

    @Test
    void rejectsMissingOrInvalidShortCodes() {
        assertThatThrownBy(() -> encoder.decode(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> encoder.decode(""))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> encoder.decode("abc-123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("invalid character");
    }

    @Test
    void rejectsValuesLargerThanLongMaxValue() {
        String overflowingCode = "1" + encoder.encode(Long.MAX_VALUE);

        assertThatThrownBy(() -> encoder.decode(overflowingCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Short code exceeds the supported ID range");
    }
}
