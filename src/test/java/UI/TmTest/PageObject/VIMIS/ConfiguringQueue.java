package UI.TmTest.PageObject.VIMIS;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class ConfiguringQueue extends BaseTest {
    /**
     * Вимис - Настройка очереди
     */
    public ConfiguringQueue(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Вимис - Настройка очереди
     */
    @FindBy(xpath = "//a[@href='/vimis/vimis-settings']")
    public WebElement Queue;
    public By QueueWait = By.xpath("//a[@href='/vimis/vimis-settings']");
    /**
     * Настройка очереди - Заголовок
     */
    public By Header = By.xpath("//header/h1[.='Настройки шлюза']");
    /**
     * Настройка очереди - Параметры очереди отправки
     */
    @FindBy(xpath = "//label[@for='maxSmsToSend']")
    public WebElement QueueSending;
    public By QueueSendingWait = By.xpath("//label[@for='maxSmsToSend']");
}
