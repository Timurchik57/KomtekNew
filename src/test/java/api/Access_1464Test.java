package api;

import Base.BaseAPI;
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

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Поиск по статусу в метод запроса документа GET")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Поиск_api/smd/document")
public class Access_1464Test extends BaseAPI {

    @Issue(value = "TEL-1464")
    @Link(name = "ТМС-1540", url = "https://team-1okm.testit.software/projects/5/tests/1540?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @Description("Поиск по статусу в метод запроса документа GET /api/smd/document")
    @DisplayName("Поиск по статусу в метод запроса документа GET vmcl=1")
    @Test
    public void Access_1464Vmcl_1() throws InterruptedException, SQLException, IOException {
        Access_1464Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result", "1"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1464Method(
                "SMS/SMS3.xml", "3", 1, 2, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result", "0"
        );

    }

    @DisplayName("Поиск по статусу в метод запроса документа GET vmcl=2")
    @Test
    public void Access_1464Vmcl_2() throws InterruptedException, SQLException, IOException {
        Access_1464Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", "1"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1464Method(
                "SMS/SMS3.xml", "3", 2, 2, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", "0"
        );
    }

    @DisplayName("Поиск по статусу в метод запроса документа GET vmcl=3")
    @Test
    public void Access_1464Vmcl_3() throws InterruptedException, SQLException, IOException {
        Access_1464Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result", "1"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1464Method(
                "SMS/SMS3.xml", "3", 3, 2, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result", "0"
        );
    }

    @DisplayName("Поиск по статусу в метод запроса документа GET vmcl=4")
    @Test
    public void Access_1464Vmcl_4() throws InterruptedException, SQLException, IOException {
        Access_1464Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result", "1"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1464Method(
                "SMS/SMS3.xml", "3", 4, 2, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result", "0"
        );
    }

    @DisplayName("Поиск по статусу в метод запроса документа GET vmcl=5")
    @Test
    public void Access_1464Vmcl_5() throws InterruptedException, SQLException, IOException {
        Access_1464Method(
                "SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result", "1"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1464Method(
                "SMS/SMS3.xml", "3", 5, 2, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result", "0"
        );
    }

    @DisplayName("Поиск по статусу в метод запроса документа GET vmcl=99")
    @Test
    public void Access_1464Vmcl_99() throws InterruptedException, SQLException, IOException {
        Access_1464Method(
                "SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "", "", "vimis.remd_sent_result", "success");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1464Method(
                "SMS/SMS3.xml", "3", 99, 2, true, 2, 1, 9, 18, 1, 57, 21, "", "", "vimis.remd_sent_result", "error");
    }

    public void Access_1464Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd, String Status) throws IOException, InterruptedException, SQLException {
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {

            System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
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
                    .prettyPeek()
                    .body()
                    .jsonPath();
            String transferId = response.get("result[0].transferId");

            if (vmcl == 99) {
                CollbekKremd("" + xml.uuid + "", Status , "Проверка 1464", remd);
            } else {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    System.out.println(sql.value);
                }
                System.out.println("Устанавливаем status = " + Status + " в " + logs + "");
                CollbekVimis("" + xml.uuid + "", Status, "Проверка 1464", sms, vmcl);
                WaitStatusKremd(remd, "" + xml.uuid + "");
            }
            if (Status == "1" | Status == "success") {

                System.out.println("Отправляем запрос в КРЭМД");
                JsonPath respons = given()
                        .filter(new AllureRestAssured())
                        .header("Authorization", "Bearer " + Token)
                        .contentType(ContentType.JSON)
                        .when()
                        .get(HostAddress + "/api/smd/document?transferId=" + transferId + "&status=1")
                        .body()
                        .jsonPath();
                Assertions.assertEquals(respons.get("result[0].localUid"), "" + xml.uuid + "", "localUid не совпадает");
                Assertions.assertEquals(
                        respons.get("result[0].transferId"), "" + transferId + "", "transferId не совпадает");

                respons = given()
                        .filter(new AllureRestAssured())
                        .header("Authorization", "Bearer " + Token)
                        .contentType(ContentType.JSON)
                        .when()
                        .get(HostAddress + "/api/smd/document?transferId=" + transferId + "&status=0")
                        .body()
                        .jsonPath();
                Assertions.assertEquals(
                        respons.get("errorMessage"), "СМС с указанными параметрами не найдены",
                        "Нашёл смс с несуществующим статусом"
                );
            } else {
                System.out.println("Отправляем запрос в КРЭМД");
                JsonPath respons = given()
                        .filter(new AllureRestAssured())
                        .header("Authorization", "Bearer " + Token)
                        .contentType(ContentType.JSON)
                        .when()
                        .get(HostAddress + "/api/smd/document?transferId=" + transferId + "&status=0")
                        .body()
                        .jsonPath();
                Assertions.assertEquals(respons.get("result[0].localUid"), "" + xml.uuid + "", "localUid не совпадает");
                Assertions.assertEquals(
                        respons.get("result[0].transferId"), "" + transferId + "", "transferId не совпадает");

                respons = given()
                        .filter(new AllureRestAssured())
                        .header("Authorization", "Bearer " + Token)
                        .contentType(ContentType.JSON)
                        .when()
                        .get(HostAddress + "/api/smd/document?transferId=" + transferId + "&status=1")
                        .body()
                        .jsonPath();
                Assertions.assertEquals(
                        respons.get("errorMessage"), "СМС с указанными параметрами не найдены",
                        "Нашёл смс с несуществующим статусом"
                );
            }
        }
    }
}
