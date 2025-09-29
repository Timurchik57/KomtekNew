package UI.TmTest.PageObject.RegisteredReferences;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

public class References extends BaseTest {

    /**
     Заказные справки
     */
    public References (WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     Заказные справки - Список новых справок
     */
    public By ReeferenceNew = By.xpath("//div/a[@href='/vimis/patient-requests-for-doc-new']");

    /**
     Заказные справки - Список новых справок
     */
    public By ReeferenceClosed = By.xpath("//div/a[@href='/vimis/patient-requests-for-doc-close']");

    /**
     Заказные справки - Всего записей
     */
    public By AllCount = By.xpath("//span[@class='el-pagination__total']");

    /**
     Заказные справки - Нужная ячейка в нужной строке
     */
    public By line (String line, String column) {
        By field = By.xpath("//tbody/tr[" + line + "]/td[" + column + "]//span");
        return field;
    }

    /**
     Заказные справки - Нет данных
     */
    public By NoData = By.xpath("//span[contains(.,'Нет данных')]");

    /**
     Заказные справки - Фильтры
     */
    public By Filters = By.xpath("//span/span[contains(.,'Фильтры')]");

    /**
     Заказные справки - Фильтры - Поле для ввода
     */
    public By Field (String value) {
        By field = By.xpath(
                "//div[@class='form-item-label'][contains(.,'" + value + "')]/following-sibling::div//input");
        return field;
    }

    /**
     Заказные справки - Фильтры - Дополнительные фильтры
     */
    public By DopFilters = By.xpath(
            "//div[@role='button'][contains(.,'Дополнительные фильтры')][@class='el-collapse-item__header']");

    /**
     Заказные справки - Фильтры - Поиск
     */
    public By Search = By.xpath("//button[@type='button'][contains(.,'Применить')]");

    /**
     Заказные справки - Фильтры - Сброс
     */
    public By Reset = By.xpath("//button[@type='button'][contains(.,'Сбросить')]");

    /**
     Заказные справки - Редактировать документ в нужной строке
     */
    public By SetDoc (String request) {
        By field = By.xpath(
                "//tbody/tr/td[2][contains(.,'" + request + "')]/following-sibling::td[8]//button[@tooltiptext='Редактировать документ']");
        return field;
    }

    /**
     Заказные справки - Редактировать - Добавить файл
     */
    public By AddFile = By.xpath("//label[contains(.,'Выбранный документ')]/following-sibling::div/button");

    /**
     Заказные справки - Редактировать документ в нужной строке + проверка по полям в табилце
     */
    public By SetDoc (String name, String snils, String date, String type, String mo, String status) {
        By str = By.xpath(
                "//tbody/tr/td[1][contains(.,'" + name + "')]/following-sibling::td[1][contains(.,'" + snils + "')]/following-sibling::td[2][contains(.,'" + date + "')]/following-sibling::td[3][contains(.,'" + type + "')]/following-sibling::td[1][contains(.,'" + mo + "')]/following-sibling::td[1][contains(.,'" + status + "')]/following-sibling::td[1]//button[@tooltiptext='Редактировать документ']");
        return str;
    }

    /**
     Заказные справки - Редактировать документ в нужной строке + проверка по полям в табилце
     */
    public By SetDocClosed (String name, String snils, String date, String type, String mo, String status, String dateEnd) {
        By str = By.xpath(
                "//tbody/tr/td[1][contains(.,'" + name + "')]/following-sibling::td[1][contains(.,'" + snils + "')]/following-sibling::td[2][contains(.,'" + date + "')]/following-sibling::td[3][contains(.,'" + type + "')]/following-sibling::td[1][contains(.,'" + mo + "')]/following-sibling::td[1][contains(.,'" + status + "')]/following-sibling::td[1][contains(.,'" + dateEnd + "')]/following-sibling::td[1]//button[2]");
        return str;
    }

    /**
     Заказные справки - Редактировать - Статус
     */
    public By InputStatus (String value) {
        By str = By.xpath("//label[contains(.,'" + value + "')]/following-sibling::div");
        return str;
    }

    /**
     Заказные справки - Редактировать - Комментарий или сообщение
     */
    public By InputComment (String value) {
        By str = By.xpath("//label[contains(.,'" + value + "')]/following-sibling::div//textarea");
        return str;
    }

    /**
     Заказные справки - Редактировать - Ошибка
     */
    public By Error = By.xpath(
            "//div[@role='alert']/following-sibling::div//p[contains(.,'Нельзя прикрепить файл повторно')]");

    /**
     Заказные справки - Редактировать - Закрыть ошибку
     */
    public By ErrorClosed = By.xpath(
            "(//div[@role='alert']//div[@class='el-notification__closeBtn el-icon-close'])[1]");

    /**
     Лк Врача - Таблица с данными
     */
    public By MedHelpTable (String date, String mo, String status, String type, boolean button) {
        By str = null;
        if (button) {
            // Скачиваем
            str = By.xpath(
                    "//tr/td[2][contains(.,'" + date + "')]/following-sibling::td[contains(.,'" + mo + "')]/following-sibling::td[contains(.,'" + status + "')]/following-sibling::td[contains(.,'" + type + "')]/following-sibling::td//button[2]");
        } else {
            // Читаем письмо
            str = By.xpath(
                    "//tr/td[2][contains(.,'" + date + "')]/following-sibling::td[contains(.,'" + mo + "')]/following-sibling::td[contains(.,'" + status + "')]/following-sibling::td[contains(.,'" + type + "')]/following-sibling::td//button[1]");
        }
        return str;
    }

    /**
     Лк Врача - Сообщение
     */
    public By AlertSms (String value) {
        By str = By.xpath(
                "//div[@role='dialog']/div[contains(.,'Сообщение от медицинской организации')]/following-sibling::div[1][contains(.,'" + value + "')]");
        return str;
    }

    /**
     Лк Врача - Заказть медицинскую справку
     */
    public By Alert = By.xpath("//div[@x-placement][contains(.,'Ваша справка еще не готова')]");

    /**
     Лк Врача - Заказть медицинскую справку
     */
    public By AddMedHelp = By.xpath("//button[@type='button'][contains(.,'Заказать медицинскую справку')]");

    /**
     Лк Врача - Заказть медицинскую справку - Поля ввода
     */
    public By MedHelp (String value) {
        By str = By.xpath("//label[contains(.,'" + value + "')]/following-sibling::div");
        return str;
    }

    /**
     Лк Врача - Заказть медицинскую справку - Поля ввода - Текущая дата
     */
    public By DateToday = By.xpath("(//div[@x-placement]//td[@class='available today'])[1]");

    /**
     Лк Врача - Заказть медицинскую справку - Поля ввода - Перед текущей датой
     */
    public By DateBackToday = By.xpath(
            "(//div[@x-placement]//td[@class='available today'])[1]/preceding::td[1]");

    /**
     Лк Врача - Заказть медицинскую справку - Готово у предупреждения
     */
    public By Button2 = By.xpath(
            "(//button[@type='button'][contains(.,'Готово')])[2]");

    /**
     Лк Врача - Заказть медицинскую справку - Любая кнопка
     */
    public By Button (String value) {
        By str = By.xpath("//button[@type='button'][contains(.,'" + value + "')]");
        return str;
    }

    /**
     Лк Врача - Заказть медицинскую справку - Любая ошибка
     */
    public By AlertError = By.xpath("//div[@role='alert'][contains(.,'Ошибка')]");

    /**
     Лк Врача - Заказть медицинскую справку - Любая ошибка
     */
    public By AlertError (String value) {
        By str = By.xpath("//div[@role='alert'][contains(.,'Ошибка')]//p[contains(.,'" + value + "')]");
        return str;
    }

}
