package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Роли_доступа")
@Tag("Редактирование_пользователя")
@Tag("Роли_доступа")
public class Access_1899Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    AcceessRoles acceessRoles;
    Users users;

    @Issue(value = "TEL-1899")
    @Link(name = "ТМС-1794", url = "https://team-1okm.testit.software/projects/5/tests/1794?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Добавляем новую роль - создаём пользователя - проверяем, что можно добавить роль пользователю")
    @Test
    @DisplayName("Выбор добавленной роли")
    public void Access_1899() throws InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        acceessRoles = new AcceessRoles(driver);
        users = new Users(driver);

        System.out.println("Генерируем Снилс");
        authorizationObject.GenerateSnilsMethod();

        System.out.println("Добавляем новую роль");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(acceessRoles.RolesWait1);

        Integer number = 1;
        while (isElementNotVisible(acceessRoles.RoleTrue("Тест1899")) == false) {
            if (isElementNotVisible(acceessRoles.NextDisabled) == true) {
                number = 0;
                break;
            } else {
                ClickElement(acceessRoles.NextWait);
            }
        }

        if (number == 0) {
            ClickElement(acceessRoles.AddRoles);
            WaitElement(acceessRoles.NameRoleText);
            inputWord(driver.findElement(acceessRoles.NameRoleText), "Тест1899 ");
            ClickElement(acceessRoles.Check("Доступ к разделу \"Нозологические регистры\""));
            ClickElement(acceessRoles.Add);
            Thread.sleep(1500);
        }

        System.out.println("Добавляем пользователя");
        ClickElement(users.UsersWait);
        WaitElement(users.HeaderUsersWait);
        ClickElement(users.AddUserWait);
        Thread.sleep(2500);
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        Thread.sleep(2500);
        AddUsersMethod(users.WorkYatskiv, users.Accounting,
                By.xpath("//ul[@class='el-scrollbar__view el-select-dropdown__list']/li/span[contains(.,'Тест1899')]"),
                "");

        System.out.println("Убираем выбранную роль");
        ClickElement(users.UsersWait);
        ClickElement(users.AddUserWait);
        Thread.sleep(2500);
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
        WaitElement(users.SnilsInputWait);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        ClickElement(users.BRBEditWaitFirst);
        ClickElement(users.SelectRoleUser);
        ClickElement(users.RoleUser5);
        ClickElement(users.DateDismiss);
        ClickElement(users.DateDismissToday);
        ClickElement(users.UpdateWorkWait);
        ClickElement(users.UpdateWait);
        Thread.sleep(1500);

        System.out.println("Переходим в роли и удаляем роль");
        ClickElement(acceessRoles.RolesWait1);
        while (!isElementNotVisible(By.xpath("(//table//tbody//tr//span[contains(.,'Тест1899')])[1]"))) {
            acceessRoles.Next.click();
        }
        ClickElement(By.xpath(
                "(//table//tbody//td[contains(.,'Тест1899')])[1]/following-sibling::td//button[@text='Удалить данный пункт?']"));
        ClickElement(acceessRoles.YesDelete);
        Thread.sleep(1500);
    }
}