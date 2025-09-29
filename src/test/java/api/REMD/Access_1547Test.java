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
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Проверка метода meta-remd-document")
@Tag("api/smd")
@Tag("МИНИО")
@Tag("Поиск_api/rremd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
public class Access_1547Test extends BaseAPI {
    public String URLRemd;
    public String TransferId;

    @Issue(value = "TEL-1547")
    @Issue(value = "TEL-1473")
    @Link(name = "ТМС-1568", url = "https://team-1okm.testit.software/projects/5/tests/1568?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Link(name = "ТМС-1537", url = "https://team-1okm.testit.software/projects/5/tests/1537?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка метода meta-remd-document для vmcl = 1")
    @Description("Отправляем смс в ремд таблицы и проверяем с помощью метода meta-remd-document отображение блока personalSignatures")
    public void Access_1547Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1547Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @Test
    @DisplayName("Проверка метода meta-remd-document для vmcl = 2")
    public void Access_1547Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1547Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );
    }

    @Test
    @DisplayName("Проверка метода meta-remd-document для vmcl = 3")
    public void Access_1547Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1547Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Проверка метода meta-remd-document для vmcl = 4")
    public void Access_1547Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1547Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );
    }

    @Test
    @DisplayName("Проверка метода meta-remd-document для vmcl = 5")
    public void Access_1547Vmcl_5() throws IOException, SQLException, InterruptedException {
        Access_1547Method(
                "SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result"
        );
    }

    @Test
    @DisplayName("Проверка метода meta-remd-document для vmcl = 99")
    public void Access_1547Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1547Method("SMS/SMS3.xml", "3", 99, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.remd_sent_result", "", "vimis.remd_sent_result");
    }

    public void Access_1547Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd) throws IOException, InterruptedException, SQLException {
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {

            System.out.println("Отправляем запрос смс 3 с vmlc = " + vmcl + "");
            xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);

            sql.StartConnection(
                    "Select * from " + sms + " where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                TransferId = sql.resultSet.getString("transfer_id");
                System.out.println(sql.value);
            }

            if (vmcl != 99) {
                CollbekVimis("" + xml.uuid + "", "1", "Проверка 1547", sms, vmcl);
                WaitStatusKremd(remd, "" + xml.uuid + "");
                CollbekKremd("" + xml.uuid + "", "success", "Проверка 1547", remd);
            }

            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .param("transferId", TransferId)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddress + "/api/smd/meta-remd-document")
                    .body()
                    .jsonPath();
            Assertions.assertEquals(response.get("result[0].localUid"), "" + xml.uuid + "", "localUid не совпадет");
            Assertions.assertNotNull(response.get("result[0].personalSignatures"), "personalSignatures отстутствует");
            Assertions.assertEquals(response.get("result[0].documentNumber"), "" + ID + "", "Версия документа не совпадает");
            Assertions.assertNull(response.get("result[0].DocContent"),  "Версия документа должна быть null");
            Assertions.assertNull(response.get("result[0].PersonalSignarure"),  "PersonalSignarure должна быть null");
            Assertions.assertNull(response.get("result[0].OrgSignature"),  "OrgSignature должна быть null");
        }
    }
}
