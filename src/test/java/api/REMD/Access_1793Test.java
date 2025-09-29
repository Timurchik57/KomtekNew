package api.REMD;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import Base.XML;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Отправка в КРЭМД даты из Info или createDate")
@Tag("api/smd")
@Tag("МИНИО")
@Tag("Поиск_api/rremd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
public class Access_1793Test extends BaseAPI {

    public String body;
    public String content;
    public String encodedString;
    public String Time;

    @Order(1)
    @Issue(value = "TEL-1793")
    @Link(name = "ТМС-1789", url = "https://team-1okm.testit.software/projects/5/tests/1789?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправить СЭМД с effectiveTime/ без effectiveTime и проверить методом /api/rremd/, что берутся разные значения, но не передаётся null")
    @Test
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=1")
    public void Access_1793_1() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21, true);
    }

    @Test
    @Order(2)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=2")
    public void Access_1793_2() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21, true);
    }

    @Test
    @Order(3)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=3")
    public void Access_1793_3() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21, true);
    }

    @Test
    @Order(4)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=4")
    public void Access_1793_4() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21, true);
    }

    @Test
    @Order(5)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=5")
    public void Access_1793_5() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21, true);
    }

    @Test
    @Order(6)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Отправляем смс для vmcl=99")
    public void Access_1793_99() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 99, 2, 1, 9, 18, 1, 57, 21, true);
    }

    @Test
    @Order(7)
    @Story("Отправляем смс c <effectiveTime nullFlavor=\"NA\"/>")
    @DisplayName("Возвращаем время")
    public void Back_1() throws IOException, InterruptedException {
        xml.ReplaceWord("SMS/SMS3.xml", "<effectiveTime nullFlavor=\"NA\"/>",
                "<effectiveTime value=\"200201231600+0300\"/>");
    }

    @Order(8)
    @Test
    @Story("Отправляем смс c <effectiveTime value=\"200201231600+0300\"/>")
    @DisplayName("Отправляем смс для vmcl=1")
    public void Access_1793_1_f() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21, false);
    }

    @Test
    @Order(9)
    @Story("Отправляем смс c <effectiveTime value=\"200201231600+0300\"/>")
    @DisplayName("Отправляем смс для vmcl=2")
    public void Access_1793_2_f() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21, false);
    }

    @Test
    @Order(10)
    @Story("Отправляем смс c <effectiveTime value=\"200201231600+0300\"/>")
    @DisplayName("Отправляем смс для vmcl=3")
    public void Access_1793_3_f() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21, false);
    }

    @Test
    @Order(11)
    @Story("Отправляем смс c <effectiveTime value=\"200201231600+0300\"/>")
    @DisplayName("Отправляем смс для vmcl=4")
    public void Access_1793_4_f() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21, false);
    }

    @Test
    @Order(12)
    @Story("Отправляем смс c <effectiveTime value=\"200201231600+0300\"/>")
    @DisplayName("Отправляем смс для vmcl=5")
    public void Access_1793_5_f() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21, false);
    }

    @Test
    @Order(13)
    @Story("Отправляем смс c <effectiveTime value=\"200201231600+0300\"/>")
    @DisplayName("Отправляем смс для vmcl=99")
    public void Access_1793_99_f() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS3.xml", "3", 99, 2, 1, 9, 18, 1, 57, 21, false);
    }

    @Step("Отправляем смс = {1} с vmcl = {2}. Меняем значение effectiveTime ")
    public void Access_1786Method(String FileName, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, Boolean time) throws IOException, InterruptedException, SQLException {
        TableVmcl(vmcl);

        if (time == true) {
            xml.ReplaceWord(FileName, "<effectiveTime value=\"200201231600+0300\"/>",
                    "<effectiveTime nullFlavor=\"NA\"/>");
        }
        xml.XmlStart(FileName, Departmen, MO, PatientGuid, NameMO, Snils, 1);
        xml.BodyXml(DocType, vmcl, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);

        System.out.println("Отправляем смс " + DocType + " с vmcl = " + vmcl + "");
        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .body(xml.body)
                .post(HostAddress + "/api/smd")
                .body()
                .prettyPeek()
                .jsonPath();
        Thread.sleep(3000);

        if (vmcl != 99) {
            sql.StartConnection(
                    "SELECT * FROM " + smsBase + "  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                System.out.println(sql.value);
                Assertions.assertNotEquals(sql.value, "NULL", "СМС не добавилось в таблицу " + smsBase + "");
                Time = sql.resultSet.getString("create_date");
            }

            System.out.println("Отправляем колбек от ФВИМИС");
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1793", smsBase, vmcl);
            WaitStatusKremd(remdBase, "" + xml.uuid + "");

            System.out.println("Отправляем колбек от КРЭМД");
            CollbekKremd("" + xml.uuid + "", "success", "Проверка 1793", remdBase);
        } else {
            WaitStatusKremd(remdBase, "" + xml.uuid + "");
        }
        Thread.sleep(2000);

        System.out.println("Отправляем запрос /api/rremd/");
        JsonPath respons = given()
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .param("LocalUid", xml.uuid)
                .when()
                .get(URLKREMD)
                .body()
                .jsonPath();
        if (time == true) {
            String date = respons.get("result[0]." + DocumentDto + "creationDateTime");
            Assertions.assertEquals(date.substring(0, date.length() - 19),
                    "" + Date + "", "Дата не совпадает");
        } else {
            Assertions.assertEquals((respons.get("result[0]." + DocumentDto + "creationDateTime")),
                    "2002-01-23T18:00:00.000+05:00", "Дата не совпадает");
        }
    }
}
