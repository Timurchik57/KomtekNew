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
@Feature("Поиск api/rremd?TransferId в запросе к КРЭМД")
@Tag("api/smd")
@Tag("МИНИО")
@Tag("Поиск_api/rremd")
public class Access_2549Test extends BaseAPI {

    public String URLRemd;
    public String local_uid;

    @Issue(value = "TEL-2549")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Поиск api/rremd?TransferId vmcl = 1")
    @Description("Отправляем смс, после ищём с помощью метода api/rremd?TransferId")
    public void Access_2549Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1619Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @DisplayName("Поиск api/rremd?TransferId vmcl=2")
    @Test
    public void Access_2549Vmcl_2() throws InterruptedException, SQLException, IOException {
        Access_1619Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );

    }

    @DisplayName("Поиск api/rremd?TransferId vmcl=3")
    @Test
    public void Access_2549Vmcl_3() throws InterruptedException, SQLException, IOException {
        Access_1619Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );

    }

    @DisplayName("Поиск api/rremd?TransferId vmcl=4")
    @Test
    public void Access_2549Vmcl_4() throws InterruptedException, SQLException, IOException {
        Access_1619Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );

    }

    @DisplayName("Поиск api/rremd?TransferId vmcl=5")
    @Test
    public void Access_2549Vmcl_5() throws InterruptedException, SQLException, IOException {
        Access_1619Method(
                "SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result"
        );
    }

    @DisplayName("Поиск api/rremd?TransferId vmcl=99")
    @Test
    public void Access_2549Vmcl_99() throws InterruptedException, SQLException, IOException {
        Access_1619Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.remd_sent_result", "", "vimis.remd_sent_result");
    }

    public void Access_1619Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd) throws IOException, InterruptedException, SQLException {
        sql = new SQL();

        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1);

            sql.StartConnection(
                    "Select * from " + sms + " where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("transfer_id");
                local_uid = sql.resultSet.getString("local_uid");
                System.out.println(sql.value);
            }

            if (vmcl != 99) {
                CollbekVimis("" + xml.uuid + "", "1", "Проверка 2549", sms, vmcl);
            }
            WaitStatusKremd(remd, "" + xml.uuid + "");

            Thread.sleep(1500);
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .param("TransferId", sql.value)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(URLKREMD)
                    .then()
                    .statusCode(200)
                    .extract().jsonPath();

            Assertions.assertEquals(response.get("result[0]." + DocumentDto + "localUid"), local_uid, "local_uid не совпадает");
        }
    }
}
