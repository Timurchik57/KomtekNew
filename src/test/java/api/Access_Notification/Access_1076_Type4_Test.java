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
@Feature("Оповещение о смене статуса с типом 4 для vmcl=3")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Вимис_статус")
public class Access_1076_Type4_Test extends BaseAPI {
    Document_667 doc;
    String Value;
    String TransferId;

    @Issue(value = "TEL-1076")
    @Link(name = "ТМС-1418", url = "https://team-1okm.testit.software/projects/5/tests/1418?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Оповещение о смене статуса с типом 4 для vmcl=3")
    @Description("Авторизоваться под МО с ИС=15, Отправить СЭМД с токеном этой авторизации и проверить изменённое оповещение для типа 4")
    public void Access_1076 () throws IOException, SQLException, InterruptedException {
        Access_1076Method("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21);
    }

    @Step("")
    public void Access_1076Method (String File, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException, SQLException {

        doc = new Document_667();
        TableVmcl(vmcl);

        /** Отправляем смс с id = 1 и vmcl = 1 */
        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);

        System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(File, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1,
                position1, speciality1);

        sql.StartConnection(
                "SELECT * FROM " + smsBase + "  where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            Value = sql.resultSet.getString("id");
            TransferId = sql.resultSet.getString("transfer_id");
            System.out.println(Value);
            Assertions.assertNotEquals(Value, "NULL", "СМС не добавилось в таблицу " + smsBase + "");
        }
        CollbekVimis("" + xml.uuid + "", "1", "Проверка 1076", smsBase, vmcl);
        Thread.sleep(2000);

        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(4, TransferId, vmcl);
        System.out.println(xml.uuid);
        System.out.println(TEXT);
        Assertions.assertTrue(
                TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                "Оповещение для vmcl = " + vmcl + " не добавилось"
        );
        Assertions.assertTrue(
                TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                "Оповещение для vmcl = " + vmcl + " не добавилось"
        );

        sql.StartConnection("SELECT * FROM " + logsBase + "  where sms_id = " + Value + ";");
        while (sql.resultSet.next()) {
            String Value1 = sql.resultSet.getString("sms_id");
            String Value2 = sql.resultSet.getString("description");
            String Value3 = sql.resultSet.getString("msg_id");
            Assertions.assertEquals(Value1, Value, "СМС не добавилось в таблицу " + logsBase + "");
            Assertions.assertEquals(
                    Value2, "Проверка 1076",
                    "СМС не добавилось в таблицу " + logsBase + " с сообщением - Проверка 1076"
            );
            Assertions.assertEquals(
                    Value3, "" + uuuid + "",
                    "СМС не добавилось в таблицу " + logsBase + " с msg_id - " + uuuid + ""
            );
        }
    }
}
