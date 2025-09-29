package UI.TmTest.PageObject.Administration;

import Base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;


public class AuditRoles extends BaseTest {

    /**
     * Аудит ролей
     */
    public AuditRoles(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Метод для выбора действий в аудите (AuditUsers)
     */
    public static void AuditAction(By locator) throws InterruptedException {
        AuditUsers auditUsers = new AuditUsers(driver);

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
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        if (isElementVisible(ActionSearch)) {
            System.out.println(Name + " - работает");
            Assert = "1";
        }
        Assertions.assertEquals(Assert, "1");
    }

    /**
     * Администрирование - Аудит ролей
     */
    public By AuditRoles = By.xpath("//a[@href='/permission/roles-audit']");

    /**
     * Администрирование - С какого числа
     */
    public By DataStart = By.xpath("//input[@placeholder='C какого числа ']");

    /**
     * Администрирование - С какого числа - Текущая дата
     */
    public By DataToday = By.xpath("//div[@x-placement='bottom-start']//td[@class='available today']");

    /**
     * Действие - Изменение доступов роли
     */
    public By AccessRoles = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Изменение доступов роли')]");
    public By AccessRolesWait = By.xpath(
            "(//tr[@class='el-table__row']/td[4]//span[contains(.,'Изменение доступов роли')])[1]");

    /**
     * Действие - Добавление доступов роли
     */
    public By AddAccessRoles = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Добавление доступов роли')]");
    public By AddAccessRolesWait = By.xpath(
            "(//tr[@class='el-table__row']/td[4]//span[contains(.,'Добавление доступов роли')])[1]");

    /**
     * Действие - Добавление роли
     */
    public By AddRoles = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Добавление роли')]");
    public By AddRolesWait = By.xpath("(//tr[@class='el-table__row']/td[4]//span[contains(.,'Добавление роли')])[1]");

    /**
     * Действие - Удаление роли
     */
    public By DeleteRoles = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Удаление роли')]");
    public By DeleteRolesWait = By.xpath("(//tr[@class='el-table__row']/td[4]//span[contains(.,'Удаление роли')])[1]");

    /**
     * Действие - Изменение роли
     */
    public By UpdateRoles = By.xpath("//div[@x-placement]//ul/li/span[contains(.,'Изменение роли')]");
    public By UpdateRolesWait = By.xpath("(//tr[@class='el-table__row']/td[4]//span[contains(.,'Изменение роли')])[1]");


}
