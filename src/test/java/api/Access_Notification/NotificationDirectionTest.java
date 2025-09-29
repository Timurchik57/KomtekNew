package api.Access_Notification;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты API")
@Feature("Уведомление для записи на слот")
@Tag("Оповещение")
@Tag("Проверка_БД")
@Tag("Консультация_на_оборудование")
@Tag("Расписание_оборудования")
public class NotificationDirectionTest extends BaseAPI {

    AuthorizationObject authorizationObject = new AuthorizationObject(driver);
    DirectionsForQuotas directionsForQuotas = new DirectionsForQuotas(driver);
    String directionGuid;
    String DirectionId;

    @Test
    @Issue(value = "TEL-3856")
    @Issue(value = "TEL-4268")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка уведомления для МО в которую записались на слот")
    @Description("Создаём консультацию в ОКБ - проверяем уведомление, создаём в Яцкив - проверяем отсутствие уведомления")
    public void Access_Type () throws SQLException, InterruptedException {
        AuthorizationMethod(authorizationObject.OKB);
        AddConsul(true);
    }

    @Test
    @DisplayName("Проверка отсутствия уведомления для другой МО")
    public void Access_Type_2 () throws SQLException, InterruptedException {
        AuthorizationMethod(authorizationObject.OKB);
        AddConsul(false);
    }

    @Step("Создаём направление, записываемся на слот, после проверяем уведомление")
    public void AddConsul (boolean MoTrue) throws InterruptedException, SQLException {

        if (MoTrue) {
            directionsForQuotas.CreateConsultationEquipment(true, "159 790 257 20",
                    authorizationObject.Select("Женская консультация"),
                    "Аорта",
                    true,
                    "",
                    authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                    authorizationObject.Select("HMP01"),
                    "1", false, true, true, PNameEquipment);
        } else {
            directionsForQuotas.CreateConsultationEquipment(true, "159 790 257 20",
                    authorizationObject.Select("Женская консультация"),
                    "Аорта",
                    false,
                    PMOTarget,
                    authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                    authorizationObject.Select("HMP01"),
                    "1", false, true, true, PNameEquipment2);
        }

        Thread.sleep(3000);

        sql.StartConnection(
                "select d.id, directionguid, m.\"oid\" req, m2.\"oid\" res, directiontype from telmed.directions d \n" +
                        "join dpc.mis_sp_mu m on d.requestermoid = m.idmu  \n" +
                        "join dpc.mis_sp_mu m2 on d.targetmoid = m2.idmu\n" +
                        "order by d.id desc limit 1;");
        while (sql.resultSet.next()) {
            directionGuid = sql.resultSet.getString("directionGuid");
            DirectionId = sql.resultSet.getString("id");
        }
        System.out.println(directionGuid);

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        authorizationObject.LoadingTime(5);
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(101, "", 0);
        System.out.println(TEXT);
        if (MoTrue) {
            softAssert.assertTrue(TEXT.contains("\"directionGuid\":\"" + directionGuid + "\""),
                    "Уведомление типа 101 не совпадает directionGuid");
            softAssert.assertTrue(TEXT.contains("\"directionId\":" + DirectionId + ""),
                    "Уведомление типа 101 не совпадает directionId");
            softAssert.assertTrue(TEXT.contains("\"accessionNumber\":\"TLMD" + DirectionId + "\""),
                    "Уведомление типа 101 не совпадает accessionNumber");
            /** Проверяем заявку 4268 */
            Assertions.assertTrue(TEXT.contains("\"requestMo\":\"" + POidMoRequest + "\""),
                    "Уведомление типа 101 не совпадает requestMo");
            Assertions.assertTrue(TEXT.contains("\"targetMo\":\"" + POidMoRequest + "\""),
                    "Уведомление типа 101 не совпадает targetMo");
        } else {
            softAssert.assertFalse(TEXT.contains("\"directionGuid\":\"" + directionGuid + "\""),
                    "Уведомление типа 101 не должно добавиться");
            System.out.println("Уведомление для типа 101 нет");
        }
        softAssert.assertAll();
    }
}
