package org.example.pages;

import org.example.utils.TestData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NewsLetterPage extends BasePage {

    @FindBy(id = TestData.EMAIL_INPUT)
    private WebElement emailInput;

    @FindBy(id = TestData.SUBSCRIBE_BTN)
    private WebElement submitButton;

    @FindBy(id = TestData.EMAIL_ERROR)
    private WebElement errorMessage;

    @FindBy(id = TestData.SIGNUP_CARD)
    private WebElement signupCard;

    public NewsLetterPage(WebDriver driver) {
        super(driver);
    }

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

    public boolean isErrorVisible() {
        return isVisible(errorMessage);
    }

    public boolean isErrorHidden() {
        return !isErrorVisible();
    }

    public String getErrorMessageText() {
        return waitForVisibility(errorMessage).getText().trim();
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