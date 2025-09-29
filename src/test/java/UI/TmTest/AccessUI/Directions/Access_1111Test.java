package UI.TmTest.AccessUI.Directions;

import Base.TestListener;
import Base.TestListenerChange;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.IncomingUnfinished;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@ExtendWith(TestListenerChange.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Редактирование_пользователя")
@Tag("РРП")
@Tag("Основные")
public class Access_1111Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    EquipmentSchedule equipmentSchedule;
    DirectionsForQuotas directionsForQuotas;
    IncomingUnfinished incomingUnfinished;

    Users users;

    @Issue(value = "TEL-1111")
    @Link(name = "ТМС-1416", url = "https://team-1okm.testit.software/projects/5/tests/1416?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Запись на оборудование в МО создающее направление")
    @Description("Создаём направление в МО, через которое авторизовались, проверяя отображение направления при различных подразделениях у авторизованного пользователя")
    public void Access_1111 () throws InterruptedException, SQLException, IOException {
        equipmentSchedule = new EquipmentSchedule(driver);
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        users = new Users(driver);

        AddRole(PRole, "Доступ ко всем направлениям на квоты", false);
        AddRole(PRole, "Доступ ко всем консультациям", false);

        System.out.println("Ищем пациента в РРП через АПИ");
        GetRRP("", Snils);

        AuthorizationMethod(authorizationObject.OKB);

        /** Метод создания консультации на оборудование и заполнении информации */
        directionsForQuotas.CreateConsultationEquipment(true, Snils,
                authorizationObject.Select("Женская консультация"), "Аорта", true,
                "",
                authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                authorizationObject.Select("Магнитно-резонансная томография головного мозга с контрастированием"),
                "1", false, true, true, PNameEquipment);

        sql.StartConnection("Select * from telmed.directions order by id limit 1");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        System.out.println(
                "Переходим в Направления на квоты - Исходящие - Незавершённые и проверяем созданную консультацию");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.SortDesc);
        WaitElement(directionsForQuotas.SearchID(sql.value));
        driver.findElement(directionsForQuotas.FirstLineMO).getText().contains(
                "БУ ХМАО-Югры \"Окружная клиническая больница\"");
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        Date = formatForDateNow.format(date);
        Assertions.assertEquals(driver.findElement(directionsForQuotas.FirstLineMO).getText(),
                "БУ ХМАО-Югры \"Окружная клиническая больница\"");
        Assertions.assertEquals(driver.findElement(directionsForQuotas.FirstLineDate).getText(), "" + Date + "");
        Assertions.assertEquals(driver.findElement(directionsForQuotas.FirstLinePatient).getText(),
                "Тестировщик Тест Тестович, " + calculateYearDifference(Response.getString("patients[0].BirthDate")));
        Assertions.assertEquals(driver.findElement(directionsForQuotas.FirstLineSnils).getText(),
                formatSnils(Snils));
        Assertions.assertEquals(driver.findElement(directionsForQuotas.FirstLineStatus).getText(), "Отправлен");

        System.out.println(
                "Переходим в Направления на квоты - Входящие - Незавершённые и проверяем созданную консультацию");
        ClickElement(incomingUnfinished.ConsultationWait);
        WaitElement(incomingUnfinished.FirstLineMO);
        Assertions.assertEquals(driver.findElement(incomingUnfinished.FirstLineMO).getText(),
                "БУ ХМАО-Югры \"Окружная клиническая больница\"");
        Assertions.assertEquals(driver.findElement(incomingUnfinished.FirstLineDate).getText(), "" + Date + "");
        System.out.println(calculateYearDifference(Response.getString("patients[0].BirthDate")));
        Assertions.assertEquals(
                driver.findElement(incomingUnfinished.FirstLinePatient).getText(),
                "Тестировщик Тест Тестович, " + calculateYearDifference(Response.getString("patients[0].BirthDate")));
        Assertions.assertEquals(driver.findElement(incomingUnfinished.FirstLineSnils).getText(),
                formatSnils(Snils));
        Assertions.assertEquals(driver.findElement(incomingUnfinished.FirstLineStatus).getText(), "Отправлен");

        System.out.println("Создание нового пользователя с другим подразделением");
        authorizationObject.GenerateSnilsMethod();

        System.out.println("Авторизация и переход в пользователи");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(users.UsersWait);
        ClickElement(users.AddUserWait);
        WaitElement(users.InputSnilsWait);
        users.InputSnils.sendKeys(authorizationObject.GenerationSnils);
        Thread.sleep(1500);
        WaitNotElement3(users.LoadingSnils, 20);
        ClickElement(users.ButtonSearchWait);
        WaitElement(users.SnilsInputWait);

        /** Условие, нужно ли заполнять данные о пользователе */
        if (!isElementNotVisible(users.NotHeaderIEMK)) {
            AddUsersMethod(users.WorkOkb, users.Accounting, users.Role("Тестовая роль"), "");
            System.out.println("Новый пользователь добавлен в ИЭМК");
        }
        InputPropFile("Snils_1111", authorizationObject.GenerationSnils);

        AddRole("Тестовая роль", "Доступ к разделу \"Консультации\"", true);
        AddRole("Тестовая роль", "Доступ к разделу \"Направления на квоты\"", true);
        AddRole("Тестовая роль", "Доступ ко всем направлениям на квоты", false);
        AddRole("Тестовая роль", "Доступ ко всем консультациям", false);

        System.out.println("Авторизуемся под тестовым пользователем");
        System.out.println(HostAddressWeb + "/auth/bysnils?snils=" + authorizationObject.SnilsFormat(
                authorizationObject.GenerationSnils) + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
        driver.get(
                HostAddressWeb + "/auth/bysnils?snils=" + authorizationObject.SnilsFormat(
                        authorizationObject.GenerationSnils) + "&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");

        Thread.sleep(2000);
        if (isElementNotVisible(authorizationObject.Selected)) {
            authorizationObject.Select.click();
            WaitElement(authorizationObject.SelectWait);
            Thread.sleep(2000);
            ClickElement(authorizationObject.OKB);
            authorizationObject.Сhoose.click();
        }
        if (isElementNotVisible(authorizationObject.Alert) || isElementNotVisible(
                authorizationObject.AlertWarning)) {
            ClickElement(authorizationObject.OKWait);
        }

        System.out.println(
                "Переходим в Направления на квоты - Исходящие - Незавершённые и проверяем созданную консультацию");
        ClickElement(incomingUnfinished.ConsultationWait);
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.Loading, 30);
        WaitElement(incomingUnfinished.FirstLineMO);
        ClickElement(incomingUnfinished.TypeDate);
        ClickElement(incomingUnfinished.SelectDate);
        ClickElement(incomingUnfinished.FirstDate);
        ClickElement(incomingUnfinished.ToDay);
        if (isElementNotVisible(By.xpath("//td[@class='available today in-range start-date end-date']"))) {
            ClickElement(incomingUnfinished.ToDay1);
        } else {
            ClickElement(incomingUnfinished.ToDay2);
        }
        driver.findElement(incomingUnfinished.Snils).sendKeys(Snils);
        ClickElement(incomingUnfinished.Search);
        Thread.sleep(2000);
        WaitElement(incomingUnfinished.NotBad);

        System.out.println(
                "Переходим в Направления на квоты - Входящие - Незавершённые и проверяем созданную консультацию");
        ClickElement(incomingUnfinished.ConsultationWait);
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.Loading, 30);
        WaitElement(incomingUnfinished.FirstLineMO);
        ClickElement(incomingUnfinished.TypeDate);
        ClickElement(incomingUnfinished.SelectDate);
        ClickElement(incomingUnfinished.FirstDate);
        ClickElement(incomingUnfinished.ToDay);
        if (isElementNotVisible(By.xpath("//td[@class='available today in-range start-date end-date']"))) {
            ClickElement(incomingUnfinished.ToDay1);
        } else {
            ClickElement(incomingUnfinished.ToDay2);
        }
        driver.findElement(incomingUnfinished.Snils).sendKeys(Snils);
        ClickElement(incomingUnfinished.Search);
        Thread.sleep(2000);
        WaitElement(incomingUnfinished.NotBad);

        /** Удаление созданного профиля, для возможности переиспользовать данный тест */
        System.out.println("Удаление созданного пользователя");
        users.DeleteUsersMethodSql(authorizationObject.GenerationSnils);

        AddRole(PRole, "Доступ ко всем направлениям на квоты", true);
        AddRole(PRole, "Доступ ко всем консультациям", true);
    }
}
