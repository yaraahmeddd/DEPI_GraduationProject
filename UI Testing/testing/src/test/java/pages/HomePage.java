package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private final By contactButton = By.cssSelector("a[href='#/contact']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickContactButton() {
        // Wait for the contact button to be clickable
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(contactButton));
        
        // Click using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('a[href=\"#/contact\"]').click();");
        
        // Wait for URL to change
        wait.until(ExpectedConditions.urlContains("#/contact"));
        
        // Add a small delay to ensure the page loads
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
} 