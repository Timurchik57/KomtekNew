package UI.TmTest.PageObject.Directions.Consultation;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class ConsultationUnfinished extends BaseTest {
    /**
     * Направления - Консультации - Входящие - Незавершённые
     **/
    public ConsultationUnfinished(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Направления - Консультации - Входящие - Незавершённые
     **/
    @FindBy(xpath = "//a[@href='/direction/incomingConsultation/uncompleted?directionType=2&directionTarget=2&directionPageType=1']")
    public WebElement Unfinished;
    public By UnfinishedWait = By.xpath(
            "//a[@href='/direction/incomingConsultation/uncompleted?directionType=2&directionTarget=2&directionPageType=1']");

    /**
     * Входящие - Незавершённые - Подписать консультации
     **/
    public By SignedTMK = By.xpath("//button[contains(.,'Подписать консультации')]");

    /**
     * Заголовок
     **/
    public By Header = By.xpath("//header/h1[contains(.,'Консультации / Входящие / Незавершенные')]");

    /**
     * Разворачиваем селекты
     */
    public By SelectAllClick = By.xpath("//button[@class='el-button el-tooltip m-0 el-button--default el-button--small']");

    /**
     * Записи на странице - сортировка desc
     **/
    public By DESK = By.xpath("//thead[@class='has-gutter']//th[1]//i[@class='sort-caret descending']");

    /**
     * Количество записей на странице
     **/
    public By Quantity = By.xpath("//tbody/tr");

    /**
     * 1 запись с "Отправлено"
     **/
    @FindBy(xpath = "(//tbody/tr/td/div/span[contains(.,'Отправлен')])[1]")
    public WebElement First;
    public By FirstWait = By.xpath(
            "(//tbody/tr/td/div/span[contains(.,'Отправлен')][1]/preceding::td[1]/following-sibling::td[2]/div[contains(.,'плановая')])[1]");

    /**
     * 1 запись с выбранным статусом или номером консультации
     **/
    public By Consuls(String value) {
        By Consul = By.xpath("(//tbody/tr/td/div/span[contains(.,'"+value+"')])[1]");
        return Consul;
    }

    /**
     * 1 запись с выбранным статусом или номером консультации с чекбоксом
     **/
    public By ConsulsCheckBox(String value) {
        By Consul = By.xpath("//tbody/tr/td[contains(.,'"+value+"')]/preceding-sibling::td//span[@class='el-checkbox__input']");
        return Consul;
    }

    /**
     * Входящие - Незавершённые - нужный столбец по нужной консультации
     **/
    public By ConsulsColumn(String value, String number) {
        By Consul = By.xpath("//tbody/tr/td[contains(.,'"+value+"')]/following-sibling::td["+number+"]");
        return Consul;
    }

    /**
     * Входящие - Незавершённые - Подписать консультации - Отправить
     **/
    public By AddTmk = By.xpath("//button[contains(.,'Отправить')]");

    /**
     * Записи на странице - 1 запись - Фамилия
     **/
    public By LastName = By.xpath("(//tbody/tr/td/div/span[contains(.,'Отправлен')])[1]/preceding::td[2]");

    public Integer Number;

    /**
     * Проверка колонки - Форма консультации у первой записи с "Отправлено"
     **/
    @FindBy(xpath = "//tbody/tr/td/div/span[contains(.,'Отправлен')]/ancestor::td/following-sibling::td//span")
    public WebElement Form;
    public By FormWait = By.xpath(
            "//tbody/tr/td/div/span[contains(.,'Отправлен')]/ancestor::td/following-sibling::td//span");

    /**
     * Удаленное консультирование - Заголовок
     **/
    public By HeaderTwo = By.xpath("//header/h1[contains(.,'Удаленное консультирование')]");

    /**
     * Консультация - Телефон
     **/
    public By Phone = By.xpath("//td[contains(.,'Телефон')]/following-sibling::td");

    /**
     * Консультация - id записи
     **/
    public By NumberConsultation = By.xpath("//span[contains(.,'Номер консультации')]");

    /**
     * Консультация - Статус
     **/
    public By StatusConsultation = By.xpath("(//span[contains(.,'Номер консультации')]/following-sibling::span/span)[1]");

    /**
     * Консультация - Текст ошибки из vimis.remd_sent_result
     **/
    public By TextErrorConsultation = By.xpath("(//span[contains(.,'Номер консультации')]/following-sibling::span/span)[2]");

    /**
     * Консультация - ФИО врача консультанта - Добавить
     **/
    public By DoctorAdd = By.xpath("//td[contains(.,'ФИО врача консультанта:')]/following-sibling::td//span[contains(.,'Добавить')]");

    /**
     * Консультация - ФИО врача консультанта - Изменить
     **/
    public By DoctorSet = By.xpath("//td[contains(.,'ФИО врача консультанта:')]/following-sibling::td//span[contains(.,'Изменить')]");

    /**
     * Консультация - ФИО врача консультанта - Выберите врача консультанта
     **/
    public By DoctorSelect = By.xpath("//input[@placeholder='Выберите врача консультанта']");

    /**
     * Консультация - Отправить протокол
     **/
    public By AddConsultation = By.xpath("(//button/span[contains(.,'Отправить протокол')])[1]");

    /**
     * Консультация - Переотправить протокол
     **/
    public By PereAddConsultation = By.xpath("(//button/span[contains(.,'Переотправить протокол')])[1]");

    /**
     * Консультация - Текст заключения
     **/
    public By TextConclusion = By.xpath("//div/h3[contains(.,'Заключение')]/parent::div/following-sibling::div/div");

    /**
     * Консультация - Дата отправки консультации
     */
    public By DateConsultation = By.xpath("//td[contains(.,'Дата отправки консультации:')]/following-sibling::td");

    /**
     * Консультация - Назначенное время консультации:
     **/
    public By TimeConsultation = By.xpath("//td[contains(.,'Назначенное время консультации')]/following-sibling::td");

    /**
     * Консультация - Завершить консультацию
     **/
    public By Closed = By.xpath("(//button/span[contains(.,'Завершить консультацию')])[1]");

    /**
     *  Консультация - Протокол консультации - Название сформированного протокола
     */
    public By Protocols = By.xpath("(//h3[contains(.,'Протоколы консультации')])[1]/ancestor::div[1]/following-sibling::div[1]//tr/td[1]");

    /**
     * Завершить консультацию - Текст
     **/
    public By ClosedText = By.xpath("//textarea");

    /**
     * Завершить консультацию - Время
     **/
    public By ClosedTime = By.xpath("//input[@placeholder='чч:мм']");

    /**
     * Завершить консультацию - Дата - Размер текста
     **/
    @FindBy(xpath = "//label[@for='date']")
    public WebElement ClosedTimeSize;

    /**
     * Завершить консультацию - Доктор - Размер текста
     **/
    @FindBy(xpath = "//label[@for='executorId']")
    public WebElement DoctorsSize;

    /**
     * Завершить консультацию - Медицинская эвакуация - Размер текста
     **/
    @FindBy(xpath = "//label[contains(.,'Медицинская эвакуация')]")
    public WebElement MESize;

    /**
     * Завершить консультацию - Доктор - Размер текста
     **/
    @FindBy(xpath = "//label[contains(.,'Заключение')]")
    public WebElement ConclusionSize;

    /**
     * Завершить консультацию - Закрыть
     **/
    public By Exit = By.xpath("//footer/button/span[contains(.,'Закрыть')]");

    /**
     * Завершить консультацию - Завершить консультацию
     **/
    public By Closed2 = By.xpath("(//button/span[contains(.,'Завершить консультацию')])[2]");

    /**
     * Отправить протокол - Фамилия
     **/
    public By LastNameProtocol = By.xpath("//input[@placeholder='Фамилия']");

    /**
     * Отправить протокол - Имя
     **/
    public By NameProtocol = By.xpath("//input[@placeholder='Имя']");

    /**
     * Отправить протокол - Отчество
     **/
    public By MiddleNameProtocol = By.xpath("//input[@placeholder='Отчество']");

    /**
     * Отправить протокол - Снилс
     **/
    public By SnilsProtocol = By.xpath("//input[@placeholder='СНИЛС']");

    /**
     * Отправить протокол - Подразделение
     **/
    public By DivisionProtocol = By.xpath("//input[@placeholder='Поздразделение']");
    public By SelectDivisionProtocol = By.xpath("//div[@x-placement]//span[contains(.,'Женская консультация')]");
    public By SelectDivisionProtocoBuh = By.xpath("//div[@x-placement]//span[contains(.,'Бухгалтерия')]");

    /**
     * Отправить протокол - Роль
     **/
    public By Role = By.xpath("//input[@placeholder='Роль подписанта']");
    public By FirstSelect = By.xpath("//div[@x-placement]//li[1]/span");
    public By SecondSelect = By.xpath("//div[@x-placement]//li[2]/span");
    public By Selected = By.xpath("//div[@x-placement]//li[@class='el-select-dropdown__item selected']/span");
    public By RoleSelect (String value) {
        By Cert = By.xpath("//div[@x-placement]//li/span[contains(.,'"+value+"')]");
        return Cert;
    }

    /**
     * Отправить протокол - Должность
     **/
    public By Post = By.xpath("//input[@placeholder='Должность']");
    public By PostFirst = By.xpath("//div[@x-placement]//li[1]/span");

    /**
     * Отправить протокол - Специальность
     **/
    public By Specialization = By.xpath("//input[@placeholder='Специальность']");
    public By SpecializationFirst = By.xpath("//div[@x-placement]//li[1]/span");

    /**
     * Отправить протокол - Сертификат медицинского работника
     **/
    public By CertDoctor = By.xpath("//input[@placeholder='Сертификат медицинского работника']");
    public By CertDoctorXMAO = By.xpath(
            "//div[@x-placement]//span[contains(.,'\"БЮДЖЕТНОЕ УЧРЕЖДЕНИЕ ХАНТЫ-МАНСИЙСКОГО АВТОНОМНОГО ОКРУГА - ЮГРЫ \"\"МЕДИЦИНСКИЙ ИНФОРМАЦИОННО-АНАЛИТИЧЕСКИЙ ЦЕНТР\"\"\"')]");
    public By CertDoctorTim = By.xpath("//div[@x-placement]//span[contains(.,'тим')]");
    public By Cert (String value) {
        By Cert = By.xpath("//div[@x-placement]//span[contains(.,'"+value+"')]");
        return Cert;
    }

    /**
     * Отправить протокол - Сертификат медицинской организации
     **/
    public By CertMO = By.xpath("//input[@placeholder='Сертификат медицинской организации']");

    /**
     * Отправить протокол - Тип консультации
     **/
    public By TypeConsul = By.xpath("//input[@placeholder='Тип консультации (Введите не менее 3 символов для поиска)']");

    /**
     * Отправить протокол - Чек бокс Текущий пользователь
     **/
    public By CheckBox = By.xpath("//span[contains(.,'Текущий пользователь')]/preceding::span[1]");

    /**
     * Отправить протокол - Чек бокс Текущий пользователь - уже стоит галочка
     **/
    public By CheckBoxCheck = By.xpath("//span[contains(.,'Текущий пользователь')]/preceding::span[@class='el-checkbox__input is-checked']");

    /**
     * Отправить протокол - Отправить протокол - Загрузка
     **/
    public By AddConsultation2Loading = By.xpath("(//button[@class='el-button el-button--primary el-button--medium is-loading']/span[contains(.,'Отправить протокол')])[2]");

    /**
     * Отправить протокол - Отправить протокол
     **/
    public By AddConsultation2 (String value) {
        By Cert = By.xpath("(//button/span[contains(.,'Отправить протокол')])["+value+"]");
        return Cert;
    }

    /**
     * Отправить протокол - Переотправить протокол
     **/
    public By PereAddConsultation2 = By.xpath("(//button/span[contains(.,'Переотправить протокол')])[2]");

    /**
     * Отправить протокол - Переотправить протокол - Загрузка
     **/
    public By PereAddConsultation2Loading = By.xpath("(//button[@class='el-button el-button--primary el-button--medium is-loading']/span[contains(.,'Переотправить протокол')])[2]");

    /**
     * Запросить доп. информацию
     **/
    @FindBy(xpath = "//button/span[contains(.,' Запросить доп. информацию')]")
    public WebElement Information;
    public By InformationWait = By.xpath("//button/span[contains(.,' Запросить доп. информацию')]");

    /**
     * Консультация с "Отправлено" - Требуется госпитализация
     **/
    @FindBy(xpath = "//button/span[contains(.,'Требуется госпитализация')]")
    public WebElement Required;
    public By RequiredWait = By.xpath("//button/span[contains(.,'Требуется госпитализация')]");

    /**
     * Консультация с "Отправлено" - Требуется госпитализация - Подтвердить
     **/
    @FindBy(xpath = "//div[@x-placement='top-end']//button/span[contains(.,'Подтвердить')]")
    public WebElement Yes;
    public By YesWait = By.xpath("//div[@x-placement='top-end']//button/span[contains(.,'Подтвердить')]");

    /**
     * Запросить дополнительную информацию - Заголовок
     **/
    public By HeaderInfo = By.xpath("//span[contains(.,'Запросить дополнительную информацию')]");

    /**
     * Запросить дополнительную информацию - поле ввода
     **/
    @FindBy(xpath = "//*[@class='el-textarea__inner']")
    public WebElement Input;
    public By InputWait = By.xpath("//*[@class='el-textarea__inner']");
    public String word = "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";

    /**
     * Запросить дополнительную информацию - Уведомление об ошибке
     **/
    public By Error = By.xpath("//div[@class='el-form-item__error']");

    /**
     * Консультация - Назначить время
     **/
    public By Time = By.xpath("//button/span[contains(.,'Назначить время')]");

    /**
     * Консультация - Назначить время - Добавить время консультации
     **/
    public By AddTime = By.xpath("//button/span[contains(.,'Добавить время консультации')]");

    /**
     * Назначить время - Добавить время консультации - Врач
     **/
    public By Doctor = By.xpath("//input[@placeholder= 'Не выбрано']");

    /**
     * Назначить время - Добавить время консультации - Врач консультант (для размера шрифта)
     **/
    @FindBy(xpath = "//label[@for='userId']")
    public WebElement DoctorSize;

    /**
     * Назначить время - Добавить время консультации - Дата
     **/
    public By DataDay = By.xpath("//input[@placeholder= 'дд.мм.гггг']");

    /**
     * Назначить время - Добавить время консультации - Дата - 1 число следующего месяца
     **/
    public By NextMonth = By.xpath("//td[@class='next-month']/div/span[text()=1]");

    /**
     * Назначить время - Добавить время консультации - Дата
     **/
    public By Data = By.xpath("//input[@placeholder= 'чч:мм']");

    /**
     * Назначить время - Добавить время консультации - Длительность
     **/
    public By NumberTime = By.xpath("//input[@type= 'number']");

    /**
     * Назначить время - Добавить время консультации - Добавить консультацию
     **/
    public By AddConsul = By.xpath("//button/span[contains(.,'Добавить консультацию')]");

    /**
     * Назначить время - Добавить время консультации - Отмена
     **/
    public By Close = By.xpath("//button/span[contains(.,'Отмена')]");

    /**
     * Назначить время - Дата
     **/
    public By DataInTime = By.xpath("//button[@class='datepicker__btn-view']");

    /**
     * Назначить время - Дата - 1 число следующего месяца
     **/
    public By DataInTimeNextMonth = By.xpath("(//div/div[@class='vuecal__cell-date'][text()=1])[2]");

    /**
     * Назначить время - Показывать весь день
     **/
    public By WatchDay = By.xpath("//button[contains(.,'Показывать весь день')]");

    /**
     * Назначить время - Созданная консультация
     **/
    public By TrueConsul (String str) {
        By TrueConsul = By.xpath("(//div/span[contains(.,'"+str+"')])[1]");
        return TrueConsul;
    }
}
