package UI.TmTest.PageObject.Directions.Kvots;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

public class Agreement extends BaseTest {
    /**
     * Направления - Лист ожидания
     */
    public Agreement (WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления - Лист ожидания
     */
    public By AgreementWait = By.xpath("//li//a[@href='/direction/unhandled?directionType=2&directionTarget=3&directionPageType=1']");

    /**
     * Направления - Лист ожидания - Сортировка
     */
    public By SortDesc = By.xpath("(//span/i[@class='sort-caret descending'])[1]");

    /**
     * Направления - Лист ожидания - Строка с нужным id
     */
    public By SearchID (String id) {
        By str = By.xpath("(//tbody/tr//div/span[contains(.,'"+id+"')])[1]");
        return str;
    }

    /**
     * Направления - Лист ожидания - Выбираем направление с нужным id и берём значение нужного поля
     */
    public By GetDirection (String id, String column) {
        By str = By.xpath("//tbody/tr/td[1][contains(.,'"+id+"')]/following-sibling::td["+column+"]//span");
        return str;
    }

    /**
     * Направления - Лист ожидания - Всего консультаций
     */
    public By ConsultationCount = By.xpath("//span[@class='el-pagination__total']");
}