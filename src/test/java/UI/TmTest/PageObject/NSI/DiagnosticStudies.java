package UI.TmTest.PageObject.NSI;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class DiagnosticStudies extends BaseTest {
    /**
     * НСИ - Диагностические исследования
     */
    public DiagnosticStudies(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * НСИ - Диагностические исследования
     */
    public By DiagnosticStudies = By.xpath("//a[@href='/nsi/diagresearch']");
    /**
     * Диагностические исследования - добавить
     */
    public By Add = By.xpath("//header/button[contains(.,'Добавить')]");
    /**
     * Добавить - Наименование
     */
    @FindBy(xpath = "//label[@for='name']")
    public WebElement Name;
    public By NameWait = By.xpath("//label[@for='name']");
    /**
     * Добавить - Закрыть
     */
    public By Close = By.xpath("//button[contains(.,'Закрыть')]");
}
