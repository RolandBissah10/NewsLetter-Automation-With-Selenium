package org.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WindowManager {

    private final WebDriver driver;

    public WindowManager(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Switches to the first window containing the given locator.
     * Restores the original window if none found. Returns true if switched.
     */
    public boolean switchToWindowWith(By locator) {
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
        try {
            driver.switchTo().window(original);
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Returns true if any open window contains the given locator.
     * Leaves the driver focused on that window if found.
     */
    public boolean anyWindowContains(By locator) {
        for (String handle : driver.getWindowHandles()) {
            try {
                driver.switchTo().window(handle);
                if (!driver.findElements(locator).isEmpty()) return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    /**
     * Switches to the window containing the given locator.
     * Falls back to the first available window if none match.
     */
    public void switchToWindowWithOrFirst(By locator) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (!driver.findElements(locator).isEmpty()) return;
        }
    }
}