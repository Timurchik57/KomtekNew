package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import Base.TestListenerChange;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.Agreement;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.IncomingUnfinished;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListenerChange.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Требующие_согласования")
@Tag("Api_Направление_на_диагностику")
@Tag("Проверка_БД")
@Tag("Основные")
public class Access_4143Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    Authorization authorization;
    Agreement agreement;
    DirectionsForQuotas directionsForQuotas;
    IncomingUnfinished incomingUnfinished;
    Access_3965Test access3965Test;

    String id;

    @Issue(value = "TEL-4143")
    @Issue(value = "TEL-4135")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Order(1)
    @Link(name = "ТМС-2310", url = "https://team-1okm.testit.software/projects/5/tests/2310?isolatedSection=71cd14d9-46c4-4330-a433-f6755c9d1214")
    @DisplayName("Проверка Требующие согласования")
    @Description("Переходим в Требующие согласования и проверяем корректное отображение направлений")
    public void Access_4143 () throws InterruptedException, SQLException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        authorization = new Authorization();
        agreement = new Agreement(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);

        System.out.println("1 проверка - Наличие патологий должно быть - Не обработано ИИ");
        System.out.println("Создаём направление на диагностику");

        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        System.out.println("Проверяем включен ли доступ ко всем консультациям - Доступ ко всем направлениям на квоты");
        AddRole(PRole, "Доступ ко всем направлениям на квоты", false);
        AddRole(PRole, "Доступ ко всем консультациям", false);

        xml.changes.put("$.TargetMedicalOid", POidMoTarget); // обязательно нужно другую МО
        xml.changes.put("$.ResearchCode", "A06.30.005"); // обязательно нужно указать КТ
        xml.changes.put("$.PatientGuid", PatientGuid);
        xml.changes.put("$.DoctorInformation.DoctorSnils", PSnils_);
        xml.changes.put("$.InformationHeadDepartment.HeadDoctorSnils", PSnils_);
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        id = Response.getString("Result.AccessionNumber").substring(4);
        InputPropFile("id_4143", id);

        System.out.println("Авторизуемся и проверяем созданную запись со статусом 25");
        AuthorizationMethod(authorizationObject.YATCKIV);
        ClickElement(agreement.AgreementWait);
        ClickElement(agreement.SortDesc);
        WaitElement(agreement.SearchID(id));
        String direction = driver.findElement(agreement.GetDirection(id, "6")).getText();
        Assertions.assertEquals(direction, "На согласовании", "Направление должно быть со статусом На согласовании");

        System.out.println("Проверяем все записи");
        GetSql("");
    }

    @Test
    @Order(2)
    @DisplayName("Проверка Требующие согласования - Наличие паталогий")
    @Description("Переходим в Требующие согласования и проверяем корректное отображение направлений с фильтром Наличие паталогий")
    public void Access_4143_ () throws InterruptedException, SQLException, IOException {
        access3965Test = new Access_3965Test();
        directionsForQuotas = new DirectionsForQuotas(driver);
        authorizationObject = new AuthorizationObject(driver);
        agreement = new Agreement(driver);

        access3965Test.id = ReadPropFile("id_4143");;
        access3965Test.SetSql();

        AuthorizationMethod(authorizationObject.YATCKIV);
        ClickElement(agreement.AgreementWait);
        ClickElement(directionsForQuotas.SortDesc);
        WaitElement(directionsForQuotas.GetDirection(access3965Test.id, "7"));
        String PresencePathology = driver.findElement(directionsForQuotas.GetDirection(access3965Test.id, "7")).getText();

        Assertions.assertEquals(PresencePathology, "Да", "Наличие патологий должно быть - Да");

        System.out.println("Выбираем фильтр наличе патологий");
        ClickElement(directionsForQuotas.SelectAllClick);
        ClickElement(directionsForQuotas.SelectAll("Наличие патологии"));
        ClickElement(authorizationObject.Select("Да"));
        ClickElement(directionsForQuotas.SearchTwo);
        Thread.sleep(1500);
        WaitElement(directionsForQuotas.AllCount);
        GetSql("and m2.pathologyflag is true");

        System.out.println("2 проверка - Наличие патологий должно быть - Нет");
        sql.UpdateConnection(
                "Update telmed.mosmedinforesults set pathologyflag = false where id = '" + access3965Test.idMosmedResult + "';");
        driver.navigate().refresh();
        ClickElement(directionsForQuotas.SortDesc);
        WaitElement(directionsForQuotas.GetDirection(access3965Test.id, "7"));
        ClickElement(directionsForQuotas.SelectAllClick);
        ClickElement(directionsForQuotas.SelectAll("Наличие патологии"));
        ClickElement(authorizationObject.Select("Нет"));
        ClickElement(directionsForQuotas.SearchTwo);
        Thread.sleep(1500);
        WaitElement(directionsForQuotas.AllCount);
        GetSql("and m2.pathologyflag is false");

        sql.UpdateConnection("delete from telmed.mosmedinforesults where id = '" + ReadPropFile(
                "idMosmedResult_3965") + "';");
        sql.UpdateConnection(
                "delete from telmed.mosmedinfo where id = '" + ReadPropFile("idMosmed_3965") + "';");
    }


    @Step("Метод Сверяющий значения с бд в Листе Ожидания")
    public void GetSql (String where) throws SQLException {
        String count = driver.findElement(agreement.ConsultationCount).getText();

        sql.StartConnection(
                "select count(*) from telmed.directions d\n" +
                        "left join telmed.slots s on d.id = s.iddirection\n" +
                        "left join telmed.cami_links cl on cl.id_direction = d.id \n" +
                        "left join telmed.mosmedinfo m on m.cami_link_id = cl.id \n" +
                        "left join telmed.mosmedinforesults m2 on m2.mosmedinfoid = m.id \n" +
                        "WHERE s.id IS null and d.directiontype = 1 and d.targetmoid = '" + PIdMoTarget + "' and status in ('25') and (d.hiddenuntildate <= CURRENT_DATE or d.hiddenuntildate is null) and d.hiddencount < 3 " + where + ";");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(count.substring(6), sql.value,
                "Количество записей в Листе ожидания для " + PMOTarget + " не совпадает с БД");
    }
}
