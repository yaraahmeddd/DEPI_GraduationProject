package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

public class ContactPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By nameInput = By.id("name");
    private final By emailInput = By.id("email");
    private final By phoneInput = By.id("phone");
    private final By subjectInput = By.id("subject");
    private final By descriptionInput = By.id("description");
    private final By submitButton = By.cssSelector("button.btn.btn-primary[type='button']");
    private final By successMessage = By.cssSelector("div.card-body h3.h4.mb-4");
    private final By errorMessage = By.cssSelector("div.alert-danger");

    public ContactPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForFormToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(subjectInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(submitButton));
    }

    public void fillContactForm(String name, String email, String phone, String subject, String description) {
        System.out.println("Starting to fill contact form...");
        
        // Wait for form to be ready
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        System.out.println("Form is visible, starting to fill fields...");
        
        // Fill form using regular Selenium sendKeys
        driver.findElement(nameInput).clear();
        driver.findElement(nameInput).sendKeys(name);
        System.out.println("Filled name: " + name);
        
        driver.findElement(emailInput).clear();
        driver.findElement(emailInput).sendKeys(email);
        System.out.println("Filled email: " + email);
        
        driver.findElement(phoneInput).clear();
        driver.findElement(phoneInput).sendKeys(phone);
        System.out.println("Filled phone: " + phone);
        
        driver.findElement(subjectInput).clear();
        driver.findElement(subjectInput).sendKeys(subject);
        System.out.println("Filled subject: " + subject);
        
        driver.findElement(descriptionInput).clear();
        driver.findElement(descriptionInput).sendKeys(description);
        System.out.println("Filled description: " + description);
        
        System.out.println("Form filling completed");
    }

    public void submitForm() {
        System.out.println("Attempting to submit form...");
        
        try {
            // Wait for button and verify it exists
            WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(submitButton));
            System.out.println("Submit button found");
            
            // Scroll the button into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            System.out.println("Scrolled to submit button");
            
            // Add a small delay after scrolling
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Click the button using JavaScript
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            System.out.println("Button clicked using JavaScript");
            
            // Wait for the success message to appear
            WebElement messageElement = wait.until(ExpectedConditions.presenceOfElementLocated(successMessage));
            String message = messageElement.getText();
            System.out.println("Success message appeared: " + message);
            
        } catch (Exception e) {
            System.out.println("Error during form submission: " + e.getMessage());
            throw e;
        }
    }

    public String getSuccessMessage() {
        try {
            // Wait for the success message to be visible
            WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            String message = messageElement.getText();
            System.out.println("Found success message: " + message);
            return message;
        } catch (Exception e) {
            System.out.println("Error getting success message: " + e.getMessage());
            return "";
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            // Wait for the success message to be visible
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            String message = element.getText();
            System.out.println("Checking success message: " + message);
            return element.isDisplayed() && message.startsWith("Thanks for getting in touch");
        } catch (Exception e) {
            System.out.println("Error checking success message: " + e.getMessage());
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean waitForSuccessMessageText(String expectedText, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(d -> d.getPageSource().contains(expectedText));
    }

    public String getSuccessMessageTextFromPageSource() {
        return driver.getPageSource();
    }
}