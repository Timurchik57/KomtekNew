package api.REMD;

import Base.SQL;
import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import Base.XML;
import api.Before.Authorization;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Передача Association В СЭМД")
@Tag("api/smd")
@Tag("Поиск_api/rremd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("МИНИО")
public class Access_1103Test extends BaseAPI {
    SQL sql;
    public String Access_1103;
    public String AssociationTarget;
    public String AssociationType;
    public String body;
    public String content;
    public String encodedString;
    public UUID uuid;

    @Issue(value = "TEL-1103")
    @Issue(value = "TEL-1104")
    @Issue(value = "TEL-1099")
    @Link(name = "ТМС-1374", url = "https://team-1okm.testit.software/projects/5/tests/1374?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 99")
    @Description("Отправляем смс с блоком Association в теле запроса, после проверяем таблицу onko/cvd/akineo/prevention и remd_sent_result. Для смс в ВИМИС устанавливаем статус 1 в logs и добавляем запись в remd, после используем запрос api/rremd и проверяем передалось ли значение Association target и type")
    public void Access_1103Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1103Method(
                "SMS/id=15-vmcl=99.xml", "15", 99, 1, true, 1, 6, 4, 18, 1, 57, 21, "vimis.remd_sent_result", "vimis.remd_sent_result", "");
    }

    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 1")
    public void Access_1103Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1103Method(
                "SMS/id=15-vmcl=99.xml", "15", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms",
                "vimis.remd_onko_sent_result", "vimis.documentlogs"
        );
    }

    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 2")
    public void Access_1103Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1103Method(
                "SMS/id=15-vmcl=99.xml", "15", 2, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms",
                "vimis.remd_prevention_sent_result", "vimis.preventionlogs"
        );
    }

    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 3")
    public void Access_1103Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1103Method(
                "SMS/id=15-vmcl=99.xml", "15", 3, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.akineosms",
                "vimis.remd_akineo_sent_result", "vimis.akineologs"
        );
    }

    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 4")
    public void Access_1103Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1103Method(
                "SMS/id=15-vmcl=99.xml", "15", 4, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.cvdsms",
                "vimis.remd_cvd_sent_result", "vimis.cvdlogs"
        );
    }

    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 5")
    public void Access_1103Vmcl_5() throws IOException, SQLException, InterruptedException {
        Access_1103Method(
                "SMS/id=15-vmcl=99.xml", "15", 5, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.infectionsms",
                "vimis.remd_infection_sent_result", "vimis.infectionlogs"
        );
    }

    public void Access_1103Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String remd, String logs) throws IOException, InterruptedException, SQLException {

        sql = new SQL();
        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        Token = TokenInit();
        XML.Type = FileName;
        Thread.sleep(1000);

        ID = (int) Math.floor(timestamp.getTime() / 1000);
        SetID = (int) Math.floor(timestamp.getTime() / 1000) + 1;
        VN = (int) Math.floor(timestamp.getTime() / 1000) + 2;
        InputPropFile("value_ID", String.valueOf(ID));
        InputPropFile("value_SetID", String.valueOf(SetID));
        InputPropFile("value_VN", String.valueOf(VN));
        xml.ReplaceWord(FileName, "${iD}", String.valueOf(ID));
        xml.ReplaceWord(FileName, "${setId}", String.valueOf(SetID));
        xml.ReplaceWord(FileName, "${versionNumber}", String.valueOf(VN));
        xml.ReplaceWord(FileName, "${mo}", MO);
        xml.ReplaceWord(FileName, "${guid}", PatientGuid);
        xml.ReplaceWord(FileName, "${namemo}", NameMO);
        xml.ReplaceWord(FileName, "${snils}", Snils);
        content = new String(Files.readAllBytes(Paths.get(FileName)));
        encodedString = Base64.getEncoder().encodeToString(content.getBytes());

        /** Подсчёт чек Суммы для XML */
        String sum = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"base64String\": \"" + encodedString + "\"\n" +
                        "}")
                .when()
                .post("http://192.168.2.126:10117/CheckSum/CalculateCheckSum")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("checkSum");
        CheckSum = sum;

        /** Получение подписи */
        String Signat = given()
                .contentType(ContentType.JSON)
                .body("{\"requestId\": \"2\",\n" +
                        "            \"serialNumber\": \""+SerialNumberXml+"\",\n" +
                        "            \"message\": \"" + encodedString + "\",\n" +
                        "            \"isBase64\": true}")
                .when()
                .post(AddressSignature)
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("result.signedMessage");
        Signatures = Signat;

        /** Подсчёт чек Суммы для Подписи */
        String sum1 = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"base64String\": \"" + Signatures + "\"\n" +
                        "}")
                .when()
                .post("http://192.168.2.126:10117/CheckSum/CalculateCheckSum")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("checkSum");
        CheckSumSign = sum1;
        uuid = UUID.randomUUID();
        System.out.println(uuid);
        if (vmcl == 99) {
            body = "{\n" +
                    "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                    "    \"DocType\":\"" + DocType + "\",\n" +
                    "    \"DocContent\":\n" +
                    "    {\n" +
                    "        \"Document\":\"" + encodedString + "\",\n" +
                    "        \"CheckSum\":" + CheckSum + "\n" +
                    "        },\n" +
                    "        \"LocalUid\":\"" + uuid + "\",\n" +
                    "        \"association\" : [{ \n" +
                    "        \"target\": \"тест 1103\", \n" +
                    "        \"type\":  \"тест 1103\" \n" +
                    "        }], \n" +
                    "        \"Payment\":1,\n" +
                    "        \"ReasonForAbsenceIdcase\":\n" +
                    "        {\n" +
                    "            \"CodeSystemVersion\":\"1.1\",\n" +
                    "            \"Code\":10,\n" +
                    "            \"CodeSystem\":\"1.2.643.5.1.13.13.99.2.286\"\n" +
                    "        },\n" +
                    "            \"VMCL\":\n" +
                    "            [\n" +
                    "                {\n" +
                    "                    \"vmcl\": " + vmcl + "\n" +
                    "                    \n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"OrgSignature\":\n" +
                    "            {\n" +
                    "                \"Data\":\"" + Signatures + "\",\n" +
                    "                \"CheckSum\":" + CheckSumSign + "\n" +
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
                    "                                \"Data\":\"" + Signatures + "\",\n" +
                    "                                \"CheckSum\":" + CheckSumSign + "\n" +
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
                    "                                        \"Data\":\"" + Signatures + "\",\n" +
                    "                                        \"CheckSum\":" + CheckSumSign + "\n" +
                    "                                        },\n" +
                    "                                        \"Description\":\"Подпись сотрудника\"\n" +
                    "                                    }\n" +
                    "        ]\n" +
                    "    }";

        }
        if (vmcl != 99) {
            body = "{\n" +
                    "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                    "    \"DocType\":\"" + DocType + "\",\n" +
                    "    \"DocContent\":\n" +
                    "    {\n" +
                    "        \"Document\":\"" + encodedString + "\",\n" +
                    "        \"CheckSum\":" + CheckSum + "\n" +
                    "        },\n" +
                    "        \"LocalUid\":\"" + uuid + "\",\n" +
                    "        \"association\" : [{ \n" +
                    "        \"target\": \"тест 1103\", \n" +
                    "        \"type\":  \"тест 1103\" \n" +
                    "        }], \n" +
                    "        \"Payment\":1,\n" +
                    "        \"ReasonForAbsenceIdcase\":\n" +
                    "        {\n" +
                    "            \"CodeSystemVersion\":\"1.1\",\n" +
                    "            \"Code\":10,\n" +
                    "            \"CodeSystem\":\"1.2.643.5.1.13.13.99.2.286\"\n" +
                    "        },\n" +
                    "            \"VMCL\":\n" +
                    "            [\n" +
                    "                {\n" +
                    "                    \"vmcl\": " + vmcl + ",\n" +
                    "                    \"triggerPoint\": 2,\n" +
                    "                    \"docTypeVersion\": " + docTypeVersion + "\n" +
                    "                    \n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"OrgSignature\":\n" +
                    "            {\n" +
                    "                \"Data\":\"" + Signatures + "\",\n" +
                    "                \"CheckSum\":" + CheckSumSign + "\n" +
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
                    "                                \"Data\":\"" + Signatures + "\",\n" +
                    "                                  \"CheckSum\":" + CheckSumSign + "\n" +
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
                    "                                        \"Data\":\"" + Signatures + "\",\n" +
                    "                                       \"CheckSum\":" + CheckSumSign + "\n" +
                    "                                        },\n" +
                    "                                        \"Description\":\"Подпись сотрудника\"\n" +
                    "                                    }\n" +
                    "        ]\n" +
                    "    }";
        }
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {

            System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            JsonPath respons = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(body)
                    .post(HostAddress + "/api/smd")
                    .body()
                    .jsonPath();

            sql.StartConnection("Select * from " + sms + " where local_uid = '" + uuid + "';");
            while (sql.resultSet.next()) {
                Access_1103 = sql.resultSet.getString("id");
                AssociationTarget = sql.resultSet.getString("association_emdid");
                AssociationType = sql.resultSet.getString("association_type");
                System.out.println(Access_1103);
            }

            if (vmcl != 99) {
                CollbekVimis("" + uuid + "", "1", "Проверка 1103", sms, vmcl);
                WaitStatusKremd(remd, "" + uuid + "");
                CollbekKremd("" + uuid + "", "success", "Проверка 1103", remd);
            } else {
                WaitStatusKremd(remd, "" + uuid + "");
            }
            Thread.sleep(2500);

            JsonPath response = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .param("LocalUid", uuid)
                    .when()
                    .get(URLKREMD)
                    .body()
                    .jsonPath();
            if (SmdToMinio == 1) {
                assertThat(response.get("result[0].documentDto.associations[0].target"), equalTo("тест 1103"));
                assertThat(response.get("result[0].documentDto.associations[0].type"), equalTo("тест 1103"));
            } else {
                assertThat(response.get("result[0].associations[0].target"), equalTo("тест 1103"));
                assertThat(response.get("result[0].associations[0].type"), equalTo("тест 1103"));
            }
        }
    }
}
