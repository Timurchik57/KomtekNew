package UI.TmTest.PageObject.NSI;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Equipments extends BaseTest {
    public Equipments (WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     НСИ - Оборудование
     */
    @FindBy(xpath = "//a[@href='/nsi/equipment']")
    public WebElement Equipment;
    public By EquipmentWait = By.linkText("Оборудование");
    public By EquipmentWaitt = By.xpath("//a[@href='/nsi/equipment']");

    /**
     Заголовок Оборудование
     */
    public By HeaderEquipmentWait = By.xpath("//h1[.='Оборудование']");

    /**
     Поиск Мед. Организации
     */
    public By InputMoWait = By.xpath("//input[@placeholder='Мед. организация']");

    /**
     Выбранная Мед. Организации
     */
    public By SelectMO = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Яцкив')]");

    /**
     Выбранная Мед. Организации - Окружная клиническая больница
     */
    public By SelectMOOKB = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Окружная клиническая больница')]");

    /**
     Выбранная Мед. Организации - Кондинская районная стоматологическая поликлиника
     */
    public By SelectMOKon = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'Кондинская районная стоматологическая поликлиника')]");

    /**
     Доступно другим МО
     */
    public By CheckIs = By.xpath("//span[contains(.,'Доступно другим МО')]/preceding-sibling::span");

    /**
     Наименование
     */
    @FindBy(xpath = "//input[@placeholder='Наименование']")
    public WebElement Name;
    public By NameWait = By.xpath("//input[@placeholder='Наименование']");

    /**
     Описание
     */
    @FindBy(xpath = "//input[@placeholder='Описание']")
    public WebElement Description;

    /**
     Загрузка Поиска
     */
    public By SearchLoading = By.xpath("//button[@disabled='disabled']//span[contains(.,'Поиск')]");

    /**
     Поиск
     */
    @FindBy(xpath = "//button//span[contains(.,'Поиск')]")
    public WebElement Search;
    public By SearchWait = By.xpath("//button//span[contains(.,'Поиск')]");

    /**
     Поиск - 1 трока в таблице
     */
    @FindBy(xpath = "//tbody/tr[1]")
    public WebElement FirstLine;

    /**
     Поиск - Все строки - Любой столбец
     */
    public By SelectList (String value, String column) {
        By str = By.xpath("//tbody/tr[" + value + "]/td[" + column + "]//span");
        return str;
    }

    /**
     Поиск - Строка в таблице для Хмао
     */
    @FindBy(xpath = "//tr/td[6][contains(.,'123')]/parent::tr")
    public WebElement LineXmao;
    public By LineXmaoWait = By.xpath("//tr/td[6][contains(.,'123')]/parent::tr");

    /**
     Поиск - 1 трока в таблице - Редактировать
     */
    public By FirstLineSet = By.xpath("//table//td[1]/following-sibling::td//button[@tooltiptext='Редактировать']");

    /**
     Поиск - Строка в таблице для Хмао - Редактировать
     */
    public By LineSetXmao = By.xpath(
            "//tr/td[6][contains(.,'123')]/following-sibling::td//button[@tooltiptext='Редактировать']");

    /**
     Поиск - 1 трока в таблице - Дата Списания
     */
    @FindBy(xpath = "//tbody/tr[1]/td[8]//span")
    public WebElement DateDownsWait;
    public By DownsWait = By.xpath("//tbody/tr[1]/td[8]//span");

    /**
     Добавить
     */
    public By AddHeader = By.xpath("//header//button[contains(.,'Добавить')]");

    /**
     Добавить - Название оборудования
     */
    @FindBy(xpath = "//label[contains(.,'Название оборудования')]")
    public WebElement NameEquiments;

    /**
     Добавить - Дата ввода в эксплуатацию
     */
    @FindBy(xpath = "//div[@class='el-col el-col-9'][contains(.,'Дата ввода в эксплуатацию')]")
    public WebElement Date;

    /**
     Найденное Наименование
     */
    public By SearchNameWait = By.xpath("//table//tr[contains(.,'X-OMAT')][1]");

    public By SelectName (String value) {
        By select = By.xpath("//table//tr[contains(.,'" + value + "')][1]");
        return select;
    }

    /**
     Найденное Наименование
     */
    public By SearchNameWaitFirst = By.xpath("(//table//tr)[2]/td[3]//span");

    /**
     Все оборудования
     */
    public By ListEquipment = By.xpath("//div[@class='el-table__body-wrapper is-scrolling-none']//tbody/tr");

    /**
     Наименование столбцов таблицы (Список)
     */
    @FindBy(xpath = "//thead/tr/th/div")
    public WebElement NameColumn;
    public By NameColumnWait = By.xpath("(//thead/tr/th/div)[1]");

    /**
     Наименование столбцов таблицы (Список) - Загрузка
     */
    public By Loading = By.xpath("//main//div[@class='el-loading-spinner']");

    /**
     Редактировать
     */
    @FindBy(xpath = "(//table//td[contains(.,'X-OMAT')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement Edit;

    /**
     Редактировать 1 элемент из списка
     */
    @FindBy(xpath = "(//tbody/tr)[1]/td//button")
    public WebElement Edit1;
    public By Edit1Wait = By.xpath("(//tbody/tr)[1]/td//button");

    /**
     Редактировать - недоступные для редактирования поля
     */
    @FindBy(xpath = "//input[@disabled]")
    public WebElement Disabled;
    public By DisabledWait = By.xpath("//input[@disabled]");

    /**
     Редактировать - Заголовок редактирования
     */
    public By HeaderEditWait = By.xpath("//h3[.='Изменение данных оборудования']");

    /**
     Редактировать - Выберите тип оборудования
     */
    public By TypeWait = By.xpath("//label[contains(.,'Выберите тип оборудования')]/following-sibling::div");

    /**
     Редактировать - Выберите тип оборудования - Проявочные автоматы и камеры
     */
    public By SelectType = By.xpath("(//ul//li//span[contains(.,'Проявочные автоматы и камеры')])[2]");

    /**
     Редактировать - Выберите тип оборудования - Выбранный тип
     */
    @FindBy(xpath = "//div[@x-placement]//ul/li[@class='el-select-dropdown__item selected']/span")
    public WebElement SelectTypeTrue;
    public By SelectTypeTrueWait = By.xpath(
            "//div[@x-placement]//ul/li[@class='el-select-dropdown__item selected']/span");
    @FindBy(xpath = "//div[@x-placement]//ul/li[@class='el-select-dropdown__item selected hover']/span")
    public WebElement SelectTypeTrueHover;

    /**
     Редактировать - Выберите модальность оборудования
     */
    public By ModelWait = By.xpath("//label[contains(.,'Выберите модальность оборудования')]/following-sibling::div");

    /**
     Редактировать - Выберите модальность оборудования - Электрофизиология сердца (Cardiac Electrophysiology)
     */
    public By SelectModel = By.xpath(
            "//ul//li//span[contains(.,'Электрофизиология сердца (Cardiac Electrophysiology)')]");

    /**
     Редактировать - Выберите модальность оборудования - Выбранная модальность
     */
    @FindBy(xpath = "//div[@x-placement]//ul/li[@class='el-select-dropdown__item selected']/span")
    public WebElement SelectModelTrue;

    /**
     Редактировать - Выберите подразделение МО
     */
    public By DivisionWait = By.xpath("//label[contains(.,'Выберите подразделение МО')]/following-sibling::div");

    /**
     Редактировать - Выберите подразделение МО - Не выбрано
     */
    public By DivisionNotWait = By.xpath(
            "//label[contains(.,'Выберите подразделение МО')]/following-sibling::div//input[@placeholder='Выбрать']");

    /**
     Редактировать - Выберите подразделение МО - Выбранное подразделение
     */
    @FindBy(xpath = "//div[@x-placement]//ul/li[@class='el-select-dropdown__item selected']/span")
    public WebElement DivisionTrue;

    /**
     Редактировать - Выберите подразделение МО - Женская консультация
     */
    public By SelectDivision = By.xpath("//ul//li//span[contains(.,'Женская консультация')]");

    /**
     Редактировать - Выберите подразделение МО - Женская консультация
     */
    public By SelectDivisionBuh = By.xpath("//ul//li//span[contains(.,'Бухгалтерия')]");

    /**
     Редактировать - Выберите подразделение МО - Административно-хозяйственный отдел
     */
    public By SelectDivisionAdm = By.xpath("//ul//li//span[contains(.,'Административно-хозяйственный отдел')]");

    /**
     Редактировать - Выберите подразделение МО - Административно-хозяйственный отдел
     */
    public By SelectTer = By.xpath("//ul//li//span[contains(.,'Терапевтическое отделение')]");

    /**
     Редактировать - Выберите подразделение МО - Инфекционное отделение
     */
    public By SelectDivisionInf = By.xpath("//ul//li//span[text()='Инфекционное отделение']");

    /**
     Редактировать - Выберите отделение кабинет
     */
    @FindBy(xpath = "//label[contains(.,'Выберите отделение/ кабинет')]/following-sibling::div")
    public WebElement Office;
    public By OfficeWait = By.xpath("//label[contains(.,'Выберите отделение/ кабинет')]/following-sibling::div");

    /**
     Редактировать - Выберите отделение кабинет - Выбранный кабинет
     */
    @FindBy(xpath = "//label[contains(.,'Выберите отделение/ кабинет')]/following-sibling::div")
    public WebElement OfficeTrue;

    /**
     Редактировать - Выберите отделение кабинет - Список
     */
    @FindBy(xpath = "//div[@x-placement=]//ul/li")
    public WebElement SelectOffice;
    public By SelectOfficeWaitt = By.xpath("//div[@x-placement]//ul/li/span");
    public By SelectOfficeWait = By.xpath("//div[@x-placement]//ul/li[1]");

    /**
     Редактировать - Дата ввода в эксплуатацию
     */
    public By DateInput = By.xpath("//div[@class='el-col el-col-9'][contains(.,'Дата ввода в эксплуатацию')]");

    /**
     Редактировать - Дата списания оборудования
     */
    public By DateDowns = By.xpath(
            "//div[@class='el-col el-col-9'][contains(.,'Дата списания оборудования')]/following-sibling::div//input");

    /**
     Редактировать - Дата списания оборудования - Текущая дата
     */
    public By DateDownsToDay = By.xpath("(//td[@class='available today'])[1]");

    /**
     Редактировать - Дата списания оборудования - Удалить дату
     */
    public By DateDownsDelete = By.xpath(
            "//input[@placeholder='Введите дату списания']/following-sibling::span[@class='el-input__suffix']/span");

    /**
     Редактировать - Редактировать - Инвентарный номер
     */
    @FindBy(xpath = "(//div[contains(.,'Инвентарный номер')]/following-sibling::div//input)[1]")
    public WebElement InventNumber;

    /**
     Редактировать - Редактировать - Серийный номер
     */
    @FindBy(xpath = "(//div[contains(.,'Серийный номер')]/following-sibling::div//input)[1]")
    public WebElement SerialNumber;

    /**
     Редактировать - Редактировать - Серийный номер нужный
     */
    public String SerialNumberTrue = "313141";

    /**
     Редактировать - Какое из списка оборудование - нужное
     */
    public Integer EquipmentTrue;

    /**
     Редактировать - Доступно другим МО
     */
    @FindBy(xpath = "(//label//span[@class='el-checkbox__inner'])[2]")
    public WebElement Check;
    public By CheckWait = By.xpath("(//label/span[@class='el-checkbox__input'])[2]");
    public By CheckInput = By.xpath("//span[contains(.,'Доступно для других МО')]/preceding-sibling::span");

    /**
     Редактировать - Причина недоступности
     */
    public By Reason = By.xpath("//label[contains(.,'Причина недоступности')]/following-sibling::div//input");
    public By SelectReason = By.xpath("//div[@x-placement]//ul/li/span");
    public By SelectReasonFirst = By.xpath("//div[@x-placement]//ul/li[1]");

    /**
     Редактировать - Введите вес
     */
    @FindBy(xpath = "//input[@placeholder='Введите вес']")
    public WebElement Weight;

    /**
     Редактировать - Исследования
     */
    @FindBy(css = "#tab-research")
    public WebElement Researches;

    /**
     Редактировать - Исследования - Введите значение
     */
    @FindBy(xpath = "//input[@placeholder='Введите значение или пробел для поиска']")
    public WebElement InputWord;
    public By InputWordWait = By.xpath("//input[@placeholder='Введите значение или пробел для поиска']");

    /**
     Редактировать - Исследования - Нужное значение значение
     */
    @FindBy(xpath = "//ul//li//span[contains(.,'HMP01')][1]")
    public WebElement HMP01;
    public By HMP01Wait = By.xpath("//ul//li//span[contains(.,'HMP01')][1]");

    /**
     Редактировать - Исследования - Появление окна снизу
     */
    public By BottomStartWait = By.xpath("//div[@x-placement='bottom-start']");

    /**
     Редактировать - Исследования - Исследование уже присутствует
     */
    public By ResearchesTrue = By.xpath("//tbody/tr//div[contains(.,'HMP01')]");

    /**
     Редактировать - Исследования
     */
    public By Researches (String value) {
        By field = By.xpath("//tbody/tr//div[contains(.,'" + value + "')]");
        return field;
    }

    /**
     Редактировать - Исследования - Закрыть
     */
    @FindBy(xpath = "//button//span[contains(.,'Закрыть')]")
    public WebElement Close;

    /**
     Редактировать - Исследования - Добавить
     */
    @FindBy(xpath = "//button//span//i[@class='far fa-plus']")
    public WebElement Add;

    /**
     Редактировать - Обновить
     */
    @FindBy(xpath = "//button//span[contains(.,'Обновить')]")
    public WebElement Update;
    public By UpdateWait = By.xpath("//button//span[contains(.,'Обновить')]");
}
