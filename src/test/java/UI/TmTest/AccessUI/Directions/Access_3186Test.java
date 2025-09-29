package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.SQL;
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

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Расписание_консультаций")
@Tag("Проверка_БД")
@Tag("Изменение_консультации")
@Tag("Врач_консультант")
public class Access_3186Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    ConsultationScheduleRemote consultationSR;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;

    String id;

    @Issue(value = "TEL-3186")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Изменение данных в консультации")
    @Description("Переходим в Расписание консультаций - создаём расписание, создаём консультацию и записываемся в свободный слот. После меняем значения и проверяем таблицу telmed.consultationstakenslots")
    public void Access_3186() throws InterruptedException, SQLException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Открываем Расписание консультаций - График консультаций - добавляем расписание");
        ClickElement(consultationSR.ConsultationScheduleremote);
        ClickElement(consultationSR.ConsultationSchedule);
        consultationSR.AddConsul("Зотин Андрей Владимирович", "детской урологии-андрологии", "Подозрение на COVID-19",
                consultationSR.Tomorrow, consultationSR.DateLastMonth, "6:00", "21:00", "70");

        /** Добавляем консультацию и записываемся на слот */
        Method_3186_AddConsul();

        System.out.println("\n1 Проверка - меняем МО консультации, после должна удалиться запись из telmed.consultationstakenslots");
        ClickElement(consultationOU.Button("Внести правки в ТМК"));
        ClickElement(directionsForQuotas.NextWait);
        SelectClickMethod(directionsForQuotas.MOWait, authorizationObject.Select("БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\""));
        SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.SelectFirst);
        inputWord(directionsForQuotas.Diagnos, "Паратиф A");
        Thread.sleep(1000);
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.NextConsul);
        ClickElement(directionsForQuotas.SetConsul);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(20);

        Method_3186(id);
        while (sql.resultSet.next()) {
            String iddirection = sql.resultSet.getString("iddirection");
            Assertions.assertNull(iddirection, "Консультация должна удалиться из telmed.consultationstakenslots");
        }

        System.out.println("\n2 Проверка - меняем Врач консультант, после должна удалиться запись из telmed.consultationstakenslots");

        /** Добавляем консультацию и записываемся на слот */
        Method_3186_AddConsul();

        ClickElement(consultationOU.Button("Внести правки в ТМК"));
        ClickElement(directionsForQuotas.NextWait);
        Thread.sleep(1500);
        ClickElement(directionsForQuotas.SelectField("Врач консультант"));
        inputWord(directionsForQuotas.Diagnos, "Паратиф A");
        Thread.sleep(1000);
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.NextConsul);
        ClickElement(directionsForQuotas.SetConsul);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(20);

        Method_3186(id);
        while (sql.resultSet.next()) {
            String iddirection = sql.resultSet.getString("iddirection");
            Assertions.assertNull(iddirection, "Консультация должна удалиться из telmed.consultationstakenslots");
        }

        System.out.println("\n3 Проверка - меняем Профиль, после должна удалиться запись из telmed.consultationstakenslots");

        /** Добавляем консультацию и записываемся на слот */
        Method_3186_AddConsul();

        ClickElement(consultationOU.Button("Внести правки в ТМК"));
        ClickElement(directionsForQuotas.NextWait);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(20);
        SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.Select("акушерскому делу"));
        inputWord(directionsForQuotas.Diagnos, "Паратиф A");
        Thread.sleep(1000);
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.NextConsul);
        ClickElement(directionsForQuotas.SetConsul);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(20);

        Method_3186(id);
        while (sql.resultSet.next()) {
            String iddirection = sql.resultSet.getString("iddirection");
            Assertions.assertNull(iddirection, "Консультация должна удалиться из telmed.consultationstakenslots");
        }
    }

    @Step("Метод создания консультации и записи на слот")
    public String Method_3186_AddConsul () throws SQLException, InterruptedException, IOException {
        sql = new SQL();

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
        ClickElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));
        Thread.sleep(2000);
        System.out.println("Выбрали свободный слот");

        System.out.println("Берём номер консультации");
        WaitElement(consultationOU.NumberConsultation);
        id = driver.findElement(consultationOU.NumberConsultation).getText().substring(20);

        Method_3186(id);
        while (sql.resultSet.next()) {
            String iddirection = sql.resultSet.getString("iddirection");
            Assertions.assertEquals(id, iddirection, "Консультация не добавилась в telmed.consultationstakenslots");
        }
        return id;
    }

    @Step("Запрос в бд с добавлением записи в telmed.consultationstakenslots")
    public void Method_3186 (String id) throws SQLException {
        sql = new SQL();
        sql.StartConnection("SELECT x.id, x.iddirection, d.status, d.executorid, op.lastname, op.firstname, op.middlename, c2.id, u.fname, u.sname, u.mname, p.\"name\", c3.purpose FROM telmed.consultationstakenslots x\n" +
                "join iemk.op_patient_reg op on x.patient_id = op.patient_id \n" +
                "join telmed.consultationslotstime c on x.idtimeinterval = c.id \n" +
                "join telmed.consultationslots c2 on c.scheduleid = c2.id \n" +
                "join telmed.users u on c2.userid = u.id\n" +
                "join telmed.profiledirectory p on c2.profile = p.id \n" +
                "join telmed.consultationpurpose c3 on c2.purpose = c3.id \n" +
                "join telmed.directions d on x.iddirection = d.id\n" +
                "where x.iddirection = '" + id + "'; ");
    }
}
