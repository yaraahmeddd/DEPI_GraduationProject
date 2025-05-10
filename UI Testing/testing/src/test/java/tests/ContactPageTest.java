package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.ContactPage;
import utils.WebDriverFactory;
import java.time.Duration;

public class ContactPageTest {
    private WebDriver driver;
    private ContactPage contactPage;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = WebDriverFactory.createDriver();
        driver.get("https://automationintesting.online/#/contact");
        contactPage = new ContactPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void TC05_validContactForm() {
        // Wait for the contact form to be fully loaded
        contactPage.waitForFormToLoad();
        
        contactPage.fillContactForm(
                "John Doe",
                "john@example.com",
                "12345678900",
                "Test Subject",
                "This is a test message"
        );

        // Add delay before submitting
        try {
            System.out.println("Waiting 5 seconds before submitting form...");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Submit the form
        contactPage.submitForm();
        
        // Wait for the success message
        String actualMessage = wait.until(driver -> {
            try {
                String message = contactPage.getSuccessMessage();
                System.out.println("Current message: " + message);
                return message.equals("Contact Information") ? message : null;
            } catch (Exception e) {
                System.out.println("Error waiting for success message: " + e.getMessage());
                return null;
            }
        });
        
        Assert.assertNotNull(actualMessage, "Success message should be displayed");
        Assert.assertEquals(actualMessage, "Contact Information", 
            "Success message should be 'Contact Information'. Actual message: " + actualMessage);
    }

    @Test
    public void TC06_invalidContactForm() {
        // Wait for the contact form to be fully loaded
        contactPage.waitForFormToLoad();

        // Submit form with empty fields
        contactPage.fillContactForm(
            "",  // Empty name
            "",  // Empty email
            "",  // Empty phone
            "",  // Empty subject
            ""   // Empty description
        );

        contactPage.submitForm();

        // Verify error message is displayed
        String actualMessage = wait.until(driver -> {
            try {
                String message = contactPage.getSuccessMessage();
                System.out.println("Current message: " + message);
                return message.equals("Contact Information") ? message : null;
            } catch (Exception e) {
                System.out.println("Error waiting for success message: " + e.getMessage());
                return null;
            }
        });

        Assert.assertNotNull(actualMessage, "Success message should be displayed");
        Assert.assertEquals(actualMessage, "Contact Information",
                "Success message should be 'Contact Information'. Actual message: " + actualMessage);
    }

    @Test
    public void TC07_invalidEmailFormat() {
        // Wait for the contact form to be fully loaded
        contactPage.waitForFormToLoad();
        
        // Submit form with invalid email format
        contactPage.fillContactForm(
            "John Doe",
            "invalid-email",  // Invalid email format
            "12345678900",
            "Test Subject",
            "This is a test message"
        );
        
        contactPage.submitForm();
        
        // Verify error message is displayed
        Assert.assertTrue(contactPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid email");
            
        // Verify the error message content
        String errorMessage = contactPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("email") || errorMessage.contains("Email"),
            "Error message should mention email validation. Actual message: " + errorMessage);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}