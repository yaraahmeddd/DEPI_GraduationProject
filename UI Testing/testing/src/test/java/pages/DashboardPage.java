package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By adminPanelHeader = By.xpath("//h1[contains(text(),'Admin Panel')]");
    private final By roomManagementSection = By.xpath("//h2[contains(text(),'Rooms')]");
    private final By createRoomButton = By.id("createRoom");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isAt() {
        try {
            // Wait for the admin panel header to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(adminPanelHeader));
            // Check if we're on the admin page by verifying the URL
            return driver.getCurrentUrl().contains("/admin/");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCreateRoomButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(createRoomButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}