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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Передача универсального PatientGuid")
@Tag("api/smd")
@Tag("Поиск_api/rremd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Основные")
public class Access_1343Test extends BaseAPI {
    public String URLRemd;

    @Issue(value = "TEL-1343")
    @Link(name = "ТМС-1497", url = "https://team-1okm.testit.software/projects/5/tests/1497?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Передача универсального PatientGuid vmcl = 1")
    @Description("Отправляем смс с универсальным PatientGuid")
    public void Access_1343_15Vmcl_1 () throws IOException, SQLException, InterruptedException {
        Access_1343Method(
                "SMS/id=15-vmcl=99.xml", "15", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @Test
    @DisplayName("Передача универсального PatientGuid для vmcl = 2")
    public void Access_1113Vmcl_2 () throws IOException, SQLException, InterruptedException {
        Access_1343Method(
                "SMS/id=15-vmcl=99.xml", "15", 2, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms",
                "vimis.preventionlogs", "vimis.remd_prevention_sent_result"
        );
    }

    @Test
    @DisplayName("Передача универсального PatientGuid для vmcl = 3")
    public void Access_1113Vmcl_3 () throws IOException, SQLException, InterruptedException {
        Access_1343Method(
                "SMS/id=15-vmcl=99.xml", "15", 3, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.akineosms",
                "vimis.akineologs", "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Передача универсального PatientGuid для vmcl = 4")
    public void Access_1113Vmcl_4 () throws IOException, SQLException, InterruptedException {
        Access_1343Method(
                "SMS/id=15-vmcl=99.xml", "15", 4, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );
    }

    @Test
    @DisplayName("Передача универсального PatientGuid для vmcl = 5")
    public void Access_1113Vmcl_5 () throws IOException, SQLException, InterruptedException {
        Access_1343Method(
                "SMS/id=15-vmcl=99.xml", "15", 5, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.infectionsms",
                "vimis.infectionlogs",
                "vimis.remd_infection_sent_result"
        );
    }

    @Test
    @DisplayName("Передача универсального PatientGuid для vmcl = 99")
    public void Access_1113Vmcl_99 () throws IOException, SQLException, InterruptedException {
        Access_1343Method(
                "SMS/id=15-vmcl=99.xml", "15", 99, 1, true, 0, 6, 4, 18, 1, 57, 21, "vimis.remd_sent_result", "",
                "vimis.remd_sent_result");
    }

    @Test
    @DisplayName("Передача универсального PatientGuid vmcl = 1 для не предназначенной смс")
    public void Access_1343_3Vmcl_1 () throws IOException, SQLException, InterruptedException {
        Access_1343Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @Step("Метод отправки смс и проверки ответа запроса - /api/rremd")
    public void Access_1343Method (String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String logs, String remd) throws IOException, InterruptedException, SQLException {
        PatientGuid = "00000000-0000-0000-0000-000000000000";

        System.out.println("Отправляем запрос с Doctype = " + DocType + " и vmlc = " + vmcl + "");
        if (FileName == "SMS/SMS3.xml") {
            xml.ReplacementWordInFile(
                    FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1
            );
            Api(HostAddress + "/api/smd", "post", null, null, xml.body, 200, true);
            Assertions.assertEquals(
                    Response.get("result[0].errorMessage"),
                    "PatientGuid: PatientGuid не может быть универсальным значением",
                    "Универсальный гуид отправился для не предназначенной смс"
            );
        } else {
            xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1);

            sql.StartConnection(
                    "Select * from " + sms + " where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                System.out.println(sql.value);
            }

            if (vmcl != 99) {
                CollbekVimis("" + xml.uuid + "", "1", "Проверка 1343", sms, vmcl);
                WaitStatusKremd(remd, "" + xml.uuid + "");
                CollbekKremd("" + xml.uuid + "", "success", "Проверка 1343", remd);
            }
            String param [] = {"LocalUid", String.valueOf(xml.uuid)};
            Api(URLKREMD, "get", null, null, "", 200, true);
            assertThat(Response.get("result[0].patient"), equalTo(null));
        }
    }
}
