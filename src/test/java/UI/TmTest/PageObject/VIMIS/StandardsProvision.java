package UI.TmTest.PageObject.VIMIS;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;


public class StandardsProvision extends BaseTest {

    /**
     * Вимис - Стандарты оказания МП
     */
    public StandardsProvision(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Вимис - Стандарты оказания МП
     */
    public By StandardsProv = By.xpath("//a[@href='/medicalCareStandards/view']");

    /**
     * Стандарты оказания МП - Поля поиска
     */
    public By GetField (String value) {
        By field = By.xpath("//div[@class='form-item-label'][contains(.,'"+value+"')]/following-sibling::div//input");
        return field;
    }

    /**
     * Стандарты оказания МП - Поиск
     */
    public By Search = By.xpath("//button[@type='submit']");

    /**
     * Стандарты оказания МП - Сбросить
     */
    public By Reset = By.xpath("//button[@class='el-button d-flex justify-content-center btn-sm m-0 el-button--default el-button--small']");

    /**
     * Стандарты оказания МП - Редактировать у 1 записи
     */
    public By Edit = By.xpath("//tbody/tr[1]/td//button[@tooltiptext='Редактировать']");

    /**
     * Стандарты оказания МП - Редактировать - Поля в окне редактирования
     */
    public By SetField (String value) {
        By field = By.xpath("//label[@class='el-form-item__label'][contains(.,'"+value+"')]/following-sibling::div//input");
        return field;
    }

    /**
     * Стандарты оказания МП - Редактировать - Обновить
     */
    public By Update = By.xpath("//button//span[contains(.,'Обновить')]");

    /**
     * Стандарты оказания МП  - Удалить у записи с нужной Частотой услуги
     */
    public By frequencyDelete (String value) {
        By field = By.xpath("//tbody/tr/td[5][contains(.,'"+value+"')]/following-sibling::td[2]//button[@text='Удалить данный пункт?']");
        return field;
    }

    /**
     * Стандарты оказания МП - Согласие на удаление
     */
    public By YesDelete = By.xpath(
            "//div[@role='tooltip'][@x-placement]/p[contains(.,'Удалить данный пункт?')]/following-sibling::div/button[1]");

    /**
     * Стандарты оказания МП - Добавить
     */
    public By Add (String value) {
        By field = By.xpath("(//button[contains(.,'Добавить')])["+value+"]");
        return field;
    }

    /**
     * Стандарты оказания МП - Всего записей
     */
    public By AllCount = By.xpath("//span[@class='el-pagination__total']");

    /**
     * Стандарты оказания МП - Следующая страница
     */
    public By Next = By.xpath("//button[@class='btn-next']");
}
