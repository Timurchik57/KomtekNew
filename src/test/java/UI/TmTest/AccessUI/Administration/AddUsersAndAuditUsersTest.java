package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.AuditUsers;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static UI.TmTest.PageObject.Administration.AuditUsers.AssertAudit;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Редактирование_пользователя")
@Tag("Аудит_пользователя")
public class AddUsersAndAuditUsersTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    Users users;
    AuditUsers auditUsers;

    @Owner(value = "Галиакберов Тимур")
    @Order(1)
    @DisplayName("Добавление нового пользователя")
    @Description("Генерация снилса для добавления пользователя. Добавление МО, роли и профиля. Проверка, что добавленный профиль отображается. Редактирование МО, роли и профиля с установкой даты увольнения.")
    @Test
    @Story("Проверка Аудита пользователя")
    public void AddUser() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);

        System.out.println("Генерация снилса для добавления пользователя");
        driver.get(GENERATE_SNILS);
        WaitElement(authorizationObject.ButtonNewNumberWait);
        authorizationObject.ButtonNewNumber.click();
        String text = authorizationObject.Snils.getText();
        System.out.println("Новый СНИЛС: " + text);

        /** Авторизация */
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(users.UsersWait);
        WaitElement(users.HeaderUsersWait);
        users.AddUser.click();
        users.InputSnils.sendKeys(text);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);
        AddUsersMethod(users.WorkYatskiv, users.Accounting, users.RoleUser5, "");

        System.out.println("Редактирование роли, профиля и добавление даты увольнения");
        Thread.sleep(1500);
        WaitNotElement3(users.Loading, 20);
        ClickElement(users.AddUserWait);
        WaitElement(users.InputSnilsWait);
        users.InputSnils.sendKeys(text);

        System.out.println("Редактирование существующего пользователя");
        /** Проверка, что добавленный профиль отображается */
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);
        WaitElement(users.OneWait);
        users.Edit.click();

        System.out.println("Изменение данных о месте работы");
        WaitElement(users.EditWait);
        SelectClickMethod(users.SelectWork, users.WorkOkb);
        SelectClickMethod(users.SelectDivision, users.Division5);
        if (KingNumber == 1) {
            SelectClickMethod(users.SelectRoleUser, users.RoleUserTest213);
        }
        if (KingNumber == 2) {
            SelectClickMethod(users.SelectRoleUser, users.RoleUserTest2);
        }
        if (KingNumber == 4) {
            SelectClickMethod(users.SelectRoleUser, users.RoleUserTest999);
        }
        SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);

        /** Обновляем */
        users.UpdateWork.click();
        WaitNotElement(users.EditWait);

        /** Изменение данных о профиле */
        ClickElement(users.ProfileButtonWait);
        WaitElement(users.HeaderProfileWait);
        users.EditProfile.click();
        SelectClickMethod(users.SelectMedProfile, users.MedProfile);
        users.EditProfileDate.click();
        WaitElement(users.BottomStartWait);
        WaitElement(users.EditProfileDateTodayWait);
        ClickElement(users.EditProfileDateTodayWait);
        WaitNotElement(users.BottomStartWait);
        users.EditProfileApply.click();
        Thread.sleep(300);
        users.EditProfileClose.click();
        users.Update.click();
        Thread.sleep(1500);
        System.out.println("Данные пользователя отредактированы");
    }

    @Issue(value = "TEL-443")
    @Link(name = "ТМС-944", url = "https://team-1okm.testit.software/projects/5/tests/944")
    @Owner(value = "Галиакберов Тимур")
    @Order(2)
    @Test
    @Story("Проверка Аудита пользователя")
    @Description("Переход в Аудит пользователя и проверка всех действий")
    @DisplayName("Проверка Аудита пользователя")
    public void AuditUser() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        auditUsers = new AuditUsers(driver);

        System.out.println("Проверка Аудита пользователя");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переход в Аудит пользователя");
        ClickElement(auditUsers.AuditUsersWait);
        WaitElement(auditUsers.HeaderAuditUsersWait);
        ClickElement(auditUsers.DataStartWait);
        ClickElement(auditUsers.DataToday);
        if (!isElementNotVisible(auditUsers.DataTodayEnd)) {
            ClickElement(auditUsers.DataTodayAnotherEnd);
        } else {
            ClickElement(auditUsers.DataTodayEnd);
        }

        System.out.println("Проверка действий");
        AssertAudit(auditUsers.CreateUser, auditUsers.CreateUserWait, "Создание пользователя");
        AssertAudit(auditUsers.AddWork, auditUsers.AddWorkWait, "Добавление места работы пользователю");
        AssertAudit(auditUsers.AddRole, auditUsers.AddRoleWait, "Добавление роли пользователю");
        AssertAudit(auditUsers.AddProfile, auditUsers.AddProfileWait, "Добавление профиля пользователю");
        AssertAudit(auditUsers.DismissProfile, auditUsers.DismissProfileWait, "Увольнение пользователя по профилю");
        AssertAudit(auditUsers.DismissWork, auditUsers.DismissWorkWait, "Увольнение пользователя с места работы");
        AssertAudit(auditUsers.EditRole, auditUsers.EditRoleWait, "Редактирование роли пользователю");
        AssertAudit(auditUsers.EditProfile, auditUsers.EditProfileWait, "Редактирование профиля пользователю");
        AssertAudit(auditUsers.EditWork, auditUsers.EditWorkWait, "Редактирование места работы пользователю");
    }
}



