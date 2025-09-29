package api;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
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
@Feature("Проверка подписей при передаче СЭМД в РЭМД со сменой статуса position_control")
@Tag("Проверка_БД")
@Tag("api/smd")
public class Access_1033Test extends BaseAPI {
    SQL sql;


    @Issue(value = "TEL-1033")
    @Link(name = "ТМС-1352", url = "https://team-1okm.testit.software/projects/5/tests/1352?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка подписей для SMSV2-(id=2)-vmcl=1")
    @Description("Отправляем смс со статусом position_control = не строгий/ не проверять. После отправляем со статусом = проверять")
    public void Signatures_1() throws IOException, SQLException, InterruptedException {
        Access_1033Method("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 1, 1, true, 3, 1, 1, 18, 1, 57, 21, "110");
    }

    @Test
    @DisplayName("Проверка подписей для SMSV2-(id=2)-vmcl=2")
    public void Signatures_2() throws IOException, SQLException, InterruptedException {
        Access_1033Method("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 2, 1, true, 3, 1, 1, 18, 1, 57, 21, "110");

    }

    @Test
    @DisplayName("Проверка подписей для SMSV2-(id=2)-vmcl=3")
    public void Signatures_3() throws IOException, SQLException, InterruptedException {
        Access_1033Method("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 3, 1, true, 2, 1, 1, 18, 1, 57, 21, "110");

    }

    @Test
    @DisplayName("Проверка подписей для SMSV2-(id=2)-vmcl=4")
    public void Signatures_4() throws IOException, SQLException, InterruptedException {
        Access_1033Method("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 4, 1, true, 3, 1, 1, 18, 1, 57, 21, "110");

    }

    @Test
    @DisplayName("Проверка подписей для SMSV2-(id=2)-vmcl=5")
    public void Signatures_5() throws IOException, SQLException, InterruptedException {
        Access_1033Method("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 5, 1, true, 3, 1, 1, 18, 1, 57, 21, "110");

    }

    public void Access_1033Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String doc_kind
    ) throws SQLException, IOException, InterruptedException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            sql.UpdateConnection("update dpc.signature_rules_remd set position_control = 'не проверять' where doc_kind = '" + doc_kind + "' and role = '" + Role + "';");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );

            System.out.println("Документ принимается с position_control = 'не проверять' \n ");
            sql.UpdateConnection(
                    "update dpc.signature_rules_remd set position_control = 'проверять' where doc_kind = '" + doc_kind + "' and role = '" + Role + "';");
            xml.ReplacementWordInFile(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(xml.body)
                    .post(HostAddress + "/api/smd")
                    .body()
                    .jsonPath();
            Assertions.assertEquals(
                    response.get("result[0].errorMessage"),
                    "Smd: В соответствии со справочниками 1.2.643.5.1.13.13.99.2.42 и 1.2.643.5.1.13.13.99.2.368 для документов вида " + doc_kind + " для роли " + Role + " не соответствует должность"
            );
            System.out.println("Выходит ошибка с position_control = 'проверять' \n ");
            sql.UpdateConnection(
                    "update dpc.signature_rules_remd set position_control = 'не проверять' where doc_kind = '" + doc_kind + "' and role = '" + Role + "';");
        }
    }
}
