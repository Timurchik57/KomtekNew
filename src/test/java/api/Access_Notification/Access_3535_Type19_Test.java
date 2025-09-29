package api.Access_Notification;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Уведомление с типом 19")
@Tag("Оповещение")
@Tag("СМП")
@Tag("Проверка_БД")
@Tag("Дополнительные_параметры")
public class Access_3535_Type19_Test extends BaseAPI {

    @Issue(value = "TEL-3535")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Приём по обратному талону карты coupon-in-query")
    @Description("Отправить нужную справку, проверить добавление значения в таблицы и уведомление")
    public void Access_3535() throws IOException, SQLException, InterruptedException {
        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);

        String body = "";

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);

        System.out.println("Приём неотложной помощи");
        body = "{\n" +
                "   \"cardUid\":\"" + uuid + "\",\n" +
                "   \"smpDiagnoses\":{\n" +
                "      \"code\":\"A00.1\",\n" +
                "      \"name\":\"Холера неуточненная\"\n" +
                "   },\n" +
                "   \"doctorDiagnoses\":{\n" +
                "      \"code\":\"A00.1\",\n" +
                "      \"name\":\"Холера неуточненная\"\n" +
                "   },\n" +
                "   \"finalDiagnoses\":{\n" +
                "      \"code\":\"A00.1\",\n" +
                "      \"name\":\"Холера неуточненная\"\n" +
                "   },\n" +
                "   \"operation\":{\n" +
                "      \"name\":\"Наименование операции\",\n" +
                "      \"dateTime\":\"2024-10-31T15:45:24.874325\"\n" +
                "   },\n" +
                "   \"timeStationary\":\"2 часа\",\n" +
                "   \"resultStationary\":\"Результат пребывания\",\n" +
                "   \"finishFact\":\"Факт о выписке пациента со стационара\",\n" +
                "   \"finishDateTime\":\"2024-10-31T15:45:24.874325\",\n" +
                "   \"smpComment\":\"Замечания к работе СМП\",\n" +
                "   \"headStationary\":{\n" +
                "      \"localId\":\"1234567896\",\n" +
                "      \"lastName\":\"Мищенко\",\n" +
                "      \"firstName\":\"Оксана\",\n" +
                "      \"middleName\":\"Святославовна\"\n" +
                "   }\n" +
                "}";
        Api(HostAddress + "/api/smd/coupon-in-query", "post", null, null, body, 200, true);
        Thread.sleep(1500);

        sql.StartConnection("SELECT * FROM vimis.coupon_in_query  where carduid = '" + uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("carduid");
        }

        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
        Thread.sleep(1500);
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        Thread.sleep(1500);
        NotificationsTrue(19, sql.value, 0);

        System.out.println(TEXT);
        Assertions.assertTrue(TEXT.contains("\"CardUid\":\"" + uuid + "\""),
                "Оповещение для coupon-in-query не добавилось");
    }
}
