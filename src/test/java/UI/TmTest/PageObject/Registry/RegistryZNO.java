package UI.TmTest.PageObject.Registry;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class RegistryZNO extends BaseTest {
    public RegistryZNO(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Аналитика
     */
    @FindBy(xpath = "//a[@href='/registry/oncology-screening']")
    public WebElement RegistryZNO;

    /**
     * Диагноз
     */
    public By Diagnosis = By.xpath("//div[@class='form-item-label'][contains(.,'Диагноз')]");

    /**
     * Статус
     */
    public By Status = By.xpath("//div[@class='form-item-label'][contains(.,'Статус')]");

    /**
     * Дистанция
     */
    public By DistanceDiagnosis = By.xpath("//div[@class='el-col el-col-6']");

    /**
     * Дистанция
     */
    public By DistanceStatus = By.xpath("(//div[@class='el-col el-col-4'])[1]");

    /**
     * Дистанция
     */
    public By Distance = By.xpath("(//div[@class='el-col el-col-4'])[2]");

    /**
     * Поиск
     */
    public By Search = By.xpath("//button[@type='submit'][contains(.,'Поиск')]");
}
