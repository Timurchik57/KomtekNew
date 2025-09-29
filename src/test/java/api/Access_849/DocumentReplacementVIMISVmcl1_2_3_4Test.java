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
@Feature("Замена документа направляемого в ВИМИС")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Проверка_БД")
public class DocumentReplacementVIMISVmcl1_2_3_4Test extends BaseAPI {
    XML xml;
    SQL sql = new SQL();
    public Integer value;
    public Integer IdSms;

    public void DocumentReplacementVIMISMethod(
            String File, String DocType, Integer vmcl, String sms, String logs, Integer docTypeVersion, Integer Role,
            Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1
    ) throws IOException, SQLException, InterruptedException {
        xml = new XML();
        sql = new SQL();
        if (KingNumber != 3) {

            System.out.println("Отправляем смс с DocType = " + DocType + " и vmcl=" + vmcl + "");
            xml.ApiSmd(
                    File, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );

            /** Берём в БД значение id данной записи */
            sql.StartConnection(
                    "SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value = Integer.valueOf(sql.resultSet.getString("id"));
            }
            IdSms = value;
            System.out.println(IdSms);

            System.out.println("status = 1 в " + logs + "");
            sql.UpdateConnection(
                    "insert into " + logs + "(create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', " + IdSms + ", '" + UUID.randomUUID() + "');");
            sql.StartConnection("SELECT * FROM " + logs + "  where sms_id = '" + IdSms + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
                Assertions.assertEquals("1", sql.value);
            }

            System.out.println("Повторно отправляем смс с DocType = " + DocType + " и vmcl = " + vmcl + "");
            xml.ReplacementWordInFile(
                    File, DocType, vmcl, 0, false, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            String value = String.valueOf(given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(xml.body)
                    .post(HostAddress + "/api/smd")
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("result[0].errorMessage"));
            Assertions.assertEquals(value, "Документ с идентификатором localUid=" + xml.uuid + " уже принят ВИМИС");
            System.out.println("status = 1 в " + logs + " - OK \n");
            System.out.println("status = 0 в " + logs + "");
            sql.UpdateConnection("update " + logs + " set status = '0' where sms_id = " + IdSms + ";");
            sql.StartConnection("SELECT * FROM " + logs + "  where sms_id = '" + IdSms + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
                Assertions.assertEquals("0", sql.value);
            }

            System.out.println("Отправляем смс с vmcl=" + vmcl + "");
            xml.ApiSmd(
                    File, DocType, vmcl, 0, false, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            System.out.println("status = 0 в " + logs + " - OK \n");
        }
    }

    @Issue(value = "TEL-849")
    @Link(name = "ТМС-1243", url = "https://team-1okm.testit.software/projects/5/tests/1243?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Замена документа направляемого в ВИМИС, vmcl=1")
    @Description("Замена одинаковых документов в бд, с присвоением новой версии документа")
    public void DocumentReplacementVIMISVmcl_1() throws SQLException, IOException, InterruptedException {
        DocumentReplacementVIMISMethod(
                "SMS/SMS3-id=43.xml", "43", 1, "vimis.sms", "vimis.documentlogs", 3, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Замена документа направляемого в ВИМИС, vmcl=2")
    public void DocumentReplacementVIMISVmcl_2() throws SQLException, IOException, InterruptedException {
        DocumentReplacementVIMISMethod(
                "SMS/SMS3-id=43.xml", "43", 2, "vimis.preventionsms", "vimis.preventionlogs", 3, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Замена документа направляемого в ВИМИС, vmcl=3")
    public void DocumentReplacementVIMISVmcl_3() throws SQLException, IOException, InterruptedException {
        DocumentReplacementVIMISMethod(
                "SMS/SMS3-id=43.xml", "43", 3, "vimis.akineosms", "vimis.akineologs", 2, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Замена документа направляемого в ВИМИС, vmcl=4")
    public void DocumentReplacementVIMISVmcl_4() throws SQLException, IOException, InterruptedException {
        DocumentReplacementVIMISMethod(
                "SMS/SMS3-id=43.xml", "43", 4, "vimis.cvdsms", "vimis.cvdlogs", 2, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Замена документа направляемого в ВИМИС, vmcl=5")
    public void DocumentReplacementVIMISVmcl_5() throws SQLException, IOException, InterruptedException {
        DocumentReplacementVIMISMethod(
                "SMS/SMS3-id=43.xml", "43", 5, "vimis.infectionsms", "vimis.infectionlogs", 3, 1, 9, 18, 1, 57, 21);
    }
}
