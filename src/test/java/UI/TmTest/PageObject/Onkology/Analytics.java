package UI.TmTest.PageObject.Onkology;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class Analytics extends BaseTest {
    public Analytics(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Аналитика
     */
    @FindBy(xpath = "//a[@href='/oncology/analytics']")
    public WebElement Analytics;
    public By AnalyticsWait = By.xpath("//a[@href='/oncology/analytics']");

    /**
     * Аналитика - Медицинская организация
     */
    public By MO = By.xpath("//div[@class='form-item-label'][contains(.,'Медицинская организация')]");

    /**
     * Аналитика - Период
     */
    public By Period = By.xpath("//div[@class='form-item-label'][contains(.,'Период')]");

    /**
     * Аналитика - Тип скрининга
     */
    public By Type = By.xpath("//div[@class='form-item-label'][contains(.,'Тип формы')]");

    /**
     * Аналитика - Дистанция
     */
    public By DistanceMO = By.xpath("//div[@class='el-col el-col-8']");

    /**
     * Аналитика - Дистанция
     */
    public By DistancePeriod = By.xpath("//div[@class='el-col el-col-5']");

    /**
     * Аналитика - Дистанция
     */
    public By DistanceType = By.xpath("//div[@class='d-flex el-col el-col-9']");

    /**
     * Аналитика - Поиск
     */
    public By Search = By.xpath("//button[@type='button'][contains(.,'Поиск')]");

    /**
     * Аналитика - Поиск
     */
    public By AddExcel = By.xpath("//button[contains(.,'Сохранить в Excel')]");
}
