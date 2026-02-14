package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SuccessPage extends BasePage {

    public SuccessPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "success-message")
    private WebElement successMessage;

    @FindBy(id = "dismiss-btn")
    private WebElement dismissButton;

    public boolean isOnSuccessPage() {
        return isVisible(successMessage);
    }

    public void clickDismiss() {
        click(dismissButton);
    }
}
