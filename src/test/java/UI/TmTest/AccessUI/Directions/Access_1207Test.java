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
@Tag("Назначение_времени_консультации")
@Tag("Api_Удалённая_консультация")
public class Access_1207Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    ConsultationUnfinished consultationUn;
    ConsultationOutgoingUnfinished consultationOU;
    Authorization authorization;
    SQL sql;

    @Issue(value = "TEL-1207")
    @Issue(value = "TEL-2049")
    @Issue(value = "TEL-3976")
    @Issue(value = "TEL-3975")
    @Link(name = "ТМС-1449", url = "https://team-1okm.testit.software/projects/5/tests/1449?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Link(name = "ТМС-1896", url = "https://team-1okm.testit.software/projects/5/tests/1901?isolatedSection=65a30e47-043a-4dd0-af4f-165d06d17427")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка назначения времени удалённой консультации")
    @Description("Перейти в Консультации - Создать направление - Направление на консультацию - Создаём направление, указывая цель. Переходим в МО, куда отправили - входящие, переходим к ней и назначаем время")
    public void Access_1207() throws InterruptedException, IOException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationUn = new ConsultationUnfinished(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        authorization = new Authorization();
        sql = new SQL();

        System.out.println("Авторизуемся и переходим в создание удалённой консультации");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходм в создание удалённой консультации");
        System.out.println("Отменяем созданную консультацию");
        consultationOU.CancelConsultation(PatientGuid);

        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        xml.changes.put("$.PatientGuid", PatientGuid);
        xml.changes.put("$.TargetMOId", "1.2.643.5.1.13.13.12.2.86.8902");
        xml.changes.put("$.Profile", 19);
        String body = JsonMethod("SMS/Body/api_direction.json", xml.changes, false, null);
        Api(HostAddress + "/api/direction", "post", null, null, body, 200, false);
        Assertions.assertNotEquals(Response.getString("StatusCode"), "400", "Ошибки при создании консультации быть не должно");

        System.out.println("Переходим в консультацию и назначаем время");
        ClickElement(consultationUn.UnfinishedWait);
        ClickElement(consultationUn.FirstWait);
        WaitElement(consultationUn.NumberConsultation);
        String number = driver.findElement(consultationUn.NumberConsultation).getText().substring(20);
        ClickElement(consultationUn.Time);
        ClickElement(consultationUn.AddTime);

        System.out.println("Добавляем время консультации");
        SelectClickMethod(consultationUn.Doctor, authorizationObject.SelectFirst);
        ClickElement(consultationUn.DataDay);
        ClickElement(consultationUn.NextMonth);
        inputWord(driver.findElement(consultationUn.NumberTime), "155");
        ClickElement(consultationUn.AddConsul);
        Thread.sleep(1500);
        ClickElement(consultationUn.DataInTime);
        ClickElement(consultationUn.DataInTimeNextMonth);
        ClickElement(consultationUn.WatchDay);
        WaitElement(consultationUn.TrueConsul("Саферов А. Н."));

        System.out.println("Проверка времени консультации по заявке 2049");
        ClickElement(consultationUn.UnfinishedWait);
        ClickElement(consultationUn.DESK);
        Thread.sleep(1500);

        System.out.println("Проверка времени консультации по заявке 3976");
        sql.StartConnection("select * from telmed.doctorslots where iddirection = '"+number+"';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("begindate");
        }
        ChangeDate(sql.value);
        WaitElement(consultationUn.ConsulsColumn(number, "9"));
        String time = driver.findElement(consultationUn.ConsulsColumn(number, "9")).getText();
        Assertions.assertEquals(time, DaySql + "." + MonthSql + "." + YearSql + " " + sql.value.substring(11, 16), "назначенное время в списке консультаций не совпадает");
        ClickElement(consultationUn.Consuls("Назначено время"));
        WaitElement(consultationUn.TimeConsultation);
    }
}
