package org.example.pages;

import org.example.utils.TestData;
import org.example.utils.WindowManager;
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

    private final WindowManager windowManager;

    public SuccessPage(WebDriver driver) {
        super(driver);
        this.windowManager = new WindowManager(driver);
    }

    public boolean isOnSuccessPage() {
        return windowManager.switchToWindowWith(SUCCESS_MESSAGE_BY) && isVisible(successMessage);
    }

    public void hoverOverDismissButton() {
        if (!windowManager.switchToWindowWith(DISMISS_BUTTON_BY)) {
            throw new IllegalStateException("Success page/window not found");
        }
        hoverOver(dismissButton);
    }

    public void clickDismiss() {
        if (!windowManager.switchToWindowWith(DISMISS_BUTTON_BY)) {
            throw new IllegalStateException("Success page/window not found");
        }
        click(dismissButton);
        waitForSignupCard();
    }

    private void waitForSignupCard() {
        wait.until(d -> {
            try {
                if (driver.getWindowHandles().size() == 1) return true;
                return windowManager.anyWindowContains(SIGNUP_CARD_BY);
            } catch (Exception ignored) {
            }
            return false;
        });

        windowManager.switchToWindowWithOrFirst(SIGNUP_CARD_BY);
    }
}