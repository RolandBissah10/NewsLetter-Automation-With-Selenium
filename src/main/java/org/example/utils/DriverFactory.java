package org.example.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    private static final int WINDOW_WIDTH  = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private DriverFactory() {}

    public static WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        if (isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        return driver;
    }

    private static boolean isHeadless() {
        String defaultValue = System.getenv().containsKey("CI") ? "true" : "false";
        return Boolean.parseBoolean(System.getProperty("headless", defaultValue));
    }
}