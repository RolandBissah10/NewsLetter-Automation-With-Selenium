package org.example.pages;

import org.example.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SuccessPage extends BasePage {

    private static final By SUCCESS_MESSAGE_BY = By.id(TestData.SUCCESS_MESSAGE);
    private static final By DISMISS_BUTTON_BY  = By.id(TestData.DISMISS_BTN);
    private static final By SIGNUP_CARD_BY     = By.id(TestData.SIGNUP_CARD);

    @FindBy(id = TestData.SUCCESS_MESSAGE)
    private WebElement successMessage;

    @FindBy(id = TestData.DISMISS_BTN)
    private WebElement dismissButton;

    public SuccessPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOnSuccessPage() {
        switchToWindow(SUCCESS_MESSAGE_BY);
        return isVisible(successMessage);
    }

    public void hoverOverDismissButton() {
        switchToWindow(DISMISS_BUTTON_BY);
        hoverOver(dismissButton);
    }

    public void clickDismiss() {
        switchToWindow(DISMISS_BUTTON_BY);
        click(dismissButton);
        waitForSignupCard();
    }

    private void switchToWindow(By locator) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (!driver.findElements(locator).isEmpty()) return;
        }
    }

    private void waitForSignupCard() {
        wait.until(d -> driver.getWindowHandles().size() == 1 || anyWindowHas(SIGNUP_CARD_BY));
        switchToWindow(SIGNUP_CARD_BY);
    }

    private boolean anyWindowHas(By locator) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (!driver.findElements(locator).isEmpty()) return true;
        }
        return false;
    }
}