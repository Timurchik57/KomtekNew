package UI.TmTest.PageObject.NSI;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class AnatomicalAreas extends BaseTest {
    /**
     * НСИ - Анатомические области
     */
    public AnatomicalAreas(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * НСИ - Анатомические области
     */
    public By AnatomicalAreas = By.xpath("//a[@href='/nsi/anatomicarea']");
    /**
     * Анатомические области - Наименование
     */
    @FindBy(xpath = "//th/div[@class='cell'][contains(.,'Наименование')]")
    public WebElement Name;
    public By NameWait = By.xpath("//th/div[@class='cell'][contains(.,'Наименование')]");
    /**
     * Анатомические области - Редактировать у первой записи
     */
    public By Edit = By.xpath("//tbody/tr[1]//button[@tooltiptext='Редактировать']");
    /**
     * Редактировать у первой записи - Наименование
     */
    @FindBy(xpath = "//label[@for='name']")
    public WebElement NameEdit;
    public By NameEditWait = By.xpath("//label[@for='name']");
    /**
     * Редактировать у первой записи - Закрыть
     */
    public By Close = By.xpath("//button[contains(.,'Закрыть')]");
}
