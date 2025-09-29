package UI.TmTest.AccessUI;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Проверка существующего пользователя")
public class UserTest extends BaseAPI {
    String SNILS = "159 790 257 20";
    AuthorizationObject authorizationObject;
    Users users;

    @Test
    @DisplayName("Проверка существующего пользователя")
    public void UserOld() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);
        System.out.println("Проверка существующего пользователя");
        /** Авторизация */
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(users.UsersWait);
        ClickElement(users.AddUserWait);
        users.InputSnils.sendKeys(SNILS);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 15);
        ClickElement(users.ButtonSearchWait);
        /** Условие, нужно ли заполнять данные о пользователе */
        if (!isElementVisible(users.NotIEMK)) {
            AddUsersMethod(users.WorkYatskiv, users.Accounting, users.RoleUser5, "");
            System.out.println("Новый пользователь добавлен в ИЭМК");

        } else {
            WaitElement(users.TrueAlertWait);
            users.CloseAlert.click();
            System.out.println("Пользователь с данным СНИЛС уже есть в базе ИЕМК");
            users.Update.click();
        }
    }
}
