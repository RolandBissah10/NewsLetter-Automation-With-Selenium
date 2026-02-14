package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NewsLetterPage extends BasePage {

    public NewsLetterPage(WebDriver driver) {
        super(driver);
    }

    // ===== Locators =====
    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "subscribe-btn")
    private WebElement submitButton;

    @FindBy(id = "email-error")
    private WebElement errorMessage;

    @FindBy(id = "signup-card")
    private WebElement signupCard;

    // ===== Actions =====
    public void enterEmail(String email) {
        type(emailInput, email);
    }

    public void clickSubmit() {
        click(submitButton);
    }

    public void subscribe(String email) {
        enterEmail(email);
        clickSubmit();
    }

    // ===== Validations =====
    public boolean isErrorVisible() {
        return isVisible(errorMessage);
    }

    public boolean isErrorHidden() {
        return !isErrorVisible();
    }

    public String getErrorMessageText() {
        WebElement e = waitForVisibility(errorMessage);
        return e.getText().trim();
    }

    public boolean isSignupVisible() {
        return isVisible(signupCard);
    }

    public String getEmailValue() {
        return getValue(emailInput);
    }

    public boolean isEmailInputEmpty() {
        return getEmailValue().isEmpty();
    }

    public void hoverOverSubmitButton() {
        hoverOver(submitButton);
    }
}
