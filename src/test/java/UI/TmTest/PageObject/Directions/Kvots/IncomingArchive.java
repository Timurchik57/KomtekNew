package UI.TmTest.PageObject.Directions.Kvots;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;


public class IncomingArchive extends BaseTest {

    /**
     * Направления на квоты / Входящие / Архивные
     */
    public IncomingArchive(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления на квоты / Входящие / Архивные
     */
    public By IncomingArchiveWait = By.xpath(
            "//a[@href='/direction/targetDiagnostic/archival?directionType=1&directionTarget=2&directionPageType=2']");

    /**
     * Записи на странице - сортировка desc
     **/
    public By DESK = By.xpath("//thead[@class='has-gutter']//th[1]//i[@class='sort-caret descending']");

    /**
     * Записи на странице - Направление - Статус
     **/
    public By StatusDirection = By.xpath("(//span[contains(.,'Номер направления')]/following-sibling::span/span)[1]");

    /**
     * Записи на странице - Направление - Текст ошибки
     **/
    public By TextErrorDirection = By.xpath("(//span[contains(.,'Номер направления')]/following-sibling::span/span)[2]");

    /**
     * Направления на квоты / Входящие / Архивные - 1 строка в таблице - МО
     */
    public By FirstLineMO = By.xpath("//tbody/tr[1]/td[2]//span");

    /**
     * Направления на квоты / Входящие / Архивные - строка с нужным id
     */
    public By SearchID (String id) {
        By str = By.xpath("(//tbody/tr//div/span[contains(.,'"+id+"')])[1]");
        return str;
    }
}
