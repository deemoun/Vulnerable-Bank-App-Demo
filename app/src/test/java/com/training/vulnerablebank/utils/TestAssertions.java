package com.training.vulnerablebank.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAssertions {
    private TestAssertions(){

    }

    public static void assertTextEqualsAny(
            String actualText,
            String expectedText1,
            String expectedText2
    ) {
        boolean textIsCorrect = actualText.equals(expectedText1)
                || actualText.equals(expectedText2);

        assertTrue(textIsCorrect);
    }
}