package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
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
import java.util.List;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Api_Удалённая_консультация")
@Tag("Проверка_БД")
public class Access_4031Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    ConsultationUnfinished consultationUnfinished;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;
    Authorization authorization;

    @Issue(value = "TEL-4031")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Link(name = "ТМС-2266", url = "https://team-1okm.testit.software/projects/5/tests/2266")
    @DisplayName("Проверка отображения кнопки Добавить/Изменить Врача консультанта")
    @Description("Переходим в Созданную консультацию и проверяем при разных доступах и статусах отображение кнопки Добавить/Изменить Врача консультанта")
    public void Access_4031() throws InterruptedException, SQLException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationUnfinished = new ConsultationUnfinished(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        authorization = new Authorization();

        AddRole(PRole, "Разрешено назначать/изменять врача-консультанта", true);

        System.out.println("Отменяем созданную консультацию");
        consultationOU.CancelConsultation(PatientGuid);

        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        xml.changes.put("$.PatientGuid", PatientGuid);
        xml.changes.put("$.TargetMOId", POidMoTarget);
        xml.changes.put("$.Profile", 19);
        String body = JsonMethod("SMS/Body/api_direction.json", xml.changes, false, null);
        Api(HostAddress + "/api/direction", "post", null, null, body, 200, false);
        Assertions.assertNotEquals(Response.getString("StatusCode"), "400", "Ошибки при создании консультации быть не должно");

        AuthorizationMethod(authorizationObject.Select(PMOTarget));
        ClickElement(consultationUnfinished.UnfinishedWait);
        ClickElement(consultationUnfinished.DESK);
        ClickElement(consultationUnfinished.FirstWait);
        WaitElement(consultationUnfinished.NumberConsultation);
        String Number = driver.findElement(consultationUnfinished.NumberConsultation).getText().substring(20);
        System.out.println(Number);

        System.out.println("1 проверка - Врач не назначен, доступ есть, МО куда отправили, статус НЕ равен 4, 5, 99");
        WaitElement(consultationUnfinished.DoctorAdd);
        SqlUpdate(Number, "14", true, false, true);
        SqlUpdate(Number, "24", true, false, true);

        System.out.println("2 проверка - Врач не назначен, доступ есть, МО куда отправили, статус равен 4, 5, 99");
        SqlUpdateError(Number);

        System.out.println("3 проверка - Врач не назначен, доступа нет, МО куда отправили, статус равен 4, 5, 99");
        AddRole(PRole, "Разрешено назначать/изменять врача-консультанта", false);
        AuthorizationMethod(authorizationObject.Select(PMOTarget));
        Thread.sleep(1500);
        System.out.println(HostAddressWeb + "/direction/consultation/" + Number);
        driver.get(HostAddressWeb + "/direction/consultation/" + Number);
        SqlUpdateError(Number);

        System.out.println("4 проверка - Врач не назначен, доступа нет, МО куда отправили, статус НЕ равен 4, 5, 99");
        SqlUpdate(Number, "3", false, false, true);
        SqlUpdate(Number, "14", false, false, true);
        SqlUpdate(Number, "24", false, false, true);

        System.out.println("5 проверка - Врач назначен, доступ есть, МО куда отправили, статус НЕ равен 4, 5, 99");
        AddRole(PRole, "Разрешено назначать/изменять врача-консультанта", true);
        AuthorizationMethod(authorizationObject.Select(PMOTarget));
        Thread.sleep(1500);
        driver.get(HostAddressWeb + "/direction/consultation/" + Number);
        ClickElement(consultationUnfinished.DoctorAdd);
        ClickElement(consultationUnfinished.DoctorSelect);
        ClickElement(authorizationObject.SelectFirst);
        WaitElement(consultationUnfinished.DoctorSet);
        SqlUpdate(Number, "3", false, true, false);
        SqlUpdate(Number, "14", false, true, false);

        System.out.println("6 проверка - Врач назначен, доступ есть, МО куда отправили, статус равен 4, 5, 99");
        SqlUpdateError(Number);

        System.out.println("7 проверка - Врач назначен, доступ есть, МО окуда отправили, статус равен 4, 5, 99");
        AuthorizationMethod(authorizationObject.OKB);
        Thread.sleep(1500);
        driver.get(HostAddressWeb + "/direction/consultation/" + Number);
        SqlUpdateError(Number);

        System.out.println("8 проверка - Врач назначен, доступ есть, МО окуда отправили, статус НЕ равен 4, 5, 99");
        SqlUpdate(Number, "3", false, false, false);
        SqlUpdate(Number, "14", false, false, false);
        SqlUpdate(Number, "24", false, false, false);
    }

    @Step("Метод для замены статус в бд у направления")
    private void SqlUpdate (String value, String status, boolean add, boolean set, boolean setting) throws SQLException {
        sql.UpdateConnection("update telmed.directions set status = '"+status+"' where id = '"+value+"';");
        driver.navigate().refresh();
        WaitElement(consultationUnfinished.NumberConsultation);
        if (add & setting) {
            WaitElement(consultationUnfinished.DoctorAdd);
        }
        if (!add & setting){
            WaitNotElement3(consultationUnfinished.DoctorAdd, 2);
        }

        if (set & !setting) {
            WaitElement(consultationUnfinished.DoctorSet);
        }
        if (!set & !setting){
            WaitNotElement3(consultationUnfinished.DoctorSet, 2);
        }
    }

    @Step("Метод для проверки, что отображается / не отображается нужная кнопка с ФИО врача, для статусов 4, 5, 99")
    private void SqlUpdateError (String value) throws SQLException {
        List<String> list = List.of("4", "5", "99");
        for (String str : list) {
            sql.UpdateConnection("update telmed.directions set status = '"+str+"' where id = '"+value+"';");
            driver.navigate().refresh();
            WaitElement(consultationUnfinished.NumberConsultation);
            WaitNotElement3(consultationUnfinished.DoctorAdd, 2);
        }
    }
}
