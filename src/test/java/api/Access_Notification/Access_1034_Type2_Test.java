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
@Feature("Уведомления при передаче ПДФ справок в КРЭМД тип 2")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Крэмд_статус")
@Tag("Региональный_документ")
public class Access_1034_Type2_Test extends BaseAPI {
    SQL sql;
    public String value_1034;
    public String TransferId;

    @Issue(value = "TEL-1034")
    @Link(name = "ТМС-1372", url = "https://team-1okm.testit.software/projects/5/tests/1372?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Уведомления при передаче ПДФ справок в КРЭМД Id=101")
    @Description("Отправляем смс, проверяем, что добавилось в таблицу vimis.certificate_remd_sent_result. Используем метод /kremd/collback, после проверяем, что пришло уведомление, в таблице vimis.certificate_remd_sent_result проверяем поля error, fremd_status, emd_id")
    public void Access_1034Id_101 () throws IOException, SQLException, InterruptedException {
        Access1034Method("SMS/SMS3.xml", "101", 99, 1, 1, 9, 18, 1, 57, 21, "vimis.certificate_remd_sent_result");
    }

    @Test
    @DisplayName("Уведомления при передаче ПДФ справок в КРЭМД Id=102")
    public void Access_1034Id_102 () throws IOException, SQLException, InterruptedException {
        Access1034Method("SMS/SMS3.xml", "102", 99, 1, 1, 9, 18, 1, 57, 21, "vimis.certificate_remd_sent_result");
    }

    @Test
    @DisplayName("Уведомления при передаче ПДФ справок в КРЭМД Id=103")
    public void Access_1034Id_103 () throws IOException, SQLException, InterruptedException {
        Access1034Method("SMS/SMS3.xml", "103", 99, 1, 1, 9, 18, 1, 57, 21, "vimis.certificate_remd_sent_result");
    }

    @Test
    @DisplayName("Уведомления при передаче ПДФ справок в КРЭМД Id=104")
    public void Access_1034Id_104 () throws IOException, SQLException, InterruptedException {
        Access1034Method("SMS/SMS3.xml", "104", 99, 1, 1, 9, 18, 1, 57, 21, "vimis.certificate_remd_sent_result");
    }

    @Test
    @DisplayName("Уведомления при передаче ПДФ справок в КРЭМД Id=105")
    public void Access_1034Id_105 () throws IOException, SQLException, InterruptedException {
        Access1034Method("SMS/SMS3.xml", "105", 99, 1, 1, 9, 18, 1, 57, 21, "vimis.certificate_remd_sent_result");
    }

    @Step("Метод отправки пдф справки и просмотр уведомления по ней")
    public void Access1034Method (String File, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String remd) throws SQLException, IOException, InterruptedException {
        sql = new SQL();
        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);
        System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(
                File, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1
        );
        sql.StartConnection(
                "SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            value_1034 = sql.resultSet.getString("id");
            TransferId = sql.resultSet.getString("transfer_id");
            Assertions.assertNotEquals(value_1034, "NULL", "СМС не добавилось в таблицу " + remd + "");
            System.out.println(value_1034);
        }
        CollbekKremd("" + xml.uuid + "", "success", "Проверка 1034", remd);
        WaitStatusKremd(remd, "" + xml.uuid + "");

        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(2, TransferId, vmcl);
        System.out.println(TEXT);

        System.out.println("Переходим в таблицу " + remd + " и проверяем, что статус сменился");
        sql.StartConnection("SELECT * FROM " + remd + "  where id = " + value_1034 + ";");
        while (sql.resultSet.next()) {
            String local_uid = sql.resultSet.getString("local_uid");
            String errors = sql.resultSet.getString("errors");
            String fremd_status = sql.resultSet.getString("fremd_status");
            String emd_id = sql.resultSet.getString("emd_id");
            Assertions.assertEquals(local_uid, "" + xml.uuid + "", "СМС не добавилось в таблицу " + remd + "");
            Assertions.assertEquals(
                    errors, "[{\"Code\": \"string\", \"Message\": \"Проверка 1034\"}]",
                    "СМС не добавилось в таблицу " + remd + " с сообщением - Проверка 1034"
            );
            Assertions.assertEquals(
                    fremd_status, "1", "СМС не добавилось в таблицу " + remd + " с fremd_status - 1");
            Assertions.assertEquals(
                    emd_id, "Проверка 1034", "СМС не добавилось в таблицу " + remd + " с emd_id - Проверка 1034");
        }
    }
}
