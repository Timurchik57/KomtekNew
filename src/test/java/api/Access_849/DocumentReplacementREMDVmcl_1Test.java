package api.Access_849;

import Base.SQL;
import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import Base.XML;
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
@Feature("Замена документа направляемого в РЭМД, vmcl=1")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
@Tag("Проверка_БД")
public class DocumentReplacementREMDVmcl_1Test extends BaseAPI {
    XML xml;
    SQL sql;
    public Integer value;
    public Integer IdSms;

    public void DocumentReplacementREMDVmclMethod(String File, Integer vmcl, String sms, String logs, String remd, Integer docTypeVersion, Integer Role,
            Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, SQLException, InterruptedException {
        xml = new XML();
        sql = new SQL();

        if (KingNumber != 3) {

            System.out.println("Отправляем смс с id = 33 и vmcl=" + vmcl + "");
            xml.ApiSmd(File, "33", vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);

            /** Берём в БД значение id данной записи */
            sql.StartConnection(
                    "SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value = Integer.valueOf(sql.resultSet.getString("id"));
                Integer registered_emd_oid = Integer.valueOf(sql.resultSet.getString("registered_emd_oid"));
                Assertions.assertNotEquals("", registered_emd_oid);
                System.out.println(value);
            }

            System.out.println("статус = 1 в " + logs + "");
            sql.UpdateConnection(
                    "insert into " + logs + "(create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', " + value + ", '" + UUID.randomUUID() + "');");
            sql.StartConnection("SELECT * FROM " + logs + "  where sms_id = '" + value + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
                Assertions.assertEquals("1", sql.value);
            }

            /** У данной записи устанавливаем status = error в vimis.remd_onko_sent_result */
            System.out.println("status = error в " + remd + "");
            sql.UpdateConnection(
                    "insert into " + remd + "(sms_id, status, created_datetime, fremd_status, is_sent) values (" + value + ", 'error', '" + Date + " 00:00:00.888 +0500', NULL, true);");
            System.out.println("Повторно отправляем смс с id = 33 и vmcl=" + vmcl + "");
            xml.ApiSmd(
                    File, "33", vmcl, 0, false, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );

            /** Берём в БД значение id данной записи (увеличилось на 1) и проверяем, что версия документа увеличилась на 1 */
            IdSms = value + 1;
            sql.StartConnection(
                    "SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and id = '" + IdSms + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("version_doc");
                Assertions.assertEquals(sql.value, "2");
            }

            System.out.println("статус = 1 в " + logs + "");
            sql.UpdateConnection(
                    "insert into " + logs + "(create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', " + IdSms + ", '" + UUID.randomUUID() + "');");
            sql.StartConnection("SELECT * FROM " + logs + "  where sms_id = '" + IdSms + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
                Assertions.assertEquals("1", sql.value);
            }

            System.out.println("status = error в " + remd + "- ОК \n ");
            /**  status = success, fremd_status = 0 в vimis.remd_onko_sent_result */
            System.out.println("status = success, fremd_status = 0 в " + remd + "");
            sql.UpdateConnection(
                    "insert into " + remd + "(sms_id, status, created_datetime, fremd_status, is_sent) values (" + IdSms + ", 'success', '" + Date + " 00:00:00.888 +0500', 0, true);");

            System.out.println("Повторно отправляем смс с id = 33 и vmcl = " + vmcl + "");
            xml.ApiSmd(
                    File, "33", vmcl, 0, false, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );

            /** Берём в БД значение id данной записи (увеличилось на 1) и проверяем, что версия документа увеличилась на 1 */
            IdSms = value + 2;
            sql.StartConnection(
                    "SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and id = '" + IdSms + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("version_doc");
                Assertions.assertEquals(sql.value, "3");
            }
            System.out.println(IdSms);

            System.out.println("status = 1 в " + logs + "");
            sql.UpdateConnection(
                    "insert into " + logs + "(create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', " + IdSms + ", '" + UUID.randomUUID() + "');");
            sql.StartConnection("SELECT * FROM " + logs + "  where sms_id = '" + IdSms + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
                Assertions.assertEquals("1", sql.value);
            }
            System.out.println("status = success, fremd_status = 0 в " + remd + " - ОК \n");

            System.out.println("status = success, fremd_status = 1 в " + remd + "");
            sql.UpdateConnection(
                    "insert into " + remd + "(sms_id, status, created_datetime, fremd_status, is_sent) values (" + IdSms + ", 'success', '" + Date + " 00:00:00.888 +0500', 1, true);");

            System.out.println("Повторно отправляем смс с id = 33 и vmcl = " + vmcl + "");
            xml.ReplacementWordInFile(
                    File, "33", vmcl, 0, false, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            String value = String.valueOf(given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(xml.body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("result[0].errorMessage"));
            Assertions.assertEquals(value, "Документ с идентификатором localUid=" + xml.uuid + " уже принят ФРЭМД");

            System.out.println("status = success, fremd_status = 1 в " + remd + " - ОК \n");

            /** У данной записи устанавливаем status = success, fremd_status = null в vimis.remd_onko_sent_result */
            System.out.println("status = success, fremd_status = null в " + remd + "");
            sql.UpdateConnection(
                    "update " + remd + " set status = 'success', fremd_status = NULL where sms_id = " + IdSms + ";");

            System.out.println("Повторно отправляем смс с id = 33 и vmcl = " + vmcl + "");
            xml.ReplacementWordInFile(
                    File, "33", vmcl, 0, false, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            String value1 = String.valueOf(given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(xml.body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("result[0].errorMessage"));
            Assertions.assertEquals(value1, "Документ с идентификатором localUid=" + xml.uuid + " уже направлен в ФРЭМД");

            System.out.println("status = success, fremd_status = null в " + remd + " - ОК \n");
            /** У данной записи устанавливаем status = null, fremd_status = null в vimis.remd_onko_sent_result */
            System.out.println("status = null, fremd_status = null в " + remd + "");
            sql.UpdateConnection(
                    "update " + remd + " set status = NULL, fremd_status = NULL where sms_id = " + IdSms + ";");
            System.out.println("Повторно отправляем смс с id = 33 и vmcl = " + vmcl + "");
            xml.ReplacementWordInFile(
                    File, "33", vmcl, 0, false, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            String value2 = String.valueOf(given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(xml.body)
                    .post(HostAddress + "/api/smd")
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("result[0].errorMessage"));
            Assertions.assertEquals(
                    value2, "Документ с идентификатором localUid=" + xml.uuid + " уже направлен в РЭМД");
            System.out.println("status = null, fremd_status = null в " + remd + " - ОК \n ");
        }
    }

    @Issue(value = "TEL-849")
    @Link(name = "ТМС-1243", url = "https://team-1okm.testit.software/projects/5/tests/1243?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Замена документа направляемого в РЭМД, vmcl=1")
    @Description("Замена одинаковых документов в бд, с присвоением новой версии документа")
    public void DocumentReplacementREMDVmcl_1() throws IOException, SQLException, InterruptedException {
        DocumentReplacementREMDVmclMethod(
                "SMS/id=33-vmcl=1.xml", 1, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result", 3, 6, 4,
                18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Замена документа направляемого в РЭМД, vmcl=2")
    public void DocumentReplacementREMDVmcl_2() throws IOException, SQLException, InterruptedException {
        DocumentReplacementREMDVmclMethod(
                "SMS/id=33-vmcl=1.xml", 2, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 6, 4, 18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Замена документа направляемого в РЭМД, vmcl=3")
    public void DocumentReplacementREMDVmcl_3() throws IOException, SQLException, InterruptedException {
        DocumentReplacementREMDVmclMethod(
                "SMS/id=33-vmcl=1.xml", 3, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result", 2, 6,
                4, 18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Замена документа направляемого в РЭМД, vmcl=4")
    public void DocumentReplacementREMDVmcl_4() throws IOException, SQLException, InterruptedException {
        DocumentReplacementREMDVmclMethod(
                "SMS/id=33-vmcl=1.xml", 4, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 6, 4, 18,
                1, 57, 21
        );
    }

    @Test
    @DisplayName("Замена документа направляемого в РЭМД, vmcl=5")
    public void DocumentReplacementREMDVmcl_5() throws IOException, SQLException, InterruptedException {
        DocumentReplacementREMDVmclMethod(
                "SMS/id=33-vmcl=1.xml", 5, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result", 3, 6, 4, 18,
                1, 57, 21
        );
    }
}
