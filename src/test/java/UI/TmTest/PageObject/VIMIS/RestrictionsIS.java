package UI.TmTest.PageObject.VIMIS;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;


public class RestrictionsIS extends BaseTest {

    /**
     * Вимис - Список ограничений на назначение ИС
     */
    public RestrictionsIS(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Вимис - Список ограничений на назначение ИС
     */
    public By RestrictionIS = By.xpath("//a[@href='/serviceRestrictions/view']");

    /**
     * Список ограничений на назначение ИС - Поля поиска
     */
    public By GetField (String value) {
        By field = By.xpath("//div[@class='form-item-label'][contains(.,'"+value+"')]/following-sibling::div//input");
        return field;
    }

    /**
     * Список ограничений на назначение ИС - Поиск
     */
    public By Search = By.xpath("//button[@type='submit']");

    /**
     * Список ограничений на назначение ИС - Сбросить
     */
    public By Reset = By.xpath("//button[@class='el-button d-flex justify-content-center btn-sm m-0 el-button--default el-button--small']");

    /**
     * Список ограничений на назначение ИС - Редактировать у 1 записи
     */
    public By Edit = By.xpath("//tbody/tr[1]/td//button[@tooltiptext='Редактировать']");

    /**
     * Список ограничений на назначение ИС - Редактировать у последней записи
     */
    public By EditLast = By.xpath("//tbody/tr[last()]/td//button[@tooltiptext='Редактировать']");

    /**
     * Список ограничений на назначение ИС - Редактировать - Поля в окне редактирования
     */
    public By SetField (String value) {
        By field = By.xpath("//label[@class='el-form-item__label'][contains(.,'"+value+"')]/following-sibling::div//input");
        return field;
    }

    /**
     * Список ограничений на назначение ИС - Редактировать - Поля в окне редактирования
     */
    public By MO = By.xpath("//label[@class='el-form-item__label'][contains(.,'Медицинская организация')]/");

    /**
     * Список ограничений на назначение ИС - Редактировать - Обновить
     */
    public By Update = By.xpath("//button//span[contains(.,'Обновить')]");

    /**
     * Список ограничений на назначение ИС  - Удалить у определённой записи оп списку
     */
    public By frequencyDelete (String value) {
        By field = By.xpath("//tbody/tr[contains(.,'"+value+"')]//button[@text='Удалить данный пункт?']");
        return field;
    }

    /**
     * Список ограничений на назначение ИС - Удалить последнюю запись
     */
    public By DeleteLast = By.xpath("//tbody/tr[last()]//button[@text='Удалить данный пункт?']");

    /**
     * Список ограничений на назначение ИС - Согласие на удаление
     */
    public By YesDelete = By.xpath(
            "//div[@role='tooltip'][@x-placement]/p[contains(.,'Удалить данный пункт?')]/following-sibling::div/button[1]");

    /**
     * Список ограничений на назначение ИС - Добавить
     */
    public By Add (String value) {
        By field = By.xpath("(//button[contains(.,'Добавить')])["+value+"]");
        return field;
    }

    /**
     * Список ограничений на назначение ИС - Добавить - Ошибка об обязательности определённого поля
     */
    public By AddFiled (String value) {
        By field = By.xpath("//label[@class='el-form-item__label'][contains(.,'"+value+"')]/following-sibling::div//div[contains(.,'Поле обязательно для заполнения')]");
        return field;
    }

    /**
     * Список ограничений на назначение ИС - Ошибка сочетания параметров
     */
    public By Error = By.xpath("//div[@role='alert'][contains(.,'Сочетание полей targetMedicalOid, diagnosisCode, labTestId и targetMedicalOid, anatomicalAreaId, documentTypesOids должно быть уникальным')]");

    /**
     * Список ограничений на назначение ИС - Всего записей
     */
    public By AllCount = By.xpath("//span[@class='el-pagination__total']");

    /**
     * Список ограничений на назначение ИС - Следующая страница
     */
    public By Next = By.xpath("//button[@class='btn-next']");

    /**
     * Список ограничений на назначение ИС - Следующая страница не активна
     */
    public By NextDisable = By.xpath("//button[@class='btn-next'][@disabled='disabled']");
}
