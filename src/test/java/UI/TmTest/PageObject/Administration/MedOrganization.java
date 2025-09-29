package UI.TmTest.PageObject.Administration;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MedOrganization extends BaseTest {
    public MedOrganization (WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     Администрирование - Мед. Организации
     */
    @FindBy(xpath = "//a[@href='/permission/access']")
    public WebElement Organization;
    public By OrganizationWait = By.xpath("//a[@href='/permission/access']");

    /**
     Заголовок Мед. Организации
     */
    public By HeaderOrganizationWait = By.xpath("//h1[.='Мед. организации']");

    /**
     Ввод МО
     */
    public By InputOrganizationWait = By.xpath("//input[@placeholder='Название медицинской организации']");

    /**
     Выбор МО
     */
    public By SelectMO = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Яцкив')]");
    public By SelectMOKon = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'Кондинская районная стоматологическая поликлиника')]");
    public By SelectMOOKB = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'БУ ХМАО-Югры \"Окружная клиническая больница\"')]");

    /**
     Отображение одной мо в селекте
     */
    public By SelectMOOne = By.xpath("//div[@x-placement]//ul/li[@class='el-select-dropdown__item'][1]");

    /**
     Отображение более одной мо в селекте
     */
    public By SelectMOTwo = By.xpath("//div[@x-placement]//ul/li[@class='el-select-dropdown__item hover'][2]");

    /**
     Поиск
     */
    @FindBy(xpath = "//div[@class='grid-table']/div[1]/form/div[3]//button[@type='submit']")
    public WebElement Search;
    public By SearchWait = By.xpath("//div[@class='grid-table']/div[1]/form/div[3]//button[@type='submit']");

    /**
     Найденная МО
     */
    public By SearchMO = By.xpath("//div[@class='grid-table']/div[2]//span[contains(.,'Яцкив')]");

    /**
     1 МО - Редактировать
     */
    public By EditFirst = By.xpath("(//tbody/tr)[1]/td[3]//button");

    /**
     Найденная МО - Редактировать
     */
    public By Edit = By.xpath("//tbody/tr[1]/td[3]//button[@tooltiptext='Редактировать']");

    /**
     Найденная МО - Редактировать - Заголовок
     */
    public By HeaderMOWait = By.xpath("//h3[contains(.,'Карточка медицинской организации')]");

    /**
     Редактировать - Состояние организации
     */
    public By HealthOrganization = By.xpath("//label[contains(.,'Состояние организации')]");

    /**
     Редактировать - Пароль COVID - Ввод
     */
    public By Covid = By.xpath("//label[contains(.,'Пароль COVID')]/following-sibling::div//input");

    /**
     Редактировать - Пароль COVID - Ввод - Ошибка
     */
    public By Error = By.xpath(
            "//div[@class='el-form-item__error'][contains(.,'Недопустимо написание \"пробел\" первым и последним символом')]");

    /**
     Редактировать - Настройки уровня
     */
    public By SettingLevel = By.xpath(
            "//div[@class='el-divider__text is-left'][contains(.,'Настройка уровней')]");

    /**
     Редактировать - Настройки уровня - Добавить
     */
    public By AddAccess = By.xpath(
            "(//div[@class='el-divider el-divider--horizontal'][contains(.,' Настройка уровней')]/following-sibling::section//button[contains(.,'Добавить')])[1]");

    /**
     Редактировать - Настройки уровня - Количество доступов
     */
    public By AccessCount = By.xpath(
            "//div[@class='el-divider el-divider--horizontal'][contains(.,' Настройка уровней')]/following-sibling::section//table[@class='el-table__body']/tbody/tr");

    /**
     Редактировать - Настройки уровня - Определённый уровень у любого доступа
     */
    public By AccessCountTrue (String value) {
        By str = By.xpath(
                "//div[@class='el-divider el-divider--horizontal'][contains(.,' Настройка уровней')]/following-sibling::section//table[@class='el-table__body']/tbody/tr/td[3]//span[contains(.,'" + value + "')]");
        return str;
    }

    /**
     Редактировать - Настройки уровня - Удалить определённую строку
     */
    public By Delete (String value) {
        By str = By.xpath(
                "//div[@class='el-divider el-divider--horizontal'][contains(.,' Настройка уровней')]/following-sibling::section//table[@class='el-table__body']/tbody/tr[" + value + "]//button[@text='Удалить данный пункт?']");
        return str;
    }

    /**
     Редактировать - Настройки доступа - Удалить элемент с определённой датой
     */
    public By DeleteForDate (String value) {
        By DeleteForDate = By.xpath(
                "//div[@class='el-divider el-divider--horizontal'][contains(.,' Настройка уровней')]/following-sibling::section//table[@class='el-table__body']/tbody/tr//span[contains(.,'" + value + "')]/ancestor::div[1]/ancestor::td/following-sibling::td[last()]//button[@text='Удалить данный пункт?']");
        return DeleteForDate;
    }

    /**
     Редактировать - Настройки доступа - Удалить - Да Удлать
     */
    public By DeleteYes = By.xpath(
            "//div[@x-placement]//span[contains(.,'Удалить')]");

    /**
     Редактировать - Настройки доступа - Редактировать элемент с определённой датой
     */
    public By EditForDate (String value) {
        By EditEorDate = By.xpath(
                "//div[@class='el-divider el-divider--horizontal'][contains(.,' Настройка уровней')]/following-sibling::section//table[@class='el-table__body']/tbody/tr//span[contains(.,'" + value + "')]/ancestor::div[1]/ancestor::td/following-sibling::td[last()]//button[@tooltiptext='Редактировать']");
        return EditEorDate;
    }

    /**
     Редактировать - Настройки доступа - Редактировать 1
     */
    public By AccessFirstEdit = By.xpath(
            "//div[@class='el-divider el-divider--horizontal'][contains(.,' Настройка уровней')]/following-sibling::section//table[@class='el-table__body']/tbody/tr[1]/td[last()]//button[@tooltiptext='Редактировать']");

    /**
     Редактировать - Настройки доступа - Редактировать - Дата начала действия уровня
     */
    public By DateStart = By.xpath(
            "//label[contains(.,'Дата начала действия уровня')]/following-sibling::div");

    /**
     Редактировать - Настройки доступа - Редактировать - Дата начала действия уровня - текущее число текущего месяца
     */
    public By DateToDay = By.xpath(
            "//div[@x-placement]//table[1]//td[@class='available today']");

    /**
     Редактировать - Настройки доступа - Редактировать - Дата начала действия уровня - 1 число текущего месяца
     */
    public By DateStart1 = By.xpath(
            "(//div[@x-placement]//table[1]//td[contains(.,'1')])[1]");

    /**
     Редактировать - Настройки доступа - Редактировать - Уровень указания медицинской помощи
     */
    public By Level = By.xpath(
            "//label[contains(.,'Уровень указания медицинской помощи')]/following-sibling::div");

    /**
     Редактировать - Настройки доступа - Редактировать - Уровень указания медицинской помощи - 1 уровень
     */
    public By Level (String value) {
        By str = By.xpath("//div[@x-placement]//ul/li[contains(.,'" + value + "')]");
        return str;
    }

    /**
     Редактировать - Настройки доступа - Редактировать - Уровень указания медицинской помощи - 1 уровень
     */
    public By Level1 = By.xpath(
            "//div[@x-placement]//ul/li[1]");

    /**
     Редактировать - Настройки доступа - Редактировать - Уровень указания медицинской помощи - 2 уровень
     */
    public By Level2 = By.xpath(
            "//div[@x-placement]//ul/li[2]");

    /**
     Редактировать - Настройки доступа - Редактировать - Уровень указания медицинской помощи - 3 уровень
     */
    public By Level3 = By.xpath(
            "//div[@x-placement]//ul/li[3]");

    /**
     Редактировать - Настройки доступа - Редактировать - Уровень указания медицинской помощи - 3 уровень - выбранный
     */
    public By Level3True = By.xpath(
            "//div[@x-placement]//ul/li[@class='el-select-dropdown__item selected']/span[contains(.,'Третий уровень')]");

    /**
     Редактировать - Настройки доступа - Редактировать - Профиль
     */
    public By Profile = By.xpath(
            "//label[contains(.,'Профиль')]/following-sibling::div");

    /**
     Редактировать - Настройки доступа - Редактировать - Профиль - 1 из списка
     */
    public By Profile1 = By.xpath(
            "//div[@x-placement]//ul/li[1]");

    /**
     Редактировать - Настройки доступа - Редактировать - Обновить
     */
    public By UpdateAccess (String value) {
        By UpdateAccess = By.xpath(
                "(//button[contains(.,'Обновить')])[" + value + "]");
        return UpdateAccess;
    }

    /**
     Редактировать - Настройки доступа - Редактировать - Добавить
     */
    public By AddAccess2 = By.xpath(
            "(//div[@class='el-dialog__footer']//button[contains(.,'Добавить')])[2]");

    /**
     Вкладка - Курируемые МО
     */
    public By Supervised = By.xpath("//div[@aria-controls][contains(.,'Курируемые МО')]");

    /**
     Вкладка - Курируемые МО - Недоступны
     */
    public By SupervisedDisable = By.xpath(
            "//div[@class='el-tabs__item is-top is-disabled'][contains(.,'Курируемые МО')]");

    /**
     Вкладка - Курируемые МО - Количество МО
     */
    public By MOCount = By.xpath(
            "//div[@class='el-tabs__content']//div[@class='el-table__body-wrapper is-scrolling-none']//tbody/tr");

    /**
     Вкладка - Курируемые МО - Отображение нужной МО
     */
    public By MOTrue = By.xpath(
            "//div[@class='el-tabs__content']//div[@class='el-table__body-wrapper is-scrolling-none']//tbody/tr[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]");

    /**
     Вкладка - Курируемые МО - Удаление нужной МО
     */
    public By MOTrueDelete = By.xpath(
            "(//div[@class='el-tabs__content']//div[@class='el-table__body-wrapper is-scrolling-none']//tbody/tr[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]/td[3]//button)[3]");

    /**
     Вкладка - Курируемые МО - Удаление МО по списку
     */
    public By MOTrueDelete (String value) {
        By str = By.xpath(
                "//div[@class='el-tabs__content']//div[@class='el-table__body-wrapper is-scrolling-none']//tbody/tr[" + value + "]//button[@text='Удалить данный пункт?']");
        return str;
    }

    /**
     Вкладка - Курируемые МО - Удаление нужной МО - Подтверждение удаления
     */
    public By MOTrueDeleteYes = By.xpath("//div[@x-placement]//button[1]");

    /**
     Вкладка - Курируемые МО - Добавить
     */
    public By MOAdd = By.xpath(
            "//div[@class='el-tabs__content']//div[@class='el-table__header-wrapper']//th[@class='el-table_3_column_11  is-center   is-leaf el-table__cell']//button[contains(.,'Добавить')]");

    /**
     Вкладка - Курируемые МО - Добавить - Выбор МО
     */
    public By MOAddBRB = By.xpath(
            "//div[@x-placement]//ul/li[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]");

    /**
     Вкладка - Курируемые МО - Добавить - Выбор МО - Добавить
     */
    public By MOAddEnter = By.xpath("//button[@tooltiptext='Добавить']");

    /**
     Вкладка - Запись на оборудование
     */
    @FindBy(xpath = "//div[@role='tablist']/div[contains(.,'Запись на оборудование')]")
    public WebElement Recording;
    public By RecordingWait = By.xpath("//div[@role='tablist']/div[contains(.,'Запись на оборудование')]");

    /**
     Вкладка - Информационные системы
     */
    @FindBy(xpath = "//div[@role='tablist']/div[contains(.,'Информационные системы')]")
    public WebElement InfoSystem;

    /**
     Информационные системы - Наименование системы
     */
    public By NameSystem = By.xpath("//label[contains(.,'Наименование системы')]");

    /**
     Вкладка - Информационные системы - Закрыть
     */
    @FindBy(xpath = "(//footer/button/span[contains(.,'Закрыть')])[3]")
    public WebElement CloseInfoSystem;

    /**
     Гама Камера
     */
    @FindBy(xpath = "//div[@role='tab']/div[contains(.,'Гамма-камера')]")
    public WebElement GammaCamera;
    public By GammaCameraWait = By.xpath("//div[@role='tab']/div[contains(.,'Гамма-камера')]");

    /**
     КТ
     */
    public By KT = By.xpath("//div[@role='tab']/div[contains(.,'КТ')]");

    /**
     МРТ
     */
    public By MRT = By.xpath("//div[@role='tab']/div[contains(.,'МРТ')]");

    /**
     Медицинские организации, получающие консультацию
     */
    public By MOReceiveWait = By.xpath(
            "//div[@class='el-collapse-item is-active'][contains(.,'Медицинские организации, получающие консультацию')]");

    /**
     Добавить
     */
    @FindBy(xpath = "//div[@class='el-collapse-item is-active']//div[@class='el-table__header-wrapper'][1]//span[contains(.,'Добавить')]")
    public WebElement Add;
    public By AddWait = By.xpath(
            "//div[@class='el-collapse-item is-active']//div[@class='el-table__header-wrapper'][1]//span[contains(.,'Добавить')]");

    /**
     ведите значение для поиска
     */
    @FindBy(xpath = "//input[@placeholder='Введите значение для поиска']")
    public WebElement SearchValue;
    public By SearchValueWait = By.xpath("//input[@placeholder='Введите значение для поиска']");

    /**
     Окружная клиническая больница
     */
    public By OKB = By.xpath(
            "//div[@x-placement='bottom-start']//ul/li[@class='el-select-dropdown__item']/span[contains(.,'Окружная клиническая больница')]");

    /**
     Окружная клиническая больница - Добавленная
     */
    public By OKBTrue = By.xpath(
            "//div[@class='el-table__body-wrapper is-scrolling-none']//tbody/tr//span[contains(.,'БУ ХМАО-Югры \"Окружная клиническая больница\"')]");

    /**
     Добавить МО
     */
    @FindBy(xpath = "//button[@tooltiptext='Добавить'][1]")
    public WebElement AddMO;
    public By AddMOWait = By.xpath("//div[@class='el-dialog__body']//footer//button//span[contains(.,'Обновить')]");

    /**
     Закрыть
     */
    @FindBy(xpath = "//button[@tooltiptext='Закрыть']")
    public WebElement Close;
    public By CloseWait = By.xpath("(//footer/button[contains(.,'Закрыть')])[2]");

    /**
     Карточка мед организации - Закрыть
     */
    public By CloseTrue = By.xpath(
            "//div[@class='el-tabs el-tabs--top el-tabs--border-card']/following-sibling::footer/button[contains(.,'Закрыть')]");

    /**
     Закрыть 2
     */
    public By Close2 (String value) {
        By Close2 = By.xpath(
                "(//footer/button[contains(.,'Закрыть')])[" + value + "]");
        return Close2;
    }

    /**
     Крестик
     */
    public By Cross = By.xpath("//div[@class='el-dialog__header']/h3/following-sibling::button[@aria-label='Close']/i");

    /**
     Обновить
     */
    @FindBy(xpath = "//div[@class='el-dialog__body']//footer//button//span[contains(.,'Обновить')]")
    public WebElement Update;
    public By UpdateWait = By.xpath("//div[@class='el-dialog__body']//footer//button//span[contains(.,'Обновить')]");
}
