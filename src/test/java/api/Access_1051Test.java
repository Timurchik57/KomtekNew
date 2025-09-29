package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import api.Before.Authorization;
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
@Feature("Проверка AnatomicalAreas  в методе /api/diagnostic/")
@Tag("Дополнительные_параметры")
@Tag("Api_Направление_на_диагностику")
@Tag("/api/diagnostic")
public class Access_1051Test extends BaseAPI {
    public String TargetMedicalOid;
    public String ResearchCode;

    @Issue(value = "TEL-1051")
    @Issue(value = "TEL-3856")
    @Link(name = "ТМС-1359", url = "https://team-1okm.testit.software/projects/5/tests/1359?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка AnatomicalAreas  в методе /api/diagnostic/ с Access_1051Method = 5")
    @Description("Используем метод создания направления с Access_1051Method = 5")
    public void Access_1051_5() throws IOException, InterruptedException, SQLException {
        Access_1051Method("5");
    }

    @Test
    @DisplayName("Проверка AnatomicalAreas  в методе /api/diagnostic/ с Access_1051Method = 1084")
    public void Access_1051_1084() throws IOException, InterruptedException, SQLException {
        Access_1051Method("1084");
    }

    @Test
    @DisplayName("Проверка AnatomicalAreas  в методе /api/diagnostic/ с Access_1051Method = 0")
    public void Access_1051_0() throws IOException, InterruptedException, SQLException {
        Access_1051Method("0");
    }

    @Test
    @DisplayName("Проверка AnatomicalAreas  в методе /api/diagnostic/ с Access_1051Method = 900001")
    public void Access_1051_900001() throws IOException, InterruptedException, SQLException {
        Access_1051Method("900001");
    }

    @Step("Метод создания направления на диагностику /api/diagnostic")
    public void Access_1051Method(String AnatomicalAreas) throws IOException, InterruptedException, SQLException {
        TargetMedicalOid = "1.2.643.5.1.13.13.12.2.86.8947";
        ResearchCode = "A06.28.009.002";

        Token = TokenInit();
        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .body("{\n" +
                        "    \"TargetMedicalOid\": \"" + TargetMedicalOid + "\",\n" +
                        "    \"DateDirection\": \"2021-12-28 08:30:52\",\n" +
                        "    \"ResearchCode\": \"" + ResearchCode + "\",\n" +
                        "    \"DoctorInformation\": {\n" +
                        "        \"DoctorSnils\": \""+Snils+"\",\n" +
                        "        \"DepartOid\": {\n" +
                        "            \"Value\": \"1.2.643.5.1.13.13.12.2.92.9196.0.201104\",\n" +
                        "            \"Version\": \"2.106dfgfdg7\"\n" +
                        "        },\n" +
                        "        \"SubdivisionOid\": {\n" +
                        "            \"Value\": \"1.2.643.5.1.13.13.12.2.92.9196.0.201104.148872\",\n" +
                        "            \"Version\": \"2.1014\"\n" +
                        "        },\n" +
                        "        \"Position\": {\n" +
                        "            \"Value\": 11,\n" +
                        "            \"Version\": \"4.2\"\n" +
                        "        },\n" +
                        "        \"Speciality\": {\n" +
                        "            \"Version\": \"1\",\n" +
                        "            \"Value\": 290\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"PatientGuid\": \"" + PatientGuid + "\",\n" +
                        "    \"PatientId_MIS\": \"T12\",\n" +
                        "    \"AnatomicalAreas\": [\n" +
                        "        " + AnatomicalAreas + "\n" +
                        "    ],\n" +
                        "    \"DiagnosisCode\": \"D13.4\",\n" +
                        "    \"InformationHeadDepartment\": {\n" +
                        "        \"HeadDoctorSnils\": \"05283659275\",\n" +
                        "        \"LastName\": \"Петров\",\n" +
                        "        \"FirstName\": \"Сергей\",\n" +
                        "        \"MiddleName\": \"Иванович\"\n" +
                        "    },\n" +
                        "    \"PatientWeight\": 80,\n" +
                        "    \"EndDateDirection\": \"2021-12-30 08:30:52\"\n" +
                        "\n" +
                        "    \n" +
                        "}")
                .post(HostAddress + "/api/diagnostic")
                .prettyPeek()
                .body()
                .jsonPath();
        if (AnatomicalAreas == "0" || AnatomicalAreas == "900001") {
            Assertions.assertEquals(
                    response.get("ErrorMessage"),
                    "Неверно указанo AnatomicalAreas. " + AnatomicalAreas + " нет в справочнике",
                    "Направление не создалось"
            );
        } else {
            Assertions.assertEquals(response.get("IsSuccess"), true, "Направление не создалось");

            // Проверяем заявку 3856
            sql.StartConnection("SELECT * FROM telmed.cami_links order by id desc limit 1;");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("accession_number");
            }
            Assertions.assertEquals(response.get("Result.AccessionNumber"), sql.value, "AccessionNumber не добавился");
        }
        Thread.sleep(2000);
    }
}
