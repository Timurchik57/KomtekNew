package api;

import Base.*;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Получение статуса из ФРЭМД")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
public class Access_871Test extends BaseAPI {
    XML xml;
    SQL sql;
    public Integer value;
    public String URLKremd;

    @Issue(value = "TEL-871")
    @Link(name = "ТМС-1273", url = "https://team-1okm.testit.software/projects/5/tests/1273?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Получение статуса из ФРЭМД vmcl=1")
    @Description("Замена одинаковых документов в бд, с присвоением новой версии документа")
    public void GettingStatusFRAMD_1() throws IOException, SQLException, InterruptedException {
        GettingStatusFRAMDMethod(
                "SMS/id=33-vmcl=1.xml", "33", 1, 3, "vimis.sms", "vimis.remd_onko_sent_result", "vimis.documentlogs", 6,
                4, 18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Получение статуса из ФРЭМД vmcl=2")
    public void GettingStatusFRAMD_2() throws IOException, SQLException, InterruptedException {
        GettingStatusFRAMDMethod(
                "SMS/id=33-vmcl=1.xml", "33", 2, 3, "vimis.preventionsms", "vimis.remd_prevention_sent_result",
                "vimis.preventionlogs", 6, 4, 18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Получение статуса из ФРЭМД vmcl=3")
    public void GettingStatusFRAMD_3() throws IOException, SQLException, InterruptedException {
        GettingStatusFRAMDMethod(
                "SMS/id=33-vmcl=1.xml", "33", 3, 2, "vimis.akineosms", "vimis.remd_akineo_sent_result",
                "vimis.akineologs", 6, 4, 18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Получение статуса из ФРЭМД vmcl=4")
    public void GettingStatusFRAMD_4() throws IOException, SQLException, InterruptedException {
        GettingStatusFRAMDMethod(
                "SMS/id=33-vmcl=1.xml", "33", 4, 2, "vimis.cvdsms", "vimis.remd_cvd_sent_result", "vimis.cvdlogs", 6, 4,
                18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Получение статуса из ФРЭМД vmcl=99")
    public void GettingStatusFRAMD_99() throws IOException, SQLException, InterruptedException {
        GettingStatusFRAMDMethod(
                "SMS/id=33-vmcl=1.xml", "33", 99, 1, "vimis.cvdsms", "vimis.remd_sent_result", "vimis.cvdlogs", 6, 4,
                18, 1, 57, 21
        );
    }

    public void GettingStatusFRAMDMethod(String FileName, String Doctype, Integer Vmlc, Integer docTypeVersion, String sms, String remd, String logs,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, SQLException, InterruptedException {
        sql = new SQL();
        xml = new XML();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {

            System.out.println("Отправляем смс с id = 33 и vmcl=" + Vmlc + "");
            xml.ApiSmd(
                    FileName, Doctype, Vmlc, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            if (Vmlc == 99) {
                sql.StartConnection(
                        "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value = Integer.valueOf(sql.resultSet.getString("id"));
                    System.out.println(value);
                }
            } else {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    value = Integer.valueOf(sql.resultSet.getString("id"));
                    System.out.println(value);
                }
                sql.UpdateConnection(
                        "update " + sms + " set msg_id = '" + UUID.randomUUID() + "', request_id = '" + UUID.randomUUID() + "' where id = '" + value + "';");
                sql.UpdateConnection(
                        "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + value + "', '" + UUID.randomUUID() + "')");
                sql.UpdateConnection(
                        "insert into " + remd + " (sms_id, local_uid, status, created_datetime, fremd_status) values (" + value + ", '" + xml.uuid + "', 'success', '" + Date + " 00:00:00.888 +0500', NULL);");
            }
            if (KingNumber == 1) {
                URLKremd = "http://192.168.2.126:1108/kremd/callback";
            }
            if (KingNumber == 2) {
                URLKremd = "http://192.168.2.126:1131/kremd/callback";
            }
            if (KingNumber == 4) {
                URLKremd = "http://212.96.206.70:1109/kremd/callback";
            }
            String value1 = String.valueOf(given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body("{\n" +
                            "  \"id\": \"" + xml.uuid + "\",\n" +
                            "  \"emdrId\": \"проверка заявки 871\",\n" +
                            "  \"status\": \"success\",\n" +
                            "  \"registrationDateTime\": \"2022-08-18\",\n" +
                            "  \"errors\": [\n" +
                            "    {\n" +
                            "      \"code\": \"string\",\n" +
                            "      \"message\": \"string\"\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}")
                    .post(URLKremd)
                    .prettyPeek()
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("id"));
            Assertions.assertEquals(value1, "" + xml.uuid + "");
            if (Vmlc == 99) {
                sql.StartConnection("Select * from " + remd + " where id = " + value + "");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("fremd_status");
                    System.out.println(value);
                    Assertions.assertEquals(sql.value, "1", "fremd_status не сменился на 1");
                }
            } else {
                sql.StartConnection("Select * from " + remd + " where sms_id = " + value + "");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("fremd_status");
                    System.out.println(value);
                    Assertions.assertEquals(sql.value, "1", "fremd_status не сменился на 1");
                }
            }
        }
    }
}
