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
@Tag("Удалённая_консультация")
@Tag("Расписание_консультаций")
@Tag("Проверка_БД")
@Tag("Назначение_времени_консультации")
public class Access_2080Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    ConsultationScheduleRemote consultationSR;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;

    @Issue(value = "TEL-2080")
    @Issue(value = "TEL-2094")
    @Issue(value = "TEL-2263")
    @Link(name = "ТМС-1895", url = "https://team-1okm.testit.software/projects/5/tests/1895?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Link(name = "ТМС-1897", url = "https://team-1okm.testit.software/projects/5/tests/1897?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Order(1)
    @Story("Запись на приём Удаленных консультаций")
    @DisplayName("Запись на приём в свободный слот")
    @Description("Переходим в Расписание консультаций - создаём расписание, создаём консультацию и записываемся в свободный слот")
    public void Access_2080() throws InterruptedException, SQLException, IOException {

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

        System.out.println("Выбираем запись на приём");
        Thread.sleep(500);
        ClickElement(directionsForQuotas.SendReception);
        Thread.sleep(2000);

        System.out.println("Выбираем Свободный слот");
        Thread.sleep(1500);
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);
        ClickElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));
        Thread.sleep(2000);
        System.out.println("Выбрали свободный слот");

        System.out.println("Берём номер консультации");
        WaitElement(consultationOU.NumberConsultation);
        String id = driver.findElement(consultationOU.NumberConsultation).getText().substring(20);

        System.out.println("Проверяем, что нет кнопки Назначить время по заявке (2263)");
        WaitNotElement3(consultationOU.Button("Назначить время"), 3);

        sql.StartConnection(
                "SELECT x.id, x.iddirection, op.lastname, op.firstname, op.middlename, c2.id, u.fname, u.sname, u.mname, p.\"name\", c3.purpose FROM telmed.consultationstakenslots x\n" +
                        "join iemk.op_patient_reg op on x.patient_id = op.patient_id \n" +
                        "join telmed.consultationslotstime c on x.idtimeinterval = c.id \n" +
                        "join telmed.consultationslots c2 on c.scheduleid = c2.id \n" +
                        "join telmed.users u on c2.userid = u.id\n" +
                        "join telmed.profiledirectory p on c2.profile = p.id \n" +
                        "join telmed.consultationpurpose c3 on c2.purpose = c3.id \n" +
                        "join telmed.directions d on x.iddirection = d.id\n" +
                        "where x.iddirection = '" + id + "'; ");
        while (sql.resultSet.next()) {
            String fname = sql.resultSet.getString("fname");
            String sname = sql.resultSet.getString("sname");
            String mname = sql.resultSet.getString("mname");
            String name = sql.resultSet.getString("name");
            String purpose = sql.resultSet.getString("purpose");
            String lastname = sql.resultSet.getString("lastname");
            String firstname = sql.resultSet.getString("firstname");
            String middlename = sql.resultSet.getString("middlename");

            Assertions.assertEquals(fname, "Андрей", "Имя консультанта не совпадает");
            Assertions.assertEquals(sname, "Зотин", "Фамилия консультанта не совпадает");
            Assertions.assertEquals(mname, "Владимирович", "Отчество консультанта не совпадает");
            Assertions.assertEquals(name, "детской урологии-андрологии", "Профиль консультанта не совпадает");
            Assertions.assertEquals(purpose, "Подозрение на COVID-19", "Цель консультанта не совпадает");
            Assertions.assertEquals(lastname, "Тестировщик", "Фамилия пациента не совпадает");
            Assertions.assertEquals(firstname, "Тест", "Имя пациента не совпадает");
            Assertions.assertEquals(middlename, "Тестович", "Отчество пациента не совпадает");
        }
    }

    @Test
    @Order(2)
    @Story("Запись на приём Удаленных консультаций")
    @DisplayName("Запись на приём в занятый слот")
    @Description("Переходим в Расписание консультаций - создаём расписание, создаём консультацию и пытаемся записаться в занятый слот")
    public void Access_2080_1() throws InterruptedException, IOException {

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

        System.out.println("Выбираем запись на приём");
        Thread.sleep(500);
        ClickElement(directionsForQuotas.SendReception);
        Thread.sleep(2000);

        System.out.println("Выбираем Свободный слот");
        Thread.sleep(1500);
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);

        ClickElement(consultationSR.SlotsBusyLastTime(consultationSR.NumberColumn));
        Thread.sleep(2000);
        WaitElement(consultationSR.Refresh);
        System.out.println("Выбрали занятый слот - запись не произошла");
    }
}
