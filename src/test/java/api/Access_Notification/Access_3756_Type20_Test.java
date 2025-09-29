package api.Access_Notification;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.XML;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import api.Before.Authorization;
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
@Epic("Тесты API")
@Feature("Уведомление с типом 20")
@Tag("Оповещение")
@Tag("Проверка_БД")
@Tag("Api_Удалённая_консультация")
@Tag("Удалённая_консультация")
public class Access_3756_Type20_Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    ConsultationOutgoingUnfinished consultationOutgoingUnfinished;
    DirectionsForQuotas directionsForQuotas;
    Authorization authorization;

    String directionGuid = null;
    String directiontype = null;
    String responseMO = null;
    String requestMO = null;

    @Test
    @Issue(value = "TEL-3756")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка уведомления типа 20 при создании консультации в разных МО")
    @Description("Создаём консультацию в ОКБ - проверяем уведомление, создаём в Яцкив - проверяем отсутствие уведомления")
    public void Access_Type20() throws IOException, SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        consultationOutgoingUnfinished = new ConsultationOutgoingUnfinished(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        authorization = new Authorization();

        System.out.println("1. Создание консультации в ОКБ");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);

        directionsForQuotas.CreateRemoteConsul(true,
                "БУ ХМАО-Югры \"Окружная клиническая больница\"",
                "159 790 257 20",
                PName,
                "Женская консультация",
                "детской урологии-андрологии",
                "плановая",
                "Подозрение на COVID-19",
                "Паратиф A");

        ClickElement(directionsForQuotas.SendConsul);
        CheckType20(true);

        System.out.println("2. Создание консультации через метод");
        PatientGuid = "0F94EB3B-C391-463E-BF5B-D90D63936951";
        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");
        System.out.println("Отменяем созданную консультацию");
        consultationOutgoingUnfinished.CancelConsultation(PatientGuid);

        xml.changes.put("$.PatientGuid", PatientGuid);
        xml.changes.put("$.TargetMOId", POidMoRequest);
        String body = JsonMethod("SMS/Body/api_direction.json", xml.changes, false, null);

        Api(HostAddress + "/api/direction", "post", null, null, body, 200, false);
        CheckType20(true);

        System.out.println("3. Создание консультации в Яцкив");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);

        directionsForQuotas.CreateRemoteConsul(
                false,
                "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"",
                "159 790 257 20",
                PName,
                "Женская консультация",
                "детской урологии-андрологии",
                "плановая",
                "Подозрение на COVID-19",
                "Паратиф A");

        ClickElement(directionsForQuotas.SendConsul);
        CheckType20(false); // Нет  уведомления так как в mis_address забит только для ОКБ
    }

    @Step("Метод для проверки 20 типа уведомления")
    public void CheckType20 (boolean check) throws SQLException, InterruptedException {
        sql = new SQL();
        authorizationObject = new AuthorizationObject(driver);
        xml = new XML();

        sql.StartConnection(
                "select directionguid, m.\"oid\" req, m2.\"oid\" res, directiontype from telmed.directions d \n" +
                        "join dpc.mis_sp_mu m on d.requestermoid = m.idmu  \n" +
                        "join dpc.mis_sp_mu m2 on d.targetmoid = m2.idmu\n" +
                        "order by id desc limit 1;");
        while (sql.resultSet.next()) {
            directionGuid = sql.resultSet.getString("directionGuid");
            directiontype = sql.resultSet.getString("directiontype");
            responseMO = sql.resultSet.getString("req");
            requestMO = sql.resultSet.getString("res");
        }

        System.out.println("Проверяем наличие уведомления типа 20");
        Assertions.assertEquals(directiontype, "2", "Консультация не добавилась в БД с типом 2");

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        authorizationObject.LoadingTime(5);
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(20, "", 0);
        System.out.println(TEXT);
        if (check) {
            Assertions.assertTrue(TEXT.contains("\"DirectionGuid\":\"" + directionGuid + "\""),
                    "Уведомление типа 20 не совпадает DirectionGuid");
            Assertions.assertTrue(TEXT.contains("\"RequestMO\":\"" + responseMO + "\""),
                    "Уведомление типа 20 не совпадает RequestMO");
            Assertions.assertTrue(TEXT.contains("\"TargetMO\":\"" + requestMO + "\""),
                    "Уведомление типа 20 не совпадает TargetMO");
            System.out.println("Уведомление для типа 20 есть");
        } else {
            Assertions.assertFalse(TEXT.contains("\"directionGuid\":\"" + directionGuid + "\""),
                    "Уведомление типа 20 не должно добавиться");
            System.out.println("Уведомление для типа 20 нет");
        }
    }
}
