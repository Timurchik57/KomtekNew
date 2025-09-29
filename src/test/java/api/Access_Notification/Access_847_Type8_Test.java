package api.Access_Notification;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Оповещение с типом 8")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
public class Access_847_Type8_Test extends BaseAPI {
    public Integer value;
    public String TransferId;
    public String URLKremd;

    @Issue(value = "TEL-847")
    @Issue(value = "TEL-3099")
    @Link(name = "ТМС-1683", url = "https://team-1okm.testit.software/projects/5/tests/1683?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Оповещение с типом 8")
    @Description("Отправить смс с vmcl = 99, после использовать запрос /kremd/callback/mse для смены статуса. Далее переходим в сервис перехвата сообщений и проверяем, что оповещение пришло")
    public void Access_847_99() throws IOException, SQLException, InterruptedException {
        Access_847TestMethod("SMS/SMS3.xml", "3", 99, 3, 1, 9, 18, 1, 57, 21);
    }

    @Step("Метод отправки смс и проверки уведомления")
    public void Access_847TestMethod(String FileName, String Doctype, Integer Vmlc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, SQLException, InterruptedException {

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);
        TableVmcl(Vmlc);

        System.out.println("Отправляем смс с Doctype = " + Doctype + " и vmcl=" + Vmlc + "");
        xml.ApiSmd(FileName, Doctype, Vmlc, 1, true, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);

        sql.StartConnection(
                "Select * from " + smsBase + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            value = Integer.valueOf(sql.resultSet.getString("id"));
            TransferId = sql.resultSet.getString("transfer_id");
            System.out.println(value);
        }

        if (Vmlc == 99) {
            WaitStatusKremd(remdBase, String.valueOf(xml.uuid));
        } else {
            CollbekVimis(String.valueOf(xml.uuid), "1", "Проверка тип уведомления 8 (847)", smsBase, Vmlc);
            WaitStatusKremd(remdBase, String.valueOf(xml.uuid));
            CollbekKremd(String.valueOf(xml.uuid), "success", "Проверка тип уведомления 8 (847)", remdBase);
        }

        if (KingNumber == 1) {
            URLKremd = "http://192.168.2.126:1108/kremd/callback/mse";
        }
        if (KingNumber == 2) {
            URLKremd = "http://192.168.2.126:1131/kremd/callback/mse";
        }
        if (KingNumber == 4) {
            URLKremd = "http://212.96.206.70:1109/kremd/callback/mse";
        }

        Api(URLKremd, "post", null, null, "" +
                "{\n" +
                        "  \"associations\": [\n" +
                        "    {\n" +
                        "      \"target\": \"Проверка тип уведомления 8 (847)\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"id\": \"" + xml.uuid + "\", \n" +
                        "  \"emdrId\": \"string\"\n" +
                        "}", 200, true);
        Assertions.assertEquals(Response.getString("id"), "" + xml.uuid + "");

        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(8, TransferId, Vmlc);
        System.out.println(xml.uuid);
        System.out.println(TEXT);

        Assertions.assertTrue(TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                "Оповещение для vmcl = " + Vmlc + " не добавилось");
        Assertions.assertTrue(TEXT.contains("\"TransferId\":\"" + TransferId + "\""),
                "Оповещение для vmcl = " + Vmlc + " не добавилось");

        /** Дополнительно проверяем в оповещении 8 PatientGuid (3099) */
        Assertions.assertTrue(TEXT.contains("\"PatientGuid\":\"" +  PatientGuid.toLowerCase() + "\""),
                "PatientGuid по типу 8 для vmcl = " + Vmlc + " не добавился");

        if (Vmlc == 99) {
            sql.StartConnection("Select * from " + remdBase + " where id = " + value + "");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("target_emdid");
                Assertions.assertEquals(sql.value, "Проверка тип уведомления 8 (847)", "fremd_status не сменился на 1");
            }
        } else {
            sql.StartConnection("Select * from " + remdBase + " where sms_id = " + value + "");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("target_emdid");
                Assertions.assertEquals(sql.value, "Проверка тип уведомления 8 (847)", "fremd_status не сменился на 1");
            }
        }
    }
}
