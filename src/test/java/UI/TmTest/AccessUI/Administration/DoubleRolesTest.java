package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isOneOf;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Редактирование_пользователя")
@Tag("Основные")
public class DoubleRolesTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    Users users;

    @Issue(value = "TEL-581")
    @Link(name = "ТМС-1151", url = "https://team-1okm.testit.software/projects/5/tests/1151")
    @Owner(value = "Галиакберов Тимур")
    @Description("Взять снилс уже добавленного пользователя. Добавляем МО и роль, по которой уволили. Проверка, что роль недоступна")
    @DisplayName("Добавление роли, у которой уже есть дата увольнения")
    @Test
    public void DoubleRole() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);

        System.out.println("Генерируем новый снилс");
        authorizationObject.GenerateSnilsMethod();

        System.out.println("Авторизуемся и добавляем нового пользователя");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(users.UsersWait);
        WaitElement(users.HeaderUsersWait);
        users.AddUser.click();
        Thread.sleep(2500);
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);
        if (KingNumber == 1) {
            AddUsersMethod(users.WorkYatskiv, users.Accounting, users.RoleUserTest213, "");
        }
        if (KingNumber == 2) {
            AddUsersMethod(users.WorkYatskiv, users.Accounting, users.RoleUserTest2, "");
        }
        if (KingNumber == 4) {
            AddUsersMethod(users.WorkYatskiv, users.Accounting, users.RoleUserTest999, "");
        }

        System.out.println("Переходим к созданному пользователю");
        WaitElement(users.AddUserWait);
        Thread.sleep(1000);
        if (KingNumber == 4) {
            Thread.sleep(3000);
        }

        users.AddUser.click();
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);

        System.out.println("Добавляем новое место работы с ролью по которой уволим");
        if (!isElementNotVisible(users.PlaceWaork)) {
            /** Добавление места работы */
            users.AddWork.click();
            WaitElement(users.HeaderAddWork);
            SelectClickMethod(users.SelectWork, users.WorkNGDP);
            Thread.sleep(1500);
            if (KingNumber == 4) {
                Thread.sleep(5000);
            }
            SelectClickMethod(users.SelectDivision, users.Accounting);
            Thread.sleep(1500);
            if (KingNumber == 4) {
                Thread.sleep(5000);
            }
            if (KingNumber == 1) {
                SelectClickMethod(users.SelectRoleUser, users.RoleUserTest213);
            }
            if (KingNumber == 2) {
                SelectClickMethod(users.SelectRoleUser, users.RoleUserTest2);
            }
            if (KingNumber == 4) {
                SelectClickMethod(users.SelectRoleUser, users.RoleUserTest999);
            }
            Thread.sleep(1500);
            if (KingNumber == 4) {
                Thread.sleep(5000);
            }
            SelectClickMethod(users.DateDismiss, users.DateDismissToday);
            users.AddWorkButton.click();
            Thread.sleep(1500);
        }

        System.out.println("Добавляем МО и роль, по которой уволили");
        WaitElement(users.AddUserWait);
        users.AddWork.click();
        WaitElement(users.HeaderAddWork);
        SelectClickMethod(users.SelectWork, users.WorkNGDP);
        SelectClickMethod(users.SelectDivision, users.Accounting);

        System.out.println("Проверка на доступность роли по которой уволили");
        if (KingNumber == 1) {
            SelectClickMethod(users.SelectRoleUser, users.RoleUserTest213);
        }
        if (KingNumber == 2) {
            SelectClickMethod(users.SelectRoleUser, users.RoleUserTest2);
        }
        if (KingNumber == 4) {
            SelectClickMethod(users.SelectRoleUser, users.RoleUserTest999);
        }

        System.out.println("Роль, по которой уволили доступна");
        users.AddWorkButton.click();

        System.out.println("Проверка на недоступность роли, которую добавили");
        WaitElement(users.AddUserWait);
        users.AddWork.click();
        WaitElement(users.HeaderAddWork);
        SelectClickMethod(users.SelectWork, users.WorkNGDP);
        Thread.sleep(1500);
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        SelectClickMethod(users.SelectDivision, users.Accounting);
        Thread.sleep(1500);
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        users.SelectRoleUserWait.click();
        WaitElement(authorizationObject.BottomStart);
        String NameClass = null;
        if (KingNumber == 1) {
            NameClass = driver.findElement(users.ClassNameRole123).getAttribute("class");
        }
        if (KingNumber == 2) {
            NameClass = driver.findElement(users.ClassNameRole2).getAttribute("class");
        }
        if (KingNumber == 4) {
            NameClass = driver.findElement(users.ClassNameRole999).getAttribute("class");
        }
        System.out.println(NameClass);
        assertThat(
                NameClass,
                isOneOf("el-select-dropdown__item is-disabled hover", "el-select-dropdown__item is-disabled")
        );
        assert NameClass.equals("el-select-dropdown__item is-disabled hover") || NameClass.equals(
                "el-select-dropdown__item is-disabled");
        users.SelectRoleUserWait.click();
        ClickElement(users.WorkClose);

        System.out.println("Добавляем дату увольнения всем местам работы");
        List<WebElement> Work = driver.findElements(users.WorkQuantity);
        for (int i = 1; i < Work.size() + 1; i++) {
            String WorkTrue = driver.findElement(By.xpath(
                    "//section/h4[contains(.,'Место работы')]/following-sibling::div//tbody/tr[" + i + "]/td[3]//span")).getText();
            if (WorkTrue == "") {
                ClickElement(By.xpath(
                        "(//section/h4[contains(.,'Место работы')]/following-sibling::div//tbody/tr[" + i + "]/td[5]//span)[1]"));
                ClickElement(users.DateDismiss);
                ClickElement(users.DateDismissBackToday);
                ClickElement(users.UpdateWorkWait);
                Thread.sleep(1500);
            }
        }
        ClickElement(users.UpdateWait);
        Thread.sleep(3000);
    }
}
