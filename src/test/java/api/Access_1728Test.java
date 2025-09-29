package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
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
@Feature("Установка статус с коллбека КРЭМД")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("sent/resending")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
public class Access_1728Test extends BaseAPI {

    String Value;
    String URLCollback;
    String SQL;

    @Step("Метод отправки смс: {0}, vmcl = {3}, status = {14} и получение Коллбэка КРЭМД")
    public void Access_1728Method(String File, String DocType, Integer vmcl, Integer number, String sms, String log,
            String remd, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1,
            Integer position1, Integer speciality1,
            String StatusRemd) throws IOException, InterruptedException, SQLException {

        System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(File, DocType, vmcl, number, true, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);
        if (vmcl != 99) {
            sql.StartConnection("SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                Value = sql.resultSet.getString("id");
                System.out.println(Value);
                Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + sms + "");
            }
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1728", sms, vmcl);
        } else {
            sql.StartConnection("SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                Value = sql.resultSet.getString("id");
                Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + sms + "");
                System.out.println(Value);
            }
        }
        WaitStatusKremd(remd, "" + xml.uuid + "");

        if (KingNumber == 1) {
            URLCollback = "http://192.168.2.126:1108/kremd/callback";
        }
        if (KingNumber == 2) {
            URLCollback = "http://192.168.2.126:1131/kremd/callback";
        }
        if (KingNumber == 4) {
            URLCollback = "http://192.168.2.21:34154/kremd/callback";
        }
        String body;
        if (StatusRemd == "error") {
            body = "{\n" +
                    "  \"id\": \"" + xml.uuid + "\",\n" +
                    "  \"emdrId\": \"Проверка Уведомлений по РЭМД (1728)\",\n" +
                    "  \"status\": \"" + StatusRemd + "\",\n" +
                    "  \"registrationDateTime\": \"2023-04-25\",\n" +
                    "  \"errors\": [\n" +
                    "    {\n" +
                    "      \"code\": \"string\",\n" +
                    "      \"message\": \"Проверка Уведомлений по РЭМД (1728)\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
        } else {
            body = "{\n" +
                    "  \"id\": \"" + xml.uuid + "\",\n" +
                    "  \"emdrId\": \"Проверка Уведомлений по РЭМД (1728)\",\n" +
                    "  \"status\": \"" + StatusRemd + "\",\n" +
                    "  \"registrationDateTime\": \"2023-04-25\"\n" +
                    "}";
        }
        String value = String.valueOf(given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .post(URLCollback)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("id"));
        Assertions.assertEquals(value, "" + xml.uuid + "", "Не сменился статус");

        if (vmcl != 99) {
            Thread.sleep(1500);
            SQL = "SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and sms_id = '" + Value + "';";
        } else {
            SQL = "SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and id = '" + Value + "';";
        }

        sql.StartConnection(SQL);
        while (sql.resultSet.next()) {
            String status = sql.resultSet.getString("status");
            String fremd_status = sql.resultSet.getString("fremd_status");
            String error = sql.resultSet.getString("errors");
            if (StatusRemd == "success") {
                Assertions.assertEquals(fremd_status, "1", "В таблице " + remd + " fremd_status не равен 1");
                Assertions.assertNull(error, "В таблице " + remd + " error не равен null");
            } else if (StatusRemd == "error") {
                Assertions.assertEquals(fremd_status, "0", "В таблице " + remd + " fremd_status не равен 0");
                Assertions.assertEquals(error,
                        "[{\"Code\": \"string\", \"Message\": \"Проверка Уведомлений по РЭМД (1728)\"}]",
                        "В таблице " + remd + " error не равен - Проверка Уведомлений по РЭМД (1728)");
            } else {
                Assertions.assertNull(fremd_status, "В таблице " + remd + " fremd_status не равен null");
                Assertions.assertNull(error, "В таблице " + remd + " error не равен null");
            }
        }
    }

    @Issue(value = "TEL-1728")
    @Link(name = "ТМС-1766", url = "https://team-1okm.testit.software/projects/5/tests/1766?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправить СЭМД, изменить статус на sent/resending/success/error для remd_sent_result, проверить поля в бд status, error, fremd_status")
    @Test
    @DisplayName("Установка статус с коллбека КРЭМД vmcl = 99")
    public void Access_1728Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1728Method("SMS/SMS3.xml", "3", 99, 1, "", "", "vimis.remd_sent_result", 2, 1, 9, 18, 1, 57, 21,
                "success");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Access_1728Method("SMS/SMS3.xml", "3", 99, 2, "", "", "vimis.remd_sent_result", 2, 1, 9, 18, 1, 57, 21,
                "sent");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Access_1728Method("SMS/SMS3.xml", "3", 99, 2, "", "", "vimis.remd_sent_result", 2, 1, 9, 18, 1, 57, 21,
                "resending");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Access_1728Method("SMS/SMS3.xml", "3", 99, 2, "", "", "vimis.remd_sent_result", 2, 1, 9, 18, 1, 57, 21,
                "error");
    }

    @Test
    @DisplayName("Установка статус с коллбека КРЭМД vmcl = 1")
    public void Access_1728Vmcl_99ID_101() throws IOException, SQLException, InterruptedException {
        Access_1728Method("SMS/SMS3.xml", "3", 1, 1, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result",
                3, 1, 9, 18, 1,
                57, 21, "success");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Access_1728Method("SMS/SMS3.xml", "3", 1, 2, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result",
                3, 1, 9, 18, 1,
                57, 21, "sent");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Access_1728Method("SMS/SMS3.xml", "3", 1, 2, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result",
                3, 1, 9, 18, 1,
                57, 21, "resending");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Access_1728Method("SMS/SMS3.xml", "3", 1, 2, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result",
                3, 1, 9, 18, 1,
                57, 21, "error");
    }
}
