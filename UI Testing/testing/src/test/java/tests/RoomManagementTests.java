package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.RoomManagementPage;
import utils.WebDriverFactory;

import java.time.Duration;

public class RoomManagementTests {
    private WebDriver driver;
    private LoginPage loginPage;
    private RoomManagementPage roomManagementPage;

    @BeforeMethod
    public void setUp() {
        driver = WebDriverFactory.createDriver();
        driver.get("https://automationintesting.online/admin");
        loginPage = new LoginPage(driver);
        loginPage.enterUsername("admin");
        loginPage.enterPassword("password");
        loginPage.clickLogin();
        // Wait for the admin dashboard to load with increased timeout
        new WebDriverWait(driver, Duration.ofSeconds(30))
            .until(ExpectedConditions.visibilityOfElementLocated(By.id("createRoom")));
        roomManagementPage = new RoomManagementPage(driver);
    }

    @Test
    public void TC12_addRoom() throws InterruptedException {
        String roomName = String.valueOf(System.currentTimeMillis());
        System.out.println("[TC12] Adding room: " + roomName);

        roomManagementPage.fillRoomForm(roomName, "Single", true, "150", true, false); // WiFi only
        roomManagementPage.clickAddRoom();


        // Increased wait time to see any errors
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        Thread.sleep(3000);
        boolean isListed = wait.until(d -> roomManagementPage.isRoomListed(roomName));

        System.out.println("[TC12] Room listed: " + isListed);
        Thread.sleep(5000);
        Assert.assertTrue(isListed, "Room should be listed after adding.");
    }

    @Test
    public void TC13_editRoom() {
        String originalRoomName = String.valueOf(System.currentTimeMillis());
        String newRoomName = String.valueOf(System.currentTimeMillis() + 1);
        System.out.println("[TC13] Adding room to edit: " + originalRoomName);
        roomManagementPage.clickAddRoom();
        roomManagementPage.fillRoomForm(originalRoomName, "Double", false, "100", false, true); // TV only
        roomManagementPage.saveRoom();
        // Increased wait time
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        Assert.assertTrue(wait.until(d -> roomManagementPage.isRoomListed(originalRoomName)), "Room should be listed before editing.");

        System.out.println("[TC13] Editing first room to: " + newRoomName);
        roomManagementPage.editFirstRoom(newRoomName, "200");
        boolean isEdited = wait.until(d -> roomManagementPage.isRoomListed(newRoomName));
        System.out.println("[TC13] Room edited: " + isEdited);
        Assert.assertTrue(isEdited, "Room should be listed with new name after editing.");
    }

    @Test
    public void TC14_deleteRoom() {
        String roomName = "DeleteRoom" + System.currentTimeMillis();
        System.out.println("[TC14] Adding room to delete: " + roomName);
        roomManagementPage.clickAddRoom();
        roomManagementPage.fillRoomForm(roomName, "Suite", true, "100", true, true); // WiFi & TV
        roomManagementPage.saveRoom();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Assert.assertTrue(wait.until(d -> roomManagementPage.isRoomListed(roomName)), "Room should be listed before deleting.");

        System.out.println("[TC14] Deleting first room...");
        roomManagementPage.deleteRoom();
        boolean isDeleted = wait.until(d -> !roomManagementPage.isRoomListed(roomName));
        System.out.println("[TC14] Room deleted: " + isDeleted);
        Assert.assertTrue(isDeleted, "Room should not be listed after deleting.");
    }

    @Test
    public void TC15_addRoomMissingFields() {
        System.out.println("[TC15] Adding room with missing fields...");
        roomManagementPage.clickAddRoom();
        roomManagementPage.fillRoomForm("", "Single", false, "", false, false); // missing name & price
        roomManagementPage.saveRoom();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String errorMsg = wait.until(d -> roomManagementPage.getErrorMessage());
        System.out.println("[TC15] Error message: " + errorMsg);
        Assert.assertTrue(errorMsg.length() > 0, "Error message should be displayed for missing fields.");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}