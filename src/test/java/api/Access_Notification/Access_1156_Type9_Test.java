package api.Access_Notification;

import Base.SQL;
import Base.TestListener;
import api.Access_613Test;
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
import org.openqa.selenium.By;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Оповещения по свидетельствам о смерти тип 9")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
public class Access_1156_Type9_Test extends BaseAPI {
    public String body;
    public String Value;
    public String TransferId;
    public String error;
    public String DateDeathsAll;
    public String DocNumber;

    Access_613Test access613Test;

    @Issue(value = "TEL-1156")
    @Issue(value = "TEL-1525")
    @Issue(value = "TEL-2926")
    @Issue(value = "TEL-2969")
    @Link(name = "ТМС-1498", url = "https://team-1okm.testit.software/projects/5/tests/1498?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Link(name = "ТМС-1738", url = "https://team-1okm.testit.software/projects/5/tests/1738?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 13 vmcl = 1 StatusVimis = 1")
    @Description("Отправить СЭМД, сменить статус, проверить, что приходит оповещение")
    public void Access_1156_13_1() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method("SMS/SMS13-id=13.xml", "13", 1, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_onko_sent_result", 3, 6,
                4, 18, 1, 57, 21, "1", "success", 9);
    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 13 vmcl = 2 StatusVimis = 1")
    public void Access_1156_13_2() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method("SMS/SMS13-id=13.xml", "13", 2, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 6, 4, 18, 1, 57, 21, "1", "success", 9);
    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 13 vmcl = 3 StatusVimis = 1")
    public void Access_1156_13_3() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/SMS13-id=13.xml", "13", 3, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result",
                2, 6, 4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 13 vmcl = 4 StatusVimis = 1")
    public void Access_1156_13_4() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/SMS13-id=13.xml", "13", 4, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 6, 4,
                18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 13 vmcl = 5 StatusVimis = 1")
    public void Access_1156_13_5() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method("SMS/SMS13-id=13.xml", "13", 5, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result", 3, 6, 4,
                18, 1, 57, 21, "1", "success", 9);
    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 14 vmcl = 1 StatusVimis = 1")
    public void Access_1156_14_1() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/SMS13-id=14.xml", "14", 1, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result", 3, 6,
                4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 14 vmcl = 2 StatusVimis = 1")
    public void Access_1156_14_2() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/SMS13-id=14.xml", "14", 2, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 6, 4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 14 vmcl = 3 StatusVimis = 1")
    public void Access_1156_14_3() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/SMS13-id=14.xml", "14", 3, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result",
                2, 6, 4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 14 vmcl = 4 StatusVimis = 1")
    public void Access_1156_14_4() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/SMS13-id=14.xml", "14", 4, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 6, 4,
                18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 14 vmcl = 5 StatusVimis = 1")
    public void Access_1156_14_5() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/SMS13-id=14.xml", "14", 5, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result", 3, 6, 4,
                18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 1 StatusVimis = 1, statusRemd = success")
    public void Access_1156_15_1() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 1, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result", 3,
                6, 4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 2 StatusVimis = 1, statusRemd = success")
    public void Access_1156_15_2() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 2, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 6, 4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 3 StatusVimis = 1, statusRemd = success")
    public void Access_1156_15_3() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 3, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_akineo_sent_result", 2, 6, 4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 4 StatusVimis = 1, statusRemd = success")
    public void Access_1156_15_4() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 4, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 6,
                4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 5 StatusVimis = 1, statusRemd = success")
    public void Access_1156_15_5() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 5, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result", 3, 6,
                4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 99 statusRemd = success")
    public void Access_1156_15_99() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 21 vmcl = 3 StatusVimis = 1")
    public void Access_1156_21_3() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/SMS19-id=21.xml", "21", 3, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result",
                3, 6, 4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 32 vmcl = 99 statusRemd = success")
    public void Access_1156_32_99() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=32-vmcl=99.xml", "32", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 33 vmcl = 1 StatusVimis = 1, statusRemd = success")
    public void Access_1156_33_1() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=33-vmcl=1.xml", "33", 1, "vimis.sms", "vimis.documentlogs", "vimis.remd_onko_sent_result", 3, 6,
                4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 33 vmcl = 2 StatusVimis = 1, statusRemd = success")
    public void Access_1156_33_2() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=33-vmcl=1.xml", "33", 2, "vimis.preventionsms", "vimis.preventionlogs",
                "vimis.remd_prevention_sent_result", 3, 6, 4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 33 vmcl = 3 StatusVimis = 1, statusRemd = success")
    public void Access_1156_33_3() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=33-vmcl=1.xml", "33", 3, "vimis.akineosms", "vimis.akineologs", "vimis.remd_akineo_sent_result",
                2, 6, 4, 18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 33 vmcl = 4 StatusVimis = 1, statusRemd = success")
    public void Access_1156_33_4() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=33-vmcl=1.xml", "33", 4, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 6, 4,
                18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 33 vmcl = 5 StatusVimis = 1, statusRemd = success")
    public void Access_1156_33_5() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=33-vmcl=1.xml", "33", 5, "vimis.infectionsms", "vimis.infectionlogs",
                "vimis.remd_infection_sent_result", 3, 6, 4,
                18, 1, 57, 21, "1", "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 33 vmcl = 99 statusRemd = success")
    public void Access_1156_33_99() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=33-vmcl=1.xml", "33", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 34 vmcl = 99 statusRemd = success")
    public void Access_1156_34_99() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/SMSV19-id=34.xml", "34", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 36 vmcl = 99 statusRemd = success")
    public void Access_1156_36_99() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=36-vmcl=99.xml", "36", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "success", 9
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 2")
    @DisplayName("Оповещения по свидетельствам о смерти id = 36 vmcl = 99 StatusVimis = 0")
    public void Access_1156_36_99_() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=36-vmcl=99.xml", "36", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "error", 2
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС тип 2")
    @DisplayName("Оповещения по свидетельствам о смерти id = 13 vmcl = 2 StatusVimis = 0")
    public void Access_1156_13_2_() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method("SMS/SMS13-id=13.xml", "13", 2, "vimis.preventionsms", "vimis.preventionlogs", "vimis.remd_prevention_sent_result", 3, 6, 4, 18, 1, 57, 21, "0", "error", 2);
    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 2")
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 4 StatusVimis = 0")
    public void Access_1156_15_4_() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 4, "vimis.cvdsms", "vimis.cvdlogs", "vimis.remd_cvd_sent_result", 2, 6,
                4, 18, 1, 57, 21, "0", "error", 2
        );

    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 2")
    @DisplayName("Оповещения по свидетельствам о смерти id = 15 vmcl = 99 statusRemd = error")
    public void Access_1156_15_99_() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=15-vmcl=99.xml", "15", 99, "", "", "vimis.remd_sent_result", 0, 6, 4, 18, 1, 57, 21, "1",
                "error", 2
        );
    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 134 vmcl = 99 statusRemd = success")
    public void Access_1156_134_99_() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=134.xml", "134", 99, "", "", "vimis.remd_sent_result", 0, 1, 6, 18, 6, 4, 21, "1",
                "success", 9
        );
    }

    @Test
    @Story("Оповещения по свидетельствам о смерти для ВИМИС и РЭМД тип 9")
    @DisplayName("Оповещения по свидетельствам о смерти id = 135 vmcl = 99 statusRemd = success")
    public void Access_1156_135_99_() throws IOException, SQLException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException {
        Access_1156Method(
                "SMS/id=135.xml", "135", 99, "", "", "vimis.remd_sent_result", 0, 1, 6, 18, 6, 4, 21, "1",
                "success", 9
        );
    }

    @Step("Метод отправки смс и проверки уведомления типа 9")
    public void Access_1156Method(String File, String DocType, Integer vmcl, String sms, String logs, String remd, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String statusVimis, String statusRemd, Integer TypeNotification) throws IOException, InterruptedException, SQLException, XPathExpressionException, ParserConfigurationException, SAXException {

        sql = new SQL();
        access613Test = new Access_613Test();
        TableVmcl(vmcl);

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);

        System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
        xml.ReplacementWordInFile(File, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1,
                position1,
                speciality1);
        JsonPath response = given()
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + Token)
                .contentType(ContentType.JSON)
                .when()
                .body(xml.body)
                .post(HostAddress + "/api/smd")
                .prettyPeek()
                .body()
                .jsonPath();
        TransferId = response.get("result[0].transferId");

        if (vmcl != 99) {
            CollbekVimis("" + xml.uuid + "", statusVimis, "Проверка Уведомлений по РЭМД (1156, 1525)", sms, vmcl);
            sql.StartConnection(
                    "SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                Value = sql.resultSet.getString("id");
                System.out.println(Value);
                Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + sms + "");
            }
        }
        if (vmcl == 99) {
            sql.StartConnection(
                    "SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                Value = sql.resultSet.getString("id");
                Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + remd + "");
                System.out.println(Value);
            }
        }
        if (DocType == "15" | DocType == "32" | DocType == "33" | DocType == "34" | DocType == "36" | DocType == "134" | DocType == "135") {
            if (statusVimis == "1") {
                WaitStatusKremd(remd, "" + xml.uuid + "");
                CollbekKremd("" + xml.uuid + "", statusRemd, "Проверка Уведомлений по РЭМД (1156, 1525)", remd);
            }
        }

        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
        if (KingNumber == 9) {
            ClickElement(By.xpath("//div[@id='selector']/div[1]"));
            WaitElement(By.xpath("//div[@class='request']/pre"));
            TEXT = driver.findElement(By.xpath("//div[@class='request']/pre")).getText();
            System.out.println(TEXT);
        } else {
            ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
            NotificationsTrue(TypeNotification, TransferId, vmcl);
            System.out.println(TEXT);
            System.out.println(xml.uuid);
        }
        if (statusVimis == "1" & statusRemd == "success") {

            if (DocType != "32" & DocType != "36") {
                /** Проверяем дату смерти в оповещении (2926) */
                DateDeathsAll = access613Test.DateDeath(File);
                DocNumber = getXml(File,
                        "//code[@displayName='Номер медицинского свидетельства о смерти']/following-sibling::value/text()");
                if (DocNumber == null) {
                    DocNumber = getXml(File,
                            "//code[@displayName='Номер медицинского свидетельства о перинатальной смерти']/following-sibling::value/text()");
                }
                System.out.println("Дата смерти " + DateDeathsAll);
                System.out.println("Номер документа " + DocNumber);
            }

            System.out.println("Проверка для типа опевещинй 9");
            if (vmcl != 99) {
                Assertions.assertTrue(
                        TEXT.contains("\"TransferId\":\"" + TransferId + "\""),
                        "TransferId для vmcl = " + vmcl + " отсутствует"
                );
                Assertions.assertTrue(
                        TEXT.contains("\"RequestId\":\"" + uuuid + "\""),
                        "RequestId для vmcl = " + vmcl + " отсутствует"
                );
                Assertions.assertTrue(
                        TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                        "PatientGuid для vmcl = " + vmcl + " отсутствует"
                );

                if (DocType != "32" & DocType != "36") {
                    /** Проверяем дату смерти в оповещении (2926) */
                    Assertions.assertTrue(TEXT.contains("\"DeathDate\":\"" + DateDeathsAll + ""),
                            "Дата смерти для vmcl = " + vmcl + " Doctype = " + DocType + " отсутствует");

                    if (DocType != "13") {
                        Assertions.assertTrue(TEXT.contains("\"DocNumber\":\"" + DocNumber + "\""),
                                "Номер документа смерти для vmcl = " + vmcl + " Doctype = " + DocType + " отсутствует");
                    }
                }

                if (DocType != "21") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"SMSV13\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                } else {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"SMSV19\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
            } else {
                Assertions.assertTrue(
                        TEXT.contains("\"TransferId\":\"" + TransferId + "\""),
                        "TransferId для vmcl = " + vmcl + " отсутствует"
                );
                Assertions.assertTrue(
                        TEXT.contains("\"RequestId\":null"),
                        "RequestId для vmcl = " + vmcl + " отсутствует"
                );
                Assertions.assertTrue(
                        TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                        "PatientGuid для vmcl = " + vmcl + " отсутствует"
                );

                if (DocType != "32" & DocType != "36") {
                    /** Проверяем дату смерти в оповещении (2926) */
                    Assertions.assertTrue(TEXT.contains("\"DeathDate\":\"" + DateDeathsAll + ""),
                            "Дата смерти для vmcl = " + vmcl + " Doctype = " + DocType + " отсутствует");

                    if (DocType != "13") {
                        Assertions.assertTrue(TEXT.contains("\"DocNumber\":\"" + DocNumber + "\""),
                                "Номер документа смерти для vmcl = " + vmcl + " Doctype = " + DocType + " отсутствует");
                    }
                }

                if (DocType == "15") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"58\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
                if (DocType == "32") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"76\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
                if (DocType == "33") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"113\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
                if (DocType == "34") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"114\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
                if (DocType == "36") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"118\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
            }
        } else {
            System.out.println("Проверка для типа опевещинй 2");
            if (vmcl != 99) {
                Assertions.assertTrue(
                        TEXT.contains("\"TransferId\":\"" + TransferId + "\""),
                        "TransferId для vmcl = " + vmcl + " отсутствует"
                );
                Assertions.assertTrue(
                        TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                        "LocalUid для vmcl = " + vmcl + " отсутствует"
                );
                Assertions.assertTrue(
                        TEXT.contains("\"RequestId\":\"" + uuuid + "\""),
                        "RequestId для vmcl = " + vmcl + " отсутствует"
                );
                Assertions.assertTrue(
                        TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                        "PatientGuid для vmcl = " + vmcl + " присутствует"
                );
                if (DocType != "21") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"SMSV13\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                } else {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"SMSV19\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
            } else {
                Assertions.assertTrue(
                        TEXT.contains("\"EmdId\":\"Проверка Уведомлений по РЭМД (1156, 1525)\""),
                        "EmdId для vmcl = " + vmcl + " отсутствует"
                );
                Assertions.assertTrue(
                        TEXT.contains("\"RequestId\":null"),
                        "RequestId для vmcl = " + vmcl + " отсутствует"
                );
                Assertions.assertTrue(
                        TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                        "PatientGuid для vmcl = " + vmcl + " отсутствует"
                );
                if (DocType == "15") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"58\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
                if (DocType == "32") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"76\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
                if (DocType == "33") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"113\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
                if (DocType == "34") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"114\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
                if (DocType == "36") {
                    Assertions.assertTrue(
                            TEXT.contains("\"DocType\":\"118\""),
                            "DocType для vmcl = " + vmcl + " отсутствует"
                    );
                }
            }
        }
        if (vmcl != 99) {
            sql.StartConnection("SELECT * FROM " + logs + "  where sms_id = " + Value + ";");
            while (sql.resultSet.next()) {
                String Value1 = sql.resultSet.getString("sms_id");
                String Value2 = sql.resultSet.getString("description");
                String Value3 = sql.resultSet.getString("msg_id");
                Assertions.assertEquals(Value1, Value, "СМС не добавилось в таблицу " + logs + "");
                Assertions.assertEquals(
                        Value2, "Проверка Уведомлений по РЭМД (1156, 1525)",
                        "СМС не добавилось в таблицу " + logs + " с сообщением - Проверка Уведомлений по РЭМД (1156, 1525)"
                );
                Assertions.assertEquals(
                        Value3, "" + uuuid + "",
                        "СМС не добавилось в таблицу " + logs + " с msg_id - " + uuuid + "");
            }
        } else {
            sql.StartConnection("SELECT * FROM " + remd + "  where id = " + Value + ";");
            while (sql.resultSet.next()) {
                String local_uid = sql.resultSet.getString("local_uid");
                String errors = sql.resultSet.getString("errors");
                String fremd_status = sql.resultSet.getString("fremd_status");
                String emd_id = sql.resultSet.getString("emd_id");
                Assertions.assertEquals(local_uid, "" + xml.uuid + "", "СМС не добавилось в таблицу " + remd + "");
                if (statusRemd.contains("error")) {
                    Assertions.assertEquals(
                            fremd_status, "0",
                            "СМС не добавилось в таблицу " + remd + " с fremd_status - 0"
                    );
                } else {
                    Assertions.assertEquals(
                            fremd_status, "1",
                            "СМС не добавилось в таблицу " + remd + " с fremd_status - 1"
                    );
                }
                Assertions.assertEquals(
                        emd_id, "Проверка Уведомлений по РЭМД (1156, 1525)",
                        "СМС не добавилось в таблицу " + remd + " с emd_id - Проверка Уведомлений по РЭМД (1156, 1525)"
                );
            }
        }
    }
}
