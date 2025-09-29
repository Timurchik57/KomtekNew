package UI.TmTest.AccessUI.Directions;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import Base.BaseAPI;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Расписание_оборудования")
@Tag("Консультация_на_оборудование")
@Tag("Квоты_оборудования")
@Tag("Api_Направление_на_диагностику")
public class Access_2459Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    EquipmentSchedule equipmentSchedule;
    DirectionsForQuotas directionsForQuotas;
    Authorization authorization;

    public String Time1;
    public String Time2;
    public String TomorrowDay;

    public String IdConsul1;
    public String IdConsul2;

    @Issue(value = "TEL-2459")
    @Issue(value = "TEL-2460")
    @Issue(value = "TEL-2461")
    @Issue(value = "TEL-2462")
    @Issue(value = "TEL-2463")
    @Issue(value = "TEL-2651")
    @Link(name = "ТМС-1944", url = "https://team-1okm.testit.software/projects/5/tests/1944?isolatedSection=a254a2c9-4e42-4504-9888-1ab573156017")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Квоты оборудования")
    @Order(1)
    @DisplayName("Добавление МО в квоты расписания оборудования")
    @Description("Переходим в Расписание оборудования - переходим в настройки расписания - квоты - добавляем на слот МО1. Создаём направление на диагностику, после пытаемся записать на этот слот МО1 и МО2 - смотрим валидные ошибки")
    public void Access_2459() throws InterruptedException, SQLException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        authorization = new Authorization();

        System.out.println("Расчёт нужной даты");
        GetDate();
        TomorrowDay = Date_2459();

        AuthorizationMethod(authorizationObject.Select(PMOTarget));

        System.out.println("Нажимаем на редактирование созданного оборудования");
        ClickElement(equipmentSchedule.EquipmentSchedulesWait);
        ClickElement(equipmentSchedule.ButtonWorkScheduleWait);
        Thread.sleep(3000);
        equipmentSchedule.ClickEquipment();
        ClickElement(equipmentSchedule.Kvots);

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
        Time1 = driver.findElement(
                equipmentSchedule.KvotsTime(TomorrowDay, String.valueOf(equipmentSchedule.trueNumber))).getText();
        InputPropFile("value_2459_time1", Time1);
        System.out.println("Время 1 квоты - " + ReadPropFile("value_2459_time1"));

        System.out.println("Выбираем 2 доступную квоту с завтрашним числом");
        equipmentSchedule.CheckKvot(TomorrowDay);

        System.out.println("Выбираем у 2 квоты МО");
        ClickElement(By.xpath(
                "(//tr/td[contains(.,'" + TomorrowDay + "')])[" + equipmentSchedule.trueNumber + "]/following-sibling::td[3]/div/span"));
        ClickElement(authorizationObject.Select(PMORequest));

        System.out.println("Берём время 2 квоты");
        Time2 = driver.findElement(
                equipmentSchedule.KvotsTime(TomorrowDay, String.valueOf(equipmentSchedule.trueNumber))).getText();
        InputPropFile("value_2459_time2", Time2);
        System.out.println("Время 2 квоты - " + ReadPropFile("value_2459_time2"));

        ClickElement(equipmentSchedule.Save);
        Thread.sleep(3000);

        System.out.println("Создаём направление на диагностику из МО1 в МО2");
        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");
        xml.changes.put("$.TargetMedicalOid", POidMoTarget);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        String id = Response.getString("Result.DirectionId");
        IdConsul1 = id;

        System.out.println("Переходим в исходящие незавершённые");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.SortDesc);
        ClickElement(directionsForQuotas.GetDirection(id, "1"));
        System.out.println("Прикрепление  файла");
        ClickElement(directionsForQuotas.Addfiles);
        Thread.sleep(1000);
        java.io.File file = new File("src/test/resources/test.txt");
        directionsForQuotas.File.sendKeys(file.getAbsolutePath());
        Thread.sleep(500);
        ClickElement(directionsForQuotas.Clouses);
        ClickElement(directionsForQuotas.AddReception);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(40);

        System.out.println("Запись на приём");
        WaitElement(equipmentSchedule.HeaderEquipmentSchedulesWait);
        equipmentSchedule.CheckDirection(PNameEquipment2);

        ClickElement(directionsForQuotas.WorkTime);

        System.out.println("1 проверка - проверяем, что слот с квотой для МО1 недосутпен через МО2");
        WaitElement(directionsForQuotas.SlotDisable(ReadPropFile("value_2459_time1")));
        actions.moveToElement(driver.findElement(
                directionsForQuotas.SlotDisable(ReadPropFile("value_2459_time1"))));
        actions.perform();
        WaitElement(directionsForQuotas.SlotNotAvailable);

        System.out.println("2 проверка - проверяем, что слот с квотой для МО2 досутпен через МО2");
        ClickElement(directionsForQuotas.SlotActive(ReadPropFile("value_2459_time2")));
        WaitElement(equipmentSchedule.WriteWait);
        if(isElementNotVisible(equipmentSchedule.AddFileWait)) {
            file = new File("src/test/resources/test.txt");
            equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
            Thread.sleep(1500);
        }
        WaitNotElement3(equipmentSchedule.loadingWriteTwo, 30);
        ClickElement(equipmentSchedule.WriteTwo);
        Thread.sleep(1500);
        WaitNotElement3(equipmentSchedule.WriteLoading, 30);
        ClickElement(equipmentSchedule.AlertCloseWait);
        System.out.println("Запись на прием успешна создана!");

        System.out.println("Создаём направление на диагностику в свою МО");
        AuthorizationMethod(authorizationObject.Select(PMOTarget));
        directionsForQuotas.CreateConsultationEquipment(true, "159 790 257 20",
                authorizationObject.Select("Женская консультация"),
                "Аорта",
                true,
                "",
                authorizationObject.Select("A00.0 Холера, вызванная холерным вибрионом 01, биовар cholerae"),
                authorizationObject.Select("HMP01"),
                "50", false, true, false, "");

        sql.StartConnection("Select * from telmed.directions order by id limit 1");
        while(sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        System.out.println("Переходим в исходящие незавершённые");
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.SortDesc);
        ClickElement(directionsForQuotas.SearchID(sql.value));
        WaitElement(directionsForQuotas.NumberConsult);
        Thread.sleep(1500);
        WaitNotElement3(directionsForQuotas.LoadingAddPatient, 30);
        String Number = driver.findElement(directionsForQuotas.NumberConsult).getText();
        IdConsul2 = Number.substring(19);
        ClickElement(directionsForQuotas.AddReception);

        System.out.println("Запись на приём");
        WaitElement(equipmentSchedule.HeaderEquipmentSchedulesWait);
        WaitElement(equipmentSchedule.NextPageWait);
        WaitNotElement3(equipmentSchedule.Loading, 30);
        Thread.sleep(1500);
        ClickElement(equipmentSchedule.NextPageWait);
        Thread.sleep(1500);

        ClickElement(directionsForQuotas.WorkTime);

        System.out.println("3 проверка - проверяем, что слот с квотой для МО1 досутпен через МО1");
        ClickElement(
                directionsForQuotas.SlotActive(ReadPropFile("value_2459_time1")));
        WaitElement(equipmentSchedule.WriteWait);
        if(isElementNotVisible(equipmentSchedule.AddFileWait)) {
            file = new File("src/test/resources/test.txt");
            equipmentSchedule.AddFile.sendKeys(file.getAbsolutePath());
            Thread.sleep(1500);
        }
        WaitNotElement3(equipmentSchedule.loadingWriteTwo, 30);
        ClickElement(equipmentSchedule.WriteTwo);
        Thread.sleep(1500);
        WaitNotElement3(equipmentSchedule.WriteLoading, 30);
        ClickElement(equipmentSchedule.AlertCloseWait);
        System.out.println("Запись на прием успешна создана!");

        System.out.println("Проверяем БД");
        String MO1Kvots = "";
        String MO1Cons = "";
        String MO1Rasp = "";
        String idDirection = "";
        sql.StartConnection(
                "SELECT s.idschedule, s.begindate, s.enddate, m.namemu MoDirection, m2.namemu mokvots, eq.moname morasp, s.iddirection FROM telmed.slots s\n" +
                        "left join telmed.directions d on s.iddirection = d.id\n" +
                        "left join dpc.mis_sp_mu m on d.requestermoid = m.idmu\n" +
                        "left join dpc.mis_sp_mu m2 on s.distributeto_medicalidmu = m2.medicalidmu\n" +
                        "join telmed.schedule sc on s.idschedule = sc.id\n" +
                        "join telmed.equipmentregistry eq on sc.equipmentid = eq.id\n" +
                        "where  eq.model = '" + PNameEquipment2 + "' and s.begindate >= '" + DateMethod(
                        TomorrowDay) + " " + TimeMethod(
                        ReadPropFile("value_2459_time1"),
                        "start") + ":00.000' and s.begindate <= '" + DateMethod(TomorrowDay) + " " + TimeMethod(
                        ReadPropFile("value_2459_time2"),
                        "start") + ":00.000' order by s.iddirection desc limit 1;");
        while (sql.resultSet.next()) {
            MO1Kvots = sql.resultSet.getString("mokvots");
            MO1Cons = sql.resultSet.getString("modirection");
            MO1Rasp = sql.resultSet.getString("morasp");

            /** Дополнитнльная проверка на передачу номера исследования (2651) */
            idDirection = sql.resultSet.getString("iddirection");
        }
        Assertions.assertEquals(MO1Kvots, PMOTarget, "МО в квотах не совпадает");
        Assertions.assertEquals(MO1Cons, PMOTarget, "МО в консультации не совпадает");
        Assertions.assertEquals(idDirection, IdConsul2, "Номер консультации не совпадает");
        Assertions.assertEquals(MO1Rasp, PMOTarget, "МО расписания оборудования не совпадает");

        sql.StartConnection(
                "SELECT s.idschedule, s.begindate, s.enddate, m.namemu MoDirection, m2.namemu mokvots, eq.moname morasp, s.iddirection FROM telmed.slots s\n" +
                        "left join telmed.directions d on s.iddirection = d.id\n" +
                        "left join dpc.mis_sp_mu m on d.requestermoid = m.idmu\n" +
                        "left join dpc.mis_sp_mu m2 on s.distributeto_medicalidmu = m2.medicalidmu\n" +
                        "join telmed.schedule sc on s.idschedule = sc.id\n" +
                        "join telmed.equipmentregistry eq on sc.equipmentid = eq.id\n" +
                        "where eq.model = '" + PNameEquipment2 + "' and s.begindate >= '" + DateMethod(
                        TomorrowDay) + " " + TimeMethod(
                        ReadPropFile("value_2459_time1"),
                        "start") + ":00.000' and s.begindate <= '" + DateMethod(TomorrowDay) + " " + TimeMethod(
                        ReadPropFile("value_2459_time2"),
                        "start") + ":00.000' order by s.iddirection desc limit 1 offset 1;");
        while (sql.resultSet.next()) {
            MO1Kvots = sql.resultSet.getString("mokvots");
            MO1Cons = sql.resultSet.getString("modirection");
            MO1Rasp = sql.resultSet.getString("morasp");
            idDirection = sql.resultSet.getString("iddirection");
        }
        Assertions.assertEquals(MO1Kvots, PMORequest, "МО в квотах не совпадает");
        Assertions.assertEquals(MO1Cons, PMORequest, "МО в консультации не совпадает");
        Assertions.assertEquals(MO1Rasp, PMOTarget, "МО расписания оборудования не совпадает");

        /** Дополнитнльная проверка на передачу номера исследования (2651) */
        Assertions.assertEquals(idDirection, IdConsul1, "Номер консультации не совпадает");
    }

    @Issue(value = "TEL-2557")
    @Link(name = "ТМС-1947", url = "https://team-1okm.testit.software/projects/5/tests/1947?isolatedSection=e0d69503-4fe3-4790-8a4f-f3e055d6d4e0")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Квоты оборудования")
    @Order(2)
    @DisplayName("Исключать из списка распределения квот занятые слоты для расписания оборудования")
    @Description("Переходим в Расписание оборудования - переходим в настройки расписания - квоты - добавляем на слот МО1. Создаём направление на диагностику  записыываем на слот с квотой. После переходим в квоты и смотрим, что данный слот не доступен")
    public void Access_2557() throws InterruptedException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Расчёт нужной даты");
        GetDate();
        TomorrowDay = Date_2459();

        System.out.println("Добавление расписания оборудования");
        AuthorizationMethod(authorizationObject.Select(PMOTarget));

        System.out.println("Нажимаем на редактирование созданного оборудования");
        ClickElement(equipmentSchedule.EquipmentSchedulesWait);
        ClickElement(equipmentSchedule.ButtonWorkScheduleWait);
        Thread.sleep(2000);
        equipmentSchedule.ClickEquipment();
        ClickElement(equipmentSchedule.Kvots);

        System.out.println("Ищем завтрашнее число");
        Integer number = 0;
        while (!isElementVisibleTime(equipmentSchedule.KvotsData(TomorrowDay), 1) & number < 30) {
            number++;
            ClickElement(equipmentSchedule.Next);
        }

        System.out.println("Проверяем, что квота не доступна");
        equipmentSchedule.CheckKvotTime(TomorrowDay, ReadPropFile("value_2459_time1"));
        WaitElement(equipmentSchedule.KvotsTimeDateDisabled(TomorrowDay,
                ReadPropFile("value_2459_time1")));

        equipmentSchedule.CheckKvotTime(TomorrowDay, ReadPropFile("value_2459_time1"));
        WaitElement(equipmentSchedule.KvotsTimeDateDisabled(TomorrowDay,
                ReadPropFile("value_2459_time2")));
    }

    @Step("Метод смены формата даты для распределения квот")
    public String DateMethod(String str) {
        String dateAll = str;
        String year = dateAll.substring(0, dateAll.length() - 6);
        String month = dateAll.substring(0, dateAll.length() - 3).substring(5);
        String day = dateAll.substring(8);
        String NewDateAll = "" + year + "-" + month + "-" + day + "";
        return NewDateAll;
    }

    @Step("Метод смены формата времени")
    public String TimeMethod(String str, String date) {
        String dateAll = str;
        String dateStart = dateAll.substring(0, dateAll.length() - 8);
        String dateEnd = dateAll.substring(8);

        if (date.contains("start")) {
            return dateStart;
        } else {
            return dateEnd;
        }
    }

    @Step("Метод установки завтрашнего числа")
    public String Date_2459() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, 1);

        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH) + 1;
        Integer day = cal.get(Calendar.DAY_OF_MONTH);

        String monthNew = "";
        String dayNew = "";
        switch (month) {
            case 1:
                monthNew = "01";
                break;
            case 2:
                monthNew = "02";
                break;
            case 3:
                monthNew = "03";
                break;
            case 4:
                monthNew = "04";
                break;
            case 5:
                monthNew = "05";
                break;
            case 6:
                monthNew = "06";
                break;
            case 7:
                monthNew = "07";
                break;
            case 8:
                monthNew = "08";
                break;
            case 9:
                monthNew = "09";
                break;
            default:
                monthNew = String.valueOf(month);
        }
        switch (day) {
            case 1:
                dayNew = "01";
                break;
            case 2:
                dayNew = "02";
                break;
            case 3:
                dayNew = "03";
                break;
            case 4:
                dayNew = "04";
                break;
            case 5:
                dayNew = "05";
                break;
            case 6:
                dayNew = "06";
                break;
            case 7:
                dayNew = "07";
                break;
            case 8:
                dayNew = "08";
                break;
            case 9:
                dayNew = "09";
                break;
            default:
                dayNew = String.valueOf(day);
        }
        String date_2459 = year + "." + monthNew + "." + dayNew;
        System.out.println(date_2459);
        return date_2459;
    }
}
