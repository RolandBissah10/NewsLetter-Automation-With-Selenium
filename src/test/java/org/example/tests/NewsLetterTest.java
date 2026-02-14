package org.example.tests;

import org.example.base.Setup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NewsLetterTest extends Setup {

    @Test
    @DisplayName("Verify alert is shown when email input is empty")
    void testEmptyEmail() {
        // Act
        newsLetterPage.clickSubmit();

        // Assert
        Assertions.assertTrue(newsLetterPage.isErrorVisible(), "Error box should be visible for empty email");
        Assertions.assertEquals("Valid email required", newsLetterPage.getErrorMessageText());
    }

    @Test
    @DisplayName("Verify alert is shown for invalid email")
    void testInvalidEmail() {
        // Act
        newsLetterPage.subscribe("invalid-email");

        // Assert
        Assertions.assertTrue(newsLetterPage.isErrorVisible(), "Error box should be visible for invalid email");
        Assertions.assertEquals("Valid email required", newsLetterPage.getErrorMessageText());
    }

    @Test
    @DisplayName("Verify successful subscription with valid email")
    void testValidEmail() {
        // Act
        newsLetterPage.subscribe("test@example.com");

        // Assert
        Assertions.assertTrue(successPage.isOnSuccessPage(), "Should land on success message after subscribing");
    }

    @Test
    @DisplayName("Verify dismiss button returns to signup page")
    void testDismissButton() {
        // Arrange: navigate to success page
        newsLetterPage.subscribe("test@example.com");
        Assertions.assertTrue(successPage.isOnSuccessPage(), "Setup: should be on success page");

        // Act
        successPage.clickDismiss();

        // Assert
        Assertions.assertTrue(newsLetterPage.isSignupVisible(), "Signup card should be visible after dismiss");
    }
}
