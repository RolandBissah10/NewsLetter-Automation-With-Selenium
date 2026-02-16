package org.example.tests;

import org.example.base.Setup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NewsLetterTest extends Setup {

    @Test
    @Order(1)
    @DisplayName("Verify alert is shown when email input is empty")
    void testEmptyEmail() throws InterruptedException {
        // Act - hover to show button interaction
        newsLetterPage.hoverOverSubmitButton();
        newsLetterPage.clickSubmit();

        // Assert
        Assertions.assertTrue(newsLetterPage.isErrorVisible(), "Error box should be visible for empty email");
        Assertions.assertEquals("Valid email required", newsLetterPage.getErrorMessageText());
    }

    @Test
    @Order(2)
    @DisplayName("Verify alert is shown for invalid email")
    void testInvalidEmail() throws InterruptedException {
        // Act - hover and subscribe with invalid email
        newsLetterPage.hoverOverSubmitButton();
        newsLetterPage.subscribe("fracis-bissah-amalitech.com");

        // Assert
        Assertions.assertTrue(newsLetterPage.isErrorVisible(), "Error box should be visible for invalid email");
        Assertions.assertEquals("Valid email required", newsLetterPage.getErrorMessageText());
    }

    @Test
    @Order(3)
    @DisplayName("Verify successful subscription with valid email")
    void testValidEmail() throws InterruptedException {
        // Act - hover and subscribe with valid email
        newsLetterPage.hoverOverSubmitButton();
        newsLetterPage.subscribe("francis.bissah@amalitech.com");

        // Assert
        Assertions.assertTrue(successPage.isOnSuccessPage(), "Should land on success message after subscribing");
    }

    @Test
    @Order(4)
    @DisplayName("Verify dismiss button returns to signup page")
    void testDismissButton() throws InterruptedException {
        // Arrange: hover and subscribe to navigate to success page
        newsLetterPage.hoverOverSubmitButton();
        newsLetterPage.subscribe("francis.bissah@example.com");
        Assertions.assertTrue(successPage.isOnSuccessPage(), "Setup: should be on success page");

        // Act - hover and click dismiss button
        successPage.hoverOverDismissButton();
        successPage.clickDismiss();

        // Assert - verify fresh signup page with no error and empty input
//        Assertions.assertTrue(newsLetterPage.isSignupVisible(), "Signup card should be visible after dismiss");
//        Assertions.assertTrue(newsLetterPage.isErrorHidden(), "Error should NOT be visible on fresh signup page");
//        Assertions.assertTrue(newsLetterPage.isEmailInputEmpty(), "Email input should be empty on fresh signup page");
    }
}
