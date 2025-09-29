package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Редактирование_пользователя")
@Tag("Основные")
public class Access_1147Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    Users users;

    @Issue(value = "TEL-1147")
    @Link(name = "ТМС-1481", url = "https://team-1okm.testit.software/projects/5/tests/1481?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отсутствия роли в редактировании пользователя при отключении видимости")
    @Description("Убираем отображение ролей и Авторизуемся под разными пользователями, проверяем, что роли не отображаются")
    public void Access_1147() throws  SQLException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);
        String mo = "";
        if (KingNumber == 12) {
            mo = "ГБУЗ \"Чукотская окружная больница\" г. Анадырь";
        } else {
            mo = "БУ ХМАО-Югры \"Окружная клиническая больница\"";
        }

        System.out.println("Генерируем снилс для нового пользователя");
        authorizationObject.GenerateSnilsMethod();
        System.out.println("Убираем видимость ролей");
        sql.UpdateConnection("update telmed.accessroles set isavailable = '0' where name = '"+PRole+"';");
        sql.UpdateConnection("update telmed.accessroles set isavailable = '0' where name = 'Консультант';");

        System.out.println("Авторизуемся под пользователем с полным доступом и переходим в добавление нового пользователя");
        AuthorizationMethod(authorizationObject.Select(mo));

        ClickElement(users.UsersWait);
        WaitElement(users.HeaderUsersWait);
        users.AddUser.click();
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);

        System.out.println("Переходим в добавление места работы и проверяем, что нет 2 убранных ролей");
        if (KingNumber == 12) {
            authorizationObject.LoadingTime(150);
        }
        ClickElement(users.AddWorkWait);
        ClickElement(users.SelectRoleUser);
        WaitNotElement(users.RoleUserPoln);
        WaitNotElement(users.RoleUserKonsult);
        ClickElement(users.SelectRoleUser);
        ClickElement(users.WorkClose);
        Thread.sleep(1500);
        ClickElement(users.CloseWait);

        System.out.println("Добавляем видимость Полного доступа и добавляем пользователя с этим доступом");
        sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = '"+PRole+"';");
        AuthorizationMethod(authorizationObject.Select(mo));
        ClickElement(users.UsersWait);
        WaitElement(users.HeaderUsersWait);
        users.AddUser.click();
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 20);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);
        AddUsersMethod(users.WorkOkb, users.Accounting, users.RoleUserPoln, "");
        System.out.println("Новый пользователь добавлен в ИЭМК");

        System.out.println("Снова убираем видимость Полного доступа и переходим к добавленному пользователю");
        sql.UpdateConnection("update telmed.accessroles set isavailable = '0' where name = '"+PRole+"';");
        AuthorizationMethod(authorizationObject.Select(mo));
        ClickElement(users.UsersWait);
        WaitElement(users.HeaderUsersWait);
        users.AddUser.click();
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);

        System.out.println("Переходим в добавление места работы и проверяем, что нет роли Консультант");
        ClickElement(users.BRBEditWaitFirst);
        ClickElement(users.SelectRoleUser);
        WaitElement(users.RoleUserPoln);
        WaitNotElement(users.RoleUserKonsult);
        ClickElement(users.RoleUser5);

        System.out.println("Выбираем роль отличную от Консультант и "+PLastNameGlobal+" и проверяем, что недоступны обе. После удаляем созданного пользователя");
        ClickElement(users.SelectRoleUser);
        WaitNotElement(users.RoleUserPoln);
        WaitNotElement(users.RoleUserKonsult);
        ClickElement(users.RoleUser5);
        SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);
        users.UpdateWork.click();
        WaitNotElement(users.EditWait);
        WaitElement(users.SnilsInputWait);
        actionElementAndClick(users.Update);
        sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = '"+PRole+"';");
        sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = 'Консультант';");
    }
}
