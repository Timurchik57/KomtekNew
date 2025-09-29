package UI.TmTest.PageObject.Directions;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class HospitalizationSchedule extends BaseTest {
    /**
     * Направления - Расписание госпитализаций
     */
    public HospitalizationSchedule(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления - Расписание госпитализаций
     */
    @FindBy(xpath = "//li//a[@href='/hospitalschedule']")
    public WebElement HospitalizationElement;
    public By HospitalizationWait = By.xpath("//li//a[@href='/hospitalschedule']");
    public By Hospitalization = By.linkText("Расписание госпитализаций");

    /**
     * Расписание госпитализаций - Настройка расписания госпитализаций
     */
    public By Settings = By.xpath("//a[@href='/hospitalschedule/settings']");

    /**
     * Настройка расписания госпитализаций - Добавить расписание
     */
    public By Add = By.xpath("//button//span[contains(.,'Добавить расписание')]");

    /**
     * Добавить расписание - Период
     */
    @FindBy(xpath = "//label[@class='el-form-item__label'][contains(.,'Период')]")
    public WebElement Period;
    public By PeriodWait = By.xpath("//label[@class='el-form-item__label'][contains(.,'Период')]");

    /**
     * Добавить расписание - Начало периода
     */
    @FindBy(xpath = "//input[@placeholder='Начало периода']")
    public WebElement FirstPeriod;
    public By FirstPeriodWait = By.xpath("//input[@placeholder='Начало периода']");
    public By Today = By.xpath("(//td[@class='available today'])[1]");
    public By TodayTwo = By.xpath("(//td[@class='available today current'])[2]");

    /**
     * Добавить расписание - Подразделение
     */
    public By DivisionWait = By.xpath("//input[@placeholder='Выберите подразделение']");
    public By SelectDivisionDP = By.xpath("//div[@x-placement]//ul/li/span[text()='Детская поликлиника']");
    public By SelectDivisionRO = By.xpath("//div[@x-placement]//ul/li/span[text()='Рентгенологическое отделение']");

    /**
     * Добавить расписание - отделение
     */
    public By DepartmentWait = By.xpath("//input[@placeholder='Выберите отделение']");
    public By SelectDepartmentList = By.xpath("//div[@x-placement]//ul/li/span");

    /**
     * Добавить расписание - Закрыть
     */
    public By Close = By.xpath("//button/span[contains(.,'Закрыть')]");
}
