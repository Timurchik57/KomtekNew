package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.AuthorizationObject;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Уровень_мед_организаций")
@Tag("Проверка_БД")
@Tag("Основные")
public class Access_1902Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    MedOrganization medOrganization;
    Access_1898Test access_1898Test;
    String code;
    String data;
    String eventtypeid;
    String moname;
    String userfullname;
    String level;
    private Integer NumberKing;

    @Issue(value = "TEL-1902")
    @Link(name = "ТМС-1797", url = "https://team-1okm.testit.software/projects/5/tests/1797?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("В мед организации добавляем уровни и проверяем изменения в бд")
    @Test
    @DisplayName("Уровни доступа мед организаций")
    public void Access_1902 () throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        medOrganization = new MedOrganization(driver);
        access_1898Test = new Access_1898Test();

        /* Уровень 1 - отображаются свои документы в регистрах
           Уровень 2 и 3 - отображаются документы, которые указаны в Курируемые МО
            Если MoLevels = 0, то отображаются свои документы
         */

        /** Запоминаем контур, чтобы после вернуть значение*/
        NumberKing = KingNumber;

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходим в Мед. Организации и редактируем нужный уровень");
        ClickElement(medOrganization.OrganizationWait);
        ClickElement(medOrganization.InputOrganizationWait);
        ClickElement(medOrganization.SelectMOOKB);
        ClickElement(medOrganization.SearchWait);
        ClickElement(medOrganization.EditFirst);
        ClickElement(medOrganization.AccessFirstEdit);
        ClickElement(medOrganization.DateStart);
        ClickElement(medOrganization.DateStart1);
        ClickElement(medOrganization.Level);
        if (isElementNotVisible(medOrganization.Level3True) == false) {
            ClickElement(medOrganization.Level3);
            level = "3";
        } else {
            ClickElement(medOrganization.Level2);
            level = "2";
        }
        ClickElement(medOrganization.Profile);
        ClickElement(medOrganization.Profile1);
        ClickElement(medOrganization.UpdateAccess("2"));
        ClickElement(medOrganization.UpdateAccess("1"));

        System.out.println("Переключаемся на аудит дева");
        if (KingNumber == 1) {
            KingNumber = 8;
        }
        if (KingNumber == 2) {
            KingNumber = 6;
        }
        if (KingNumber == 4) {
            Thread.sleep(1500);
            KingNumber = 7;
        }
        sql.ReplacementConnection();

        sql.StartConnection("select types.code, events.* from audit.telemed events\n" +
                "left join audit.eventtypes types on types.id = events.eventtypeid\n" +
                "where types.code in ('CreateMoLevel', 'UpdateMoLevel', 'DeleteMoLevel') order by id desc limit 1;");
        while (sql.resultSet.next()) {
            code = sql.resultSet.getString("code");
            data = sql.resultSet.getString("data");
            eventtypeid = sql.resultSet.getString("eventtypeid");
            moname = sql.resultSet.getString("moname");
            userfullname = sql.resultSet.getString("userfullname");
        }
        Assertions.assertEquals(code, "UpdateMoLevel", "У обновления код не UpdateMoLevel");
        data.contains("\"New\": {\"Id\"");
        data.contains("\"Old\": {\"Id\"");
        data.contains("\"EndDate\": \"" + Date + "");
        data.contains("\"Level\": " + level + "");
        data.contains(" \"ProfileId\": 1");
        if (KingNumber == 7) {
            Assertions.assertEquals(eventtypeid, "45", "У обновления id не 41 (Изменение записи об уровне МО)");
        } else {
            Assertions.assertEquals(eventtypeid, "41", "У обновления id не 41 (Изменение записи об уровне МО)");
        }
        Assertions.assertEquals(moname, "БУ ХМАО-Югры \"Окружная клиническая больница\"", "Не верно указана МО");
        Assertions.assertEquals(userfullname, PName,
                "ФИО пользователя через которого авторизован - не верно указан");

        System.out.println("Переходим в Мед. Организации и создаём нужный уровень");
        ClickElement(medOrganization.OrganizationWait);
        ClickElement(medOrganization.InputOrganizationWait);
        ClickElement(medOrganization.SelectMOOKB);
        ClickElement(medOrganization.SearchWait);
        ClickElement(medOrganization.EditFirst);

        ClickElement(medOrganization.AddAccess);
        ClickElement(medOrganization.DateStart);
        ClickElement(medOrganization.DateToDay);
        ClickElement(medOrganization.Level);
        ClickElement(medOrganization.Level2);
        ClickElement(medOrganization.Profile);
        ClickElement(medOrganization.Profile1);
        ClickElement(medOrganization.AddAccess2);
        ClickElement(medOrganization.UpdateAccess("1"));

        System.out.println("Переключаемся на аудит дева");
        if (KingNumber == 1) {
            KingNumber = 8;
        }
        if (KingNumber == 2) {
            KingNumber = 6;
        }
        if (KingNumber == 4) {
            Thread.sleep(1500);
            KingNumber = 7;
        }
        sql.ReplacementConnection();

        sql.StartConnection("select types.code, events.* from audit.telemed events\n" +
                "left join audit.eventtypes types on types.id = events.eventtypeid\n" +
                "where types.code in ('CreateMoLevel', 'UpdateMoLevel', 'DeleteMoLevel') order by id desc limit 1;");
        while (sql.resultSet.next()) {
            code = sql.resultSet.getString("code");
            data = sql.resultSet.getString("data");
            eventtypeid = sql.resultSet.getString("eventtypeid");
            moname = sql.resultSet.getString("moname");
            userfullname = sql.resultSet.getString("userfullname");
        }
        Assertions.assertEquals(code, "CreateMoLevel", "У обновления код не CreateMoLevel");
        data.contains("\"New\": {}");
        data.contains("\"Old\": {\"Id\"");
        data.contains("\"EndDate\": \"" + Date + "");
        data.contains("\"Level\": 2");
        data.contains(" \"ProfileId\": 1");
        if (KingNumber == 7) {
            Assertions.assertEquals(eventtypeid, "41", "У обновления id не 40 (Добавление записи об уровне МО)");
        } else {
            Assertions.assertEquals(eventtypeid, "40", "У обновления id не 40 (Добавление записи об уровне МО)");
        }
        Assertions.assertEquals(moname, "БУ ХМАО-Югры \"Окружная клиническая больница\"", "Не верно указана МО");
        Assertions.assertEquals(userfullname, PName,
                "ФИО пользователя через которого авторизован - не верно указан");

        System.out.println("Переходим в Мед. Организации и удаляем нужный уровень");
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        Date = formatForDateNow.format(date);
        ClickElement(medOrganization.EditFirst);
        ClickElement(medOrganization.DeleteForDate("" + Date + ""));
        ClickElement(medOrganization.DeleteYes);

        System.out.println("Переключаемся на аудит дева");
        if (KingNumber == 1) {
            Thread.sleep(1500);
            KingNumber = 8;
        }
        if (KingNumber == 2) {
            Thread.sleep(1500);
            KingNumber = 6;
        }
        if (KingNumber == 4) {
            Thread.sleep(1500);
            KingNumber = 7;
        }
        sql.ReplacementConnection();

        System.out.println("Дожидаемся смены статуса фоновым сервисом");
        Integer CountCollback = 1;
        while (code.contains("DeleteMoLevel") == false & CountCollback < 150) {
            sql.StartConnection("select types.code, events.* from audit.telemed events\n" +
                    "left join audit.eventtypes types on types.id = events.eventtypeid\n" +
                    "where types.code in ('CreateMoLevel', 'UpdateMoLevel', 'DeleteMoLevel') order by id desc limit 1;");
            while (sql.resultSet.next()) {
                code = sql.resultSet.getString("code");
            }
            CountCollback++;
        }
        sql.StartConnection("select types.code, events.* from audit.telemed events\n" +
                "left join audit.eventtypes types on types.id = events.eventtypeid\n" +
                "where types.code in ('CreateMoLevel', 'UpdateMoLevel', 'DeleteMoLevel') order by id desc limit 1;");
        while (sql.resultSet.next()) {
            code = sql.resultSet.getString("code");
            data = sql.resultSet.getString("data");
            eventtypeid = sql.resultSet.getString("eventtypeid");
            moname = sql.resultSet.getString("moname");
            userfullname = sql.resultSet.getString("userfullname");
        }
        Assertions.assertEquals(code, "DeleteMoLevel", "У обновления код не DeleteMoLevel");
        data.contains("\"New\": {}");
        data.contains("\"Old\": {\"Id\"");
        data.contains("\"EndDate\": \"" + Date + "");
        data.contains("\"Level\": 2");
        data.contains(" \"ProfileId\": 1");
        if (KingNumber == 7) {
            Assertions.assertEquals(eventtypeid, "46", "У обновления id не 42 (Удаление записи об уровне МО)");
        } else {
            Assertions.assertEquals(eventtypeid, "42", "У обновления id не 42 (Удаление записи об уровне МО)");
        }
        Assertions.assertEquals(moname, "БУ ХМАО-Югры \"Окружная клиническая больница\"", "Не верно указана МО");
        Assertions.assertEquals(userfullname, PName,
                "ФИО пользователя через которого авторизован - не верно указан");

        /** Меняем значение обратно */
        KingNumber = NumberKing;
    }
}
