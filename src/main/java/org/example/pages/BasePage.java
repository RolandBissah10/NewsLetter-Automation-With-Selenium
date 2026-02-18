package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Actions actions;
    protected final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected boolean isVisible(WebElement element) {
        try {
            return waitForVisibility(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void click(WebElement element) {
        waitForClickable(element).click();
    }

    protected void hoverOver(WebElement element) {
        waitForVisibility(element);
        actions.moveToElement(element).perform();
    }

    protected void type(WebElement element, String text) {
        waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
    }

    protected String getValue(WebElement element) {
        return waitForVisibility(element).getAttribute("value");
    }
}
