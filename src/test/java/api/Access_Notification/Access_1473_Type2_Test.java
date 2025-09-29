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
@Feature("Оповещение о смене статуса СЭМД для статуса = 6/7 тип 2")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("sent/resending")
@Tag("Проверка_БД")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
@Tag("Фоновый_сервис")
public class Access_1473_Type2_Test extends BaseAPI {
    String Value;
    public String TransferId;

    @Issue(value = "TEL-1473")
    @Issue(value = "TEL-1590")
    @Issue(value = "TEL-2191")
    @Issue(value = "TEL-2744")
    @Link(name = "ТМС-1552", url = "https://team-1okm.testit.software/projects/5/tests/1552?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Оповещение о смене статуса СЭМД для статуса = 6/7, vmcl=1")
    @Description("Отправить СЭМД, сменить статус, проверить, что приходит оповещение")
    public void Access_1473Vmcl_1 () throws IOException, SQLException, InterruptedException {
        Access_1473REMDVmclMethod("SMS/SMS3.xml", "3", 1, 1,  3, 1, 9, 18, 1, 57, 21, "sent");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(3000);
        Access_1473REMDVmclMethod("SMS/SMS3.xml", "3", 1, 2, 3, 1, 9, 18, 1, 57, 21, "resending");
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД для статуса = 6/7, vmcl=2")
    public void Access_1473Vmcl_2 () throws IOException, SQLException, InterruptedException {
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 2, 1, 3, 1, 9, 18, 1, 57, 21, "sent"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 2, 2, 3, 1, 9, 18, 1, 57, 21, "resending"
        );
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД для статуса = 6/7, vmcl=3")
    public void Access_1473Vmcl_3 () throws IOException, SQLException, InterruptedException {
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 3, 1, 2, 1,
                9, 18, 1, 57, 21, "sent"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 3, 2,  2, 1,
                9, 18, 1, 57, 21, "resending"
        );
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД для статуса = 6/7, vmcl=4")
    public void Access_1473Vmcl_4 () throws IOException, SQLException, InterruptedException {
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 4, 1,  2, 1, 9, 18,
                1, 57, 21, "sent"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 4, 2,  2, 1, 9, 18,
                1, 57, 21, "resending"
        );
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД для статуса = 6/7, vmcl=5")
    public void Access_1473Vmcl_5 () throws IOException, SQLException, InterruptedException {
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 5, 1, 3, 1, 9, 18,
                1, 57, 21, "sent"
        );
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 5, 2,  3, 1, 9, 18,
                1, 57, 21, "resending"
        );
    }

    @Test
    @DisplayName("Оповещение о смене статуса СЭМД для статуса = 6/7, vmcl=99")
    public void Access_1473Vmcl_99 () throws IOException, SQLException, InterruptedException {
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 99, 1, 2, 1, 9, 18, 1, 57,
                21, "sent");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(2000);
        Access_1473REMDVmclMethod(
                "SMS/SMS3.xml", "3", 99, 2,  2, 1, 9, 18, 1, 57,
                21, "resending");
    }

    @Step("")
    public void Access_1473REMDVmclMethod (String File, String DocType, Integer vmcl, Integer number, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String StatusRemd) throws IOException, InterruptedException, SQLException {

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);
        TableVmcl(vmcl);

        System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(File, DocType, vmcl, number, true, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);

        sql.StartConnection("SELECT * FROM " + smsBase + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            Value = sql.resultSet.getString("id");
            TransferId = sql.resultSet.getString("transfer_id");
            System.out.println(Value);
            Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + smsBase + "");
        }

        if (vmcl != 99) {
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1473", smsBase, vmcl);
        }

        WaitStatusKremd(remdBase, "" + xml.uuid + "");

        /** Добавляем статус ФРЭМД = 0, чтобы после проверить, что он сбрасывается при статусе от КРЭМД resending (2191) */
        System.out.println("Добавляем статус ФРЭМД = 0");
        sql.UpdateConnection("Update " + remdBase + " set fremd_status = '0' where local_uid = '" + xml.uuid + "';");

        CollbekKremd("" + xml.uuid + "", StatusRemd, "Проверка 1473", remdBase);
        Thread.sleep(1500);

        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(2, TransferId, vmcl);
        System.out.println(TEXT);
        System.out.println(xml.uuid);

        Assertions.assertTrue(TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                "Оповещение для vmcl = " + vmcl + " не добавилось - LocalUid не совпадает"
        );
        if (vmcl != 99 && StatusRemd == "sent") {
            Assertions.assertTrue(TEXT.contains("\"StatusREMD\":6"),
                    "Оповещение для vmcl = " + vmcl + " не добавилось - StatusREMD не совпадает"
            );
            Assertions.assertTrue(
                    TEXT.contains("\"ResultDescription\":\"Документ передан на федеральный уровень ФРЭМД\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось - ResultDescription не совпадает"
            );
        }
        if (vmcl != 99 && StatusRemd == "resending") {
            Assertions.assertTrue(TEXT.contains("\"StatusREMD\":7"),
                    "Оповещение для vmcl = " + vmcl + " не добавилось - StatusREMD не совпадает"
            );
            Assertions.assertTrue(TEXT.contains(
                            "\"ResultDescription\":\"Документ переотправлен на федеральный уровень РЭМД (повторная отправка в связи с ошибками вызванные сбоем в федеральном сервисе)\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось - ResultDescription не совпадает"
            );
        }
        if (vmcl == 99 && StatusRemd == "resending") {
            Assertions.assertTrue(TEXT.contains(
                            "\"ResultDescription\":\"Документ переотправлен на федеральный уровень РЭМД (повторная отправка в связи с ошибками вызванные сбоем в федеральном сервисе)\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось - ResultDescription не совпадает"
            );
            Assertions.assertTrue(TEXT.contains("\"StatusREMD\":7"),
                    "Оповещение для vmcl = " + vmcl + " не добавилось - StatusREMD не совпадает"
            );
        }
        if (vmcl == 99 && StatusRemd == "sent") {
            Assertions.assertTrue(
                    TEXT.contains("\"ResultDescription\":\"Документ передан на федеральный уровень ФРЭМД\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось - ResultDescription не совпадает"
            );
            Assertions.assertTrue(TEXT.contains("\"StatusREMD\":6"),
                    "Оповещение для vmcl = " + vmcl + " не добавилось - StatusREMD не совпадает"
            );
        }
        if (vmcl != 99) {
            sql.StartConnection("SELECT * FROM " + logsBase + "  where sms_id = " + Value + ";");
            while (sql.resultSet.next()) {
                String Value1 = sql.resultSet.getString("sms_id");
                String Value2 = sql.resultSet.getString("description");
                String Value3 = sql.resultSet.getString("msg_id");
                String status = sql.resultSet.getString("status");

                Assertions.assertEquals(Value1, Value, "СМС не добавилось в таблицу " + logsBase + "");
                Assertions.assertEquals(Value2, "Проверка 1473",
                        "СМС не добавилось в таблицу " + logsBase + " с сообщением - Проверяем отправку по заявке 1473"
                );
                Assertions.assertEquals(Value3, "" + uuuid + "",
                        "СМС не добавилось в таблицу " + logsBase + " с msg_id - " + uuuid + ""
                );
                Assertions.assertEquals(status, "1", "СМС не добавилось в таблицу " + smsBase + " с status - 1");
            }

            sql.StartConnection("SELECT * FROM " + remdBase + "  where sms_id = '" + Value + "';");
        } else {
            sql.StartConnection("SELECT * FROM " + remdBase + "  where id = '" + Value + "';");
        }
        while (sql.resultSet.next()) {
            String local_uid = sql.resultSet.getString("local_uid");
            String errors = sql.resultSet.getString("errors");
            String fremd_status = sql.resultSet.getString("fremd_status");
            String status = sql.resultSet.getString("status");

            /** Проверяем, что fremd_status сбросился (2191) */
            Assertions.assertEquals(fremd_status, null, "СМС не добавилось в таблицу " + smsBase + "");

            Assertions.assertEquals(local_uid, "" + xml.uuid + "", "СМС не добавилось в таблицу " + smsBase + "");
            Assertions.assertEquals(errors, "[{\"Code\": \"string\", \"Message\": \"Проверка 1473\"}]",
                    "Текст ошибки от КРЭМД не добавлился в таблицу " + remdBase + "");
            if (StatusRemd == "sent") {
                Assertions.assertEquals(status, "sent",
                        "СМС не добавилось в таблицу " + smsBase + " с status - sent");
            }
            if (StatusRemd == "resending") {
                Assertions.assertEquals(status, "resending",
                        "СМС не добавилось в таблицу " + smsBase + " с status - resending"
                );
            }
        }

        /** Добавляем статус ФРЭМД = 1, чтобы после проверить, что он не сбрасывается при статусе от КРЭМД resending (2744) */
        System.out.println("Добавляем статус ФРЭМД = 1");
        sql.UpdateConnection("Update " + remdBase + " set fremd_status = '1' where local_uid = '" + xml.uuid + "';");
        CollbekKremd("" + xml.uuid + "", StatusRemd, "Проверка 1473", remdBase);
        Thread.sleep(1500);

        if (vmcl != 99) {
            sql.StartConnection("SELECT * FROM " + remdBase + "  where sms_id = '" + Value + "';");
        } else {
            sql.StartConnection("SELECT * FROM " + remdBase + "  where id = '" + Value + "';");
        }
        while (sql.resultSet.next()) {
            String local_uid = sql.resultSet.getString("local_uid");
            String errors = sql.resultSet.getString("errors");
            String fremd_status = sql.resultSet.getString("fremd_status");
            String status = sql.resultSet.getString("status");

            /** Проверяем, что fremd_status не сбросился (2744) */
            Assertions.assertEquals(fremd_status, "1", "СМС не добавилось в таблицу " + smsBase + "");

            Assertions.assertEquals(local_uid, "" + xml.uuid + "", "СМС не добавилось в таблицу " + smsBase + "");
            if (StatusRemd == "sent") {
                Assertions.assertEquals(status, "sent",
                        "СМС не добавилось в таблицу " + smsBase + " с status - sent");
            }
            if (StatusRemd == "resending") {
                Assertions.assertEquals(status, "resending",
                        "СМС не добавилось в таблицу " + smsBase + " с status - resending"
                );
            }
        }
    }
}
