package UI.TmTest.AccessUI.Directions;

import Base.SQL;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationArchived;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingArchived;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.IncomingArchive;
import UI.TmTest.PageObject.Directions.Kvots.IncomingUnfinished;
import Base.BaseAPI;
import Base.TestListenerApi;
import api.Before.Authorization;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isOneOf;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Протокол")
@Tag("РРП")
@Disabled
public class Access_1219_1256 extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    IncomingUnfinished incomingUnfinished;
    ConsultationUnfinished consultationUnfinished;
    ConsultationArchived consultationArchived;
    ConsultationOutgoingArchived consultationOutgoingArchived;
    EquipmentSchedule equipmentSchedule;
    IncomingArchive incomingArchive;
    SQL sql;
    public static String[] FIO;
    public static String Role;
    public static String RoleId;
    public static String Post;
    public static String PostId;
    public static String Specialization;
    public static String SpecializationId;
    public static String Id;
    public static String Doctype;
    public static String LocalUid;
    public static String org_signature_id;
    public static String surname;
    public static String name;
    public static String patrName;
    public static String snils;
    public static String typeConsul;
    public static String typeConsulId;
    public String URLRemd;
    public static String orgSignatureSum;
    public static String personalSignaturesSum;
    public static String uuid;
    public static String NumberConsul;
    public static String DirectionType;
    public static String created_datetime;
    public static String effectivetime;
    public static String status;
    public static String statusWeb;
    public static String errorsWeb;
    public static String formatProtocol;
    public static String version_doc;
    public static String pathology;
    public boolean OchnoeError;

    /**
     Для массовой отправки ТМК
     */
    public Integer NumberConsulId = 1;
    public boolean MassTMK;

    /**
     Для направления на диагностику
     */
    public static String protocol;
    public static String conclusion;
    public static String recommendations;
    public static String directionid;
    public static String performersnils;
    public static String performername;
    public static String performerlname;
    public static String performermname;
    public static String positionKvot;

    @Issue(value = "TEL-1219")
    @Issue(value = "TEL-1228")
    @Issue(value = "TEL-1521")
    @Issue(value = "TEL-1523")
    @Issue(value = "TEL-1709")
    @Issue(value = "TEL-2471")
    @Issue(value = "TEL-2532")
    @Issue(value = "TEL-2553")
    @Issue(value = "TEL-2607")
    @Issue(value = "TEL-2611")
    @Issue(value = "TEL-2694")
    @Issue(value = "TEL-2702")
    @Issue(value = "TEL-2741")
    @Issue(value = "TEL-2820")
    @Issue(value = "TEL-2839")
    @Issue(value = "TEL-2832")
    @Issue(value = "TEL-2833")
    @Issue(value = "TEL-2821")
    @Issue(value = "TEL-2840")
    @Issue(value = "TEL-2901")
    @Issue(value = "TEL-2915")
    @Issue(value = "TEL-3181")
    @Issue(value = "TEL-3183")
    @Issue(value = "TEL-3211")
    @Issue(value = "TEL-3315")
    @Issue(value = "TEL-3357")
    @Issue(value = "TEL-3400")
    @Issue(value = "TEL-4080")
    @Issue(value = "TEL-4110")
    @Link(name = "ТМС-1469", url = "https://team-1okm.testit.software/projects/5/tests/1469?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Link(name = "ТМС-1725", url = "https://team-1okm.testit.software/projects/5/tests/1725?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Link(name = "ТМС-1751", url = "https://team-1okm.testit.software/projects/5/tests/1751?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Подписи")
    @DisplayName("Подписание протокола удалённой консультации")
    @Description("Перейти в Консультации - Создать направление - Направление на консультацию - Создаём направление, указывая цель. Переходим в МО, куда отправили - входящие, переходим к ней, завершаем т отправляем протокол")
    public void Access_1219 () throws IOException, SQLException, InterruptedException, AWTException, XPathExpressionException, ParserConfigurationException, SAXException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        /** По заявке 3183 (Веха Назначения врача и редактирование ТМК)
         * Отправка в свою МО + Врач назначен = меняется врач консультант при завершении консультации на того, кто завершил
         * Отправка в свою МО + Врач не назначен = меняется врач консультант при завершении консультации на того, кто завершил
         * Отправка не в свою МО + Врач не назначен = меняется врач консультант при завершении консультации на того, кто завершил
         * Отправка не в свою МО + Врач нельзя назначить (так что без этого варианта) */

        System.out.println("Авторизуемся и переходим в создание направления на оборудование");
        System.out.println("1 проверка - создание направления на диагностику и отсутствие создания протокола");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", false,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select("Женская консультация"),
                authorizationObject.Select("детской урологии-андрологии"), directionsForQuotas.SelectCovid,
                "Врач не назначен");

        System.out.println("Переходим в МО куда отправили направление и завершаем его");
        if (KingNumber == 1 | KingNumber == 2) {
            AuthorizationMethod(authorizationObject.YATCKIV);
        }
        if (KingNumber == 4) {
            AuthorizationMethod(authorizationObject.Kondinsk);
        }
        ClickElement(incomingUnfinished.ConsultationWait);
        ClickElement(incomingUnfinished.FirstLineMO);
        ClickElement(incomingUnfinished.Button("Отклонить направление"));
        WaitElement(incomingUnfinished.CloseText);
        inputWord(driver.findElement(incomingUnfinished.CloseText), "Проверяю 12199");
        ClickElement(incomingUnfinished.CloseOutDirection2);
        Thread.sleep(2000);
        WaitNotElement(consultationUnfinished.AddConsultation);

        System.out.println("Устанавливаем расширение Крипто про");
        CryptoProMethod();

        System.out.println("2 проверка - создание консультации с целью очное консультирование");
        /** Если False то не выводим ошибку при выборе типа консультации Очное консультрование */
        OchnoeError = false;
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", true,
                directionsForQuotas.SelectResearch, directionsForQuotas.DoctorSearch(PName),
                authorizationObject.Select("Женская консультация"),
                authorizationObject.Select("детской урологии-андрологии"), directionsForQuotas.SelectOchnoe,
                "Зотин Андрей Владимирович");

        AddProtocolMethod(consultationUnfinished.FirstSelect, true, false, "Новый", "Врач", "Александр ", "Саферов ",
                "Николаевич ", "15748720095 ", false);

        /** Проверка с переотправкой документов (2532) */
        CheckArchived(true);

        /**  Отправка не в свою МО + Врач не назначен */
        System.out.println(
                "3 проверка - создание консультации с целью отличной от очное консультирование, в другое МО, без чек бокса (заявка 1521)");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", false,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select(""), authorizationObject.Select("детской урологии-андрологии"),
                directionsForQuotas.SelectCovid, "Врач не назначен");

        AddProtocolMethod(consultationUnfinished.FirstSelect, false, false, "Новый", "Врач", "Александр ", "Саферов ",
                "Николаевич ", "15748720095 ", false);

        /**  Отправка в свою МО + Врач не назначен */
        System.out.println(
                "4 проверка - создание консультации с Врачом консультаном - Врач не выбран, в своё МО (3183)");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", true,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select("Женская консультация"),
                authorizationObject.Select("детской урологии-андрологии"), directionsForQuotas.SelectCovid,
                "Врач не назначен");

        AddProtocolMethod(consultationUnfinished.FirstSelect, true, false, "Новый", "Врач", "Александр ", "Саферов ",
                "Николаевич ", "15748720095 ", false);

        /**  Отправка в свою МО + Врач назначен */
        System.out.println("5 проверка - создание консультации с Врачом консультаном - Врач выбран, в своё МО (3183)");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", true,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select("Женская консультация"),
                authorizationObject.Select("детской урологии-андрологии"), directionsForQuotas.SelectCovid,
                "Зотин Андрей Владимирович");

        AddProtocolMethod(consultationUnfinished.FirstSelect, true, false, "Новый", "Врач", "Александр ", "Саферов ",
                "Николаевич ", "15748720095 ", false);
    }

    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Подписи")
    @DisplayName("Отправка протокола направления на диагностику не в свою МО")
    @Description("Создаём направление на диагностику - отправляем по неё протокол - после проверяем БД и тело запроса. Также проверяем статусы консультации и переотправляем её")
    public void Access_1256_Direction () throws IOException, SQLException, InterruptedException, AWTException, XPathExpressionException, ParserConfigurationException, SAXException {

        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        System.out.println("Устанавливаем расширение Крипто про");
        CryptoProMethod();

        System.out.println("Создание напрвления на диагностику в другую МО, с чек боксом");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.DistrictDiagnosticWait, "Направление на диагностику", false,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select(""), authorizationObject.Select(""), null, "");

        AddProtocolKvotMethod(consultationUnfinished.FirstSelect, false, true, "Новый", "Врач", "Александр ",
                "Саферов ", "Николаевич ", "15748720095 ", false);

        //   CheckArchivedKvot(false);
    }

    @Issue(value = "TEL-4114")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Подписи")
    @DisplayName("Отправка протокола направления на диагностику в свою МО")
    @Description("Создаём направление на диагностику - отправляем по неё протокол - после проверяем БД и тело запроса. Также проверяем статусы консультации и переотправляем её")
    public void Access_1256_DirectionMyMo () throws IOException, SQLException, InterruptedException, AWTException, XPathExpressionException, ParserConfigurationException, SAXException {

        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        System.out.println("Устанавливаем расширение Крипто про");
        CryptoProMethod();

        System.out.println("Создание напрвления на диагностику в другую МО, с чек боксом");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.DistrictDiagnosticWait, "Направление на диагностику", true,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select(""), authorizationObject.Select(""), null, "");

        AddProtocolKvotMethod(consultationUnfinished.FirstSelect, true, true, "Новый", "Врач", "Александр ",
                "Саферов ", "Николаевич ", "15748720095 ", false);

        // CheckArchivedKvot(true);
    }

    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Подписи")
    @DisplayName("Отправка протокола консультации в свою МО")
    @Description("Создаём удалённую консультацию - отправляем по неё протокол - после проверяем БД и тело запроса. Также проверяем статусы консультации и переотправляем её")
    public void Access_1256_Consul () throws IOException, SQLException, InterruptedException, AWTException, XPathExpressionException, ParserConfigurationException, SAXException {

        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        consultationArchived = new ConsultationArchived(driver);

        System.out.println("Устанавливаем расширение Крипто про");
        CryptoProMethod();

        NumberConsulId = 1;
        System.out.println("Создание консультации с целью отличной от очное консультирование, в своё МО, с чек боксом");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", true,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select("Женская консультация"),
                authorizationObject.Select("детской урологии-андрологии"), directionsForQuotas.SelectCovid,
                "Зотин Андрей Владимирович");

        AddProtocolMethod(consultationUnfinished.FirstSelect, true, true, "Новый", "Врач", "Александр ", "Саферов ",
                "Николаевич ", "15748720095 ", false);

        /** Проверка с переотправкой документов (2532) */
        // CheckArchived(true);
    }

    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Подписи")
    @DisplayName("Отправка протокола консультации в чужую МО")
    @Description("Создаём удалённую консультацию - отправляем по неё протокол - после проверяем БД и тело запроса. Также проверяем статусы консультации и переотправляем её")
    public void Access_1256_Consul_NeMyMo () throws IOException, SQLException, InterruptedException, AWTException, XPathExpressionException, ParserConfigurationException, SAXException {

        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        consultationArchived = new ConsultationArchived(driver);

        System.out.println("Устанавливаем расширение Крипто про");
        CryptoProMethod();

        NumberConsulId = 1;
        System.out.println("Создание консультации с целью отличной от очное консультирование, в своё МО, с чек боксом");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", false,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select(""),
                authorizationObject.Select("детской урологии-андрологии"), directionsForQuotas.SelectCovid,
                "Врач не назначен");

        AddProtocolMethod(consultationUnfinished.FirstSelect, false, true, "Новый", "Врач", "Александр ", "Саферов ",
                "Николаевич ", "15748720095 ", false);

        /** Проверка с переотправкой документов (2532) */
        CheckArchived(false);
    }

    @Issue(value = "TEL-3400")
    @Test
    @Story("Подписи")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Массовая подпись ТМК")
    @Description("Создаём 2 удалённые консультации - завершаем их - Переходим в Архивные - выбираем Подписать консультации и подписываем сразу несколько - после проверяем бд")
    public void Access_3400_Consul () throws IOException, SQLException, InterruptedException, AWTException, XPathExpressionException, ParserConfigurationException, SAXException {

        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        consultationArchived = new ConsultationArchived(driver);
        MassTMK = true;

        System.out.println("Устанавливаем расширение Крипто про");
        CryptoProMethod();

        System.out.println("Создание консультации с целью отличной от очное консультирование, в своё МО, с чек боксом");
        AuthorizationMethod(authorizationObject.OKB);
        NumberConsulId = 1;
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", true,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select("Женская консультация"),
                authorizationObject.Select("детской урологии-андрологии"), directionsForQuotas.SelectCovid,
                "Зотин Андрей Владимирович");

        NumberConsulId = 2;
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", true,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select("Женская консультация"),
                authorizationObject.Select("детской урологии-андрологии"), directionsForQuotas.SelectCovid,
                "Зотин Андрей Владимирович");

        AddProtocolMethod(consultationUnfinished.FirstSelect, true, true, "Новый", "Врач", "Александр ", "Саферов ",
                "Николаевич ", "15748720095 ", false);

        /** Проверка с переотправкой документов (2532) */
        CheckArchived(true);
    }

    @Issue(value = "TEL-1256")
    @Issue(value = "TEL-1424")
    @Link(name = "ТМС-1470", url = "https://team-1okm.testit.software/projects/5/tests/1470?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Link(name = "ТМС-1511", url = "https://team-1okm.testit.software/projects/5/tests/1511?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Подписи")
    @DisplayName("Сохранение ЭЦП протокола направлений на диагностику")
    @Description("После подписания и отправки протокола, переходим в таблицы vimis.eds_signatures/ vimis.eds_personal_signatures/ vimis.eds_signers/ telmed.directions и проверяем значения")
    public void Access_1256_Diagnostick () throws IOException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        sql = new SQL();
        String IdOrg = null;

        System.out.println(
                "Переходим в vimis.eds_signatures и берём значение check_sum, сверяем значение подписи организации");
        System.out.println();
        sql.StartConnection(
                "SELECT * FROM vimis.eds_signatures WHERE id = '" + ReadPropFile("Test_1219_org_signature_id") + "';");
        while (sql.resultSet.next()) {
            IdOrg = sql.resultSet.getString("id");
            sql.value = sql.resultSet.getString("check_sum");
            Assertions.assertEquals(
                    ReadPropFile("Test_1219_orgSignatureSum"),
                    sql.value, "Чек сумма подписи организации не совпадает");
        }
        Integer IdVrach = Integer.valueOf(IdOrg) + 1;
        System.out.println(
                "Переходим в vimis.eds_signatures и берём значение check_sum уже у подписи врача, она следует сразу за подписью МО");
        sql.StartConnection("SELECT * FROM vimis.eds_signatures WHERE id = '" + IdVrach + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("check_sum");
            Assertions.assertEquals(
                    ReadPropFile("Test_1219_personalSignaturesSum"),
                    sql.value, "Чек сумма подписи врача не совпадает");
        }
        System.out.println(
                "Переходим в vimis.eds_personal_signatures и берём значение signer_id чтобы найти врача отправителя в vimis.eds_signers");
        sql.StartConnection("SELECT * FROM vimis.eds_personal_signatures WHERE signature_id = '" + IdVrach + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("signer_id");
        }
        System.out.println("Переходим в vimis.eds_signers и сверяем данные врача");
        sql.StartConnection(
                "SELECT x.id, x.role_id, s.name_role, x.last_name, x.first_name, x.middle_name, x.birthdate, x.snils, x.position_id, mwp.work_position, x.speciality_id, m.\"name\", x.department_oid FROM vimis.eds_signers x\n" +
                        "join dpc.signer_role s on x.role_id = s.id \n" +
                        "join dpc.med_worker_positions mwp on x.position_id = mwp.id \n" +
                        "join dpc.medical_and_pharmaceutical_specialties m on x.speciality_id = m.id \n" +
                        "WHERE x.id = " + sql.value + ";");
        while (sql.resultSet.next()) {
            String Roleid = sql.resultSet.getString("role_id");
            String role = sql.resultSet.getString("name_role");
            String Name = sql.resultSet.getString("first_name");
            String LastName = sql.resultSet.getString("last_name");
            String MiddleName = sql.resultSet.getString("middle_name");
            String SNILS = sql.resultSet.getString("snils");
            String Postid = sql.resultSet.getString("position_id");
            String post = sql.resultSet.getString("work_position");
            String Specialityid = sql.resultSet.getString("speciality_id");
            String speciality = sql.resultSet.getString("name");

            Assertions.assertEquals(role, ReadPropFile("Test_1219_Role"),
                    "Наименование Роли не совпадает");
            Assertions.assertEquals(Postid, ReadPropFile("Test_1219_PostId"),
                    "Код Должности не совпадает");
            Assertions.assertEquals(post, ReadPropFile("Test_1219_Post"),
                    "Наименование Должности не совпадает");
            Assertions.assertEquals(Specialityid,
                    ReadPropFile("Test_1219_SpecializationId"),
                    "Наименование Специальности не совпадает");
            Assertions.assertEquals(speciality,
                    ReadPropFile("Test_1219_Specialization"),
                    "Наименование Специальности не совпадает");
            Assertions.assertEquals(Name, ReadPropFile("Test_1219_name"),
                    "Имя Врача не совпадает");
            Assertions.assertEquals(LastName, ReadPropFile("Test_1219_surname"),
                    "Фамилия Врача не совпадает");
            Assertions.assertEquals(MiddleName, ReadPropFile("Test_1219_patrName"),
                    "Отчество Врача не совпадает");
            Assertions.assertEquals(SNILS, ReadPropFile("Test_1219_snils"),
                    "Снилс Врача не совпадает");
        }
    }

    @Issue(value = "TEL-1256")
    @Issue(value = "TEL-1424")
    @Link(name = "ТМС-1470", url = "https://team-1okm.testit.software/projects/5/tests/1470?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Link(name = "ТМС-1511", url = "https://team-1okm.testit.software/projects/5/tests/1511?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Подписи")
    @DisplayName("Сохранение ЭЦП протокола консультаций")
    @Description("После подписания и отправки протокола, переходим в таблицы vimis.eds_signatures/ vimis.eds_personal_signatures/ vimis.eds_signers/ telmed.directions и проверяем значения")
    public void Access_1256 () throws IOException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        sql = new SQL();
        String IdOrg = null;

        /* Меняем в зависимости от количества ТМК */
        MassTMK = false;

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                System.out.println("\n -------------Проверяем первый документ");
            } else {
                System.out.println("\n -------------Проверяем второй документ");
            }
            System.out.println(
                    "Переходим в vimis.eds_signatures и берём значение check_sum, сверяем значение подписи организации");
            sql.StartConnection(
                    "SELECT * FROM vimis.eds_signatures WHERE id = '" + ReadPropFile(
                            "Test_1219_org_signature_id_" + i + "") + "';");
            while (sql.resultSet.next()) {
                IdOrg = sql.resultSet.getString("id");
                sql.value = sql.resultSet.getString("check_sum");
                Assertions.assertEquals(
                        ReadPropFile("Test_1219_orgSignatureSum_" + i + ""),
                        sql.value, "Чек сумма подписи организации не совпадает");
            }
            Integer IdVrach = Integer.valueOf(IdOrg) + 1;
            System.out.println(
                    "Переходим в vimis.eds_signatures и берём значение check_sum уже у подписи врача, она следует сразу за подписью МО");
            sql.StartConnection("SELECT * FROM vimis.eds_signatures WHERE id = '" + IdVrach + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("check_sum");
                Assertions.assertEquals(
                        ReadPropFile("Test_1219_personalSignaturesSum_" + i + ""),
                        sql.value, "Чек сумма подписи врача не совпадает");
            }
            System.out.println(
                    "Переходим в vimis.eds_personal_signatures и берём значение signer_id чтобы найти врача отправителя в vimis.eds_signers");
            sql.StartConnection("SELECT * FROM vimis.eds_personal_signatures WHERE signature_id = '" + IdVrach + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("signer_id");
            }
            System.out.println("Переходим в vimis.eds_signers и сверяем данные врача");
            sql.StartConnection(
                    "SELECT x.id, x.role_id, s.name_role, x.last_name, x.first_name, x.middle_name, x.birthdate, x.snils, x.position_id, mwp.work_position, x.speciality_id, m.\"name\", x.department_oid FROM vimis.eds_signers x\n" +
                            "join dpc.signer_role s on x.role_id = s.id \n" +
                            "join dpc.med_worker_positions mwp on x.position_id = mwp.id \n" +
                            "join dpc.medical_and_pharmaceutical_specialties m on x.speciality_id = m.id \n" +
                            "WHERE x.id = " + sql.value + ";");
            while (sql.resultSet.next()) {
                String Roleid = sql.resultSet.getString("role_id");
                String role = sql.resultSet.getString("name_role");
                String Name = sql.resultSet.getString("first_name");
                String LastName = sql.resultSet.getString("last_name");
                String MiddleName = sql.resultSet.getString("middle_name");
                String SNILS = sql.resultSet.getString("snils");
                String Postid = sql.resultSet.getString("position_id");
                String post = sql.resultSet.getString("work_position");
                String Specialityid = sql.resultSet.getString("speciality_id");
                String speciality = sql.resultSet.getString("name");

                Assertions.assertEquals(role, ReadPropFile("Test_1219_Role"),
                        "Наименование Роли не совпадает");
                Assertions.assertEquals(Postid, ReadPropFile("Test_1219_PostId"),
                        "Код Должности не совпадает");
                Assertions.assertEquals(post, ReadPropFile("Test_1219_Post"),
                        "Наименование Должности не совпадает");
                Assertions.assertEquals(Specialityid,
                        ReadPropFile("Test_1219_SpecializationId"),
                        "Наименование Специальности не совпадает");
                Assertions.assertEquals(speciality,
                        ReadPropFile("Test_1219_Specialization"),
                        "Наименование Специальности не совпадает");
                Assertions.assertEquals(Name, ReadPropFile("Test_1219_name"),
                        "Имя Врача не совпадает");
                Assertions.assertEquals(LastName, ReadPropFile("Test_1219_surname"),
                        "Фамилия Врача не совпадает");
                Assertions.assertEquals(MiddleName, ReadPropFile("Test_1219_patrName"),
                        "Отчество Врача не совпадает");
                Assertions.assertEquals(SNILS, ReadPropFile("Test_1219_snils"),
                        "Снилс Врача не совпадает");
            }
            if (!MassTMK) {
                break;
            }
        }
    }

    @Issue(value = "TEL-1364")
    @Link(name = "ТМС-1470", url = "https://team-1okm.testit.software/projects/5/tests/1470?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Подписи")
    @DisplayName("Запоминание данных подписанта при отправке протокола")
    @Description("Перейти в Консультации - Создать направление - Направление на консультацию - Создаём направление, указывая цель. Переходим в МО, куда отправили - входящие, переходим к ней, завершаем и отправляем протокол. После проверяем таблицу  telmed.userplacework.role,position,speciality на добавленные значения из dpc.signer_role.id, dpc.med_worker_positions.id,dpc.medical_and_pharmaceutical_specialties.id. Обновляем страницу и проверяем, что на ui отображаются наименования, которые выбрали до это (запомнились), меняем значения и проверяем, что изменились значения в telmed.userplacework")
    public void Access_1364 () throws IOException, SQLException, InterruptedException, AWTException, XPathExpressionException, ParserConfigurationException, SAXException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);

        System.out.println("Устанавливаем расширение Крипто про");
        CryptoProMethod();

        System.out.println("Авторизуемся и переходим в создание направления на консультацию");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", true,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select("Женская консультация"),
                authorizationObject.Select("детской урологии-андрологии"), directionsForQuotas.SelectCovid,
                "Зотин Андрей Владимирович");

        AddProtocolMethod(consultationUnfinished.FirstSelect, true, true, "Новый", null, "Саферов", "Александр",
                "Николаевич", "15748720095 ", false);
        Thread.sleep(1500);

        System.out.println("Сверяем значения с таблицей telmed.userplacework");
        sql.StartConnection("select u.*, s.name_role, w.work_position, m.\"name\"  FROM telmed.userplacework u\n" +
                "join dpc.signer_role s on u.\"role\" = s.id \n" +
                "join dpc.med_worker_positions w on u.\"position\" = w.id \n" +
                "join dpc.medical_and_pharmaceutical_specialties m on u.speciality = m.id \n" +
                "join dpc.mis_sp_mu msm on u.placework = msm.idmu \n" +
                "where u.userid = " + PId + " and msm.namemu= 'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"' order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("name_role");
            Assertions.assertEquals(sql.value, Role, "Не совпадет роль с БД");
            sql.value = sql.resultSet.getString("work_position");
            Assertions.assertEquals(sql.value, Post, "Не совпадет должность с БД");
            sql.value = sql.resultSet.getString("name");
            Assertions.assertEquals(sql.value, Specialization, "Не совпадет специализация с БД");
        }

        System.out.println("Снова Авторизуемся и переходим в создание направления на консультацию");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.RemoteConsultationWait, "Удаленная консультация", true,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select("Женская консультация"),
                authorizationObject.Select("детской урологии-андрологии"), directionsForQuotas.SelectCovid,
                "Зотин Андрей Владимирович");

        System.out.println("Переходим в МО куда отправили консультацию и завершаем её");
        if (KingNumber == 1 | KingNumber == 2) {
            AuthorizationMethod(authorizationObject.YATCKIV);
        }
        if (KingNumber == 4) {
            AuthorizationMethod(authorizationObject.Kondinsk);
        }
        ClickElement(consultationUnfinished.UnfinishedWait);
        ClickElement(consultationUnfinished.FirstWait);
        ClickElement(consultationUnfinished.Closed);
        WaitElement(consultationUnfinished.ClosedText);
        uuid = String.valueOf(UUID.randomUUID());
        System.out.println(uuid);
        inputWord(driver.findElement(consultationUnfinished.ClosedText), uuid + "j");
        SelectClickMethod(consultationUnfinished.DataDay, consultationUnfinished.NextMonth);
        ClickElement(consultationUnfinished.Closed2);
        Thread.sleep(3000);

        System.out.println("Отправляем протокол");
        ClickElement(consultationUnfinished.AddConsultation);

        System.out.println("Выбираем подразделение");
        ClickElement(consultationUnfinished.DivisionProtocol);
        if (KingNumber != 4) {
            ClickElement(consultationUnfinished.SelectDivisionProtocol);
        } else {
            ClickElement(consultationUnfinished.SelectDivisionProtocoBuh);
        }
        ClickElement(consultationUnfinished.CheckBox);
        ClickElement(consultationUnfinished.Role);
        Thread.sleep(1500);
        String RoleUI = driver.findElement(consultationUnfinished.Selected).getText();
        Assertions.assertEquals(RoleUI, Role, "Выбранная роль не совпадает с ранее выбранной");
//        RoleUI = driver.findElement(consultationUnfinished.SecondSelect).getText();
//        ClickElement(consultationUnfinished.SecondSelect);
        ClickElement(consultationUnfinished.Post);
        Thread.sleep(1500);
        String PostUI = driver.findElement(consultationUnfinished.Selected).getText();
        Assertions.assertEquals(PostUI, Post, "Выбранная должность не совпадает с ранее выбранной");
        PostUI = driver.findElement(consultationUnfinished.SecondSelect).getText();
        ClickElement(consultationUnfinished.SecondSelect);
        ClickElement(consultationUnfinished.Specialization);
        Thread.sleep(1500);
        String SpecialisationUI = driver.findElement(consultationUnfinished.Selected).getText();
        Assertions.assertEquals(
                SpecialisationUI, Specialization, "Выбранная специализация не совпадает с ранее выбранной");
        SpecialisationUI = driver.findElement(consultationUnfinished.SecondSelect).getText();
        ClickElement(consultationUnfinished.SecondSelect);

        System.out.println("Выбираем сертификаты");
        ClickElement(consultationUnfinished.CertDoctor);
        ClickElement(consultationUnfinished.Cert("Новый"));
        ClickElement(consultationUnfinished.CertMO);
        ClickElement(consultationUnfinished.Cert("Новый"));
        ClickElement(consultationUnfinished.AddConsultation2("2"));
        Thread.sleep(5000);

        System.out.println("Сверяем значения с таблицей telmed.userplacework");
        sql.StartConnection("select u.*, s.name_role, w.work_position, m.\"name\"  FROM telmed.userplacework u\n" +
                "join dpc.signer_role s on u.\"role\" = s.id \n" +
                "join dpc.med_worker_positions w on u.\"position\" = w.id \n" +
                "join dpc.medical_and_pharmaceutical_specialties m on u.speciality = m.id \n" +
                "join dpc.mis_sp_mu msm on u.placework = msm.idmu \n" +
                "where u.userid = " + PId + " and msm.namemu= 'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"' order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("name_role");
            Assertions.assertEquals(sql.value, RoleUI, "Не совпадет роль с БД");
            sql.value = sql.resultSet.getString("work_position");
            Assertions.assertEquals(sql.value, PostUI, "Не совпадет должность с БД");
            sql.value = sql.resultSet.getString("name");
            Assertions.assertEquals(sql.value, SpecialisationUI, "Не совпадет специализация с БД");
        }
    }

    @Step("Метод для установки Крипто Про в расширение")
    public void CryptoProMethod () throws AWTException, InterruptedException {
        Robot r = new Robot();

        if (Yandex) {
            driver.get("https://addons.opera.com/ru/extensions/details/cryptopro-extension-for-cades-browser-plug-in/");
            Thread.sleep(10000);
        } else {
            driver.get("https://chrome.google.com/webstore/category/extensions");
            inputWord(driver.findElement(By.xpath("//input[@aria-label]")), "CryptoProo");
            driver.findElement(By.xpath("//input[@aria-label]")).sendKeys(Keys.ENTER);
            Thread.sleep(2000);
            ClickElement(By.xpath("//h1[contains(.,'Результаты поиска')]/following-sibling::div[1]/div[1]"));
            Thread.sleep(5000);
            ClickElement(By.xpath("//span[contains(.,'Установить')]"));
            Thread.sleep(3000);
            r.keyPress(KeyEvent.VK_LEFT);
            r.keyPress(KeyEvent.VK_ENTER);
            r.keyPress(KeyEvent.VK_ESCAPE);
            while (!isElementNotVisible(By.xpath(
                    "//span[contains(.,'Удалить из Chrome')]"))) {
                r.keyPress(KeyEvent.VK_ESCAPE);
                Thread.sleep(1500);
                ClickElement(By.xpath("//span[contains(.,'Установить')]"));
                Thread.sleep(1500);
                r.keyPress(KeyEvent.VK_LEFT);
                Thread.sleep(1500);
                r.keyPress(KeyEvent.VK_ENTER);
            }
            Thread.sleep(2000);
            r.keyPress(KeyEvent.VK_ESCAPE);
            Thread.sleep(2000);
        }
    }

    @Step("Метод для создания консультации")
    public void Access_1219Method (By Consultation, String Direction, boolean MyMO, By Research, By Doctor, By Division, By Profile, By Goal, String ConsulDoctor) throws InterruptedException, SQLException, IOException {
        Authorization authorization = new Authorization();
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        sql = new SQL();

        if (!Direction.equals("Направление на диагностику")) {
            String GoalString = String.valueOf(Goal);
            System.out.println(GoalString);
            InputPropFile("Direction_1256",
                    GoalString.substring(0, GoalString.length() - 3).substring(49));
        }

        /** Переход в создание консультации */
        ClickElement(directionsForQuotas.ConsultationWait);

        System.out.println("Создание консультации");
        ClickElement(directionsForQuotas.CreateWait);

        System.out.println("Выбираем консультацию");
        ClickElement(Consultation);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        Thread.sleep(1500);
        ClickElement(directionsForQuotas.NextWait);
        if (Direction == "Направление на диагностику") {

            System.out.println("Заполнение информации о направившем враче");
            WaitElement(directionsForQuotas.InfoDoctorWait);
            WaitElement(directionsForQuotas.SubmittingDoctorWait);
            Thread.sleep(2000);
            SelectClickMethod(
                    directionsForQuotas.SubmittingDoctorWait,
                    directionsForQuotas.FIO
            );
            Thread.sleep(1500);
            WaitNotElement(directionsForQuotas.loading);
            SelectClickMethod(
                    directionsForQuotas.Division,
                    directionsForQuotas.SelectDivision
            );
            Thread.sleep(1500);
            WaitNotElement(directionsForQuotas.loading);
            SelectClickMethod(
                    directionsForQuotas.DepartmentWait,
                    directionsForQuotas.SelectDepartment
            );
            Thread.sleep(1500);
            WaitNotElement(directionsForQuotas.loading);
            SelectClickMethod(
                    directionsForQuotas.Post,
                    directionsForQuotas.SelectPost
            );
            Thread.sleep(1500);
            WaitNotElement(directionsForQuotas.loading);
            SelectClickMethod(
                    directionsForQuotas.Specialization,
                    directionsForQuotas.SelectSpecializationFirst
            );
            Thread.sleep(1500);
            WaitNotElement(directionsForQuotas.loading);
            driver.findElement(directionsForQuotas.AnatomicalAreas).sendKeys("ис");
            ClickElement(authorizationObject.Select("Височная кость"));
            if (MyMO) {
                ClickElement(directionsForQuotas.MyMODirection);
                Thread.sleep(2000);
                SelectClickMethod(directionsForQuotas.MyDivision, directionsForQuotas.MySelectDivision);
            }
            if (!MyMO) {
                if (KingNumber == 1 | KingNumber == 2) {
                    SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirection);
                }
                if (KingNumber == 4) {
                    SelectClickMethod(directionsForQuotas.MODirection, directionsForQuotas.SelectMODirectionKon);
                }
            }
            directionsForQuotas.Diagnosis.sendKeys(Keys.SPACE);
            ClickElement(directionsForQuotas.SelectDiagnosisWait);
            SelectClickMethod(directionsForQuotas.Research, Research);
            SelectClickMethod(directionsForQuotas.BenefitCode, directionsForQuotas.SelectBenefitCode);
            WaitElement(directionsForQuotas.MassWait);
            inputWord(directionsForQuotas.Mass, "50");
            System.out.println("Вес пациента 5");

            /** Проверка цели направления (3211) */
            inputWord(driver.findElement(directionsForQuotas.GoalDirections), "Цель направления ");

            inputWord(directionsForQuotas.NamePatient, "Тестовыйй");
            inputWord(directionsForQuotas.LastNamePatient, "Тестт");
            inputWord(directionsForQuotas.MiddleNamePatient, "Тестовичч");
            actionElementAndClick(directionsForQuotas.NextPatient);

            /** Окно направления на диагностику*/
            WaitElement(directionsForQuotas.Header);
            WaitElement(directionsForQuotas.CreateDirectionWait);

            /** Проверка цели направления (3211) */
            WaitElement(directionsForQuotas.GoalDirection("Цель направления"));

            Thread.sleep(1000);
            actionElementAndClick(directionsForQuotas.CreateDirection);

            System.out.println("Прикрепление  файла");
            WaitElement(directionsForQuotas.FileWait);
            WaitElement(directionsForQuotas.AddFileWait);
            Thread.sleep(1000);
            java.io.File file = new File("src/test/resources/test.txt");
            directionsForQuotas.File.sendKeys(file.getAbsolutePath());
            Thread.sleep(500);
            ClickElement(directionsForQuotas.ReceptionWait);

            System.out.println("Запись на приём");
            WaitElement(equipmentSchedule.HeaderEquipmentSchedulesWait);
            WaitElement(equipmentSchedule.NextPageWait);
            Thread.sleep(1500);
            WaitNotElement3(equipmentSchedule.Loading, 30);
            if (!isElementNotVisible(equipmentSchedule.SlotsTrue)) {
                ClickElement(equipmentSchedule.NextPageWait);
                Thread.sleep(500);
            }
            int number = 0;
            while (!isElementNotVisible(equipmentSchedule.SlotsFreeWait) & number < 3) {
                number++;
                Thread.sleep(1500);
                WaitNotElement3(equipmentSchedule.Loading, 30);
                ClickElement(equipmentSchedule.NextPageWait);
                Thread.sleep(500);
                /* Условие нужно потому что, такое может быть при отсутствии расписания оборудования. Добавляем расписание*/
                if (!isElementNotVisible(equipmentSchedule.SlotsTrue)) {
                    System.out.println("Добавляем расписание с завтрашнего дня");
                    if (KingNumber != 4) {
                        // 17478 - X-OMAT, БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"
                        authorization.BodyEquipment("17478", "1.2.643.5.1.13.13.12.2.86.9003", "43");
                    } else {
                        // 48434 - CDR Kit, АУ ХМАО-Югры \"Кондинская районная стоматологическая поликлиника\"
                        authorization.BodyEquipment("48434", "1.2.643.5.1.13.13.12.2.86.8987", "43");
                    }
                    AuthorizationMethod(authorizationObject.OKB);
                    ClickElement(directionsForQuotas.ConsultationWait);
                    ClickElement(directionsForQuotas.FirstLine);
                    ClickElement(directionsForQuotas.AddReception);
                    Thread.sleep(1000);
                }
            }
            ClickElement(equipmentSchedule.SlotsFreeWait);
            Thread.sleep(1500);
            System.out.println("Выбрали свободный слот");

            if (KingNumber != 4) {
                WaitElement(equipmentSchedule.AddFileWait);
                file = new File("src/test/resources/test.txt");
                equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
                Thread.sleep(1500);
                WaitNotElement3(equipmentSchedule.loadingWriteTwo, 30);
            }
            ClickElement(equipmentSchedule.WriteTwo);
            Thread.sleep(1500);
            WaitNotElement3(authorizationObject.LoadingTrue("3"), 30);
        } else {
            System.out.println("Заполняем данные");
            SelectClickMethod(directionsForQuotas.DoctorWait, Doctor);
            if (!MyMO) {
                if (KingNumber == 1 | KingNumber == 2) {
                    SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMO);
                }
                if (KingNumber == 4) {
                    SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMOKon);
                }
            } else {
                ClickElement(directionsForQuotas.MyMO);
                SelectClickMethod(directionsForQuotas.MyDivision, Division);

            }
            if (KingNumber == 4) {
                Thread.sleep(4000);
            }
            SelectClickMethod(directionsForQuotas.ProfileWait, Profile);
            ClickElement(directionsForQuotas.Plan);

            /** Для заявки 3183 */
            if (!ConsulDoctor.equals("Врач не назначен")) {
                InputPropFile("IdDoctorConsulTrue_1256", "true");
                ClickElement(directionsForQuotas.SelectField("Врач консультант"));

                WaitElement(authorizationObject.SelectFirst);
                List<WebElement> select = driver.findElements(authorizationObject.SelectALL);
                List<String> Web = new ArrayList<>();
                for (int i = 0; i < select.size(); i++) {
                    Web.add(select.get(i).getAttribute("innerText"));
                }

                System.out.println("Сверяем значения с БД");
                List<String> RoleSQL = new ArrayList<>();
                sql.StartConnection(
                        "select u.id, u.sname, u.fname, u.mname, u.id, m.namemu, u2.profileid, u2.profilename, u3.id, s.depart_name from telmed.users u \n" +
                                "join telmed.usermedprofile u2 on u.id = u2.userid \n" +
                                "join telmed.userplacework u3 on u2.userplaceworkid = u3.id \n" +
                                "join dpc.mis_sp_mu m on u3.placework = m.idmu \n" +
                                "left join dpc.mo_subdivision_structure s on u3.departmentoid = s.depart_oid \n" +
                                "where s.depart_name = 'Женская консультация' and u2.profilename = 'детской урологии-андрологии' and m.namemu = 'БУ ХМАО-Югры \"Окружная клиническая больница\"';");
                while (sql.resultSet.next()) {
                    String sname = sql.resultSet.getString("sname");
                    String fname = sql.resultSet.getString("fname");
                    String mname = sql.resultSet.getString("mname");
                    String id = sql.resultSet.getString("id");
                    RoleSQL.add("Врач не назначен");
                    RoleSQL.add(sname + " " + fname + " " + mname);
                    InputPropFile("IdDoctorConsul_1256", id);
                }
                Assertions.assertEquals(Web, RoleSQL, "Значения Врача консультанта не совпадают с БД");

                /** Врач консультант (3183) */
                ClickElement(authorizationObject.Select(ConsulDoctor));
            } else {
                InputPropFile("IdDoctorConsulTrue_1256", "false");
            }
            SelectClickMethod(directionsForQuotas.Goal, Goal);

            /** Для заявки 3181 и 3315 */
            inputWord(driver.findElement(directionsForQuotas.TypeConsul), "конс");
            ClickElement(authorizationObject.Select("Консультация детского уролога-андролога"));
            inputWord(directionsForQuotas.Diagnos, "AA");
            Thread.sleep(1000);
            ClickElement(authorizationObject.SelectFirst);
            if (isElementNotVisible(directionsForQuotas.Text("Жалобы пациента"))) {
                inputWord(driver.findElement(directionsForQuotas.Text("Жалобы пациента")), "йцуу");
                inputWord(driver.findElement(directionsForQuotas.Text("Анамнез")), "йцуу");
                inputWord(driver.findElement(directionsForQuotas.Text("Объективное состояние")), "йцуу");
            }
            ClickElement(directionsForQuotas.NextConsul);
            ClickElement(directionsForQuotas.CreateConsul);

            System.out.println("Прикрепление  файла");
            WaitElement(directionsForQuotas.AddFilesWait);
            Thread.sleep(1000);
            File file = new File("src/test/resources/test.txt");
            directionsForQuotas.File.sendKeys(file.getAbsolutePath());
            Thread.sleep(500);
            ClickElement(directionsForQuotas.SendConsul);
            Thread.sleep(2000);

            sql.StartConnection(
                    "Select d.id, d.targetmoid, d.executorid, m.oid, c.id idType, c.name from telmed.directions d \n" +
                            "join dpc.mis_sp_mu m on d.targetmoid = m.idmu \n" +
                            "join dpc.consultation_types c on d.consultationtypeid = c.id order by d.id desc limit 1;");

            String executorid = null;
            String numberConsulId = null;

            while (sql.resultSet.next()) {
                executorid = sql.resultSet.getString("executorid");
                numberConsulId = sql.resultSet.getString("id");
            }
            if (NumberConsulId == 1) {
                InputPropFile("NumberConsulId_0_1256", numberConsulId);
            }
            if (NumberConsulId == 2) {
                InputPropFile("NumberConsulId_1_1256", numberConsulId);
            }

            /** Проверяем id Врача в БД (3183) */
            if (ConsulDoctor.equals("Врач не назначен")) {
                Assertions.assertEquals(executorid, null,
                        "Id Врача консультанта не выбрано, поэтому должно равняться null");
            } else {
                Assertions.assertEquals(executorid, ReadPropFile("IdDoctorConsul_1256"),
                        "Id Врача консультанта в БД не совпадает");
            }
        }
    }

    @Step("Метод для сбора данных из селекта")
    public void SelectAccessMethod (String Select, By NumberSelect, String SQL, String NameSQL, String Name) throws SQLException {
        authorizationObject = new AuthorizationObject(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        sql = new SQL();
        List<WebElement> select = driver.findElements(authorizationObject.SelectALL);
        List<String> Web = new ArrayList<>();
        for (int i = 0; i < select.size(); i++) {
            Web.add(select.get(i).getAttribute("innerText"));
        }
        Select = driver.findElement(NumberSelect).getAttribute("innerText")
        ;
        System.out.println("Сверяем значения с БД");
        List<String> RoleSQL = new ArrayList<>();
        sql.StartConnection(SQL);
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString(NameSQL);
            RoleSQL.add(sql.value);
        }
        Assertions.assertEquals(Web, RoleSQL, "Значения " + Name + " не совпадают с БД");
    }

    @Step("Метод для сбора названия и id выбранной роли, должности и специальности")
    public String ChoiceMethod (String SQL, String NameSQL, String Name) throws SQLException {
        sql = new SQL();
        System.out.println("Берём значение из БД");
        sql.StartConnection(SQL);
        String Id = null;
        while (sql.resultSet.next()) {
            if (Name == "Роль") {
                Id = sql.resultSet.getString(NameSQL);
                return Id;
            }
            if (Name == "Должность") {
                Id = sql.resultSet.getString(NameSQL);
                return Id;
            }
            if (Name == "Специальность") {
                Id = sql.resultSet.getString(NameSQL);
                return Id;
            }
        }
        return Id;
    }

    @Step("Метод для отправки протокола в консультации с проверкой выбранных значений в бд и в запросе отправки самого протокола")
    public void AddProtocolMethod (By Select, boolean MyMO, boolean CurrentUser, String NameCert, String RoleName, String NameUser, String SurNameUser, String LastNameUser, String Snils, boolean PereAdd) throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        sql = new SQL();
        String numberOff = null;

        /** Узнаем формат отправки протокола (2446))*/
        if (GetSettings().get("result.formatForTelemedProtocol") == Integer.valueOf("1")) {
            formatProtocol = "8";
        } else {
            formatProtocol = "40";
        }
        /** Отправляем сразу несколько ТМК или нет (3400) */
        if (!PereAdd) {
            System.out.println("Переходим в МО куда отправили консультацию и завершаем её");
            if (!MyMO) {
                if (KingNumber == 1 | KingNumber == 2) {
                    AuthorizationMethod(authorizationObject.YATCKIV);
                }
                if (KingNumber == 4) {
                    AuthorizationMethod(authorizationObject.Kondinsk);
                }
            } else {
                AuthorizationMethod(authorizationObject.OKB);
            }
            for (int i = 0; i < 2; i++) {
                ClickElement(consultationUnfinished.UnfinishedWait);
                ClickElement(consultationUnfinished.DESK);
                Thread.sleep(1500);
                ClickElement(consultationUnfinished.Consuls(ReadPropFile("NumberConsulId_" + i + "_1256")));

                /** Берём номер конультации, для проверки, что он добавляется в бд (2523) */
                WaitElement(consultationUnfinished.NumberConsultation);
                NumberConsul = driver.findElement(consultationUnfinished.NumberConsultation).getText().substring(20);
                System.out.println(NumberConsul + " - Номер Консультации");

                ClickElement(consultationUnfinished.Closed);
                WaitElement(consultationUnfinished.ClosedText);
                uuid = String.valueOf(UUID.randomUUID());
                InputPropFile("Test_1219_" + i + "_uuid", uuid);
                System.out.println(uuid);
                inputWord(driver.findElement(consultationUnfinished.ClosedText), uuid + "j");
                SelectClickMethod(consultationUnfinished.DataDay, consultationUnfinished.NextMonth);
                ClickElement(consultationUnfinished.Closed2);
                Thread.sleep(3000);

                /** Проверяем id Врача консультанта, должен установиться того, кто завершил (Работает если отправили консультацию в собственную МО, если в другую, то консультант остаётся) */
                sql.StartConnection(
                        "Select d.id, d.targetmoid, d.executorid, m.oid, c.id idType, c.name from telmed.directions d \n" +
                                "join dpc.mis_sp_mu m on d.targetmoid = m.idmu \n" +
                                "join dpc.consultation_types c on d.consultationtypeid = c.id where d.id = '" + ReadPropFile(
                                "NumberConsulId_" + i + "_1256") + "';");

                String executorid = null;
                while (sql.resultSet.next()) {
                    executorid = sql.resultSet.getString("executorid");
                }

                /** Проверяем id Врача в БД (5885 это Тестировщик под которой авторизуемся) (3183) */
                Assertions.assertEquals(executorid, PId,
                        "Id Врача консультанта в БД должно смениться на того, кто завершил");

                if (!MassTMK) {
                    System.out.println("Отправляем протокол");
                    ClickElement(consultationUnfinished.AddConsultation);
                    break;
                }
            }
        } else {
            ClickElement(consultationUnfinished.PereAddConsultation);
        }
        if (MassTMK) {
            ClickElement(consultationArchived.ArchivedWait);
            Thread.sleep(1500);
            WaitNotElement3(authorizationObject.Loading, 60);
            ClickElement(consultationArchived.DESK);
            Thread.sleep(2000);
            ClickElement(consultationUnfinished.SignedTMK);
            ClickElement(consultationUnfinished.ConsulsCheckBox(ReadPropFile("NumberConsulId_0_1256")));
            ClickElement(consultationUnfinished.ConsulsCheckBox(ReadPropFile("NumberConsulId_1_1256")));
            ClickElement(consultationUnfinished.AddTmk);
        }
        WaitElement(consultationUnfinished.LastNameProtocol);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);
        if (!CurrentUser) {
            System.out.println("Заполняем Фамилия");
            inputWord(driver.findElement(consultationUnfinished.LastNameProtocol), SurNameUser);
            System.out.println("Заполняем Имя");
            inputWord(driver.findElement(consultationUnfinished.NameProtocol), NameUser);
            System.out.println("Заполняем Отчество");
            inputWord(driver.findElement(consultationUnfinished.MiddleNameProtocol), LastNameUser);
            System.out.println("Заполняем Снилс");
            inputWord(driver.findElement(consultationUnfinished.SnilsProtocol), Snils);
            System.out.println("Выбираем подразделение");
            ClickElement(consultationUnfinished.DivisionProtocol);
            if (KingNumber != 4) {
                ClickElement(consultationUnfinished.SelectDivisionProtocol);
            } else {
                ClickElement(consultationUnfinished.SelectDivisionProtocoBuh);
            }
        } else {
            System.out.println("Выбираем подразделение");
            ClickElement(consultationUnfinished.DivisionProtocol);
            if (KingNumber != 4) {
                ClickElement(consultationUnfinished.SelectDivisionProtocol);
            } else {
                Thread.sleep(1500);
                ClickElement(consultationUnfinished.SelectDivisionProtocoBuh);
            }
            if (!isElementNotVisible(consultationUnfinished.CheckBoxCheck)) {
                ClickElement(consultationUnfinished.CheckBox);
            }
        }

        /** дополнительно проверяем отображение должностей и ролей по заявке (2607) */
        System.out.println("Берём все значения Роли");
        ClickElement(consultationUnfinished.Role);
        Thread.sleep(1500);
        WaitElementTime(Select, 500);
        SelectAccessMethod(Role, consultationUnfinished.FirstSelect, "SELECT * FROM dpc.signer_role s\n" +
                "right join dpc.signature_rules_remd sr on s.id = sr.\"role\"\n" +
                "where sr.doc_kind ='" + formatProtocol + "';", "name_role", "Роли");
        if (RoleName == null) {
            Role = driver.findElement(Select).getText();
        } else {
            Role = RoleName;
        }
        InputPropFile("Test_1219_Role", Role);
        ClickElement(consultationUnfinished.RoleSelect(Role));
        RoleId = ChoiceMethod("SELECT * FROM dpc.signer_role where name_role = '" + Role + "';", "code_role", "Роль");
        System.out.println(Role);
        System.out.println(RoleId);

        System.out.println("Берём все значения Должности");
        ClickElement(consultationUnfinished.Post);
        Thread.sleep(1500);
        WaitElement(Select);
        SelectAccessMethod(Post, consultationUnfinished.FirstSelect, "SELECT * FROM dpc.med_worker_positions;",
                "work_position",
                "Должности");
        Post = driver.findElement(Select).getText();
        InputPropFile("Test_1219_Post", Post);
        ClickElement(Select);
        PostId = ChoiceMethod(
                "SELECT * FROM dpc.med_worker_positions where work_position = '" + Post + "';", "id",
                "Должность"
        );
        System.out.println(Post);
        System.out.println(PostId);
        InputPropFile("Test_1219_PostId", PostId);
        System.out.println("Берём все значения Специальности");
        ClickElement(consultationUnfinished.Specialization);
        Thread.sleep(1500);
        WaitElement(Select);
        SelectAccessMethod(Specialization, consultationUnfinished.FirstSelect,
                "SELECT * FROM dpc.medical_and_pharmaceutical_specialties;", "name", "Специальности");
        Specialization = driver.findElement(Select).getText();
        InputPropFile("Test_1219_Specialization", Specialization);
        ClickElement(Select);
        SpecializationId = ChoiceMethod(
                "SELECT * FROM dpc.medical_and_pharmaceutical_specialties where name = '" + Specialization + "' limit 1;",
                "id", "Специальность"
        );
        System.out.println(Specialization);
        System.out.println(SpecializationId);
        InputPropFile("Test_1219_SpecializationId", SpecializationId);

        System.out.println("Выбираем сертификаты");
        ClickElement(consultationUnfinished.CertDoctor);
        ClickElement(consultationUnfinished.Cert(NameCert));
        ClickElement(consultationUnfinished.CertMO);
        ClickElement(consultationUnfinished.Cert(NameCert));

        System.out.println("Выбираем тип консультации для заявки (2694)");
        System.out.println(getShadow(driver.findElement(consultationUnfinished.TypeConsul)));
        typeConsul = getShadow(driver.findElement(consultationUnfinished.TypeConsul));

        if (TextUtils.isEmpty(typeConsul)) {
            ClickElement(consultationUnfinished.TypeConsul);
            inputWord(driver.findElement(consultationUnfinished.TypeConsul), "конс");
            WaitElement(Select);

            typeConsul = driver.findElement(Select).getText();

            ClickElement(Select);
            System.out.println(typeConsul);
        }
        InputPropFile("Test_1219_typeConsul", typeConsul);

        System.out.println("Берём Id тип консультации для заявки (2694)");
        sql.StartConnection("Select * from dpc.consultation_types where \"name\" = '" + typeConsul + "';");
        while (sql.resultSet.next()) {
            typeConsulId = sql.resultSet.getString("id");
        }
        Thread.sleep(1500);
        WaitNotElement3(consultationUnfinished.AddConsultation2Loading, 20);
        if (!PereAdd) {
            if (MassTMK) {
                ClickElement(consultationUnfinished.AddConsultation2("1"));
            } else {
                ClickElement(consultationUnfinished.AddConsultation2("2"));
            }
            WaitNotElement3(consultationUnfinished.AddConsultation2Loading, 30);
        } else {
            ClickElement(consultationUnfinished.PereAddConsultation2);
            WaitNotElement3(consultationUnfinished.PereAddConsultation2Loading, 30);
        }
        if (OchnoeError) {
            WaitElement(authorizationObject.AlertTrue("Произошла ошибка при формировании протокола"));
        } else {
            Thread.sleep(10000);
            authorizationObject.LoadingTime(20);
            if (!MassTMK) {
                WaitElement(consultationUnfinished.StatusConsultation);
            }

            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    System.out.println(
                            "---Проверяем добавление в таблицу vimis.remd_sent_result для первого документа");
                } else {
                    System.out.println(
                            "---Проверяем добавление в таблицу vimis.remd_sent_result для второго документа");
                }
                System.out.println("Проверяем добавление в таблицу vimis.remd_sent_result");
                Integer number = null;
                Integer count = 0;
                if (count != 0) {
                    sql.PrintSQL = false;
                }

                if (i == 0 & MassTMK) {
                    numberOff = "1";
                }
                if (i == 1 & MassTMK | !MassTMK) {
                    numberOff = "0";
                }

                while (number == null & count < 100) {
                    sql.StartConnection(
                            "SELECT * FROM vimis.remd_sent_result r\n" +
                                    "join telmed.directions d on d.id = r.direction_id \n" +
                                    "where d.id = '" + ReadPropFile("NumberConsulId_" + i + "_1256") + "';");
                    while (sql.resultSet.next()) {
                        Id = sql.resultSet.getString("id");
                        Doctype = sql.resultSet.getString("doctype");
                        LocalUid = sql.resultSet.getString("local_uid");
                        org_signature_id = sql.resultSet.getString("org_signature_id");
                        created_datetime = sql.resultSet.getString("created_datetime").substring(0, 10);
                        sql.value = sql.resultSet.getString("direction_id");
                        version_doc = sql.resultSet.getString("version_doc");
                    }
                    if (sql.value != null & sql.value.equals(ReadPropFile("NumberConsulId_" + i + "_1256"))) {
                        number = 1;
                    }
                    count++;
                }
                sql.PrintSQL = true;
                InputPropFile("Test_1219_org_signature_id_" + i + "", org_signature_id);
                InputPropFile("Test_1219_created_datetime_" + i + "", created_datetime);
                InputPropFile("Test_1219_LocalUid_" + i + "", LocalUid);
                InputPropFile("Test_1219_version_doc_" + i + "", version_doc);
                Assertions.assertEquals(created_datetime, Date, "Дата не равняется текущей дате");

                /** Проверка по заявке (2523) */
                Assertions.assertEquals(sql.value,
                        ReadPropFile("NumberConsulId_" + i + "_1256"), "Номер консультации не добавился");
                System.out.println("LocalUid - " + LocalUid);

                System.out.println(
                        "Переходим в Направления - Консультации - Входящие - Архивные к МО в Которую отправили");
                if (!MyMO) {
                    if (KingNumber == 1 | KingNumber == 2) {
                        AuthorizationMethod(authorizationObject.YATCKIV);
                    }
                    if (KingNumber == 4) {
                        AuthorizationMethod(authorizationObject.Kondinsk);
                    }
                } else {
                    AuthorizationMethod(authorizationObject.OKB);
                }
                ClickElement(consultationArchived.ArchivedWait);
                Thread.sleep(1500);
                WaitNotElement3(authorizationObject.Loading, 60);
                ClickElement(consultationArchived.DESK);
                ClickElement(By.xpath("//tbody/tr[1]/td[1][contains(.,'" + ReadPropFile(
                        "NumberConsulId_0_1256") + "')]"));

                /** Проверка по заявке (4080) */
                sql.StartConnection("SELECT * FROM telmed.directions  where id = '" + ReadPropFile(
                        "NumberConsulId_" + i + "_1256") + "';");
                while (sql.resultSet.next()) {
                    DirectionType = sql.resultSet.getString("directiontype");
                }

                WaitElement(consultationUnfinished.Protocols);
                String DirectionTypeWeb = driver.findElement(consultationUnfinished.Protocols).getText();
                Assertions.assertEquals(DirectionType, "2");
                Assertions.assertEquals(DirectionTypeWeb, "Протокол телемедицинской консультации (CDA) Редакция 1.pdf",
                        "Название для Протокола консультации должно быть - Протокол телемедицинской консультации (CDA) Редакция 1.pdf");

                sql.StartConnection(
                        "SELECT * FROM vimis.remdadditionalinfo WHERE smsid ='" + Id + "';");
                while (sql.resultSet.next()) {
                    effectivetime = sql.resultSet.getString("effectivetime").substring(0, 10);
                }
                InputPropFile("Test_1219_effectivetime_" + i + "", effectivetime);
                Assertions.assertEquals(created_datetime, effectivetime, "Дата не равняется текущей дате");
                System.out.println(LocalUid);
                if (CurrentUser) {
                    System.out.println("Берём значения доктора из бд");
                    sql.StartConnection("SELECT * FROM telmed.users WHERE sname = '" + PLastNameGlobal + "';");
                    while (sql.resultSet.next()) {
                        name = sql.resultSet.getString("fname");
                        patrName = sql.resultSet.getString("mname");
                        surname = sql.resultSet.getString("sname");
                        snils = sql.resultSet.getString("snils");
                    }

                    System.out.println("Убираем в snils пробелы и тире");
                    String chars = "- !";
                    for (char c : chars.toCharArray()) {
                        snils = snils.replace(String.valueOf(c), "");
                    }
                } else {
                    name = NameUser.substring(0, NameUser.length() - 1);
                    patrName = LastNameUser.substring(0, LastNameUser.length() - 1);
                    surname = SurNameUser.substring(0, SurNameUser.length() - 1);
                    snils = Snils.substring(0, Snils.length() - 1);
                }
                InputPropFile("Test_1219_name", name);
                InputPropFile("Test_1219_patrName", patrName);
                InputPropFile("Test_1219_surname", surname);
                InputPropFile("Test_1219_snils", snils);

                System.out.println(name);
                System.out.println(patrName);
                System.out.println(surname);
                System.out.println(snils);

                System.out.println("Сверяем тело запроса в РЭМД");
                Thread.sleep(2500);
                Token = TokenInit();
                JsonPath response = given()
                        .filter(new AllureRestAssured())
                        .header("Authorization", "Bearer " + Token)
                        .param("LocalUid", LocalUid)
                        .log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .get(ApiVimis + "/api/rremd")
                        .prettyPeek()
                        .then()
                        .statusCode(200)
                        .extract().jsonPath();

                String PereResult = "";
                if (PereAdd) {
                    PereResult = "result[1]";
                } else {
                    PereResult = "result[0]";
                }
                assertThat(response.get("" + PereResult + "." + DocumentDto + "localUid"), equalTo("" + LocalUid + ""));

                /** Узнаем формат отправки протокола (2446))*/
                if (GetSettings().get("result.formatForTelemedProtocol") == Integer.valueOf("1")) {
                    assertThat(response.get("" + PereResult + "." + DocumentDto + "kind.code"), equalTo("8"));
                } else {
                    assertThat(response.get("" + PereResult + "." + DocumentDto + "kind.code"), equalTo("40"));
                }

                assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.surname"),
                        equalTo("Тестировщик"));
                assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.name"), equalTo("Тест"));
                assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.patrName"), equalTo("Тестович"));
                assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.snils"), equalTo("15979025720"));
                orgSignatureSum = response.get("" + PereResult + "." + DocumentDto + "orgSignature.checksum");
                personalSignaturesSum = response.get(
                        "" + PereResult + "." + DocumentDto + "personalSignatures[0].signature.checksum");
                assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.role.code"),
                        equalTo("" + RoleId + ""));
                assertThat(response.get(
                                "" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.position.code"),
                        equalTo(ReadPropFile("Test_1219_PostId")));
                assertThat(
                        response.get(
                                "" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.speciality.code"),
                        equalTo("" + SpecializationId + ""));
                assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.surname"),
                        equalTo("" + surname + ""));
                assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.name"),
                        equalTo("" + name + ""));
                assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.patrName"),
                        equalTo("" + patrName + ""));
                assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.snils"),
                        equalTo("" + snils + ""));

                /** Проврека по заявке 2611 */
                if (CurrentUser) {
                    Assertions.assertNotNull(
                            response.get(
                                    "" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.birthDate"),
                            "Дата рождения должна присутствовать, когда выбран текущий пользователь (2611)");
                } else {
                    Assertions.assertNull(
                            response.get(
                                    "" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.birthDate"),
                            "Дата рождения не должна присутствовать, когда выбран не текущий пользователь (2611)");
                }
                InputPropFile("Test_1219_orgSignatureSum_" + i + "", orgSignatureSum);
                InputPropFile("Test_1219_personalSignaturesSum_" + i + "",
                        personalSignaturesSum);

                /** Проверяем заполнение данных автора в документе (2741) */
                JsonPath responses = given()
                        .filter(new AllureRestAssured())
                        .header("Authorization", "Bearer " + Token)
                        .param("localUid", ReadPropFile("Test_1219_LocalUid_" + i + ""))
                        .contentType(ContentType.JSON)
                        .when()
                        .get(HostAddress + "/api/smd/document")
                        .prettyPeek()
                        .then()
                        .statusCode(200)
                        .extract().jsonPath();
                String Base64 = responses.getString("result[0].document");
                byte[] decoded = java.util.Base64.getDecoder().decode(Base64);
                String xml = new String(decoded, StandardCharsets.UTF_8);

                System.out.println("Запсываем полученный xml в файл");
                PrintWriter out = new PrintWriter("src/test/resources/ignored/1219_test.xml");
                out.println(xml);
                out.close();

                /** В теле запроса и в самом документе должности берутся из разных справочников, так что значение кода должности отличается */

                String Post2 = Post.substring(0, 1).toUpperCase() + Post.substring(1).toLowerCase();

                sql.StartConnection("SELECT * FROM dpc.medical_position WHERE \"name\" = '" + Post2 + "'");
                while (sql.resultSet.next()) {
                    PostId = sql.resultSet.getString("id");
                }

                System.out.println("Берём версию Типа консультации (2915) ");
                sql.StartConnection("SELECT * FROM dpc.consultation_types offset " + i + " limit 1;");
                String versionConsul = null;
                while (sql.resultSet.next()) {
                    versionConsul = sql.resultSet.getString("version");
                }

                System.out.println("Сверяем значения автора в файле");
                /** Снилс может быть в 2 форматах поэтому сравниваем 2 значения */
                assertThat(
                        getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/author/assignedAuthor/id[2]/@extension"),
                        isOneOf(snils, snilsSet(snils)));
                Assertions.assertEquals(PostId,
                        getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/author/assignedAuthor/code/@code"),
                        "Код должности в файле не совпадает");
                Assertions.assertEquals(Post2,
                        getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/author/assignedAuthor/code/@displayName"),
                        "Наименование должности в файле не совпадает");
                Assertions.assertEquals("NI", getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/author/assignedAuthor/telecom/@nullFlavor"), "Телефон в файле не совпадает");
                Assertions.assertEquals(surname, getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/author/assignedAuthor/assignedPerson/name/family/text()"),
                        "Фамилия в файле не совпадает");
                Assertions.assertEquals(name, getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/author/assignedAuthor/assignedPerson/name/given[1]/text()"),
                        "Имя в файле не совпадает");
                Assertions.assertEquals(patrName, getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/author/assignedAuthor/assignedPerson/name/given[2]/text()"),
                        "Отчество в файле не совпадает");

                System.out.println("Сверяем значения Типа консультации в файле (2694) ");
                Assertions.assertEquals(typeConsul, getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/documentationOf/serviceEvent/code/@displayName"),
                        "Тип консультации в файле не совпадает");
                Assertions.assertEquals(typeConsulId,
                        getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/documentationOf/serviceEvent/code/@code"),
                        "Тип консультации в файле не совпадает");

                Assertions.assertEquals(typeConsul, getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/component//section[title='ОБЩИЕ СВЕДЕНИЯ']/text//tr[3]/td[2]/content/text()"),
                        "Тип консультации в файле не совпадает");

                Assertions.assertEquals(typeConsul, getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/component//section[title='ОБЩИЕ СВЕДЕНИЯ']/entry[1]//value/@displayName"),
                        "Тип консультации в файле не совпадает");
                Assertions.assertEquals(typeConsulId, getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/component//section[title='ОБЩИЕ СВЕДЕНИЯ']/entry[1]//value/@code"),
                        "Тип консультации в файле не совпадает");

                System.out.println("Сверяем значение версии Типа консультации в файле (2915) ");
                Assertions.assertEquals(versionConsul, getXml("src/test/resources/ignored/1219_test.xml",
                                "//code[@codeSystemName='Типы консультаций']/@codeSystemVersion"),
                        "Версия типа консультации в файле не совпадает");
                Assertions.assertEquals(versionConsul, getXml("src/test/resources/ignored/1219_test.xml",
                                "//entry/observation/value[@displayName='" + typeConsul + "']/@codeSystemVersion"),
                        "Версия типа консультации в файле не совпадает");

                System.out.println("Сверяем Место проведения (4110)");
                Assertions.assertEquals("Дистанционно", getXml("src/test/resources/ignored/1219_test.xml",
                                "/ClinicalDocument/component//section[title='ОБЩИЕ СВЕДЕНИЯ']/text[1]//content[@ID='DOC_3']/text()"),
                        "Место проведения для консультации должно быть - Дистационно");

                System.out.println("Сверяем значениея в файле по 3133 заявке");
                GetRRP(PatientGuid, "");

                /** "Кем выдан" паспорт */
                if (Response.getString("PatientIdentity[1].Issuer") == null) {
                    Assertions.assertEquals("РФ", getXml("src/test/resources/ignored/1219_test.xml",
                                    "//ClinicalDocument/recordTarget/patientRole/id[2]/@assigningAuthorityName"),
                            "Кем выдан паспорт в файле не совпадает");
                }

                /** Уникальный идентификатор МИС */
                sql.StartConnection(
                        "Select d.id, d.targetmoid, d.executorid, m.oid, c.id idType, c.name from telmed.directions d \n" +
                                "join dpc.mis_sp_mu m on d.targetmoid = m.idmu \n" +
                                "join dpc.consultation_types c on d.consultationtypeid = c.id where d.local_uid = '" + LocalUid + "';");

                String oid = null;
                String idType = null;
                String nameType = null;

                while (sql.resultSet.next()) {
                    oid = sql.resultSet.getString("oid");
                    idType = sql.resultSet.getString("idType");
                    nameType = sql.resultSet.getString("name");
                }
                Assertions.assertTrue(getXml("src/test/resources/ignored/1219_test.xml",
                        "//author/assignedAuthor/id[1]/@root").contains(oid));

                /** Проверяем тип консультации в БД (3181) */
                Assertions.assertEquals(idType, typeConsulId, "Тип консультации в БД не совпадает");
                Assertions.assertEquals(nameType, typeConsul, "Название Типа консультации в БД не совпадает");

                /** Пол пациента */
                if (Response.getString("Sex.$") == "1") {
                    Assertions.assertEquals("1", getXml("src/test/resources/ignored/1219_test.xml",
                                    "//patient/administrativeGenderCode/@code"),
                            "Пол пациента в файле не совпадает");
                    Assertions.assertEquals("Мужской", getXml("src/test/resources/ignored/1219_test.xml",
                                    "//patient/administrativeGenderCode/@displayName"),
                            "Пол пациента в файле не совпадает");
                } else if (Response.getString("Sex.$") == "2") {
                    Assertions.assertEquals("2", getXml("src/test/resources/ignored/1219_test.xml",
                                    "//patient/administrativeGenderCode/@code"),
                            "Пол пациента в файле не совпадает");
                    Assertions.assertEquals("Женский", getXml("src/test/resources/ignored/1219_test.xml",
                                    "//patient/administrativeGenderCode/@displayName"),
                            "Пол пациента в файле не совпадает");
                } else if (Response.getString("Sex.$") == "") {
                    Assertions.assertEquals("3", getXml("src/test/resources/ignored/1219_test.xml",
                                    "//patient/administrativeGenderCode/@code"),
                            "Пол пациента в файле не совпадает");
                    Assertions.assertEquals("Неопределенный", getXml("src/test/resources/ignored/1219_test.xml",
                                    "//patient/administrativeGenderCode/@displayName"),
                            "Пол пациента в файле не совпадает");
                }

                /** Дата создания */
                String dateXml = getXml("src/test/resources/ignored/1219_test.xml",
                        "//effectiveTime/@value").substring(0, 8);

                String params[] = {"LocalUid", LocalUid};
                Api(ApiVimis + "/api/rremd", "get", params, null, "", 200, true);
                String date = Response.getString("" + PereResult + "." + DocumentDto + "creationDateTime").substring(0,
                        10).replaceAll("-", "");
                Assertions.assertEquals(dateXml, date, "Дата в xml и в теле запроса Крэмд не совпадает");
                if (!MassTMK) {
                    break;
                }
            }
        }
    }

    @Step("Метод для отправки протокола в борудовании с проверкой выбранных значений в бд и в запросе отправки самого протокола")
    public void AddProtocolKvotMethod (By Select, boolean MyMO, boolean CurrentUser, String NameCert, String RoleName, String NameUser, String SurNameUser, String LastNameUser, String Snils, boolean PereAdd) throws SQLException, InterruptedException, IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        authorizationObject = new AuthorizationObject(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        incomingArchive = new IncomingArchive(driver);
        sql = new SQL();

        /** Узнаем формат отправки протокола (2446)) */
        if (GetSettings().get("result.formatForTelemedProtocol") == Integer.valueOf("1")) {
            formatProtocol = "8";
        } else {
            formatProtocol = "40";
        }

        if (!PereAdd) {
            System.out.println("Переходим в МО куда отправили консультацию и Соглавсовываем её");
            if (!MyMO) {
                if (KingNumber == 1 | KingNumber == 2) {
                    AuthorizationMethod(authorizationObject.YATCKIV);
                }
                if (KingNumber == 4) {
                    AuthorizationMethod(authorizationObject.Kondinsk);
                }
            } else {
                AuthorizationMethod(authorizationObject.OKB);
            }
            ClickElement(incomingUnfinished.ConsultationWait);
            Thread.sleep(1500);
            WaitNotElement3(authorizationObject.Loading, 30);
            ClickElement(incomingUnfinished.DESK);
            Thread.sleep(1500);
            ClickElement(incomingUnfinished.SearchMO("БУ ХМАО-Югры \"Окружная клиническая больница\"", "Отправлен"));
            //ClickElement(incomingUnfinished.SearchID("50676"));

            /** Берём номер конультации, для проверки, что он добавляется в бд (2523) */
            Thread.sleep(1500);
            WaitElement(incomingUnfinished.NumberConsultation);
            NumberConsul = driver.findElement(incomingUnfinished.NumberConsultation).getText().substring(19);

            /** Проверка цели направления (3211) */
            WaitElement(incomingUnfinished.Meaning("Цель направления", "Цель направления"));

            if (MyMO) {
                ClickElement(incomingUnfinished.Button("Выполнить исследование"));
            } else {
                ClickElement(incomingUnfinished.Button("Согласовать"));
            }
            Thread.sleep(4000);

            /** Проверяем статус 15 Запрос согласован (3357) */
            sql.StartConnection("Select * from telmed.directions where id = '" + NumberConsul + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
            }
            if (MyMO) {
                Assertions.assertEquals(sql.value, "3",
                        "После нажатия кнопки Выполнить исследования для своей МО, статус не меняется и сотаётся равным 3 (Отправлено)");
            } else {
                Assertions.assertEquals(sql.value, "15",
                        "После нажатия кнопки Согласовать, статус должен быть 15 (Запрос согласован)");
                WaitElement(incomingUnfinished.Button("Выполнить исследование"));
                ClickElement(incomingUnfinished.Button("Выполнить исследование"));
            }

            uuid = String.valueOf(UUID.randomUUID());
            InputPropFile("Test_1219_uuid", uuid);
            System.out.println("UUID который используем в подписании протокола - " + uuid);
            WaitElement(incomingUnfinished.Text("Описание"));

            /** Изменённое окно по заявке 3983 */
            inputWord(driver.findElement(incomingUnfinished.Text("Описание")), uuid + "j");
            inputWord(driver.findElement(incomingUnfinished.Text("Заключение")), uuid + "j");
            inputWord(driver.findElement(incomingUnfinished.Text("Рекомендации")), uuid + "j");

            System.out.println("Сохранить и подписать");
            ClickElement(incomingUnfinished.Save);

            Thread.sleep(1500);
            WaitNotElement3(incomingUnfinished.Loading, 30);
        } else {
            ClickElement(consultationUnfinished.PereAddConsultation);
        }
        WaitElement(consultationUnfinished.LastNameProtocol);
        if (!CurrentUser) {
            System.out.println("Заполняем Фамилия");
            inputWord(driver.findElement(consultationUnfinished.LastNameProtocol), SurNameUser);
            System.out.println("Заполняем Имя");
            inputWord(driver.findElement(consultationUnfinished.NameProtocol), NameUser);
            System.out.println("Заполняем Отчество");
            inputWord(driver.findElement(consultationUnfinished.MiddleNameProtocol), LastNameUser);
            System.out.println("Заполняем Снилс");
            inputWord(driver.findElement(consultationUnfinished.SnilsProtocol), Snils);
        } else {
            ClickElement(consultationUnfinished.CheckBox);
        }
        System.out.println("Выбираем подразделение");
        ClickElement(consultationUnfinished.DivisionProtocol);
        if (KingNumber != 4) {
            ClickElement(consultationUnfinished.SelectDivisionProtocol);
        } else {
            Thread.sleep(1500);
            ClickElement(consultationUnfinished.SelectDivisionProtocoBuh);
        }

        /** дополнительно проверяем отображение должностей и ролей по заявке (2607) */
        System.out.println("Берём все значения Роли");
        ClickElement(consultationUnfinished.Role);
        Thread.sleep(1500);
        WaitElementTime(Select, 500);
        SelectAccessMethod(Role, consultationUnfinished.FirstSelect, "SELECT * FROM dpc.signer_role s\n" +
                "right join dpc.signature_rules_remd sr on s.id = sr.\"role\"\n" +
                "where sr.doc_kind ='" + formatProtocol + "';", "name_role", "Роли");
        if (RoleName == null) {
            Role = driver.findElement(Select).getText();
        } else {
            Role = RoleName;
        }
        InputPropFile("Test_1219_Role", Role);
        ClickElement(consultationUnfinished.RoleSelect(Role));
        RoleId = ChoiceMethod("SELECT * FROM dpc.signer_role where name_role = '" + Role + "';", "code_role", "Роль");
        System.out.println(Role);
        System.out.println(RoleId);

        System.out.println("Берём все значения Должности");
        ClickElement(consultationUnfinished.Post);
        Thread.sleep(1500);
        WaitElement(Select);
        SelectAccessMethod(Post, consultationUnfinished.FirstSelect, "SELECT * FROM dpc.med_worker_positions;",
                "work_position",
                "Должности");
        Post = driver.findElement(Select).getText();
        InputPropFile("Test_1219_Post", Post);
        ClickElement(Select);
        PostId = ChoiceMethod(
                "SELECT * FROM dpc.med_worker_positions where work_position = '" + Post + "';", "id",
                "Должность"
        );
        System.out.println(Post);
        System.out.println(PostId);
        InputPropFile("Test_1219_PostId", PostId);
        System.out.println("Берём все значения Специальности");
        ClickElement(consultationUnfinished.Specialization);
        Thread.sleep(1500);
        WaitElement(Select);
        SelectAccessMethod(Specialization, consultationUnfinished.FirstSelect,
                "SELECT * FROM dpc.medical_and_pharmaceutical_specialties;", "name", "Специальности");
        Specialization = driver.findElement(Select).getText();
        InputPropFile("Test_1219_Specialization", Specialization);
        ClickElement(Select);
        SpecializationId = ChoiceMethod(
                "SELECT * FROM dpc.medical_and_pharmaceutical_specialties where name = '" + Specialization + "' limit 1;",
                "id", "Специальность"
        );
        System.out.println(Specialization);
        System.out.println(SpecializationId);
        InputPropFile("Test_1219_SpecializationId", SpecializationId);

        System.out.println("Выбираем сертификаты");
        ClickElement(consultationUnfinished.CertDoctor);
        ClickElement(consultationUnfinished.Cert(NameCert));
        ClickElement(consultationUnfinished.CertMO);
        ClickElement(consultationUnfinished.Cert(NameCert));

        if (!PereAdd) {
            ClickElement(consultationUnfinished.AddConsultation2("1"));
        } else {
            ClickElement(consultationUnfinished.PereAddConsultation2);
        }
        Thread.sleep(3000);

        System.out.println("Проверяем добавление в таблицу vimis.remd_sent_result");
        Integer number = null;
        Integer count = 0;
        sql.PrintSQL = false;
        while (number == null & count < 50) {
            sql.StartConnection(
                    "SELECT * FROM vimis.remd_sent_result  where  created_datetime > '" + Date + " 00:00:00.888 +0500' order by id desc limit 1;");
            while (sql.resultSet.next()) {
                Id = sql.resultSet.getString("id");
                Doctype = sql.resultSet.getString("doctype");
                LocalUid = sql.resultSet.getString("local_uid");
                org_signature_id = sql.resultSet.getString("org_signature_id");
                created_datetime = sql.resultSet.getString("created_datetime").substring(0, 10);
                sql.value = sql.resultSet.getString("direction_id");
                version_doc = sql.resultSet.getString("version_doc");
            }
            if (sql.value != null && sql.value.equals(NumberConsul)) {
                number = 1;
            }
            count++;
        }
        sql.PrintSQL = true;
        InputPropFile("Test_1219_org_signature_id", org_signature_id);
        InputPropFile("Test_1219_created_datetime", created_datetime);
        InputPropFile("Test_1219_LocalUid", LocalUid);
        InputPropFile("Test_1219_version_doc", version_doc);
        Assertions.assertEquals(created_datetime, Date, "Дата не равняется текущей дате");

        /** Проверка по заявке (2523) */
        Assertions.assertEquals(sql.value, NumberConsul, "Номер консультации не добавился");
        System.out.println("LocalUid - " + LocalUid);

        /** Проверка по заявке (2821) */
        Assertions.assertEquals(Doctype, "110", "Тип консультации не верный");

        System.out.println("Переходим в Направления - Консультации - Входящие - Архивные у МО в Которую отправили");
        if (!MyMO) {
            if (KingNumber == 1 | KingNumber == 2) {
                AuthorizationMethod(authorizationObject.YATCKIV);
            }
            if (KingNumber == 4) {
                AuthorizationMethod(authorizationObject.Kondinsk);
            }
        } else {
            AuthorizationMethod(authorizationObject.OKB);
        }
        ClickElement(incomingArchive.IncomingArchiveWait);
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.Loading, 30);
        ClickElement(incomingArchive.DESK);
        ClickElement(By.xpath("//tbody/tr[1]/td[1][contains(.,'" + NumberConsul + "')]"));

        /** Проверка по заявке (4080) */
        sql.StartConnection("SELECT * FROM telmed.directions  where id = '" + NumberConsul + "';");
        while (sql.resultSet.next()) {
            DirectionType = sql.resultSet.getString("directiontype");
        }

        WaitElement(incomingUnfinished.Protocols);
        String DirectionTypeWeb = driver.findElement(incomingUnfinished.Protocols).getText();
        Assertions.assertEquals(DirectionType, "1");
        Assertions.assertEquals(DirectionTypeWeb, "Протокол инструментального исследования.pdf",
                "Название для Протокола направления должно быть - Протокол инструментального исследования.pdf");

        /** Выполненное иследование сохраняет данные в telmed.diagnosticresults (2820) */
        System.out.println("Проверяем заполнение данных в telmed.diagnosticresults");
        sql.StartConnection("SELECT d.*, mp.\"name\", s.\"name\" pathology FROM telmed.diagnosticresults d\n" +
                "left join dpc.medical_position mp on d.\"position\" = mp.id \n" +
                "left join telmed.diagnosticpathology dia on d.directionid = dia.directionid\n" +
                "left join telmed.s1mbegi1t7 s on dia.pathologyid = s.id\n" +
                "where d.directionid = '" + NumberConsul + "';");
        while (sql.resultSet.next()) {
            protocol = sql.resultSet.getString("protocol");
            conclusion = sql.resultSet.getString("conclusion");
            recommendations = sql.resultSet.getString("recommendations");
            directionid = sql.resultSet.getString("directionid");
            performersnils = sql.resultSet.getString("performersnils");
            performername = sql.resultSet.getString("performername");
            performerlname = sql.resultSet.getString("performerlname");
            performermname = sql.resultSet.getString("performermname");
            positionKvot = sql.resultSet.getString("name");
            pathology = sql.resultSet.getString("pathology");
        }

        Assertions.assertEquals(protocol, uuid, "Протокол не совпадает");
        Assertions.assertEquals(conclusion, uuid, "Заключение не совпадает");
        Assertions.assertEquals(recommendations, uuid, "Рекомендации не совпадает");
        Assertions.assertEquals(directionid, NumberConsul, "Номер направления не совпадает");
        // Авторизуемся через Тестировщика, значит и данные его
        Assertions.assertEquals(performersnils, PSnils, "СНИЛС исполнителя не совпадает");
        Assertions.assertEquals(performername, "Тест", "Имя не совпадает");
        Assertions.assertEquals(performerlname, "Тестировщик", "Фамилия не совпадает");
        Assertions.assertEquals(performermname, "Тестович", "Отчество не совпадает");

        // Проверяем должность испольнителя
        Session = driver.manage().getCookieNamed(".AspNetCore.Session");
        TelemedC1 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC1");
        TelemedC2 = driver.manage().getCookieNamed(".AspNet.Core.TelemedC2");
        Telemed = driver.manage().getCookieNamed(".AspNet.Core.Telemed");
        cookies = driver.manage().getCookies();
        String moUrl = null;
        if (MyMO) {
            moUrl = PIdMoRequest;
        } else {
            moUrl = PIdMoTarget;
        }

        Api(HostAddressWeb + "/api/tmk/filter/mos/" + moUrl + "/users/" + PId + "/positions", "get", null, null, "", 200, false);
        String positionKvotResponse = Response.getString("result[0].workPositionName");
         Assertions.assertEquals(positionKvot, positionKvotResponse,
           "Должность исполнителя не совпадает в telmed.diagnosticresults");

        //Тут ошибка, в старой версии протокола это поле заполнялось добавим в 25.3 #Доделать
        //  Assertions.assertEquals(pathology, "Гепатоз", "Потология не совпадает");

        sql.StartConnection(
                "SELECT * FROM vimis.remdadditionalinfo WHERE smsid ='" + Id + "';");
        while (sql.resultSet.next()) {
            effectivetime = sql.resultSet.getString("effectivetime").substring(0, 10);
        }
        InputPropFile("Test_1219_effectivetime", effectivetime);
        Assertions.assertEquals(created_datetime, effectivetime, "Дата не равняется текущей дате");
        System.out.println(LocalUid);
        if (CurrentUser) {
            System.out.println("Берём значения доктора из бд");
            sql.StartConnection("SELECT * FROM telmed.users WHERE sname = '" + PLastNameGlobal + "';");
            while (sql.resultSet.next()) {
                name = sql.resultSet.getString("fname");
                patrName = sql.resultSet.getString("mname");
                surname = sql.resultSet.getString("sname");
                snils = sql.resultSet.getString("snils");
            }

            System.out.println("Убираем в snils пробелы и тире");
            String chars = "- !";
            for (char c : chars.toCharArray()) {
                snils = snils.replace(String.valueOf(c), "");
            }
        } else {
            name = NameUser.substring(0, NameUser.length() - 1);
            patrName = LastNameUser.substring(0, LastNameUser.length() - 1);
            surname = SurNameUser.substring(0, SurNameUser.length() - 1);
            snils = Snils.substring(0, Snils.length() - 1);
        }
        InputPropFile("Test_1219_name", name);
        InputPropFile("Test_1219_patrName", patrName);
        InputPropFile("Test_1219_surname", surname);
        InputPropFile("Test_1219_snils", snils);

        System.out.println(name);
        System.out.println(patrName);
        System.out.println(surname);
        System.out.println(snils);

        System.out.println("Сверяем тело запроса в РЭМД");
        Thread.sleep(2500);
        Token = TokenInit();

        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(ApiVimis + "/api/rremd?LocalUid=" + LocalUid)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath();

        String PereResult = "";
        if (PereAdd) {
            PereResult = "result[1]";
        } else {
            PereResult = "result[0]";
        }

        /** Узнаем формат отправки протокола (2446) */
        assertThat(response.get("" + PereResult + "." + DocumentDto + "localUid"), equalTo("" + LocalUid + ""));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "kind.code"), equalTo("110"));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.surname"), equalTo("Тестировщик"));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.name"), equalTo("Тест"));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.patrName"), equalTo("Тестович"));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "patient.snils"), equalTo("15979025720"));
        orgSignatureSum = response.get("" + PereResult + "." + DocumentDto + "orgSignature.checksum");
        personalSignaturesSum = response.get(
                "" + PereResult + "." + DocumentDto + "personalSignatures[0].signature.checksum");
        assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.role.code"),
                equalTo("" + RoleId + ""));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.position.code"),
                equalTo("" + PostId + ""));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.speciality.code"),
                equalTo("" + SpecializationId + ""));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.surname"),
                equalTo("" + surname + ""));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.name"),
                equalTo("" + name + ""));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.patrName"),
                equalTo("" + patrName + ""));
        assertThat(response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.snils"),
                equalTo("" + snils + ""));

        /** Проврека по заявке 2611 */
        if (CurrentUser) {
            Assertions.assertNotNull(
                    response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.birthDate"),
                    "Дата рождения должна присутствовать, когда выбран текущий пользователь (2611)");
        } else {
            Assertions.assertNull(
                    response.get("" + PereResult + "." + DocumentDto + "personalSignatures[0].signer.birthDate"),
                    "Дата рождения не должна присутствовать, когда выбран не текущий пользователь (2611)");
        }
        InputPropFile("Test_1219_orgSignatureSum", orgSignatureSum);
        InputPropFile("Test_1219_personalSignaturesSum", personalSignaturesSum);

        /** Проверяем заполниен данных автора в документе (2741) */
        JsonPath responses = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .param("localUid", ReadPropFile("Test_1219_LocalUid"))
                .contentType(ContentType.JSON)
                .when()
                .get(HostAddress + "/api/smd/document")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath();
        String Base64 = responses.getString("result[0].document");
        byte[] decoded = java.util.Base64.getDecoder().decode(Base64);
        String xml = new String(decoded, StandardCharsets.UTF_8);

        System.out.println("Запсываем полученный xml в файл");
        PrintWriter out = new PrintWriter("src/test/resources/ignored/1219_test.xml");
        out.println(xml);
        out.close();

        /** В теле запроса и в самом документе должности берутся из разных справочников, так что значение кода должности отличается */
        String Post2 = Post.substring(0, 1).toUpperCase() + Post.substring(1).toLowerCase();

        sql.StartConnection("SELECT * FROM dpc.medical_position WHERE \"name\" = '" + Post2 + "'");
        while (sql.resultSet.next()) {
            PostId = sql.resultSet.getString("id");
        }

        System.out.println("Сверяем значения автора в файле");
        /** Снилс может быть в 2 форматах поэтому сравниваем 2 значения */
        assertThat(
                getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/author/assignedAuthor/id[2]/@extension"),
                isOneOf(snils, snilsSet(snils)));
        Assertions.assertEquals(PostId,
                getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/author/assignedAuthor/code/@code"),
                "Код должности в файле не совпадает");
        Assertions.assertEquals(Post2,
                getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/author/assignedAuthor/code/@displayName"),
                "Наименование должности в файле не совпадает");

        Assertions.assertEquals(surname, getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/author/assignedAuthor/assignedPerson/name/family/text()"),
                "Фамилия в файле не совпадает");
        Assertions.assertEquals(name, getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/author/assignedAuthor/assignedPerson/name/given[1]/text()"),
                "Имя в файле не совпадает");

        System.out.println(
                "Сверяем значения Типа консультации в файле, если не указан тип консультации, то берём по профилю авторизованного пользователя в таблице telmed.profiletypematching (2694) ");
        Assertions.assertEquals("Инструментальное исследование", getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/documentationOf/serviceEvent/code/@displayName"),
                "Тип консультации в файле не совпадает");
        Assertions.assertEquals("3",
                getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/documentationOf/serviceEvent/code/@code"),
                "Тип консультации в файле не совпадает");

        Assertions.assertEquals("HMP01", getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/component//section[title='Общие сведения']/text//tr[4]/td[2]/content/text()"),
                "Номер исследования в файле не совпадает");

        System.out.println("Сверяем Место проведения (4111)");
        sql.StartConnection("select rm.namefull  from telmed.directions d\n" +
                "join dpc.mis_sp_mu m on d.targetmoid = m.idmu \n" +
                "join dpc.reestr_mo rm  on m.\"oid\" = rm.\"oid\" \n" +
                "where d.id = '" + NumberConsul + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("namefull");
        }

        Assertions.assertEquals(sql.value, getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/component//section[title='Общие сведения']/text//tr[3]/td[2]/content/text()"),
                "Место проведения для консультации должно быть - " + sql.value);

        System.out.println("Сверяем значениея в файле по 3133 заявке");
        GetRRP(PatientGuid, "");

        /** Уникальный идентификатор МИС */
        sql.StartConnection("Select d.id, d.targetmoid, m.oid, c.id idType, c.name from telmed.directions d \n" +
                "left join dpc.mis_sp_mu m on d.targetmoid = m.idmu \n" +
                "left join dpc.consultation_types c on d.consultationtypeid = c.id where d.local_uid = '" + LocalUid + "';");

        String oid = null;

        while (sql.resultSet.next()) {
            oid = sql.resultSet.getString("oid");
        }
        Assertions.assertTrue(getXml("src/test/resources/ignored/1219_test.xml",
                "//author/assignedAuthor/id[1]/@root").contains(oid));

        /** Пол пациента */
        if (Response.getString("Sex.$") == "1") {
            Assertions.assertEquals("1", getXml("src/test/resources/ignored/1219_test.xml",
                            "//patient/administrativeGenderCode/@code"),
                    "Пол пациента в файле не совпадает");
            Assertions.assertEquals("Мужской", getXml("src/test/resources/ignored/1219_test.xml",
                            "//patient/administrativeGenderCode/@displayName"),
                    "Пол пациента в файле не совпадает");
        } else if (Response.getString("Sex.$") == "2") {
            Assertions.assertEquals("2", getXml("src/test/resources/ignored/1219_test.xml",
                            "//patient/administrativeGenderCode/@code"),
                    "Пол пациента в файле не совпадает");
            Assertions.assertEquals("Женский", getXml("src/test/resources/ignored/1219_test.xml",
                            "//patient/administrativeGenderCode/@displayName"),
                    "Пол пациента в файле не совпадает");
        } else if (Response.getString("Sex.$") == "") {
            Assertions.assertEquals("3", getXml("src/test/resources/ignored/1219_test.xml",
                            "//patient/administrativeGenderCode/@code"),
                    "Пол пациента в файле не совпадает");
            Assertions.assertEquals("Неопределенный", getXml("src/test/resources/ignored/1219_test.xml",
                            "//patient/administrativeGenderCode/@displayName"),
                    "Пол пациента в файле не совпадает");
        }

        /** Дата создания */
        String dateXml = getXml("src/test/resources/ignored/1219_test.xml",
                "//effectiveTime/@value").substring(0, 8);

        String params[] = {"LocalUid", LocalUid};
        Api(ApiVimis + "/api/rremd", "get", params, null, "", 200, true);
        String date = Response.getString("" + PereResult + "." + DocumentDto + "creationDateTime").substring(0,
                10).replaceAll("-", "");
        Assertions.assertEquals(dateXml, date, "Дата в xml и в теле запроса Крэмд не совпадает");
    }

    @Step("Метод проверки добавления записи в архивные, в МО, из которой/в которую отправляли (заявка 1709) + переотправка документа")
    public void CheckArchived (boolean MyMO) throws InterruptedException, SQLException, IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        consultationArchived = new ConsultationArchived(driver);
        consultationOutgoingArchived = new ConsultationOutgoingArchived(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        sql = new SQL();

        System.out.println("Берём ID консультации");
        sql.StartConnection(
                "Select * from telmed.directions where local_uid = '" + ReadPropFile("Test_1219_LocalUid_0") + "';");
        while (sql.resultSet.next()) {
            NumberConsul = sql.resultSet.getString("id");
        }

        System.out.println("Переходим в Направления - Консультации - Входящие - Архивные к МО в Которую отправили");
        if (!MyMO) {
            if (KingNumber == 1 | KingNumber == 2) {
                AuthorizationMethod(authorizationObject.YATCKIV);
            }
            if (KingNumber == 4) {
                AuthorizationMethod(authorizationObject.Kondinsk);
            }
        } else {
            AuthorizationMethod(authorizationObject.OKB);
        }
        ClickElement(consultationArchived.ArchivedWait);
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.Loading, 60);
        ClickElement(consultationArchived.DESK);
        ClickElement(By.xpath("//tbody/tr[1]/td[1][contains(.,'" + ReadPropFile(
                "NumberConsulId_0_1256") + "')]"));

        System.out.println("Проверяем, та ли это кансультация");
        WaitElement(consultationArchived.TextConclusion);
        driver.findElement(consultationArchived.TextConclusion).getText().equals(
                "" + ReadPropFile("Test_1219_0_uuid") + "");

        /** Проверка с переотправкой документов (2532) */
        System.out.println("Дожидаемся смены статуса");
        String error = null;
        sql.PrintSQL = false;
        while (error == null) {
            sql.StartConnection("Select * from vimis.remd_sent_result where local_uid = '" + ReadPropFile(
                    "Test_1219_LocalUid_0") + "';");
            while (sql.resultSet.next()) {
                error = sql.resultSet.getString("status");
            }
        }
        /** Даже с ошибочным протоколом может быть статус Success от РЭМД (они могут отключить проверку), в этом случае нет смысла переотправлять документ */
        if (!error.contains("success")) {
            System.out.println("------------ Ошибочный документ получил статус error");
            System.out.println("Статсус меняется не сразу, дождёмся когда будет статус 24 при статусе error от КРЭМД");
            sql.PrintSQL = false;
            error = "21";
            while (error == "21") {
                sql.StartConnection(
                        "select d.id, d.status, d.local_uid from telmed.directions d where local_uid = '" + ReadPropFile(
                                "Test_1219_LocalUid_0") + "';");
                while (sql.resultSet.next()) {
                    error = sql.resultSet.getString("status");
                    System.out.println(error);
                }
            }
            sql.PrintSQL = true;
            Thread.sleep(6000);

            System.out.println("Мы отправили протокол с ошибкой - проверяем нужный статус в бд и на веб");
            sql.StartConnection(
                    "select d.id, d.status, d.local_uid  from telmed.directions d where local_uid = '" + ReadPropFile(
                            "Test_1219_LocalUid_0") + "';");
            while (sql.resultSet.next()) {
                status = sql.resultSet.getString("status");
            }
            Assertions.assertEquals(status, "24", "Ошибочный протокол должен быть со татусом 24");

            System.out.println("Проверяем на вебе");
            driver.navigate().refresh();
            WaitElement(consultationUnfinished.StatusConsultation);
            statusWeb = driver.findElement(consultationUnfinished.StatusConsultation).getText();
            Assertions.assertEquals(statusWeb, "Выполнено (Протокол получил ошибку)");

            System.out.println("Берём текст ошибки после получения статуса 24 (2900)");
            String errorsText = null;

            sql.StartConnection("Select * from vimis.remd_sent_result where local_uid = '" + ReadPropFile(
                    "Test_1219_LocalUid_0") + "' order by id desc limit 1;");
            while (sql.resultSet.next()) {
                /** Берём текст ошибки после получения статуса 24 */
                errorsText = sql.resultSet.getString("errors");
            }

            System.out.println("Проверяем текст ошибки у консультации (2900)");
            String textError = driver.findElement(consultationUnfinished.TextErrorConsultation).getText();

            Assertions.assertEquals(textError, JsonArrayRead(errorsText, "Message"),
                    "Текст ошибки не совпадает (2900)");

            System.out.println("Теперь переотправляем документ");

            AddProtocolMethod(consultationUnfinished.FirstSelect, true, false, "Рабочий", "Врач", "Александрр",
                    "Саферовв", "Николаевичв", "15748720095 ", true);

            System.out.println(
                    "Переходим в Направления - Консультации - Исходящие - Архивные у МО из Которой отправили");
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(consultationOutgoingArchived.OutgoingArchived);
            Thread.sleep(3000);
            ClickElement(consultationOutgoingArchived.DESK);
            ClickElement(By.xpath("//tbody/tr[1]/td[1][contains(.,'" + ReadPropFile(
                    "NumberConsulId_0_1256") + "')]"));

            System.out.println("Проверяем, та ли это кансультация");
            WaitElement(consultationOutgoingArchived.TextConclusion);
            driver.findElement(consultationOutgoingArchived.TextConclusion).getText().equals(
                    "" + ReadPropFile("Test_1219_0_uuid") + "");

            System.out.println("Дожидаемся смены статуса на success");
            error = "error";
            String version_doc2 = null;
            while (error == "error") {
                sql.StartConnection("Select * from vimis.remd_sent_result where local_uid = '" + ReadPropFile(
                        "Test_1219_LocalUid_0") + "' order by id desc limit 1;");
                while (sql.resultSet.next()) {
                    error = sql.resultSet.getString("status");
                    version_doc2 = sql.resultSet.getString("version_doc");
                }
            }

            /** При переотправке документа версия документа должна увеличиваться */
            Assertions.assertEquals(ReadPropFile("Test_1219_version_doc_0"),
                    version_doc2,
                    "Версии отправленного и переотправленного документа остались одинаковыми");

        } else {
            System.out.println("------------ Ошибочный документ получил статус Success");
        }
        sql.PrintSQL = true;

        /** статус 23 можно получить либо сначала отправив ошибочный, затем верный протокол, а затем использовать колбэк КРЭМД,
         * либо сразу отправив верный протокол */
        System.out.println("После получения success нужно отправить колбэк КРЭМД для чтобы сменился статус на 23");
        CollbekKremd("" + ReadPropFile("Test_1219_LocalUid_0") + "", "success",
                "Проверка переотправки протокола", "vimis.remd_sent_result");

        /** Долго меняет статус, так что нужно ждать */
        Thread.sleep(6000);
        System.out.println("Мы отправили протокол без ошибки - проверяем нужный статус в бд и на веб");
        sql.StartConnection(
                "select d.id, d.status, d.local_uid  from telmed.directions d where local_uid = '" + ReadPropFile(
                        "Test_1219_LocalUid_0") + "';");
        while (sql.resultSet.next()) {
            status = sql.resultSet.getString("status");
        }

        Assertions.assertEquals(status, "23", "Переотправленный протокол без ошибки должен быть со татусом 23");

        System.out.println("Проверяем на вебе");
        driver.navigate().refresh();
        WaitElement(consultationUnfinished.StatusConsultation);
        statusWeb = driver.findElement(consultationUnfinished.StatusConsultation).getText();

        Assertions.assertEquals(statusWeb, "Выполнено (Протокол успешно добавлен в РЭМД)",
                "Статус на вебе не совпадает");
    }

    @Step("Метод проверки добавления записи в архивные, в МО, из которой/в которую отправляли (заявка 2840)")
    public void CheckArchivedKvot (boolean MyMO) throws InterruptedException, SQLException, IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        consultationArchived = new ConsultationArchived(driver);
        consultationOutgoingArchived = new ConsultationOutgoingArchived(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        incomingArchive = new IncomingArchive(driver);
        sql = new SQL();

        System.out.println("Берём ID консультации");
        sql.StartConnection(
                "Select * from telmed.directions where local_uid = '" + ReadPropFile("Test_1219_LocalUid") + "';");
        while (sql.resultSet.next()) {
            NumberConsul = sql.resultSet.getString("id");
        }

        System.out.println("Переходим в Направления - Консультации - Входящие - Архивные у МО в Которую отправили");
        if (!MyMO) {
            if (KingNumber == 1 | KingNumber == 2) {
                AuthorizationMethod(authorizationObject.YATCKIV);
            }
            if (KingNumber == 4) {
                AuthorizationMethod(authorizationObject.Kondinsk);
            }
        } else {
            AuthorizationMethod(authorizationObject.OKB);
        }
        ClickElement(incomingArchive.IncomingArchiveWait);
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.Loading, 30);
        ClickElement(incomingArchive.DESK);
        ClickElement(By.xpath("//tbody/tr[1]/td[1][contains(.,'" + NumberConsul + "')]"));

        /** Проверка статуса  */
        System.out.println("Дожидаемся смены статуса");
        String error = null;
        sql.PrintSQL = false;
        while (error == null) {
            sql.StartConnection("Select * from vimis.remd_sent_result where local_uid = '" + ReadPropFile(
                    "Test_1219_LocalUid") + "';");
            while (sql.resultSet.next()) {
                error = sql.resultSet.getString("status");
            }
        }

        System.out.println("Статсус меняется не сразу, дождёмся когда будет статус 24 при статусе error от КРЭМД");
        sql.PrintSQL = false;
        error = "21";
        while (error == "21") {
            sql.StartConnection(
                    "select d.id, d.status, d.local_uid from telmed.directions d where local_uid = '" + ReadPropFile(
                            "Test_1219_LocalUid") + "';");
            while (sql.resultSet.next()) {
                error = sql.resultSet.getString("status");
                System.out.println(error);
            }
        }
        sql.PrintSQL = true;
        Thread.sleep(6000);

        System.out.println("Мы отправили протокол с ошибкой - проверяем нужный статус в бд и на веб");
        sql.StartConnection(
                "select d.id, d.status, d.local_uid  from telmed.directions d where local_uid = '" + ReadPropFile(
                        "Test_1219_LocalUid") + "';");
        while (sql.resultSet.next()) {
            status = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(status, "24", "Ошибочный протокол должен быть со татусом 24");

        System.out.println("Проверяем на вебе");
        driver.navigate().refresh();
        WaitElement(incomingArchive.StatusDirection);
        statusWeb = driver.findElement(incomingArchive.StatusDirection).getText();
        Assertions.assertEquals(statusWeb, "Выполнено (Протокол получил ошибку)");

        System.out.println(
                "Теперь отправляем документ повторно, просто чтобы проверить статус 23 (пока переотправка для диагностики не реализована)");
        AuthorizationMethod(authorizationObject.OKB);
        Access_1219Method(directionsForQuotas.DistrictDiagnosticWait, "Направление на диагностику", false,
                directionsForQuotas.SelectResearch, directionsForQuotas.SelectDoctorSecond,
                authorizationObject.Select(""), authorizationObject.Select("детской урологии-андрологии"),
                directionsForQuotas.SelectOchnoe, "Зотин Андрей Владимирович");

        AddProtocolKvotMethod(consultationUnfinished.FirstSelect, false, false, "Рабочий", "Врач", "Александр ",
                "Саферов ", "Николаевич ", "15748720095 ", false);

        System.out.println("Переходим в Направления - Консультации - Исходящие - Архивные у МО из Которой отправили");
        if (KingNumber != 4) {
            AuthorizationMethod(authorizationObject.YATCKIV);
        } else {
            AuthorizationMethod(authorizationObject.Kondinsk);
        }
        ClickElement(incomingArchive.IncomingArchiveWait);
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.Loading, 30);
        ClickElement(incomingArchive.DESK);
        ClickElement(By.xpath("//tbody/tr[1]/td[1][contains(.,'" + NumberConsul + "')]"));

        System.out.println("Дожидаемся смены статуса на success");
        error = "error";
        String version_doc2 = null;
        while (error == "error") {
            sql.StartConnection("Select * from vimis.remd_sent_result where local_uid = '" + ReadPropFile(
                    "Test_1219_LocalUid") + "' order by id desc limit 1;");
            while (sql.resultSet.next()) {
                error = sql.resultSet.getString("status");
                version_doc2 = sql.resultSet.getString("version_doc");
            }
        }
        sql.PrintSQL = true;

        /** статус 23 можно получить либо сначала отправив ошибочный, затем верный протокол, а затем использовать колбэк КРЭМД,
         * либо сразу отправив верный протокол */
        System.out.println("После получения success нужно отправить колбэк КРЭМД для чтобы сменился статус на 23");
        CollbekKremd("" + ReadPropFile("Test_1219_LocalUid") + "", "success",
                "Проверка переотправки протокола", "vimis.remd_sent_result");

        /** Долго меняет статус, так что нужно ждать */
        Thread.sleep(6000);
        System.out.println("Мы отправили протокол без ошибки - проверяем нужный статус в бд и на веб");
        sql.StartConnection(
                "select d.id, d.status, d.local_uid  from telmed.directions d where local_uid = '" + ReadPropFile(
                        "Test_1219_LocalUid") + "';");
        while (sql.resultSet.next()) {
            status = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(status, "23", "Переотправленный протокол без ошибки должен быть со татусом 23");

        System.out.println("Берём текст ошибки после получения статуса 23 (2900)");
        String errorsText = null;

        sql.StartConnection("Select * from vimis.remd_sent_result where local_uid = '" + ReadPropFile(
                "Test_1219_LocalUid") + "' order by id desc limit 1;");
        while (sql.resultSet.next()) {
            /** Берём текст ошибки после получения статуса 23 */
            errorsText = sql.resultSet.getString("errors");
        }

        System.out.println("Проверяем на вебе");
        driver.navigate().refresh();
        WaitElement(incomingArchive.StatusDirection);
        statusWeb = driver.findElement(incomingArchive.StatusDirection).getText();

        System.out.println(
                "Проверяем текст у успешного направления на диагностику - Выполнено (Протокол успешно добавлен в РЭМД) (2900)");
        // String textError = driver.findElement(incomingArchive.TextErrorDirection).getText();

        Assertions.assertEquals(statusWeb, "Выполнено (Протокол успешно добавлен в РЭМД)",
                "Статус на вебе не совпадает");
        // Assertions.assertEquals(textError, JsonArrayRead(errorsText, "Message"), "Текст ошибки не совпадает (2900)");
    }

    @Step("Метод для добавления пробелов и тире в снилс")
    public String snilsSet (String snils) {

        String fullSnils = snils;
        String one = fullSnils.substring(0, 3);
        String two = fullSnils.substring(0, fullSnils.length() - 5).substring(3);
        String three = fullSnils.substring(0, fullSnils.length() - 2).substring(6);
        String four = fullSnils.substring(9);
        String finish = one + "-" + two + "-" + three + " " + four;
        return finish;
    }
}
