package utils;

import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

// import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverFactory {
    public static WebDriver createDriver() {
        // WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.gecko.driver", "C://DEPI//geckodriver.exe");
        return new FirefoxDriver();
    }
}