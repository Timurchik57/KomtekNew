package api.Access_1444;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Сохранение файлов в minio при отправке смс")
@Tag("МИНИО")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Проверка_БД")
public class Access_1444 extends BaseAPI {
    public String URLRemd;
    public String minio_document_name;

    /**
     * Отправка vmcl = 1/2/3/4 в ВИМИС
     */
    @Step("Отправка смс с DocType = {1} vmcl = {2} с SMDToMinio = {15}")
    public void Access_1444Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd, Integer SMDToMinio) throws IOException, InterruptedException, SQLException {

        /** Вычисляем текущую дату */
        GetDate();

        System.out.println("Отправляем запрос смс " + DocType + " с vmlc=" + vmcl + "");
        xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                position1, speciality1);
        if (vmcl != 99) {
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                System.out.println(sql.value);
                String Local_Uid = sql.resultSet.getString("local_uid");
                String document = sql.resultSet.getString("document");
                minio_document_name = sql.resultSet.getString("minio_document_name");
                if (SMDToMinio == 1) {
                    if (vmcl == 1 & FileName != "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis1", Local_Uid);
                    }
                    if (vmcl == 2 & FileName != "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis2", Local_Uid);
                    }
                    if (vmcl == 3 & FileName != "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis3", Local_Uid);
                    }
                    if (vmcl == 4 & FileName != "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis4", Local_Uid);
                    }
                    if (vmcl == 5 & FileName != "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis5", Local_Uid);
                    }
                    if (vmcl == 1 & FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd1", Local_Uid);
                    }
                    if (vmcl == 2 & FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd2", Local_Uid);
                    }
                    if (vmcl == 3 & FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd3", Local_Uid);
                    }
                    if (vmcl == 4 & FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd4", Local_Uid);
                    }
                    if (vmcl == 5 & FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd5", Local_Uid);
                    }
                    if (SmdToMinio == 1) {
                        Assertions.assertNull(document,
                                "При SmdToMinio = 1, поле document не должно быть заполнено");
                        Assertions.assertNotEquals(
                                minio_document_name, null,
                                "При SmdToMinio = 1, поле minio_document_name должно быть заполнено");

                        /** Проверяем что, minio_document_name сохраняется в формате 2024/5/6/10/1001/21/967f33f6-76cb-4acc-8142-061e39eec2e9 */
                        Assertions.assertTrue(minio_document_name.contains("" + Year + "/" + Month + "/" + Day + ""));
                    } else {
                        Assertions.assertNotNull(document,
                                "При SmdToMinio = 0, поле document должно быть заполнено");
                        Assertions.assertEquals(
                                minio_document_name, null,
                                "При SmdToMinio = 0, поле minio_document_name не должно быть заполнено");
                    }
                }
                if (SMDToMinio == 0) {
                    if (vmcl == 1 & FileName != "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis1_2", Local_Uid);
                    }
                    if (vmcl == 2 & FileName != "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis2_2", Local_Uid);
                    }
                    if (vmcl == 3 & FileName != "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis3_2", Local_Uid);
                    }
                    if (vmcl == 4 & FileName != "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis4_2", Local_Uid);
                    }
                    if (vmcl == 5 & FileName != "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimis5_2", Local_Uid);
                    }
                    if (vmcl == 1 & FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd1_2", Local_Uid);
                    }
                    if (vmcl == 2 & FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd2_2", Local_Uid);
                    }
                    if (vmcl == 3 & FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd3_2", Local_Uid);
                    }
                    if (vmcl == 4 & FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd4_2", Local_Uid);
                    }
                    if (vmcl == 5 & FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdVimisRemd5_2", Local_Uid);
                    }
                    if (SmdToMinio == 0) {
                        Assertions.assertNotEquals(document, null,
                                "При SmdToMinio = 0, поле document должно быть заполнено");
                        Assertions.assertNull(
                                minio_document_name,
                                "При SmdToMinio = 0, поле minio_document_name не должно быть заполнено"
                        );
                    } else {
                        Assertions.assertEquals(document, null,
                                "При SmdToMinio = 1, поле document не должно быть заполнено");
                        Assertions.assertNotNull(
                                minio_document_name,
                                "При SmdToMinio = 1, поле minio_document_name должно быть заполнено"
                        );
                    }
                }
            }
            if (FileName == "SMS/SMS3.xml") {
                CollbekVimis("" + xml.uuid + "", "1", "Проверка 1444", sms, vmcl);
                WaitStatusKremd(remd, "" + xml.uuid + "");
                CollbekKremd("" + xml.uuid + "", "success", "Проверка 1444", remd);
            }
        }
        if (vmcl == 99) {
            sql.StartConnection(
                    "SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                String Local_Uid = sql.resultSet.getString("local_uid");
                String document = sql.resultSet.getString("document");
                minio_document_name = sql.resultSet.getString("minio_document_name");
                System.out.println(sql.value);
                if (SMDToMinio == 1) {
                    if (FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdRemd", Local_Uid);
                    }
                    if (FileName == "SMS/id=101-vmcl=99.txt") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdCertRemd", Local_Uid);
                    }
                }
                if (SMDToMinio == 0) {
                    if (FileName == "SMS/SMS3.xml") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdRemd_2", Local_Uid);
                    }
                    if (FileName == "SMS/id=101-vmcl=99.txt") {
                        InputProp("src/test/java/api/Access_1444/1444.properties", "SmsIdCertRemd_2", Local_Uid);
                    }
                }

                if (SmdToMinio == 1) {
                    Assertions.assertNull(document,
                            "При SmdToMinio = 1, поле document не должно быть заполнено");
                    Assertions.assertNotEquals(
                            minio_document_name, null,
                            "При SmdToMinio = 1, поле minio_document_name должно быть заполнено");

                    /** Проверяем что, minio_document_name сохраняется в формате 2024/5/6/10/1001/21/967f33f6-76cb-4acc-8142-061e39eec2e9 */
                    Assertions.assertTrue(minio_document_name.contains("" + Year + "/" + Month + "/" + Day + ""));
                } else {
                    Assertions.assertNotNull(document,
                            "При SmdToMinio = 0, поле document должно быть заполнено");
                    Assertions.assertEquals(
                            minio_document_name, null,
                            "При SmdToMinio = 0, поле minio_document_name не должно быть заполнено");
                }
            }
        }
        if (FileName != "SMS/id=101-vmcl=99.txt") {
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddress + "/api/smd/document?LocalUid=" + xml.uuid)
                    .body()
                    .jsonPath();
            Assertions.assertEquals(
                    response.get("result[0].localUid"), "" + xml.uuid + "", "Local_uid не совпадает");
            Assertions.assertNotNull(response.get("result[0].document"), "document не должен отсутствовать");
            Assertions.assertNull(
                    response.get("result[0].objectStorageDocumentName"),
                    "ObjectStorageDocumentName не должен присутствовать");
        }
        if (vmcl == 99 | FileName == "SMS/SMS3.xml") {
            URLRemd = ApiVimis + "/api/rremd?LocalUid=" + xml.uuid;
            JsonPath respons = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(URLRemd)
                    .body()
                    .jsonPath();
            Assertions.assertEquals(respons.get("result[0]." + DocumentDto + "localUid"), "" + xml.uuid + "",
                    "Local_uid не совпадает");


            if (SmdToMinio == 1) {
                Assertions.assertEquals(respons.get("result[0].objectStorageDocumentName"), "" + minio_document_name + "",
                        "minio_document_name не совпадает");
            } else {
                Assertions.assertNull(respons.get("result[0].objectStorageDocumentName"), "objectStorageDocumentName должен отсутствовать");
            }
        }
    }

    @Issue(value = "TEL-1444")
    @Issue(value = "TEL-1443")
    @Issue(value = "TEL-1457")
    @Issue(value = "TEL-3015")
    @Link(name = "ТМС-1553", url = "https://team-1okm.testit.software/projects/5/tests/1553?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Description("Сохранение файлов в minio при отправке смс")
    @Story("Отправка смс в ВИМИС")
    @DisplayName("Отправка смс с vmcl=1 в ВИМИС SMDToMinio = 1")
    @Test
    public void Access_1444_vmcl1234Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "", "", 1);
    }

    @DisplayName("Отправка смс с vmcl=2 в ВИМИС SMDToMinio = 1")
    @Story("Отправка смс в ВИМИС")
    @Test
    public void Access_1444_vmcl2Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS2-id=42.xml", "42", 2, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms", "", "", 1);
    }

    @DisplayName("Отправка смс с vmcl=3 в ВИМИС SMDToMinio = 1")
    @Story("Отправка смс в ВИМИС")
    @Test
    public void Access_1444_vmcl3Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 3, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "", "", 1);
    }

    @DisplayName("Отправка смс с vmcl=4 в ВИМИС SMDToMinio = 1")
    @Story("Отправка смс в ВИМИС")
    @Test
    public void Access_1444_vmcl4Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 4, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.cvdsms", "", "", 1);
    }

    @DisplayName("Отправка смс с vmcl=5 в ВИМИС SMDToMinio = 1")
    @Story("Отправка смс в ВИМИС")
    @Test
    public void Access_1444_vmcl5Vimis() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 5, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.infectionsms", "", "",
                1);
    }

    @DisplayName("Отправка смс с vmcl=99 SMDToMinio = 1")
    @Story("Отправка смс в РЭМД")
    @Test
    public void Access_1444_vmcl99() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 99, 1, true, 3, 1, 9, 18, 1, 57, 21, "", "", "vimis.remd_sent_result", 1);
    }

    @DisplayName("Отправка смс с vmcl=99 pdf SMDToMinio = 1")
    @Story("Отправка смс в РЭМД")
    @Test
    public void Access_1444_vmcl99Pdf() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/id=101-vmcl=99.txt", "101", 99, 1, true, 3, 1, 57, 18, 1, 57, 21, "", "",
                "vimis.certificate_remd_sent_result", 1
        );
    }

    @DisplayName("Отправка смс с vmcl=1 в ВИМИС и РЭМД SMDToMinio = 1")
    @Story("Отправка смс в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl1VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result", 1
        );

    }

    @DisplayName("Отправка смс с vmcl=2 в ВИМИС и РЭМД SMDToMinio = 1")
    @Story("Отправка смс в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl2VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 1
        );
    }

    @DisplayName("Отправка смс с vmcl=3 в ВИМИС и РЭМД SMDToMinio = 1")
    @Story("Отправка смс в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl3VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result", 1
        );

    }

    @DisplayName("Отправка смс с vmcl=4 в ВИМИС и РЭМД SMDToMinio = 1")
    @Story("Отправка смс в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl4VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result", 1
        );
    }

    @DisplayName("Отправка смс с vmcl=5 в ВИМИС и РЭМД SMDToMinio = 1")
    @Story("Отправка смс в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl5VimisRemd() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result", 1
        );
    }

    @DisplayName("Отправка смс с vmcl=1 в ВИМИС SMDToMinio = 0")
    @Story("Отправка смс в ВИМИС")
    @Test
    public void Access_1444_vmcl1234Vimis0() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "", "", 0);
    }

    @DisplayName("Отправка смс с vmcl=2 в ВИМИС SMDToMinio = 0")
    @Story("Отправка смс в ВИМИС")
    @Test
    public void Access_1444_vmcl2Vimis0() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS2-id=42.xml", "42", 2, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms", "", "", 0);
    }

    @DisplayName("Отправка смс с vmcl=3 в ВИМИС SMDToMinio = 0")
    @Story("Отправка смс в ВИМИС")
    @Test
    public void Access_1444_vmcl3Vimis0() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 3, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "", "", 0);
    }

    @DisplayName("Отправка смс с vmcl=4 в ВИМИС SMDToMinio = 0")
    @Story("Отправка смс в ВИМИС")
    @Test
    public void Access_1444_vmcl4Vimis0() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 4, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.cvdsms", "", "", 0);
    }

    @DisplayName("Отправка смс с vmcl=5 в ВИМИС SMDToMinio = 0")
    @Story("Отправка смс в ВИМИС")
    @Test
    public void Access_1444_vmcl5Vimis0() throws InterruptedException, SQLException, IOException {
        Access_1444Method("SMS/SMS2-id=42.xml", "42", 5, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.infectionsms", "", "",
                0);
    }

    @DisplayName("Отправка смс с vmcl=99 SMDToMinio = 0")
    @Story("Отправка смс в РЭМД")
    @Test
    public void Access_1444_vmcl990() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 99, 1, true, 3, 1, 9, 18, 1, 57, 21, "", "", "vimis.remd_sent_result", 0);
    }

    @DisplayName("Отправка смс с vmcl=99 pdf SMDToMinio = 0")
    @Story("Отправка смс в РЭМД")
    @Test
    public void Access_1444_vmcl990Pdf() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/id=101-vmcl=99.txt", "101", 99, 1, true, 3, 1, 57, 18, 1, 57, 21, "", "",
                "vimis.certificate_remd_sent_result", 0
        );
    }

    @DisplayName("Отправка смс с vmcl=1 в ВИМИС и РЭМД SMDToMinio = 0")
    @Story("Отправка смс в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl1VimisRemd0() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result", 0
        );

    }

    @DisplayName("Отправка смс с vmcl=2 в ВИМИС и РЭМД SMDToMinio = 0")
    @Story("Отправка смс в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl2VimisRemd0() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 0
        );
    }

    @DisplayName("Отправка смс с vmcl=3 в ВИМИС и РЭМД SMDToMinio = 0")
    @Story("Отправка смс в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl3VimisRemd0() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result", 0
        );

    }

    @DisplayName("Отправка смс с vmcl=4 в ВИМИС и РЭМД SMDToMinio = 0")
    @Story("Отправка смс в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl4VimisRemd0() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result", 0
        );
    }

    @DisplayName("Отправка смс с vmcl=5 в ВИМИС и РЭМД SMDToMinio = 0")
    @Story("Отправка смс в ВИМИС и РЭМД")
    @Test
    public void Access_1444_vmcl5VimisRemd0() throws InterruptedException, SQLException, IOException {
        Access_1444Method(
                "SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result", 0
        );
    }
}
