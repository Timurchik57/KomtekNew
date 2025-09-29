package api.CheckingRequests;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.hc.core5.util.TextUtils;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты API")
@Feature("Добавление СЭМД с необязательным блоком report")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("ФЛК")
@Tag("Проверка_json")
@Tag("Оповещение")
@Tag("Дополнительные_параметры")
@Tag("РРП")
@Tag("Основные")
public class Report_api_smd_2951Test extends BaseAPI {

    public String SnilsPatient;
    AuthorizationObject authorizationObject;

    /*/
    Отправляемые смс 3/117/216/212
    Нужно чтобы были включены настройки /vimis/tm-dev api:
    ReportRrn_AllowedDoctypes
    ReportRrn_IsEnabled

    Нужно чтоб у пациента был ЕНП (3023)
     */

    @Test
    @Order(1)
    @DisplayName("Берём Cookies с веба")
    public void GetTestCookies() throws IOException, InterruptedException {
        GetCookies();
    }

    @Issue(value = "TEL-2951")
    @Issue(value = "TEL-2953")
    @Issue(value = "TEL-3122")
    @Issue(value = "TEL-3123")
    @Link(name = "ТМС-2006", url = "https://team-1okm.testit.software/projects/5/tests/2006?isolatedSection=701d9777-a0eb-4d94-b711-0fe5d4f6bdbd")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с report")
    @Description("Отправить смс с report, проверить добавление значения в таблицы")
    public void initTest() throws IOException, InterruptedException, ParseException, SQLException {
        PatientGuid = "c1cff77c-5efb-43a8-8ff7-7c5efb73a8f5";
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, false, "", "", "");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с dirnumber - int")
    public void initTest1() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.dirnumber", 1);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true, "errorMessage", "report.dirnumber: Произошла ошибка. Неверный формат.", "Нет ошибки при dirnumber - int");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с dirnumber - null")
    public void initTest2() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.dirnumber", null);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true, "result[0].errorMessage", "ReportRrn.DirectionNumber: 'dirNumber' должно быть заполнено.", "Нет ошибки при dirnumber - null");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с podr - int")
    public void initTest3() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.podr", 1);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true,
                "errorMessage", "report.podr: Произошла ошибка. Неверный формат.", "Нет ошибки при podr - int");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с podr - null")
    public void initTest4() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.podr", null);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true,
                "result[0].errorMessage", "ReportRrn.DepartmentCode: 'PODR' должно быть заполнено.",
                "Нет ошибки при podr - null");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с sourse - varchar")
    public void initTest5() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.source", "1");
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true,
                "errorMessage", "report.source: Произошла ошибка. Неверный формат.", "Нет ошибки при sourse - varchar");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с sourse - null")
    public void initTest6() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.source", null);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true,
                "errorMessage", "report.source: Произошла ошибка. Неверный формат.", "Нет ошибки при sourse - null");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с sourse - 0")
    public void initTest7() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.source", 0);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true,
                "result[0].errorMessage", "ReportRrn.Source: Источник направления может принимать значения от 1 до 7",
                "Нет ошибки при sourse - 0");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с sourse - 8")
    public void initTest8() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.source", 8);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true,
                "result[0].errorMessage", "ReportRrn.Source: Источник направления может принимать значения от 1 до 7",
                "Нет ошибки при sourse - 8");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с disp - int")
    public void initTest9() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.disp", 1);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true,
                "errorMessage", "report.disp: Произошла ошибка. Неверный формат.", "Нет ошибки при disp - int");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с disp - null, sourse - 1")
    public void initTest10() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.disp", null);
        xml.changes.put("$.report.source", 1);
        PatientGuid = "c1cff77c-5efb-43a8-8ff7-7c5efb73a8f5";
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, false,
                "", "", "");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с disp - null, sourse - 6")
    public void initTest11() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.disp", null);
        xml.changes.put("$.report.source", 6);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true,
                "result[0].errorMessage",
                "ReportRrn.DispensarizationType: Тип диспансеризации обязателен при указании источника \"Во время профмероприятия\"",
                "Нет ошибки при disp - null, sourse - 6");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с target - varchar")
    public void initTest12() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.target", "1");
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true,
                "errorMessage", "report.target: Произошла ошибка. Неверный формат.", "Нет ошибки при target- varchar");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с target - null")
    public void initTest13() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.target", null);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true,
                "errorMessage", "report.target: Произошла ошибка. Неверный формат.", "Нет ошибки при target- null");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с target - 0")
    public void initTest14() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.target", 0);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true,
                "result[0].errorMessage", "ReportRrn.Target: Цель направления может принимать значения от 1 до 6",
                "Нет ошибки при target- 0");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с target - 7")
    public void initTest15() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.target", 7);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true,
                "result[0].errorMessage", "ReportRrn.Target: Цель направления может принимать значения от 1 до 6",
                "Нет ошибки при target - 7");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с medicalCareProfile - int")
    public void initTest16() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.medicalCareProfile", 1);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true,
                "errorMessage", "report.medicalCareProfile: Произошла ошибка. Неверный формат.",
                "Нет ошибки при  medicalCareProfile - int");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с medicalCareProfile - null")
    public void initTest17() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.medicalCareProfile", null);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true,
                "result[0].errorMessage", "ReportRrn.MedicalCareProfile: 'medicalCareProfile' должно быть заполнено.",
                "Нет ошибки при  medicalCareProfile - null");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с hospDate  - null")
    public void initTest18() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.hospDate ", null);
        PatientGuid = "c1cff77c-5efb-43a8-8ff7-7c5efb73a8f5";
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, false,
                "", "", "");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с hospDate - 2024-01-01")
    public void initTest19() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.hospDate", "2024-01-01");
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true,
                "result[0].errorMessage",
                "ReportRrn.HospitalizationDate: Для типа документов \"Протокол лабораторного исследования (CDA) Редакция 4\" дата планируемой госпитализации должна быть null",
                "Нет ошибки при hospDate - 2024-01-01");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с bedProfile - null")
    public void initTest20() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.bedProfile", null);
        PatientGuid = "c1cff77c-5efb-43a8-8ff7-7c5efb73a8f5";
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, false,
                "", "", "");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с bedProfile - int")
    public void initTest21() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.bedProfile", 1);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true,
                "errorMessage", "report.bedProfile: Произошла ошибка. Неверный формат.",
                "Нет ошибки при bedProfile - int");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с bedProfile - varchar")
    public void initTest22() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.bedProfile", "1");
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true,
                "result[0].errorMessage",
                "ReportRrn.BedProfile: Для типа документов \"Протокол лабораторного исследования (CDA) Редакция 4\" тип койки должен быть null",
                "Нет ошибки при bedProfile - varchar");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с diagnosis - int")
    public void initTest23() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.diagnosis", 1);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true,
                "errorMessage", "report.diagnosis: Произошла ошибка. Неверный формат.",
                "Нет ошибки при diagnosis - int");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с diagnosis - null")
    public void initTest24() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.diagnosis", null);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true,
                "result[0].errorMessage", "ReportRrn.DiagnosisCode: 'diagnosis' должно быть заполнено.",
                "Нет ошибки при diagnosis - null");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с vidVme - null")
    public void initTest25() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.vidVme", null);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, true,
                "result[0].errorMessage",
                "ReportRrn.MedicalServiceCode: Код номенклатуры медицинских услуг обязателен при типе документа \"Протокол лабораторного исследования (CDA) Редакция 4\"",
                "Нет ошибки при vidVme - null");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с vidVme - int")
    public void initTest26() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.vidVme", 1);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true,
                "errorMessage", "report.vidVme: Произошла ошибка. Неверный формат.", "Нет ошибки при vidVme - int");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с idcase  - null")
    public void initTest27() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.idcase", null);
        PatientGuid = "c1cff77c-5efb-43a8-8ff7-7c5efb73a8f5";
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, false,
                "", "", "");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с idcase  - int")
    public void initTest28() throws IOException, InterruptedException, ParseException, SQLException {
        xml.changes.put("$.report.idcase", 1);
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 400, true,
                "errorMessage", "report.idcase: Произошла ошибка. Неверный формат.", "Нет ошибки при idcase - int");
    }

    @Test
    @Issue(value = "TEL-2988")
    @Issue(value = "TEL-3071")
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с пациентом без ЕНП, но с PolicyNumber = 16 символов")
    public void initTest29() throws IOException, InterruptedException, ParseException, SQLException {
        PatientGuid = "42f00926-18f2-4986-905b-b7b81ba54d63";
        GetCookies();
        AddSms("SMS/SMS3.xml", "3", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, false,
                "", "", "");
    }

    @Test
    @Issue(value = "TEL-2988")
    @Issue(value = "TEL-3071")
    @DisplayName("Отправка валидной смс id = 3 по всем направлениям с пациентом без ЕНП, но с PolicyNumber < 16 символов")
    public void initTest30() throws IOException, InterruptedException, ParseException, SQLException {
        PatientGuid = "c1cff77c-5efb-43a8-8ff7-7c5efb73a8f5";
        GetCookies();
        AddSms("SMS/SMS3.xml", "3", "1", "57", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, false,
                "", "", "");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 117 с report")
    public void initTest31() throws IOException, InterruptedException, ParseException, SQLException {
        PatientGuid = "c1cff77c-5efb-43a8-8ff7-7c5efb73a8f5";
        AddSms("SMS/id=117.xml", "117", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, false,
                "", "", "");
    }

    @Test
    @DisplayName("Отправка валидной смс id = 216 с report")
    public void initTest32() throws IOException, InterruptedException, ParseException, SQLException {
        PatientGuid = "c1cff77c-5efb-43a8-8ff7-7c5efb73a8f5";
        AddSms("SMS/id=216.xml", "216", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, false,
                "", "", "");
    }

    @Test
    @Issue(value = "TEL-3840")
    @DisplayName("Отправка валидной смс id = 212 с report")
    public void initTest33() throws IOException, InterruptedException, ParseException, SQLException {
        PatientGuid = "c1cff77c-5efb-43a8-8ff7-7c5efb73a8f5";
        AddSms("SMS/id=212.xml", "212", "1", "9", "18", "1", "57", "21", "SMS/Body/2951-report(api_smd).json", 200, false,
                "", "", "");
    }

    @Step("Метод отправки смс по всем направлениям")
    public void AddSms(String FileName, String docType, String Role, String position, String speciality, String Role1, String position1, String speciality1, String body, Integer statusCode, boolean mistake, String responsePath, String responseActual, String responseMessage) throws IOException, InterruptedException, ParseException, SQLException {

        authorizationObject = new AuthorizationObject(driver);

        // Узнаем отправляется ли в ВИМИС документ
        sql.StartConnection("select * from dpc.emd_types where id = '" + docType + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("send_to_vimis");
        }

        if (sql.value.contains("t")) {
            Method_2951(FileName, docType, "1", "2", "3", Role, position, speciality, Role1, position1, speciality1,
                    body, statusCode, false, mistake, responsePath, responseActual, responseMessage);
            Method_2951(FileName, docType, "2", "2", "3", Role, position, speciality, Role1, position1, speciality1,
                    body, statusCode, false, mistake, responsePath, responseActual, responseMessage);
            Method_2951(FileName, docType, "3", "2", "3", Role, position, speciality, Role1, position1, speciality1,
                    body, statusCode, false, mistake, responsePath, responseActual, responseMessage);
            Method_2951(FileName, docType, "4", "2", "3", Role, position, speciality, Role1, position1, speciality1,
                    body, statusCode, false, mistake, responsePath, responseActual, responseMessage);
            Method_2951(FileName, docType, "5", "2", "3", Role, position, speciality, Role1, position1, speciality1,
                    body, statusCode, false, mistake, responsePath, responseActual, responseMessage);
            Method_2951(FileName, docType, "99", "", "", Role, position, speciality, Role1, position1, speciality1,
                    body, statusCode, true, mistake, responsePath, responseActual, responseMessage);
        } else {
            Method_2951(FileName, docType, "99", "", "", Role, position, speciality, Role1, position1, speciality1,
                    body, statusCode, true, mistake, responsePath, responseActual, responseMessage);
        }
    }

    @Step("Метод меняющий нужные элементы в теле запроса, после отправляет смс")
    public void Method_2951(String FileName, String docType, String vmcl, String triggerPoint, String docTypeVersion, String Role, String position, String speciality, String Role1, String position1, String speciality1, String body, Integer statusCode, boolean deleteYes, boolean mistake, String responsePath, String responseActual, String responseMessage) throws IOException, InterruptedException, ParseException, SQLException {

        // Изменяем тело запроса для СЭМД документа
        xml.XmlStart(FileName, Departmen, MO, PatientGuid, NameMO, Snils, 1);
        xml.JsonChange(docType, vmcl, triggerPoint, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);

        // Удаляем (если нужно) параметры
        String[] pathDelete = {"$.VMCL[0].triggerPoint", "$.VMCL[0].docTypeVersion"};

        // Меняем параметры в нужном файле параметры
        String modifiedJson = JsonMethod(body, xml.changes, deleteYes, pathDelete);

        Api("" + HostAddress + "/api/smd", "post", null, null, modifiedJson, statusCode, true);

        xml.ReplacementWordInFileBack(FileName);
        AssertResponseTrue(vmcl, mistake, responsePath, responseActual, responseMessage);
        Sql_2951(vmcl, body, mistake);

        // Сбрасываем тело запроса, чтобы все изменения не переходили от теста к тесту
    }

    @Step("Метод проверки тела ответа (успешный)")
    public void AssertResponseTrue(String vmcl, boolean mistake, String responsePath, String responseActual, String responseMessage) {

        String text = null;
        if (vmcl == "1") {
            text = "Онкология";
        }
        if (vmcl == "2") {
            text = "Профилактика";
        }
        if (vmcl == "3") {
            text = "АкиНео";
        }
        if (vmcl == "4") {
            text = "ССЗ";
        }
        if (vmcl == "5") {
            text = "Инфекционные болезни";
        }
        if (vmcl == "99") {
            text = "Онкология";
        }

        if (!mistake) {
            if (vmcl != "99") {
                Assertions.assertEquals(Response.getString("result[0].message"),
                        "СМС по направлению \"" + text + "\" успешно опубликован в ЦУ РС ЕГИСЗ.",
                        "Валидный документ не принят");
            } else {

                Assertions.assertEquals(Response.getString("result[0].message"),
                        "СМС предназначенная только для передачи в РЭМД успешно опубликована в ЦУ РС ЕГИСЗ.",
                        "Валидный документ не принят");
            }
        } else {
            Assertions.assertEquals(Response.getString(responsePath), responseActual, responseMessage);
            //assertThat(Response.getString(responsePath), isOneOf(responseActual));
        }
    }

    @Step("Метод проверки таблиц vimis.report_rrn")
    public void Sql_2951(String vmcl, String body, boolean mistake) throws SQLException, InterruptedException, IOException {
        Thread.sleep(1500);

        TableVmcl(Integer.valueOf(vmcl));
        if (!mistake) {
            String sms_id = null;
            sql.StartConnection("Select * from " + smsBase+ " where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sms_id = sql.resultSet.getString("id");
            }

            String lpu_direct = null;
            String idcase = null;
            String dir_num = null;
            String podr = null;
            String source = null;
            String disp = null;
            String target = null;
            String profil = null;
            String profil_k = null;
            String target_date = null;
            String vid_vme = null;
            String diagnosis = null;

            System.out.println("Проверяем таблицу " + rrnBase+ "");
            sql.StartConnection("Select * from " + rrnBase+ " where sms_id = '" + sms_id + "';");
            while (sql.resultSet.next()) {
                lpu_direct = sql.resultSet.getString("lpu_direct");
                idcase = sql.resultSet.getString("idcase");
                dir_num = sql.resultSet.getString("dir_num");
                podr = sql.resultSet.getString("podr");
                source = sql.resultSet.getString("source");
                disp = sql.resultSet.getString("disp");
                target = sql.resultSet.getString("target");
                profil = sql.resultSet.getString("profil");
                profil_k = sql.resultSet.getString("profil_k");
                target_date = sql.resultSet.getString("target_date");
                vid_vme = sql.resultSet.getString("vid_vme");
                diagnosis = sql.resultSet.getString("diagnosis");
            }

            System.out.println("Ищем снилс пациента по Гуид");
            GetRRP(PatientGuid, "");

            /** Проверка отсутствия блока report если у пациента нет ЕНП (2988) */
            JsonPath Responsee = given()
                    .cookie(String.valueOf(ReadPropFile("Session")))
                    .cookie(String.valueOf(ReadPropFile("TelemedC1")))
                    .cookie(String.valueOf(ReadPropFile("TelemedC2")))
                    .cookie(String.valueOf(ReadPropFile("Telemed")))
                    .param("snils", Response.getString("snils"))
                    .log().uri()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddressWeb + "/patients")
                    .prettyPeek()
                    .then()
                    .extract().jsonPath();

            if (!TextUtils.isEmpty(Responsee.getString("result.items[0].enp"))) {

                Assertions.assertEquals(lpu_direct, JsonMethodRead(body, "$.report.lpudirect", false, false),
                        "lpu_direct не совпадает");
                Assertions.assertEquals(idcase, JsonMethodRead(body, "$.report.idcase", false, false),
                        "idcase не совпадает");
                Assertions.assertEquals(dir_num, JsonMethodRead(body, "$.report.dirnumber", false, false),
                        "dir_num не совпадает");
                Assertions.assertEquals(podr, JsonMethodRead(body, "$.report.podr", false, false), "podr не совпадает");
                Assertions.assertEquals(source, String.valueOf(JsonMethodRead(body, "$.report.source", false, false)),
                        "source не совпадает");
                Assertions.assertEquals(disp, JsonMethodRead(body, "$.report.disp", false, false), "disp не совпадает");
                Assertions.assertEquals(target, String.valueOf(JsonMethodRead(body, "$.report.target", false, false)),
                        "target не совпадает");
                Assertions.assertEquals(profil, JsonMethodRead(body, "$.report.medicalCareProfile", false, false),
                        "profil не совпадает");
                Assertions.assertEquals(profil_k, JsonMethodRead(body, "$.report.bedProfile", false, false),
                        "bedProfile не совпадает");
                Assertions.assertEquals(target_date, JsonMethodRead(body, "$.report.hospDate", false, false),
                        "hospDate не совпадает");
                Assertions.assertEquals(vid_vme, JsonMethodRead(body, "$.report.vidVme", false, false),
                        "vid_vme не совпадает");
                Assertions.assertEquals(diagnosis, JsonMethodRead(body, "$.report.diagnosis", false, false),
                        "diagnosis не совпадает");
            }

            // Проверяем сформированное тело для РЭМД
            if (vmcl != "99") {
                CollbekVimis("" + xml.uuid + "", "1", "Проверка 2951", smsBase, Integer.valueOf(vmcl));
            }
            WaitStatusKremd(remdBase, "" + xml.uuid + "");

            String[] params = {
                    "LocalUid", "" + xml.uuid + ""
            };
            Thread.sleep(2000);
            Api("" + ApiVimis + "/api/rremd", "get", params, null, "", 200, true);

            if (!TextUtils.isEmpty(Responsee.getString("result.items[0].enp"))) {

                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.lpuDirect"),
                        JsonMethodRead(body, "$.report.lpudirect", false, false), "lpu_direct не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.idcase"),
                        JsonMethodRead(body, "$.report.idcase", false, false), "idcase не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.dirNumber"),
                        JsonMethodRead(body, "$.report.dirnumber", false, false), "dir_num не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.podr"),
                        JsonMethodRead(body, "$.report.podr", false, false), "podr не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.source"),
                        String.valueOf(JsonMethodRead(body, "$.report.source", false, false)),
                        "source не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.disp"),
                        JsonMethodRead(body, "$.report.disp", false, false), "disp не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.target"),
                        String.valueOf(JsonMethodRead(body, "$.report.target", false, false)),
                        "target не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.medicalCareProfile"),
                        JsonMethodRead(body, "$.report.medicalCareProfile", false, false), "profil не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.bedProfile"),
                        JsonMethodRead(body, "$.report.bedProfile", false, false), "bedProfile не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.hospDate"),
                        JsonMethodRead(body, "$.report.hospDate", false, false), "hospDate не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.vidVme"),
                        JsonMethodRead(body, "$.report.vidVme", false, false), "vid_vme не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "report.diagnosis"),
                        JsonMethodRead(body, "$.report.diagnosis", false, false), "diagnosis не совпадает");
                Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "patient.enp"),
                        Responsee.getString("result.items[0].enp"));
            } else {
                if (PatientGuid.contains("c1cff77c-5efb-43a8-8ff7-7c5efb73a8f5")) {
                    Assertions.assertNull(Response.getString("result[0]." + DocumentDto + "report."));
                    Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "patient.enp"),
                            "", "ЕНП должен быть пустым, так как policyNumber < 16");
                } else {
                    /** Проверяем что ЕНП при отправке в РЭМД равно policyNumber (3071) */
                    Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "patient.enp"),
                            Responsee.getString("result.items[0].policyNumber"),
                            "ЕНП должен быть заполнен, так как policyNumber = 16");
                }
            }

            /** Проверка оповещения с типом 15 (3122) */
            if (ReadPropFile( "methodName").equals("initTest")) {
                System.out.println("Используем колбэк для РРН ТФОМС");
                String error1 = "Направление зарегистрировано для ТФОМС";
                String error2 = "Направление зарегистрировано в ТФОМС";
                String error3 = "Ошибка регистрации в ТФОМС";
                String status = null;
                List<String> list = new ArrayList<>(Arrays.asList(error1, error2, error3));

                date = new Date();
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
                Date = formatForDateNow.format(date);

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == error1) {
                        status = "1";
                    } else if (list.get(i) == error2) {
                        status = "2";
                    } else if (list.get(i) == error3) {
                        status = "3";
                    }
                    String uuid = String.valueOf(UUID.randomUUID());
                    String Body = "{   \n" +
                            "  \"status\": "+status+",\n" +
                            "  \"UuidRoute\":\"" + xml.uuid + "\",\n" +
                            "  \"Message\": \"" + list.get(i) + "\",\n" +
                            "  \"UuidPkg\": \"" + uuid + "\",\n" +
                            "  \"AlertTime\": \"" + Date + "\"\n" +
                            "}";

                    Api("" + ApiVimis + "/kremd/callback/tfoms", "post", null, null, Body, 200, true);
                    Thread.sleep(1500);

                    System.out.println("Переходим на сайт для перехвата сообщений");
                    driver.get(Notification);
                    Thread.sleep(1500);

                    ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
                    WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
                    NotificationsTrue(15, "", Integer.valueOf(vmcl));
                    System.out.println(TEXT);
                    System.out.println(xml.uuid);
                    Assertions.assertTrue(TEXT.contains("\"Status\":"+status+""),
                            "Оповещение 15 для vmcl = " + vmcl + " - Статус не совпадает");
                    if (vmcl != "99") {
                        Assertions.assertTrue(TEXT.contains("\"DocType\":\"SMSV3\""),
                                "Оповещение 15 для vmcl = " + vmcl + " - DocType не совпадает");
                    } else {
                        Assertions.assertTrue(TEXT.contains("\"DocType\":\"75\""),
                                "Оповещение 15 для vmcl = " + vmcl + " - DocType не совпадает");
                    }
                    Assertions.assertTrue(TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                            "Оповещение 15 для vmcl = " + vmcl + " - LocalUid не совпадает");
                    Assertions.assertTrue(TEXT.contains("\"PatientGuid\":\"" + PatientGuid + "\""),
                            "Оповещение 15 для vmcl = " + vmcl + " - PatientGuid не совпадает");
                    Assertions.assertTrue(TEXT.contains("\"IdCase\":\"" + idcase + "\""),
                            "Оповещение 15 для vmcl = " + vmcl + " - IdCase не совпадает");
                    Assertions.assertTrue(TEXT.contains("\"UuidPkg\":\"" + uuid + "\""),
                            "Оповещение 15 для vmcl = " + vmcl + " - UuidPkg не совпадает");
                    if (status == "2") {
                    Assertions.assertTrue(TEXT.contains("\"Message\":\"Направление принято в ТФОМС\""),
                            "Оповещение 15 для vmcl = " + vmcl + " - Message не совпадает");
                    } else {
                        Assertions.assertTrue(TEXT.contains("\"Message\":\"" + list.get(i) + "\""),
                                "Оповещение 15 для vmcl = " + vmcl + " - Message не совпадает");
                    }
                    Assertions.assertTrue(TEXT.contains("\"AlertTime\":\"" + Date + "T00:00:00\""),
                            "Оповещение 15 для vmcl = " + vmcl + " - AlertTime не совпадает");

                    System.out.println("Проверяем таблицу " + rrnBase+ "");
                    String statusBD = null;
                    String errorsBD = null;
                    String alert_timeBD = null;
                    String uuid_pkgBD = null;

                    sql.StartConnection("Select * from " + rrnBase+ " where sms_id = '" + sms_id + "';");
                    while (sql.resultSet.next()) {
                        statusBD = sql.resultSet.getString("status");
                        errorsBD = sql.resultSet.getString("errors");
                        alert_timeBD = sql.resultSet.getString("alert_time");
                        uuid_pkgBD = sql.resultSet.getString("uuid_pkg");
                    }

                    Assertions.assertEquals(statusBD, status, "Статус в бд не равен ОР");
                    if (status == "3") {
                        Assertions.assertEquals(errorsBD, list.get(i), "Статус в бд не равен ОР");
                    } else {
                        Assertions.assertEquals(errorsBD, null, "Ошибка в бд не равен ОР");
                    }
                    Assertions.assertEquals(alert_timeBD.substring(0,10), ""+Date+"", "alert_time в бд не равен ОР");
                    Assertions.assertEquals(uuid_pkgBD, uuid, "uuid_pkg в бд не равен ОР");
                }
            }
        }
    }
}
