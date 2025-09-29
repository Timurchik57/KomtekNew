package api.Access_Notification;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import Base.XML;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Добавление данных из Soap запроса, проверка уведомления тип 10")
@Tag("Оповещение")
@Tag("soap")
@Tag("Проверка_БД")
@Tag("Вимис_статус")
@Tag("Проверка_info")
public class Access_1318_Type10_Test extends BaseAPI {
    public String URLCollback;
    Document_667 doc;
    public String body;
    public String content;
    public String encodedString;
    public String id;
    public String local_uid;
    public UUID uuid;
    public String SignatureBody;
    public String SignatureValue;
    public String SignedInfo;
    public String BinSecurityToken;

    @Issue(value = "TEL-1318")
    @Issue(value = "TEL-1330")
    @Issue(value = "TEL-1352")
    @Issue(value = "TEL-2660")
    @Link(name = "ТМС-1493", url = "https://team-1okm.testit.software/projects/5/tests/1493?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Link(name = "ТМС-1508", url = "https://team-1okm.testit.software/projects/5/tests/1508?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Добавление данных из Soap запроса vmcl = 1")
    @Description("Отправляем смс soap запросом, проверить добавление в таблицы. Сменить статус смс и проверить уведомление")
    public void Access_1318 () throws IOException, SQLException, InterruptedException {
        Access1328SignatureMethod("SMS/SMS5.xml", SoapSystemId, 1, "5", 3);
        Access_1318BodyMethod("SMS/SMS5.xml", "5", 1, 3, SoapSystemId, "vimis.sms", "vimis.additionalinfo");
        Access1318CallBackMethod("vimis.sms", "vimis.documentlogs", 1);
    }

    @Test
    @DisplayName("Добавление данных из Soap запроса vmcl = 2")
    public void Access_1318_2 () throws IOException, SQLException, InterruptedException {
        Access1328SignatureMethod("SMS/SMS5-vmcl=2.xml", SoapSystemId, 2, "5", 3);
        Access_1318BodyMethod("SMS/SMS5-vmcl=2.xml", "5", 2, 3, SoapSystemId, "vimis.preventionsms",
                "vimis.preventionadditionalinfo");
        Access1318CallBackMethod("vimis.preventionsms", "vimis.preventionlogs", 2);
    }

    @Test
    @DisplayName("Добавление данных из Soap запроса vmcl = 3")
    public void Access_1318_3 () throws IOException, SQLException, InterruptedException {
        Access1328SignatureMethod("SMS/SMS5-vmcl=3(v1.2).xml", SoapSystemId, 3, "5", 2);
        Access_1318BodyMethod("SMS/SMS5-vmcl=3(v1.2).xml", "5", 3, 2, SoapSystemId, "vimis.akineosms", "vimis.akineoadditionalinfo");
        Access1318CallBackMethod("vimis.akineosms", "vimis.akineologs", 3);
    }

    @Test
    @DisplayName("Добавление данных из Soap запроса vmcl = 4")
    public void Access_1318_4 () throws IOException, SQLException, InterruptedException {
        Access1328SignatureMethod("SMS/SMS6-vmcl=4.xml", SoapSystemId, 4, "6", 2);
        Access_1318BodyMethod("SMS/SMS6-vmcl=4.xml", "6", 4, 2, SoapSystemId, "vimis.cvdsms", "vimis.cvdadditionalinfo");
        Access1318CallBackMethod("vimis.cvdsms", "vimis.cvdlogs", 4);

    }

    @Test
    @DisplayName("Добавление данных из Soap запроса vmcl = 5")
    public void Access_1318_5 () throws IOException, SQLException, InterruptedException {
        Access1328SignatureMethod("SMS/SMS5-vmcl=5.xml", SoapSystemId, 5, "5", 3);
        Access_1318BodyMethod("SMS/SMS5-vmcl=5.xml", "5", 5, 3, SoapSystemId, "vimis.infectionsms",
                "vimis.infectionadditionalinfo");
        Access1318CallBackMethod("vimis.infectionsms", "vimis.infectionlogs", 5);
    }

    @Step("Метод для создания подписи Soap запроса")
    public void Access1328SignatureMethod (String File, String SystemId, Integer vmcl, String doctype, Integer docTypeVersion
    ) throws IOException, InterruptedException {
        if (KingNumber == 2 | KingNumber == 5) {
            xml.XmlStart(File, Departmen, MO, PatientGuid, NameMO, Snils, 1);

            System.out.println("Отправляем запрос на подпись для Soap");
            SignatureBody = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                    "\t<s:Header>\n" +
                    "\t\t<a:Action wsu:Id=\"id-action\">sendDocument</a:Action>\n" +
                    "\t\t<transportHeader xmlns=\"http://egisz.rosminzdrav.ru\">\n" +
                    "\t\t\t<authInfo>\n" +
                    "\t\t\t\t<clientEntityId>" + SystemId + "</clientEntityId>\n" +
                    "\t\t\t</authInfo>\n" +
                    "\t\t</transportHeader>\n" +
                    "\t\t<a:MessageID>43afe69d-b21a-428b-a624-f113f95ec743</a:MessageID>\n" +
                    "\t\t<a:ReplyTo wsu:Id=\"id-replyto\">\n" +
                    "\t\t\t<a:Address>https://ips.rosminzdrav.ru/79f5771afb36b</a:Address>\n" +
                    "\t\t</a:ReplyTo>\n" +
                    "\t\t<a:FaultTo>\n" +
                    "\t\t\t<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\n" +
                    "\t\t</a:FaultTo>\n" +
                    "\t\t<a:To>https://ips.rosminzdrav.ru/8b02e1e4e03c7</a:To>\n" +
                    "\t</s:Header>\n" +
                    "\t<s:Body xmlns:d2p1=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" d2p1:Id=\"body\">\n" +
                    "\t\t<sendDocument xmlns=\"http://vimis.rosminzdrav.ru/\">\n" +
                    "\t\t\t<vmcl xmlns=\"\">" + vmcl + "</vmcl>\n" +
                    "\t\t\t<vmcl_array xmlns=\"\">" + vmcl + "</vmcl_array>\n" +
                    "\t\t\t<docType xmlns=\"\">" + doctype + "</docType>\n" +
                    "\t\t\t<docTypeVersion xmlns=\"\">" + docTypeVersion + "</docTypeVersion>\n" +
                    "\t\t\t<interimMsg xmlns=\"\">1</interimMsg>\n" +
                    "\t\t\t<document xmlns=\"\">" + xml.encodedString + "</document>\n" +
                    "\t\t\t<triggerPoint xmlns=\"\">2</triggerPoint>\n" +
                    "\t\t</sendDocument>\n" +
                    "\t</s:Body>\n" +
                    "</s:Envelope>";
            String SignatureBodyBase64 = Base64.getEncoder().encodeToString(SignatureBody.getBytes());

            Api(HostSignatureSoap, "post", null, null,
                    "{\n" +
                            "                    \"requestId\": \"1\",\n" +
                            "                    \"signReferenceUri\": \"#body\",\n" +
                            "                    \"document\": \"" + SignatureBodyBase64 + "\",\n" +
                            "                    \"algorithm\": \"Gost3411_2012_256\",\n" +
                            "                    \"systemId\": \"" + SystemId + "\"\n" +
                            "  }", 200, false);

            System.out.println("Декодируем SignatureValue и SignedInfo");
            String SignatureValueBase64 = Response.get("result.signatureValue");
            byte[] decoded = Base64.getDecoder().decode(SignatureValueBase64);
            SignatureValue = new String(decoded, StandardCharsets.UTF_8);
            String SignedInfoBase64 = Response.get("result.signedInfo");
            decoded = Base64.getDecoder().decode(SignedInfoBase64);
            SignedInfo = new String(decoded, StandardCharsets.UTF_8);
            BinSecurityToken = Response.get("result.binSecurityToken");
        }
    }

    @Step("Метод для отправки смс с другим телом запроса (soap), и проверка добавления значений в нужные таблицы")
    public void Access_1318BodyMethod (String FileName, String DocType, Integer vmcl, Integer docTypeVersion, String SystemId, String sms, String info
    ) throws IOException, SQLException {
        if (KingNumber == 2 | KingNumber == 5) {
            body = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                    "\t<s:Header>\n" +
                    "\t\t<a:Action wsu:Id=\"id-action\">sendDocument</a:Action>\n" +
                    "\t\t<transportHeader xmlns=\"http://egisz.rosminzdrav.ru\">\n" +
                    "\t\t\t<authInfo>\n" +
                    "\t\t\t\t<clientEntityId>" + SystemId + "</clientEntityId>\n" +
                    "\t\t\t</authInfo>\n" +
                    "\t\t</transportHeader>\n" +
                    "\t\t<a:MessageID>43afe69d-b21a-428b-a624-f113f95ec743</a:MessageID>\n" +
                    "\t\t<a:ReplyTo wsu:Id=\"id-replyto\">\n" +
                    "\t\t\t<a:Address>https://ips.rosminzdrav.ru/79f5771afb36b</a:Address>\n" +
                    "\t\t</a:ReplyTo>\n" +
                    "\t\t<a:FaultTo>\n" +
                    "\t\t\t<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\n" +
                    "\t\t</a:FaultTo>\n" +
                    "\t\t<a:To>https://ips.rosminzdrav.ru/8b02e1e4e03c7</a:To>\n" +
                    "\t\t<wsse:Security>\n" +
                    "\t\t\t<ds:Signature Id=\"SIG-" + UUID.randomUUID() + "\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n" +
                    "\t\t\t\t" + SignedInfo + "\n" +
                    "\t\t\t\t<SignatureValue xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" + SignatureValue + "</SignatureValue>\n" +
                    "\t\t\t\t<ds:KeyInfo Id=\"KI-d11574b2-8970-4767-9e19-d9377929227d\">\n" +
                    "\t\t\t\t\t<wsse:SecurityTokenReference Id=\"" + UUID.randomUUID() + "\">\n" +
                    "\t\t\t\t\t\t<wsse:Reference ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" URI=\"#" + UUID.randomUUID() + "\"/>\n" +
                    "\t\t\t\t\t</wsse:SecurityTokenReference>\n" +
                    "\t\t\t\t</ds:KeyInfo>\n" +
                    "\t\t\t</ds:Signature>\n" +
                    "\t\t\t<wsse:BinarySecurityToken EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\" wsu:Id=\"a9671d73-c729-4327-959e-31928de90b2a\">" + BinSecurityToken + "</wsse:BinarySecurityToken>\n" +
                    "\t\t</wsse:Security>\n" +
                    "\t</s:Header>\n" +
                    "\t<s:Body xmlns:d2p1=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" d2p1:Id=\"body\">\n" +
                    "\t\t<sendDocument xmlns=\"http://vimis.rosminzdrav.ru/\">\n" +
                    "\t\t\t<vmcl xmlns=\"\">" + vmcl + "</vmcl>\n" +
                    "\t\t\t<vmcl_array xmlns=\"\">" + vmcl + "</vmcl_array>\n" +
                    "\t\t\t<docType xmlns=\"\">" + DocType + "</docType>\n" +
                    "\t\t\t<docTypeVersion xmlns=\"\">" + docTypeVersion + "</docTypeVersion>\n" +
                    "\t\t\t<interimMsg xmlns=\"\">1</interimMsg>\n" +
                    "\t\t\t<document xmlns=\"\">" + xml.encodedString + "</document>\n" +
                    "\t\t\t<triggerPoint xmlns=\"\">2</triggerPoint>\n" +
                    "\t\t</sendDocument>\n" +
                    "\t</s:Body>\n" +
                    "</s:Envelope>";

            System.out.println("Отправляем Soap запрос");
            Api(HostSoap, "post", null, null, body, 200, false);

            System.out.println("Запсываем полученный xml в файл");
            PrintWriter out = new PrintWriter("src/test/resources/ignored/1219_test.xml");
            out.println(body);
            out.close();

            System.out.println("Находим id записи в таблице " + sms + "");
            sql.StartConnection(
                    "Select max(id) from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and centralized_unloading_system_id = '99' and open_telemetry_trace_id = '00000000000000000000000000000000'");
            while (sql.resultSet.next()) {
                id = sql.resultSet.getString("max");
                System.out.println(id);
            }

            System.out.println("Находим запись по id в таблице " + sms + " и сверяем значения");
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and id = '" + id + "'");
            while (sql.resultSet.next()) {
                String patient_guid = sql.resultSet.getString("patient_guid");
                Assertions.assertEquals(
                        patient_guid, "e3c3323e-1e05-4f59-b733-9abe7dfc88ce", "patient_guid не совпадает");
                local_uid = sql.resultSet.getString("local_uid");
                String transfer_id = sql.resultSet.getString("transfer_id");
                Assertions.assertEquals(transfer_id, local_uid, "transfer_id не совпадает");
                String patient_snils = sql.resultSet.getString("patient_snils");
                Assertions.assertEquals(patient_snils, "15979025720", "patient_snils не совпадает");
            }

            System.out.println("Находим запись по id в таблице " + info + "");
            sql.StartConnection("Select * from " + info + " where smsid = '" + id + "'");
            while (sql.resultSet.next()) {
                String authorfname = sql.resultSet.getString("authorfname");
                String authorlname = sql.resultSet.getString("authorlname");
                String authorsnils = sql.resultSet.getString("authorsnils");
                String patientbirthday = sql.resultSet.getString("patientbirthday");
                String patientfname = sql.resultSet.getString("patientfname");
                String patientlname = sql.resultSet.getString("patientlname");
                String patient_local_id = sql.resultSet.getString("patient_local_id");
                String department_oid = sql.resultSet.getString("department_oid");

                String date = getXml(FileName, "(//administrativeGenderCode[@codeSystem='1.2.643.5.1.13.13.11.1040']/following-sibling::birthTime/@value)[1]");
                String patientName = getXml(FileName, "(//recordTarget/patientRole/patient/name/given[1]/text())");
                String patientlName = getXml(FileName, "(//recordTarget/patientRole/patient/name/family/text())");
                String name = getXml(FileName, "(//author/assignedAuthor/assignedPerson/name/given[1]/text())");
                String lname = getXml(FileName, "(//author/assignedAuthor/assignedPerson/name/family/text())");

                Assertions.assertEquals(authorfname, name, "authorfname не совпадает");
                Assertions.assertEquals(authorlname, lname, "authorlname не совпадает");
                Assertions.assertEquals(patientbirthday, date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8), "patientbirthday не совпадает");
                Assertions.assertEquals(patientfname, patientName, "patientfname не совпадает");
                Assertions.assertEquals(patientlname, patientlName, "patientlname не совпадает");
                Assertions.assertEquals(authorsnils, PSnils_, "authorsnils не совпадает");
                Assertions.assertEquals(patient_local_id, PatientGuid, "patient_local_id не совпадает");
                if (vmcl != 3) {
                    Assertions.assertEquals(department_oid, Departmen, "department_oid не совпадает");
                }
            }
        }
    }

    @Step("Метод для смены статуса отправленной смс (soap), и проверка отображения уведомления")
    public void Access1318CallBackMethod (String sms, String log, Integer vmcl) throws SQLException, InterruptedException {
        doc = new Document_667();
        if (KingNumber == 2 | KingNumber == 5) {
            System.out.println("Переходим на сайт для перехвата сообщений");
            driver.get("https://12345.requestcatcher.com/");
            Thread.sleep(1500);

            System.out.println(
                    "В таблице " + sms + " устанавливаем нужный uuid, который будет в запросе на смену статуса");
            sql.UpdateConnection("update " + sms + " set request_id = '" + doc.uuid + "' where id = " + id + ";");

            System.out.println("Отправляем запрос на смену статуса");
            if (KingNumber == 1) {
                URLCollback = "http://192.168.2.126:1108/onko/callback";
            }
            if (KingNumber == 2) {
                URLCollback = "http://192.168.2.126:1131/onko/callback";
            }
            if (KingNumber == 5 && vmcl == 1) {
                URLCollback = "192.168.137.77:1108/onko/callback";
            }
            if (KingNumber == 5 && vmcl == 2) {
                URLCollback = "http://212.96.206.70:1109/prevention/callback";
            }
            if (KingNumber == 5 && vmcl == 3) {
                URLCollback = "192.168.137.77:1108/akineo/callback";
            }
            if (KingNumber == 5 && vmcl == 4) {
                URLCollback = "192.168.137.77:1108/ssz/callback";
            }
            given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .when()
                    .body(doc.body)
                    .post(URLCollback)
                    .then()
                    .statusCode(200);
            Thread.sleep(2000);

            System.out.println("В таблице logs поле msg_id " + doc.uuid);
            System.out.println("localUid и transferId " + local_uid);

            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            ClickElement(By.xpath("//div[@id='selector']/div[1]"));
            WaitElement(By.xpath("//div[@class='request']/pre"));
            String text = driver.findElement(By.xpath("//div[@class='request']/pre")).getText();
            System.out.println(text);

            Assertions.assertTrue(text.contains("Проверяем отправку уведомлений"),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");

            /** Проверка, что передаётся в оповещении local_uid (2660) */
            Assertions.assertTrue(text.contains("" + local_uid + ""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");

            sql.StartConnection("SELECT * FROM " + log + "  where sms_id = " + id + ";");
            while (sql.resultSet.next()) {
                String Value1 = sql.resultSet.getString("sms_id");
                String Value2 = sql.resultSet.getString("description");
                String Value3 = sql.resultSet.getString("msg_id");

                Assertions.assertEquals(Value1, id, "СМС не добавилось в таблицу " + log + "");
                Assertions.assertEquals(Value2, "Проверяем отправку уведомлений",
                        "СМС не добавилось в таблицу " + log + " с сообщением - Проверяем отправку уведомлений");
                Assertions.assertEquals(Value3, "" + doc.uuid + "",
                        "СМС не добавилось в таблицу " + log + " с msg_id - " + doc.uuid + "");
            }
        }
    }
}
