package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Проверка_интерфейса")
@Tag("Api_Удалённая_консультация")
public class Access_1152Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    ConsultationUnfinished consultationUn;
    ConsultationOutgoingUnfinished consultationOU;
    Authorization authorization;
    SQL sql;

    @Issue(value = "TEL-1152")
    @Link(name = "ТМС-1450", url = "https://team-1okm.testit.software/projects/5/tests/1450?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Очное консультирование в удаленном консультировании")
    @Description("Перейти в Консультации - Создать направление - Направление на консультацию - Создаём направление, указывая цель консультации - Очное консультирование. В исходящих выделена синим. Переходим в МО, куда отправили - входящие, выделена синим, после назначения времени, также выделена синим")
    public void Access_1152() throws InterruptedException, IOException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationUn = new ConsultationUnfinished(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        authorization = new Authorization();
        sql = new SQL();

        System.out.println("Отменяем созданную консультацию");
        consultationOU.CancelConsultation(PatientGuid);

        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        xml.changes.put("$.PatientGuid", PatientGuid);
        xml.changes.put("$.TargetMOId", MO);
        xml.changes.put("$.Profile", 19);
        xml.changes.put("$.consultationReason", 4);
        String body = JsonMethod("SMS/Body/api_direction.json", xml.changes, false, null);
        Api(HostAddress + "/api/direction", "post", null, null, body, 200, false);
        Assertions.assertNotEquals(Response.getString("StatusCode"), "400", "Ошибки при создании консультации быть не должно");

        System.out.println("Авторизуемся и переходим созданной консультации");
        AuthorizationMethod(authorizationObject.OKB);

        ClickElement(consultationOU.Consultation);
        ClickElement(consultationUn.DESK);
        Thread.sleep(1500);
        WaitElement(consultationUn.Consuls("Отправлен"));
        System.out.println("Проверка цвета оборудования (голубой)");
        String Color = driver.findElement(consultationUn.ConsulsColumn(ReadPropFile("IdDirection"), "1")).getCssValue("background-color");
        Assertions.assertEquals(Color, "rgba(182, 196, 252, 1)", " Цвет не совпадает с голубым");
    }
}
