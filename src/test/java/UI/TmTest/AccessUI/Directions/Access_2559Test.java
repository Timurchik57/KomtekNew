package UI.TmTest.AccessUI.Directions;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Расписание_консультаций")
@Tag("Удалённая_консультация")
@Tag("Квоты_консультации")
@Tag("Основные")
public class Access_2559Test extends BaseAPI {

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

    @Issue(value = "TEL-2559")
    @Issue(value = "TEL-2777")
    @Link(name = "ТМС-1951", url = "https://team-1okm.testit.software/projects/5/tests/1951?isolatedSection=e0d69503-4fe3-4790-8a4f-f3e055d6d4e0")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Запись на слот у которого есть распределение квот")
    @Description("Переходим в Расписание консультаций - создаём расписание - в квотах добавляем МО1 и МО2 - создаём консультацию через МО1 и МО2 и пробуем записаться в нужный слот + записываемся на 3 слот без квоты. После повторно переходим в Квоты консультации и проверяем, что 3 слот не активен, так как там уже есть записанная консультация")
    public void Access_2559() throws InterruptedException, SQLException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        equipmentSchedule = new EquipmentSchedule(driver);

        System.out.println("Расчёт нужной даты");
        TomorrowDay = Date_2559(false);
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
        Integer number = 0;
        while (!isElementVisibleTime(equipmentSchedule.KvotsData(TomorrowDay), 1) & number < 30) {
            number++;
            ClickElement(equipmentSchedule.Next);
        }

        System.out.println("Выбираем 1 доступную квоту с завтрашним числом");
        equipmentSchedule.CheckKvot(TomorrowDay);

        System.out.println("Выбираем у 1 квоты МО");
        ClickElement(By.xpath(
                "(//tr/td[contains(.,'" + TomorrowDay + "')])[" + equipmentSchedule.trueNumber + "]/following-sibling::td[3]/div/span"));
        ClickElement(authorizationObject.Select(PMOTarget));

        System.out.println("Берём время 1 квоты");
        Time1 = driver.findElement(consultationSR.KvotsTime(TomorrowDay, String.valueOf(equipmentSchedule.trueNumber))).getText();
        InputPropFile("value_2559_time1", Time1);
        System.out.println("Время 1 квоты - " + ReadPropFile("value_2559_time1"));

        System.out.println("Выбираем 2 доступную квоту с завтрашним числом");
        equipmentSchedule.CheckKvot(TomorrowDay);

        System.out.println("Выбираем у 2 квоты МО");
        ClickElement(By.xpath(
                "(//tr/td[contains(.,'" + TomorrowDay + "')])[" + equipmentSchedule.trueNumber + "]/following-sibling::td[3]/div/span"));
        ClickElement(authorizationObject.Select(PMORequest));

        System.out.println("Берём время 2 квоты");
        Time2 = driver.findElement(consultationSR.KvotsTime(TomorrowDay, String.valueOf(equipmentSchedule.trueNumber))).getText();
        InputPropFile("value_2559_time2", Time2);
        System.out.println("Время 2 квоты - " + ReadPropFile("value_2559_time2"));

        ClickElement(consultationSR.EditSave);

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
        ClickElement(consultationOU.AddReception);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");

        ClickElement(consultationSR.AllDay);
        System.out.println(TimeMethod(ReadPropFile("value_2559_time1"), false, false));

        System.out.println("1 проверка - проверяем, что слот с квотой для МО2 недосутпен через МО1");
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        actions.moveToElement(driver.findElement(consultationSR.SlotDisable(
                TimeMethod(ReadPropFile("value_2559_time1"), false, false))));
        actions.perform();
        WaitElement(consultationSR.SlotNotAvailable);

        System.out.println("2 проверка - проверяем, что слот с квотой для МО1 досутпен через МО1");
        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                TimeMethod(ReadPropFile("value_2559_time2"), false, false)));
        WaitElement(consultationOU.Button("Завершить консультацию"));
        System.out.println("Запись на прием успешна создана!");

        System.out.println("Создаём направление на консультацию через МО2");
        if (KingNumber != 4) {
            AuthorizationMethod(authorizationObject.YATCKIV);
        } else {
            AuthorizationMethod(authorizationObject.Kondinsk);
        }

        System.out.println("Переходм в создание удалённой консультации");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        directionsForQuotas.CreateRemoteConsul(false, "БУ ХМАО-Югры \"Окружная клиническая больница\"", "159 790 257 20", PName, "Женская консультация", "детской урологии-андрологии", "плановая", "Подозрение на COVID-19", "Паратиф A");

        System.out.println("Закрываем создание и переходим в исходящие незавершённые");
        ClickElement(directionsForQuotas.CloseReception);
        ClickElement(consultationOU.Consultation);
        ClickElement(consultationOU.SortDesc);
        ClickElement(consultationOU.ConsultationFirst);
        ClickElement(consultationOU.AddReception);

        System.out.println("Выбираем свободный слот");
        consultationSR.CheckConsulRemote("Зотин");
        ClickElement(consultationSR.AllDay);

        System.out.println("3 проверка - проверяем, что слот с квотой для МО2 досутпен через МО2");
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        actions.moveToElement(driver.findElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                TimeMethod(ReadPropFile("value_2559_time1"), false, false))));
        actions.perform();
        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                TimeMethod(ReadPropFile("value_2559_time1"), false, false)));
        WaitElement(consultationOU.Button("Отменить запрос"));
        System.out.println("Запись на прием успешна создана!");

        System.out.println("Проверяем БД для МО2");
        String MOKvots = "";
        String MORasp = "";
        String MOCons = "";
        String startTime = "";
        String endTime = "";
        sql.StartConnection(
                "SELECT cs.id, cs.userid, u.fname, u.sname, u.mname, cs.datefrom, cs.dateto, cts.patient_diagnosis, msm.namemu morasp, msm2.namemu mocons, msm3.namemu mokvot, ct.id , ct.starttime, ct.endtime,  ct.for_kid FROM telmed.consultationstakenslots cts\n" +
                        "join telmed.consultationslotstime ct on cts.idtimeinterval = ct.id\n" +
                        "join telmed.consultationslots cs on ct.scheduleid = cs.id\n" +
                        "join telmed.users u on cs.userid = u.id\n" +
                        "join telmed.profiledirectory p on cs.profile = p.id\n" +
                        "join dpc.mis_sp_mu msm on cs.medicalidmu = msm.medicalidmu\n" +
                        "left join dpc.mis_sp_mu msm2 on cts.medicalidmu = msm2.medicalidmu\n" +
                        "left join dpc.mis_sp_mu msm3 on ct.distributeto_medicalidmu = msm3.medicalidmu\n" +
                        "join telmed.directions d on cts.iddirection = d.id \n" +
                        "left join dpc.mis_sp_mu msm4 on d.targetmoid = msm4.idmu\n" +
                        "order by cts.id desc limit 1;");
        while (sql.resultSet.next()) {
            MOKvots = sql.resultSet.getString("mokvot");
            MORasp = sql.resultSet.getString("morasp");
            MOCons = sql.resultSet.getString("mocons");
            startTime = sql.resultSet.getString("starttime");
            endTime = sql.resultSet.getString("endtime");
        }
        Assertions.assertEquals(MOKvots, PMOTarget, "МО в квотах не совпадает");
        Assertions.assertEquals(MORasp, PMORequest, "МО в расписании консультации не совпадает");

        /** Проверка МО через которую создали консультацию (2777) */
        Assertions.assertEquals(MOCons, PMOTarget, "МО через которую создали консультацию не совпадает");

        Assertions.assertEquals(startTime, Date_2559(true) + " " +TimeMethod(ReadPropFile("value_2559_time1"), true, true) +  ":00", "МО в консультации не совпадает");
        Assertions.assertEquals(endTime, Date_2559(true) + " " +TimeMethod(ReadPropFile("value_2559_time1"), true, false) +  ":00", "МО в консультации не совпадает");

        System.out.println("Проверяем БД для МО1");
        sql.StartConnection(
                "SELECT cs.id, cs.userid, u.fname, u.sname, u.mname, cs.datefrom, cs.dateto, cts.patient_diagnosis, msm.namemu morasp, msm2.namemu mocons, msm3.namemu mokvot, ct.id , ct.starttime, ct.endtime,  ct.for_kid FROM telmed.consultationstakenslots cts\n" +
                        "join telmed.consultationslotstime ct on cts.idtimeinterval = ct.id\n" +
                        "join telmed.consultationslots cs on ct.scheduleid = cs.id\n" +
                        "join telmed.users u on cs.userid = u.id\n" +
                        "join telmed.profiledirectory p on cs.profile = p.id\n" +
                        "join dpc.mis_sp_mu msm on cs.medicalidmu = msm.medicalidmu\n" +
                        "left join dpc.mis_sp_mu msm2 on cts.medicalidmu = msm2.medicalidmu\n" +
                        "left join dpc.mis_sp_mu msm3 on ct.distributeto_medicalidmu = msm3.medicalidmu\n" +
                        "join telmed.directions d on cts.iddirection = d.id \n" +
                        "left join dpc.mis_sp_mu msm4 on d.targetmoid = msm4.idmu\n" +
                        "order by cts.id desc limit 1 offset 1;");
        while (sql.resultSet.next()) {
            MOKvots = sql.resultSet.getString("mokvot");
            MORasp = sql.resultSet.getString("morasp");
            MOCons = sql.resultSet.getString("mocons");
            startTime = sql.resultSet.getString("starttime");
            endTime = sql.resultSet.getString("endtime");
        }
        Assertions.assertEquals(MOKvots, PMORequest, "МО в квотах не совпадает");
        Assertions.assertEquals(MORasp, PMORequest, "МО в расписании консультации не совпадает");

        /** Проверка МО через которую создали консультацию (2777) */
        Assertions.assertEquals(MOCons, PMORequest, "МО через которую создали консультацию не совпадает");

        Assertions.assertEquals(startTime, Date_2559(true) + " " +TimeMethod(ReadPropFile("value_2559_time2"), true, true) +  ":00", "МО в консультации не совпадает");
        Assertions.assertEquals(endTime, Date_2559(true) + " " +TimeMethod(ReadPropFile("value_2559_time2"), true, false) +  ":00", "МО в консультации не совпадает");
    }

    @Issue(value = "TEL-2562")
    @Issue(value = "TEL-2563")
    @Issue(value = "TEL-2573")
    @Issue(value = "TEL-2574")
    @Issue(value = "TEL-2575")
    @Issue(value = "TEL-2626")
    @Link(name = "ТМС-1951", url = "https://team-1okm.testit.software/projects/5/tests/1951?isolatedSection=e0d69503-4fe3-4790-8a4f-f3e055d6d4e0")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Распределение квоты на детей")
    @Description("Переходим в Расписание консультаций - создаём расписание - в квотах добавляем МО1 и ставим чек бокс Для детей - создаём консультацию через МО1 и пробуем записаться на слот с данной квотой с пациентом разного возраста - смотрим валидные ошибки")
    public void Access_2562 () throws SQLException, IOException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);
        equipmentSchedule = new EquipmentSchedule(driver);

        System.out.println("Расчёт нужной даты");
        TomorrowDay = Date_2559(false);
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

        System.out.println("Выбираем у 1 квоты МО");
        ClickElement(By.xpath(
                "(//tr/td[contains(.,'" + TomorrowDay + "')])[" + equipmentSchedule.trueNumber + "]/following-sibling::td[3]/div/span"));
        ClickElement(authorizationObject.Select(PMORequest));

        System.out.println("Выбираем чек бокс Только для ребёнка");
        ClickElement(By.xpath(
                "((//tr/td[contains(.,'"+ TomorrowDay +"')])["+ equipmentSchedule.trueNumber +"]/following-sibling::td[4]/div/span//span)[1]"));

        System.out.println("Берём время квоты");
        Time1 = driver.findElement(consultationSR.KvotsTime(TomorrowDay, String.valueOf(equipmentSchedule.trueNumber))).getText();
        InputPropFile("value_2562_time1", Time1);

        /** Дополнительно выбираем ещё один чек бокс */
        equipmentSchedule.CheckKvot(TomorrowDay);
      //  Integer newInt = equipmentSchedule.trueNumber + 1;
        ClickElement(By.xpath(
                "((//tr/td[contains(.,'"+ TomorrowDay +"')])["+ equipmentSchedule.trueNumber +"]/following-sibling::td[4]/div/span//span)[1]"));
        Thread.sleep(1500);

        System.out.println("Берём время квоты 2");
        Time2 = driver.findElement(consultationSR.KvotsTime(TomorrowDay, String.valueOf(equipmentSchedule.trueNumber))).getText();
        InputPropFile("value_2562_time2", Time2);

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
                TimeMethod(ReadPropFile("value_2562_time1"), false, false)));
        actions.moveToElement(driver.findElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                TimeMethod(ReadPropFile("value_2562_time1"), false, false))));
        actions.perform();
        WaitElement(consultationSR.SlotKids("Слот только для записи ребенка"));

        System.out.println("2 проверка - пробуем записать пациента с возрастом больше 18 - Ошибка Данный слот распределен только для записи ребенка (младше 18 лет)");
        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                TimeMethod(ReadPropFile("value_2562_time1"), false, false)));
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

        WaitElement(consultationSR.SlotKidsError("Данный слот распределен только для записи ребенка (младше 18 лет)"));

        System.out.println("3 проверка - пробуем записать пациента с возрастом меньше 18 - Ошибок нет");
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                TimeMethod(ReadPropFile("value_2562_time1"), false, false)));
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

        System.out.println("Проверяем БД для МО2");
        String MOKvots = "";
        String MORasp = "";
        sql.StartConnection(
                "SELECT cs.id, cs.userid, u.fname, u.sname, u.mname, cs.datefrom, cs.dateto, cts.patient_diagnosis, msm.namemu morasp, msm2.namemu mocons, msm3.namemu mokvot,\n" +
                        "ct.id , ct.starttime, ct.endtime,  ct.for_kid FROM telmed.consultationstakenslots cts\n" +
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
            sql.value = sql.resultSet.getString("for_kid");
        }
        Assertions.assertEquals(MOKvots, PMORequest, "МО в квотах не совпадает");
        Assertions.assertEquals(MORasp, PMORequest, "МО в расписании консультации не совпадает");
        Assertions.assertEquals(sql.value, "t", "Чек бокс для детей должен быть активным");

        System.out.println("Переходм в создание удалённой консультации с пациентом больше 18 лет");
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

        System.out.println("4 проверка - Создаём консультацию и пробуем записать пациента с возрастом больше 18, в слот с ребёнокм до 18 - Данный слот распределен только для записи ребенка (младше 18 лет)");
        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                TimeMethod(ReadPropFile("value_2562_time2"), false, false)));
        WaitElement(consultationSR.SlotKidsError("Данный слот распределен только для записи ребенка (младше 18 лет)"));

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

        System.out.println("5 проверка - Создаём консультацию и пробуем записать пациента с возрастом меньше года, в слот с ребёнокм до года - ошибок нет");
        ClickElement(consultationSR.SlotActiveCons(consultationSR.NumberColumn,
                TimeMethod(ReadPropFile("value_2562_time2"), false, false)));
        WaitNotElement3(consultationSR.SlotKidsError("Данный слот распределен только для записи ребенка (младше 18 лет)"), 3);
        Thread.sleep(3000);
    }

    @Step("Метод смены формата даты для распределения квот")
    public static String DateMethod(String str) {
        String dateAll = str.substring(0, 10);
        String year = dateAll.substring(0, 4);
        String month = dateAll.substring(0, dateAll.length() - 3).substring(5);
        String day = dateAll.substring(8);
        String NewDateAll = "" + day + "." + month + "." + year + "";
        return NewDateAll;
    }

    @Step("Метод установки завтрашнего числа")
    public String Date_2559(boolean bd) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, 1);

        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH) + 1;
        Integer day = cal.get(Calendar.DAY_OF_MONTH);

        String monthNew = "";
        String dayNew = "";
        switch (month) {
            case 1: monthNew = "01";
                break;
            case 2: monthNew = "02";
                break;
            case 3: monthNew = "03";
                break;
            case 4: monthNew = "04";
                break;
            case 5: monthNew = "05";
                break;
            case 6: monthNew = "06";
                break;
            case 7: monthNew = "07";
                break;
            case 8: monthNew = "08";
                break;
            case 9: monthNew = "09";
                break;
            default:
                monthNew = String.valueOf(month);
        }
        switch (day) {
            case 1: dayNew = "01";
                break;
            case 2: dayNew = "02";
                break;
            case 3: dayNew = "03";
                break;
            case 4: dayNew = "04";
                break;
            case 5: dayNew = "05";
                break;
            case 6: dayNew = "06";
                break;
            case 7: dayNew = "07";
                break;
            case 8: dayNew = "08";
                break;
            case 9: dayNew = "09";
                break;
            default:
                dayNew = String.valueOf(day);
        }
        if (!bd) {
            String date_2459 = year + "." + monthNew + "." + dayNew;
            return date_2459;
        } else {
            String date_2459 = year + "-" + monthNew + "-" + dayNew;
            return date_2459;
        }
    }

    @Step("Метод смены формата времени для расписания консультаций")
    public String TimeMethod(String str, boolean bd, boolean start) {
        String dateAll = str;
        String dateStart = dateAll.substring(0, dateAll.length() - 8);
        String dateEnd = dateAll.substring(8);
        if (!bd) {
            String finish = "с " + dateStart + " до " + dateEnd;
            return finish;
        } else {
            if (start) {
                return dateStart;
            } else {
                return dateEnd;
            }
        }
    }
}
