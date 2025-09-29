package api.RegionalDocs;

import Base.SQL;
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
@Feature("Отображение статусов sent/resending в методе api/smd")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("sent/resending")
@Tag("Поиск_api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Основные")
public class Access_1591Test extends BaseAPI {
    SQL sql;
    String Value;

    @Issue(value = "TEL-1591")
    @Issue(value = "TEL-1664")
    @Issue(value = "TEL-2161")
    @Link(name = "ТМС-1720", url = "https://team-1okm.testit.software/projects/5/tests/1720?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отображение статусов sent/resending в методе api/smd, vmcl=99")
    @Description("Отправить СЭМД, изменить статус на sent/resending для remd_sent_result, и на success для certificate_remd_sent_result. Проверить меотдом api/smd верное отображение данных")
    public void Access_1591Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1591Method("SMS/SMS3.xml", "3", 99, 1, "vimis.remd_sent_result", "", "vimis.remd_sent_result", 2, 1, 9, 18, 1, 57, 21, "sent");
    }

    @Test
    @DisplayName("Отображение статусов sent/resending в методе api/smd, vmcl=99, regional_document = true")
    public void Access_1591Vmcl_99ID_101() throws IOException, SQLException, InterruptedException {
        Access_1591Method(
                "SMS/id=101-vmcl=99.txt", "101", 99, 1, "vimis.certificate_remd_sent_result", "", "vimis.certificate_remd_sent_result", 2, 1, 57, 18, 1,
                57, 21, "success"
        );
    }

    @Test
    @DisplayName("Отображение статусов sent/resending в методе api/smd, vmcl=1")
    public void Access_1591Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1591Method(
                "SMS/SMS3.xml", "3", 1, 1, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result", 3, 1, 9,
                18, 1, 57, 21, "sent"
        );
    }

    @Test
    @DisplayName("Отображение статусов sent/resending в методе api/smd, vmcl = 2")
    public void Access_1591Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1591Method(
                "SMS/SMS3.xml", "3", 2, 1, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 1, 9, 18, 1, 57, 21, "sent"
        );
    }

    @Test
    @DisplayName("Отображение статусов sent/resending в методе api/smd, vmcl = 3")
    public void Access_1591Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1591Method(
                "SMS/SMS3.xml", "3", 3, 1, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result", 2, 1,
                9, 18, 1, 57, 21, "sent"
        );
    }

    @Test
    @DisplayName("Отображение статусов sent/resending в методе api/smd, vmcl = 4")
    public void Access_1591Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1591Method(
                "SMS/SMS3.xml", "3", 4, 1, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 1, 9, 18,
                1, 57, 21, "sent"
        );
    }

    @Test
    @DisplayName("Отображение статусов sent/resending в методе api/smd, vmcl = 5")
    public void Access_1591Vmcl_5() throws IOException, SQLException, InterruptedException {
        Access_1591Method(
                "SMS/SMS3.xml", "3", 5, 1, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result", 3, 1, 9, 18,
                1, 57, 21, "sent"
        );
    }

    public void Access_1591Method(String File, String DocType, Integer vmcl, Integer number, String sms, String log, String remd,
            Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1,
            Integer position1, Integer speciality1, String StatusRemd) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {

            System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
            xml.ApiSmd(File, DocType, vmcl, number, true, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1);

            sql.StartConnection(
                    "SELECT * FROM " + sms + "  where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                Value = sql.resultSet.getString("id");
                System.out.println(Value);
                Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + sms + "");
            }

            if (vmcl != 99) {
                CollbekVimis("" + xml.uuid + "", "1", "Проверка 1591", sms, vmcl);
            }
            WaitStatusKremd(remd, "" + xml.uuid + "");
            CollbekKremd("" + xml.uuid + "", StatusRemd, "Проверка 1591", remd);

            if (File == "SMS/id=101-vmcl=99.txt") {
                sql.UpdateConnection(
                        "Update " + sms + " set fremd_status = null where local_uid = '" + xml.uuid + "';");
            }

            if (vmcl != 99) {
                sql.StartConnection("SELECT * FROM " + log + "  where sms_id = " + Value + ";");
                while (sql.resultSet.next()) {
                    String Value1 = sql.resultSet.getString("sms_id");
                    String Value2 = sql.resultSet.getString("description");
                    String Value3 = sql.resultSet.getString("msg_id");
                    String status = sql.resultSet.getString("status");
                    Assertions.assertEquals(Value1, Value, "СМС не добавилось в таблицу " + log + "");
                    Assertions.assertEquals(
                            Value2, "Проверка 1591",
                            "СМС не добавилось в таблицу " + log + " с сообщением - Проверка 1591"
                    );
                    Assertions.assertEquals(
                            Value3, "" + uuuid + "",
                            "СМС не добавилось в таблицу " + log + " с msg_id - " + uuuid + ""
                    );
                    Assertions.assertEquals(status, "1", "СМС не добавилось в таблицу " + sms + " с status - 1");
                }
                sql.StartConnection("SELECT * FROM " + remd + "  where sms_id = " + Value + ";");
                while (sql.resultSet.next()) {
                    String status = sql.resultSet.getString("status");
                    if (StatusRemd == "Sent") {
                        Assertions.assertEquals(status, "sent", "СМС не добавилось в таблицу " + remd + "");
                    }
                    if (StatusRemd == "resending") {
                        Assertions.assertEquals(status, "resending", "СМС не добавилось в таблицу " + remd + "");
                    }
                }

            } else {
                sql.StartConnection("SELECT * FROM " + sms + "  where id = " + Value + ";");
                while (sql.resultSet.next()) {
                    String local_uid = sql.resultSet.getString("local_uid");
                    String errors = sql.resultSet.getString("errors");
                    String fremd_status = sql.resultSet.getString("fremd_status");
                    String status = sql.resultSet.getString("status");
                    Assertions.assertEquals(local_uid, "" + xml.uuid + "", "СМС не добавилось в таблицу " + sms + "");
                    if (StatusRemd == "sent") {
                        Assertions.assertEquals(
                                status, "sent", "СМС не добавилось в таблицу " + sms + " с status - sent");
                    }
                    if (StatusRemd == "resending") {
                        Assertions.assertEquals(
                                status, "resending", "СМС не добавилось в таблицу " + sms + " с status - resending");
                    }
                    if (StatusRemd == "success") {
                        Assertions.assertEquals(
                                status, "success", "СМС не добавилось в таблицу " + sms + " с status - success");
                    }
                }
            }
            JsonPath response = given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(HostAddress + "/api/smd/?LocalUid=" + xml.uuid + "")
                    .then()
                    .statusCode(200)
                    .extract().jsonPath();
            if (StatusRemd == "success") {
                Assertions.assertEquals(
                        response.getString("result[0].statusREMD"), "5",
                        "Для статуса в БД success в методе api/smd statusREMD не равен 5"
                );
                if (vmcl != 99) {
                    Assertions.assertEquals(
                            response.getString("result[0].result.description"),
                            "Региональный документ успешно добавлен в интеграционный шлюз",
                            "Для статуса в БД success в методе api/smd description не равен - Региональный документ успешно добавлен в интеграционный шлюз"
                    );
                } else {
                    Assertions.assertEquals(
                            response.getString("result[0].errorsREMD"),
                            "[{\"Code\": \"string\", \"Message\": \"Проверка 1591\"}]",
                            "Для статуса в БД success в методе api/smd description не равен - [{\"Code\": \"string\", \"Message\": \"Проверка 1591\"}]"
                    );
                }
            } else {
                Assertions.assertEquals(
                        response.getString("result[0].statusREMD"), "6",
                        "Для статуса в БД sent в методе api/smd statusREMD не равен 6"
                );
                if (vmcl != 99) {
                    Assertions.assertEquals(
                            response.getString("result[0].result.description"),
                            "Документ передан на федеральный уровень ФРЭМД",
                            "Для статуса в БД sent в методе api/smd description не равен - Документ передан на федеральный уровень ФРЭМД"
                    );
                } else {
                    Assertions.assertEquals(
                            response.getString("result[0].errorsREMD"),
                            "[{\"Code\": \"string\", \"Message\": \"Проверка 1591\"}]",
                            "Для статуса в БД sent в методе api/smd description не равен - [{\"Code\": \"string\", \"Message\": \"Проверка 1591\"}]"
                    );
                }
                if (vmcl == 99) {
                    System.out.println("Меняем статус на resending");
                    sql.UpdateConnection(
                            "update " + sms + " set status = 'resending' where local_uid = '" + xml.uuid + "';");
                } else {
                    sql.UpdateConnection(
                            "update " + remd + " set status = 'resending' where local_uid = '" + xml.uuid + "';");
                }
                response = given()
                        .filter(new AllureRestAssured())
                        .header("Authorization", "Bearer " + Token)
                        .contentType(ContentType.JSON)
                        .when()
                        .get(HostAddress + "/api/smd/?LocalUid=" + xml.uuid + "")
                        .prettyPeek()
                        .then()
                        .statusCode(200)
                        .extract().jsonPath();
                Assertions.assertEquals(
                        response.getString("result[0].statusREMD"), "7",
                        "Для статуса в БД sent в методе api/smd statusREMD не равен 7"
                );
                if (vmcl != 99) {
                    Assertions.assertEquals(
                            response.getString("result[0].result.description"),
                            "Документ переотправлен на федеральный уровень РЭМД (повторная отправка в связи с ошибками вызванные сбоем в федеральном сервисе)",
                            "Для статуса в БД sent в методе api/smd description не равен - Документ переотправлен на федеральный уровень РЭМД (повторная отправка в связи с ошибками вызванные сбоем в федеральном сервисе)"
                    );
                } else {
                    Assertions.assertEquals(
                            response.getString("result[0].errorsREMD"),
                            "[{\"Code\": \"string\", \"Message\": \"Проверка 1591\"}]",
                            "Для статуса в БД sent в методе api/smd description не равен - [{\"Code\": \"string\", \"Message\": \"Проверка 1591\"}]"
                    );
                }
            }
        }
    }
}
