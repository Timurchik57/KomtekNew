package api.REMD;

import Base.SQL;
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

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Заполнение блока подразделения в запросе к РРЭМД")
@Tag("api/smd")
@Tag("Поиск_api/rremd")
@Tag("Проверка_БД")
@Tag("Проверка_info")
@Tag("МИНИО")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
public class Access_603Test extends BaseAPI {
    public String value603;
    public String department_oid;

    @Issue(value = "TEL-603")
    @Issue(value = "TEL-2633")
    @Link(name = "ТМС-1184", url = "https://team-1okm.testit.software/projects/5/tests/1184?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправка смс в РЭМД ")
    @DisplayName("Заполнение блока подразделения в запросе к РРЭМД vmcl=1")
    @Test
    public void Access_603Vmcl_1() throws InterruptedException, SQLException, IOException {
        Access_603Method("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21);
    }

    @DisplayName("Заполнение блока подразделения в запросе к РРЭМД vmcl=2")
    @Test
    public void Access_603Vmcl_2() throws InterruptedException, SQLException, IOException {
        Access_603Method("SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21);

    }

    @DisplayName("Заполнение блока подразделения в запросе к РРЭМД vmcl=3")
    @Test
    public void Access_603Vmcl_3() throws InterruptedException, SQLException, IOException {
        Access_603Method("SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21);

    }

    @DisplayName("Заполнение блока подразделения в запросе к РРЭМД vmcl=4")
    @Test
    public void Access_603Vmcl_4() throws InterruptedException, SQLException, IOException {
        Access_603Method("SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21);

    }

    @DisplayName("Заполнение блока подразделения в запросе к РРЭМД vmcl=5")
    @Test
    public void Access_603Vmcl_5() throws InterruptedException, SQLException, IOException {
        Access_603Method("SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21);
    }

    @DisplayName("Заполнение блока подразделения в запросе к РРЭМД vmcl=99")
    @Test
    public void Access_603Vmcl_99() throws InterruptedException, SQLException, IOException {
        Access_603Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21);
    }

    public void Access_603Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException, SQLException {

        TableVmcl(vmcl);

        System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                position1, speciality1);

        sql.StartConnection("Select * from " + smsBase + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            value603 = sql.resultSet.getString("id");
            System.out.println(value603);
        }

        if (vmcl != 99) {
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 603", smsBase, vmcl);
            WaitStatusKremd(remdBase, "" + xml.uuid + "");
        }

        /** Проверка по заявке (2633) */
        sql.StartConnection("Select * from " + infoBase + " where smsid = '" + value603 + "';");
        while (sql.resultSet.next()) {
            department_oid = sql.resultSet.getString("department_oid");
            System.out.println(department_oid);
        }

        System.out.println("Отправляем запрос в КРЭМД");
        JsonPath response = given()
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .log().uri()
                .param("LocalUid", xml.uuid)
                .when()
                .get(URLKREMD)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath();

        Assertions.assertEquals(
                response.getString("result[0]." + DocumentDto + "department.localId.code"),
                "" + department_oid + "",
                "Signer.Department в смс не совпадает с department.localId.code из КРЕМД"
        );
    }
}
