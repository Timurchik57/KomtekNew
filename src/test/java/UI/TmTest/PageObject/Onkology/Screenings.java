package UI.TmTest.PageObject.Onkology;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class Screenings extends BaseTest {
    public Screenings(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Скрининги
     */
    @FindBy(xpath = "//a[@href='/oncology/screenings']")
    public WebElement Screening;
    public By ScreeningWait = By.xpath("//a[@href='/oncology/screenings']");
    /**
     * Скрининги - Медицинская организация
     */
    public By MO = By.xpath("//div[@class='form-item-label'][contains(.,'Медицинская организация')]");
    /**
     * Скрининги - Период
     */
    public By Period = By.xpath("//div[@class='form-item-label'][contains(.,'Период')]");
    /**
     * Скрининги - Тип скрининга
     */
    public By Type = By.xpath("//div[@class='form-item-label'][contains(.,'Тип скрининга')]");
    /**
     * Скрининги - Дистанция
     */
    public By DistanceMO = By.xpath("//div[@class='el-col el-col-8']");
    /**
     * Скрининги - Дистанция
     */
    public By DistancePeriod = By.xpath("//div[@class='el-col el-col-5']");
    /**
     * Скрининги - Дистанция
     */
    public By DistanceType = By.xpath("//div[@class='d-flex el-col el-col-9']");
    /**
     * Скрининги - Поиск
     */
    @FindBy(xpath = "//button[@type='button'][contains(.,'Поиск')]")
    public WebElement SearchWeb;
    public By Search = By.xpath("//button[@type='button'][contains(.,'Поиск')]");
    /**
     * Скрининги - Дистанция Поиск
     */
    public By DistanceSearch = By.xpath("//div[@class='el-form-item margin-0 mr-10 el-form-item--small']");
    /**
     * Скрининги - Поиск
     */
    public By AddExcel = By.xpath("//button[contains(.,'Сохранить в Excel')]");
}
