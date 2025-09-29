package UI.TmTest.AccessUI.Administration;

import Base.*;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListenerChange.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Редактирование_пользователя")
public class EmailDeleteUserTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    Users users;
    String Snils1;

    @Issue(value = "TEL-633")
    @Link(name = "ТМС-1181", url = "https://team-1okm.testit.software/projects/5/tests/1181")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Email удалённого пользователя")
    @Description("Создаём пользователя с email, удаляем пользователя вчерашним числом. Создаём нового пользователя, проверяем, что тот же email можно использовать")
    public void EmailDeleteUser () throws InterruptedException, AWTException, IOException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);

        System.out.println("Проверяем, что нет нужного mail в системе");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(users.UsersWait);
        WaitElement(users.EmailSearch("Введите email адрес"));
        inputWord(driver.findElement(users.EmailSearch("Введите email адрес")), "trewq@mail.ru ");
        ClickElement(users.SearchWait);
        Thread.sleep(1000);
        if (isElementNotVisible(users.FirstUser) == true) {
            Thread.sleep(1500);
            String snils = driver.findElement(users.SnilsWait).getText();

            ClickElement(users.AddUserWait);
            WaitElement(users.InputSnilsWait);
            users.InputSnils.sendKeys(snils);
            Thread.sleep(1500);
            WaitNotElement3(users.LoadingSnils, 20);
            ClickElement(users.ButtonSearchWait);
            WaitElement(users.SnilsInputWait);

            System.out.println("Изменение данных о месте работы");
            ClickElement(users.EditWaitt);
            WaitElement(users.EditWait);
            SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);

            /** Обновляем */
            users.UpdateWork.click();
            WaitNotElement(users.EditWait);
            WaitElement(users.SnilsInputWait);
            actionElementAndClick(users.Update);
        }

        /** Генерация снилса для добавления пользователя, Авторизация, переход в пользователи и ввод сгенерированного СНИЛС*/
        authorizationObject.GenerationSnilsAndAuthorizationMethod();
        InputPropFile("Snils_Email", authorizationObject.GenerationSnils);

        System.out.println("Заполнение данных пользователя");
        AddUsersMethod(users.WorkYatskiv, users.DivisionChildPolyclinic, users.RoleUser5, "trewq@mail.ruu");

        System.out.println("Редактирование роли, профиля и добавление даты увольнения");
        WaitElement(users.AddUserWait);
        Thread.sleep(1000);
        users.AddUser.click();
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);

        /** Проверка, что добавленный профиль отображается */
        if (KingNumber == 4) {
            Thread.sleep(2000);
        }
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 20);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.OneWait);

        System.out.println("Изменение данных о месте работы");
        users.Edit.click();
        WaitElement(users.EditWait);
        SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);

        users.UpdateWork.click();
        WaitNotElement(users.EditWait);
        WaitElement(users.SnilsInputWait);
        actionElementAndClick(users.Update);
        Thread.sleep(1500);

        /** Генерация снилса для добавления пользователя, Авторизация, переход в пользователи и ввод сгенерированного СНИЛС*/
        authorizationObject.GenerationSnilsAndAuthorizationMethod();

        System.out.println("Заполнение данных пользователя");
        AddUsersMethod(users.WorkYatskiv, users.DivisionChildPolyclinic, users.RoleUser5, "trewq@mail.ruu");

        Snils1 = authorizationObject.GenerationSnils;

        /** Проверка на ошибку ввода уже существующего email */
        authorizationObject.GenerationSnilsAndAuthorizationMethod();

        /** Заполнение данных пользователя с вводом существующего email проверкой появления ошибки - Данный email-адрес уже существует в системе  */
        WaitElement(users.EmailWait);
        inputWord(users.Email, "trewq@mail.ruu");
        WaitElement(users.Fail);
        actionElementAndClick(users.Close);

        users.DeleteUsersMethodSql(ReadPropFile("Snils_Email"));
    }

    public void deleteUser () throws InterruptedException {
        /** Удаление созданного профиля, для возможности переиспользовать данный тест */
        WaitElement(users.HeaderUsersWait);
        users.AlertClose.click();
        Thread.sleep(1500);
        users.AlertClose.click();
        WaitElement(users.AddUserWait);
        users.AddUser.click();
        users.InputSnils.sendKeys(Snils1);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 20);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);

        /** Проверка, что добавленный профиль отображается */
        WaitElement(users.OneWait);
        System.out.println("Изменение данных о месте работы");
        users.Edit.click();
        WaitElement(users.EditWait);
        SelectClickMethod(users.DateDismiss, users.DateDismissBackToday);

        /** Обновляем */
        users.UpdateWork.click();
        WaitNotElement(users.EditWait);
        WaitElement(users.SnilsInputWait);
        actionElementAndClick(users.Update);
    }
}
