package api.Access_Notification;

import Base.*;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;


import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты API")
@Feature("Уведомление с типом 21")
@Tag("Оповещение")
@Tag("Проверка_БД")
@Tag("МосМед")
@Tag("Цами")
@Disabled
public class Access_4112_Type21_Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    ConsultationOutgoingUnfinished consultationOutgoingUnfinished;
    DirectionsForQuotas directionsForQuotas;
    Authorization authorization;
    // Можно проверить только на тесте ХМАО

    @Test
    @Issue(value = "TEL-3622")
    @Issue(value = "TEL-4112")
    @Owner(value = "Галиакберов Тимур")
    @Link(name = "ТМС-2190", url = "https://team-1okm.testit.software/projects/5/tests/2190?isolatedSection=af882452-d09a-4051-af99-3d04bf599188")
    @DisplayName("Проверка уведомления типа 21 после получения результата от ИИ")
    @Description("Создаём направление на диагносику с исследованием из telmed.limitmosmed - меняяем telmed.cami_links.accession_number на валидный (который есть ы ЦАМИ) - ждём когда добавится запись в telmed.mosmedinfo - меняем в нём task_id на тот b7c5f3eb-1782-5bac-9d8e-c6e8973d6a9a, по которому есть запись в telmed.mosmedinforesults - из telmed.mosmedinforesults эту запись удаляем. После того как в telmed.mosmedinforesults запись добавится снова - придёт уведомление")
    public void Access_Type20 () throws IOException, SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        consultationOutgoingUnfinished = new ConsultationOutgoingUnfinished(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        authorization = new Authorization();

        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        xml.changes.put("$.TargetMedicalOid", POidMoRequest);
        xml.changes.put("$.ResearchCode", "A06.20.004");
        xml.changes.put("$.PatientGuid", PatientGuid);
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        String id = Response.getString("Result.DirectionId");

        /**
         1996427
         2014453
         */
        sql.UpdateConnection("Update telmed.cami_links set accession_number = '2014453' where id_direction = '"+id+"';");
        sql.UpdateConnection("Select * from telmed.cami_links where id_direction = '"+id+"';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        Integer number = 0;
        String mosmedinfoid = null;
        while (number < 250 && mosmedinfoid != null) {
            sql.StartConnection("Select m.* from telmed.mosmedinfo m" +
                    "join telmed.cami_links c on m.cami_link_id = c.id" +
                    "where m.cami_link_id = '"+sql.value+"';");
            while (sql.resultSet.next()) {
                mosmedinfoid = sql.resultSet.getString("id");
            }

            if (mosmedinfoid == null) {
                number++;
            }
        }
    }
}
