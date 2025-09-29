package UI.TmTest.AccessUI.Statistick;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
import Base.TestListenerApi;
import Base.XML;
import api.Before.Authorization;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("Отображение_документов_ЛК")
@Tag("Отображение_документа_UI")
public class Access_1663Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    public String Host;
    public String body;
    public static String IDVimis;
    public static String IDRemd;
    public String Number;

    /**
     * Метод для установки нужного статуса и проверки документа
     */
    @Step("Устанавливаем нужные статусы и проверяем отображение документа в ЛК Врача")
    public void Access_1663Method(Boolean Remd) throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        Number = (String.valueOf(timestamp.getTime())).substring(8);

        if (Remd == true) {
            System.out.println("Устанавливаем status = 'success', fremd_statsu = '1'");
            sql.StartConnection("Select * from vimis.remd_sent_result where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                IDRemd = sql.value;
            }
            sql.UpdateConnection(
                    "Update vimis.remd_sent_result set status = 'success', fremd_status = '1' where local_uid = '" + xml.uuid + "';");
            System.out.println("Устанавливаем сегодняшнюю дату для данной смс");
            sql.UpdateConnection(
                    "Update vimis.remdadditionalinfo set effectivetime = '10.10.2100 12:44:00.000 +0500' where smsid = '" + sql.value + "';");
        } else {
            System.out.println("Устанавливаем status = 'success', fremd_statsu = '1'");
            sql.StartConnection("Select * from vimis.sms where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                IDVimis = sql.value;
            }
            InputPropFile("IdSms_1663", IDVimis);

            System.out.println("Устанавливаем status = 1 в vimis.documentlogs");
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1663", "vimis.sms", 1);

            System.out.println("Устанавливаем сегодняшнюю дату для данной смс");
            sql.UpdateConnection(
                    "Update vimis.additionalinfo set effectivetime = '10.10.2100 12:44:00.000 +0500' where smsid = '" + sql.value + "';");
        }

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);

        System.out.println("Проверяем где есть маршруты, во всех блоках");
        WaitElement(analyticsMO.Tall);
        WaitElement(analyticsMO.Average);
        WaitElement(analyticsMO.low);
        if (KingNumber == 1) {
            Host = "https://tm-test.pkzdrav.ru";
        }
        if (KingNumber == 2) {
            Host = "https://tm-dev.pkzdrav.ru";
        }
        if (KingNumber == 4) {
            Host = "https://remotecons-test.miacugra.ru";
        }

        System.out.println("Переходим к ЛК нужного пациента");
        driver.get("" + Host + "/registry/patient/3791bfa4-c234-43d4-a83c-8d495bca5a55/dashboard");
        WaitElement(analyticsMO.Snils);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 40);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 40);
        WaitElement(analyticsMO.Docs);
        Thread.sleep(1500);
        ClickElement(analyticsMO.Sort);
        ClickElement(analyticsMO.CreateDate);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 30);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 40);
        Thread.sleep(1500);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView(false);", driver.findElement(analyticsMO.FederalTrue));
        ClickElement(analyticsMO.FederalTrue);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablonAndDocs, 30);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 40);
        Thread.sleep(1500);
        ClickElement(analyticsMO.LastDocs);
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.LoadingDocsShablon, 30);

        System.out.println("Меняем формат даты");
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        Date = formatForDateNow.format(date);
        WaitElement(By.xpath("(//ul[@class='vimis-document-card__list']/li//p[@class='vimis-document-card-info__date'][contains(.,'10.10.2100')])[1]"));
        Thread.sleep(2000);

        if (Remd == true) {
            String text = driver.findElement(analyticsMO.DocumentTemplate).getAttribute("class");
            Assertions.assertEquals(text.contains("vimis-document-card__iframe__pdf"), true,
                    "Для ПДФ документа не отображается шаблон ПДФ");

            System.out.println("Меняем статусы обратно, чтобы не копились документы");
            sql.UpdateConnection(
                    "Update vimis.remd_sent_result set status = 'error', fremd_status = null where local_uid = '" + xml.uuid + "';");
            sql.UpdateConnection(
                    "Update vimis.remdadditionalinfo set effectivetime = '10.10.2005 12:44:00.000 +0500' where smsid = '" + sql.value + "';");
        } else {
            String text = driver.findElement(analyticsMO.DocumentTemplate).getAttribute("srcdoc");
            Assertions.assertEquals(text.contains("<!DOCTYPE html"), true,
                    "Для XML документа не отображается шаблон XML");

            System.out.println("Меняем статусы обратно, чтобы не копились документы");
            sql.UpdateConnection("Update vimis.documentlogs set status = '0' where sms_id = '" + sql.value + "';");
            sql.UpdateConnection(
                    "Update vimis.additionalinfo set effectivetime = '10.10.2005 12:44:00.000 +0500' where smsid = '" + sql.value + "';");
        }
    }

    /**
     * Метод для отправки смс
     */
    @Step("Отправляем смс = {0}, Doctype = {1}, Method = {2}")
    public void Access_1663MethodAddSms(String FileName, Integer Doctype, String Method, Boolean BOM, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException {

        PatientGuid = "3791bfa4-c234-43d4-a83c-8d495bca5a55";

        Token = TokenInit();
        XML.Type = "" + FileName + "";

        Thread.sleep(1000);
        ID = (int) Math.floor(timestamp.getTime() / 1000);
        SetID = (int) Math.floor(timestamp.getTime() / 1000) + 1;
        VN = (int) Math.floor(timestamp.getTime() / 1000) + 2;
        InputPropFile("value_ID", String.valueOf(ID));
        InputPropFile("value_SetID", String.valueOf(SetID));
        InputPropFile("value_VN", String.valueOf(VN));
        xml.ReplaceWord(FileName, "${iD}", String.valueOf(ID));
        xml.ReplaceWord(FileName, "${setId}", String.valueOf(SetID));
        xml.ReplaceWord(FileName, "${versionNumber}", String.valueOf(VN));
        xml.ReplaceWord(FileName, "${mo}", MO);
        xml.ReplaceWord(FileName, "${guid}", PatientGuid);
        xml.ReplaceWord(FileName, "${namemo}", NameMO);
        xml.ReplaceWord(FileName, "${snils}", Snils);

        xml.content = new String(Files.readAllBytes(Paths.get("" + FileName + "")));
        if (BOM == false) {
            xml.encodedString = Base64.getEncoder().encodeToString(xml.content.getBytes());
        } else {
            xml.encodedString = "77u/" + Base64.getEncoder().encodeToString(xml.content.getBytes());
        }

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

        if (vmcl == 99) {
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
                    "                            },\n" +
                    "                            {\n" +
                    "                                \"Signer\":\n" +
                    "                                {\n" +
                    "                                    \"LocalId\":\"0003948083\",\n" +
                    "                                    \"Role\":{\"$\":\"" + Role1 + "\",\"@version\":\"2.4\"},\n" +
                    "                                    \"LastName\":\"Стрекнев\",\n" +
                    "                                    \"FirstName\":\"Денис\",\n" +
                    "                                    \"MiddleName\":\"Юрьевич\",\n" +
                    "                                    \"Snils\":\"18287265004\",\n" +
                    "                                    \"Position\":{\"$\":\"" + speciality1 + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                                    \"Speciality\":{\"$\":" + position1 + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                                    \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                                },\n" +
                    "                                    \"Signature\":\n" +
                    "                                    {\n" +
                    "                                        \"Data\":\"" + Signatures + "\",\n" +
                    "                                        \"CheckSum\":" + CheckSumSign + "\n" +
                    "                                        },\n" +
                    "                                        \"Description\":\"Подпись сотрудника\"\n" +
                    "                                    }\n" +
                    "        ]\n" +
                    "    }";
        } else {
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
                    "                    \"vmcl\": " + vmcl + ",\n" +
                    "                    \"triggerPoint\": 2,\n" +
                    "                    \"docTypeVersion\": " + docTypeVersion + "\n" +
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
                    "                            },\n" +
                    "                            {\n" +
                    "                                \"Signer\":\n" +
                    "                                {\n" +
                    "                                    \"LocalId\":\"0003948083\",\n" +
                    "                                    \"Role\":{\"$\":\"" + Role1 + "\",\"@version\":\"2.4\"},\n" +
                    "                                    \"LastName\":\"Стрекнев\",\n" +
                    "                                    \"FirstName\":\"Денис\",\n" +
                    "                                    \"MiddleName\":\"Юрьевич\",\n" +
                    "                                    \"Snils\":\"18287265004\",\n" +
                    "                                    \"Position\":{\"$\":\"" + speciality1 + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                                    \"Speciality\":{\"$\":" + position1 + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                                    \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                                },\n" +
                    "                                    \"Signature\":\n" +
                    "                                    {\n" +
                    "                                        \"Data\":\"" + Signatures + "\",\n" +
                    "                                        \"CheckSum\":" + CheckSumSign + "\n" +
                    "                                        },\n" +
                    "                                        \"Description\":\"Подпись сотрудника\"\n" +
                    "                                    }\n" +
                    "        ]\n" +
                    "    }";
        }
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

    @Test
    @Story("Отправка документов PDF/ PDF+BOM/ XML")
    @Order(1)
    public void ClearDocsTest() throws SQLException {
        sql.StartConnection("Select count(*) from vimis.remd_sent_result where patient_guid = '3791bfa4-c234-43d4-a83c-8d495bca5a55';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
            System.out.println(sql.value);
        }
        if (!sql.value.contains("0")) {
            sql.UpdateConnection(
                    "Update vimis.remd_sent_result set status = 'error', fremd_status = null where patient_guid = '3791bfa4-c234-43d4-a83c-8d495bca5a55';");
        }
    }

    @Test
    @Order(2)
    @Story("Отправка документов PDF/ PDF+BOM/ XML")
    @Issue(value = "TEL-1663")
    @Issue(value = "TEL-1671")
    @Link(name = "ТМС-1732", url = "https://team-1okm.testit.software/projects/5/tests/1732?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Link(name = "ТМС-1756", url = "https://team-1okm.testit.software/projects/5/tests/1756?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправляем ПДФ файл с нужным пациентом, выставляем status = success, fremd_status = 1, переходим в ЛК Врача этого пациента и проверяем отображение ПДФ документа. Отправляем XML документ и проверяем отображенеи в ЛК Врача")
    @DisplayName("1 Проверка - Проверяем добавление XML документа, метод api/smd")
    public void Access_1663_1() throws SQLException, InterruptedException, IOException {
        System.out.println("1 Проверка - Проверяем добавление XML документа без BOM, метод api/smd");
        Access_1663MethodAddSms("SMS/SMS3.xml", 3, "/api/smd", false, 1, 3, 1, 9, 18, 1, 57, 21);
        Access_1663Method(false);
    }

    @Test
    @Order(3)
    @Story("Отправка документов PDF/ PDF+BOM/ XML")
    @DisplayName("Возвращаем значения")
    public void Access_1663_1_() throws SQLException, IOException {
        sql.UpdateConnection("Update vimis.documentlogs set status = '0' where sms_id = '" + ReadPropFile("IdSms_1663") + "';");
        sql.UpdateConnection(
                "Update vimis.additionalinfo set effectivetime = '10.10.2005 12:44:00.000 +0500' where smsid = '" + ReadPropFile("IdSms_1663") + "';");
    }

    @Test
    @Order(4)
    @Story("Отправка документов PDF/ PDF+BOM/ XML")
    @DisplayName("2 Проверка - Проверяем добавление XML документа с BOM, метод api/smd")
    public void Access_1663_2() throws SQLException, InterruptedException, IOException {
        System.out.println("2 Проверка - Проверяем добавление PDF документа с BOM, метод api/smd");
        Access_1663MethodAddSms("SMS/SMS3.xml", 3, "/api/smd", true, 1, 3, 1, 9, 18, 1, 57, 21);
        Access_1663Method(false);
    }

    @Test
    @Order(5)
    @Story("Отправка документов PDF/ PDF+BOM/ XML")
    @DisplayName("Возвращаем значения")
    public void Access_1663_2_() throws SQLException, IOException {
        sql.UpdateConnection("Update vimis.documentlogs set status = '0' where sms_id = '" + ReadPropFile("IdSms_1663") + "';");
        sql.UpdateConnection(
                "Update vimis.additionalinfo set effectivetime = '10.10.2005 12:44:00.000 +0500' where smsid = '" + ReadPropFile("IdSms_1663") + "';");
    }

    @Test
    @Order(6)
    @Story("Отправка документов PDF/ PDF+BOM/ XML")
    @DisplayName("3 Проверка - Проверяем добавление XML документа без BOM, метод api/smd/withoutvalidation")
    public void Access_1663_3() throws SQLException, InterruptedException, IOException {
        System.out.println("3 Проверка - Проверяем добавление XML документа, метод api/smd/withoutvalidation");
        Access_1663MethodAddSms("SMS/SMS3.xml", 3, "/api/smd/withoutvalidation", false, 1, 3, 1, 9, 18, 1, 57, 21);
        Access_1663Method(false);
    }

    @Test
    @Order(7)
    @Story("Отправка документов PDF/ PDF+BOM/ XML")
    @DisplayName("Возвращаем значения")
    public void Access_1663_3_() throws SQLException, IOException {
        sql.UpdateConnection("Update vimis.documentlogs set status = '0' where sms_id = '" + ReadPropFile("IdSms_1663") + "';");
        sql.UpdateConnection(
                "Update vimis.additionalinfo set effectivetime = '10.10.2005 12:44:00.000 +0500' where smsid = '" + ReadPropFile("IdSms_1663") + "';");
    }

    @Test
    @Order(8)
    @Story("Отправка документов PDF/ PDF+BOM/ XML")
    @DisplayName("4 Проверка - Проверяем добавление XML документа с BOM, метод api/smd/withoutvalidation")
    public void Access_1663_4() throws SQLException, InterruptedException, IOException {
        System.out.println("4 Проверка - Проверяем добавление XML документа с BOM, метод api/smd/withoutvalidation");
        Access_1663MethodAddSms("SMS/SMS3.xml", 3, "/api/smd/withoutvalidation", true, 1, 3, 1, 9, 18, 1, 57, 21);
        Access_1663Method(false);
    }

    @Test
    @Order(9)
    @Story("Отправка документов PDF/ PDF+BOM/ XML")
    @DisplayName("Возвращаем значения")
    public void Access_1663_4_() throws SQLException, IOException {
        sql.UpdateConnection("Update vimis.documentlogs set status = '0' where sms_id = '" + ReadPropFile("IdSms_1663") + "';");
        sql.UpdateConnection(
                "Update vimis.additionalinfo set effectivetime = '10.10.2005 12:44:00.000 +0500' where smsid = '" + ReadPropFile("IdSms_1663") + "';");
    }
}
