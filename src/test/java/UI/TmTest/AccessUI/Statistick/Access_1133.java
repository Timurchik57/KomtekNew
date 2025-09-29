package UI.TmTest.AccessUI.Statistick;

import Base.SQL;
import Base.TestListener;
import UI.TmTest.AccessUI.Directions.Access_1219_1256;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
import api.Before.Authorization;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isOneOf;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("Задания")
@Tag("Протокол")
@Tag("РРП")
@Disabled
public class Access_1133 extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;
    Access_1219_1256 access_1219_1256;

    public String SNILS;
    public String formatProtocol;
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
    public String URLRemd;
    public static String orgSignatureSum;
    public static String personalSignaturesSum;
    public static String uuid;
    public static String created_datetime;
    public static String effectivetime;
    public static String status;
    public static String version_doc;
    public static String typeConsul;
    public static String typeConsulId;

    @Test
    @Issue(value = "TEL-1133")
    @Issue(value = "TEL-1178")
    @Issue(value = "TEL-2848")
    @Issue(value = "TEL-2850")
    @Issue(value = "TEL-2852")
    @Issue(value = "TEL-2853")
    @Link(name = "ТМС-1441", url = "https://team-1okm.testit.software/projects/5/tests/1441?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Link(name = "ТМС-1445", url = "https://team-1okm.testit.software/projects/5/tests/1445?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Link(name = "ТМС-2001", url = "https://team-1okm.testit.software/projects/5/tests/2001?isolatedSection=701d9777-a0eb-4d94-b711-0fe5d4f6bdbd")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Добавление заданий в Маршрутах ОМП")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок гдк есть маршруты, переходим к пациенту, у пациента смотрим на блок добавления заданий. Добавляем задание, проверяем бд")
    public void Access_1133() throws SQLException, InterruptedException, IOException, AWTException, XPathExpressionException, ParserConfigurationException, SAXException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();
        access_1219_1256 = new Access_1219_1256();

        String name_header = "1133----------------------------------------------------------------------------------------------------------";

        String Type1 = "Медицинская справка в бассейн (CDA) Редакция 3";
        String Type2 = "Справка об оплате медицинских услуг для предоставления в налоговые органы Российской Федерации (CDA) Редакция 1";
        String Type3 = "Справка об отказе в направлении на медико-социальную экспертизу (CDA) Редакция 1";

        String service1 = "Определение антител класса М к вирусу SARS-CoV-2 в сыворотке венозной крови (ИФА)";
        String service2 = "Исследование уровня антител к антигенам растительного, животного и химическогопроисхождения в крови (пшеничная мука)";
        String service3 = "Определение концентрации Д-димера в крови";

        System.out.println("Удаляем запись, которую создадим далее (нужно, если тест упал и не дошёл да конца)");
        deleteSql(name_header);

        /** Узнаем включена ли отправка протокола в заданиях */
        GetCookies();
        Api(HostAddressWeb + "/vimis/pmc/routes/tasks/settings/needsign", "get", null, null, "", 200, true);
        if (Response.get("result").equals(true)) {
            Crypto = true;
            System.out.println("Добавляем сертификаты");
            access_1219_1256.CryptoProMethod();
        }

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(analyticsMO.Analytics);
        //Специальный пациент для проверки ЛК, небольшое количество СЭМД
        driver.get(HostAddressWeb + "/registry/patient/2ed97c3b-5f30-4fda-bcdc-39d930c6c224/dashboard");
        WaitElement(analyticsMO.Snils);
        String Snils = driver.findElement(analyticsMO.Snils).getText();

        GetRRP("", Snils);

        InputPropFile("Test_1219_lastnamePatient", Response.getString("patients[0].LastName"));
        InputPropFile("Test_1219_firstnamePatient", Response.getString("patients[0].FirstName"));
        InputPropFile("Test_1219_middlenamePatient", Response.getString("patients[0].MiddleName"));
        InputPropFile("Test_1219_snilsPatient", Snils);
        String patient_guid = Response.getString("patients[0].guid");

        System.out.println("Открываем задания");
        ClickElement(analyticsMO.Action);
        ClickElement(analyticsMO.ActionAddTask);
        WaitElement(analyticsMO.TaskPatient);
        Thread.sleep(2000);
        WaitNotElement3(analyticsMO.TaskPatientLoading, 30);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.NewTaskDisabled, 30);
        Thread.sleep(2000);
        WaitNotElement3(analyticsMO.NewTaskLoading, 30);

        System.out.println(
                "Берём численное значение всех заданий, считаем отображаемые задания, берём список заданий из бд и сравниваем");
        List<WebElement> QuantityList = driver.findElements(analyticsMO.TaskList);
        String AllTask = driver.findElement(analyticsMO.AllTask).getText();
        Integer Error = Integer.valueOf(driver.findElement(analyticsMO.ErrorTask).getText());
        Assertions.assertEquals(
                QuantityList.size(), Integer.valueOf(AllTask),
                "Численное значение всех заданий и отображаемые задания не совпадают"
        );
        sql.StartConnection("SELECT count(rt.task_header) FROM vimis.routes_tasks rt\n" +
                "join vimis.nosological_patients np on rt.nosological_patient_id = np.id \n" +
                "where np.patient_guid = '" + patient_guid.toLowerCase() + "' and np.vimis_patient_id = '" + patient_guid.toLowerCase() + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(QuantityList.size(), Integer.valueOf(sql.value),
                "Значение заданий в бд и отображаемые задания не совпадают");

        System.out.println("Создаём задание");
        ClickElement(analyticsMO.NewTask);
        ClickElement(analyticsMO.Done);

        System.out.println("Проверяем обязательность полей");
        String error = driver.findElement(analyticsMO.Header).getAttribute("class");
        Assertions.assertEquals(
                error, "el-form-item is-error is-required el-form-item--medium",
                "Поле Заголовок не выделилось обязательным"
        );
        error = driver.findElement(analyticsMO.Text).getAttribute("class");
        Assertions.assertEquals(
                error, "el-form-item is-error is-required el-form-item--medium",
                "Поле Текст задания не выделилось обязательным"
        );
        error = driver.findElement(analyticsMO.Data).getAttribute("class");
        Assertions.assertEquals(
                error, "el-form-item is-error is-required el-form-item--medium",
                "Поле Дата окончания не выделилось обязательным"
        );
        error = driver.findElement(analyticsMO.MO).getAttribute("class");
        Assertions.assertEquals(
                error, "el-form-item is-error is-required el-form-item--medium",
                "Поле Мед организация не выделилось обязательным"
        );

        System.out.println("Заполняем поля");
        inputWord(analyticsMO.HeaderInput, name_header + " ");
        ClickElement(analyticsMO.Done);
        inputWord(analyticsMO.TextInput, name_header + " ");
        ClickElement(analyticsMO.Done);
        ClickElement(analyticsMO.Data);
        ClickElement(analyticsMO.Day1);
        ClickElement(analyticsMO.Done);
        SelectClickMethod(analyticsMO.MO, authorizationObject.Select("БУ ХМАО-Югры \"Белоярская районная больница\""));
        SelectClickMethod(analyticsMO.Doctor, authorizationObject.Select(PName));

        System.out.println("Заполняем Ожидаемый результат");
        inputWord(driver.findElement(analyticsMO.Margin("Тип документа", "1")), "Медицинская справка в бассейн (CDA) Редакция ");
        ClickElement(authorizationObject.Select(Type1));
        ClickElement(analyticsMO.ADD);

        inputWord(driver.findElement(analyticsMO.Margin("Тип документа", "2")), "Справка ");
        ClickElement(authorizationObject.Select(Type2));

        driver.findElement(analyticsMO.Margin("Медицинские услуги", "3")).sendKeys(" ");
        ClickElement(authorizationObject.Select(service1));
        ClickElement(analyticsMO.ADD);

        inputWord(driver.findElement(analyticsMO.Margin("Тип документа", "3")), "Справка ");
        ClickElement(authorizationObject.Select(Type3));
        ClickElement(analyticsMO.Margin("Медицинские услуги", "5"));
        ClickElement(authorizationObject.Select(service2));
        ClickElement(authorizationObject.Select(service3));

        /** Узнаем включена ли отправка протокола в заданиях */
        if (Crypto) {
            System.out.println("Заполняем данные протокола");
            ProtocolMethod(false, "Александр ", "Саферов ", "Николаевич ", "15748720095", "Врач", "Новый");
        } else {
            ClickElement(analyticsMO.Done);
        }

        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 50);

        if (Crypto) {
            System.out.println("Проверяем заполнение таблиц и тело протокола");
            AccessProtocolBd(false, "Александр ", "Саферов ", "Николаевич ", "15748720095");
        }

        System.out.println("Проверяем что запись появилась");
        WaitElement(analyticsMO.TaskHeader);

        System.out.println("Проверяем БД");
        taskBd(name_header, name_header, "БУ ХМАО-Югры \"Белоярская районная больница\"", Type1, Type2, Type3, service1,
                service2, service3);

        System.out.println("Проверяем Появление полного названия заголовка");
        actions.moveToElement(driver.findElement(analyticsMO.TaskHeader));
        actions.perform();
        Thread.sleep(1000);
        WaitElement(analyticsMO.Tooltip);
        ClickElement(analyticsMO.AllTask);
        if (KingNumber == 4) {
            driver.navigate().refresh();
            WaitElement(analyticsMO.TaskText);
        }

        System.out.println("Проверяем Появление полного названия Текст задания");
        actions.moveToElement(driver.findElement(analyticsMO.TaskText));
        actions.perform();
        Thread.sleep(1000);
        if (KingNumber != 4) {
            WaitElement(analyticsMO.Tooltip1);
        } else {
            WaitElement(analyticsMO.Tooltip);
        }

        System.out.println("Считаем отображаемые задания, берём список заданий из бд и сравниваем");
        QuantityList = driver.findElements(analyticsMO.TaskList);

        sql.StartConnection("SELECT count(rt.task_header) FROM vimis.routes_tasks rt\n" +
                "join vimis.nosological_patients np on rt.nosological_patient_id = np.id \n" +
                "where np.patient_guid = '" + patient_guid.toLowerCase() + "' and np.vimis_patient_id = '" + patient_guid.toLowerCase() + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(
                QuantityList.size(), Integer.valueOf(sql.value),
                "Значение заданий в бд и отображаемые задания не совпадают"
        );

        System.out.println("Ищем созданную задачу в бд");
        sql.StartConnection(
                "SELECT rt.id, rt.task_header, rt.task, rt.estimated_end_date, npd.last_name, npd.first_name, npd.middle_name, npd.snils, npd.birthdate  FROM vimis.routes_tasks rt\n" +
                        "join vimis.nosological_patients np on rt.nosological_patient_id = np.id \n" +
                        "join vimis.nosological_patients_details npd on np.patient_guid = npd.patient_guid  where rt.task_header = '" + name_header + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            SNILS = sql.resultSet.getString("snils");
        }
        Assertions.assertEquals(SNILS, Snils, "Задача создалась на другого пациента");
        sql.UpdateConnection(
                "update vimis.routes_tasks set estimated_end_date = '" + Date + " 00:10:29.600' where id = '" + sql.value + "';");
        driver.navigate().refresh();
        Thread.sleep(1500);
        WaitElement(analyticsMO.TaskText);

        System.out.println("Берём число просроченных заданий и сверяем с бд");
        WaitElement(analyticsMO.ErrorTask);
        Integer ErrorNew = Integer.valueOf(driver.findElement(analyticsMO.ErrorTask).getText());
        Assertions.assertEquals(Error + 1, ErrorNew, "Просроченных заданий не увеличилось");

        System.out.println("Сравниваем просроченную дату с бд");
        String EndDate = driver.findElement(analyticsMO.EndDate).getText();
        date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        Date = formatForDateNow.format(date);
        Assertions.assertEquals(EndDate, Date, "Даты не совпадают");
        deleteSql(name_header);

        if (Crypto) {
            System.out.println("Проверяем таблицы с подписями (2853)");
            access_1219_1256.Access_1256();
        }
    }

    @Step("Удаление заданий из таблиц vimis.routes_tasks и vimis.routes_tasks_expected_result")
    public void deleteSql(String name) throws SQLException {
        sql = new SQL();

        String id = "н";
        while (id != null) {
            id = null;
            sql.StartConnection(
                    "select * from vimis.routes_tasks where task_header = '" + name + "';");
            while (sql.resultSet.next()) {
                id = sql.resultSet.getString("id");
            }
            if (id != null) {
                sql.UpdateConnection("delete from vimis.routes_tasks_expected_result where taskid = '" + id + "';");
                sql.UpdateConnection("delete from vimis.routes_tasks where id = '" + id + "';");
            } else {
                break;
            }
        }
    }

    @Step("Заполнение протокола через Задания")
    public void ProtocolMethod(boolean CurrentUser, String Name, String SurName, String LastName, String Snils, String RoleName, String NameCert) throws InterruptedException, SQLException, IOException {

        formatProtocol = "40";
        analyticsMO = new AnalyticsMO(driver);
        authorizationObject = new AuthorizationObject(driver);
        access_1219_1256 = new Access_1219_1256();

        if (!CurrentUser) {
            inputWord(driver.findElement(analyticsMO.Margin("Фамилия", "1")), SurName);
            inputWord(driver.findElement(analyticsMO.Margin("Имя", "1")), Name);
            inputWord(driver.findElement(analyticsMO.Margin("Отчество", "1")), LastName);
            driver.findElement(analyticsMO.Margin("СНИЛС", "1")).sendKeys(Snils);
        } else {
            ClickElement(analyticsMO.CheckBox);
        }
        System.out.println("Выбираем подразделение");
        ClickElement(analyticsMO.Margin("Подразделение", "1"));
        ClickElement(authorizationObject.Select("Бухгалтерия"));

        /** дополнительно проверяем отображение должностей и ролей по заявке (2607) */
        System.out.println("Берём все значения Роли");
        ClickElement(analyticsMO.Margin("Роль подписанта", "1"));
        Thread.sleep(1500);
        WaitElement(authorizationObject.SelectFirst);
        access_1219_1256.SelectAccessMethod(Role, authorizationObject.SelectFirst, "SELECT * FROM dpc.signer_role s\n" +
                        "right join dpc.signature_rules_remd sr on s.id = sr.\"role\"\n" +
                        "where sr.doc_kind ='" + formatProtocol + "';", "name_role", "Роли");
        if (RoleName == null) {
            Role = driver.findElement(authorizationObject.SelectFirst).getText();
        } else {
            Role = RoleName;
        }
        InputPropFile("Test_1219_Role", Role);
        ClickElement(authorizationObject.Select(Role));
        RoleId = access_1219_1256.ChoiceMethod("SELECT * FROM dpc.signer_role where name_role = '" + Role + "';",
                "code_role", "Роль");
        System.out.println(Role);
        System.out.println(RoleId);

        System.out.println("Берём все значения Должности");
        ClickElement(analyticsMO.Margin("Должность", "1"));
        Thread.sleep(1500);
        WaitElement(authorizationObject.SelectFirst);
        access_1219_1256.SelectAccessMethod(Post, authorizationObject.SelectFirst, "SELECT * FROM dpc.med_worker_positions;", "work_position",
                "Должности");
        Post = driver.findElement(authorizationObject.SelectFirst).getText();
        InputPropFile("Test_1219_Post", Post);
        ClickElement(authorizationObject.SelectFirst);
        PostId = access_1219_1256.ChoiceMethod(
                "SELECT * FROM dpc.med_worker_positions where work_position = '" + Post + "';", "id",
                "Должность"
        );
        System.out.println(Post);
        System.out.println(PostId);
        InputPropFile("Test_1219_PostId", PostId);

        System.out.println("Берём все значения Специальности");
        ClickElement(analyticsMO.Margin("Специальность", "1"));
        Thread.sleep(1500);
        WaitElement(authorizationObject.SelectFirst);
        access_1219_1256.SelectAccessMethod(Specialization, authorizationObject.SelectFirst,
                "SELECT * FROM dpc.medical_and_pharmaceutical_specialties;", "name", "Специальности");
        Specialization = driver.findElement(authorizationObject.SelectFirst).getText();
        InputPropFile("Test_1219_Specialization", Specialization);
        ClickElement(authorizationObject.SelectFirst);
        SpecializationId = access_1219_1256.ChoiceMethod(
                "SELECT * FROM dpc.medical_and_pharmaceutical_specialties where name = '" + Specialization + "' limit 1;",
                "id", "Специальность"
        );
        System.out.println(Specialization);
        System.out.println(SpecializationId);
        InputPropFile("Test_1219_SpecializationId", SpecializationId);

        System.out.println("Выбираем сертификаты");
        ClickElement(analyticsMO.Margin("Сертификат медицинского работника", "1"));
        ClickElement(authorizationObject.Select(NameCert));
        ClickElement(analyticsMO.Margin("Сертификат медицинской организации", "1"));
        ClickElement(authorizationObject.Select(NameCert));

        ClickElement(analyticsMO.Done);
        Thread.sleep(15000);
    }

    @Step("Проверяем заполнение таблиц и тело протокола")
    public void AccessProtocolBd(boolean CurrentUser, String Name, String SurName, String LastName, String Snils) throws SQLException, XPathExpressionException, ParserConfigurationException, IOException, SAXException, InterruptedException {

        formatProtocol = "40";

        System.out.println("Проверяем добавление в таблицу vimis.remd_sent_result");
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
        InputPropFile("Test_1219_org_signature_id_0", org_signature_id);
        InputPropFile("Test_1219_created_datetime", created_datetime);
        InputPropFile("Test_1219_LocalUid", LocalUid);
        InputPropFile("Test_1219_version_doc", version_doc);
        Assertions.assertEquals(created_datetime, Date, "Дата не равняется текущей дате");

        System.out.println("LocalUid - " + LocalUid);

        /** Проверка по заявке (2821, 2853) */
        Assertions.assertEquals(Doctype, formatProtocol, "Тип консультации не верный");

        sql.StartConnection(
                "SELECT * FROM vimis.remdadditionalinfo WHERE smsid ='" + Id + "';");
        while (sql.resultSet.next()) {
            effectivetime = sql.resultSet.getString("effectivetime").substring(0, 10);
        }
        InputPropFile("Test_1219_effectivetime", effectivetime);
        Assertions.assertEquals(created_datetime, effectivetime, "Дата не равняется текущей дате");

        if (CurrentUser) {
            System.out.println("Берём значения доктора из бд");
            sql.StartConnection("SELECT * FROM telmed.users WHERE sname = '"+PLastNameGlobal+"';");
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
            name = Name.substring(0, Name.length() - 1);
            patrName = LastName.substring(0, LastName.length() - 1);
            surname = SurName.substring(0, SurName.length() - 1);
            snils = Snils;
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
        if (KingNumber == 1) {
            URLRemd = "http://192.168.2.126:1108/api/rremd?LocalUid=" + LocalUid;
        }
        if (KingNumber == 2) {
            URLRemd = "http://192.168.2.126:1131/api/rremd?LocalUid=" + LocalUid;
        }
        if (KingNumber == 4) {
            Thread.sleep(3000);
            URLRemd = "http://192.168.2.21:34154/api/rremd?LocalUid=" + LocalUid;
        }
        if (KingNumber == 3) {
            Thread.sleep(3000);
            URLRemd = "http://192.168.2.86:35007/api/rremd?LocalUid=" + LocalUid;
        }

        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(URLRemd)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath();

        String PereResult = "result[0]";

        assertThat(response.get("" + PereResult + "."+ DocumentDto +"localUid"), equalTo("" + LocalUid + ""));

        /** Узнаем формат отправки протокола (2446))*/
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"kind.code"), equalTo(formatProtocol));
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"patient.surname"), equalTo(ReadPropFile("Test_1219_lastnamePatient")));
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"patient.name"), equalTo(ReadPropFile("Test_1219_firstnamePatient")));
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"patient.patrName"), equalTo(ReadPropFile("Test_1219_middlenamePatient")));
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"patient.snils"), equalTo(ReadPropFile("Test_1219_snilsPatient")));
        orgSignatureSum = response.get("" + PereResult + "."+ DocumentDto +"orgSignature.checksum");
        personalSignaturesSum = response.get(
                "" + PereResult + "."+ DocumentDto +"personalSignatures[0].signature.checksum");
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"personalSignatures[0].signer.role.code"),
                equalTo("" + RoleId + ""));
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"personalSignatures[0].signer.position.code"),
                equalTo("" + PostId + ""));
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"personalSignatures[0].signer.speciality.code"),
                equalTo("" + SpecializationId + ""));
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"personalSignatures[0].signer.surname"),
                equalTo("" + surname + ""));
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"personalSignatures[0].signer.name"),
                equalTo("" + name + ""));
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"personalSignatures[0].signer.patrName"),
                equalTo("" + patrName + ""));
        assertThat(response.get("" + PereResult + "."+ DocumentDto +"personalSignatures[0].signer.snils"),
                equalTo("" + snils + ""));

        InputPropFile("Test_1219_orgSignatureSum_0", orgSignatureSum);
        InputPropFile("Test_1219_personalSignaturesSum_0", personalSignaturesSum);

        /** Проврека по заявке 2611 */
        if (CurrentUser) {
            Assertions.assertNotNull(
                    response.get("" + PereResult + "."+ DocumentDto +"personalSignatures[0].signer.birthDate"),
                    "Дата рождения должна присутствовать, когда выбран текущий пользователь (2611)");
        } else {
            Assertions.assertNull(
                    response.get("" + PereResult + "."+ DocumentDto +"personalSignatures[0].signer.birthDate"),
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
         assertThat(getXml("src/test/resources/ignored/1219_test.xml", "/ClinicalDocument/author/assignedAuthor/id[2]/@extension"), isOneOf(snils, access_1219_1256.snilsSet(snils)));
        Assertions.assertEquals(PostId,
                getXml("src/test/resources/ignored/1219_test.xml", "/ClinicalDocument/author/assignedAuthor/code/@code"),
                "Код должности в файле не совпадает");
        Assertions.assertEquals(Post2,
                getXml("src/test/resources/ignored/1219_test.xml", "/ClinicalDocument/author/assignedAuthor/code/@displayName"),
                "Наименование должности в файле не совпадает");

        Assertions.assertEquals(surname, getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/author/assignedAuthor/assignedPerson/name/family/text()"),
                "Фамилия в файле не совпадает");
        Assertions.assertEquals(name, getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/author/assignedAuthor/assignedPerson/name/given[1]/text()"),
                "Имя в файле не совпадает");

        sql.StartConnection(
                "SELECT u.id, u.profileid, u.profilename, us.id, us.fname, us.sname, us.mname, m.namemu, u.quitdate , pr.consultation_typesid, t.\"name\"\n" +
                        "FROM telmed.usermedprofile u\n" +
                        "join telmed.users us on u.userid = us.id \n" +
                        "join telmed.userplacework pl on u.userplaceworkid = pl.id \n" +
                        "join dpc.mis_sp_mu m on pl.placework = m.idmu \n" +
                        "join telmed.profiletypematching pr on u.profileid = pr.\"type\" \n" +
                        "join dpc.consultation_types t on pr.consultation_typesid = t.id \n" +
                        "where us.sname  = '"+PLastNameGlobal+"' and m.namemu = 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and u.quitdate is null;");
        while (sql.resultSet.next()) {
            typeConsul = sql.resultSet.getString("name");
            typeConsulId = sql.resultSet.getString("consultation_typesid");
        }

        System.out.println("Берём версию Типа консультации (2915) ");
        sql.StartConnection("SELECT * FROM dpc.consultation_types limit 1;");
        String versionConsul = null;
        while (sql.resultSet.next()) {
            versionConsul = sql.resultSet.getString("version");
        }

        System.out.println(
                "Сверяем значения Типа консультации в файле, если не указан тип консультации, то берём по профилю авторизованного пользователя в таблице telmed.profiletypematching (2694) ");
        Assertions.assertEquals(typeConsul, getXml("src/test/resources/ignored/1219_test.xml",
                        "/ClinicalDocument/documentationOf/serviceEvent/code/@displayName"),
                "Тип консультации в файле не совпадает");
        Assertions.assertEquals(typeConsulId,
                getXml("src/test/resources/ignored/1219_test.xml", "/ClinicalDocument/documentationOf/serviceEvent/code/@code"),
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
        Assertions.assertEquals(versionConsul, getXml("src/test/resources/ignored/1219_test.xml", "//code[@codeSystemName='Типы консультаций']/@codeSystemVersion"), "Тип консультации в файле не совпадает");

        System.out.println("Сверяем значениея в файле по 3133 заявке");
        GetRRP(PatientGuid,"");

        /** "Кем выдан" паспорт */
        if (Response.getString("patients[0].PatientIdentity[1].Issuer") == null || Response.getString("patients[0].PatientIdentity[0].Issuer") == null) {
            Assertions.assertEquals("РФ", getXml("src/test/resources/ignored/1219_test.xml",
                            "//ClinicalDocument/recordTarget/patientRole/id[2]/@assigningAuthorityName"),
                    "Кем выдан паспорт в файле не совпадает");
        }

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
                "//effectiveTime/@value").substring(0,8);

        String params [] = {"LocalUid", LocalUid};
        Api(ApiVimis + "/api/rremd", "get", params, null, "", 200, true);
        String date = Response.getString("" + PereResult + "."+ DocumentDto +"creationDateTime").substring(0,10).replaceAll("-", "");
        Assertions.assertEquals(dateXml, date, "Дата в xml и в теле запроса Крэмд не совпадает");
    }


    @Step("Проверяем добавление заданий в БД (2850)")
    public void taskBd(String Task_header, String Task, String MO, String Type1, String Type2, String Type3, String Service1, String Service2, String Service3) throws SQLException {
        String task_header = null;
        String task = null;
        String create_date = null;
        String status_task = null;
        String namemu = null;
        String type_name = null;
        String serviceid_name = null;

        sql.StartConnection("select * from vimis.routes_tasks order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        List<String> list_type_nameBD = new ArrayList<>();
        List<String> list_serviceid_nameBD = new ArrayList<>();

        List<String> list_type_name = new ArrayList<>(Arrays.asList(Type2, Type3, Type3, Type1));
        List<String> list_serviceid_name = new ArrayList<>(Arrays.asList(Service1, Service3, Service2, null));

        sql.StartConnection(
                "SELECT r.id, u.fname , u.sname, u.mname, r.task_header, r.task, r.create_date, ts.\"name\" status_task, m.namemu, r.sms_id, rsr.doctype, rsr.status, rsr.local_uid,  \n" +
                        "re.document_type, emd.\"name\" type_name, re.serviceid, ms.\"name\" serviceid_name FROM vimis.routes_tasks r\n" +
                        "full outer join dpc.mis_sp_mu m on r.mo_recieve = m.idmu\n" +
                        "full outer join telmed.users u on r.author_id = u.id\n" +
                        "full outer join dpc.tasks_statuses ts on r.status = ts.id\n" +
                        "full outer join vimis.remd_sent_result rsr on r.sms_id = rsr.id\n" +
                        "full outer join vimis.routes_tasks_expected_result re on r.id = re.taskid\n" +
                        "full outer join dpc.registered_emd emd on re.document_type = emd.oid \n" +
                        "full outer join dpc.medical_services ms on re.serviceid = ms.id\n" +
                        "where re.taskid  = '" + sql.value + "' order by re.serviceid asc;");
        while (sql.resultSet.next()) {
            task_header = sql.resultSet.getString("task_header");
            task = sql.resultSet.getString("task");
            create_date = sql.resultSet.getString("create_date");
            status_task = sql.resultSet.getString("status_task");
            namemu = sql.resultSet.getString("namemu");
            type_name = sql.resultSet.getString("type_name");
            serviceid_name = sql.resultSet.getString("serviceid_name");

            list_type_nameBD.add(type_name);
            list_serviceid_nameBD.add(serviceid_name);
        }

        Assertions.assertEquals(task_header, Task_header, "Заголовок задания не совпадает");
        Assertions.assertEquals(task, Task, "Текст задания не совпадает");
        Assertions.assertEquals(create_date.substring(0, 10), Date, "Дата задания не совпадает");
        Assertions.assertEquals(status_task, "Создано", "Статус задания не совпадает");
        Assertions.assertEquals(namemu, MO, "Мед. организация задания не совпадает");
        Assertions.assertEquals(list_type_nameBD, list_type_name, "Типы документов задания не совпадает");
        Assertions.assertEquals(list_serviceid_nameBD, list_serviceid_name, "Медицинские услуги задания не совпадает");
    }
}
