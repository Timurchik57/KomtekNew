package UI.TmTest.PageObject.Registry;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class RegistrPatients extends BaseTest {
    public RegistrPatients(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Регистр пациентов
     */
    @FindBy(xpath = "//a[@href='/registry/patients-search']")
    public WebElement Registry;
    public By RegistryWait = By.linkText("Регистр пациентов");

    /**
     * Регистр пациентов
     */
    public By Header = By.xpath("//h1[.='Регистр пациентов']");

    /**
     * Регистр пациентов - Поиск
     */
    @FindBy(xpath = "//input[@placeholder='Введите значение для поиска пациента']")
    public WebElement Search;
    public By SearchWait = By.xpath("//input[@placeholder='Введите значение для поиска пациента']");
    @FindBy(xpath = "//button[@type='submit']")
    public WebElement SearchButton;

    /**
     * Регистр пациентов - Результаты поиска
     */
    public By ResultSearch = By.xpath("//tbody/tr/td[3]//span[contains(.,'АЛИ ОГЛЫ')]");

    /**
     * Медицинская организация
     */
    public By FIO = By.xpath("//div[@class='form-item-label'][contains(.,'ФИО пациента')]");

    /**
     * Дистанция
     */
    public By DistanceFIO = By.xpath("//i[@class='el-icon-search search-icon margin-bottom-3']");

    /**
     * Дистанция
     */
    public By Distance = By.xpath("//div[@class='el-form-item el-form-item--small']");
}
