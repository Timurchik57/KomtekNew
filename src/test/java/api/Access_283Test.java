package api;

import Base.*;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
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

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Добавление СЭМД с необязательным блоком replace")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("ФЛК")
@Tag("Версия_документа")
@Tag("Основные")
public class Access_283Test extends BaseAPI {

    public String value_283;
    public static String random;
    public String body;
    public String content;
    public String encodedString;
    public UUID uuid;
    public String EmdId = "1";

    @Issue(value = "TEL-283")
    @Link(name = "ТМС-1110", url = "https://team-1okm.testit.software/projects/5/tests/1110?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Добавление СЭМД с необязательным блоком replace id = 15")
    @Description("Отправляем смс только для vmcl=99, добавляем значение в vimis.remd_sent_result.emdid, после отправляем тот же СЭМД с указанием добавленного emdid и version")
    public void Access_283Id_15() throws IOException, SQLException, InterruptedException {
        Access_283Method("SMS/id=15-vmcl=99.xml", "15", 99, 1, true, 1, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Добавление СЭМД с необязательным блоком replace id = 3")
    public void Access_283Id_3() throws IOException, SQLException, InterruptedException {
        Access_283Method("SMS/SMS3.xml", "3", 99, 1, true, 1, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Добавление СЭМД с необязательным блоком replace id = 36")
    public void Access_283Id_36() throws IOException, SQLException, InterruptedException {
        Access_283Method("SMS/id=36-vmcl=99.xml", "36", 99, 1, true, 1, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Добавление СЭМД с необязательным блоком replace id = 32")
    public void Access_283Id_32() throws IOException, SQLException, InterruptedException {
        Access_283Method("SMS/id=32-vmcl=99.xml", "32", 99, 1, true, 1, 6, 4, 18, 1, 57, 21);
    }

    public String AddBody_283(String FileName, String DocType, Integer vmcl, Integer number, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException {

        xml.XmlStart(FileName, Departmen, MO, PatientGuid, NameMO, Snils, number);
        String textVmxl;
        if (vmcl != 99) {
            textVmxl = "{\n" +
                    "                    \"vmcl\": " + vmcl + ",\n" +
                    "                    \"triggerPoint\": 2 ,\n" +
                    "                    \"docTypeVersion\": " + docTypeVersion + "\n" +
                    "                    \n" +
                    "                }";
        } else {
            textVmxl = "{\n" +
                    "                    \"vmcl\": " + vmcl + "\n" +
                    "                }";
        }

        if (vmcl == 99) {
            body = "{\n" +
                    "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                    "    \"DocType\":\"" + DocType + "\",\n" +
                    "    \"DocContent\":\n" +
                    "    {\n" +
                    "        \"Document\":\"" + xml.encodedString + "\",\n" +
                    "        \"CheckSum\":" + xml.CheckSum + "\n" +
                    "        },\n" +
                    "        \"LocalUid\":\"" + xml.uuid + "\",\n" +
                    "        \"replace\" : { \n" +
                    "        \"emdId\": \"" + EmdId + "\", \n" +
                    "        \"replaced_version\":  1 \n" +
                    "        }, \n" +
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
                    "                            \"Position\":{\"$\":\"" + position + "\",\"@version\":\"" + xml.Position + "\"},\n" +
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
        return body;
    }

    public void Access_283Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException, SQLException {

        AddBody_283(FileName, DocType, vmcl, 1, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            random = String.valueOf((int) Math.floor(Math.random() * 1001));

            System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            JsonPath respons = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            sql.StartConnection(
                    "Select * from vimis.remd_sent_result where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value_283 = sql.resultSet.getString("id");
                System.out.println(value_283);
            }
            System.out.println("Добавляем смс рандомное значение emd_id = " + random + "");
            sql.UpdateConnection(
                    "update vimis.remd_sent_result set emd_id = " + random + " where local_uid = '" + xml.uuid + "';");
            System.out.println("Повторно отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
            EmdId = random;
            xml.ReplacementWordInFileBack(FileName);

            AddBody_283(FileName, DocType, vmcl, 2, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1);
            JsonPath respons2 = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .body()
                    .jsonPath();

            System.out.println("Проверяем, что добавилась новая запись с emd_id = " + random + " и  version = 1");
            sql.StartConnection(
                    "Select * from vimis.remd_sent_result where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                String emd_id = sql.resultSet.getString("emd_id");
                String version = sql.resultSet.getString("version");
                Assertions.assertEquals(emd_id, "" + random + "", "Не добавилась новая смс с emd_id = " + random + "");
                Assertions.assertEquals(version, "1", "Не добавилась новая смс с  version = 1");
            }
            EmdId = "9876879";
            xml.ReplacementWordInFileBack(FileName);

            AddBody_283(FileName, DocType, vmcl, 2, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1);
            JsonPath respons3 = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .body()
                    .jsonPath();
            Assertions.assertEquals(
                    respons3.get("result[0].errorMessage"),
                    "EmdId: Документ с указанный идентификатором \"emdId\" не может быть заменен, так как не передавался ранее через интеграционный шлюз"
            );
        }
    }
}
