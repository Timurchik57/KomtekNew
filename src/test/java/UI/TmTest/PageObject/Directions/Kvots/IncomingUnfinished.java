package UI.TmTest.PageObject.Directions.Kvots;

import Base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;

import java.sql.SQLException;
import java.util.Arrays;

public class IncomingUnfinished extends BaseTest {
    /**
     * Направления на квоты / Входящие / Незавершенные
     */
    public IncomingUnfinished(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления на квоты / Входящие / Незавершенные
     */
    public By ConsultationWait = By.xpath("//a[@href='/direction/targetDiagnostic/uncompleted?directionType=1&directionTarget=2&directionPageType=1']");

    /**
     * Направления на квоты / Входящие / Незавершенные - сортировка desc
     **/
    public By DESK = By.xpath("//thead[@class='has-gutter']//th[1]//i[@class='sort-caret descending']");

    /**
     * Направления на квоты / Входящие / Незавершенные - 1 строка в таблице - МО
     */
    public By FirstLineMO = By.xpath("//tbody/tr[1]/td[2]//span");

    /**
     * Направления на квоты / Входящие / Незавершенные - строка с нужной МО и статусом
     */
    public By SearchMO (String mo, String status) {
         By MO = By.xpath("(//tbody/tr//div/span[contains(.,'"+status+"')]/ancestor::tr//span[contains(.,'"+mo+"')])[1]");
         return MO;
    }

    /**
     * Направления на квоты / Входящие / Незавершенные - строка с нужным id
     */
    public By SearchID (String id) {
        By str = By.xpath("(//tbody/tr//div/span[contains(.,'"+id+"')])[1]");
        return str;
    }

    /**
     * Направления на квоты / Входящие / Незавершенные - 1 строка в таблице - Дата
     */
    public By FirstLineDate = By.xpath("//tbody/tr[1]/td[4]//span");

    /**
     * Направления на квоты / Входящие / Незавершенные - 1 строка в таблице - Пациент
     */
    public By FirstLinePatient = By.xpath("//tbody/tr[1]/td[7]//span");

    /**
     * Направления на квоты / Входящие / Незавершенные - 1 строка в таблице - Снилс
     */
    public By FirstLineSnils = By.xpath("//tbody/tr[1]/td[8]//span");

    /**
     * Направления на квоты / Входящие / Незавершенные - 1 строка в таблице - Статус
     */
    public By FirstLineStatus = By.xpath("(//tbody/tr[1]/td[9]//span)[3]");

    /**
     * Направления на квоты / Входящие / Незавершенные - Тип даты
     */
    public By TypeDate = By.xpath("//input[@placeholder='Тип даты']");

    /**
     * Направления на квоты / Входящие / Незавершенные - Дата направления
     */
    public By SelectDate = By.xpath("//ul/li/span[contains(.,'Дата направления')]");

    /**
     * Направления на квоты / Входящие / Незавершенные - Дата направления
     */
    public By FirstDate = By.xpath("//input[@placeholder='C какого числа']");

    /**
     * Направления на квоты / Входящие / Незавершенные - Текущая дата
     */
    public By ToDay = By.xpath("//td[@class='available today']");

    /**
     * Направления на квоты / Входящие / Незавершенные - Текущая дата
     */
    public By ToDay1 = By.xpath("//td[@class='available today in-range start-date end-date']");

    /**
     * Направления на квоты / Входящие / Незавершенные - Текущая дата
     */
    public By ToDay2 = By.xpath("//td[@class='available today in-range start-date']");

    /**
     * Направления на квоты / Входящие / Незавершенные - Снилс
     */
    public By Snils = By.xpath("//input[@placeholder='Введите СНИЛС']");

    /**
     * Направления на квоты / Входящие / Незавершенные - Поиск
     */
    public By Search = By.xpath("//span[contains(.,'Поиск')]");

    /**
     * Направления на квоты / Входящие / Незавершенные - Нет данных
     */
    public By NotBad = By.xpath("//div[@class='el-table__empty-block']/span[contains(.,'Нет данных')]");

    /**
     * Направление - id записи
     **/
    public By NumberConsultation = By.xpath("//span[contains(.,'Номер направления')]");

    /**
     * Направление - Нужное значение
     **/
    public By Meaning (String value, String text) {
        By str = By.xpath("//tbody/tr/td[contains(.,'"+value+"')]/following-sibling::td[contains(.,'"+text+"')]");
        return str;
    }

    /**
     * Направление - Нужная кнопка
     */
    public By Button (String value) {
        By str = By.xpath("(//button/span[contains(.,'"+value+"')])[1]");
        return str;
    }

    /**
     * Направление - Отменить направление - Причина отмены
     */
    public By CloseText = By.xpath("//textarea");

    /**
     *  Направление - Отменить направление - Отклонить направление
     */
    public By CloseOutDirection2 = By.xpath("(//button/span[contains(.,'Отклонить направление')])[2]");

    /**
     *  Направление - Протокол исследования - Название сформированного протокола
     */
    public By Protocols = By.xpath("(//h3[contains(.,'Протоколы исследования')])[1]/ancestor::div[1]/following-sibling::div[1]//tr/td[1]");

    /**
     * Направление - Выполнить исследование - Поля ввода
     */
    public By Text (String value) {
        By str =  By.xpath("//label[contains(.,'"+value+"')]/following-sibling::div//textarea");
        return str;
    }

    /**
     * Направление - Выполнить исследование - Фильтры шаблонов
     */
    public By Filter = By.xpath("//div[@class='el-dropdown']");

    /**
     * Направление - Выполнить исследование - Фильтры шаблонов - Выбор
     */
    public By FilterSet (String value) {
        By str =  By.xpath("//ul[@x-placement]/li[contains(.,'"+value+"')]");
        return str;
    }

    /**
     * Направление - Выполнить исследование - Все шаблоны
     */
    public By PatternAll = By.xpath("//div[@class='diagnostic-protocol-patterns']/div[@class='diagnostic-protocol-pattern']");

    /**
     * Направление - Выполнить исследование - Определённый шаблон
     */
    public By PatternOne (String value) {
        By str =  By.xpath("//div[@class='diagnostic-protocol-patterns']/div[@class='diagnostic-protocol-pattern']["+value+"]");
        return str;
    }

    /**
     * Направление - Выполнить исследование - Определённый шаблон - По названию
     */
    public By PatternName (String value) {
        By str =  By.xpath("//div[@class='diagnostic-protocol-patterns']/div[@class='diagnostic-protocol-pattern'][contains(.,'"+value+"')]");
        return str;
    }

    /**
     * Направление - Выполнить исследование - Определённый шаблон - Берём Название
     */
    public By PatternOneName (String value) {
        By str =  By.xpath("//div[@class='diagnostic-protocol-patterns']/div[@class='diagnostic-protocol-pattern']["+value+"]//div[@class='diagnosti-protocol-pattern-name']");
        return str;
    }

    /**
     * Направление - Выполнить исследование - Поиск шаблонов
     */
    public By SearchPattern = By.xpath("//input[@placeholder='Поиск по шаблонам']");

    /**
     * Направление - Выполнить исследование - Новый шаблон
     */
    public By NewPattern = By.xpath("//span[contains(.,'Новый шаблон')]");

    /**
     * Направление - Выполнить исследование - Новый шаблон - Наименование шаблона
     */
    public By NewPatternString =  By.xpath("//label[contains(.,'Наименование шаблона')]/following-sibling::div//input");

    /**
     * Направление - Выполнить исследование - Новый шаблон - Исследование
     */
    public By NewPatternStudy =  By.xpath("//label[contains(.,'Исследование')]/following-sibling::div//input");

    /**
     * Направление - Выполнить исследование - Новый шаблон - Поля ввода
     */
    public By NewPatternString (String value) {
        By str =  By.xpath("(//label[contains(.,'"+value+"')]/following-sibling::div//textarea)[2]");
        return str;
    }

    /**
     * Направление - Выполнить исследование - Новый шаблон - Сохранить
     */
    public By SavePattern =  By.xpath("//div[@class='diagnostic-pattern-actions']//button/span[contains(.,'Сохранить')]");

    /**
     * Направление - Выполнить исследование - Шаблон - Редактировать/Избранное/Удалить
     */
    public By EditPattern (String value) {
        By str =  By.xpath("//div[@title='"+value+"']");
        return str;
    }

    /**
     * Направление - Выполнить исследование - Шаблон - Импортировать
     */
    public By Import =  By.xpath("//div/span[contains(.,'Импортировать в протокол')]");

    /**
     * Выполнить исследование - Выполнить исследование - Сохранить и подписать
     **/
    public By Save = By.xpath("//button/span[contains(.,'Сохранить и подписать')]");

    /**
     * Выполнить исследование - Выполнить исследование - Сохранить без подписания
     **/
    public By SaveNot = By.xpath("//button/span[contains(.,'Сохранить без подписания')]");

    /**
     * Выполнить исследование - Загрузка
     **/
    public By Loading = By.xpath("(//div[@class='el-loading-spinner'])[5]");

    @Step("Метод для проверки отображения нужных кнопок (Сразу несколько) и проверка, что не отображаются другие")
    public void WaitButton (String[] value) throws SQLException {
        for (String str : value) {
            WaitElement(Button(str));
        }

        String[] mass = {"Отменить направление", "Отправить на интерпретацию", "Отклонить направление", "Согласовать", "Выполнить исследование"};

        // Фильтруем массив, убираем те, которые передали в методе и проверяем, что оставшихся нет, они не отображаются
        String[] filteredArray = Arrays.stream(mass)
                .filter(button -> !Arrays.asList(value).contains(button))
                .toArray(String[]::new);

        for (String str : filteredArray) {
            WaitNotElement(Button(str));
        }
    }
}
