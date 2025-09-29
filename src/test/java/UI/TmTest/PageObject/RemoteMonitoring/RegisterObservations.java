package UI.TmTest.PageObject.RemoteMonitoring;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;


public class RegisterObservations extends BaseTest {

    /**
     * Дистанционный мониторинг - Регистр наблюдений
     */
    public RegisterObservations(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Регистр наблюдений
     */
    public By Register = By.xpath("//div/a[@href='/monitoring/observation-register']");

    /**
     * Регистр наблюдений - Всего записей
     */
    public By AllCount = By.xpath("//span[@class='el-pagination__total']");

    /**
     * Регистр наблюдений - Фильтры
     */
    public By Filters = By.xpath("//span/span[contains(.,'Фильтры')]");

    /**
     * Регистр наблюдений - Фильтры - Поле для ввода
     */
    public By Field (String value) {
        By field = By.xpath("//div[@class='form-item-label'][contains(.,'"+value+"')]/following-sibling::div//input");
        return field;
    }

    /**
     * Регистр наблюдений - Фильтры - Дополнительные фильтры
     */
    public By DopFilters = By.xpath("//div[@role='button'][contains(.,'Дополнительные фильтры')][@class='el-collapse-item__header']");

    /**
     * Регистр наблюдений - Фильтры - Поиск
     */
    public By Search = By.xpath("//button[@type='submit']");

    /**
     * Регистр наблюдений - Фильтры - Сброс
     */
    public By Reset = By.xpath("//button[@type='button'][contains(.,'Сбросить')]");
}
