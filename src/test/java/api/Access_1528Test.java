package api;

import Base.BaseAPI;
import Base.TestListener;
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

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Поиск документов по параметрам doctype/identityDocumentSeries/identityDocumentNumber")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Поиск_api/smd")
@Tag("Проверка_info")
public class Access_1528Test extends BaseAPI {
    public String URL;
    public String document_series;
    public String document_number;

    @Issue(value = "TEL-1528")
    @Link(name = "ТМС-1567", url = "https://team-1okm.testit.software/projects/5/tests/1567?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка смс для vmcl = 1")
    @Description("Отправляем смс и проверяем с помощью метода api/smd с новыми параметрами")
    public void Access_1313Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1528Method(
                "SMS/id=33-vmcl=1.xml", "33", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "vimis.additionalinfo");
    }

    @Test
    @DisplayName("Отправка смс для vmcl = 2")
    public void Access_1113Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1528Method(
                "SMS/id=33-vmcl=1.xml", "33", 2, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms",
                "vimis.preventionadditionalinfo"
        );
    }

    @Test
    @DisplayName("Отправка смс для vmcl = 3")
    public void Access_1113Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1528Method(
                "SMS/id=33-vmcl=1.xml", "33", 3, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.akineosms",
                "vimis.akineoadditionalinfo"
        );
    }

    @Test
    @DisplayName("Отправка смс для vmcl = 4")
    public void Access_1113Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1528Method(
                "SMS/id=33-vmcl=1.xml", "33", 4, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.cvdsms",
                "vimis.cvdadditionalinfo"
        );
    }

    @Test
    @DisplayName("Отправка смс для vmcl = 5")
    public void Access_1113Vmcl_5() throws IOException, SQLException, InterruptedException {
        Access_1528Method(
                "SMS/id=33-vmcl=1.xml", "33", 5, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.infectionsms",
                "vimis.infectionadditionalinfo"
        );
    }

    @Test
    @DisplayName("Отправка смс для vmcl = 99")
    public void Access_1528Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1528Method(
                "SMS/id=33-vmcl=1.xml", "33", 99, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.remd_sent_result",
                "vimis.remdadditionalinfo"
        );
    }

    public void Access_1528Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String info
    ) throws IOException, InterruptedException, SQLException {
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем запрос смс3 с vmlc=" + vmcl + "");
            xml.ApiSmd(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            if (vmcl != 99) {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    System.out.println(sql.value);
                }
            } else {
                sql.StartConnection(
                        "Select * from " + sms + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    System.out.println(sql.value);
                }
            }
            sql.StartConnection("Select * from " + info + " where smsid = '" + sql.value + "';");
            while (sql.resultSet.next()) {
                document_series = sql.resultSet.getString("document_series");
                document_number = sql.resultSet.getString("document_number");
            }
            if (vmcl != 99) {
                URL = HostAddress + "/api/smd/?doctype=SMSV13&LocalUid=" + xml.uuid + "";
            }
            if (vmcl == 99) {
                URL = HostAddress + "/api/smd/?doctype=113&LocalUid=" + xml.uuid + "";
            }

            System.out.println("Проверка поиска документа по doctype");
            JsonPath response = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(URL)
                    .body()
                    .jsonPath();
            Assertions.assertEquals(response.get("result[0].localUid"), "" + xml.uuid + "", "localUid не совпадет");

            System.out.println("Проверка поиска документа по document_series");
            JsonPath response1 = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddress + "/api/smd/?identityDocumentSeries=" + document_series + "&LocalUid=" + xml.uuid + "")
                    .body()
                    .jsonPath();
            Assertions.assertEquals(response1.get("result[0].localUid"), "" + xml.uuid + "", "localUid не совпадет");

            System.out.println("Проверка поиска документа по document_number");
            JsonPath response2 = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddress + "/api/smd/?identityDocumentNumber =" + document_number + "&LocalUid=" + xml.uuid + "")
                    .body()
                    .jsonPath();
            Assertions.assertEquals(response2.get("result[0].localUid"), "" + xml.uuid + "", "localUid не совпадет");
        }
    }
}
