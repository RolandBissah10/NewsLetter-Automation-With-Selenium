package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SuccessPage extends BasePage {

    private static final By SUCCESS_MESSAGE_BY = By.id("success-message");
    private static final By DISMISS_BUTTON_BY = By.id("dismiss-btn");
    private static final By SIGNUP_CARD_BY = By.id("signup-card");

    public SuccessPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "success-message")
    private WebElement successMessage;

    @FindBy(id = "dismiss-btn")
    private WebElement dismissButton;

    /**
     * Switches driver to a window that contains the given locator. Returns true if found.
     */
    private boolean switchToWindowWith(By locator) {
        String original = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            try {
                driver.switchTo().window(handle);
                if (!driver.findElements(locator).isEmpty()) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        // restore original handle if nothing matched
        try {
            driver.switchTo().window(original);
        } catch (Exception ignored) {
        }
        return false;
    }

    public boolean isOnSuccessPage() {
        if (!switchToWindowWith(SUCCESS_MESSAGE_BY)) {
            return false;
        }
        return isVisible(successMessage);
    }

    public void hoverOverDismissButton() {
        if (!switchToWindowWith(DISMISS_BUTTON_BY)) {
            throw new IllegalStateException("Success page/window not found");
        }
        hoverOver(dismissButton);
    }

    public void clickDismiss() {
        if (!switchToWindowWith(DISMISS_BUTTON_BY)) {
            throw new IllegalStateException("Success page/window not found");
        }

        click(dismissButton);

        // wait until either only one window remains or signup card is visible in some window
        wait.until(d -> {
            try {
                if (driver.getWindowHandles().size() == 1) return true;
                for (String h : driver.getWindowHandles()) {
                    driver.switchTo().window(h);
                    if (!driver.findElements(SIGNUP_CARD_BY).isEmpty()) return true;
                }
            } catch (Exception ignored) {
            }
            return false;
        });

        // switch to the window that contains the signup card (fallback to first handle)
        for (String h : driver.getWindowHandles()) {
            driver.switchTo().window(h);
            if (!driver.findElements(SIGNUP_CARD_BY).isEmpty()) return;
        }
    }
}

