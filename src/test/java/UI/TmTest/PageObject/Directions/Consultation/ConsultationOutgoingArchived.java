package UI.TmTest.PageObject.Directions.Consultation;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;


public class ConsultationOutgoingArchived extends BaseTest {
    /**
     * Направления - Консультации - Исходящие - Архивные
     **/
    public ConsultationOutgoingArchived(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления - Консультации - Исходящие - Архивные
     **/
    public By OutgoingArchived = By.xpath(
            "//a[@href='/direction/requestConsultation/archival?directionType=2&directionTarget=1&directionPageType=2']");

    /**
     * Записи на странице - сортировка desc
     **/
    public By DESK = By.xpath("//thead[@class='has-gutter']//th[1]//i[@class='sort-caret descending']");

    /**
     * Консультации / Исходящие / Архивные - Записи на странице - с нужным статусом
     */
    public By Line (String status) {
        By Add = By.xpath("//tbody/tr/td/div/span[contains(.,'"+status+"')]");
        return Add;
    }

    /**
     * Консультации / Исходящие / Архивные - Записи на странице - всё количество записей
     */
    public By ConsultationCountList = By.xpath("//tbody/tr");

    /**
     * Консультации / Исходящие / Архивные - Всего консультаций
     */
    public By ConsultationCount = By.xpath("//span[@class='el-pagination__total']");

    /**
     * Разворачиваем селекты
     */
    public By SelectAllClick = By.xpath("//button[@class='el-button el-tooltip m-0 el-button--default el-button--small']");

    /**
     * Консультации / Исходящие / Архивные - Статус
     */
    public By Status (String value) {
        By Add = By.xpath("//div[@class='form-item-label'][contains(.,'"+value+"')]/following-sibling::div");
        return Add;
    }

    /**
     * Консультации / Исходящие / Архивные - Статус
     */
    public By Search = By.xpath("//button[contains(.,'Поиск')]");

    /**
     * Консультация - Текст заключения
     **/
    public By TextConclusion = By.xpath("//div/h3[contains(.,'Заключение')]/parent::div/following-sibling::div/div");
}
