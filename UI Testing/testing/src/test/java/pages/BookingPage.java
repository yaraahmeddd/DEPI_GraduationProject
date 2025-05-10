package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // Locators
    private final By bookNowButton = By.cssSelector("button.btn.btn-primary");
    private final By reserveNowButton = By.cssSelector("button.btn.btn-primary");
    private final By firstNameField = By.cssSelector("input[name='firstname']");
    private final By lastNameField = By.cssSelector("input[name='lastname']");
    private final By emailField = By.cssSelector("input[name='email']");
    private final By phoneField = By.cssSelector("input[name='phone']");
    private final By submitButton = By.cssSelector("button.btn.btn-primary");
    private final By successMessage = By.cssSelector("div.alert.alert-success");
    private final By validationErrors = By.cssSelector("div.alert.alert-danger");

    public BookingPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js = (JavascriptExecutor) driver;
    }

    public void waitForPageToLoad() {
        System.out.println("Waiting for page to load...");
        try {
            // Wait for page to be fully loaded
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            // Wait for either the Book Now button or form fields
            wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(bookNowButton),
                ExpectedConditions.presenceOfElementLocated(firstNameField)
            ));
            
            // Add a small delay to ensure page is stable
            Thread.sleep(2000);
            
            System.out.println("Page loaded successfully");
        } catch (Exception e) {
            System.out.println("Warning: Page elements not found: " + e.getMessage());
        }
    }

    public void clickReserveNow() {
        System.out.println("Clicking Reserve Now button...");
        try {
            // Wait for the page to be fully loaded
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            // Wait for the reserve now button to be clickable
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(reserveNowButton));
            
            // Scroll the button into view using JavaScript
            js.executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                button
            );
            
            // Add a small delay after scrolling
            Thread.sleep(2000);
            
            // Click using JavaScript
            js.executeScript("arguments[0].click();", button);
            System.out.println("Reserve Now button clicked");

            // Wait for form elements to be present and visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
            Thread.sleep(2000); // Add delay after form appears
            System.out.println("Form fields are visible");
            
        } catch (Exception e) {
            System.out.println("Error clicking Reserve Now: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to click Reserve Now button: " + e.getMessage());
        }
    }

    public void fillBookingForm(String firstName, String lastName, String email, String phone) {
        System.out.println("Filling booking form...");
        try {
            // Wait for form fields to be visible and interactable
            WebElement firstNameElement = wait.until(ExpectedConditions.elementToBeClickable(firstNameField));
            WebElement lastNameElement = wait.until(ExpectedConditions.elementToBeClickable(lastNameField));
            WebElement emailElement = wait.until(ExpectedConditions.elementToBeClickable(emailField));
            WebElement phoneElement = wait.until(ExpectedConditions.elementToBeClickable(phoneField));

            // Clear and fill each field with explicit waits
            fillField(firstNameElement, firstName);
            System.out.println("Filled first name: " + firstName);
            Thread.sleep(1000);

            fillField(lastNameElement, lastName);
            System.out.println("Filled last name: " + lastName);
            Thread.sleep(1000);

            fillField(emailElement, email);
            System.out.println("Filled email: " + email);
            Thread.sleep(1000);

            fillField(phoneElement, phone);
            System.out.println("Filled phone: " + phone);
            Thread.sleep(1000);

            System.out.println("Booking form filled successfully");
        } catch (Exception e) {
            System.out.println("Error filling booking form: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fill booking form: " + e.getMessage());
        }
    }

    private void fillField(WebElement element, String value) {
        try {
            // Wait for element to be clickable
            wait.until(ExpectedConditions.elementToBeClickable(element));
            
            // Scroll element into view
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            Thread.sleep(1000);
            
            // Clear field using regular Selenium
            element.clear();
            Thread.sleep(500);
            
            // Fill field using regular Selenium
            element.sendKeys(value);
            Thread.sleep(500);
            
            // Verify the value was set correctly
            String actualValue = element.getAttribute("value");
            if (!actualValue.equals(value)) {
                System.out.println("Regular filling failed, trying JavaScript method");
                js.executeScript("arguments[0].value = arguments[1];", element, value);
                Thread.sleep(500);
                js.executeScript(
                    "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    element
                );
                Thread.sleep(500);
            }
        } catch (Exception e) {
            System.out.println("Field filling failed: " + e.getMessage());
            throw new RuntimeException("Failed to fill field: " + e.getMessage());
        }
    }

    public boolean isSubmitButtonDisappeared() {
        try {
            // Wait for page to be fully loaded
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            // Try to find the submit button with a shorter timeout
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            
            try {
                // If we can find the button, it hasn't disappeared
                shortWait.until(ExpectedConditions.presenceOfElementLocated(submitButton));
                System.out.println("Submit button is still present");
                return false;
            } catch (Exception e) {
                System.out.println("Submit button has disappeared");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error checking submit button: " + e.getMessage());
            return false;
        }
    }

    public void submitBooking() {
        System.out.println("Submitting booking...");
        int maxRetries = 3;
        int currentRetry = 0;
        
        while (currentRetry < maxRetries) {
            try {
                // Wait for submit button to be clickable
                WebElement submitElement = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
                
                // Scroll the button into view
                js.executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                    submitElement
                );
                
                // Add a small delay after scrolling
                Thread.sleep(2000);
                
                // Get the current URL before submission
                String urlBeforeSubmit = driver.getCurrentUrl();
                System.out.println("URL before submission: " + urlBeforeSubmit);
                
                // Find the form element
                WebElement form = submitElement.findElement(By.xpath("./ancestor::form"));
                
                // Get all form fields
                List<WebElement> formFields = form.findElements(By.cssSelector("input"));
                System.out.println("Found " + formFields.size() + " form fields");
                
                // Print form field values for debugging
                for (WebElement field : formFields) {
                    String name = field.getAttribute("name");
                    String value = field.getAttribute("value");
                    System.out.println("Field: " + name + " = " + value);
                }

                // Try direct form submission first
                try {
                    System.out.println("Attempting direct form submission...");
                  submitElement.click();
                    System.out.println("Form submitted directly");
                } catch (Exception e) {
                    System.out.println("Direct submission failed: " + e.getMessage());
                    
                    // Try JavaScript click as fallback
                    try {
                        System.out.println("Attempting JavaScript click...");
                        js.executeScript("arguments[0].click();", submitElement);
                        System.out.println("Button clicked via JavaScript");
                    } catch (Exception jsError) {
                        System.out.println("JavaScript click failed: " + jsError.getMessage());
                        throw new RuntimeException("All submission methods failed");
                    }
                }
                
                // Wait for page response
                System.out.println("Waiting for page response...");
                Thread.sleep(5000);
                
                // Check if we're still on the same page
                String currentUrl = driver.getCurrentUrl();
                System.out.println("Current URL: " + currentUrl);
                
                // Check for any alerts or messages
                List<WebElement> alerts = driver.findElements(By.cssSelector("div.alert, div.message, div.notification, div.error"));
                if (!alerts.isEmpty()) {
                    System.out.println("Found " + alerts.size() + " messages:");
                    for (WebElement alert : alerts) {
                        System.out.println("Message: " + alert.getText());
                    }
                }
                
                // Check if form is still present
                try {
                    WebElement formAfterSubmit = driver.findElement(By.xpath("//form"));
                    System.out.println("Form is still present on page");
                    
                    // Check if submit button is still present
                    if (isSubmitButtonDisappeared()) {
                        System.out.println("Submit button has disappeared");
                        return; // Success case
                    } else {
                        System.out.println("Submit button is still present");
                        throw new RuntimeException("Form submission may have failed - submit button still present");
                    }
                } catch (Exception e) {
                    System.out.println("Form is no longer present on page - submission may have succeeded");
                    return; // Success case
                }
                
            } catch (Exception e) {
                currentRetry++;
                System.out.println("Attempt " + currentRetry + " failed: " + e.getMessage());
                
                if (currentRetry >= maxRetries) {
                    throw new RuntimeException("Failed to submit booking after " + maxRetries + " attempts: " + e.getMessage());
                }
                
                // Wait before retrying
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            // Wait for page to be fully loaded
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            // Try to find success message with a shorter timeout
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            
            // Try multiple selectors for success message
            By[] successSelectors = {
                By.cssSelector("div.alert.alert-success"),
                By.cssSelector("div.alert-success"),
                By.cssSelector("div.alert"),
                By.xpath("//div[contains(@class, 'alert') and contains(@class, 'success')]"),
                By.xpath("//div[contains(text(), 'Booking Successful')]")
            };
            
            for (By selector : successSelectors) {
                try {
                    WebElement messageElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(selector));
                    String messageText = messageElement.getText().trim();
                    System.out.println("Found potential success message with selector " + selector + ": " + messageText);
                    
                    if (messageText.toLowerCase().contains("success") || 
                        messageText.toLowerCase().contains("booking")) {
                        System.out.println("Success message found: " + messageText);
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("Selector " + selector + " did not find success message");
                }
            }
            
            // If no success message found with selectors, check page source
            String pageSource = driver.getPageSource().toLowerCase();
            if (pageSource.contains("booking successful") || 
                pageSource.contains("success")) {
                System.out.println("Success message found in page source");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.out.println("Error checking success message: " + e.getMessage());
            return false;
        }
    }

    public String getSuccessMessage() {
        try {
            // Wait for page to be fully loaded
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            // Try multiple selectors for success message
            By[] successSelectors = {
                By.cssSelector("div.alert.alert-success"),
                By.cssSelector("div.alert-success"),
                By.cssSelector("div.alert"),
                By.xpath("//div[contains(@class, 'alert') and contains(@class, 'success')]"),
                By.xpath("//div[contains(text(), 'Booking Successful')]")
            };
            
            for (By selector : successSelectors) {
                try {
                    WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
                    String message = messageElement.getText().trim();
                    if (message.toLowerCase().contains("success") || 
                        message.toLowerCase().contains("booking")) {
                        System.out.println("Found success message: " + message);
                        return message;
                    }
                } catch (Exception e) {
                    System.out.println("Selector " + selector + " did not find success message");
                }
            }
            
            // If no success message found with selectors, check page source
            String pageSource = driver.getPageSource();
            if (pageSource.toLowerCase().contains("booking successful")) {
                return "Booking Successful";
            }
            
            return "";
        } catch (Exception e) {
            System.out.println("Error getting success message: " + e.getMessage());
            return "";
        }
    }

    public List<WebElement> getValidationErrors() {
        try {
            // Wait for page to be fully loaded
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            // Try to find validation errors with a shorter timeout
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return shortWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(validationErrors));
        } catch (Exception e) {
            System.out.println("No validation errors found: " + e.getMessage());
            return List.of();
        }
    }
}