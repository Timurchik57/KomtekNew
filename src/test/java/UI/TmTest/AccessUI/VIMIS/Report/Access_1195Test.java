package UI.TmTest.AccessUI.VIMIS.Report;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.Report;
import Base.BaseAPI;
import Base.TestListenerApi;
import Base.XML;
import api.Before.Authorization;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("Отчёты")
@Tag("Отображение_документа_UI")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
public class Access_1195Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    Report report;
    String MOWeb;
    String DateWeb;
    String LocalWeb;
    String IdentWeb;
    String IdenSMStWeb;
    String DateTwo;
    public String body;
    public String docType;
    public String error;

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @Issue(value = "TEL-1195")
    @Issue(value = "TEL-1973")
    @Issue(value = "TEL-2511")
    @Issue(value = "TEL-2512")
    @Link(name = "ТМС-1447", url = "https://team-1okm.testit.software/projects/5/tests/1447?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 1")
    @Description("Переходим в ВИМИС - Отчёты - Заполняем параметры поиска. Отправляем смс и смотрим у неё статусы в колонках Успешно загруженные в регион/Отправленные на федеральный уровень/Успешно приняты на федеральном уровне/Не прошли ФЛК/В очереди отправки. Открываем Отправленные на федеральный уровень сравниваем значения с БД, после открываем документ")
    public void Access_1195_1() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);

        Access_1195Method("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21, report.Oncology, "success");
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 2")
    public void Access_1195_2() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);

        Access_1195Method("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21, report.Prevention,  "success");
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 3")
    public void Access_1195_3() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);

        Access_1195Method("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21, report.Akineo,  "success");
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 4")
    public void Access_1195_4() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);

        Access_1195Method("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21, report.SSZ, "success");
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 5")
    public void Access_1195_5() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);

        Access_1195Method("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21, report.Infection,  "success");
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 99")
    public void Access_1195_99() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);

        Access_1195Method("SMS/SMS3.xml", "3", 99, 0, 1, 9, 18, 1, 57, 21, report.Other, "success");
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 99 status = error")
    public void Access_1195_99_error() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);

        Access_1195Method("SMS/SMS3.xml", "3", 99, 0, 1, 9, 18, 1, 57, 21, report.Other,  "error");
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 99 status = fremd_status")
    public void Access_1195_99_fremd_status() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);

        Access_1195Method("SMS/SMS3.xml", "3", 99, 0, 1, 9, 18, 1, 57, 21, report.Other,  "fremd_status");
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 99 ПДФ")
    public void Access_1195_99_PDF() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);

        Access_1195Method("SMS/id=101-vmcl=99.txt", "51", 99, 0, 1, 49, 18, 1, 57, 21, report.Other, "success");
    }

    /**
     * Метод для отправки смс
     */
    @Step("Отправляем смс = {0}, Doctype = {1}, Method = {2}")
    public void Access_1195MethodAddSms(String FileName, String Doctype, String Method, Integer vmcl, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException {

        Token = TokenInit();
        XML.Type = "" + FileName + "";

        xml.encodedString = new String(Files.readAllBytes(Paths.get("" + FileName + "")));

        /** Подсчёт чек Суммы для XML */
        String sum = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"base64String\": \"" + xml.encodedString + "\"\n" +
                        "}")
                .when()
                .post("http://192.168.2.126:10117/CheckSum/CalculateCheckSum")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("checkSum");
        CheckSum = sum;

        /** Получение подписи */
        String Signat = given()
                .contentType(ContentType.JSON)
                .body("{\"requestId\": \"2\",\n" +
                        "            \"serialNumber\": \"" + SerialNumberXml + "\",\n" +
                        "            \"message\": \"" + xml.encodedString + "\",\n" +
                        "            \"isBase64\": true}")
                .when()
                .post(AddressSignature)
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("result.signedMessage");
        Signatures = Signat;

        /** Подсчёт чек Суммы для Подписи */
        String sum1 = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"base64String\": \"" + Signatures + "\"\n" +
                        "}")
                .when()
                .post("http://192.168.2.126:10117/CheckSum/CalculateCheckSum")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("checkSum");
        CheckSumSign = sum1;
        xml.uuid = UUID.randomUUID();
        System.out.println(xml.uuid);

        body = "{\n" +
                "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                "    \"DocType\":\"" + Doctype + "\",\n" +
                "    \"DocContent\":\n" +
                "    {\n" +
                "        \"Document\":\"" + xml.encodedString + "\",\n" +
                "        \"CheckSum\":" + CheckSum + "\n" +
                "        },\n" +
                "        \"LocalUid\":\"" + xml.uuid + "\",\n" +
                "        \"Payment\":1,\n" +
                "        \"ReasonForAbsenceIdcase\":\n" +
                "        {\n" +
                "            \"CodeSystemVersion\":\"1.1\",\n" +
                "            \"Code\":10,\n" +
                "            \"CodeSystem\":\"1.2.643.5.1.13.13.99.2.286\"\n" +
                "        },\n" +
                "            \"VMCL\":\n" +
                "            [\n" +
                "                {\n" +
                "                    \"vmcl\":  " + vmcl + " \n" +
                "                    \n" +
                "                }\n" +
                "            ],\n" +
                "            \"OrgSignature\":\n" +
                "            {\n" +
                "                \"Data\":\"" + Signatures + "\",\n" +
                "                \"CheckSum\":" + CheckSumSign + "\n" +
                "                },\n" +
                "                \"PersonalSignatures\":\n" +
                "                [\n" +
                "                    {\n" +
                "                        \"Signer\":\n" +
                "                        {\n" +
                "                            \"LocalId\":\"0001510378\",\n" +
                "                            \"Role\":{\"$\":\"" + Role + "\",\"@version\":\"2.4\"},\n" +
                "                            \"LastName\":\"Коситченков\",\n" +
                "                            \"FirstName\":\"Андрей\",\n" +
                "                            \"MiddleName\":\"Александрович\",\n" +
                "                            \"Snils\":\"18259202174\",\n" +
                "                            \"Position\":{\"$\":\"" + position + "\",\"@version\":\"" + Position + "\"},\n" +
                "                            \"Speciality\":{\"$\":" + speciality + ",\"@version\":\"" + Speciality + "\"},\n" +
                "                            \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                "                        },\n" +
                "                            \"Signature\":\n" +
                "                            {\n" +
                "                                \"Data\":\"" + Signatures + "\",\n" +
                "                                \"CheckSum\":" + CheckSumSign + "\n" +
                "                            },\n" +
                "                            \"Description\":\"Подпись сотрудника\"\n" +
                "                            }\n" +
                "        ]\n" +
                "    }";

        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .post(HostAddress + "" + Method + "")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath();
    }

    @Step("Метод для отправки смс с изменением статусов, с последующим открытием данной смс на ui")
    public void Access_1195Method(String File, String Doctype, Integer vmcl, Integer docTypeVersion, Integer role, Integer position, Integer speciality, Integer role1, Integer position1, Integer speciality1, By Directions, String status) throws IOException, InterruptedException, SQLException {
        report = new Report(driver);
        authorizationObject = new AuthorizationObject(driver);

        TableVmcl(vmcl);

        if (File == "SMS/SMS3.xml") {
            docType = "75";
        } else {
            docType = "53";
        }

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        DateTwo = formatForDateNow.format(date);

        System.out.println("Отправляем  смс c DocType = " + Doctype + " с vmlc=" + vmcl + "");
        if (File == "SMS/SMS3.xml") {
            xml.ApiSmd(File, Doctype, vmcl, 1, true, docTypeVersion, role, position, speciality, role1, position1,
                    speciality1);
        } else {
            Access_1195MethodAddSms(File, Doctype, "/api/smd", vmcl, role, position, speciality, role1, position1,
                    speciality1);
        }

        if (vmcl == 99) {
            WaitStatusKremd(smsBase, "" + xml.uuid + "");

            if (status.equals("success")) {
                System.out.println("Берём отправленную смс и меняем у неё status с null на success и добавляем emd_id");
                sql.UpdateConnection(
                        "update " + smsBase + " set status = 'success', emd_id = 'Проверка 1195', fremd_status = '1' where local_uid = '" + xml.uuid + "';");
            }
            /** Проверка по заявке 2512 */
            else if (status.equals("error")) {
                System.out.println("Берём отправленную смс и меняем у неё status с null на error и добавляем emd_id");
                sql.UpdateConnection(
                        "update " + smsBase + " set status = 'error', errors = '[{\"Code\": \"E-007\", \"Message\": \"Проверка 1195\"}]', emd_id = 'Проверка 1195', fremd_status = null where local_uid = '" + xml.uuid + "';");
            }
            else if (status.equals("fremd_status")) {
                System.out.println("Берём отправленную смс и меняем у неё status с null на success + fremd_status = 0 и добавляем emd_id");
                sql.UpdateConnection(
                        "update " + smsBase + " set status = 'success', errors = '[{\"Code\": \"E-007\", \"Message\": \"Проверка 1195\"}]', emd_id = 'Проверка 1195', fremd_status = '0' where local_uid = '" + xml.uuid + "';");
            }

            sql.StartConnection(
                    "SELECT r.id, r.status, r.fremd_status, r.emd_id ,  r.created_datetime,r.local_uid, msm.namemu  FROM " + smsBase + " r\n" +
                            "join dpc.mis_sp_mu msm on r.medicalidmu = msm.medicalidmu\n" +
                            "where created_datetime > '" + Date + " 00:00:00.888 +0500' AND doctype = '" + docType + "' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                MOWeb = sql.resultSet.getString("namemu");
                DateWeb = sql.resultSet.getString("created_datetime");
                LocalWeb = sql.resultSet.getString("local_uid");
                IdentWeb = sql.resultSet.getString("emd_id");
            }

        } else {
            if (status.equals("success")) {
                System.out.println(
                        "Берём отправленную смс и меняем у неё в " + smsBase + " status = true  msg_id какое либо значение, request_id какое либо значение");
                sql.UpdateConnection(
                        "update " + smsBase + " set is_sent = 'true', msg_id = '" + UUID.randomUUID() + "', request_id = '" + UUID.randomUUID() + "' where local_uid = '" + xml.uuid + "';");

                sql.StartConnection("select * from " + smsBase + " where local_uid = '" + xml.uuid + "'");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                }

                System.out.println("Берём отправленную смс и устанавливаем у неё в " + logsBase + " status = 1");
                sql.UpdateConnection(
                        "insert into " + logsBase + "(create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', " + sql.value + ", '" + UUID.randomUUID() + "');");
            }

            sql.StartConnection(
                    "select s.id, s.local_uid, s.is_sent, s.create_date, s.msg_id, s.request_id, msm.namemu  from " + smsBase + " s \n" +
                            "join " + logsBase + " d on s.id = d.sms_id\n" +
                            "join dpc.mis_sp_mu msm on s.medicalidmu = msm.medicalidmu where s.id = '" + sql.value + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                MOWeb = sql.resultSet.getString("namemu");
                DateWeb = sql.resultSet.getString("create_date");
                LocalWeb = sql.resultSet.getString("local_uid");
                IdentWeb = sql.resultSet.getString("msg_id");
                IdenSMStWeb = sql.resultSet.getString("request_id");
            }
            System.out.println(IdentWeb);
            System.out.println(IdenSMStWeb);

        }

        System.out.println("Переход в Отчёты");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(report.ReportWait);
        WaitElement(report.IntelligenceWait);

        System.out.println("Выбираем направление");
        SelectClickMethod(report.Direction, Directions);
        if (vmcl == 99) {
            System.out.println("Выбираем Период");
            ClickElement(report.PeriodWait);
            ClickElement(report.DateOneWait);
            Thread.sleep(1500);
            ClickElement(report.DateTwoWait);
        } else {
            System.out.println("Выбираем Период");
            ClickElement(report.PeriodWait);
            if (isElementNotVisible(report.DateOneWait)) {
                ClickElement(report.DateOneWait);
                Thread.sleep(1500);
                ClickElement(report.DateTwoWait);
            } else {
                ClickElement(report.DateTwoWait);
                Thread.sleep(1500);
                ClickElement(report.DateTwoWait);
            }
        }
        System.out.println("Выбираем Тип документа");
        if (vmcl == 99) {
            if (File == "SMS/SMS3.xml") {
                inputWord(driver.findElement(report.SMS), "755");
                ClickElement(report.SelectSms75);
            } else {
                inputWord(driver.findElement(report.SMS), "533");
                ClickElement(report.SelectSms53);
            }
        } else {
            ClickElement(report.SMS);
            ClickElement(report.SelectSms);
        }
        report.Search.click();
        Thread.sleep(1500);

        System.out.println("Нажимаем на Успешно приняты на федеральном уровне - по конкретной ИС");
        if (status.equals("success")) {
            WaitElement(report.SuccessfullyFederalMO);
            ClickElement(report.SuccessfullyFederalMO);
        }
        /** Проверка Не прошли ФЛК РЭМД по заявке 2512 */
        else if (status.equals("error") | status.equals("fremd_status")) {
            if (status.equals("error")) {
                ClickElement(report.ErrorFlkKremdMO);
            } else {
                ClickElement(report.FederalFalseIS);
            }

            System.out.println("Берём ошибку по отправленной смс");
            sql.StartConnection("Select * from "+smsBase+" where local_uid = '"+xml.uuid+"';");
            while (sql.resultSet.next()) {
                error = sql.resultSet.getString("errors");
            }
            ClickElement(report.Error(error));
        }

        WaitElement(report.DetalMOWait);
        Thread.sleep(1500);

        System.out.println("Берём название МО");
        String DetalMO = report.DetalMO.getText();
        Thread.sleep(2500);

        System.out.println("Берём Дату");
        while (isElementNotVisible(
                By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/preceding-sibling::td//span")) == false) {
            if (isElementNotVisible(report.DisableRight) == true) {
                break;
            } else {
                ClickElement(report.ActiveRight);
            }
        }
        String DetalNewDate = driver.findElement(By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/preceding-sibling::td//span")).getText();
        String DetalDate = StringUtils.substring(DetalNewDate, 0, 10);

        System.out.println("Берём Идентификатор");
        String DetalIdent = null;
        if (vmcl == 99) {
            if (status.equals("success")) {
                DetalIdent = driver.findElement(
                        By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/following-sibling::td[1]//span")).getText();
            }
        } else {
            DetalIdent = driver.findElement(
                    By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/following-sibling::td[2]//span")).getText();
        }
        String DetalSMSIdent = null;
        if (vmcl != 99) {
            System.out.println("Берём Идентификатор СМС");
            DetalSMSIdent = driver.findElement(
                    By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/following-sibling::td[1]//span")).getText();
        }
        Assertions.assertEquals(DetalMO, MOWeb + " (МИС \"Пациент\")", "Название МО не совпадает");
        Assertions.assertEquals(DetalDate, "" + DateTwo + "", "Дата не совпадает");
        if (status.equals("success")) {
            Assertions.assertEquals(DetalIdent, IdentWeb, "Идентификатор не совпадает");
        }

        if (vmcl != 99) {
            Assertions.assertEquals(DetalSMSIdent, IdenSMStWeb, "Идентификатор  СМС не совпадает");
        }

        System.out.println("Открываем документ");
        if (vmcl == 99) {
            if (status.equals("success")) {
                driver.findElement(
                        By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/following-sibling::td[2]/div")).click();
            } else {
                driver.findElement(
                        By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/following-sibling::td[1]/div")).click();
            }
        } else {
            driver.findElement(
                    By.xpath("//tr/td[contains(.,'" + xml.uuid + "')]/following-sibling::td[3]/div")).click();
        }
        Thread.sleep(1500);

        if (File == "SMS/SMS3.xml") {
            WaitElement(report.DocumentWait);
        } else {
            WaitElement(report.DocumentWaitPDF);
        }
        Thread.sleep(1000);
        if (isElementNotVisible(report.Closes("1"))) {
            ClickElement(report.Closes("1"));
        } else if (isElementNotVisible(report.Closes("2"))) {
            ClickElement(report.Closes("2"));
        } else {
            ClickElement(report.Closes("3"));
        }
        ClickElement(report.DetalMOWait);
    }
}
