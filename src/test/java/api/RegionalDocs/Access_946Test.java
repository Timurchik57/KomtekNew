package api.RegionalDocs;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Передача ПДФ справок в КРЭМД")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Поиск_api/rremd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("МИНИО")
@Tag("Региональный_документ")
@Tag("Основные")
public class Access_946Test extends BaseAPI {
    public String URLRemd;
    public String LocalUid;
    public String transfer_id;
    public String Doctype;
    public String SystemName;

    @Issue(value = "TEL-946")
    @Issue(value = "TEL-2921")
    @Link(name = "ТМС-1476", url = "https://team-1okm.testit.software/projects/5/tests/1476?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Передача ПДФ справок в КРЭМД id=101")
    @Description("Отправляем смс 101/102/103/104/105 - проверяем добавление в таблицу vimis.certificate_remd_sent_result - проверяем тело отправки в РЭМД")
    public void Access_946ID_101() throws IOException, SQLException, InterruptedException {
        Access_946Method("SMS/id=101-vmcl=99.txt", "101", 1, 57, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Передача ПДФ справок в КРЭМД id=102")
    public void Access_946ID_102() throws IOException, SQLException, InterruptedException {
        Access_946Method("SMS/id=101-vmcl=99.txt", "102", 1, 57, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Передача ПДФ справок в КРЭМД id=103")
    public void Access_946ID_103() throws IOException, SQLException, InterruptedException {
        Access_946Method("SMS/id=101-vmcl=99.txt", "103", 1, 57, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Передача ПДФ справок в КРЭМД id=104")
    public void Access_946ID_104() throws IOException, SQLException, InterruptedException {
        Access_946Method("SMS/id=101-vmcl=99.txt", "104", 1, 57, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Передача ПДФ справок в КРЭМД id=105")
    public void Access_946ID_105() throws IOException, SQLException, InterruptedException {
        Access_946Method("SMS/id=101-vmcl=99.txt", "105", 1, 57, 18, 1, 57, 21);
    }

    public void Access_946Method(
            String FileName, String DocType, Integer Role, Integer position, Integer speciality, Integer Role1,
            Integer position1, Integer speciality1
    ) throws IOException, InterruptedException, SQLException {

        System.out.println("Проверка по заявке 946 с смс = " + DocType + "");
        xml.ApiSmd(FileName, DocType, 99, 1, true, 2, Role, position, speciality, Role1, position1, speciality1);

        System.out.println("Берём название смс для id = " + DocType + "");
        sql.StartConnection("SELECT * FROM dpc.emd_types_additional where id = '" + DocType + "';");
        while (sql.resultSet.next()) {
            SystemName = sql.resultSet.getString("name");
        }
        System.out.println("Проверяем добавление в таблицу vimis.certificate_remd_sent_result");
        sql.StartConnection(
                "Select * from vimis.certificate_remd_sent_result where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            LocalUid = sql.resultSet.getString("local_uid");
            Doctype = sql.resultSet.getString("doctype");
            transfer_id = sql.resultSet.getString("transfer_id");
            System.out.println(sql.value);
            Assertions.assertEquals(LocalUid, "" + xml.uuid + "", "LocalUid не совпадает");
            Assertions.assertEquals(Doctype, DocType, "Doctype не совпадает");
        }

        System.out.println("Проверяем тело отправки в РЭМД");
        JsonPath response = given()
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .param("LocalUid", xml.uuid)
                .when()
                .get(URLKREMD)
                .body()
                .jsonPath();

        assertThat(response.get("result[0]."+DocumentDto+"localUid"), equalTo("" + xml.uuid + ""));
        assertThat(response.get("result[0]."+DocumentDto+"kind.displayName"), equalTo("" + SystemName + ""));
        assertThat(response.get("result[0]."+DocumentDto+"kind.code"), equalTo("" + DocType + ""));
        assertThat(response.get("result[0]."+DocumentDto+"kind.codeSystem"), equalTo("remd0001"));
        sql.NotSQL(
                "Select count(*) from vimis.remd_sent_result  where  created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");

        System.out.println("Дополнительно проверяем запросом get api/smd 2921");
        Thread.sleep(1500);
        String[] param = {"localUid", "" + xml.uuid + ""};
        Api(HostAddress + "/api/smd", "get", param, null, "", 200, true);
        Assertions.assertEquals((Integer) Response.get("result[0].statusREMD"), 5, "statusREMD для регионального докумнта должен быть = 5");
        Assertions.assertEquals(Response.get("result[0].errorsREMD"), "Региональный документ успешно добавлен в интеграционный шлюз", "errorsREMD для регионального докумнта должен быть = Региональный документ успешно добавлен в интеграционный шлюз");
    }
}
