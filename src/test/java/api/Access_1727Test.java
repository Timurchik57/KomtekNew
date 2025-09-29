package api;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.StringUtils;
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
@Feature("Поиск смс по параметрам api/smd")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Поиск_api/smd")
@Tag("Проверка_info")
@Tag("Основные")
public class Access_1727Test extends BaseAPI {
    public String LocallUid;
    public UUID UuidEmd_id;
    public UUID UuidRequestId;
    public String TransferId;

    @Issue(value = "TEL-1727")
    @Issue(value = "TEL-1720")
    @Issue(value = "TEL-1713")
    @Issue(value = "TEL-1954")
    @Issue(value = "TEL-1954")
    @Issue(value = "TEL-3887")
    @Issue(value = "TEL-3284")
    @Link(name = "ТМС-1754", url = "https://team-1okm.testit.software/projects/5/tests/1754?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Link(name = "ТМС-1828", url = "https://team-1okm.testit.software/projects/5/tests/1872?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Вимис и Рэмд")
    @DisplayName("Поиск смс по параметрам api/smd vmcl = 1")
    @Description("Отправляем смс по необходимости добовляем нужные параметры в бд - осеществляем поиск смс по этому пареметру")
    public void Access_1727Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1727Method("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21);
    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=2")
    @Test
    @Story("Вимис и Рэмд")
    public void Access_1727Vmcl_2() throws InterruptedException, SQLException, IOException {
        Access_1727Method("SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21);
    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=3")
    @Test
    @Story("Вимис и Рэмд")
    public void Access_1727Vmcl_3() throws InterruptedException, SQLException, IOException {
        Access_1727Method("SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21);
    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=4")
    @Test
    @Story("Вимис и Рэмд")
    public void Access_1727Vmcl_4() throws InterruptedException, SQLException, IOException {
        Access_1727Method("SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21);
    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=5")
    @Test
    @Story("Вимис и Рэмд")
    public void Access_1727Vmcl_5() throws InterruptedException, SQLException, IOException {
        Access_1727Method("SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21);
    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=99")
    @Test
    @Story("Вимис и Рэмд")
    public void Access_1727Vmcl_99() throws InterruptedException, SQLException, IOException {
        Access_1727Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @Story("Вимис")
    @DisplayName("Поиск смс по параметрам api/smd vmcl = 1")
    public void Access_1727Vmcl_1V() throws IOException, SQLException, InterruptedException {
        Access_1727Method("SMS/SMS2-id=42.xml", "42", 1, 1, true, 3, 6, 4, 18, 1, 57, 21);
    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=2")
    @Test
    @Story("Вимис")
    public void Access_1727Vmcl_2V() throws InterruptedException, SQLException, IOException {
        Access_1727Method("SMS/SMS2-id=42.xml", "42", 2, 1, true, 3, 6, 4, 18, 1, 57, 21);
    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=3")
    @Test
    @Story("Вимис")
    public void Access_1727Vmcl_3V() throws InterruptedException, SQLException, IOException {
        Access_1727Method("SMS/SMS2-id=42.xml", "42", 3, 1, true, 2, 6, 4, 18, 1, 57, 21);
    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=4")
    @Test
    @Story("Вимис")
    public void Access_1727Vmcl_4V() throws InterruptedException, SQLException, IOException {
        Access_1727Method("SMS/SMS2-id=42.xml", "42", 4, 1, true, 2, 6, 4, 18, 1, 57, 21);
    }

    @DisplayName("Поиск смс по параметрам api/smd vmcl=5")
    @Test
    @Story("Вимис")
    public void Access_1727Vmcl_5V() throws InterruptedException, SQLException, IOException {
        Access_1727Method("SMS/SMS2-id=42.xml", "42", 5, 1, true, 3, 6, 4, 18, 1, 57, 21);
    }

    @Step("Отправляем запрос методом api/smd")
    public void ApiSmdMethod(String FileName, String request, String sms, String local_uid, String info, Integer vmcl) throws SQLException {
        String patientmname = null;
        JsonPath response = given()
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .get(HostAddress + "/api/smd/?" + request)
                .prettyPeek()
                .body()
                .jsonPath();

        Assertions.assertEquals(response.get("result[0].localUid"), LocallUid,
                "Смс не найдена по пареметру " + request + "");

        if (vmcl != 99) {
            Assertions.assertEquals(response.get("result[0].isSent"), Boolean.valueOf("false"),
                    "Нет параметра isSent (Заявка 1954)");
            Assertions.assertEquals(response.get("result[0].result.description"), "Проверка 1727",
                    "Нет параметра description (Заявка 1954)");
            Assertions.assertEquals(response.get("result[0].result.status"), Integer.valueOf("1"),
                    "Нет параметра status (Заявка 1954)");
        } else {
            Assertions.assertEquals(response.get("result[0].isSent"), Boolean.valueOf("true"),
                    "Нет параметра isSent (Заявка 1954)");
            Assertions.assertNull(response.get("result[0].result.description"),
                    "Для vmcl = " + vmcl + "  не должно быть параметра description (Заявка 1954)");
            Assertions.assertNull(response.get("result[0].result.status"),
                    "Для vmcl = " + vmcl + "  не должно быть параметра status (Заявка 1954)");
        }

        sql.StartConnection("Select * from " + sms + " where local_uid = '" + local_uid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        sql.StartConnection("Select * from " + info + " where smsid = " + sql.value + ";");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("effectivetime");
            patientmname = sql.resultSet.getString("patientmname");
        }
        String str = response.get("result[0].createDate");
        Assertions.assertEquals(StringUtils.substring(str, 0, 10), StringUtils.substring(sql.value, 0, 10),
                "Не совпадает createDate");

        // Проверяем заявку 3887
        if (FileName.equals("SMS/SMS3.xml")) {
            Assertions.assertEquals(patientmname,
                    getXml(FileName, "//recordTarget/patientRole/patient/name/Patronymic/text()"),
                    "Отчество должно сохраниться в patientmname");
        } else {
            Assertions.assertEquals(patientmname,
                    getXml(FileName, "//recordTarget/patientRole/patient/name/given[2]/text()"),
                    "Отчество должно сохраниться в patientmname");
        }

        // Проверяем заявку 3284
        sql.StartConnection("Select * from dpc.mis_sp_mu msm where oid = '" + MO + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("medicalidmu");
        }
        Assertions.assertEquals(response.getString("result[0].medicalIdMu"), sql.value, "medicalIdMu");
    }

    @Step("Отправляем смс с Doctype = {1} и устанавливаем в бд нужные значения")
    public void Access_1727Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        TableVmcl(vmcl);

        System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);
        LocallUid = String.valueOf(xml.uuid);

        if (vmcl == 99) {
            sql.StartConnection("Select * from " + remdBase + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                TransferId = sql.resultSet.getString("transfer_id");
                System.out.println(sql.value);
            }
            UuidEmd_id = UUID.randomUUID();
            sql.UpdateConnection("update " + remdBase + " set emd_id = '" + UuidEmd_id + "' where id = '" + sql.value + "';");

        } else {
            sql.StartConnection("Select * from " + smsBase + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                TransferId = sql.resultSet.getString("transfer_id");
                System.out.println(sql.value);
            }
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1727", smsBase, vmcl);
            if (FileName != "SMS/SMS2-id=42.xml") {
                WaitStatusKremd(remdBase, "" + xml.uuid + "");
                CollbekKremd("" + xml.uuid + "", "success", "Проверка 1727", remdBase);
                UuidEmd_id = UUID.randomUUID();
                sql.UpdateConnection(
                        "update " + remdBase + " set emd_id = '" + UuidEmd_id + "' where sms_id = '" + sql.value + "';");
            }
            UuidRequestId = UUID.randomUUID();
            sql.UpdateConnection("update " + smsBase + " set request_id = '" + UuidRequestId + "' where id = '" + sql.value + "';");
        }

        System.out.println("Ищем по localUid");
        ApiSmdMethod(FileName,"localUid=" + xml.uuid + "", smsBase, LocallUid, infoBase, vmcl);
        Thread.sleep(2500);

        if (FileName == "SMS/SMS3.xml") {
            System.out.println("Ищем по emdId");
            ApiSmdMethod(FileName,"emdId=" + UuidEmd_id + "", smsBase, LocallUid, infoBase, vmcl);
            Thread.sleep(2500);
        }

        if (vmcl != 99) {
            System.out.println("Ищем по requestId для заявки 1724");
            ApiSmdMethod(FileName,"requestId=" + UuidRequestId + "", smsBase, LocallUid, infoBase, vmcl);
            Thread.sleep(2500);
        }

        System.out.println("Ищем по TransferId");
        ApiSmdMethod(FileName,"TransferId=" + TransferId + "", smsBase, LocallUid, infoBase, vmcl);
        Thread.sleep(2500);
    }
}
