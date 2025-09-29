package UI.TmTest.AccessUI.Directions;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Расписание_консультаций")
@Tag("Удалённая_консультация")
@Tag("Квоты_консультации")
@Tag("Основные")
public class Access_3082Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    ConsultationScheduleRemote consultationSR;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;
    EquipmentSchedule equipmentSchedule;
    public String dateStart;
    public String dateEnd;
    public String TomorrowDay;
    public String Time1;
    public String Time2;

    @Issue(value = "TEL-3081")
    @Issue(value = "TEL-3082")
    @Issue(value = "TEL-3083")
    @Issue(value = "TEL-3084")
    @Issue(value = "TEL-3085")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Распределение квоты на детей до года")
    @Description("Переходим в Расписание консультаций - создаём расписание - в квотах добавляем МО1 и ставим чек бокс Для детей до года - создаём консультацию через МО1 и пробуем записаться на слот с данной квотой с пациентом разного возраста - смотрим валидные ошибки")
    public void Access_3082 () throws SQLException, IOException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        Access_2559Test access_2559Test = new Access_2559Test();

        System.out.println("Расчёт нужной даты");
        TomorrowDay = access_2559Test.Date_2559(false);
        System.out.println( TomorrowDay);

        System.out.println("Обновляем данные у пациента в РРП, чтобы возраст всегда был меньше года");
        //высчитываем нужную дату - ровно 1 месяц назад
        SetDate(0, -1);
        String birthDate = Year + "-" + SetDate(0, -1).substring(0,2) + "-20";
        xml.changes.put("$.BirthDate", birthDate);
        String modifiedJson = JsonMethod("SMS/Body/3082_snils_do_goda.json", xml.changes, false, null);
        PostRRP(modifiedJson);

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
                access_2559Test.DateMethod(dateStart) + " - " + access_2559Test.DateMethod(dateEnd)));

        System.out.println("Переходим в Распределение квот");
        ClickElement(consultationSR.Kvots);

        System.out.println("Ищем завтрашнее число");
        ClickElement(equipmentSchedule.Next);
        Integer number = 0;
        while (!isElementVisibleTime(equipmentSchedule.KvotsData( TomorrowDay), 1) & number < 30) {
            number++;
            ClickElement(equipmentSchedule.Next);
        }

        System.out.println("Выбираем 1 доступную квоту с завтрашним числом");
        equipmentSchedule.CheckKvot(TomorrowDay);

        System.out.println("Выбираем у 1 квоты МО");
        ClickElement(By.xpath(
                "(//tr/td[contains(.,'" +  TomorrowDay + "')])[" +  equipmentSchedule.trueNumber + "]/following-sibling::td[3]/div/span"));
        ClickElement(authorizationObject.Select(PMORequest));

        System.out.println("Выбираем чек бокс Только для ребёнка");
        ClickElement(By.xpath(
                "((//tr/td[contains(.,'"+  TomorrowDay +"')])["+  equipmentSchedule.trueNumber +"]/following-sibling::td[4]/div/span//span)[1]"));

        System.out.println("Проверяем, что при переключении другого чекбокса, второй отключается");
        ClickElement(By.xpath(
                "((//tr/td[contains(.,'"+  TomorrowDay +"')])["+  equipmentSchedule.trueNumber +"]/following-sibling::td[5]/div/span//span)[1]"));

        String Class = driver.findElement(By.xpath(
                "((//tr/td[contains(.,'"+  TomorrowDay +"')])["+  equipmentSchedule.trueNumber +"]/following-sibling::td[4]/div/span//span)[1]")).getAttribute("class");
        Assertions.assertFalse(Class.contains("is-checked"), "Чек бокс для детей должен стать не активным");

        System.out.println("Берём время квоты");
        Time1 = driver.findElement(consultationSR.KvotsTime( TomorrowDay, String.valueOf( equipmentSchedule.trueNumber))).getText();
        InputPropFile("value_3082_time1", Time1);
        System.out.println("Время 1 квоты - " + ReadPropFile("value_3082_time1"));

        /** Дополнительно выбираем ещё один чек бокс - Только для ребёнка, чтобы потом проверить его смену в БД */
        equipmentSchedule.CheckKvot(TomorrowDay);
        ClickElement(By.xpath(
                "((//tr/td[contains(.,'"+  TomorrowDay +"')])["+ equipmentSchedule.trueNumber +"]/following-sibling::td[4]/div/span//span)[1]"));
        Thread.sleep(1500);

        System.out.println("Берём время квоты 2");
        Time2 = driver.findElement(consultationSR.KvotsTime( TomorrowDay, String.valueOf(equipmentSchedule.trueNumber))).getText();
        InputPropFile("value_3082_time2", Time2);
        System.out.println("Время 2 квоты - " + ReadPropFile("value_3082_time2"));

        ClickElement(consultationSR.EditSave);
        Thread.sleep(1500);

        System.out.println("Повторно открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);

        System.out.println("1 проверка - проверяем, что в нужном слоте есть предупреждение - Слот только для записи ребёнка");
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        WaitElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                access_2559Test.TimeMethod(ReadPropFile("value_3082_time1"), false, false)));
        actions.moveToElement(driver.findElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                access_2559Test.TimeMethod(ReadPropFile("value_3082_time1"), false, false))));
        actions.perform();
        WaitElement(consultationSR.SlotKids("Слот только для записи детей до года"));

        System.out.println("2 проверка - пробуем записать пациента с возрастом больше 18 - Данный слот распределен только для записи детей до года");
        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                access_2559Test.TimeMethod(ReadPropFile("value_3082_time1"), false, false)));
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
        Thread.sleep(1500);

        WaitElement(consultationSR.SlotKidsError("Данный слот распределен только для записи детей до года"));

        System.out.println("3 проверка - пробуем записать пациента с возрастом меньше 18, но больше 1 года - Данный слот распределен только для записи детей до года");
        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                access_2559Test.TimeMethod(ReadPropFile("value_3082_time1"), false, false)));
        System.out.println("Выбрали свободный слот");
        WaitElement(directionsForQuotas.BigSnils);
        driver.findElement(directionsForQuotas.BigSnils).sendKeys("42181220227");
        ClickElement(directionsForQuotas.SearchWait("1"));
        ClickElement(directionsForQuotas.listPatientFirst2);

        ClickElement(directionsForQuotas.Diagnose);
        inputWord(driver.findElement(directionsForQuotas.DiagnoseSearch), "AA");
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.DiagnoseSearchSave);
        ClickElement(directionsForQuotas.WriteConsul);
        Thread.sleep(1500);

        WaitElement(consultationSR.SlotKidsError("Данный слот распределен только для записи детей до года"));

        System.out.println("4 проверка - пробуем записать пациента с возрастом меньше года - Ошибок нет");

        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                access_2559Test.TimeMethod(ReadPropFile("value_3082_time1"), false, false)));
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

        WaitNotElement3(consultationSR.SlotKidsError("Данный слот распределен только для записи детей до года"), 3);
        Thread.sleep(3000);

        System.out.println("Проверяем БД для МО2");
        String MOKvots = "";
        String MORasp = "";
        String for_newborn = "";
        sql.StartConnection(
                "SELECT cs.id, cs.userid, u.fname, u.sname, u.mname, cs.datefrom, cs.dateto, cts.patient_diagnosis, msm.namemu morasp, msm2.namemu mocons, msm3.namemu mokvot,\n" +
                        "ct.id , ct.starttime, ct.endtime, ct.for_kid, ct.for_newborn FROM telmed.consultationstakenslots cts\n" +
                        "join telmed.consultationslotstime ct on cts.idtimeinterval = ct.id\n" +
                        "join telmed.consultationslots cs on ct.scheduleid = cs.id\n" +
                        "join telmed.users u on cs.userid = u.id\n" +
                        "join telmed.profiledirectory p on cs.profile = p.id\n" +
                        "join dpc.mis_sp_mu msm on cs.medicalidmu = msm.medicalidmu\n" +
                        "left join dpc.mis_sp_mu msm2 on cts.medicalidmu = msm2.medicalidmu\n" +
                        "left join dpc.mis_sp_mu msm3 on ct.distributeto_medicalidmu = msm3.medicalidmu\n" +
                        "left join telmed.directions d on cts.iddirection = d.id \n" +
                        "left join dpc.mis_sp_mu msm4 on d.targetmoid = msm4.idmu\n" +
                        "order by cts.id desc limit 1;");
        while (sql.resultSet.next()) {
            MOKvots = sql.resultSet.getString("mokvot");
            MORasp = sql.resultSet.getString("morasp");
            for_newborn = sql.resultSet.getString("for_newborn");
            sql.value = sql.resultSet.getString("for_kid");
        }
        Assertions.assertEquals(MOKvots, PMORequest, "МО в квотах не совпадает");
        Assertions.assertEquals(MORasp, PMORequest, "МО в расписании консультации не совпадает");
        Assertions.assertEquals(for_newborn, "t", "Чек бокс для детей до года должен быть активным");
        Assertions.assertEquals(sql.value, "f", "Чек бокс для детей должен быть не активным");

        /** Проверям у второй квоты чек бокс */
        sql.StartConnection(
                "select co.* from telmed.consultationslotstime  co\n" +
                        "join telmed.consultationslots cs on co.scheduleid = cs.id\n" +
                        "join telmed.users u on cs.userid = u.id\n" +
                        "where u.sname = 'Зотин' and starttime = '"+ TomorrowDay.replace(".", "-")+" "+ReadPropFile("value_3082_time2").substring(0,5)+"'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("for_kid");
            for_newborn = sql.resultSet.getString("for_newborn");
        }
        Assertions.assertEquals(sql.value, "t", "Чек бокс для детей должен быть активным");
        Assertions.assertEquals(for_newborn, "f", "Чек бокс для детей до года должен быть не активным");


        System.out.println("Открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);
        ClickElement(consultationSR.ConsultationSchedule);

        System.out.println("Нажимаем редактировать у нужного консультанта");
        ClickElement(consultationSR.UserEdit("Андрей Зотин Владимирович",
                access_2559Test.DateMethod(dateStart) + " - " + access_2559Test.DateMethod(dateEnd)));

        System.out.println("Переходим в Распределение квот");
        ClickElement(consultationSR.Kvots);

        System.out.println("Ищем завтрашнее число");
        ClickElement(equipmentSchedule.Next);
        number = 0;
        while (!isElementVisibleTime(equipmentSchedule.KvotsData( TomorrowDay), 1) & number < 30) {
            number++;
            ClickElement(equipmentSchedule.Next);
        }

        System.out.println("Меняем чек бокс на для детей до года");
        equipmentSchedule.CheckKvotTime(TomorrowDay, ReadPropFile("value_3082_time2"));
        ClickElement(By.xpath(
                "(//tr/td[contains(.,'"+TomorrowDay+"')])/following-sibling::td[1][contains(.,'"+ReadPropFile("value_3082_time2")+"')]/following-sibling::td[4]"));
        Thread.sleep(1500);

        ClickElement(consultationSR.EditSave);
        Thread.sleep(1500);

        /** Проверям у второй квоты чек бокс */
        sql.StartConnection(
                "select co.* from telmed.consultationslotstime  co\n" +
                        "join telmed.consultationslots cs on co.scheduleid = cs.id\n" +
                        "join telmed.users u on cs.userid = u.id\n" +
                        "where u.sname = 'Зотин' and starttime = '"+ TomorrowDay.replace(".", "-")+" "+ReadPropFile("value_3082_time2").substring(0,5)+"'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("for_kid");
            for_newborn = sql.resultSet.getString("for_newborn");
        }
        Assertions.assertEquals(sql.value, "f", "Чек бокс для детей должен быть не активным");
        Assertions.assertEquals(for_newborn, "t", "Чек бокс для детей до года должен быть активным");

        System.out.println("5 проверка - Создаём консультацию и пробуем записать пациента с возрастом больше 18, в слот с ребёнком до года - Данный слот распределен только для записи детей до года");
        System.out.println("Переходм в создание удалённой консультации с пацинтом больше 18 лет");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        directionsForQuotas.CreateRemoteConsul(true, "", "159 790 257 20", "Зотин Андрей Владимирович",
                "Женская консультация", "детской урологии-андрологии", "плановая", "Подозрение на COVID-19",
                "Паратиф A");
        ClickElement(directionsForQuotas.SendReception);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);

        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                access_2559Test.TimeMethod(ReadPropFile("value_3082_time2"), false, false)));
        WaitElement(consultationSR.SlotKidsError("Данный слот распределен только для записи детей до года"));

        System.out.println("6 проверка - Создаём консультацию и пробуем записать пациента с возрастом меньше года, в слот с ребёнокм до года - ошибок нет");
        System.out.println("Переходм в создание удалённой консультации с пациентом меньше года");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        directionsForQuotas.CreateRemoteConsul(true, "", "074 008 992 62", "Зотин Андрей Владимирович",
                "Женская консультация", "детской урологии-андрологии", "плановая", "Подозрение на COVID-19",
                "Паратиф A");
        ClickElement(directionsForQuotas.SendReception);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);

        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                access_2559Test.TimeMethod(ReadPropFile("value_3082_time2"), false, false)));
        WaitNotElement3(consultationSR.SlotKidsError("Данный слот распределен только для записи детей до года"), 3);
        Thread.sleep(3000);
    }
}
