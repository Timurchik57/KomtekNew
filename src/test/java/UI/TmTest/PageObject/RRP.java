package UI.TmTest.PageObject;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class RRP extends BaseTest {
    /**
     * РРП
     */
    public RRP(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Логин
     */
    @FindBy(xpath = "//input[@formcontrolname='username']")
    public WebElement Login;
    public By LoginWait = By.xpath("//input[@formcontrolname='username']");
    /**
     * Пароль
     */
    @FindBy(xpath = "//input[@formcontrolname='password']")
    public WebElement Password;
    public By PasswordWait = By.xpath("//input[@formcontrolname='password']");
    /**
     * Войти
     */
    @FindBy(xpath = "//button/span[contains(.,'Войти')]")
    public WebElement Enter;
    /**
     * Снилс
     */
    @FindBy(xpath = "//input[@name='snilsSearch']")
    public WebElement Snils;
    public By SnilsWait = By.xpath("//input[@name='snilsSearch']");
    /**
     * Поиск
     */
    @FindBy(xpath = "//button/span[contains(.,'Поиск')]")
    public WebElement Search;
    public By SearchWait = By.xpath("//button/span[contains(.,'Поиск')]");
    /**
     * Результат поиска Снилс
     */
    @FindBy(xpath = "//tbody/tr[1]/td[6]")
    public WebElement SearchSnils;
    public By SearchSnilsWait = By.xpath("//tbody/tr[1]/td[6]");
    /**
     * Результат поиска Енп
     */
    @FindBy(xpath = "//tbody/tr[1]/td[7]")
    public WebElement SearchEnp;
}
