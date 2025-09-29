package api.REMD;

import Base.SQL;
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

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Формирование поля speciality в запросе к КРЭМД")
@Tag("api/smd")
@Tag("МИНИО")
@Tag("Поиск_api/rremd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Основные")
public class Access_1619Test extends BaseAPI {
    SQL sql;
    public String URLRemd;
    public String local_id;
    public String role_id;
    public String role_ref_version;
    public String last_name;
    public String first_name;
    public String middle_name;
    public String snils;
    public String position_id;
    public String position_ref_version;
    public String speciality_id;
    public String speciality_ref_version;
    public String department_oid;

    @Issue(value = "TEL-1619")
    @Link(name = "ТМС-1674", url = "https://team-1okm.testit.software/projects/5/tests/1674?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование поля speciality в запросе к КРЭМД vmcl = 1")
    @Description("Отправляем смс c подписью, проверяем добавление данных в таблицу vimis.eds_signers, проверяем запрос к КРЭМД")
    public void Access_1619Vmcl_1 () throws IOException, SQLException, InterruptedException {
        Access_1619Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @DisplayName("Формирование поля speciality в запросе к КРЭМД vmcl=2")
    @Test
    public void Access_1619Vmcl_2 () throws InterruptedException, SQLException, IOException {
        Access_1619Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );

    }

    @DisplayName("Формирование поля speciality в запросе к КРЭМД vmcl=3")
    @Test
    public void Access_1619Vmcl_3 () throws InterruptedException, SQLException, IOException {
        Access_1619Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );

    }

    @DisplayName("Формирование поля speciality в запросе к КРЭМД vmcl=4")
    @Test
    public void Access_1619Vmcl_4 () throws InterruptedException, SQLException, IOException {
        Access_1619Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );

    }

    @DisplayName("Формирование поля speciality в запросе к КРЭМД vmcl=5")
    @Test
    public void Access_1619Vmcl_5 () throws InterruptedException, SQLException, IOException {
        Access_1619Method(
                "SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result"
        );
    }

    @DisplayName("Формирование поля speciality в запросе к КРЭМД vmcl=99")
    @Test
    public void Access_1619Vmcl_99 () throws InterruptedException, SQLException, IOException {
        Access_1619Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");

    }

    public void Access_1619Method (String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String logs, String remd) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {

            System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);
            if (vmcl == 99) {
                sql.StartConnection(
                        "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    System.out.println(sql.value);
                }
            } else {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    System.out.println(sql.value);
                }
                CollbekVimis("" + xml.uuid + "", "1", "Проверка 1619", sms, vmcl);
                WaitStatusKremd(remd, "" + xml.uuid + "");
                CollbekKremd("" + xml.uuid + "", "success", "Проверка 1619", remd);
            }
            System.out.println("Проверяем добавление значений в таблице vimis.eds_signers для 1 подписи");
            sql.StartConnection("Select * from vimis.eds_signers ORDER BY id DESC limit 1 offset 1;");
            while (sql.resultSet.next()) {
                local_id = sql.resultSet.getString("local_id");
                role_id = sql.resultSet.getString("role_id");
                role_ref_version = sql.resultSet.getString("role_ref_version");
                last_name = sql.resultSet.getString("last_name");
                first_name = sql.resultSet.getString("first_name");
                middle_name = sql.resultSet.getString("middle_name");
                snils = sql.resultSet.getString("snils");
                position_id = sql.resultSet.getString("position_id");
                position_ref_version = sql.resultSet.getString("position_ref_version");
                speciality_id = sql.resultSet.getString("speciality_id");
                speciality_ref_version = sql.resultSet.getString("speciality_ref_version");
                department_oid = sql.resultSet.getString("department_oid");
            }
            Thread.sleep(1500);
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .param("LocalUid", xml.uuid)
                    .when()
                    .get(URLKREMD)
                    .body()
                    .jsonPath();

            sql.StartConnection("SELECT * FROM dpc.signer_role WHERE code_role = 'DOCTOR';");
            String role_ref_versionSql = null;
            while (sql.resultSet.next()) {
                role_ref_versionSql = sql.resultSet.getString("version");
            }
            Assertions.assertEquals(
                    response.get("result[0]." + DocumentDto + "personalSignatures[0].signer.localId"), local_id,
                    "local_id не совпадает"
            );
            Assertions.assertEquals(
                    response.get("result[0]." + DocumentDto + "personalSignatures[0].signer.role.code"), "DOCTOR",
                    "role не совпадает"
            );
            Assertions.assertEquals(role_ref_versionSql, "2.8", "role_ref_version не совпадает");
            Assertions.assertEquals(
                    response.get("result[0]." + DocumentDto + "personalSignatures[0].signer.surname"), last_name,
                    "last_name не совпадает"
            );
            Assertions.assertEquals(
                    response.get("result[0]." + DocumentDto + "personalSignatures[0].signer.name"), first_name,
                    "first_name не совпадает"
            );
            Assertions.assertEquals(
                    response.get("result[0]." + DocumentDto + "personalSignatures[0].signer.patrName"), middle_name,
                    "middle_name не совпадает"
            );
            Assertions.assertEquals(
                    response.get("result[0]." + DocumentDto + "personalSignatures[0].signer.snils"), snils,
                    "snils не совпадает"
            );
            Assertions.assertEquals(
                    response.get("result[0]." + DocumentDto + "personalSignatures[0].signer.position.code"),
                    position_id,
                    "position_id не совпадает"
            );
            Assertions.assertEquals(
                    response.get(
                            "result[0]." + DocumentDto + "personalSignatures[0].signer.position.codeSystemVersion"),
                    position_ref_version, "position_ref_version не совпадает"
            );
            Assertions.assertEquals(
                    response.get("result[0]." + DocumentDto + "personalSignatures[0].signer.speciality.code"),
                    speciality_id, "speciality_id не совпадает"
            );
            Assertions.assertEquals(
                    response.get(
                            "result[0]." + DocumentDto + "personalSignatures[0].signer.speciality.codeSystemVersion"),
                    speciality_ref_version, "speciality_ref_version не совпадает"
            );
            Assertions.assertEquals(
                    response.get("result[0]." + DocumentDto + "personalSignatures[0].signer.department.localId.code"),
                    department_oid, "department_oid не совпадает"
            );
        }
    }
}