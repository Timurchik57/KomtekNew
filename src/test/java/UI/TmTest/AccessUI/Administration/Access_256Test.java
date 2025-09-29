package UI.TmTest.AccessUI.Administration;

import Base.TestListener;
import UI.TmTest.PageObject.Administration.RouteOMP;
import UI.TmTest.PageObject.AuthorizationObject;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Настройка_маршрутов_ОМП")
@Tag("Проверка_БД")
@Tag("Основные")
public class Access_256Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    RouteOMP routeOMP;
    private String SQLRegName;
    private String SQLMarName;
    private String SQLOMPName;
    private String SQLPodOMPName;
    private String SQLLevel;
    private String SQLMO;
    private String SQLNextPodOMPName;
    private String SQLCodeName;
    private String SQLSCodeName;
    private String SQLDateName;
    private String SQLLevelName;
    private String SQLMOName;
    private String code;
    private String data;
    private String eventtypeid;
    private String moname;
    private String userid;
    private String userfullname;
    private Integer NumberKing;

    @Test
    @Order(1)
    @Issue(value = "TEL-256")
    @Issue(value = "TEL-1904")
    @Link(name = "ТМС-1378", url = "https://team-1okm.testit.software/projects/5/tests/945?isolatedSection=9087652d-2067-4fe3-9490-66bb61e906ba")
    @Link(name = "ТМС-1799", url = "https://team-1okm.testit.software/projects/5/tests/1799?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Добавление и Удаление маршрута ОМП")
    @Description("Переходим в администрирование - настройка маршрутов ОМП, добавляем новый маршрут со всеми обязательными полями, проверяем добавление в БД. После удаляем созданный маршрут")
    public void Access_256 () throws SQLException, InterruptedException {

        routeOMP = new RouteOMP(driver);
        authorizationObject = new AuthorizationObject(driver);
        String mo = "";

        System.out.println("Авторизуемся и переходим в настройка маршрутов ОМП");
        if (KingNumber == 12) {
            AuthorizationLoginPassword(LOGIN_PAGE_URL, LoginBase, PasswordBase,
                    "ГБУЗ \"Чукотская окружная больница\" г. Анадырь");
            mo = "ГБУЗ \"Чукотская окружная больница\" г. Анадырь";
        } else {
            AuthorizationMethod(authorizationObject.OKB);
            mo = "БУ ХМАО-Югры \"Окружная клиническая больница\"";
        }
        ClickElement(routeOMP.RouteOMPWait);

        System.out.println("Добавление нового маршрута");
        WaitElement(routeOMP.Header);
        Thread.sleep(1500);
        while (isElementNotVisible(routeOMP.Error)) {
            ClickElement(routeOMP.Error);
            Thread.sleep(1500);
        }
        ClickElement(routeOMP.Add);

        System.out.println("Выбираем регистр");
        ClickElement(routeOMP.Registers);
        WaitElement(routeOMP.SelectNameFirst);
        String RegName = driver.findElement(routeOMP.SelectNameFirst).getText();
        ClickElement(authorizationObject.SelectFirst);

        System.out.println("Выбираем Тип");
        ClickElement(routeOMP.Federation);

        System.out.println("Выбираем Наименование маршрута");
        ClickElement(routeOMP.NameRoute);
        WaitElementTime(routeOMP.SelectNameFirst, 60);

        WaitElement(authorizationObject.SelectFirst);
        String MarName = driver.findElement(routeOMP.SelectNameFirst).getText();
        ClickElement(authorizationObject.SelectFirst);

        System.out.println("Выбираем ОМП");
        ClickElement(routeOMP.OMP);
        WaitElement(routeOMP.SelectNameFirst);
        String OMPName = driver.findElement(routeOMP.SelectNameFirst).getText();
        ClickElement(authorizationObject.SelectFirst);

        System.out.println("Выбираем Подэтап ОМП");
        ClickElement(routeOMP.PodOMP);
        WaitElement(routeOMP.SelectNameFirst);
        String PodOMPName = driver.findElement(routeOMP.SelectNameFirst).getText();
        ClickElement(authorizationObject.SelectFirst);

        System.out.println("Выбираем Уровень указания");
        ClickElement(routeOMP.Level);
        WaitElement(routeOMP.SelectNameFirst);
        String LevelName = driver.findElement(routeOMP.SelectNameFirst).getText();
        ClickElement(authorizationObject.SelectFirst);

        System.out.println("Выбираем МО");
        ClickElement(routeOMP.MO);
        WaitElement(authorizationObject.Select(mo));
        String MOName = driver.findElement(authorizationObject.Select(mo)).getText();
        ClickElement(authorizationObject.Select(mo));

        System.out.println("Выбираем Настройки доступа");
        ClickElement(routeOMP.ADD);
        ClickElement(routeOMP.Code);
        inputWord(driver.findElement(routeOMP.Code), "A26 ");
        WaitElement(routeOMP.SelectNameFirst);
        ClickElement(authorizationObject.SelectFirst);
        String CodeName = driver.findElement(routeOMP.SelectNameFirst).getText();

        inputWord(driver.findElement(routeOMP.Norma), "3 ");
        ClickElement(routeOMP.LevelAdd);
        ClickElement(authorizationObject.SelectFirst);

        inputWord(driver.findElement(routeOMP.MOAdd), mo + "1");
        ClickElement(authorizationObject.Select(mo));
        ClickElement(routeOMP.ADDDuble);

        System.out.println("Выбираем Следующий подэтап ОМП");
        ClickElement(routeOMP.NextPodOMP);
        String NextPodOMPName = null;
        WaitElement(routeOMP.SelectNameSecond);
        NextPodOMPName = driver.findElement(routeOMP.SelectNameSecond).getText();
        ClickElement(authorizationObject.SelectSecond);
        inputWord(driver.findElement(routeOMP.NormTime), "365");
        ClickElement(routeOMP.AddTwo);
        if (isElementVisibleTime(authorizationObject.AlertTrue(""), 10)) {

        }
        Thread.sleep(6000);
        authorizationObject.LoadingTime(60);
        sql.StartConnection(
                "select m. id, med.\"name\", t.\"name\" Rname, st.stage, su.short_name, m.\"level\", msm.namemu, sub.short_name NextName, \n" +
                        "m.\"period\", ms.s_code scode, ms.\"name\" lcode, mp.term, mp.\"level\" llevel, msmm.namemu lname\n" +
                        "from vimis.medical_care_procedure_settings_period_stages m\n" +
                        "left join vimis.medical_care_procedure_routes me on m.graph_id  =  me.id\n" +
                        "left join vimis.medical_care_procedure_detailed med on med.id=me.graph_id\n" +
                        "left join vimis.medical_care_procedure_registers_for_routes r on r.graph_id = me.id\n" +
                        "left join telmed.registertypes t on t.id = r.register_id\n" +
                        "left join vimis.medical_care_procedure_routes_stages st on st.id = m.stage_id\n" +
                        "left join vimis.medical_care_procedure_routes_substages su on su.id = m.start_state\n" +
                        "left join vimis.medical_care_procedure_routes_substages sub on sub.id = m.final_state\n" +
                        "left join dpc.mis_sp_mu msm  on m.moid  = msm.idmu\n" +
                        "left join vimis.medical_care_procedure_settings_service_period_stages mp on m.id  = mp.settingid\n" +
                        "left join dpc.medical_services ms on mp.serviceid = ms.id \n" +
                        "left join dpc.mis_sp_mu msmm on mp.idmu = msmm.idmu\n" +
                        "where \"period\" = '36' and med.\"name\" = '" + MarName + "';");
        while (sql.resultSet.next()) {
            SQLRegName = sql.resultSet.getString("rname");
            SQLMarName = sql.resultSet.getString("name");
            SQLOMPName = sql.resultSet.getString("stage");
            SQLPodOMPName = sql.resultSet.getString("short_name");
            SQLLevel = sql.resultSet.getString("level");
            SQLMO = sql.resultSet.getString("namemu");
            SQLNextPodOMPName = sql.resultSet.getString("nextname");
            sql.value = sql.resultSet.getString("period");
            SQLSCodeName = sql.resultSet.getString("scode");
            SQLCodeName = sql.resultSet.getString("lcode");
            SQLDateName = sql.resultSet.getString("term");
            SQLLevelName = sql.resultSet.getString("llevel");
            SQLMOName = sql.resultSet.getString("lname");
        }
        Assertions.assertEquals(SQLRegName, RegName, "Регистр не совпадает");
        Assertions.assertEquals(SQLMarName, MarName, "Наименование маршрута не совпадает");
        Assertions.assertEquals(SQLOMPName, OMPName, "ОМП не совпадает");
        Assertions.assertEquals(SQLPodOMPName, PodOMPName, "Подэтап ОМП не совпадает");
        Assertions.assertEquals(SQLLevel, "1", "Уровень не совпадает");
        Assertions.assertEquals(SQLMO, MOName, "Название МО не совпадает");
        Assertions.assertEquals(SQLNextPodOMPName, NextPodOMPName, "Следующий подэтап ОМП не совпадает");
        Assertions.assertEquals(sql.value, "36", "Нормативный срок не совпадает");
        Assertions.assertEquals(SQLSCodeName + " " + SQLCodeName, CodeName, "Code не совпадает");
        Assertions.assertEquals(SQLDateName, "0", "Нормативный срок не совпадает");
        Assertions.assertEquals(SQLLevelName, "1", "Нормативный срок не совпадает");
        Assertions.assertEquals(SQLMOName, MOName, "Название МО не совпадает");

        System.out.println("Удаляем Маршрут");
        inputWord(routeOMP.NameMarch, MarName);
        Thread.sleep(2000);
        ClickElement(routeOMP.Search);
        Thread.sleep(1500);
        ClickElement(routeOMP.Edit);
        ClickElement(routeOMP.DeleteLevel);
        ClickElement(routeOMP.DeleteLevelYes);
        ClickElement(routeOMP.Update);
        authorizationObject.LoadingTime(60);
        Thread.sleep(1500);
        ClickElement(routeOMP.Delete);
        ClickElement(routeOMP.DeleteYes);
    }

    @Test
    @Order(2)
    @Issue(value = "TEL-1904")
    @Link(name = "ТМС-1799", url = "https://team-1okm.testit.software/projects/5/tests/1799?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка аудита по ОМП")
    @Description("После изменнений проверяем аудит добавления/редактирования/удаления для ОМП")
    public void Access_1904 () throws InterruptedException, SQLException {

        /** Запоминаем контур, чтобы после вернуть значение*/
        NumberKing = KingNumber;

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
                "where types.code in ('ChangePmcRouteSetting', 'AddPmcRouteSetting', 'RemovePmcRouteSetting') order by id desc limit 1;");
        while (sql.resultSet.next()) {
            code = sql.resultSet.getString("code");
            data = sql.resultSet.getString("data");
            eventtypeid = sql.resultSet.getString("eventtypeid");
            moname = sql.resultSet.getString("moname");
            userid = sql.resultSet.getString("userid");
            userfullname = sql.resultSet.getString("userfullname");
        }
        Assertions.assertEquals(code, "RemovePmcRouteSetting", "code не равен RemovePmcRouteSetting");
        Assertions.assertEquals(data.contains("{\"New\": {\"Id\": 0"), true,
                "New долэен быть в data для RemovePmcRouteSetting");
        Assertions.assertNotEquals(data.contains("\"Old\": {\""), true,
                "Old не должно быть в data для RemovePmcRouteSetting");
        Assertions.assertEquals(eventtypeid, "45", "для RemovePmcRouteSetting eventtypeid должно быть 45");
        Assertions.assertEquals(moname, PMORequest, "moname должно быть " + PMORequest);
        Assertions.assertEquals(userid, PId, "userid не равно 25");
        Assertions.assertEquals(userfullname, PName,
                "userfullname должно быть равно " + PName);

        sql.StartConnection("select types.code, events.* from audit.telemed events\n" +
                "left join audit.eventtypes types on types.id = events.eventtypeid\n" +
                "where types.code in ('ChangePmcRouteSetting', 'AddPmcRouteSetting', 'RemovePmcRouteSetting') order by id desc limit 1 offset 1;");
        while (sql.resultSet.next()) {
            code = sql.resultSet.getString("code");
            data = sql.resultSet.getString("data");
            eventtypeid = sql.resultSet.getString("eventtypeid");
            moname = sql.resultSet.getString("moname");
            userid = sql.resultSet.getString("userid");
            userfullname = sql.resultSet.getString("userfullname");
        }
        Assertions.assertEquals(code, "ChangePmcRouteSetting", "code не равен ChangePmcRouteSetting");
        Assertions.assertEquals(data.contains("{\"New\": {\"Id\""), true,
                "New долженен быть в data для ChangePmcRouteSetting");
        Assertions.assertEquals(data.contains("\"Old\": {\""), true,
                "Old должно быть в data для ChangePmcRouteSetting");
        Assertions.assertEquals(eventtypeid, "43", "для ChangePmcRouteSetting eventtypeid должно быть 43");
        Assertions.assertEquals(moname, PMORequest, "moname должно быть " + PMORequest);
        Assertions.assertEquals(userid, PId, "userid не равно 25");
        Assertions.assertEquals(userfullname, PName,
                "userfullname должно быть равно " + PName);

        sql.StartConnection("select types.code, events.* from audit.telemed events\n" +
                "left join audit.eventtypes types on types.id = events.eventtypeid\n" +
                "where types.code in ('ChangePmcRouteSetting', 'AddPmcRouteSetting', 'RemovePmcRouteSetting') order by id desc limit 1 offset 2;");
        while (sql.resultSet.next()) {
            code = sql.resultSet.getString("code");
            data = sql.resultSet.getString("data");
            eventtypeid = sql.resultSet.getString("eventtypeid");
            moname = sql.resultSet.getString("moname");
            userid = sql.resultSet.getString("userid");
            userfullname = sql.resultSet.getString("userfullname");
        }
        Assertions.assertEquals(code, "AddPmcRouteSetting", "code не равен AddPmcRouteSetting");
        Assertions.assertEquals(data.contains("{\"New\": {\"Id\""), true,
                "New долженен быть в data для AddPmcRouteSetting");
        Assertions.assertNotEquals(data.contains("\"Old\": {\""), true,
                "Old не должно быть в data для AddPmcRouteSetting");
        Assertions.assertEquals(eventtypeid, "44", "для AddPmcRouteSetting eventtypeid должно быть 44");
        Assertions.assertEquals(moname, PMORequest, "moname должно быть " + PMORequest);
        Assertions.assertEquals(userid, PId, "userid не равно 25");
        Assertions.assertEquals(userfullname, PName,
                "userfullname должно быть равно " + PName);

        /** Меняем значение обратно */
        KingNumber = NumberKing;
    }
}
