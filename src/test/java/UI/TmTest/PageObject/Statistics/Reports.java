package UI.TmTest.PageObject.Statistics;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class Reports extends BaseTest {
    public Reports(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Статистика - Отчёты
     */
    @FindBy(xpath = "//li//a[@href='/stats/bi-reports']")
    public WebElement ReportsElement;
    public By ReportsWait = By.xpath("//li//a[@href='/stats/bi-reports']");
    public By Reports = By.linkText("Отчёты");
}
