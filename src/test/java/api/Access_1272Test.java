package api;

import Base.*;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Несколько_направлений")
public class Access_1272Test extends BaseAPI {
    public String URLRemd;
    public String body;
    public String content;
    public String encodedString;
    public UUID uuid;
    public static String value_1272_vmcl_1;
    public static String value_1272_vmcl_2;
    public static String value_1272_vmcl_3;
    public static String value_1272_vmcl_4;
    public static String value_1272_vmcl_5;

    @Issue(value = "TEL-1272")
    @Link(name = "ТМС-1475", url = "https://team-1okm.testit.software/projects/5/tests/1475?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких vmcl = 1")
    @Description("Отправляем смс по нескольким направлениям и проверяем, что for_send = true только у первого стоящего vmcl, и отправка в РЭМД идёт только у него")
    public void Access_1272_Vmcl1 () throws IOException, SQLException, InterruptedException {
        Access_1272Method(1, 3, 2, 3, 3, 2, 4, 2, 5, 3);
        InsertSMS(1, value_1272_vmcl_1, "value_1272_vmcl_1");
    }

    @Test
    @DisplayName("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких vmcl = 2")
    public void Access_1272_Vmcl2 () throws IOException, SQLException, InterruptedException {
        Access_1272Method(2, 3, 1, 3, 3, 2, 4, 2, 5, 3);
        InsertSMS(2, value_1272_vmcl_2, "value_1272_vmcl_2");
    }

    @Test
    @DisplayName("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких vmcl = 3")
    public void Access_1272_Vmcl3 () throws IOException, SQLException, InterruptedException {
        Access_1272Method(3, 2, 1, 3, 2, 3, 4, 2, 5, 3);
        InsertSMS(3, value_1272_vmcl_3, "value_1272_vmcl_3");
    }

    @Test
    @DisplayName("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких vmcl = 4")
    public void Access_1272_Vmcl4 () throws IOException, SQLException, InterruptedException {
        Access_1272Method(4, 2, 1, 3, 2, 3, 3, 2, 5, 3);
        InsertSMS(4, value_1272_vmcl_4, "value_1272_vmcl_4");
    }

    @Test
    @DisplayName("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких vmcl = 5")
    public void Access_1272_Vmcl5 () throws IOException, SQLException, InterruptedException {
        Access_1272Method(5, 3, 1, 3, 2, 3, 3, 2, 4, 2);
        InsertSMS(5, value_1272_vmcl_5, "value_1272_vmcl_5");
    }

    @Step("Метод для отправки смс с другим телом запроса, где есть все 5 направлений")
    public void Access_1272Method (Integer v1, Integer d1, Integer v2, Integer d2, Integer v3, Integer d3, Integer v4, Integer d4, Integer v5, Integer d5) throws IOException, InterruptedException {
        XML xml = new XML();
        xml.XmlStart("SMS/SMS3.xml", Departmen, MO, PatientGuid, NameMO, Snils, 1);
        InputPropFile("local_uid_1272", String.valueOf(xml.uuid));

        body = "{\n" +
                "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                "    \"DocType\":\"3\",\n" +
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
                "            [\n" +
                "                {\n" +
                "                    \"vmcl\": " + v1 + ",\n" +
                "                    \"triggerPoint\": 2,\n" +
                "                    \"docTypeVersion\": " + d1 + "\n" +
                "                    \n" +
                "                },\n" +
                "                {\n" +
                "                    \"vmcl\": " + v2 + ",\n" +
                "                    \"triggerPoint\": 2,\n" +
                "                    \"docTypeVersion\": " + d2 + "\n" +
                "                    \n" +
                "                },\n" +
                "                {\n" +
                "                    \"vmcl\": " + v3 + ",\n" +
                "                    \"triggerPoint\": 2,\n" +
                "                    \"docTypeVersion\": " + d3 + "\n" +
                "                    \n" +
                "                },\n" +
                "                {\n" +
                "                    \"vmcl\": " + v4 + ",\n" +
                "                    \"triggerPoint\": 2,\n" +
                "                    \"docTypeVersion\": " + d4 + "\n" +
                "                    \n" +
                "                },\n" +
                "                {\n" +
                "                    \"vmcl\": " + v5 + ",\n" +
                "                    \"triggerPoint\": 2,\n" +
                "                    \"docTypeVersion\": " + d5 + "\n" +
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
                "                            \"Role\":{\"$\":\"1\",\"@version\":\"2.4\"},\n" +
                "                            \"LastName\":\"Коситченков\",\n" +
                "                            \"FirstName\":\"Андрей\",\n" +
                "                            \"MiddleName\":\"Александрович\",\n" +
                "                            \"Snils\":\"18259202174\",\n" +
                "                            \"Position\":{\"$\":\"9\",\"@version\":\"" + Position + "\"},\n" +
                "                            \"Speciality\":{\"$\":18,\"@version\":\"" + Speciality + "\"},\n" +
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
                "                                    \"Role\":{\"$\":\"1\",\"@version\":\"2.4\"},\n" +
                "                                    \"LastName\":\"Стрекнев\",\n" +
                "                                    \"FirstName\":\"Денис\",\n" +
                "                                    \"MiddleName\":\"Юрьевич\",\n" +
                "                                    \"Snils\":\"18287265004\",\n" +
                "                                    \"Position\":{\"$\":\"57\",\"@version\":\"" + Position + "\"},\n" +
                "                                    \"Speciality\":{\"$\":21,\"@version\":\"" + Speciality + "\"},\n" +
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
        Api(HostAddress + "/api/smd", "post", null, null, body, 200, true);
    }

    @Step("Метод для проверки for_send и установки status = 1 в logs")
    public void InsertSMS (Integer vmcl, String value, String NameProp) throws SQLException, IOException {

        System.out.println("Проверяем смс3 с vmcl=" + vmcl + "");
        String sms = "";
        String logs = "";
        for (int i = 1; i < 6; i++) {
            if (i == 1) {
                sms = "vimis.sms";
                logs = "vimis.documentlogs";
            }
            if (i == 2) {
                sms = "vimis.preventionsms";
                logs = "vimis.preventionlogs";
            }
            if (i == 3) {
                sms = "vimis.akineosms";
                logs = "vimis.akineologs";
            }
            if (i == 4) {
                sms = "vimis.cvdsms";
                logs = "vimis.cvdlogs";
            }
            if (i == 5) {
                sms = "vimis.infectionsms";
                logs = "vimis.infectionlogs";
            }
            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + ReadPropFile("local_uid_1272") + "';");
            while (sql.resultSet.next()) {
                value = sql.resultSet.getString("id");
                sql.value = sql.resultSet.getString("for_send");
                System.out.println(value);
            }
            if (i == vmcl) {
                Assertions.assertEquals(sql.value, "t", "У первого vmcl статус должен быть true");
                InputPropFile(NameProp, value);
            } else {
                Assertions.assertEquals(sql.value, "f", "У vmcl кроме первого статус должен быть false");
            }
            System.out.println("Устанавливаем status = 1 в " + logs + "");
            CollbekVimis("" + ReadPropFile("local_uid_1272") + "", "1", "Проверка 1272", sms, vmcl);
        }
    }

    @Step("Метод для проверки, что в remd добавилась только нужная смс")
    public void AfterAccess_1272 (String sms, String value) throws SQLException, IOException {
        sql = new SQL();
        System.out.println("Проверяю в таблице " + sms + " с id = " + ReadPropFile(value) + "");
        sql.SQL("Select count(*) from " + sms + " where sms_id = " + ReadPropFile(value) + ";");
    }
}
