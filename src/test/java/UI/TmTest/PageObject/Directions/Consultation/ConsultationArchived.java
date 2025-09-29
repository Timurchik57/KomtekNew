package UI.TmTest.PageObject.Directions.Consultation;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class ConsultationArchived extends BaseTest {
    /**
     * Направления - Консультации - Входящие - Архивные
     **/
    public ConsultationArchived(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления - Консультации - Входящие - Архивные
     **/
    @FindBy(xpath = "//a[@href='/direction/incomingConsultation/archival?directionType=2&directionTarget=2&directionPageType=2']")
    public WebElement Archived;
    public By ArchivedWait = By.xpath(
            "//a[@href='/direction/incomingConsultation/archival?directionType=2&directionTarget=2&directionPageType=2']");

    /**
     * Заголовок
     **/
    public By HeaderWait = By.xpath("//header/h1[contains(.,'Консультации / Входящие / Архивные')]");

    /**
     * Разворачиваем селекты
     */
    public By SelectAllClick = By.xpath("//button[@class='el-button el-tooltip m-0 el-button--default el-button--small']");

    /**
     * 1 запись
     **/
    public By FirstWait = By.xpath("//tbody/tr[1]");

    /**
     * Записи на странице - сортировка desc
     **/
    public By DESK = By.xpath("//thead[@class='has-gutter']//th[1]//i[@class='sort-caret descending']");

    /**
     * Последняя запись
     **/
    public By LastWait = By.xpath("//tbody/tr[last()]");

    /**
     * Консультация - Текст заключения
     **/
    public By TextConclusion = By.xpath("//div/h3[contains(.,'Заключение')]/parent::div/following-sibling::div/div");
}
