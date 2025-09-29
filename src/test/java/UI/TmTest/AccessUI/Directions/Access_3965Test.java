package UI.TmTest.AccessUI.Directions;

import Base.*;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionOutgoingArchive;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.IncomingArchive;
import UI.TmTest.PageObject.Directions.Kvots.IncomingUnfinished;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerChange.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Фильтры_в_направлении")
@Tag("Api_Направление_на_диагностику")
public class Access_3965Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    Authorization authorization;
    IncomingArchive incomingArchive;
    IncomingUnfinished incomingUnfinished;
    DirectionOutgoingArchive directionOutgoingArchive;

    String id;
    String idCami;
    String idMosmed;
    String idMosmedResult;
    String DopBD = "";
    String SqlStatus = null;

    @Issue(value = "TEL-3965")
    @Issue(value = "TEL-3912")
    @Issue(value = "TEL-3973")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отображения Наличие паталогий в списке направлений Исходящие / Незавершенные")
    @Description("Переходим в Созданные консультации - находим созданное направление - проверяем Наличие паталогий (обработано ИИ)")
    public void Access_3965 () throws InterruptedException, SQLException, IOException {
        DirectionPathology("Направления на квоты / Исходящие / Незавершенные");
    }

    @Test
    @DisplayName("Проверка отображения Наличие паталогий в списке направлений Исходящие / Архивные")
    public void Access_3965_2 () throws InterruptedException, SQLException, IOException {
        DirectionPathology("Направления на квоты / Исходящие / Архивные");
    }

    @Test
    @DisplayName("Проверка отображения Наличие паталогий в списке направлений Входящие / Незавершенные")
    public void Access_3965_3 () throws InterruptedException, SQLException, IOException {
        DirectionPathology("Направления на квоты / Входящие / Незавершенные");
    }

    @Test
    @DisplayName("Проверка отображения Наличие паталогий в списке направлений Входящие / Архивные")
    public void Access_3965_4 () throws InterruptedException, SQLException, IOException {
        DirectionPathology("Направления на квоты / Входящие / Архивные");
    }

    @Step("Метод для проверки Наличие патологий в {0}")
    public void DirectionPathology (String directoin) throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingArchive = new IncomingArchive(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        directionOutgoingArchive = new DirectionOutgoingArchive(driver);
        authorization = new Authorization();

        By getDirection = null;
        if (directoin.equals("Направления на квоты / Исходящие / Незавершенные")) {
            getDirection = directionsForQuotas.ConsultationWait;
            SqlStatus = "6";
        }
        if (directoin.equals("Направления на квоты / Исходящие / Архивные")) {
            getDirection = directionOutgoingArchive.DirectionOutgoingArchiveWait;
            SqlStatus = "21";
        }
        if (directoin.equals("Направления на квоты / Входящие / Незавершенные")) {
            getDirection = incomingUnfinished.ConsultationWait;
            SqlStatus = "3";
        }
        if (directoin.equals("Направления на квоты / Входящие / Архивные")) {
            getDirection = incomingArchive.IncomingArchiveWait;
            SqlStatus = "21";
        }

        System.out.println("1 проверка - Наличие патологий должно быть - Не обработано ИИ");
        System.out.println("Создаём направление на диагностику");

        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        System.out.println("Проверяем включен ли доступ ко всем консультациям - Доступ ко всем направлениям на квоты");
        AddRole(PRole, "Доступ ко всем направлениям на квоты", true);
        AddRole(PRole, "Доступ ко всем консультациям", true);

        // Проверяем есть ли доступ у нужной роли (нужно только если отключены верхние доступы)
        sql.StartConnection("SELECT count(*)  FROM telmed.rolewithaccess r\n" +
                "join telmed.accessroles a on r.roleid = a.id \n" +
                "join telmed.accessesdirectory d on r.accessid = d.id \n" +
                "WHERE a.\"name\" = '" + PRole + "' and d.\"name\" = 'Доступ ко всем направлениям на квоты';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }

        System.out.println("sql value = " + sql.value);
        if (sql.value.equals("0")) {
            System.out.println("зашли");
            DopBD = "and d.requestermoid = '12'";
        }

        xml.changes.put("$.TargetMedicalOid", "1.2.643.5.1.13.13.12.2.86.8902");
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        id = Response.getString("Result.AccessionNumber").substring(4);

        System.out.println("Авторизуемся и проверяем созданную запись");
        sql.UpdateConnection("update telmed.directions set status = '" + SqlStatus + "' where id = '" + id + "';");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(getDirection);
        ClickElement(directionsForQuotas.SortDesc);
        WaitElement(directionsForQuotas.GetDirection(id, "9"));
        String PresencePathology = driver.findElement(directionsForQuotas.GetDirection(id, "9")).getText();

        Assertions.assertEquals(PresencePathology, "Не обработано ИИ",
                "Наличие патологий должно быть - Не обработано ИИ");

        System.out.println("2 проверка - Наличие патологий должно быть - Да");
        SetSql();
        driver.navigate().refresh();
        ClickElement(directionsForQuotas.SortDesc);
        WaitElement(directionsForQuotas.GetDirection(id, "9"));
        PresencePathology = driver.findElement(directionsForQuotas.GetDirection(id, "9")).getText();

        Assertions.assertEquals(PresencePathology, "Да", "Наличие патологий должно быть - Да");

        System.out.println("Выбираем фильтр наличе патологий");
        ClickElement(directionsForQuotas.SelectAllClick);
        ClickElement(directionsForQuotas.SelectAll("Наличие патологии"));
        ClickElement(authorizationObject.Select("Да"));
        ClickElement(directionsForQuotas.SearchTwo);
        Thread.sleep(1500);
        WaitElement(directionsForQuotas.AllCount);
        String count = driver.findElement(directionsForQuotas.AllCount).getText().substring(6);

        Assertions.assertEquals(count, GetSql("true"));

        System.out.println("Проверяем цвет данной строки");
        String color = driver.findElement(directionsForQuotas.Color(id)).getCssValue("background-color");
        Assertions.assertEquals(color, "rgba(255, 255, 224, 1)", "Цвет не совпадает с нужным");

        System.out.println("3 проверка - Наличие патологий должно быть - Нет");
        sql.UpdateConnection(
                "Update telmed.mosmedinforesults set pathologyflag = false where id = '" + idMosmedResult + "';");
        driver.navigate().refresh();
        ClickElement(directionsForQuotas.SortDesc);
        WaitElement(directionsForQuotas.GetDirection(id, "9"));
        ClickElement(directionsForQuotas.SelectAllClick);
        ClickElement(directionsForQuotas.SelectAll("Наличие патологии"));
        ClickElement(authorizationObject.Select("Нет"));
        ClickElement(directionsForQuotas.SearchTwo);
        Thread.sleep(1500);
        WaitElement(directionsForQuotas.AllCount);
        count = driver.findElement(directionsForQuotas.AllCount).getText().substring(6);

        Assertions.assertEquals(count, GetSql("false"));

        sql.UpdateConnection("delete from telmed.mosmedinforesults where id = '" + ReadPropFile(
                "idMosmedResult_3965") + "';");
        sql.UpdateConnection(
                "delete from telmed.mosmedinfo where id = '" + ReadPropFile("idMosmed_3965") + "';");
        AddRole(PRole, "Доступ ко всем направлениям на квоты", false);
        AddRole(PRole, "Доступ ко всем консультациям", false);
    }

    @Step("Метод добавляющий значения в таблицы telmed.mosmedinfo и telmed.mosmedinforesults, устанавливаем флаг Наличие паталогий в true")
    public void SetSql () throws SQLException, IOException {
        date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Date = formatForDateNow.format(date);

        System.out.println("Находим нужную запись в telmed.cami_links");
        sql.StartConnection("Select * from telmed.cami_links where id_direction = '" + id + "'");
        while (sql.resultSet.next()) {
            idCami = sql.resultSet.getString("id");
        }

        System.out.println("Создаём запись в telmed.mosmedinfo");
        sql.UpdateConnection(
                "insert into telmed.mosmedinfo (cami_link_id, task_id, date_task, study_uid, last_check, instances_count, mosmed_link, count_search, is_sent) values ('" + idCami + "', '" + UUID.randomUUID() + "', '" + Date + " 13:12:39.460', '0.00.000.0000000TLMD" + id + "', '" + Date + " 13:12:39.460', '4', 'http://HOST-TSAMI-WAPP.miac-s.local/pacs/wado.php?mode=4&id_1=0000000TLMD49565', '0', '0');");

        sql.StartConnection("Select * from telmed.mosmedinfo where cami_link_id = '" + idCami + "'");
        while (sql.resultSet.next()) {
            idMosmed = sql.resultSet.getString("id");
        }
        InputPropFile("idMosmed_3965", idMosmed);

        System.out.println("Создаём запись в telmed.mosmedinforesults");
        sql.UpdateConnection(
                "insert into telmed.mosmedinforesults (mosmedinfoid, series_uid, model_id, report, pathologyflag) values ('" + idMosmed + "', '123', '456', 'Проверяю заявку 3965', 'true');");

        sql.StartConnection("Select * from telmed.mosmedinforesults where mosmedinfoid = '" + idMosmed + "'");
        while (sql.resultSet.next()) {
            idMosmedResult = sql.resultSet.getString("id");
        }
        InputPropFile("idMosmedResult_3965", idMosmedResult);
    }

    @Step("Метод Сверяющий значения с бд при применении фильтров")
    public String GetSql (String value) throws SQLException {
        List<String> list = new ArrayList<>();
        sql.StartConnection("select distinct d.id\n" +
                "from telmed.directions d \n" +
                "left join telmed.cami_links cl on cl.id_direction = d.id \n" +
                "left join telmed.mosmedinfo m on m.cami_link_id = cl.id \n" +
                "left join telmed.mosmedinforesults m2 on m2.mosmedinfoid = m.id \n" +
                "where m2.pathologyflag is " + value + " " + DopBD + ";");
        while (sql.resultSet.next()) {
            list.add(sql.resultSet.getString("id"));
        }
        sql.value = String.valueOf(list.size());
        return sql.value;
    }
}
