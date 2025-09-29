package UI.TmTest.PageObject.Administration;

import Base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class RouteOMP extends BaseTest {
    /**
     * Администрирование - Настройка маршрутов ОМП
     */
    public RouteOMP(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Администрирование - Настройка маршрутов ОМП
     */
    @FindBy(xpath = "//a[@href='/administration/pmc-route-settings']")
    public WebElement RouteOMP;
    public By RouteOMPWait = By.xpath("//a[@href='/administration/pmc-route-settings']");

    /**
     * Настройка маршрутов ОМП - Заголовок
     */
    public By Header = By.xpath("//header/h1[contains(.,'Настройка маршрутов ОМП')]");

    /**
     * Настройка маршрутов ОМП - Наименование маршрута
     */
    @FindBy(xpath = "//input[@placeholder='Введите название маршрута']")
    public WebElement NameMarch;

    /**
     * Настройка маршрутов ОМП - Поиск
     */
    public By Search = By.xpath("//button/span[contains(.,'Поиск')]");

    /**
     * Настройка маршрутов ОМП - Поиск - Ошибка
     */
    public By Error = By.xpath("//div[@role='alert'][1]//div[@class='el-notification__closeBtn el-icon-close']");

    /**
     * Настройка маршрутов ОМП - Добавить
     */
    public By Add = By.xpath("//button/span[contains(.,'Добавить маршрут')]");

    /**
     * Настройка маршрутов ОМП - Удалить
     */
    public By Delete = By.xpath("//tbody/tr/td[5]//button[@placement='top-end']");

    /**
     * Настройка маршрутов ОМП - Редактировать первый
     */
    public By Edit = By.xpath("(//button[@tooltiptext='Редактировать'])[1]");

    /**
     * Настройка маршрутов ОМП - Удалить - Удалить
     */
    public By DeleteYes = By.xpath("//button/span[contains(.,'Удалить')]");

    /**
     * Добавить - Тип Маршрута - Федеральный
     */
    public By Federation = By.xpath("//label[contains(.,'Тип маршрута')]/following-sibling::div//label[1]/span[1]");

    /**
     * Добавить - Тип Маршрута - Размер текста
     */
    @FindBy(xpath = "//label[@for='graphType']")
    public WebElement NameMarchSize;

    /**
     * Добавить - Наименование маршрута
     */
    public By NameRoute = By.xpath(
            "//label[contains(.,'Наименование маршрута')]/following-sibling::div//input[@placeholder='Выберите наименование маршрута']");

    /**
     * Добавить - Наименование маршрута - Размер текста
     */
    @FindBy(xpath = "//label[@for='routeName']")
    public WebElement NameRouteSize;

    /**
     * Добавить - Регистры
     */
    public By Registers = By.xpath("//label[contains(.,'Регистры')]/following-sibling::div");

    /**
     * Добавить - Регистры - Размер текста
     */
    @FindBy(xpath = "//label[@for='registerTypes']")
    public WebElement RegistershSize;

    /**
     * Добавить - Этап ОМП
     */
    public By OMP = By.xpath("//label[contains(.,'Этап ОМП')]/following-sibling::div");

    /**
     * Добавить - Этап ОМП - Размер текста
     */
    @FindBy(xpath = "//label[@for='routePeriodSettings[0].stage']")
    public WebElement OMPSize;

    /**
     * Добавить - Подэтап ОМП
     */
    public By PodOMP = By.xpath("//label[contains(.,'Подэтап ОМП')]/following-sibling::div");

    /**
     * Добавить - Подэтап ОМП - Размер текста
     */
    @FindBy(xpath = "//label[@for='routePeriodSettings[0].substageStart']")
    public WebElement PodSize;

    /**
     * Добавить - Уровень указания
     */
    public By Level = By.xpath("//label[contains(.,'Уровень указания')]/following-sibling::div");

    /**
     * Добавить - Медицинская организация
     */
    public By MO = By.xpath("//label[contains(.,'Медицинская')]/following-sibling::div");

    /**
     * Добавить - Медицинская организация - OKБ
     */
    public By SelectMOOKB = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'БУ ХМАО-Югры \"Окружная клиническая больница\"')]");

    /**
     * Добавить - Настройки доступа - Добавить
     */
    public By ADD = By.xpath("//th//button[contains(.,'Добавить')]");

    /**
     * Добавить - Настройки доступа - Добавить - Код
     */
    public By Code = By.xpath("//input[@placeholder='Введите значение для поиска']");

    /**
     * Добавить - Настройки доступа - Добавить - Введите нормативный срок (в днях)
     */
    public By Norma = By.xpath("(//input[@placeholder='Введите нормативный срок (в днях)'])[1]");

    /**
     * Добавить - Настройки доступа - Добавить - Уровень указания медицинской помощи
     */
    public By LevelAdd = By.xpath("(//input[@placeholder='Уровень указания медицинской помощи'])[2]");

    /**
     * Добавить - Настройки доступа - Добавить - Уровень указания медицинской помощи
     */
    public By MOAdd = By.xpath("(//input[@placeholder='Введите название мед. организации'])[2]");

    /**
     * Добавить - Настройки доступа - Добавить - Добавить
     */
    public By ADDDuble = By.xpath("//button[@tooltiptext='Добавить']");

    /**
     * Добавить - Настройки доступа - удалить 1 доступ
     */
    public By DeleteLevel = By.xpath("//div[@title='Услуги']//button[@text='Удалить данный пункт?']");

    /**
     * Добавить - Настройки доступа - удалить 1 доступ - Да удалить
     */
    public By DeleteLevelYes = By.xpath("//div[@x-placement]//button[1]");

    /**
     * Добавить - Следующий подэтап ОМП
     */
    public By NextPodOMP = By.xpath("//label[contains(.,'Следующий подэтап ОМП')]/following-sibling::div");

    /**
     * Добавить - Следующий подэтап ОМП - Размер текста
     */
    @FindBy(xpath = "//label[@for='routePeriodSettings[0].substageStart']")
    public WebElement NextPodSize;

    /**
     * Добавить - Нормативный срок (в днях)
     */
    public By NormTime = By.xpath("//input[@placeholder='Введите нормативный срок (в днях)']");

    /**
     * Добавить - Нормативный срок (в днях) - Размер текста
     */
    @FindBy(xpath = "//label[@for='routePeriodSettings[0].period']")
    public WebElement NormTimeSize;

    /**
     * Добавить - Название первого Селекта
     */
    public By SelectNameFirst = By.xpath("//div[@x-placement]//ul/li[1]/span");

    /**
     * Добавить - Название Второго Селекта
     */
    public By SelectNameSecond = By.xpath("//div[@x-placement]//ul/li[2]/span");

    /**
     * Добавить - Название Последнего Селекта
     */
    public By SelectNameLast = By.xpath("//div[@x-placement]//ul/li[last()]/span");

    /**
     * Добавить - Добавить
     */
    public By AddTwo = By.xpath("//footer/button/span[contains(.,'Добавить')]");

    /**
     * Обновить
     */
    public By Update = By.xpath("//footer//button[contains(.,'Обновить')]");
}
