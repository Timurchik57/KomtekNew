package api.Access_1444;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import Base.XML;
import api.Before.Authorization;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileInputStream;
import java.io.IOException;
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
@Feature("Сохранение файлов в minio при отправке смс по нескольким направлениям")
@Tag("МИНИО")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Проверка_info")
public class Access_1444_2 extends BaseAPI {
    public String URLRemd;
    public String body;
    public String content;
    public String encodedString;
    public UUID uuid;
    public String minio_document_name;
    public String SmsId1;
    public String SmsId2;
    public String SmsId3;
    public String SmsId4;
    public String SmsId5;

    @Step("Отправляем документ {0} по нескольким направлениям")
    public void Access_1444Method(String FileName) throws IOException, InterruptedException {
        if (KingNumber == 2) {
            FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");

            props = new Properties();
            props.load(in);
            in.close();

            Token = TokenInit();

            XML.Type = FileName;
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
            content = new String(Files.readAllBytes(Paths.get(FileName)));
            encodedString = Base64.getEncoder().encodeToString(content.getBytes());
            /** Подсчёт чек Суммы для XML */
            String sum = given()
                    .contentType(ContentType.JSON)
                    .body("{\n" +
                                  "  \"base64String\": \"" + encodedString + "\"\n" +
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
                            "            \"serialNumber\": \""+SerialNumberXml+"\",\n" +
                            "            \"message\": \"" + encodedString + "\",\n" +
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
            uuid = UUID.randomUUID();
            System.out.println(uuid);
            body = "{\n" +
                    "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                    "    \"DocType\":\"3\",\n" +
                    "    \"DocContent\":\n" +
                    "    {\n" +
                    "        \"Document\":\"" + encodedString + "\",\n" +
                    "        \"CheckSum\":" + CheckSum + "\n" +
                    "        },\n" +
                    "        \"LocalUid\":\"" + uuid + "\",\n" +
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
                    "                    \"vmcl\": 1,\n" +
                    "                    \"triggerPoint\": 2,\n" +
                    "                    \"docTypeVersion\": 3\n" +
                    "                    \n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"vmcl\": 2,\n" +
                    "                    \"triggerPoint\": 2,\n" +
                    "                    \"docTypeVersion\": 3\n" +
                    "                    \n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"vmcl\": 3,\n" +
                    "                    \"triggerPoint\": 2,\n" +
                    "                    \"docTypeVersion\": 2\n" +
                    "                    \n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"vmcl\": 4,\n" +
                    "                    \"triggerPoint\": 2,\n" +
                    "                    \"docTypeVersion\": 2\n" +
                    "                    \n" +
                    "                },\n" +
                    "                {\n" +
                    "                            \"vmcl\": 5,\n" +
                    "                    \"triggerPoint\": 2,\n" +
                    "                    \"docTypeVersion\": 3\n" +
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
                    "                            \"Role\":{\"$\":\"1\",\"@version\":\"2.4\"},\n" +
                    "                            \"LastName\":\"Коситченков\",\n" +
                    "                            \"FirstName\":\"Андрей\",\n" +
                    "                            \"MiddleName\":\"Александрович\",\n" +
                    "                            \"Snils\":\"18259202174\",\n" +
                    "                            \"Position\":{\"$\":\"9\",\"@version\":\"" + Position + "\"},\n" +
                    "                            \"Speciality\":{\"$\":18,\"@version\":\"" + Speciality + "\"},\n" +
                    "                            \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                        },\n" +
                    "                            \"Signature\":\n" +
                    "                            {\n" +
                    "                                \"Data\":\"" + Signatures + "\",\n" +
                    "                                  \"CheckSum\":" + CheckSumSign + "\n" +
                    "                            },\n" +
                    "                            \"Description\":\"Подпись сотрудника\"\n" +
                    "                            },\n" +
                    "                            {\n" +
                    "                                \"Signer\":\n" +
                    "                                {\n" +
                    "                                    \"LocalId\":\"0003948083\",\n" +
                    "                                    \"Role\":{\"$\":\"1\",\"@version\":\"2.4\"},\n" +
                    "                                    \"LastName\":\"Стрекнев\",\n" +
                    "                                    \"FirstName\":\"Денис\",\n" +
                    "                                    \"MiddleName\":\"Юрьевич\",\n" +
                    "                                    \"Snils\":\"18287265004\",\n" +
                    "                                    \"Position\":{\"$\":\"57\",\"@version\":\"" + Position + "\"},\n" +
                    "                                    \"Speciality\":{\"$\":21,\"@version\":\"" + Speciality + "\"},\n" +
                    "                                    \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                                },\n" +
                    "                                    \"Signature\":\n" +
                    "                                    {\n" +
                    "                                        \"Data\":\"" + Signatures + "\",\n" +
                    "                                       \"CheckSum\":" + CheckSumSign + "\n" +
                    "                                        },\n" +
                    "                                        \"Description\":\"Подпись сотрудника\"\n" +
                    "                                    }\n" +
                    "        ]\n" +
                    "    }";
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .body()
                    .jsonPath();
        }
    }

    @Step("Проверка значений в таблицах sms и info")
    public void Access_1444_2Method() throws SQLException {

        /** Вычисляем текущую дату */
        GetDate();

        System.out.println("Находим запись по local_uid в таблицах и проверяем добавление данных");
        sql.StartConnection("Select * from vimis.sms where local_uid = '" + uuid + "';");
        while (sql.resultSet.next()) {
            minio_document_name = sql.resultSet.getString("minio_document_name");
            SmsId1 = sql.resultSet.getString("id");
            sql.value = sql.resultSet.getString("document");
            if (SmdToMinio == 1) {
                Assertions.assertNotNull(minio_document_name, "minio_document_name пустое при SmdToMinio = 1");
                Assertions.assertNull(sql.value, "document заполнено при SmdToMinio = 1");

                /** Проверяем что, minio_document_name сохраняется в формате 2024/5/6/10/1001/21/967f33f6-76cb-4acc-8142-061e39eec2e9 */
                Assertions.assertTrue(minio_document_name.contains(""+Year+"/"+Month+"/"+Day+""));
            }
            if (SmdToMinio == 0) {
                Assertions.assertNull(minio_document_name, "minio_document_name не пустое при SmdToMinio = 0");
                Assertions.assertNotNull(sql.value, "document не заполнено при SmdToMinio = 0");
            }
        }
        sql.StartConnection("Select * from vimis.preventionsms where local_uid = '" + uuid + "';");
        while (sql.resultSet.next()) {
            SmsId2 = sql.resultSet.getString("id");
            if (SmdToMinio == 1) {
                sql.value = sql.resultSet.getString("minio_document_name");
                Assertions.assertEquals(sql.value, minio_document_name, "minio_document_name не совпадает");
                sql.value = sql.resultSet.getString("document");
                Assertions.assertNull(sql.value, "document заполнено при SmdToMinio = 1");
            }
            if (SmdToMinio == 0) {
                sql.value = sql.resultSet.getString("minio_document_name");
                Assertions.assertNull(sql.value, "minio_document_name не пустое при SmdToMinio = 0");
                sql.value = sql.resultSet.getString("document");
                Assertions.assertNotNull(sql.value, "document не заполнено при SmdToMinio = 0");
            }
        }
        sql.StartConnection("Select * from vimis.akineosms where local_uid = '" + uuid + "';");
        while (sql.resultSet.next()) {
            SmsId3 = sql.resultSet.getString("id");
            if (SmdToMinio == 1) {
                sql.value = sql.resultSet.getString("minio_document_name");
                Assertions.assertEquals(sql.value, minio_document_name, "minio_document_name не совпадает");
                sql.value = sql.resultSet.getString("document");
                Assertions.assertNull(sql.value, "document заполнено при SmdToMinio = 1");
            }
            if (SmdToMinio == 0) {
                sql.value = sql.resultSet.getString("minio_document_name");
                Assertions.assertNull(sql.value, "minio_document_name не пустое при SmdToMinio = 0");
                sql.value = sql.resultSet.getString("document");
                Assertions.assertNotNull(sql.value, "document не заполнено при SmdToMinio = 0");
            }
        }
        sql.StartConnection("Select * from vimis.cvdsms where local_uid = '" + uuid + "';");
        while (sql.resultSet.next()) {
            SmsId4 = sql.resultSet.getString("id");
            if (SmdToMinio == 1) {
                sql.value = sql.resultSet.getString("minio_document_name");
                Assertions.assertEquals(sql.value, minio_document_name, "minio_document_name не совпадает");
                sql.value = sql.resultSet.getString("document");
                Assertions.assertNull(sql.value, "document заполнено при SmdToMinio = 1");
            }
            if (SmdToMinio == 0) {
                sql.value = sql.resultSet.getString("minio_document_name");
                Assertions.assertNull(sql.value, "minio_document_name не пустое при SmdToMinio = 0");
                sql.value = sql.resultSet.getString("document");
                Assertions.assertNotNull(sql.value, "document не заполнено при SmdToMinio = 0");
            }
        }
        sql.StartConnection("Select * from vimis.infectionsms where local_uid = '" + uuid + "';");
        while (sql.resultSet.next()) {
            SmsId5 = sql.resultSet.getString("id");
            if (SmdToMinio == 1) {
                sql.value = sql.resultSet.getString("minio_document_name");
                Assertions.assertEquals(sql.value, minio_document_name, "minio_document_name не совпадает");
                sql.value = sql.resultSet.getString("document");
                Assertions.assertNull(sql.value, "document заполнено при SmdToMinio = 1");
            }
            if (SmdToMinio == 0) {
                sql.value = sql.resultSet.getString("minio_document_name");
                Assertions.assertNull(sql.value, "minio_document_name не пустое при SmdToMinio = 0");
                sql.value = sql.resultSet.getString("document");
                Assertions.assertNotNull(sql.value, "document не заполнено при SmdToMinio = 0");
            }
        }

        System.out.println("Находим запись по local_uid в таблицах info и проверяем добавление данных");
        sql.StartConnection("Select * from vimis.additionalinfo where smsid = '" + SmsId1 + "'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("version");
            Assertions.assertNotNull(sql.value, "version не заполнено");
            sql.value = sql.resultSet.getString("setid");
            Assertions.assertNotNull(sql.value, "setid не заполнено");
            sql.value = sql.resultSet.getString("documentid");
            Assertions.assertNotNull(sql.value, "documentid не заполнено");
        }
        sql.StartConnection("Select * from vimis.preventionadditionalinfo where smsid = '" + SmsId2 + "'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("version");
            Assertions.assertNotNull(sql.value, "version не заполнено");
            sql.value = sql.resultSet.getString("setid");
            Assertions.assertNotNull(sql.value, "setid не заполнено");
            sql.value = sql.resultSet.getString("documentid");
            Assertions.assertNotNull(sql.value, "documentid не заполнено");
        }
        sql.StartConnection("Select * from vimis.akineoadditionalinfo where smsid = '" + SmsId3 + "'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("version");
            Assertions.assertNotNull(sql.value, "version не заполнено");
            sql.value = sql.resultSet.getString("setid");
            Assertions.assertNotNull(sql.value, "setid не заполнено");
            sql.value = sql.resultSet.getString("documentid");
            Assertions.assertNotNull(sql.value, "documentid не заполнено");
        }
        sql.StartConnection("Select * from vimis.cvdadditionalinfo where smsid = '" + SmsId4 + "'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("version");
            Assertions.assertNotNull(sql.value, "version не заполнено");
            sql.value = sql.resultSet.getString("setid");
            Assertions.assertNotNull(sql.value, "setid не заполнено");
            sql.value = sql.resultSet.getString("documentid");
            Assertions.assertNotNull(sql.value, "documentid не заполнено");
        }
        sql.StartConnection("Select * from vimis.infectionadditionalinfo where smsid = '" + SmsId5 + "'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("version");
            Assertions.assertNotNull(sql.value, "version не заполнено");
            sql.value = sql.resultSet.getString("setid");
            Assertions.assertNotNull(sql.value, "setid не заполнено");
            sql.value = sql.resultSet.getString("documentid");
            Assertions.assertNotNull(sql.value, "documentid не заполнено");
        }
    }

    @Issue(value = "TEL-1444")
    @Issue(value = "TEL-1443")
    @Issue(value = "TEL-1457")
    @Link(name = "ТМС-1553", url = "https://team-1okm.testit.software/projects/5/tests/1553?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Description("Переходим в таблицы sms/info и проверяем добавление данных")
    @DisplayName("Сохранение файлов в minio при отправке смс по нескольким направлениям")
    @Test
    public void Access_1444_vmcl1234Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS3.xml");
        Access_1444_2Method();
    }
}
