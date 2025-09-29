package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Tag;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Возможность передавать в РЭМД документы pdf в base64")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Проверка_info")
public class Access_388Test extends BaseAPI {

    public String value388;

    @Issue(value = "TEL-388")
    @Link(name = "ТМС-11", url = "https://team-1okm.testit.software/projects/5/tests/11?isolatedSection=827ef86d-406f-4fec-a839-c7939a1a4497")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Возможность передавать в РЭМД документы pdf в base64 format = 2")
    @Description("Отправляем смс vmcl = 99 с ошибкой в xml. После отправить такой же документ, но для dpc.registered_emd.format = 1 и проверить, что ошибки нет")
    public void Access_388() throws IOException, SQLException, InterruptedException {
        Access_388TestMethod("SMS/388.xml", "15", 99, 1, true, 1, 6, 4, 18, 1, 57, 21, "vimis.remd_sent_result",
                "vimis.remdadditionalinfo", "2");
    }

    @Test
    @DisplayName("Возможность передавать в РЭМД документы pdf в base64 format = 1")
    public void Access_388_1() throws IOException, SQLException, InterruptedException {
        Access_388TestMethod("SMS/388.xml", "15", 99, 1, true, 1, 6, 4, 18, 1, 57, 21, "vimis.remd_sent_result",
                "vimis.remdadditionalinfo", "1");
    }

    @Step("Метод отправки смс и смены формата в таблице dpc.registered_emd")
    public void Access_388TestMethod(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc,
            Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String remd, String info, String format) throws IOException, InterruptedException, SQLException {

        if (format == "2") {
            xml.ReplacementWordInFile(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
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
            Assertions.assertEquals(
                    value,
                    "Ошибка при попытке распарсить поле document в xml: The 'family' start tag on line 85 position 22 does not match the end tag of 'name'. Line 90, position 19.");
        } else {
            sql.UpdateConnection("Update dpc.registered_emd set format = '1' where oid = '58';");

            System.out.println("Отправляем смс с Doctype = " + DocType + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            sql.StartConnection(
                    "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value388 = sql.resultSet.getString("id");
                System.out.println(value388);
            }
            sql.StartConnection(
                    "Select * from " + info + " where effectivetime > '" + Date + " 00:00:00.888 +0500' and smsid = '" + value388 + "';");
            while (sql.resultSet.next()) {
                String documentid = sql.resultSet.getString("documentid");
                Assertions.assertEquals(
                        documentid, "" + xml.uuid + "", "Значение в поле documentid не совпадает с local_uid");
            }
            sql.UpdateConnection("Update dpc.registered_emd set format = '2' where oid = '58';");
        }
    }
}
