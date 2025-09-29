package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.HospitalizationSchedule;
import UI.TmTest.PageObject.Statistics.Reports;
import Base.TestListenerApi;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Редактирование_пользователя")
@Tag("Основные")
public class Access_2986Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    AcceessRoles acceessRoles;
    Users users;
    Reports statisticReport;
    ConsultationScheduleRemote consultationSR;
    HospitalizationSchedule hospitalizationSchedule;

    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка добавления профиля по которуму уволили")
    @Description("Предоставляем тестовому пользователю нужный профиль, добавляем дату увольнения, после проверяем, что данный профиль можно добавить повторно")
    public void Access_2986() throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        acceessRoles = new AcceessRoles(driver);
        users = new Users(driver);
        statisticReport = new Reports(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        hospitalizationSchedule = new HospitalizationSchedule(driver);

        System.out.println("Генерируем снилс для нового пользователя");
        authorizationObject.GenerateSnilsMethod();

        /** Авторизация */
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(users.UsersWait);
        WaitElement(users.HeaderUsersWait);
        users.AddUser.click();
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 20);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);
        AddUsersMethod(users.WorkOkb, users.Accounting, users.Role("Тестовая роль"), "");
        Thread.sleep(1500);

        ClickElement(users.AddUserWait);
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 20);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);

        System.out.println("Удаление профиля профиля");
        ClickElement(users.ProfileButtonWait);
        ClickElement(users.EditProfileWait);
        ClickElement(users.EditProfileDateWait);
        ClickElement(users.EditProfileDateTodayWait);
        ClickElement(users.EditProfileApplyWait);

        System.out.println("Добавление этого же профиля");
        ClickElement(users.ProfileAddButton);
        ClickElement(users.ProfileChoose);
        ClickElement(authorizationObject.Select("авиационной и космической медицине"));

        ClickElement(users.EditProfileApplyWait);
        Thread.sleep(300);
        ClickElement(users.EditProfileCloseWait);
        ClickElement(users.UpdateWait2);
        Thread.sleep(1500);

        System.out.println("Проверяем БД");
        String output = authorizationObject.GenerationSnils.substring(0,3) + "-" + authorizationObject.GenerationSnils.substring(4,7) + "-" + authorizationObject.GenerationSnils.substring(8,11) + " " + authorizationObject.GenerationSnils.substring(12,14);

        sql.StartConnection("SELECT x.* FROM telmed.userplacework x\n" +
                "join telmed.users u on x.userid = u.id where u.snils = '"+output+"';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("quitdate");
        }
        Assertions.assertNull(sql.value, "У профиля не должно быть даты увольнения");

        System.out.println("Удаление созданного пользователя");
        users.DeleteUsersMethod(authorizationObject.OKB, authorizationObject.GenerationSnils,
                users.BRBEditWaitFirst);
    }
}
