package UI.TmTest.PageObject.Administration;

import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class AuditUsers extends BaseTest {
    public AuditUsers(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Метод для выбора действий в аудите (AuditUsers)
     */
    public static void AuditAction(By locator) throws InterruptedException {
        AuditUsers auditUsers = new AuditUsers(driver);
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        ClickElement(auditUsers.ChoiceActionWait);
        Thread.sleep(2000);
        actions.moveToElement(driver.findElement(locator));
        actions.perform();
        Thread.sleep(1000);
        driver.findElement(locator).click();
        ClickElement(auditUsers.SearchWait);
    }

    /**
     * Метод для использоания метода AuditAction с проверкой отображения данных в таблице
     */
    public static void AssertAudit(By Action, By ActionSearch, String Name) throws InterruptedException {
        AuditUsers auditUsers = new AuditUsers(driver);
        String Assert = "0";
        AuditAction(Action);
        Thread.sleep(1500);
        WaitNotElement3(auditUsers.Loading, 15);
        if (isElementVisible(ActionSearch)) {
            System.out.println(Name + " - работает");
            Assert = "1";
        }
        Assertions.assertEquals(Assert, "1", ""+Name+" не отображается");
    }

    /**
     * Администрирование - Аудит пользователя
     */
    @FindBy(xpath = "//a[@href='/permission/users-audit']")
    public WebElement AuditUsers;
    public By AuditUsersWait = By.linkText("Аудит пользователей");

    /**
     * Аудит пользователя - Заголовок
     */
    public By HeaderAuditUsersWait = By.cssSelector("#UsersAudit");

    /**
     * Аудит пользователя - С какого числа
     */
    @FindBy(xpath = "//input[@placeholder='C какого числа ']")
    public WebElement DataStart;
    public By DataStartWait = By.xpath("//input[@placeholder='C какого числа ']");

    /**
     * Аудит пользователя - Текущее число
     */
    public By DataToday = By.xpath(
            "//div[@x-placement]//td[@class='available today']");
    public By DataTodayEnd = By.xpath(
            "//div[@x-placement]//td[@class='available today in-range start-date end-date']");
    public By DataTodayAnotherEnd = By.xpath(
            "//div[@x-placement]//td[@class='available today in-range end-date']");

    /**
     * Аудит пользователя - Выберите действие
     */
    @FindBy(xpath = "//input[@placeholder='Выберите действие']")
    public WebElement ChoiceAction;
    public By ChoiceActionWait = By.xpath("//input[@placeholder='Выберите действие']");

    /**
     * Аудит пользователя - Поиск
     */
    @FindBy(xpath = "//button[contains(.,'Поиск')]")
    public WebElement Search;
    public By SearchWait = By.xpath("//button[contains(.,'Поиск')]");

    /**
     * Аудит пользователя - Поиск - Загрузка результатов
     */
    public By Loading = By.xpath("//div[@class='grid-table']//div[@class='el-loading-spinner']");

    /**
     * Поиск - Создание пользователя
     */
    public By CreateUser = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Создание пользователя')]");

    public By CreateUserWait = By.xpath(
            "(//tr[@class='el-table__row']/td[4]//span[contains(.,'Создание пользователя')])[1]");

    /**
     * Поиск - Добавление места работы пользователю
     */
    public By AddWork = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Добавление места работы пользователю')]");
    public By AddWorkWait = By.xpath(
            "(//tr[@class='el-table__row']/td[4]//span[contains(.,'Добавление места работы пользователю')])[1]");

    /**
     * Поиск - Добавление роли пользователю
     */
    public By AddRole = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Добавление роли пользователю')]");
    public By AddRoleWait = By.xpath(
            "(//tr[@class='el-table__row']/td[4]//span[contains(.,'Добавление роли пользователю')])[1]");

    /**
     * Поиск - Добавление профиля пользователю
     */
    public By AddProfile = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Добавление профиля пользователю')]");
    public By AddProfileWait = By.xpath(
            "(//tr[@class='el-table__row']/td[4]//span[contains(.,'Добавление профиля пользователю')])[1]");

    /**
     * Поиск - Увольнение пользователя по профилю
     */
    public By DismissProfile = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'Увольнение пользователя по профилю')]");
    public By DismissProfileWait = By.xpath(
            "(//tr[@class='el-table__row']/td[4]//span[contains(.,'Увольнение пользователя по профилю')])[1]");

    /**
     * Поиск - Увольнение пользователя с места работы
     */
    public By DismissWork = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'Увольнение пользователя с места работы')]");
    public By DismissWorkWait = By.xpath(
            "(//tr[@class='el-table__row']/td[4]//span[contains(.,'Увольнение пользователя с места работы')])[1]");

    /**
     * Поиск - Редактирование роли пользователю
     */
    public By EditRole = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Редактирование роли пользователю')]");
    public By EditRoleWait = By.xpath(
            "(//tr[@class='el-table__row']/td[4]//span[contains(.,'Редактирование роли пользователю')])[1]");

    /**
     * Поиск - Редактирование профиля пользователю
     */
    public By EditProfile = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'Редактирование профиля пользователю')]");
    public By EditProfileWait = By.xpath(
            "(//tr[@class='el-table__row']/td[4]//span[contains(.,'Редактирование профиля пользователю')])[1]");

    /**
     * Поиск - Редактирование места работы пользователю
     */
    public By EditWork = By.xpath(
            "//div[@x-placement]//ul/li/span[contains(.,'Редактирование места работы пользователю')]");
    public By EditWorkWait = By.xpath(
            "(//tr[@class='el-table__row']/td[4]//span[contains(.,'Редактирование места работы пользователю')])[1]");
}