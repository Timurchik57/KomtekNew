package UI.TmTest.PageObject.NSI;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class Researches extends BaseTest {
    /**
     * НСИ - Исследования
     */
    public Researches(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * НСИ - Исследования
     */
    public By Researches = By.xpath("//a[@href='/nsi/medicalreserch']");
    /**
     * Анатомические области - добавить
     */
    public By Add = By.xpath("//header/button[contains(.,'Добавить')]");
    /**
     * Добавить - Наименование
     */
    @FindBy(xpath = "//label[@for='id']")
    public WebElement Name;
    public By NameWait = By.xpath("//label[@for='id']");
    /**
     * Добавить - Тип мероприятия
     */
    @FindBy(xpath = "//div[@class='el-col el-col-6']")
    public WebElement Type;
    /**
     * Добавить - Закрыть
     */
    public By Close = By.xpath("//button[contains(.,'Закрыть')]");
}
