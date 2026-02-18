package org.example.base;

import org.example.utils.DriverFactory;
import org.example.pages.NewsLetterPage;
import org.example.pages.SuccessPage;
import org.example.utils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public class Setup {

    protected WebDriver driver;
    protected NewsLetterPage newsLetterPage;
    protected SuccessPage successPage;

    @BeforeEach
    public void setUp() {
        driver = DriverFactory.createDriver();
        driver.get(TestData.BASE_URL);
        newsLetterPage = new NewsLetterPage(driver);
        successPage = new SuccessPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver == null) return;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            driver.quit();
        }
    }
}