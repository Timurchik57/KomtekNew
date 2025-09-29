package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Проверка_БД")
@Tag("Расписание_консультаций")
public class Access_2062Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    ConsultationScheduleRemote consultationSR;
    String id;
    String fname;
    String sname;
    String mname;
    String profile;
    String purpose;
    String datefrom;
    String dateto;
    String timefrom;
    String timeto;
    String slotinterval;
    String placework;
    List<String> list = new ArrayList<>();
    List<String> listBD = new ArrayList<>();
    List<WebElement> WebList;
    public String dateStart;
    public String dateEnd;

    @Issue(value = "TEL-2062")
    @Issue(value = "TEL-2094")
    @Issue(value = "TEL-2214")
    @Issue(value = "TEL-2284")
    @Issue(value = "TEL-2666")
    @Issue(value = "TEL-2692")
    @Issue(value = "TEL-2693")
    @Issue(value = "TEL-2696")
    @Link(name = "ТМС-1893", url = "https://team-1okm.testit.software/projects/5/tests/1893?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Tag("Расписание_консультаций")
    @Tag("Удалённая_консультация")
    @DisplayName("Составление \"Графика консультаций\" на странице \"Расписание консультаций\"")
    @Description("Переходим в Расписание консультаций проверяем количество записей. После создаём расписание и проверяем корректное добавление данных в таблицу в бд и на веб")
    public void Access_2062() throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Открываем Расписание консультаций - График консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);

        System.out.println("\n 1 Проверка - сверяем отображение консультантов в селекте (2284)");
        ClickElement(consultationSR.Selected("Выберите консультанта"));
        WaitElement(authorizationObject.SelectFirst);
        Thread.sleep(1500);
        WebList = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < WebList.size(); i++) {
            list.add(WebList.get(i).getText());
        }
        sql.StartConnection("select u2.id, u2.fname, u2.sname, u2.mname, u.quitdate, a.\"name\", a.id from telmed.userplacework u \n" +
                "join telmed.users u2 on u.userid = u2.id \n" +
                "join telmed.accessroles a on u.roleid  = a.id\n" +
                "join dpc.mis_sp_mu m on u.placework  = m.idmu\n" +
                "where a.id = '103' and m.namemu ='БУ ХМАО-Югры \"Окружная клиническая больница\"' and u.quitdate is null order by u2.id asc;");
        while (sql.resultSet.next()) {
            String sname = sql.resultSet.getString("sname");
            String fname = sql.resultSet.getString("fname");
            String mname = sql.resultSet.getString("mname");
            listBD.add(sname + " " + fname + " " + mname);
        }
        assertListsEqualIgnoreOrder(list, listBD, "Консультанты не совпадают в селекте");

        System.out.println("\n 2 Проверка - сверяем отображение профилей в селекте (2666)");
        list.clear();
        listBD.clear();
        ClickElement(consultationSR.Selected("Выберите профиль"));
        WaitElement(authorizationObject.SelectFirst);
        Thread.sleep(2000);
        String SelectFirst = driver.findElement(authorizationObject.SelectFirst).getText();
        System.out.println(SelectFirst);
        Thread.sleep(1500);
        WebList = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < WebList.size(); i++) {
            list.add(WebList.get(i).getText());
        }
        Collections.sort(list);
        sql.StartConnection("select p.\"name\" from consultationslots c\n" +
                "join telmed.users u on c.userid = u.id\n" +
                "join telmed.profiledirectory p on c.profile = p.id\n" +
                "join dpc.mis_sp_mu msm on c.medicalidmu = msm.medicalidmu\n" +
                "join telmed.consultationpurpose con on c.purpose = con.id where c.dateto > '"+Date+" 00:00:00.000'\n" +
                "group by p.\"name\" ;");
        while (sql.resultSet.next()) {
            String name = sql.resultSet.getString("name");
            listBD.add(name);
        }
        assertListsEqualIgnoreOrder(list, listBD, "Профили не совпадают в селекте");
        list.clear();
        listBD.clear();

        System.out.println("\n 3 Проверка - Выбираем первый профиль и сверяем, что в поле консультант отображаются только те консультанты, у которых данный профиль (2692)");
        ClickElement(authorizationObject.SelectFirst);

        ClickElement(consultationSR.Selected("Выберите консультанта"));
        WaitElement(authorizationObject.SelectFirst);
        Thread.sleep(1500);
        WebList = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < WebList.size(); i++) {
            list.add(WebList.get(i).getText());
        }
        sql.StartConnection("select c.id, c.userid, u.fname, u.sname, u.mname, p.\"name\", con.purpose , c.datefrom, c.dateto,msm.namemu from consultationslots c\n" +
                "join telmed.users u on c .userid = u.id\n" +
                "join telmed.profiledirectory p on c.profile = p.id\n" +
                "join dpc.mis_sp_mu msm on c.medicalidmu = msm.medicalidmu\n" +
                "join telmed.consultationpurpose con on c.purpose = con.id\n" +
                "where name = '"+SelectFirst+"' and c.dateto > '"+Date+" 00:00:00.000';");
        while (sql.resultSet.next()) {
            String sname = sql.resultSet.getString("sname");
            String fname = sql.resultSet.getString("fname");
            String mname = sql.resultSet.getString("mname");
            listBD.add(sname + " " + fname + " " + mname);
        }
        Assertions.assertEquals(list, listBD, "Консультанты не совпадают в селекте");
        list.clear();
        listBD.clear();

        ClickElement(consultationSR.ConsultationSchedule);
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 30);
        WaitElement(consultationSR.ConsultationTotal);
        String count = driver.findElement(consultationSR.ConsultationTotal).getText();
        sql.StartConnection("select count(id) from telmed.consultationslots;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }

        System.out.println("\n 4 Проверка - сверяем количество всех записей");
        Thread.sleep(1500);
        Assertions.assertEquals(count.substring(6), sql.value, "Количество записей не совпадает");

        System.out.println("\n 5 Проверка - сверяем отображаемых консультантов в селекте");
        System.out.println("Создаём новый график консультаций");
        ClickElement(consultationSR.ConsultationScheduleAdd);
        ClickElement(consultationSR.Select("Консультант"));
        WaitElement(authorizationObject.SelectFirst);
        WebList = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < WebList.size(); i++) {
            list.add(WebList.get(i).getText());
        }
        Collections.sort(list);
        sql.StartConnection("select u2.id, u2.fname, u2.sname, u2.mname from telmed.userplacework u \n" +
                "join telmed.users u2 on u.userid = u2.id \n" +
                "join telmed.accessroles a on u.roleid  = a.id\n" +
                "join dpc.mis_sp_mu m on u.placework  = m.idmu \n" +
                "where a.id = '103' and m.namemu ='БУ ХМАО-Югры \"Окружная клиническая больница\"' and u.quitdate is null order by u2.id asc limit 30;");
        while (sql.resultSet.next()) {
            String sname = sql.resultSet.getString("sname");
            String fname = sql.resultSet.getString("fname");
            String mname = sql.resultSet.getString("mname");
            listBD.add(sname + " " + fname + " " + mname);
        }
        Collections.sort(listBD);
        assertListsEqualIgnoreOrder(list, listBD, "Консультанты не совпадают в селекте");

        ClickElement(authorizationObject.Select("Зотин Андрей Владимирович"));
        listBD.clear();
        list.clear();

        System.out.println("\n 6 Проверка - сверяем отображаемые профили в селекте");
        ClickElement(consultationSR.Select("Профиль"));
        WaitElement(authorizationObject.SelectFirst);
        WebList = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < WebList.size(); i++) {
            list.add(WebList.get(i).getText());
        }
        sql.StartConnection("select max(c.id), u3.profilename from telmed.userplacework u\n" +
                "join telmed.users u2 on u.userid = u2.id\n" +
                "join telmed.usermedprofile u3 on u.id  = u3.userplaceworkid\n" +
                "join telmed.consultationslots c on u2.id = c.userid \n" +
                "join telmed.accessroles a on u.roleid  = a.id\n" +
                "join dpc.mis_sp_mu m on u.placework  = m.idmu \n" +
                "where a.id = '103' and m.namemu ='БУ ХМАО-Югры \"Окружная клиническая больница\"' and u2.sname = 'Зотин' and u2.fname = 'Андрей'\n" +
                "group by u3.profilename;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("profilename");
            id = sql.resultSet.getString("max");
            listBD.add(sql.value);
        }
        assertListsEqualIgnoreOrder(list, listBD, "Профили не совпадают в селекте");
        ClickElement(authorizationObject.Select(listBD.get(0)));
        SelectClickMethod(consultationSR.Select("Цель консультаций"),
                authorizationObject.Select("Подозрение на COVID-19"));
        ClickElement(consultationSR.DateStart);
        ClickElement(consultationSR.DateFirstMonth);
        ClickElement(consultationSR.DateLastMonth);
        SelectClickMethod(consultationSR.Select("Время начала"), consultationSR.Time("6:00"));
        SelectClickMethod(consultationSR.Select("Время окончания"), consultationSR.Time("21:00"));
        inputWord(driver.findElement(consultationSR.Select("Отображение расписания")), "70");
        for (int i = 1; i < 8; i++) {
            ClickElement(consultationSR.DayActivity("" + i + ""));
        }
        ClickElement(consultationSR.Add);
        if (isElementVisibleTime(
                consultationSR.Error("Такое расписание пересекается с ранее созданным расписанием указанного врача"),
                3) | isElementVisibleTime(consultationSR.Error("Такое расписание уже существуют"), 3)) {
            ClickElement(consultationSR.Close);
        }

        System.out.println("\n 7 Проверка - сверяем данные отображаемые в таблице");
        System.out.println("Берём данные последней записи");

        System.out.println("Берём данные последней записи из БД");
        sql.StartConnection(
                "select c.id, u.fname, u.sname, u.mname, p.\"name\", con.purpose, c.datefrom, c.dateto, t.slotinterval from telmed.consultationslots c \n" +
                        "join telmed.users u on c.userid = u.id\n" +
                        "join telmed.profiledirectory p on c.profile = p.id\n" +
                        "join telmed.consultationpurpose con on c.purpose = con.id\n" +
                        "join telmed.consultationslotstime t on c.id = t.scheduleid where u.sname = 'Зотин' group by c.id, u.fname, u.sname, u.mname, p.\"name\", \n" +
                        "con.purpose, c.datefrom, c.dateto, t.slotinterval order by c.id desc limit 1;");
        while (sql.resultSet.next()) {
            id = sql.resultSet.getString("id");
            fname = sql.resultSet.getString("fname");
            sname = sql.resultSet.getString("sname");
            mname = sql.resultSet.getString("mname");
            profile = sql.resultSet.getString("name");
            purpose = sql.resultSet.getString("purpose");
            datefrom = sql.resultSet.getString("datefrom");
            dateto = sql.resultSet.getString("dateto");
            slotinterval = sql.resultSet.getString("slotinterval");
        }

        String WenName = driver.findElement(By.xpath("//tr/td[contains(.,'Андрей Зотин Владимирович')]/following-sibling::td[3][contains(.,'"+DateMethod(datefrom) +" - "+ DateMethod(dateto)+"')]/preceding-sibling::td[3]//span")).getText();

        String WenProfile = driver.findElement(By.xpath("//tr/td[contains(.,'Андрей Зотин Владимирович')]/following-sibling::td[3][contains(.,'"+DateMethod(datefrom) +" - "+ DateMethod(dateto)+"')]/preceding-sibling::td[2]//span")).getText();
        String WenPurpose = driver.findElement(By.xpath("//tr/td[contains(.,'Андрей Зотин Владимирович')]/following-sibling::td[3][contains(.,'"+DateMethod(datefrom) +" - "+ DateMethod(dateto)+"')]/preceding-sibling::td[1]//span")).getText();
        String WenDate = driver.findElement(By.xpath("//tr/td[contains(.,'Андрей Зотин Владимирович')]/following-sibling::td[3][contains(.,'"+DateMethod(datefrom) +" - "+ DateMethod(dateto)+"')]//span")).getText();
        String WenActive = driver.findElement(By.xpath("//tr/td[contains(.,'Андрей Зотин Владимирович')]/following-sibling::td[3][contains(.,'"+DateMethod(datefrom) +" - "+ DateMethod(dateto)+"')]/following-sibling::td[1]//span")).getText();
        String WenTime = driver.findElement(By.xpath("//tr/td[contains(.,'Андрей Зотин Владимирович')]/following-sibling::td[3][contains(.,'"+DateMethod(datefrom) +" - "+ DateMethod(dateto)+"')]/following-sibling::td[2]//span")).getText();
        String WenInterval = driver.findElement(By.xpath("//tr/td[contains(.,'Андрей Зотин Владимирович')]/following-sibling::td[3][contains(.,'"+DateMethod(datefrom) +" - "+ DateMethod(dateto)+"')]/following-sibling::td[3]//span")).getText();

        System.out.println("\n Берём данные для определения активных дней");
        listBD.clear();
        sql.StartConnection(
                "select t.dayofweek from telmed.consultationslots c \n" +
                        "join telmed.consultationslotstime t on c.id = t.scheduleid\n" +
                        "where scheduleid = "+id+" group by t.dayofweek order by t.dayofweek;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("dayofweek");
            listBD.add(sql.value);
        }

        System.out.println("\n Берём данные для определения времени работы");
        sql.StartConnection(
                "select t.id, t.starttime, t.endtime from telmed.consultationslots c \n" +
                        "join telmed.consultationslotstime t on c.id = t.scheduleid\n" +
                        "where scheduleid = "+id+" order by id asc limit 1;");
        while (sql.resultSet.next()) {
            timefrom = sql.resultSet.getString("starttime");
        }
        sql.StartConnection(
                "select t.id, t.starttime, t.endtime from telmed.consultationslots c \n" +
                        "join telmed.consultationslotstime t on c.id = t.scheduleid\n" +
                        "where scheduleid = "+id+" order by id desc limit 1;");
        while (sql.resultSet.next()) {
            timeto = sql.resultSet.getString("endtime");
        }

        softAssert.assertEquals(WenName, fname + " " + sname + " " + mname, "ФИО не совпадает");
        softAssert.assertEquals(WenProfile, profile, "Профиль не совпадает");
        softAssert.assertEquals(WenPurpose, purpose, "Цель не совпадает");
        softAssert.assertEquals(WenDate, DateMethod(datefrom) + " - " + DateMethod(dateto), "Дата не совпадает");
        softAssert.assertEquals(ActiveDay(WenActive), listBD, "Активные дни не совпадает");
        softAssert.assertEquals(TimeMethod(timefrom) +" - "+ TimeMethod(timeto), WenTime, "Время не совпадает");
        softAssert.assertEquals(WenInterval, slotinterval + " мин.", "Интервал не совпадает");
        softAssert.assertAll();
    }

    @Step("Метод смены формата даты")
    public String DateMethod(String str) {
        String dateAll = str.substring(0, str.length() - 9);
        String year = dateAll.substring(0, dateAll.length() - 6);
        String month = dateAll.substring(0, dateAll.length() - 3).substring(5);
        String day = dateAll.substring(8);
        String NewDateAll = "" + day + "." + month + "." + year + "";
        return NewDateAll;
    }

    @Step("Метод смены формата времени")
    public String TimeMethod(String str) {
        String dateAll = str.substring(0, str.length() - 3).substring(11);
        switch (dateAll) {
            case "01:00": dateAll = "1:00";
                break;
            case "02:00": dateAll = "2:00";
                break;
            case "03:00": dateAll = "3:00";
                break;
            case "04:00": dateAll = "4:00";
                break;
            case "05:00": dateAll = "5:00";
                break;
            case "06:00": dateAll = "6:00";
                break;
            case "07:00": dateAll = "7:00";
                break;
            case "08:00": dateAll = "8:00";
                break;
            case "09:00": dateAll = "9:00";
                break;
        }
        return dateAll;
    }

    @Step("Метод преобразования числовых значений дней недели в строковое")
    public List ActiveDay (String str) {
        List <String> list = new ArrayList<>();
        if (str.contains("Пн.")) {
            list.add("1");
        }
        if (str.contains("Вт.")) {
            list.add("2");
        }
        if (str.contains("Ср.")) {
            list.add("3");
        }
        if (str.contains("Чт.")) {
            list.add("4");
        }
        if (str.contains("Пт.")) {
            list.add("5");
        }
        if (str.contains("Сб.")) {
            list.add("6");
        }
        if (str.contains("Вс.")) {
            list.add("7");
        }
        return list;
    }
}
