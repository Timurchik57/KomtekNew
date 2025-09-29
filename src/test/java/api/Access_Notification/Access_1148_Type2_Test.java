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

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Отправка документа со статусом regional тип 2")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Крэмд_статус")
@Tag("Региональный_документ")
public class Access_1148_Type2_Test extends BaseAPI {
    SQL sql;
    public String TransferId;

    public void Access1148Method(String File, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position,
            Integer speciality, Integer Role1, Integer position1, Integer speciality1, String remd) throws SQLException, IOException, InterruptedException {
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Переходим на сайт для перехвата сообщений");
            driver.get(Notification);
            Thread.sleep(1500);
            System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
            xml.ApiSmd(
                    File, DocType, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1
            );
            Thread.sleep(1500);
            sql.StartConnection(
                    "SELECT * FROM " + remd + "  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
                TransferId = sql.resultSet.getString("transfer_id");
                System.out.println(sql.value);
                Assertions.assertEquals(
                        sql.value, "regional", "СМС не добавилось в таблицу " + remd + " со статусом regional");
            }
            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
            NotificationsTrue(99, TransferId, vmcl);
            System.out.println(xml.uuid);
            System.out.println(TEXT);
            Assertions.assertTrue(
                    TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                    "Оповещение для vmcl = " + vmcl + " не добавилось"
            );
            Assertions.assertTrue(
                    TEXT.contains(
                            "\"ResultDescription\":\"Региональный документ успешно добавлен в интеграционный шлюз"),
                    "Оповещение для vmcl = " + vmcl + " не добавилось"
            );
        }
    }

    @Issue(value = "TEL-1148")
    @Link(name = "ТМС-1420", url = "https://team-1okm.testit.software/projects/5/tests/1420?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка документа со статусом regional")
    @Description("Отправить документ у которого regional_document = true (на данный момент это id=130), проверить, что Добавился в таблицу vimis.certificate_remd_sent_result со статусом = regional. На сайте перехвата уведомлений появилось нужное уведомление")
    public void Access_1148Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access1148Method(
                "SMS/id=130-vmcl=99.xml", "130", 99, 1, 14, 4, 18, 16, 4, 21, "vimis.certificate_remd_sent_result");
    }
}
