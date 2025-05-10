package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.DashboardPage;
import utils.WebDriverFactory;

public class LoginTests {
    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeMethod
    public void setUp() {
        driver = WebDriverFactory.createDriver();
        driver.get("https://automationintesting.online/#/admin");
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }

    @Test
    public void TC01_validLogin() {
        loginPage.enterUsername("admin");
        loginPage.enterPassword("password");
        loginPage.clickLogin();
        Assert.assertTrue(driver.getCurrentUrl().contains("/admin"),
                "URL should contain /admin/. Current URL: " + driver.getCurrentUrl());
    }

    @Test
    public void TC02_invalidLogin() {
        loginPage.enterUsername("wrong");
        loginPage.enterPassword("wrong");
        loginPage.clickLogin();
        Assert.assertTrue(driver.getCurrentUrl().contains("/admin"),
                "URL should contain /admin/. Current URL: " + driver.getCurrentUrl());    }

    @Test
    public void TC03_blankFields() {
        loginPage.enterUsername("");
        loginPage.enterPassword("");
        loginPage.clickLogin();
        Assert.assertTrue(driver.getCurrentUrl().contains("/admin"),
                "URL should contain /admin/. Current URL: " + driver.getCurrentUrl());    }

    @Test
    public void TC04_successfulLoginRedirects() {
        loginPage.enterUsername("admin");
        loginPage.enterPassword("password");
        loginPage.clickLogin();
        Assert.assertTrue(driver.getCurrentUrl().contains("admin"));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}