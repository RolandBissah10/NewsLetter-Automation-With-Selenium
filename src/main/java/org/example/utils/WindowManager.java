package org.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WindowManager {

    private final WebDriver driver;

    public WindowManager(WebDriver driver) {
        this.driver = driver;
    }


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


    public void switchToWindowWithOrFirst(By locator) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (!driver.findElements(locator).isEmpty()) return;
        }
    }
}