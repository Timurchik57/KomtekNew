package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import Base.TestListenerChange;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListenerChange.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Api_Направление_на_диагностику")
@Tag("Лист_Ожидания")
@Tag("Отказ_пациента")
@Tag("Проверка_БД")
public class Access_4160Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    Authorization authorization;
    DirectionsForQuotas directionsForQuotas;

    @Test
    @Order(1)
    @Issue(value = "TEL-4160")
    @Owner(value = "Галиакберов Тимур")
    @Link(name = "ТМС-2310", url = "https://team-1okm.testit.software/projects/5/tests/2310?isolatedSection=71cd14d9-46c4-4330-a433-f6755c9d1214")
    @DisplayName("Проверка Отметить отказ пациента")
    @Description("Создаём направление и переходим к нему - для статуса 6 и 15 отображется кнопка Отметить отказ пациента - проверяем таблицу на вебе и бд")
    public void Access_4160 () throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        authorization = new Authorization();
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("1 проверка - Создаём направление и Отмечаем отказ - проверяем заполнение таблицы и бд");

        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        System.out.println("Проверяем включен ли доступ ко всем консультациям - Доступ ко всем направлениям на квоты");
        AddRole(PRole, "Доступ ко всем направлениям на квоты", false);
        AddRole(PRole, "Доступ ко всем консультациям", false);

        xml.changes.put("$.TargetMedicalOid", POidMoRequest);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        xml.changes.put("$.DoctorInformation.DoctorSnils", PSnils_);
        xml.changes.put("$.InformationHeadDepartment.HeadDoctorSnils", PSnils_);
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        String id = Response.getString("Result.AccessionNumber").substring(4);
        InputPropFile("id_4160", id);

        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.SortDesc);
        ClickElement(directionsForQuotas.SearchID(id));
        ClickElement(directionsForQuotas.Button("Отметить отказ пациента"));
        String uuid = String.valueOf(UUID.randomUUID());
        inputWord(driver.findElement(directionsForQuotas.ReasonRefusal), uuid + " ");
        ClickElement(directionsForQuotas.Button("Зафиксировать отказ"));
        Thread.sleep(1500);

        AssertTable("1", uuid, PName, "1", PId);

        System.out.println("2 проверка - отмечаем еще отказы пациента и проверяем их отображение");
        ClickElement(directionsForQuotas.Button("Отметить отказ пациента"));
        uuid = String.valueOf(UUID.randomUUID());
        inputWord(driver.findElement(directionsForQuotas.ReasonRefusal), uuid + " ");
        ClickElement(directionsForQuotas.Button("Зафиксировать отказ"));
        Thread.sleep(1500);

        AssertTable("2", uuid, PName, "2", PId);

        ClickElement(directionsForQuotas.Button("Отметить отказ пациента"));
        uuid = String.valueOf(UUID.randomUUID());
        inputWord(driver.findElement(directionsForQuotas.ReasonRefusal), uuid + " ");
        ClickElement(directionsForQuotas.Button("Зафиксировать отказ"));
        Thread.sleep(1500);

        AssertTable("3", uuid, PName, "3", PId);

        System.out.println("3 проверка - сменить статус на 15 и проверить, что кнопка отображается");
        sql.UpdateConnection(
                "Update telmed.directions set status = '15' where id = '" + ReadPropFile("id_4160") + "';");
        driver.navigate().refresh();
        ClickElement(directionsForQuotas.Button("Отметить отказ пациента"));
        uuid = String.valueOf(UUID.randomUUID());
        inputWord(driver.findElement(directionsForQuotas.ReasonRefusal), uuid + " ");
        ClickElement(directionsForQuotas.Button("Зафиксировать отказ"));
        Thread.sleep(1500);

        AssertTable("4", uuid, PName, "4", PId);

        System.out.println("4 проверка - сменить статус отличный от 6/15 - кнопки нет, но есть таблица");
        sql.UpdateConnection(
                "Update telmed.directions set status = '3' where id = '" + ReadPropFile("id_4160") + "';");
        driver.navigate().refresh();
        WaitNotElement3(directionsForQuotas.Button("Отметить отказ пациента"), 3);
        AssertTable("4", uuid, PName, "4", PId);

        System.out.println("5 проверка - отметить отказ через другого пользвателя");
        sql.UpdateConnection(
                "Update telmed.directions set status = '6' where id = '" + ReadPropFile("id_4160") + "';");
        AuthoUser(PSnils_Rezerv);
        System.out.println(LOGIN_PAGE_URL);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("id_4160"));
        ClickElement(directionsForQuotas.Button("Отметить отказ пациента"));
        uuid = String.valueOf(UUID.randomUUID());
        inputWord(driver.findElement(directionsForQuotas.ReasonRefusal), uuid + " ");
        ClickElement(directionsForQuotas.Button("Зафиксировать отказ"));
        Thread.sleep(1500);

        AssertTable("5", uuid, PNameGlobal_FIO, "5", PNameRezerv_Id);

    }

    @Step("Проверяем данные из таблицы в направлении")
    public void AssertTable (String line, String Reason, String FIO, String Hiddencount, String User) throws SQLException, IOException {
        String hiddenuntildate = null;
        String hiddencount = null;
        String reasonBD = null;
        String userid = null;
        String createddate = null;
        GetDate();

        System.out.println("Проверяем БД");
        Integer lineBD = Integer.valueOf(line) - 1;
        sql.StartConnection(
                "select d.id, d.status, d.hiddenuntildate, d.hiddencount, dre.reason, dre.userid, dre.createddate from telmed.directions d \n" +
                        "join telmed.directions_decline_relation dr on d.id = dr.directionid \n" +
                        "join telmed.directions_decline_reasons dre on dr.declineid = dre.id\n" +
                        "where d.id = '" + ReadPropFile("id_4160") + "' offset " + lineBD + " limit 1;");
        while (sql.resultSet.next()) {
            hiddenuntildate = sql.resultSet.getString("hiddenuntildate");
            hiddencount = sql.resultSet.getString("hiddencount");
            reasonBD = sql.resultSet.getString("reason");
            userid = sql.resultSet.getString("userid");
            createddate = sql.resultSet.getString("createddate");
        }

        Assertions.assertEquals(hiddenuntildate.substring(0, 10),
                Year + "-" + SetDate(1, 0).substring(0, 2) + "-" + SetDate(1, 0).substring(3, 5),
                "Дата до которой направление скрыто из листа ожидания в БД не совпадает");
        Assertions.assertEquals(hiddencount, Hiddencount, "Счётчик отказов в БД не совпадает");
        Assertions.assertEquals(reasonBD, Reason, "Причина в БД не совпадает");
        Assertions.assertEquals(userid, User, "Пользователь в БД не совпадает");
        Assertions.assertEquals(createddate.substring(0, 10),
                Year + "-" + SetDate(0, 0).substring(0, 2) + "-" + SetDate(0, 0).substring(3, 5),
                "Дата создания отказа в БД не совпадает");

        System.out.println("Проверяем таблицу в направлении");
        WaitElement(directionsForQuotas.ReasonTable(line, "1"));
        String date = driver.findElement(directionsForQuotas.ReasonTable(line, "1")).getText();
        String reason = driver.findElement(directionsForQuotas.ReasonTable(line, "2")).getText();
        String fio = driver.findElement(directionsForQuotas.ReasonTable(line, "3")).getText();

        Assertions.assertEquals(date, SetDate(0, 0).substring(3, 5) + "." + SetDate(0, 0).substring(0, 2) + "." + Year,
                "Дата в таблице направления не совпадает");
        Assertions.assertEquals(reason, Reason, "Причина в таблице направления не совпадает");
        Assertions.assertEquals(fio, FIO, "ФИО в таблице направления не совпадает");
    }
}
