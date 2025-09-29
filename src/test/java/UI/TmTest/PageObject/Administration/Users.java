package UI.TmTest.PageObject.Administration;

import Base.BaseTest;
import Base.SQL;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.sql.SQLException;

public class Users extends BaseTest {
    public Users (WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     Администрирование - Пользователи
     */
    @FindBy(xpath = "//a[@href='/permission/users']")
    public WebElement Users;
    public By UsersWait = By.xpath("//a[@href='/permission/users']");

    /**
     Заголовок - Пользователи
     */
    public By HeaderUsersWait = By.xpath("//h1[.='Пользователи']");

    /**
     Пользователи - Снилс пользователя
     */
    @FindBy(xpath = "//input[@placeholder='Введите СНИЛС']")
    public WebElement SnilsUsers;
    public By SnilsUsersWait = By.xpath("//input[@placeholder='Введите СНИЛС']");

    /**
     Пользователи - Выбор профиля
     */
    @FindBy(xpath = "//input[@placeholder='Введите профиль']")
    public WebElement ProfileСhoice;
    public By ProfileWait = By.xpath("//input[@placeholder='Введите профиль']");
    public By SelectProfile = By.xpath("//li/span[text()='косметологии']");
    public By SelectProfile1 = By.xpath("//li/span[text()='медицинской оптике']");

    /**
     Изменение данных о месте работы
     */
    public By EmailSearch (String value) {
        By Search = By.xpath("//input[@placeholder='" + value + "']");
        return Search;
    }

    /**
     Пользователи - Мед.организация
     */
    @FindBy(xpath = "//input[@placeholder='Введите название мед. организации']")
    public WebElement MedOrgСhoice;
    public By MedOrgWait = By.xpath("//input[@placeholder='Введите название мед. организации']");
    public By SelectMedOrg = By.xpath(
            "//li[contains(.,'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"')]");

    /**
     Пользователи - В таблице нет данных
     */
    public By NotDataWait = By.xpath("//div[@class='el-table__empty-block']/span[contains(.,'Нет данных')]");

    /**
     Значение для выбора нужного профиля
     */
    public String Condition;

    /**
     Пользователи - Дата
     */
    @FindBy(xpath = "//input[@placeholder='Введите дату регистрации']")
    public WebElement Data;

    /**
     Дата - 2022 год
     */
    public By Years2022 = By.xpath("//div[@class='el-picker-panel__body']//span[contains(.,'2022')]");

    /**
     Дата - Май
     */
    public By MonthMay = By.xpath("//div[@class='el-picker-panel__body']//span[contains(.,'Май')]");

    /**
     Дата - Сентябрь
     */
    public By MonthSeptember = By.xpath("//div[@class='el-picker-panel__body']//span[contains(.,'Сентябрь')]");

    /**
     Дата - Предыдущий месяц
     */
    @FindBy(xpath = "//div[@class='el-picker-panel__body']//button[@aria-label='Предыдущий месяц']")
    public WebElement BeforeMouth;

    /**
     Дата - 19 число
     */
    @FindBy(xpath = "//div[@class='el-picker-panel__body']//table//span[contains(.,' 19')]")
    public WebElement Day19;
    public By Day19Wait = By.xpath("//div[@class='el-picker-panel__body']//table//span[contains(.,' 19')]");

    /**
     Дата - 12 число
     */
    @FindBy(xpath = "//div[@class='el-picker-panel__body']//table//span[contains(.,' 12')]")
    public WebElement Day12;
    public By Day12Wait = By.xpath("//div[@class='el-picker-panel__body']//table//span[contains(.,' 12')]");

    /**
     Поиск
     */
    @FindBy(xpath = "//span[contains(.,' Поиск')]")
    public WebElement Search;
    public By SearchWait = By.xpath("//span[contains(.,' Поиск')]");

    /**
     Снилс найденного пользователя
     */
    @FindBy(xpath = "(//table//tbody/tr[@class='el-table__row']/td)[3]//span")
    public WebElement Snils;
    public By SnilsWait = By.xpath("(//table//tbody/tr[@class='el-table__row']/td)[3]//span");

    /**
     Поиск - 1 пользователь
     */
    public By FirstUser = By.xpath("(//table[@class='el-table__body']/tbody/tr)[1]");

    /**
     Добавить пользователя
     */
    @FindBy(xpath = "//header/button")
    public WebElement AddUser;
    public By AddUserWait = By.xpath("//header/button");

    /**
     Редактирование пользователя 1
     */
    public By EditUserWaitOne = By.xpath(
            "(//tbody/tr[1]//button[@tooltiptext='Редактировать'])[1]");

    /**
     Редактирование пользователя 2
     */
    @FindBy(xpath = "(//tbody//button[@tooltiptext='Редактировать'])[2]")
    public WebElement EditUser;
    public By EditUserWait = By.xpath(
            "//div[@class='el-table__body-wrapper is-scrolling-none']//tbody/tr[2]/td[last()]/div/span/button");

    /**
     Ввод значения
     */
    @FindBy(css = ".el-input--medium > .el-input__inner")
    public WebElement InputSnils;
    public By InputSnilsWait = By.cssSelector(".el-input--medium > .el-input__inner");

    /**
     Ввод значения - Загрузка после ввода снилс
     */
    public By LoadingSnils = By.xpath(
            "//section[@class='user-permission el-loading-parent--relative']//div[@class='el-loading-spinner']");

    /**
     Найти
     */
    @FindBy(xpath = "//button[contains(.,'Найти')]")
    public WebElement ButtonSearch;
    public By ButtonSearchWait = By.xpath("//button[contains(.,'Найти')]");

    /**
     СНИЛС
     */
    public By SnilsInputWait = By.xpath("//label[contains(.,'СНИЛС')]");

    /**
     Уведомление - Пользователь найден
     */
    public By Alert = By.xpath("//div[@role='alert']");

    /**
     Уведомление - Пользователь найден - Закрыть
     */
    @FindBy(xpath = "(//div[@class='el-notification__closeBtn el-icon-close'])[1]")
    public WebElement AlertClose;

    /**
     Заголовок - "Пользователь не найден в ИЭМК"
     */
    public By NotIEMK = By.xpath("//div/p[contains(.,'Добавьте недостающую информацию')]");

    /**
     Заголовок - "Ошибка поиска медицинского работника"
     */
    public By NotIEMK1 = By.xpath("//div/p[contains(.,'Добавьте недостающую информацию')]");

    /**
     Пользователь - Фамилия
     */
    public By LastName = By.xpath("//input[@placeholder = 'Фамилия']");

    /**
     Пользователь - Имя
     */
    public By Name = By.xpath("//input[@placeholder = 'Имя']");

    /**
     Пользователь - Отчество
     */
    public By MiddleName = By.xpath("//input[@placeholder = 'Отчество (необязательно)']");

    /**
     Пользователь - Дата
     */
    public By Date = By.xpath("//input[@placeholder = 'Введите дату']");

    /**
     Пользователь - Дата - Дата в третьей строке
     */
    public By DateYToday = By.xpath("(//div[@x-placement]//tbody)[1]/tr[3]/td[4]");

    /**
     Пользователь - Номер
     */
    public By Number = By.xpath("//input[@placeholder = '+7 (___) ___-___-__']");

    /**
     Пользователь - Логин
     */
    public By Login = By.xpath("//input[@placeholder = 'Логин']");

    /**
     Пользователь - Пароль
     */
    public By Password = By.xpath("//input[@placeholder = 'Пароль']");

    /**
     Email
     */
    @FindBy(xpath = "//input[@placeholder='example@mail.ru']")
    public WebElement Email;
    public By EmailWait = By.xpath("//input[@placeholder='example@mail.ru']");

    /**
     Ошибка - Данный email-адрес уже существует в системе
     */
    public By Fail = By.xpath(
            "//div[@class='el-form-item__error'][contains(.,'Данный email-адрес уже существует в системе')]");

    /**
     Пользователь - Сверять данные с ФРМР - Нажатый
     */
    public By FRMR = By.xpath(
            "//label[contains(.,'Сверять данные с ФРМР')]/following-sibling::div//span[@class='el-checkbox__input is-checked']");

    /**
     Заголовок - "Изменение данных пользователя"
     */
    public By NotHeaderIEMK = By.xpath("//h3[contains(.,'Изменение данных пользователя')]");

    /**
     Заголовок - Изменение данных о месте работы
     */
    public By EditWait = By.xpath("//h3[contains(.,'Изменение данных о месте работы')]");

    /**
     Место работы
     */
    public By PlaceWaork = By.xpath(
            "//tbody/tr[contains(.,'БУ ХМАО-Югры \"Нижневартовская городская детская поликлиника\"')]");

    /**
     Определённое Место работы - Изменить
     */
    @FindBy(xpath = "//div[@role='dialog']//span/div[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]/ancestor::td/following-sibling::td[4]//button/span[contains(.,'Изменить')]")
    public WebElement BRBEdit;
    public By BRBEditWait = By.xpath(
            "//div[@role='dialog']//span/div[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]/ancestor::td/following-sibling::td[4]//button/span[contains(.,'Изменить')]");

    /**
     Определённое Место работы - с нужной ролью
     */
    public By Work (String line, String mo, String role) {
        By str = By.xpath(
                "//tbody/tr[" + line + "]/td[1][contains(.,'" + mo + "')]/following-sibling::td[1][contains(.,'" + role + "')]/following-sibling::td[1][contains(.,'')]");
        return str;
    }

    /**
     Пользователь - Количество мест работы
     */
    public By CountWork = By.xpath("//h4[contains(.,'Место работы')]/following-sibling::div//tbody/tr");

    /**
     Первое Место работы - Изменить
     */
    public By BRBEditWaitFirst = By.xpath("(//tbody/tr[1]/td[last()]/div)[3]");

    /**
     Добавление нового места работы
     */
    @FindBy(xpath = "//button[@tooltiptext='Редактировать']/span[contains(.,'Добавить')]")
    public WebElement AddWork;
    public By AddWorkWait = By.xpath("(//button[@tooltiptext='Редактировать']/span[contains(.,'Добавить')])[1]");

    /**
     Добавление нового места работы - Заголовок
     */
    public By HeaderAddWork = By.xpath("//h3[contains(.,'Добавление места работы')]");

    /**
     Селект места работы
     */
    public By SelectWork = By.xpath("//input[@placeholder = 'Укажите мед. организацию']");

    /**
     Добавить место работы - Место работы
     */
    public By WorkYatskiv = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"')]");

    /**
     Добавить место работы - Место работы
     */
    public By WorkOkb = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'БУ ХМАО-Югры \"Окружная клиническая больница\"')]");

    /**
     Добавить место работы - Место работы
     */
    public By WorkNGDP = By.xpath(
            "//ul/li[contains(.,'БУ ХМАО-Югры \"Нижневартовская городская детская поликлиника\"')]");

    /**
     Добавить место работы - Место работы
     */
    public By WorkBRB = By.xpath("//ul/li[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]");

    /**
     Селект Подразделение
     */
    public By SelectDivision = By.xpath("//input[@placeholder = 'Укажите подразделение']");

    /**
     Добавить место работы - Подразделение
     */
    public By DivisionChildPolyclinic = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'Административно-хозяйственное отделение детская поликлиника')]");

    /**
     Добавить место работы - Подразделение 5
     */
    public By Division5 = By.xpath("//div[@x-placement]//ul/li[5]");

    /**
     Добавить место работы - Подразделение - Бухгалтерия
     */
    public By Accounting = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'Бухгалтерия')]");

    /**
     Селект Роль пользователя
     */
    @FindBy(xpath = "//input[@placeholder = 'Укажите роль пользователя']")
    public WebElement SelectRoleUserWait;
    public By SelectRoleUser = By.xpath("//input[@placeholder = 'Укажите роль пользователя']");

    /**
     Добавить место работы - Роль пользователя
     */
    public By RoleUser5 = By.xpath("//div[@x-placement]//ul/li[5]");

    /**
     Добавить место работы - Роль пользователя
     */
    public By RoleUserTest213 = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'Тестовая роль 213')]");

    /**
     Добавить место работы - Роль пользователя
     */
    public By Role (String Role) throws InterruptedException {
        By str = By.xpath(
                "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'" + Role + "')]");
        return str;
    }

    public By RoleUserTest2 = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'Тестовая роль')]");
    public By RoleUserTest999 = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'Тестовая 999')]");
    public By RoleUserPoln = By.xpath("//div[@x-placement]//span[contains(.,'" + PRole + "')]");
    public By RoleUserKonsult = By.xpath("//div[@x-placement]//span[contains(.,'Консультант')]");

    /**
     Добавить место работы - Роль пользователя -  Разработчик МИС
     */
    public By RoleUserDeveloperMIS = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'Разработчик МИС')]");

    /**
     Добавить место работы - Роль пользователя - Консультант - Название класса
     */
    @FindBy(xpath = "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'Консультант')]/ancestor::li")
    public WebElement ClassNameRoleKonsult;
    public By ClassNameRole123 = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'Тестовая роль 213')]/ancestor::li");
    public By ClassNameRole2 = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'Тестовая роль')]/ancestor::li");
    public By ClassNameRole999 = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'Тестовая 999')]/ancestor::li");

    /**
     Добавить место работы - Дата увольнения
     */
    public By DateDismiss = By.xpath("//input[@placeholder = 'Укажите дату']");

    /**
     Добавить место работы - Дата увольнения - Текущая дата
     */
    public By DateDismissToday = By.xpath("(//div[@x-placement]//td[@class='available today'])[1]");

    /**
     Добавить место работы - Дата увольнения - Перед Текущая дата
     */
    public By DateDismissBackToday = By.xpath(
            "(//div[@x-placement]//td[@class='available today'])[1]/preceding::td[1]");

    /**
     Добавить место работы - Обновить
     */
    @FindBy(xpath = "(//button/span[contains(.,'Обновить')])[2]")
    public WebElement UpdateWork;
    public By UpdateWorkWait = By.xpath("(//button/span[contains(.,'Обновить')])[2]");

    /**
     Добавить место работы - Добавить
     */
    @FindBy(xpath = "//div[@class='el-dialog__footer']//button[@class='el-button el-button--success el-button--medium']/span[contains(.,'Добавить')]")
    public WebElement AddWorkButton;
    public By AddWorkButtonWait = By.xpath(
            "//div[@class='el-dialog__footer']//button[@class='el-button el-button--success el-button--medium']/span[contains(.,'Добавить')]");

    /**
     Добавить место работы - Добавить - Загрузка
     */
    public By Loading = By.xpath("(//div[@class='el-loading-spinner'])[4]");

    /**
     Добавить место работы - Закрыть
     */
    public By WorkClose = By.xpath("(//div[@class='el-dialog__footer']//button[contains(.,'Закрыть')])[2]");

    /**
     Уведомление - Пользователь с данным СНИЛС уже есть в базе ИЕМК
     */
    public By TrueAlertWait = By.xpath("//div[@role='alert']");

    /**
     Закрыть Уведомление
     */
    @FindBy(xpath = "//div[@class='el-notification__closeBtn el-icon-close']")
    public WebElement CloseAlert;

    /**
     Обновить
     */
    @FindBy(xpath = "(//button/span[contains(.,'Обновить')])[1]")
    public WebElement Update;
    public By UpdateWait = By.xpath("(//button/span[contains(.,'Обновить')])[1]");
    public By UpdateWait2 = By.xpath("(//button/span[contains(.,'Обновить')])[2]");

    /**
     Закрыть
     */
    @FindBy(xpath = "//footer//button/span[contains(.,'Закрыть')]")
    public WebElement Close;
    public By CloseWait = By.xpath("//footer//button/span[contains(.,'Закрыть')]");

    /**
     Пользователь - Добаввить
     */
    public By AddUsers = By.xpath(
            "//footer[@class='margin-top-30 text-right']//span[contains(.,'Добавить')]");

    /**
     1 на "Профиле" - означает, что добавлен профиль
     */
    public By OneWait = By.xpath(
            "//sup[@class='el-badge__content el-badge__content--primary is-fixed'][contains(.,'1')]");

    /**
     Изменить
     */
    @FindBy(xpath = "//button/span[contains(.,'Изменить')]")
    public WebElement Edit;
    public By EditWaitt = By.xpath("//button/span[contains(.,'Изменить')]");

    /**
     Количество мест работы
     */
    public By WorkQuantity = By.xpath("//section/h4[contains(.,'Место работы')]/following-sibling::div//tbody/tr");

    /**
     Изменить у первого
     */
    @FindBy(xpath = "(//button/span[contains(.,'Изменить')])[1]")
    public WebElement Edit1;
    public By Edit1Wait = By.xpath("(//button/span[contains(.,'Изменить')])[1]");

    /**
     Изменить у Второго
     */
    @FindBy(xpath = "(//button/span[contains(.,'Изменить')])[2]")
    public WebElement Edit2;
    public By Edit2Wait = By.xpath("(//button/span[contains(.,'Изменить')])[2]");

    /**
     Профиль
     */
    @FindBy(xpath = "(//table[@class='el-table__body']//span[contains(.,'Профили')])[1]")
    public WebElement Profile;
    public By ProfileButtonWait = By.xpath("(//table[@class='el-table__body']//span[contains(.,'Профили')])[1]");

    /**
     Профиль - Добавить
     */
    public By ProfileAddButton = By.xpath("//div[@class='cell']//button[@tooltiptext='Добавить']");

    /**
     Профиль - Добавить - Выберите медицинские профили
     */
    public By ProfileChoose = By.xpath("//input[@placeholder='Выберите медицинские профили']");

    /**
     Заголовок Профили
     */
    public By HeaderProfileWait = By.xpath("//h3[contains(.,'Медицинские профили')]");

    /**
     Профиль - Добавить
     */
    @FindBy(xpath = "//div[@class='cell']//button[@tooltiptext='Добавить']")
    public WebElement ProfileAdd;

    /**
     Профиль - Добавить - Нет данных
     */
    public By ProfileAddNotData = By.xpath("//span[contains(.,'Нет данных')]");

    /**
     Профиль - Редактировать
     */
    @FindBy(xpath = "(//div[@class='grid-table']//tr[@class='el-table__row']//button[@tooltiptext='Редактировать'])[2]")
    public WebElement EditProfile;
    public By EditProfileWait = By.xpath(
            "(//div[@class='grid-table']//tr[@class='el-table__row']//button[@tooltiptext='Редактировать'])[2]");

    /**
     Профиль - Редактировать - Выберите медицинские профили
     */
    @FindBy(xpath = "//input[@placeholder='Выберите медицинские профили']")
    public WebElement MedProfileButton;
    public By SelectMedProfile = By.xpath("//input[@placeholder='Выберите медицинские профили']");

    /**
     Профиль - Редактировать - Выберите медицинские профили - акушерскому делу
     */
    public By MedProfile = By.xpath("//div[@x-placement]//ul/li[contains(.,'акушерскому делу')]");
    public By MedProfileKosmetologi = By.xpath("//div[@x-placement]//ul/li[contains(.,'косметологии')]");
    public By MedProfileOptike = By.xpath("//div[@x-placement]//ul/li[contains(.,'медицинской оптике')]");

    public By DisableMedProfileKosmetologiTrue = By.xpath(
            "//div[@x-placement='bottom-start']//ul/li[@class='el-select-dropdown__item']/span[text()='косметологии']");
    public By DisableMedProfileOptike = By.xpath(
            "//div[@x-placement='bottom-start']//ul/li[@class='el-select-dropdown__item is-disabled']/span[contains(.,'медицинской оптике')]");

    /**
     Профиль - Укажите дату
     */
    @FindBy(xpath = "//input[@placeholder = 'Укажите дату увольнения']")
    public WebElement EditProfileDate;
    public By EditProfileDateWait = By.xpath(
            "//input[@placeholder = 'Укажите дату увольнения']");

    /**
     Ожидание селекта bottom-start
     */
    public By BottomStartWait = By.xpath("//div[@x-placement='bottom-start']");

    /**
     Профиль - Укажите дату - Текущая дата
     */
    @FindBy(xpath = "(//div[@x-placement='bottom-start']//td[@class='available today'])[1]")
    public WebElement EditProfileDateToday;
    public By EditProfileDateTodayWait = By.xpath(
            "(//div[@x-placement='bottom-start']//td[@class='available today'])[1]");

    /**
     Профиль - Укажите дату - Дата перед текущей
     */
    @FindBy(xpath = "(//div[@x-placement='bottom-start']//td[@class='available today'])[1]/preceding::td[1]")
    public WebElement EditProfileDatePrevious;
    public By EditProfileDatePreviousWait = By.xpath(
            "(//div[@x-placement='bottom-start']//td[@class='available today'])[1]/preceding::td[1]");

    /**
     Профиль - Отменить
     */
    @FindBy(xpath = "//button[@tooltiptext='Отменить']")
    public WebElement EditProfileCancel;

    /**
     Профиль - Прменить
     */
    @FindBy(xpath = "//button[@tooltiptext='Применить']")
    public WebElement EditProfileApply;
    public By EditProfileApplyWait = By.xpath(
            "//button[@tooltiptext='Применить']");

    /**
     Профиль - Закрыть
     */
    @FindBy(xpath = "(//button/span[contains(.,'Закрыть')])[3]")
    public WebElement EditProfileClose;
    public By EditProfileCloseWait = By.xpath(
            "(//button/span[contains(.,'Закрыть')])[3]");

    /**
     Метод для увольнения сотрудника с места работы
     MO - Мед. Организация через которую авторизуемся
     SNILS - Снилс, который используем при поиске пациента
     editMo - Мед. Организация, по которой нужно уволить пациента
     */
    public void DeleteUsersMethod (By MO, String SNILS, By editWait) throws InterruptedException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        System.out.println("Увольнение сотрудника для дальнейших тестов");
        AuthorizationMethod(MO);
        WaitElement(UsersWait);
        actionElementAndClick(Users);
        WaitElement(HeaderUsersWait);
        AddUser.click();
        WaitElement(InputSnilsWait);
        InputSnils.sendKeys(SNILS);
        Thread.sleep(1500);
        WaitNotElement3(LoadingSnils, 20);
        ClickElement(ButtonSearchWait);
        WaitElement(SnilsInputWait);
        ClickElement(editWait);
        SelectClickMethod(DateDismiss, DateDismissBackToday);
        ClickElement(UpdateWorkWait);
        WaitNotElement(EditWait);
        ClickElement(UpdateWait);
    }

    @Step("Метод удаления из БД созданоого пользователя")
    public void DeleteUsersMethodSql (String Snils) throws InterruptedException, SQLException {
        sql = new SQL();

        String snils = Snils.substring(0, 3) + "-" + Snils.substring(4, 7) + "-" + Snils.substring(8,
                11) + " " + Snils.substring(12, 14);

        sql.UpdateConnection("update telmed.userplacework set quitdate = '2024-06-05 00:00:00.000' \n" +
                "where userid in (\n" +
                "SELECT u.id\n" +
                "    FROM telmed.users u\n" +
                "     WHERE u.snils = '" + snils + "'\n" +
                "    ) ;");

        sql.UpdateConnection("DELETE FROM telmed.users_credentials\n" +
                "WHERE id IN (\n" +
                "    SELECT u.id\n" +
                "    FROM telmed.users u\n" +
                "    WHERE u.snils = '" + snils + "'\n" +
                ");");

        sql.UpdateConnection("DELETE FROM telmed.usermedprofile  \n" +
                "WHERE userid IN (\n" +
                "    SELECT u.id\n" +
                "    FROM telmed.users u\n" +
                "    WHERE u.snils = '" + snils + "'\n" +
                ");");

        sql.UpdateConnection("DELETE FROM telmed.userplacework \n" +
                "WHERE userid IN (\n" +
                "    SELECT u.id\n" +
                "    FROM telmed.users u\n" +
                "    WHERE u.snils = '" + snils + "'\n" +
                ");");

        sql.UpdateConnection("DELETE FROM telmed.consultationstakenslots \n" +
                "WHERE user_id IN (\n" +
                "    SELECT id \n" +
                "    FROM telmed.users \n" +
                "    WHERE snils = '" + snils + "'\n" +
                ");");

        sql.UpdateConnection("Delete from telmed.users where snils = '" + snils + "';");
    }
}
