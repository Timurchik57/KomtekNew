package UI.TmTest.AccessUI.Administration;

import Base.TestListener;
import Base.TestListenerChange;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListenerChange.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Расписание_консультаций")
@Tag("Роли_доступа")
@Tag("Редактирование_пользователя")
public class Access_2552Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    AcceessRoles acceessRoles;
    Users users;
    ConsultationScheduleRemote consultationSR;
    DirectionsForQuotas directionsForQuotas;

    @Issue(value = "TEL-2552")
    @Link(name = "ТМС-1949", url = "https://team-1okm.testit.software/projects/5/tests/1949?isolatedSection=e0d69503-4fe3-4790-8a4f-f3e055d6d4e0")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Description("Добавлям доступ к Расписании консультаций - проверяем отображение доступа")
    @DisplayName("Роли доступа в Расписании консультаций")
    public void Access_2552 () throws InterruptedException, IOException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        acceessRoles = new AcceessRoles(driver);
        users = new Users(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

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

        System.out.println("Проверяемый СНИЛС: " + authorizationObject.GenerationSnils);
        InputPropFile("Snils_2552", authorizationObject.GenerationSnils);

        System.out.println("Редактирование роли - включение доступа");
        AddRole("Тестовая роль", "Доступ к разделу \"Расписание консультаций\"", true);
        AddRole("Тестовая роль", "Доступ к созданию/редактированию графиков консультаций в \"Расписание консультаций\"",
                true);
        AddRole("Тестовая роль", "Доступ к просмотру графиков консультаций в \"Расписание консультаций\"", true);
        AddRole("Тестовая роль", "Доступ к записи на консультацию в \"Расписание консультаций\"", true);

        System.out.println("Авторизуемся под тестовым пользователем");
        TestUser();

        System.out.println("\n1 Проверка - Проверяем, что полностью доступен функционал Расписание консультаций");

        System.out.println("Доступны слоты расписания");
        ClickElement(consultationSR.ConsultationScheduleremote);
        Assertions.assertTrue(consultationSR.CheckConsulRemote("Зотин"), "Слоты не отображаются");

        System.out.println("Доступен График консультаций");
        ClickElement(consultationSR.ConsultationSchedule);
        ClickElement(consultationSR.UserEditFirst);
        ClickElement(consultationSR.Close);

        System.out.println("Доступна запись через слот");
        System.out.println("Открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        ClickElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));

        System.out.println("Выбрали свободный слот");
        WaitElement(directionsForQuotas.BigSnils);
        driver.findElement(directionsForQuotas.BigSnils).sendKeys("15979025720");
        ClickElement(directionsForQuotas.SearchWait("1"));
        ClickElement(directionsForQuotas.listPatientFirst2);

        System.out.println("Выбираем диагноз для заявки");
        ClickElement(directionsForQuotas.Diagnose);
        inputWord(driver.findElement(directionsForQuotas.DiagnoseSearch), "AA");
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.DiagnoseSearchSave);
        ClickElement(directionsForQuotas.WriteConsul);
        Thread.sleep(1500);
        WaitNotElement(consultationSR.AlertNotConsul);

        System.out.println(
                "Редактирование роли - убираем доступ - Доступ к созданию/редактированию графиков консультаций в \"Расписание консультаций\"");
        AddRole("Тестовая роль",
                "Доступ к созданию/редактированию графиков консультаций в \"Расписание консультаций\"", false);

        System.out.println("Авторизуемся под тестовым пользователем");
        TestUser();

        System.out.println("\n2 Проверка - Проверяем, что не доступно редактирование расписания консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);
        ClickElement(consultationSR.ConsultationSchedule);
        Thread.sleep(1500);
        WaitElement(consultationSR.UserEditFirstNo);

        System.out.println(
                "Редактирование роли - убираем доступ - Доступ к просмотру графиков консультаций в \"Расписание консультаций\"");
        AddRole("Тестовая роль", "Доступ к просмотру графиков консультаций в \"Расписание консультаций\"", false);

        System.out.println("Авторизуемся под тестовым пользователем");
        TestUser();

        System.out.println("\n3 Проверка - Проверяем, что недоступно График консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);
        Thread.sleep(1500);
        ClickElement(consultationSR.ConsultationSchedule);
        Thread.sleep(1500);
        if (KingNumber != 4) {
            WaitElement(consultationSR.AlertNotSchedule);
        } else {
            WaitElement(consultationSR.AccessDenied);
        }

        System.out.println(
                "Редактирование роли - убираем доступ - Доступ к записи на консультацию в \"Расписание консультаций\"");
        AddRole("Тестовая роль", "Доступ к записи на консультацию в \"Расписание консультаций\"", false);

        System.out.println("Авторизуемся под тестовым пользователем");
        TestUser();

        System.out.println("\n4 Проверка - Проверяем, что недоступна запись через свободный слот");
        ClickElement(consultationSR.ConsultationScheduleremote);
        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        ClickElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));

        System.out.println("Выбрали свободный слот");
        WaitElement(directionsForQuotas.BigSnils);
        driver.findElement(directionsForQuotas.BigSnils).sendKeys("15979025720");
        ClickElement(directionsForQuotas.SearchWait("1"));
        ClickElement(directionsForQuotas.listPatientFirst2);

        System.out.println("Выбираем диагноз для заявки");
        ClickElement(directionsForQuotas.Diagnose);
        inputWord(driver.findElement(directionsForQuotas.DiagnoseSearch), "AA");
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.DiagnoseSearchSave);
        ClickElement(directionsForQuotas.WriteConsul);
        Thread.sleep(1500);
        if (KingNumber != 4) {
            WaitElement(consultationSR.AlertNotConsul);
        } else {
            WaitElement(consultationSR.AccessDenied);
        }

        System.out.println("Редактирование роли - убираем доступ - Доступ к разделу \"Расписание консультаций\"");
        AddRole("Тестовая роль", "Доступ к разделу \"Расписание консультаций\"", false);

        System.out.println("Авторизуемся под тестовым пользователем");
        TestUser();

        System.out.println("\n5 Проверка - Проверяем, что нет Расписания консультаций");
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.ConsultationScheduleremote, 5);

        System.out.println("Удаление созданного пользователя");
        users.DeleteUsersMethodSql(authorizationObject.GenerationSnils);

        AddRole("Тестовая роль", "Доступ к разделу \"Расписание консультаций\"", true);
        AddRole("Тестовая роль", "Доступ к созданию/редактированию графиков консультаций в \"Расписание консультаций\"",
                true);
        AddRole("Тестовая роль", "Доступ к просмотру графиков консультаций в \"Расписание консультаций\"", true);
        AddRole("Тестовая роль", "Доступ к записи на консультацию в \"Расписание консультаций\"", true);
    }

    @Step("Метод авторизации под тестовым пользователем и выбор МО")
    public void TestUser () throws InterruptedException {

        System.out.println("Очищаем Cookie");
        driver.manage().deleteAllCookies();

        driver.get(HostAddressWeb + "/auth/bysnils?snils=" + authorizationObject.SnilsFormat(
                authorizationObject.GenerationSnils) + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
        Thread.sleep(2000);
        if (isElementNotVisible(authorizationObject.Selected)) {
            authorizationObject.Select.click();
            WaitElement(authorizationObject.SelectWait);
            Thread.sleep(2000);
            actionElementAndClick(authorizationObject.Okb);
            authorizationObject.Сhoose.click();
        }
        if (isElementNotVisible(By.xpath("//h2[contains(.,'Предупреждение')]"))) {
            if (!isElementNotVisible(By.xpath("(//button/span[contains(.,'Ок')])[2]"))) {
                ClickElement(By.xpath("//button/span[contains(.,'Ок')]"));
            } else {
                ClickElement(By.xpath("(//button/span[contains(.,'Ок')])[2]"));
            }
        }
        if (isElementNotVisible(authorizationObject.Alert) || isElementNotVisible(authorizationObject.AlertWarning)) {
            ClickElement(authorizationObject.OKWait);
        }
    }
}

