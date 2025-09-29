package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Расписание_консультаций")
@Tag("Удалённая_консультация")
@Tag("Пациент_в_стационаре")
public class Access_2270Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    ConsultationScheduleRemote consultationSR;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;

    @Issue(value = "TEL-2270")
    @Issue(value = "TEL-3737")
    @Link(name = "ТМС-1896", url = "https://team-1okm.testit.software/projects/5/tests/1896?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Запись на приём из незавершенных консультаций")
    @Description("Переходим в Расписание консультаций - создаём расписание, создаём консультацию и нажимаем закрыть. ОТкрываем консультации - исходящие - созданная консультация. Записываемся на приём")
    public void Access_2270() throws InterruptedException, IOException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходм в создание удалённой консультации");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        directionsForQuotas.CreateRemoteConsul(true, "", "159 790 257 20", "Зотин Андрей Владимирович",
                "Женская консультация", "детской урологии-андрологии", "плановая", "Подозрение на COVID-19",
                "Паратиф A");

        System.out.println("Выбираем закрыть");
        Thread.sleep(500);
        ClickElement(directionsForQuotas.CloseReception);
        Thread.sleep(2000);

        System.out.println("Открываем созданную консультацию");
        ClickElement(consultationOU.Consultation);
        ClickElement(consultationOU.ConsultationFirst);

        // Пациент в стационаре (3737)
        WaitElement(consultationOU.Consul("Пациент в стационаре"));
        String hospital = driver.findElement(consultationOU.Consul("Пациент в стационаре")).getText();
        Assertions.assertEquals(hospital, "Да", "Пациент в стационаре должно быть Да");

        sql.StartConnection("Select * from telmed.directions ORDER BY id DESC limit 1");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("inhospital");
        }
        Assertions.assertEquals( sql.value, "2", "Пациент в стационаре должно быть 2 (Да)");

        ClickElement(consultationOU.AddReception);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        ClickElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));
        Thread.sleep(2000);
        System.out.println("Выбрали свободный слот");
    }
}
