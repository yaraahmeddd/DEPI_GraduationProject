package com.restfulbooker;

// import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.chrome.ChromeDriver;
// import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected static final String BASE_URL = "https://automationintesting.online/";

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "C://DEPI//geckodriver.exe");
        driver= new FirefoxDriver();
        // ChromeOptions options = new ChromeOptions();
        // options.addArguments("--start-maximized");
        // driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.get(BASE_URL);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
