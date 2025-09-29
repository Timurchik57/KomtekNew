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
@Feature("Формирование запроса к КРЭМД")
@Tag("api/smd")
@Tag("Поиск_api/rremd")
@Tag("Проверка_БД")
@Tag("МИНИО")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
public class Access_1009Test extends BaseAPI {
    SQL sql;
    public String value_1009;

    @Issue(value = "TEL-1009")
    @Link(name = "ТМС-1348", url = "https://team-1okm.testit.software/projects/5/tests/1348?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование запроса к КРЭМД vmcl=1")
    @Description("Отправить смс в ВИМИС и РЭМД, добавить статус 1 в logs, Добавить данную смс в таблицу vimis.remd_onko_sent_result, vimis.remd_cvd_sent_result, vimis.remd_akineo_sent_result, vimis.remd_prevention_sent_result (в зависимости он указанного направления ) и указать local_uid отправленной смс. Отправить запрос api/rremd")
    public void Access_1009Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД vmcl=2")
    public void Access_1009Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД vmcl=3")
    public void Access_1009Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД vmcl=4")
    public void Access_1009Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД vmcl=5")
    public void Access_1009Vmcl_5() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД смс32 vmcl=99")
    public void Access_1009Id_32Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/id=32-vmcl=99.xml", "32", 99, 1, true, 2, 6, 4, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД смс36 vmcl=99")
    public void Access_1009Id_36Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/id=36-vmcl=99.xml", "36", 99, 1, true, 2, 6, 4, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД с id=34 vmcl=99")
    public void Access_1009Id_34Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMSV19-id=34.xml", "34", 99, 1, true, 2, 6, 4, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД id=34 vmcl=3")
    public void Access_1009Id_34Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/SMSV19-id=34.xml", "34", 3, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД id=35 vmcl=3")
    public void Access_1009Id_35Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/id=35.xml", "35", 3, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД с id=35 vmcl=99")
    public void Access_1009Id_35Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/id=35.xml", "35", 99, 1, true, 2, 6, 4, 18, 1, 57, 21, "", "", "vimis.remd_sent_result");
    }

    @Test
    @DisplayName("Формирование запроса к КРЭМД с id=15 vmcl=99")
    public void Access_1009Id_15Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1009VimisMethod(
                "SMS/id=15-vmcl=99.xml", "15", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    public void Access_1009VimisMethod(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String logs, String remd) throws SQLException, IOException, InterruptedException {

        sql = new SQL();

        System.out.println("Отправляем запрос смс " + DocType + " с vmlc=" + vmcl + "");
        xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                position1, speciality1);
        if (vmcl != 99) {
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value_1009 = sql.resultSet.getString("id");
                System.out.println(value_1009);
            }
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1009", sms, vmcl);
            WaitStatusKremd(remd, "" + xml.uuid + "");
            CollbekKremd("" + xml.uuid + "", "success", "Проверка 1009", remd);
        }

        JsonPath response = given()
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .param("LocalUid", xml.uuid)
                .when()
                .get(URLKREMD)
                .body()
                .jsonPath();
        if (vmcl == 99 | DocType == "34" | DocType == "35") {
            Assertions.assertEquals(response.getString("result[0]." + DocumentDto + "patient.localId"),
                    "" + PatientGuid.toLowerCase() + "");
        } else {
            Assertions.assertEquals(response.getString("result[0]." + DocumentDto + "patient.localId"),
                    "" + PatientGuid + "");
        }
        System.out.println("В localId отображается гуид пациента " + PatientGuid + " \n ");
    }
}
