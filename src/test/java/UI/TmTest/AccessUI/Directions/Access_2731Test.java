package UI.TmTest.AccessUI.Directions;

import Base.SQL;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

import static UI.TmTest.AccessUI.Directions.Access_2559Test.DateMethod;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Расписание_консультаций")
@Tag("Удалённая_консультация")
@Tag("Квоты_консультации")
public class Access_2731Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    ConsultationScheduleRemote consultationSR;
    DirectionsForQuotas directionsForQuotas;
    Access_2559Test access_2559Test;
    EquipmentSchedule equipmentSchedule;
    public String TomorrowDay;
    public String Time1;
    public String patient_diagnosis;
    public String morasp;
    public String mocons;
    public String mokvot;
    public String for_kid;
    public String lastname;
    public String dateStart;
    public String dateEnd;

    @Issue(value = "TEL-2731")
    @Issue(value = "TEL-2732")
    @Issue(value = "TEL-2872")
    @Link(name = "ТМС-1965", url = "https://team-1okm.testit.software/projects/5/tests/1965?isolatedSection=d46a6069-ce1e-46cc-a7d6-77ce55d402be")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Перезапись нового пациента на консультацию")
    @Description("Переходим в Расписание консультаций - создаём расписание - создаём консультацию через МО1 и записываемся на свободный слот. После нажимаем на него и перезаписываем другого пациента")
    public void Access_2731() throws SQLException, InterruptedException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        access_2559Test = new Access_2559Test();
        equipmentSchedule = new EquipmentSchedule(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);

        System.out.println("1 проверка - записываем пациента на свободный слот и после перезаписываем в этом же слоте");
        ClickElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));
        System.out.println("Выбрали свободный слот");
        WaitElement(directionsForQuotas.BigSnils);
        driver.findElement(directionsForQuotas.BigSnils).sendKeys(PSnils_);
        ClickElement(directionsForQuotas.SearchWait("1"));
        ClickElement(directionsForQuotas.listPatientFirst2);

        ClickElement(directionsForQuotas.Diagnose);
        inputWord(driver.findElement(directionsForQuotas.DiagnoseSearch), "AA");
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.DiagnoseSearchSave);
        ClickElement(directionsForQuotas.WriteConsul);
        Thread.sleep(2500);

        Access_2732BD("A00-A09", PMORequest, PMORequest, "null", "null", PLastNameGlobal);

        System.out.println("Берём время слота");
        String time = consultationSR.GetTimeConsul();

        System.out.println("Нажимаем на тот же слот");
        ClickElement(consultationSR.SlotDisableCons(consultationSR.NumberColumn, time));
        ClickElement(consultationSR.Refresh);
        WaitElement(directionsForQuotas.BigSnils);
        driver.findElement(directionsForQuotas.BigSnils).sendKeys("19253535078");
        ClickElement(directionsForQuotas.SearchWait("1"));
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);
        ClickElement(directionsForQuotas.listPatientFirst2);

        ClickElement(directionsForQuotas.Diagnose);
        inputWord(driver.findElement(directionsForQuotas.DiagnoseSearch), "AA");
        ClickElement(authorizationObject.SelectSecond);
        ClickElement(directionsForQuotas.DiagnoseSearchSave);
        ClickElement(directionsForQuotas.ChangePatient);
        Thread.sleep(2500);

        Access_2732BD("A00", PMORequest, PMORequest, "null", "null", "Пользователь");

        System.out.println(
                "2 проверка - записываем пациента на свободный слот через другую МО и после пробуем перезаписать в этом же слоте");

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

        Access_2732BD("A00-A09", PMORequest, PMOTarget, "null", "null", "Тестировщик");

        System.out.println("Берём время слота");
        time = consultationSR.GetTimeConsul();

        System.out.println("Нажимаем на тот же слот");
        ClickElement(consultationSR.SlotDisableCons(consultationSR.NumberColumn, time));
        ClickElement(consultationSR.Refresh);
        WaitElement(directionsForQuotas.BigSnils);
        driver.findElement(directionsForQuotas.BigSnils).sendKeys("19253535078");
        ClickElement(directionsForQuotas.SearchWait("1"));
        ClickElement(directionsForQuotas.listPatientFirst2);

        ClickElement(directionsForQuotas.Diagnose);
        inputWord(driver.findElement(directionsForQuotas.DiagnoseSearch), "AA");
        ClickElement(authorizationObject.SelectSecond);
        ClickElement(directionsForQuotas.DiagnoseSearchSave);
        ClickElement(directionsForQuotas.ChangePatient);
        Thread.sleep(2500);

        Access_2732BD("A00", PMORequest, PMOTarget, "null", "null", "Пользователь");

        System.out.println("Авторизуемся под МО1 и проверяем что через неё данный слот нельзя перезаписать (2872) ");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);
        SelectClickMethod(consultationSR.Selected("Выберите мед. организацию"),
                authorizationObject.Select("БУ ХМАО-Югры \"Окружная клиническая больница\""));

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);

        System.out.println("Нажимаем на тот же слот");
        ClickElement(consultationSR.SlotDisableCons(consultationSR.NumberColumn, time));
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Refresh, 3);

        System.out.println("3 проверка - добавляем квоту 'Для ребёнка' и перезаписываем пациента");

        System.out.println("Расчёт нужной даты");
        TomorrowDay = access_2559Test.Date_2559(false);
        System.out.println(TomorrowDay);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);
        ClickElement(consultationSR.ConsultationSchedule);

        System.out.println("Ищем последнюю созданную запись в БД, чтобы найти её по дате на вебе");
        sql.StartConnection(
                "select c.id, c.userid, u.fname, u.sname, u.mname, p.\"name\", con.purpose , c.datefrom, c.dateto, msm.namemu from consultationslots c\n" +
                        "join telmed.users u on c .userid = u.id\n" +
                        "join telmed.profiledirectory p on c.profile = p.id\n" +
                        "join dpc.mis_sp_mu msm on c.medicalidmu = msm.medicalidmu\n" +
                        "join telmed.consultationpurpose con on c.purpose = con.id\n" +
                        "where u.sname = 'Зотин' order by c.dateto desc limit 1;");
        while (sql.resultSet.next()) {
            dateStart = sql.resultSet.getString("datefrom");
            dateEnd = sql.resultSet.getString("dateto");
        }

        System.out.println("Нажимаем редактировать у нужного консультанта");
        ClickElement(consultationSR.UserEdit("Андрей Зотин Владимирович",
                DateMethod(dateStart) + " - " + DateMethod(dateEnd)));

        System.out.println("Переходим в Распределение квот");
        ClickElement(consultationSR.Kvots);

        System.out.println("Ищем завтрашнее число");
        ClickElement(equipmentSchedule.Next);
        Integer number = 0;
        while (!isElementVisibleTime(equipmentSchedule.KvotsData(TomorrowDay), 1) & number < 30) {
            number++;
            ClickElement(equipmentSchedule.Next);
        }

        System.out.println("Выбираем 1 доступную квоту с завтрашним числом");
        equipmentSchedule.CheckKvot(TomorrowDay);

        System.out.println("Выбираем чек бокс Только для ребёнка");
        ClickElement(By.xpath(
                "((//tr/td[contains(.,'" + TomorrowDay + "')])[" + equipmentSchedule.trueNumber + "]/following-sibling::td[4]/div/span//span)[1]"));

        System.out.println("Берём время квоты");
        Time1 = driver.findElement(consultationSR.KvotsTime(TomorrowDay, String.valueOf(equipmentSchedule.trueNumber))).getText();
        InputPropFile("value_2731_time1", Time1);

        ClickElement(consultationSR.EditSave);
        Thread.sleep(1500);

        System.out.println("Повторно открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);

        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                access_2559Test.TimeMethod(ReadPropFile("value_2731_time1"), false,
                        false)));

        System.out.println("Выбрали свободный слот");
        WaitElement(directionsForQuotas.BigSnils);
        driver.findElement(directionsForQuotas.BigSnils).sendKeys("07400899262");
        ClickElement(directionsForQuotas.SearchWait("1"));
        Thread.sleep(1500);
        ClickElement(directionsForQuotas.listPatientFirst2);

        ClickElement(directionsForQuotas.Diagnose);
        inputWord(driver.findElement(directionsForQuotas.DiagnoseSearch), "AA");
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.DiagnoseSearchSave);
        ClickElement(directionsForQuotas.WriteConsul);
        Thread.sleep(1500);

        WaitNotElement3(consultationSR.SlotKidsError("Данный слот распределен только для записи ребенка (младше 18 лет)"), 3);
        Thread.sleep(3000);

        Access_2732BD("A00-A09", PMORequest, PMORequest, "null", "t", "Возраст");

        System.out.println("Повторно открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);

        System.out.println("Нажимаем на тот же слот");
        ClickElement(consultationSR.SlotDisableCons(consultationSR.NumberColumn,
                access_2559Test.TimeMethod(ReadPropFile("value_2731_time1"), false,
                        false)));

        ClickElement(consultationSR.Refresh);
        WaitElement(directionsForQuotas.BigSnils);
        driver.findElement(directionsForQuotas.BigSnils).sendKeys("15979025720");
        ClickElement(directionsForQuotas.SearchWait("1"));
        ClickElement(directionsForQuotas.listPatientFirst2);

        ClickElement(directionsForQuotas.Diagnose);
        inputWord(driver.findElement(directionsForQuotas.DiagnoseSearch), "AA");
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.DiagnoseSearchSave);
        ClickElement(directionsForQuotas.ChangePatient);
        Thread.sleep(1500);

        WaitElement(consultationSR.SlotKidsError("Данный слот распределен только для записи ребенка (младше 18 лет)"));
    }

    @Step("Проверяем БД после перезаписи пациента на слот")
    public void Access_2732BD(String diagnosis, String Morasp, String Mocons, String Mokvot, String For_kid, String name) throws SQLException {

        SQL sql = new SQL();

        System.out.println("Проверяем в БД");
        sql.StartConnection(
                "SELECT cs.id, cs.userid, u.fname, u.sname, u.mname, cs.datefrom, cs.dateto, cts.patient_diagnosis, msm.namemu morasp, msm2.namemu mocons, msm3.namemu mokvot,\n" +
                        "ct.id , ct.starttime, ct.endtime, ct.for_kid, cts.patient_id, opr.lastname, opr.firstname, opr.middlename FROM telmed.consultationstakenslots cts\n" +
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
                        "order by cts.id desc limit 1;");
        while (sql.resultSet.next()) {
            patient_diagnosis = sql.resultSet.getString("patient_diagnosis");
            morasp = sql.resultSet.getString("morasp");
            mocons = sql.resultSet.getString("mocons");
            mokvot = sql.resultSet.getString("mokvot");
            for_kid = sql.resultSet.getString("for_kid");
            lastname = sql.resultSet.getString("lastname");
        }
        Assertions.assertEquals(patient_diagnosis, diagnosis, "Диагноз не совпадает");
        Assertions.assertEquals(morasp, Morasp, "МО расписания не совпадает");
        Assertions.assertEquals(mocons, Mocons, "МО консультации не совпадает");
        if (Mokvot.contains("null")) {
            Assertions.assertEquals(mokvot, null, "МО квоты не совпадает");
        } else {
            Assertions.assertEquals(mokvot, Mokvot, "МО квоты не совпадает");
        }

        if (For_kid.contains("null")) {
            Assertions.assertEquals(for_kid, null, "Чек бокс 'Для детей' не совпадает");
        } else {
            Assertions.assertEquals(for_kid, For_kid, "Чек бокс 'Для детей' не совпадает");
        }

        Assertions.assertEquals(lastname, name, "Фамилия пациента не совпадает");
    }
}
