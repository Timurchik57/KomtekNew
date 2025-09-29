package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Тесты API")
@Feature("Отпрвка смс с параметром BasedOnLocalUids")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Дополнительные_параметры")
public class Access_2176Test extends BaseAPI {

    public String body;
    public String content;
    public String encodedString;
    public String resultLocalUid;

    @Issue(value = "TEL-2176")
    @Link(name = "ТМС-1879", url = "https://team-1okm.testit.software/projects/5/tests/1879?isolatedSection=65a30e47-043a-4dd0-af4f-165d06d17427")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка смс с BasedOnLocalUids vmcl = 1")
    @Description("Отправить смс с BasedOnLocalUids, проверить добавление значения в таблицы")
    public void Access_2176_1() throws IOException, SQLException, InterruptedException {
        Access_2176Method("SMS/SMS3.xml", "3", 1, 2, 3, 1, 9,
                18, 1, 57, 21, "vimis.sms", "vimis.onko_based_on_local_uids");
    }

    @Test
    @DisplayName("Отправка смс с BasedOnLocalUids vmcl = 2")
    public void Access_2176_2() throws IOException, SQLException, InterruptedException {
        Access_2176Method("SMS/SMS3.xml", "3", 2, 2, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms",
                "vimis.prevention_based_on_local_uids");
    }

    @Test
    @DisplayName("Отправка смс с BasedOnLocalUids vmcl = 3")
    public void Access_2176_3() throws IOException, SQLException, InterruptedException {
        Access_2176Method("SMS/SMS3.xml", "3", 3, 2, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms",
                "vimis.akineo_based_on_local_uids");
    }

    @Test
    @DisplayName("Отправка смс с BasedOnLocalUids vmcl = 4")
    public void Access_2176_4() throws IOException, SQLException, InterruptedException {
        Access_2176Method("SMS/SMS3.xml", "3", 4, 2, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms",
                "vimis.cvd_based_on_local_uids");
    }

    @Test
    @DisplayName("Отправка смс с BasedOnLocalUids vmcl = 5")
    public void Access_2176_5() throws IOException, SQLException, InterruptedException {
        Access_2176Method("SMS/SMS3.xml", "3", 5, 2, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms",
                "vimis.infection_based_on_local_uids");
    }

    @Test
    @DisplayName("Отправка смс с BasedOnLocalUids vmcl = 99")
    public void Access_2176_99() throws IOException, SQLException, InterruptedException {
        Access_2176Method("SMS/SMS3.xml", "3", 99, 2, 3, 1, 9, 18, 1, 57, 21, "vimis.remd_sent_result",
                "vimis.remd_based_on_local_uids");
    }

    @Step("Метод отправки смс с BasedOnLocalUids")
    public void Access_2176Method(String FileName, String DocType, Integer vmcl, Integer triggerPoint,
            Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1,
            Integer position1, Integer speciality1, String sms,
            String smsBased) throws InterruptedException, IOException, SQLException {

        xml.XmlStart(FileName, Departmen, MO, PatientGuid, NameMO, Snils, 1);
        String uuid2176 = String.valueOf(UUID.randomUUID());
        System.out.println(uuid2176);

        String textVmxl;
        if (vmcl != 99) {
            textVmxl = "{\n" +
                    "                    \"vmcl\": " + vmcl + ",\n" +
                    "                    \"triggerPoint\": " + triggerPoint + ",\n" +
                    "                    \"docTypeVersion\": " + docTypeVersion + "\n" +
                    "                    \n" +
                    "                }";
        } else {
            textVmxl = "{\n" +
                    "                    \"vmcl\": " + vmcl + "\n" +
                    "                }";
        }
        body = "{\n" +
                "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                "    \"DocType\":\"" + DocType + "\",\n" +
                "    \"BasedOnLocalUids\":[\"" + uuid2176 + "\"], \n" +
                "    \"DocContent\":\n" +
                "    {\n" +
                "        \"Document\":\"" + xml.encodedString + "\",\n" +
                "        \"CheckSum\":" + xml.CheckSum + "\n" +
                "        },\n" +
                "        \"LocalUid\":\"" + xml.uuid + "\",\n" +
                "        \"Payment\":1,\n" +
                "        \"ReasonForAbsenceIdcase\":\n" +
                "        {\n" +
                "            \"CodeSystemVersion\":\"1.1\",\n" +
                "            \"Code\":10,\n" +
                "            \"CodeSystem\":\"1.2.643.5.1.13.13.99.2.286\"\n" +
                "        },\n" +
                "            \"VMCL\":\n" +
                "            [" + textVmxl + "],\n" +
                "            \"OrgSignature\":\n" +
                "            {\n" +
                "                \"Data\":\"" + xml.Signatures + "\",\n" +
                "                \"CheckSum\":" + xml.CheckSumSign + "\n" +
                "                },\n" +
                "                \"PersonalSignatures\":\n" +
                "                [\n" +
                "                    {\n" +
                "                        \"Signer\":\n" +
                "                        {\n" +
                "                            \"LocalId\":\"0001510378\",\n" +
                "                            \"Role\":{\"$\":\"" + Role + "\",\"@version\":\"2.4\"},\n" +
                "                            \"LastName\":\"Коситченков\",\n" +
                "                            \"FirstName\":\"Андрей\",\n" +
                "                            \"MiddleName\":\"Александрович\",\n" +
                "                            \"Snils\":\"18259202174\",\n" +
                "                            \"Position\":{\"$\":\"" + position + "\",\"@version\":\"" + Position + "\"},\n" +
                "                            \"Speciality\":{\"$\":" + speciality + ",\"@version\":\"" + Speciality + "\"},\n" +
                "                            \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                "                        },\n" +
                "                            \"Signature\":\n" +
                "                            {\n" +
                "                                \"Data\":\"" + xml.Signatures + "\",\n" +
                "                                  \"CheckSum\":" + xml.CheckSumSign + "\n" +
                "                            },\n" +
                "                            \"Description\":\"Подпись сотрудника\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"Signer\":\n" +
                "                                {\n" +
                "                                    \"LocalId\":\"0003948083\",\n" +
                "                                    \"Role\":{\"$\":\"" + Role1 + "\",\"@version\":\"2.4\"},\n" +
                "                                    \"LastName\":\"Стрекнев\",\n" +
                "                                    \"FirstName\":\"Денис\",\n" +
                "                                    \"MiddleName\":\"Юрьевич\",\n" +
                "                                    \"Snils\":\"18287265004\",\n" +
                "                                    \"Position\":{\"$\":\"" + position1 + "\",\"@version\":\"" + Position + "\"},\n" +
                "                                    \"Speciality\":{\"$\":" + speciality1 + ",\"@version\":\"" + Speciality + "\"},\n" +
                "                                    \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                "                                },\n" +
                "                                    \"Signature\":\n" +
                "                                    {\n" +
                "                                        \"Data\":\"" + xml.Signatures + "\",\n" +
                "                                       \"CheckSum\":" + xml.CheckSumSign + "\n" +
                "                                        },\n" +
                "                                        \"Description\":\"Подпись сотрудника\"\n" +
                "                                    }\n" +
                "        ]\n" +
                "    }";

        JsonPath response = given()
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .post(HostAddress + "/api/smd")
                .prettyPeek()
                .body()
                .jsonPath();
        response.getString("result[0].message").contains("успешно опубликован");

        sql.StartConnection(
                "SELECT * FROM " + sms + "  where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            Assertions.assertNotEquals(sql.value, "NULL", "СМС не добавилось в таблицу vimis.akineosms");
            System.out.println(sql.value);
        }

        sql.StartConnection(
                "SELECT * FROM " + smsBased + "  where smsid = '" + sql.value + "';");
        while (sql.resultSet.next()) {
            resultLocalUid = sql.resultSet.getString("localuid");
        }
        Assertions.assertEquals(uuid2176, resultLocalUid, "BasedOnLocalUids не добавился в таблицу " + smsBased + "");
    }
}
