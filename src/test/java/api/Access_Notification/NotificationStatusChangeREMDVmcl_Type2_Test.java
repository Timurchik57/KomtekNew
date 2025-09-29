package api.Access_Notification;

import Base.SQL;
import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Оповещение о смене статуса СЭМД тип 2")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
public class NotificationStatusChangeREMDVmcl_Type2_Test extends BaseAPI {
    Document_667 doc;
    SQL sql;
    String Value;
    String error;
    String TransferId;

    @Issue(value = "TEL-667")
    @Link(name = "ТМС-1185", url = "https://team-1okm.testit.software/projects/5/tests/1185?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Оповещение о смене статуса СЭМД, vmcl=1")
    @Description("Отправить СЭМД, сменить статус, проверить, что приходит оповещение")
    public void NotificationStatusChangeREMDVmcl_1 () throws IOException, SQLException, InterruptedException {
        NotificationStatusChangeREMDVmclMethod(
                "SMS/SMS3.xml", "3", 1, "vimis.sms", "vimis.documentlogs", 3, 1, 9, 18, 1, 57, 21,
                "vimis.remd_onko_sent_result");

    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД, vmcl=2")
    public void NotificationStatusChangeREMDVmcl_2 () throws IOException, SQLException, InterruptedException {
        NotificationStatusChangeREMDVmclMethod(
                "SMS/SMS3.xml", "3", 2, "vimis.preventionsms", "vimis.preventionlogs", 3, 1, 9, 18, 1, 57, 21,
                "vimis.remd_prevention_sent_result");
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД, vmcl=3")
    public void NotificationStatusChangeREMDVmcl_3 () throws IOException, SQLException, InterruptedException {
        NotificationStatusChangeREMDVmclMethod(
                "SMS/SMS3.xml", "3", 3, "vimis.akineosms", "vimis.akineologs", 2, 1, 9, 18, 1, 57, 21,
                "vimis.remd_akineo_sent_result");
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД, vmcl=4")
    public void NotificationStatusChangeREMDVmcl_4 () throws IOException, SQLException, InterruptedException {
        NotificationStatusChangeREMDVmclMethod(
                "SMS/SMS3.xml", "3", 4, "vimis.cvdsms", "vimis.cvdlogs", 2, 1, 9, 18, 1, 57, 21,
                "vimis.remd_cvd_sent_result");
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД, vmcl=5")
    public void NotificationStatusChangeREMDVmcl_5 () throws IOException, SQLException, InterruptedException {
        NotificationStatusChangeREMDVmclMethod(
                "SMS/SMS3.xml", "3", 5, "vimis.infectionsms", "vimis.infectionlogs", 3, 1, 9, 18, 1, 57, 21,
                "vimis.remd_infection_sent_result");
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД, vmcl=99")
    public void NotificationStatusChangeREMDVmcl_99 () throws IOException, SQLException, InterruptedException {
        NotificationStatusChangeREMDVmclMethod(
                "SMS/SMS3.xml", "3", 99, "vimis.remd_sent_result", "", 2, 1, 9, 18, 1, 57, 21, "");
    }

    @Step("Метод отправки смс и проверка уведомления")
    public void NotificationStatusChangeREMDVmclMethod (String File, String DocType, Integer vmcl, String sms, String log, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String remd) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        doc = new Document_667();

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);
        System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(File, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);

        sql.StartConnection(
                "SELECT * FROM " + sms + "  where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            Value = sql.resultSet.getString("id");
            TransferId = sql.resultSet.getString("transfer_id");
            Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + sms + "");
            System.out.println(Value);
        }

        if (vmcl != 99) {
            CollbekVimis("" + xml.uuid + "", "1", "Проверка уведомления 667", sms, vmcl);
            WaitStatusKremd(remd, "" + xml.uuid + "");
        } else {
            WaitStatusKremd(sms, "" + xml.uuid + "");
            CollbekKremd("" + xml.uuid + "", "success", "Проверка уведомления 667", sms);
        }

        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(2, TransferId, vmcl);
        System.out.println(TEXT);
        System.out.println(xml.uuid);

        if (vmcl != 99) {
            Assertions.assertTrue(TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");
            Assertions.assertTrue(TEXT.contains("\"ResultDescription\":\"Проверка уведомления 667\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");

            /** Дополнительно проверяем в оповещении 8 PatientGuid (3099) */
            Assertions.assertTrue(TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                    "PatientGuid по типу 2 для vmcl = " + vmcl + " не добавился");

            sql.StartConnection("SELECT * FROM " + log + "  where sms_id = " + Value + ";");
            while (sql.resultSet.next()) {
                String Value1 = sql.resultSet.getString("sms_id");
                String Value2 = sql.resultSet.getString("description");
                String Value3 = sql.resultSet.getString("msg_id");
                Assertions.assertEquals(Value1, Value, "СМС не добавилось в таблицу " + log + "");
                Assertions.assertEquals(
                        Value2, "Проверка уведомления 667",
                        "СМС не добавилось в таблицу " + log + " с сообщением - Проверяем отправку по заявке 667"
                );
                Assertions.assertEquals(
                        Value3, "" + uuuid + "",
                        "СМС не добавилось в таблицу " + log + " с msg_id - " + uuuid + ""
                );
            }
        } else {
            Assertions.assertTrue(TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");
            Assertions.assertTrue(TEXT.contains("\"EmdId\":\"Проверка уведомления 667\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");

            /** Дополнительно проверяем в оповещении 8 PatientGuid (3099) */
            Assertions.assertTrue(TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                    "PatientGuid по типу 2 для vmcl = " + vmcl + " не добавился");

            sql.StartConnection("SELECT * FROM " + sms + "  where id = " + Value + ";");
            while (sql.resultSet.next()) {
                String local_uid = sql.resultSet.getString("local_uid");
                String errors = sql.resultSet.getString("errors");
                String fremd_status = sql.resultSet.getString("fremd_status");
                String emd_id = sql.resultSet.getString("emd_id");
                Assertions.assertEquals(local_uid, "" + xml.uuid + "", "СМС не добавилось в таблицу " + sms + "");
                Assertions.assertEquals(
                        errors, "[{\"Code\": \"string\", \"Message\": \"Проверка уведомления 667\"}]",
                        "СМС не добавилось в таблицу " + sms + " с сообщением - Проверка 1034"
                );
                Assertions.assertEquals(
                        fremd_status, "1", "СМС не добавилось в таблицу " + sms + " с fremd_status - 1");
                Assertions.assertEquals(
                        emd_id, "Проверка уведомления 667",
                        "СМС не добавилось в таблицу " + sms + " с emd_id - Проверка уведомления 667"
                );
            }
        }
    }
}
