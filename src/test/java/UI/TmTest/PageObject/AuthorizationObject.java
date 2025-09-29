package UI.TmTest.PageObject;

import Base.BaseTest;
import UI.TmTest.PageObject.Administration.Users;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class AuthorizationObject extends BaseTest {

    public AuthorizationObject(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * БУ ХМАО-Югры МИАЦ
     */
    @FindBy(xpath = "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li[@class='el-select-dropdown__item']/span[contains(.,'БУ ХМАО-Югры МИАЦ')]")
    public By MIAC = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li[@class='el-select-dropdown__item']/span[contains(.,'БУ ХМАО-Югры МИАЦ')]");

    /**
     * БУ ХМАО-Югры "Нефтеюганская окружная клиническая больница имени В.И. Яцкив"
     */
    @FindBy(xpath = "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"')]")
    public WebElement Yatckiv;
    public By YATCKIV = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li[@class='el-select-dropdown__item']/span[contains(.,'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"')]");

    /**
     * БУ ХМАО-Югры МИАЦ Окружная клиническая больница
     */
    @FindBy(xpath = "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'БУ ХМАО-Югры \"Окружная клиническая больница\"')]")
    public WebElement Okb;
    public By OKB = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'БУ ХМАО-Югры \"Окружная клиническая больница\"')]");
    /**
     * БУ ХМАО-Югры МИАЦ АУ ХМАО-Югры "Кондинская районная стоматологическая поликлиника"
     */
    public By Kondinsk = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'АУ ХМАО-Югры \"Кондинская районная стоматологическая поликлиника\"')]");

    /**
     * БУ ХМАО-Югры МИАЦ Белоярская районная больница
     */
    @FindBy(xpath = "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li[@class='el-select-dropdown__item']/span[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]")
    public WebElement BRB;
    public By BRBWait = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li[@class='el-select-dropdown__item']/span[contains(.,'БУ ХМАО-Югры \"Белоярская районная больница\"')]");

    /**
     * БУ ХМАО-Югры МИАЦ
     */
    @FindBy(xpath = "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li[@class='el-select-dropdown__item']/span[contains(.,'БУ ХМАО-Югры МИАЦ')]")
    public By Seva = By.xpath(
            "//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'МИАЦ г. Севастополь')]");

    /**
     * Селект
     */
    @FindBy(xpath = "//span[@class='el-input__suffix']")
    public WebElement Select;
    public By Selected = By.xpath("//span[@class='el-input__suffix']");

    /**
     * Селект - Выпадающий список
     */
    public By SelectWait = By.cssSelector(".el-scrollbar__view");

    /**
     * Выбрать
     */
    @FindBy(xpath = "//button[contains(.,'Выбрать')]")
    public WebElement Сhoose;

    /**
     * Select
     */
    public By SelectFirst = By.xpath("//div[@x-placement]//ul/li[1]");
    public By SelectSecond = By.xpath("//div[@x-placement]//ul/li[2]");
    public By SelectLast = By.xpath("//div[@x-placement]//ul/li[last()]");
    public By SelectALL = By.xpath("//div[@x-placement]//ul/li/span");
    public By Select (String value) {
        By Select = By.xpath("//div[@x-placement]//ul/li[contains(.,'"+value+"')]");
        return Select;
    }

    /**
     * Bottom-start
     */
    public By BottomStart = By.xpath("//div[@x-placement='bottom-start']");

    /**
     * Bottom-start
     */
    public By Xplacement = By.xpath("//div[@x-placement]");

    /**
     * Top-start
     */
    public By TopStart = By.xpath("//div[@x-placement='top-start']");

    /**
     * Alert "Что нового"
     */
    public By Alert = By.xpath("//h1[contains(.,'Что нового')]");

    /**
     * Alert "Пердупреждение"
     */
    public By AlertWarning = By.xpath("//h2[contains(.,'Предупреждение')]");

    /**
     * Алерт Нужный
     */
    public By AlertTrue (String value) {
        By True = By.xpath("//div[@role='alert'][contains(.,'"+value+"')]");
        return True;
    }

    /**
     * Alert "Что нового" - Ок
     */
    @FindBy(xpath = "//footer//span[contains(.,'Ок')]")
    public WebElement OK;
    public By OKWait = By.xpath("//footer//span[contains(.,'Ок')]");

    /**
     * Следующая страница (недоступно)
     */
    public By NextDisable = By.xpath("//button[@class='btn-next'][@disabled='disabled']");

    /**
     * Следующая страница
     */
    public By Next = By.xpath("//button[@class='btn-next']");

    /**
     * Поиск - Загрузка
     */
    public By Loading = By.xpath("(//div[@class='el-loading-spinner'])[2]");

    /**
     * Поиск - Загрузка 2
     */
    public By Loading2 = By.xpath("//i[@class='el-icon-loading']");

    /**
     * Поиск - Загрузка - Нужная
     */
    public By LoadingTrue (String value) {
        By True = By.xpath("(//div[@class='el-loading-spinner'])["+value+"]");
        return True;
    }

    /**
     * Поиск - Загрузка - Ждём пока закончится любая загрузка
     */
    public void LoadingTime (Integer time) {
        WaitNotElement3(LoadingTrue("1"),time);
        WaitNotElement3(LoadingTrue("2"),time);
        WaitNotElement3(LoadingTrue("3"),time);
        WaitNotElement3(Loading2,time);
    }

    /**
     * Нужная кнопка
     */
    public By ButtonTrue (String value, String number) {
        By True = By.xpath("(//button[contains(.,'"+value+"')])["+number+"]");
        return True;
    }

    /**
     * --------------------------Локаторы для сайта генерации снилса----------------------
     */
    public String GenerationSnils;

    @Step("Метод Генерации снилса для добавления пользователя")
    public void GenerateSnilsMethod() {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        /** Генерация снилса для добавления пользователя */
        System.out.println("Генерация снилса для добавления пользователя");
        driver.get(GENERATE_SNILS);
        WaitElement(authorizationObject.ButtonNewNumberWait);
        authorizationObject.ButtonNewNumber.click();
        String number = authorizationObject.Snils.getText();
        System.out.println("Новый СНИЛС: " + number);
        GenerationSnils = number;
    }

    @Step("Метод Генерации снилса для добавления пользователя, Авторизация, переход в пользователи и ввод сгенерированного СНИЛС")
    public void GenerationSnilsAndAuthorizationMethod() throws InterruptedException {
        Users users = new Users(driver);
        System.out.println("Генерация снилса для добавления пользователя");
        driver.get(GENERATE_SNILS);
        ClickElement(ButtonNewNumberWait);
        GenerationSnils = Snils.getText();
        System.out.println("Новый СНИЛС: " + GenerationSnils);
        AuthorizationMethod(OKB);
        ClickElement(users.UsersWait);
        ClickElement(users.AddUserWait);
        users.InputSnils.sendKeys(GenerationSnils);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);
    }

    @Step("Метод преобразования снилса в нужный формат")
    public String SnilsFormat(String str) {
        //779 038 580 58
        String str1 = str.substring(0, 3);
        String str2 = str.substring(0, str.length() - 7).substring(4);
        String str3 = str.substring(0, str.length() - 3).substring(8);
        String str4 = str.substring(12);
        String str5 = str1 + "-" + str2 + "-" + str3 + " " + str4;
        return  str5;
    }

    /**
     * Новое значение
     */
    @FindBy(xpath = "//button[contains(.,'Новое значение')]")
    public WebElement ButtonNewNumber;
    public By ButtonNewNumberWait = By.xpath("//button[contains(.,'Новое значение')]");

    /**
     * CНИЛС
     */
    @FindBy(css = ".number")
    public WebElement Snils;

    /**
     * --------------------------Локаторы для health----------------------
     */
    public String addressTWApi;
    public String addressApi;
    public String addressHubs;
    public String addressUserStatuses;
    public String addressVWAPI;
    public String addressVAPI;

    /**
     * Метод проверяющий, что все элементы прогрузились
     */
    public void WaitIntegrationServicesMethod() {
        WaitElement(WebApi);
        WaitElement(DataBase);
        WaitElement(IntegrationServicesWaitTWAPI);
        WaitElement(IntegrationServicesWaitAPI);
        WaitElement(IntegrationServicesWaitHubs);
        WaitElement(IntegrationServicesWaitUserStatuses);
        WaitElement(IntegrationServicesWaitVWAPI);
        WaitElement(IntegrationServicesWaitVAPI);
    }

    /**
     * Метод, который берёт значения из адресов интеграционных сервисов
     */
    public void GetIntegrationServicesMethod() {
        addressTWApi = IntegrationServicesTWAPI.getText();
        addressApi = IntegrationServicesAPI.getText();
        addressHubs = IntegrationServicesHubs.getText();
        addressUserStatuses = IntegrationServicesUserStatuses.getText();
        addressVWAPI = IntegrationServicesVWAPI.getText();
        addressVAPI = IntegrationServicesVAPI.getText();
    }

    public String GetHealth = "https://tm-test.pkzdrav.ru/health";
    public String GetHealthDev = "https://tm-dev.pkzdrav.ru/health";
    public String GetHealthHMAO = "https://remotecons-test.miacugra.ru/health";

    /**
     * Телемедицина WebApi
     */
    public By WebApi = By.xpath("//h2[contains(.,'Телемедицина WebApi')]");

    /**
     * База данных
     */
    public By DataBase = By.xpath("(//tbody//tr[contains(.,'База данных')])[1]");

    /**
     * Интеграционные сервисы Телемедицина WebApi
     */
    @FindBy(xpath = "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[1]//tr[contains(.,'Доступность сервиса Kibana')]/td[3]")
    public WebElement IntegrationServicesTWAPI;
    public By IntegrationServicesWaitTWAPI = By.xpath(
            "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[1]//tr[contains(.,'Доступность сервиса Kibana')]");

    /**
     * Интеграционные сервисы Телемедицина Api
     */
    @FindBy(xpath = "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[3]//tr[contains(.,'Доступность сервиса Kibana')]/td[3]")
    public WebElement IntegrationServicesAPI;
    public By IntegrationServicesWaitAPI = By.xpath(
            "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[3]//tr[contains(.,'Доступность сервиса Kibana')]");

    /**
     * Интеграционные сервисы Телемедицина Hubs
     */
    @FindBy(xpath = "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[5]//tr[contains(.,'Доступность сервиса Kibana')]/td[3]")
    public WebElement IntegrationServicesHubs;
    public By IntegrationServicesWaitHubs = By.xpath(
            "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[5]//tr[contains(.,'Доступность сервиса Kibana')]");

    /**
     * Интеграционные сервисы Телемедицина UserStatuses
     */
    @FindBy(xpath = "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[7]//tr[contains(.,'Доступность сервиса Kibana')]/td[3]")
    public WebElement IntegrationServicesUserStatuses;
    public By IntegrationServicesWaitUserStatuses = By.xpath(
            "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[7]//tr[contains(.,'Доступность сервиса Kibana')]");

    /**
     * Интеграционные сервисы Вимис WebApi
     */
    @FindBy(xpath = "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[9]//tr[contains(.,'Доступность RRP сервиса')]/td[3]")
    public WebElement IntegrationServicesVWAPI;
    public By IntegrationServicesWaitVWAPI = By.xpath(
            "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[9]//tr[contains(.,'Доступность RRP сервиса')]");

    /**
     * Интеграционные сервисы Вимис Api
     */
    @FindBy(xpath = "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[11]//tr[contains(.,'Доступность RRP сервиса')]/td[3]")
    public WebElement IntegrationServicesVAPI;
    public By IntegrationServicesWaitVAPI = By.xpath(
            "(//h3[.='Интеграционные сервисы:']/following-sibling::div)[11]//tr[contains(.,'Доступность RRP сервиса')]");
}
