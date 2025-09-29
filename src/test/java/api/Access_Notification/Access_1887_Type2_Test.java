package api.Access_Notification;

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
@Feature("Проверка уведомлений фоновым сервисом Тип 2")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Вимис_статус")
public class Access_1887_Type2_Test extends BaseAPI {

    public String error;
    public String TransferId;

    @Issue(value = "TEL-1887")
    @Link(name = "ТМС-1810", url = "https://team-1okm.testit.software/projects/5/tests/1810?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправить СЭМД - установить статус 1 в logs, дождаться смены статуса. Проверить оповещение фоновым сервисом от ФВИМИС для vmcl != 99, после проверить оповещение от РЭМД")
    @Test
    @DisplayName("Отправка смс с vmcl = 1")
    public void Access_1887_1() throws IOException, SQLException, InterruptedException {
        Access_1887Method("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.remd_onko_sent_result");
    }

    @Test
    @DisplayName("Отправка смс с vmcl = 2")
    public void Access_1887_2() throws IOException, SQLException, InterruptedException {
        Access_1887Method("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms",
                "vimis.remd_prevention_sent_result");
    }

    @Test
    @DisplayName("Отправка смс с vmcl = 3")
    public void Access_1887_3() throws IOException, SQLException, InterruptedException {
        Access_1887Method("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms",
                "vimis.remd_akineo_sent_result");
    }

    @Test
    @DisplayName("Отправка смс с vmcl = 4")
    public void Access_1887_4() throws IOException, SQLException, InterruptedException {
        Access_1887Method("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.remd_cvd_sent_result");
    }

    @Test
    @DisplayName("Отправка смс с vmcl = 5")
    public void Access_1887_5() throws IOException, SQLException, InterruptedException {
        Access_1887Method("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms",
                "vimis.remd_infection_sent_result");
    }

    @Test
    @DisplayName("Отправка смс с vmcl = 99")
    public void Access_1887_99() throws IOException, SQLException, InterruptedException {
        Access_1887Method("SMS/SMS3.xml", "3", 99, 3, 1, 9, 18, 1, 57, 21, "", "vimis.remd_sent_result");
    }

    @Step("Метод отправки смс: {0}, vmcl = {3}")
    public void Access_1887Method(String FileName, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String remd) throws IOException, InterruptedException, SQLException {

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);

        System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(FileName, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);

        if (vmcl != 99) {
            sql.StartConnection("Select * from " + sms + " where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                TransferId = sql.resultSet.getString("transfer_id");
                System.out.println(sql.value);
            }

            System.out.println("Используем колбэк ФВИМИС");
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1887", sms, vmcl);
            WaitStatusKremd(remd, "" + xml.uuid + "");

            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
            NotificationsTrue(2, TransferId, vmcl);
            System.out.println(TEXT);
            System.out.println(xml.uuid);
            Assertions.assertTrue(TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");
            Assertions.assertTrue(TEXT.contains("\"ResultDescription\":\"Проверка 1887\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");
            Assertions.assertTrue(TEXT.contains("\"StatusREMD\":null"),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");

        } else {
            sql.StartConnection("Select * from " + remd + " where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                TransferId = sql.resultSet.getString("transfer_id");
                System.out.println(sql.value);
                System.out.println(TransferId);
            }
            Thread.sleep(2500);

            WaitStatusKremd(remd, "" + xml.uuid + "");
            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
            NotificationsTrue(100, TransferId, vmcl);
            System.out.println(TEXT);
            System.out.println(xml.uuid);
            Assertions.assertTrue(TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");
            Assertions.assertFalse(TEXT.contains("\"ResultDescription\":\"Проверка 1887\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");
            Assertions.assertTrue(TEXT.contains("\"DocType\":\"75\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось");
        }
    }
}
