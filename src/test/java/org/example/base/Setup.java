package org.example.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.NewsLetterPage;
import org.example.pages.SuccessPage;
import org.example.utils.TestData;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Setup {

    protected static WebDriver driver;
    protected static final String BASE_URL = TestData.BASE_URL;
    protected NewsLetterPage newsLetterPage;
    protected SuccessPage successPage;

    @BeforeEach
    public void setUp() {
        // allow overriding headless via -Dheadless=false for local debugging
        // default: headless=true on CI, headless=false locally
        boolean headless = Boolean
                .parseBoolean(System.getProperty("headless", System.getenv().containsKey("CI") ? "true" : "false"));

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1280, 1024));

        driver.get(BASE_URL);

        newsLetterPage = new NewsLetterPage(driver);
        successPage = new SuccessPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            driver.quit();
        }
    }
}
