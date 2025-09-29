package UI.TmTest.AccessUI.Directions;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingArchived;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import Base.BaseAPI;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Расписание_консультаций")
@Tag("Удалённая_консультация")
public class Access_2886Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    ConsultationScheduleRemote consultationSR;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;
    ConsultationOutgoingArchived consultationOA;
    public String idSlots;
    public String idDirection;


    @Issue(value = "TEL-2886")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Удаление записи на слоты после отмены консультации")
    @Description("Переходим в Расписание консультаций - создаём расписание - создаём консультацию через МО1 и  записываемся на слот. После переходим в исходящие незавершённые и Отменяем запрос консультации, проверяем что слот в таблице telmed.consultationtakenslots удалился")
    public void Access_2886() throws InterruptedException, SQLException, IOException {

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

        System.out.println("Закрываем создание и переходим в исходящие незавершённые");
        ClickElement(directionsForQuotas.CloseReception);
        ClickElement(consultationOU.Consultation);
        ClickElement(consultationOU.SortDesc);
        ClickElement(consultationOU.ConsultationFirst);
        WaitElement(consultationOU.NumberConsultation);
        idDirection = driver.findElement(consultationOU.NumberConsultation).getText().substring(20);
        ClickElement(consultationOU.AddReception);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);

        ClickElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));
        WaitElement(consultationOU.Button("Завершить консультацию"));
        System.out.println("Запись на прием успешна создана!");
        Thread.sleep(2000);

        System.out.println("Проверяем что появилась запись в telmed.consultationtakenslots");
        int number = 0;
        while (idSlots == null & number < 20) {
            if (number > 0) {
                sql.PrintSQL = false;
            }
            sql.StartConnection(
                    "SELECT cs.id, cs.userid, u.fname, u.sname, u.mname, cs.datefrom, cs.dateto, cts.patient_diagnosis, msm.namemu morasp, msm2.namemu mocons, msm3.namemu mokvot,\n" +
                            "ct.id , ct.starttime, ct.endtime, ct.for_kid, cts.patient_id, opr.lastname, opr.firstname, opr.middlename, cts.id idslots FROM telmed.consultationstakenslots cts\n" +
                            "join telmed.consultationslotstime ct on cts.idtimeinterval = ct.id\n" +
                            "join telmed.consultationslots cs on ct.scheduleid = cs.id\n" +
                            "join telmed.users u on cs.userid = u.id\n" +
                            "join telmed.profiledirectory p on cs.profile = p.id\n" +
                            "join dpc.mis_sp_mu msm on cs.medicalidmu = msm.medicalidmu\n" +
                            "left join dpc.mis_sp_mu msm2 on cts.medicalidmu = msm2.medicalidmu\n" +
                            "left join dpc.mis_sp_mu msm3 on ct.distributeto_medicalidmu = msm3.medicalidmu\n" +
                            "left join telmed.directions d on cts.iddirection = d.id\n" +
                            "left join dpc.mis_sp_mu msm4 on d.targetmoid = msm4.idmu\n" +
                            "left join iemk.op_patient_reg opr on cts.patient_id = opr.patient_id\n" +
                            "where d.id = '"+idDirection+"' order by cts.id desc limit 1;");
            while (sql.resultSet.next()) {
                idSlots = sql.resultSet.getString("idslots");
                System.out.println("Номер слота " + idSlots);
            }
            number++;
        }
        sql.PrintSQL = true;

        System.out.println("Отменяем запрос");
        ClickElement(consultationOU.Button("Отменить запрос"));
        String uuid = String.valueOf(UUID.randomUUID());
        WaitElement(consultationOU.ReasonCancellation);
        inputWord(driver.findElement(consultationOU.ReasonCancellation), uuid + " ");
        ClickElement(consultationOU.ButtonCancellation);
        Thread.sleep(2500);
        authorizationObject.LoadingTime(20);
        WaitElement(consultationOU.StatusConsultation);
        driver.findElement(consultationOU.StatusConsultation).getText().contains("Запрос отменен");
        Thread.sleep(3000);

        System.out.println("Проверяем что удалилась запись в telmed.consultationtakenslots");
        String idSlots2 = null;
        sql.StartConnection(
                "SELECT cs.id, cs.userid, u.fname, u.sname, u.mname, cs.datefrom, cs.dateto, cts.patient_diagnosis, msm.namemu morasp, msm2.namemu mocons, msm3.namemu mokvot,\n" +
                        "ct.id , ct.starttime, ct.endtime, ct.for_kid, cts.patient_id, opr.lastname, opr.firstname, opr.middlename, cts.id idslots FROM telmed.consultationstakenslots cts\n" +
                        "join telmed.consultationslotstime ct on cts.idtimeinterval = ct.id\n" +
                        "join telmed.consultationslots cs on ct.scheduleid = cs.id\n" +
                        "join telmed.users u on cs.userid = u.id\n" +
                        "join telmed.profiledirectory p on cs.profile = p.id\n" +
                        "join dpc.mis_sp_mu msm on cs.medicalidmu = msm.medicalidmu\n" +
                        "left join dpc.mis_sp_mu msm2 on cts.medicalidmu = msm2.medicalidmu\n" +
                        "left join dpc.mis_sp_mu msm3 on ct.distributeto_medicalidmu = msm3.medicalidmu\n" +
                        "left join telmed.directions d on cts.iddirection = d.id\n" +
                        "left join dpc.mis_sp_mu msm4 on d.targetmoid = msm4.idmu\n" +
                        "left join iemk.op_patient_reg opr on cts.patient_id = opr.patient_id\n" +
                        "where cts.id = '"+idSlots+"';");
        while (sql.resultSet.next()) {
            idSlots2 = sql.resultSet.getString("idslots");
        }
        Assertions.assertNull(idSlots2, "Запись не удалилась из таблицы telmed.consultationtakenslots");
    }

    @Issue(value = "TEL-2880")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Flaky
    @DisplayName("Удаление записи на слоты после отмены консультации в расписании консультаций")
    @Description("Переходим в Расписание консультаций - создаём расписание - создаём консультацию через МО1 и  записываемся на слот. После переходим в расписание консультаций и отменяем запись, проверяем что слот в таблице telmed.consultationtakenslots удалился. После записываемся через МО2 и переходим в МО1 - пробуем отменить запись - нельзя отменить запись, можно только через МО1")
    public void Access_2880() throws InterruptedException, SQLException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        consultationOA = new ConsultationOutgoingArchived(driver);
        Access_2731Test access2731Test = new Access_2731Test();

        String Patient = "Тестировщик Т. Т.";

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходм в создание удалённой консультации");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        directionsForQuotas.CreateRemoteConsul(true, "", "159 790 257 20", "Зотин Андрей Владимирович",
                "Женская консультация", "детской урологии-андрологии", "плановая", "Подозрение на COVID-19",
                "Паратиф A");

        System.out.println("Берём id записи");
        sql.StartConnection("Select * from telmed.directions order by id desc limit 1;");
        while (sql.resultSet.next()) {
            idDirection = sql.resultSet.getString("id");
        }

        System.out.println("Закрываем создание и переходим в исходящие незавершённые");
        ClickElement(directionsForQuotas.CloseReception);
        ClickElement(consultationOU.Consultation);
        ClickElement(consultationOU.SortDesc);
        ClickElement(consultationOU.Line(idDirection));
        ClickElement(consultationOU.AddReception);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);

        ClickElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));
        WaitElement(consultationOU.Button("Завершить консультацию"));
        System.out.println("Запись на прием успешна создана!");
        Thread.sleep(2000);

        System.out.println("Берём время слота");
        String time = consultationSR.GetTimeConsul();

        System.out.println("Проверяем что появилась запись в telmed.consultationtakenslots");
        int number = 0;
        while (idSlots == null & number < 20) {
            sql.StartConnection(
                    "SELECT cs.id, cs.userid, u.fname, u.sname, u.mname, cs.datefrom, cs.dateto, cts.patient_diagnosis, msm.namemu morasp, msm2.namemu mocons, msm3.namemu mokvot,\n" +
                            "ct.id , ct.starttime, ct.endtime, ct.for_kid, cts.patient_id, opr.lastname, opr.firstname, opr.middlename, cts.id idslots FROM telmed.consultationstakenslots cts\n" +
                            "join telmed.consultationslotstime ct on cts.idtimeinterval = ct.id\n" +
                            "join telmed.consultationslots cs on ct.scheduleid = cs.id\n" +
                            "join telmed.users u on cs.userid = u.id\n" +
                            "join telmed.profiledirectory p on cs.profile = p.id\n" +
                            "join dpc.mis_sp_mu msm on cs.medicalidmu = msm.medicalidmu\n" +
                            "left join dpc.mis_sp_mu msm2 on cts.medicalidmu = msm2.medicalidmu\n" +
                            "left join dpc.mis_sp_mu msm3 on ct.distributeto_medicalidmu = msm3.medicalidmu\n" +
                            "left join telmed.directions d on cts.iddirection = d.id\n" +
                            "left join dpc.mis_sp_mu msm4 on d.targetmoid = msm4.idmu\n" +
                            "left join iemk.op_patient_reg opr on cts.patient_id = opr.patient_id\n" +
                            "where d.id = '"+idDirection+"' order by cts.id desc limit 1;");
            while (sql.resultSet.next()) {
                idSlots = sql.resultSet.getString("idslots");
                System.out.println("Номер слота " + idSlots);
            }
            number++;
        }

        System.out.println("Открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);
        Thread.sleep(2000);
        authorizationObject.LoadingTime(30);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);

        System.out.println("Нажимаем на занятый слот");
        WebElement element = driver.findElement(consultationSR.SlotDisableCons(consultationSR.NumberColumn, time));
        actions.moveToElement(element);
        actions.perform();
        Scroll("400");
        Thread.sleep(2500);
        ClickElement(consultationSR.SlotDisableCons(consultationSR.NumberColumn, time));
        Thread.sleep(2500);
        number = 0;
        while (!isElementNotVisible(consultationSR.ClosePatient) & number < 5) {
            ClickElement(consultationSR.SlotDisableCons(consultationSR.NumberColumn, time));
            number++;
        }
        ClickElement(consultationSR.ClosePatient);
        ClickElement(consultationSR.ClosePatientAdd);

        System.out.println("Переходим в исходящие архивные и проверяем статус");
        ClickElement(consultationOA.OutgoingArchived);
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 30);
        ClickElement(consultationOU.SortDesc);
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 30);
        ClickElement(consultationOU.Line(idDirection));
        WaitElement(consultationOU.StatusConsultation);
        driver.findElement(consultationOU.StatusConsultation).getText().contains("Запрос отменен");
        Thread.sleep(1500);

        System.out.println("Проверяем что удалилась запись в telmed.consultationtakenslots");
        String idSlots2 = "1";
        number = 0;
        while (!idSlots2.equals("0") & number < 20) {
            sql.StartConnection(
                    "SELECT count(*) FROM telmed.consultationstakenslots cts\n" +
                            "join telmed.consultationslotstime ct on cts.idtimeinterval = ct.id\n" +
                            "join telmed.consultationslots cs on ct.scheduleid = cs.id\n" +
                            "join telmed.users u on cs.userid = u.id\n" +
                            "join telmed.profiledirectory p on cs.profile = p.id\n" +
                            "join dpc.mis_sp_mu msm on cs.medicalidmu = msm.medicalidmu\n" +
                            "left join dpc.mis_sp_mu msm2 on cts.medicalidmu = msm2.medicalidmu\n" +
                            "left join dpc.mis_sp_mu msm3 on ct.distributeto_medicalidmu = msm3.medicalidmu\n" +
                            "left join telmed.directions d on cts.iddirection = d.id\n" +
                            "left join dpc.mis_sp_mu msm4 on d.targetmoid = msm4.idmu\n" +
                            "left join iemk.op_patient_reg opr on cts.patient_id = opr.patient_id\n" +
                            "where cts.id = '"+idSlots+"';");
            while (sql.resultSet.next()) {
                idSlots2 = sql.resultSet.getString("count");
            }
            System.out.println(idSlots2);
            number++;
        }
        Assertions.assertEquals(idSlots2, "0", "Запись не удалилась из таблицы telmed.consultationtakenslots");

        System.out.println("2 проверка - записываем пациента через слот, Авторизуемся под другой МО и проверяем, что нельзя отменить");

        System.out.println("Авторизуемся через МО2");
        AuthorizationMethod(authorizationObject.YATCKIV);

        System.out.println("Открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);
        SelectClickMethod(consultationSR.Selected("Выберите мед. организацию"),
                authorizationObject.Select("БУ ХМАО-Югры \"Окружная клиническая больница\""));

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);

        ClickElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));
        System.out.println("Выбрали свободный слот");
        WaitElement(directionsForQuotas.BigSnils);
        driver.findElement(directionsForQuotas.BigSnils).sendKeys("15979025720");
        ClickElement(directionsForQuotas.SearchWait("1"));
        ClickElement(directionsForQuotas.listPatientFirst2);

        ClickElement(directionsForQuotas.Diagnose);
        inputWord(driver.findElement(directionsForQuotas.DiagnoseSearch), "AA");
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.DiagnoseSearchSave);
        ClickElement(directionsForQuotas.WriteConsul);
        Thread.sleep(2500);

        System.out.println("Берём время у этого слота");
        time = consultationSR.GetTimeConsul();

        access2731Test.Access_2732BD("A00-A09", PMORequest, PMOTarget, "null", "null", PLastNameGlobal);

        System.out.println("Авторизуемся под МО1 и проверяем что через неё данный слот нельзя отменить (2880) ");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);
        SelectClickMethod(consultationSR.Selected("Выберите мед. организацию"),
                authorizationObject.Select("БУ ХМАО-Югры \"Окружная клиническая больница\""));

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);

        System.out.println("Нажимаем на тот же слот");
        ClickElement(consultationSR.SlotDisableCons(consultationSR.NumberColumn, time));
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Refresh, 3);
    }
}
