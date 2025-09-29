package api.REMD;

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

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Отправка смс с пациентом без снилс")
@Tag("api/smd")
@Tag("Поиск_api/rremd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("МИНИО")
public class Access_1313Test extends BaseAPI {
    public String URLRemd;

    @Issue(value = "TEL-1313")
    @Link(name = "ТМС-1478", url = "https://team-1okm.testit.software/projects/5/tests/1478?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка смс с пациентом без снилс для vmcl = 1")
    @Description("Отправляем смс с пациентом без снилс и проверяем что в РЭМД идёт отправка без снилс")
    public void Access_1313Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1313Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @Test
    @DisplayName("Отправка смс с пациентом без снилс для vmcl = 2")
    public void Access_1113Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1313Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );
    }

    @Test
    @DisplayName("Отправка смс с пациентом без снилс для vmcl = 3")
    public void Access_1113Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1313Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Отправка смс с пациентом без снилс для vmcl = 4")
    public void Access_1113Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1313Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );
    }

    @Test
    @DisplayName("Отправка смс с пациентом без снилс для vmcl = 5")
    public void Access_1113Vmcl_5() throws IOException, SQLException, InterruptedException {
        Access_1313Method(
                "SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result"
        );
    }

    @Test
    @DisplayName("Отправка смс с пациентом без снилс для vmcl = 99")
    public void Access_1113Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1313Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.remd_sent_result", "", "vimis.remd_sent_result");
    }

    public void Access_1313Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd
    ) throws IOException, InterruptedException, SQLException {
        PatientGuid = "c89bfaeb-732e-4d0e-a519-559743166fca";
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем запрос смс3 с vmlc=" + vmcl + "");
            xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1);

            sql.StartConnection(
                    "Select * from " + sms + " where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                System.out.println(sql.value);
            }

            if (vmcl != 99) {
                CollbekVimis("" + xml.uuid + "", "1", "Проверка 1313", sms, vmcl);
                WaitStatusKremd(remd, "" + xml.uuid + "");
                CollbekKremd("" + xml.uuid + "", "success", "Проверка 1313", remd);
            }

            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .param("LocalUid", xml.uuid)
                    .when()
                    .get(URLKREMD)
                    .body()
                    .jsonPath();
            Assertions.assertEquals(
                    response.get("result[0]." + DocumentDto + "localUid"), "" + xml.uuid + "", "localUid не совпадет");
            Assertions.assertNull(response.get("result[0]." + DocumentDto + "patient.snils"), "snils не null");
        }
    }
}
