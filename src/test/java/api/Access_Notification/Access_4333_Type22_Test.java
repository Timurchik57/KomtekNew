package api.Access_Notification;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.PageObject.AuthorizationObject;
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
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Уведомление с типом 22")
@Tag("Оповещение")
@Tag("Проверка_БД")
@Tag("Api_Удалённая_консультация")
@Tag("Api_Направление_на_диагностику")
@Tag("Консультация_на_оборудование")
@Tag("Удалённая_консультация")
public class Access_4333_Type22_Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    Authorization authorization;

    private static final Integer TYPE_22 = 22;

    @Issue(value = "TEL-4333")
    @Owner(value = "Галиакберов Тимур")
    @Link(name = "ТМС-2310", url = "https://team-1okm.testit.software/projects/5/tests/2310?isolatedSection=71cd14d9-46c4-4330-a433-f6755c9d1214")
    @DisplayName("UI: уведомление тип 22 при TargetMo = ОКБ")
    @Description("Создать направление на диагностику в UI при TargetMo = ОКБ и проверить получение уведомления типа 22")
    @Test
    public void ui_4333_true() throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        AuthorizationMethod(authorizationObject.OKB);

        directionsForQuotas.CreateConsultationEquipment(
                true,
                "159 790 257 20",
                authorizationObject.Select("Женская консультация"),
                "Аорта",
                true,
                PMOTarget,
                authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                authorizationObject.Select("HMP01"),
                "40",
                false,
                true,
                false,
                ""
        );

        Check(true);
    }

    @DisplayName("UI: нет уведомления тип 22 при TargetMo ≠ ОКБ")
    @Description("Создать направление на диагностику в UI при TargetMo ≠ ОКБ и проверить отсутствие уведомления типа 22")
    @Test
    public void ui_4333_false() throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        AuthorizationMethod(authorizationObject.OKB);

        directionsForQuotas.CreateConsultationEquipment(
                true,
                "159 790 257 20",
                authorizationObject.Select("Женская консультация"),
                "Аорта",
                false,
                PMOTarget,
                authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                authorizationObject.Select("HMP01"),
                "40",
                false,
                true,
                false,
                ""
        );

        Check(false);
    }

    @DisplayName("API: уведомление тип 22 при TargetMo = ОКБ")
    @Description("Создать направление на диагностику через API при TargetMo = ОКБ и проверить получение уведомления типа 22")
    @Test
    public void api_4333_true() throws IOException, InterruptedException, SQLException {
        authorization = new Authorization();

        xml.changes.clear();
        xml.changes.put("$.TargetMedicalOid", POidMoRequest);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);

        Token = authorization.AuthorizationsAdd(POidMoRequest, "21");

        String body = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, body, 200, false);

        Check(true);
    }

    @DisplayName("API: нет уведомления тип 22 при TargetMo ≠ ОКБ")
    @Description("Создать направление на диагностику через API при TargetMo ≠ ОКБ и проверить отсутствие уведомления типа 22")
    @Test
    public void api_4333_false() throws IOException, InterruptedException, SQLException {
        authorization = new Authorization();

        xml.changes.clear();
        xml.changes.put("$.TargetMedicalOid", POidMoTarget);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);

        String body = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, body, 200, true);

        Check(false);
    }

    @Step("Метод для проверки уведмомления типа 22")
    public void Check (boolean check) throws SQLException, InterruptedException {
        Thread.sleep(1500);
        sql.StartConnection("Select * from telmed.directions order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("directionguid");
        }

        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
        driver.get(Notification);
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(TYPE_22, sql.value, 0);
        System.out.println(TEXT);

        if (check) {
            Assertions.assertTrue(TEXT.contains("\"RequestMO\":\"" + POidMoRequest + "\""),
                    "RequestMO для 22 типа не совпадает");
            Assertions.assertTrue(TEXT.contains("\"TargetMO\":\"" + POidMoRequest + "\""),
                    "TargetMO для 22 типа не совпадает");
        } else {
            Assertions.assertFalse(TEXT.contains("\"DirectionGuid\":\"" + sql.value + "\""),
                    "Не должно быть уведомления");
        }
    }
}
