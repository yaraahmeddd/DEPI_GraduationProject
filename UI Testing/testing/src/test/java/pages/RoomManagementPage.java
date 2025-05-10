package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RoomManagementPage {

    private final WebDriver driver;

    private final By addRoomButton = By.id("createRoom");
    private final By roomNameField = By.id("roomName");
    private final By roomTypeField = By.id("type");
    private final By accessibleField = By.id("accessible");
    private final By roomPriceField = By.id("roomPrice");
    private final By wifiCheckboxField = By.id("wifiCheckbox");
    private final By tvCheckboxField = By.id("tvCheckbox");
    private final By saveRoomButton = By.id("createRoom");
    private final By editRoomButton = By.cssSelector(".editRoom");
    private final By deleteRoomButton = By.cssSelector(".deleteRoom");
    private final By errorMessage = By.cssSelector(".alert-danger");

    public RoomManagementPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickAddRoom() {
        driver.findElement(addRoomButton).click();
    }

    public void fillRoomForm(String name, String type, boolean accessible, String price, boolean wifi, boolean tv) {
        setFieldWithEvents(roomNameField, name);
        selectDropdownByVisibleText(roomTypeField, type);

        String accessibleText = String.valueOf(accessible);
        selectDropdownByVisibleText(accessibleField, accessibleText);


        setFieldWithEvents(roomPriceField, price);

        setCheckbox(wifiCheckboxField, wifi);
        setCheckbox(tvCheckboxField, tv);
    }

    public void saveRoom() {
        driver.findElement(saveRoomButton).click();
    }

    public boolean isRoomListed(String roomName) {
        String xpath = "//*[@id='root-container']/div/div/div//*[contains(text(), '" + roomName + "')]";
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement roomElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            System.out.println("Clicking room with name: " + roomName);
            roomElement.click();
            return true;
        } catch (Exception e) {
            System.out.println("Room with name '" + roomName + "' not found.");
            return false;
        }
    }


    public void editFirstRoom(String newType, String newPrice) {
        driver.findElements(editRoomButton).get(0).click();
        fillRoomForm("", newType, true, newPrice, true, true);
        saveRoom();
    }

    public void deleteRoom() {
        driver.findElements(deleteRoomButton).get(0).click();
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    // ---------- Helper methods ----------

    private void setFieldWithEvents(By locator, String value) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(value);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", element);
        js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element);
    }

    private void selectDropdownByVisibleText(By locator, String visibleText) {
        WebElement dropdown = driver.findElement(locator);
        Select select = new Select(dropdown);
        select.selectByVisibleText(visibleText);
    }

    private void setCheckbox(By locator, boolean shouldBeChecked) {
        WebElement checkbox = driver.findElement(locator);
        if (checkbox.isSelected() != shouldBeChecked) {
            checkbox.click();
        }
    }
}