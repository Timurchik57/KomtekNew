package UI.TmTest.PageObject.Directions.Consultation;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class QueueHospitalizations extends BaseTest {
    /**
     * Консультации - Исходящие - Очередь госпитализаций
     */
    public QueueHospitalizations(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Консультации - Исходящие - Очередь госпитализаций
     */
    public By QueueHospitalizations = By.xpath(
            "//a[@href='/direction/requestConsultation/evacuation?directionType=2&directionTarget=1&directionPageType=4']");

    /**
     * Очередь госпитализаций - 1 запись в таблице
     */
    public By TableFirstWait = By.xpath("//tbody/tr[1]");

    /**
     * Очередь госпитализаций - Эвакуирован
     */
    public By Evacuated = By.xpath("//tbody/tr/td[8][contains(.,'Эвакуирован')]");

    /**
     * Очередь госпитализаций - Следующая страница
     */
    public By Next = By.xpath("//button[@class='btn-next']");

    /**
     * Эвакуирован - Госпитализировать
     */
    public By ButtonHospital = By.xpath("(//button[contains(.,'Госпитализировать')])[1]");

    /**
     * Госпитализировать - Факт госпитализации
     */
    @FindBy(xpath = "//span[contains(.,'Факт госпитализации')]")
    public WebElement Fact;
    public By FactWait = By.xpath("//span[contains(.,'Факт госпитализации')]");

    /**
     * Госпитализировать - Дата госпитализации
     */
    @FindBy(xpath = "//label[contains(.,'Дата госпитализации')]")
    public WebElement Date;

    /**
     * Госпитализировать - Закрыть
     */
    public By ButtonClose = By.xpath("//div[@aria-label='Факт госпитализации']//button[contains(.,'Закрыть')]");
}
