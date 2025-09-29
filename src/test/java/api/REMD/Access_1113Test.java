package api.REMD;

import Base.SQL;
import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Получение данных об смс переданных в КРЭМД с рандомным local_uid в remd")
@Tag("api/smd")
@Tag("Поиск_api/rremd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("МИНИО")
@Tag("Основные")
public class Access_1113Test extends BaseAPI {
    SQL sql;
    public String value1113;
    public String URLRemd;
    public String rundom;

    @Issue(value = "TEL-1113")
    @Link(name = "ТМС-1384", url = "https://team-1okm.testit.software/projects/5/tests/1384?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Получение данных об смс переданных в КРЭМД с рандомным local_uid в remd для смс с vmcl = 1 id = 3")
    @Description("Отправить смс в ВИМИС и РЭМД, добавить статус 1 в logs, Добавить данную смс в таблицу vimis.remd_onko_sent_result, vimis.remd_cvd_sent_result, vimis.remd_akineo_sent_result, vimis.remd_prevention_sent_result (в зависимости он указанного направления ) и указать другой local_uid. Отправить запрос api/rremd с другим local_uid и проверить, что в ответе отображается local_uid отправленной смс")
    public void Access_1113Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1113Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result"
        );
    }

    @Test
    @DisplayName("Получение данных об смс переданных в КРЭМД с рандомным local_uid в remd для смс с vmcl = 2 id = 3")
    public void Access_1113Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1113Method(
                "SMS/SMS3.xml", "3", 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result"
        );
    }

    @Test
    @DisplayName("Получение данных об смс переданных в КРЭМД с рандомным local_uid в remd для смс с vmcl = 3 id = 3")
    public void Access_1113Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1113Method(
                "SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result"
        );
    }

    @Test
    @DisplayName("Получение данных об смс переданных в КРЭМД с рандомным local_uid в remd для смс с vmcl = 4 id = 3")
    public void Access_1113Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1113Method(
                "SMS/SMS3.xml", "3", 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_cvd_sent_result"
        );
    }

    @Test
    @DisplayName("Получение данных об смс переданных в КРЭМД с рандомным local_uid в remd для смс с vmcl = 5 id = 3")
    public void Access_1113Vmcl_5() throws IOException, SQLException, InterruptedException {
        Access_1113Method(
                "SMS/SMS3.xml", "3", 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result"
        );
    }

    public void Access_1113Method(
            String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String sms, String logs, String remd
    ) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем запрос смс3 с vmlc=" + vmcl + "");
            xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                    position1, speciality1);

            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value1113 = sql.resultSet.getString("id");
                System.out.println(value1113);
            }
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1103", sms, vmcl);
            WaitStatusKremd(remd, "" + xml.uuid + "");
            rundom = String.valueOf(UUID.randomUUID());
            System.out.println(rundom);

            System.out.println("Меняем local_uid на другой в " + remd + "");
            sql.UpdateConnection(
                    "update " + remd + " set local_uid = '" + rundom + "' where sms_id = '" + value1113 + "';");

            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .param("LocalUid", rundom)
                    .when()
                    .get(URLKREMD)
                    .body()
                    .jsonPath();
            if (SmdToMinio == 0) {
                assertThat(response.get("result[0].localUid"), equalTo("" + xml.uuid + ""));
            } else {
                assertThat(response.get("result[0].documentDto.localUid"), equalTo("" + xml.uuid + ""));
            }
        }
    }
}
