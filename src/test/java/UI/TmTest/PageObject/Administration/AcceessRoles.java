package UI.TmTest.PageObject.Administration;

import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AcceessRoles extends BaseTest {

    public AcceessRoles(WebDriver driver) {
        BaseTest.driver = (RemoteWebDriver) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Администрирование - Роли доступа
     */
    @FindBy(xpath = "//a[@href='/permission/roles']")
    public WebElement Roles;
    public By RolesWait = By.linkText("Роли доступа");
    public By RolesWait1 = By.xpath("//a[@href='/permission/roles']");

    /**
     * Заголовок Роли доступа
     */
    public By HeaderRoles = By.xpath("//h1[.='Роли доступа']");

    /**
     * Роли доступа - Добавить роль
     */
    public By AddRoles = By.xpath("//button/span[contains(.,'Добавить роль')]");

    /**
     * Роли доступа - Согласие на удаление
     */
    public By YesDelete = By.xpath(
            "//div[@role='tooltip'][@x-placement]/p[contains(.,'Удалить данный пункт?')]/following-sibling::div/button[1]");

    /**
     * Роль - Нужная роль
     */
    public By RoleTrue (String value) {
        By RoleTrue = By.xpath("(//table//tbody//tr//span[contains(.,'"+value+"')])[1]");
        return RoleTrue;
    }

    /**
     * Роль - Администратор
     */
    public By RoleAdministrator = By.xpath("(//table//tbody//tr//span[contains(.,'Администратор')])[1]");

    /**
     * Роль - Тестировщик
     */
    public By RoleTester = By.xpath("(//table//tbody//tr//span[contains(.,'"+PRole+"')])[1]");

    /**
     * Роль - Тестовая роль 213
     */
    public By RoleTest213 = By.xpath("(//table//tbody//tr//span[contains(.,'Тестовая роль 213')])[1]");

    /**
     * Роль - Тестовая роль 2
     */
    public By RoleTest2 = By.xpath("(//table//tbody//tr//span[contains(.,'Tестовая роль 2')])[1]");

    /**
     * Роль - Тестовая роль
     */
    public By RoleTest1 = By.xpath("(//table//tbody//tr//span[contains(.,'Тестовая роль')])[1]");

    /**
     * Роль - Тестовая 999
     */
    public By RoleTest999 = By.xpath("//table//tbody//tr//span[contains(.,'Тестовая 999')]");

    /**
     * Роль - Редактировать - Нужная роль
     */
    public By EditTrue (String value) {
        By EditTrue = By.xpath("(//table//tbody//td[contains(.,'"+value+"')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']");
        return EditTrue;
    }

    /**
     * Роль - Тестовая роль 213 - Редактировать
     */
    @FindBy(xpath = "(//table//tbody//td[contains(.,'Тестовая роль 213')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement EditTest;

    /**
     * Роль - Тестовая роль 2 - Редактировать
     */
    @FindBy(xpath = "(//table//tbody//td[contains(.,'Tестовая роль 2')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement EditTest2;

    /**
     * Роль - Тестовая роль - Редактировать
     */
    @FindBy(xpath = "(//table//tbody//td[contains(.,'Тестовая роль')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement EditTest1;

    /**
     * Роль - Тестовая роль 999 - Редактировать
     */
    @FindBy(xpath = "//table//tbody//td[contains(.,'Тестовая 999')]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement EditTest999;

    /**
     * Следующая страница
     */
    @FindBy(xpath = "//button[@class='btn-next']")
    public WebElement Next;
    public By NextWait = By.xpath("//button[@class='btn-next']");

    /**
     * Следующая страница - неактивная кнопка
     */
    public By NextDisabled = By.xpath("//button[@class='btn-next'][@disabled='disabled']");

    /**
     * Роль - Администратор - Редактировать
     */
    @FindBy(xpath = "(//table//tbody//td[contains(.,'Администратор')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement Edit;

    /**
     * Роль - Тестировщик - Редактировать
     */
    @FindBy(xpath = "(//table//tbody//td[contains(.,'Тестировщик')])[1]/following-sibling::td//button[@tooltiptext='Редактировать']")
    public WebElement EditPolny;

    /**
     * Заголовок редактирование роли
     */
    public By EditRole = By.xpath("//h3['Редактирование роли']");

    /**
     * Редактировать - Наименование роли
     */
    public By NameRole = By.xpath("//label[contains(.,'Наименование роли')]");

    /**
     * Роль - Наименование роли
     */
    public By NameRoleText = By.xpath("//input[@placeholder='Введите наименование роли']");

    /**
     * Роль - Описание роли
     */
    public By DescriptionRoleText = By.xpath("//textarea[@placeholder='Введите описание роли']");

    /**
     * Введите текст
     */
    @FindBy(xpath = "//input[@placeholder='Введите текст']")
    public WebElement InputWord;
    public By InputWordWait = By.xpath("//input[@placeholder='Введите текст']");

    /**
     * Доступ - Нужный доступ
     */
    public  By Access (String value) {
        By Access = By.xpath(
                "//section//div[@class='el-tree-node__content']//span[contains(.,'"+value+"')]");
        return Access;
    }

    /**
     * Чек бокс - активный
     */
    public  By Check (String value) {
        By Access = By.xpath(
                "//span[contains(.,'"+value+"')]/preceding-sibling::label/span");
        return Access;
    }

    /**
     * Чек бокс - не активный
     */
    public  By CheckDisable (String value) {
        By Access = By.xpath(
                "//section//div[@class='el-tree-node__content']//span[contains(.,'"+value+"')]/preceding-sibling::label[@class='el-checkbox']/span");
        return Access;
    }

    /**
     * Чек бокс - активный
     */
    public  By CheckActive (String value) {
        By Access = By.xpath(
                "//section//div[@class='el-tree-node__content']//span[contains(.,'"+value+"')]/preceding-sibling::label[@class='el-checkbox is-checked']/span");
        return Access;
    }

    /**
     * Обновить
     */
    @FindBy(xpath = "//button/span[contains(.,'Обновить')]")
    public WebElement Update;
    public By UpdateWait = By.xpath("//button/span[contains(.,'Обновить')]");

    /**
     * Добавить
     */
    public By Add = By.xpath("(//button/span[contains(.,'Добавить')])[2]");

    /**
     * Закрыть
     */
    @FindBy(xpath = "//button/span[contains(.,'Закрыть')]")
    public WebElement Close;
    public By CloseBy = By.xpath("//button/span[contains(.,'Закрыть')]");

    /**
     * Метод для очистки Cooke
     */
    public void Cooke() {
        /** Очищаем Cookie */
        Cookie cookie = driver.manage().getCookieNamed(".AspNetCore.Session");
        Cookie cookie1 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC1");
        Cookie cookie2 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC2");
        Cookie cookie3 = driver.manage().getCookieNamed(".AspNet.Core.Telemed");
        driver.manage().deleteCookie(cookie);
        driver.manage().deleteCookie(cookie1);
        driver.manage().deleteCookie(cookie2);
        driver.manage().deleteCookie(cookie3);
    }

    /**
     * Метод для Перехода в роли доступа и открытие редактирование для роли "Тестовая роль 213"
     */
    public void OpenRole() throws InterruptedException {
        AcceessRoles acceessRoles = new AcceessRoles(driver);

        ClickElement(acceessRoles.RolesWait);
        WaitElement(acceessRoles.HeaderRoles);
        if (KingNumber == 1) {
            while (!isElementNotVisible(acceessRoles.RoleTest213)) {
                acceessRoles.Next.click();
            }
            actionElementAndClick(acceessRoles.EditTest);
        }
        if (KingNumber == 2) {
            while (!isElementNotVisible(acceessRoles.RoleTest1)) {
                acceessRoles.Next.click();
            }
            actionElementAndClick(acceessRoles.EditTest1);
        }
        if (KingNumber == 4) {
            while (!isElementNotVisible(acceessRoles.RoleTest999)) {
                acceessRoles.Next.click();
            }
            actionElementAndClick(acceessRoles.EditTest999);
        }
        WaitElement(acceessRoles.EditRole);
        WaitElement(acceessRoles.InputWordWait);
    }

    @Step("Добавление/отключение доступа {0}")
    public void AccessAdd(String accessWord, boolean add) throws InterruptedException {

        AcceessRoles acceessRoles = new AcceessRoles(driver);
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);

        inputWord(acceessRoles.InputWord, accessWord);
        WaitNotElement3(authorizationObject.LoadingTrue("3"), 20);
        WaitElement(acceessRoles.Access(accessWord));
        String Class = String.valueOf(driver.findElement((By.xpath(
                "//section//div[@class='el-tree-node__content']//span[contains(.,'" + accessWord + "')]/preceding-sibling::label"))).getAttribute("class"));
        if (add & !Class.equals("el-checkbox is-checked")) {
            ClickElement(By.xpath("//section//div[@class='el-tree-node__content']//span[contains(.,'" + accessWord + "')]/preceding-sibling::label[@class='el-checkbox']"));
        }
        if (!add & Class.equals("el-checkbox is-checked")) {
            ClickElement(By.xpath(
                    "//section//div[@class='el-tree-node__content']//span[contains(.,'" + accessWord + "')]/preceding-sibling::label[@class='el-checkbox is-checked']"));
        }
        /** Нужно для заявки (2552) */
        if (accessWord.contains("Доступ к разделу \"Расписание консультаций\"")) {
            ClickElement(acceessRoles.CheckDisable(accessWord));
            Thread.sleep(1500);
            ClickElement(acceessRoles.CheckActive(accessWord));
            Thread.sleep(1500);
        }
    }
}
