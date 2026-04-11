package it.sara.demo.service.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilTest {

    private final StringUtil stringUtil = new StringUtil();

    @Test
    void isNullOrEmpty_withNull_shouldReturnTrue() {
        assertTrue(stringUtil.isNullOrEmpty(null));
    }

    @Test
    void isNullOrEmpty_withEmptyString_shouldReturnTrue() {
        assertTrue(stringUtil.isNullOrEmpty(""));
    }

    @Test
    void isNullOrEmpty_withNonEmptyString_shouldReturnFalse() {
        assertFalse(stringUtil.isNullOrEmpty("Mario"));
    }

    @Test
    void isNullOrEmpty_withWhitespaceOnly_shouldReturnFalse() {
        assertFalse(stringUtil.isNullOrEmpty("   "));
    }
}
