package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Расписание_консультаций")
@Tag("Проверка_БД")
public class Access_2060Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    ConsultationScheduleRemote consultationSR;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOU;

    boolean dateTrue;

    @Issue(value = "TEL-2060")
    @Issue(value = "TEL-2061")
    @Issue(value = "TEL-2065")
    @Link(name = "ТМС-1894", url = "https://team-1okm.testit.software/projects/5/tests/1894?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Смена фильтров в Расписание консультаций")
    @Description("Переходим в Расписание консультаций - создаём расписание, меняем фильтры, сверяем отображение с БД")
    public void Access_2060() throws InterruptedException, SQLException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        consultationSR = new ConsultationScheduleRemote(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOU = new ConsultationOutgoingUnfinished(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Открываем Расписание консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);
        ClickElement(consultationSR.NextDay);
        Thread.sleep(1500);

        System.out.println("Берём текущую дату");
        WaitElement(consultationSR.DateToDay);
        String TodayDate = driver.findElement(consultationSR.DateToDay).getText();
        System.out.println("TodayDate = " + TodayDate);

        System.out.println("Берём из БД количество слотов в определённый день");
        sql.StartConnection("select count(*) from (select c.id, c.dayofweek, c.starttime, c.endtime, c.slotinterval, c.for_kid, u.fname, u.sname, u.mname, m.namemu, a.\"name\", p.\"name\", con.purpose\n" +
                "from telmed.consultationslotstime c \n" +
                "join telmed.consultationslots c2 on c.scheduleid = c2.id\n" +
                "join telmed.profiledirectory p on c2.profile = p.id \n" +
                "join telmed.consultationpurpose con on c2.purpose = con.id\n" +
                "join telmed.users u on c2.userid = u.id\n" +
                "join telmed.userplacework u2 on u2.userid = u.id \n" +
                "join telmed.usermedprofile u3 on u.id = u3.userid\n" +
                "join dpc.mis_sp_mu m on u2.placework = m.idmu \n" +
                "join telmed.accessroles a on u2.roleid = a.id\n" +
                "where m.namemu = 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and a.id = '103' and u.sname = 'Зотин' and c.starttime > '"+DateMethod(TodayDate)+" 00:00:00.000' \n" +
                "and c.starttime < '"+DateMethod(TodayDate)+" 23:59:00.000' \n" +
                "group by c.id, c.dayofweek, c.starttime, c.endtime, c.slotinterval, c.for_kid, u.fname, u.sname, u.mname, m.namemu, a.\"name\", p.\"name\", con.purpose) m ;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }

        System.out.println("Определяем какая колонка с нужным консультантом");
        Integer number = null;
        for (int i = 1; i < 15; i++) {
            if (isElementNotVisible(By.xpath("//div[@class='vuecal__flex vuecal__split-days-headers']/div["+i+"][contains(.,'Зотин')]"))) {
                number = i;
                break;
            }
        }
        ClickElement(consultationSR.AllDay);

        System.out.println("Берём количество слотов с веба");
        Thread.sleep(1500);
        WaitNotElement3(consultationSR.Loading, 30);
        WaitElement(consultationSR.SlotsFreeWait(consultationSR.NumberColumn, true));
        List<WebElement> list = driver.findElements(consultationSR.StotsSearch(number));

        System.out.println("\n 1 проверка - сверяем количество отображаемых слотов");
        Assertions.assertEquals(Integer.valueOf(sql.value), list.size(), "Количество слотов отличается на вебе");

        System.out.println("Выбираем любого консультанта");
        SelectClickMethod(consultationSR.Selected("Выберите консультанта"), authorizationObject.Select("БИЛАН ЕВГЕНИЙ ВИКТОРОВИЧ"));
        Thread.sleep(1500);
        WaitElement(consultationSR.WeekDay(1));

        String pn = driver.findElement(consultationSR.WeekDay(1)).getText();
        String vs = driver.findElement(consultationSR.WeekDay(7)).getText();

        // Проверяем больше ли дата в понедельник чем в воскресенье, такое может быть если понедельник из прошлого месяца
        if (Integer.valueOf(pn.substring(1)) > Integer.valueOf(vs.substring(1))) {
            dateTrue = false;
        }

        System.out.println("Берём количество слотов на неделе из БД");
        System.out.println("Берём из БД количество слотов в определённый день");
        sql.StartConnection("select count(*) from (select c.id, c.dayofweek, c.starttime, c.endtime, c.slotinterval, c.for_kid, u.fname, u.sname, u.mname, m.namemu, a.\"name\", p.\"name\", con.purpose\n" +
                "from telmed.consultationslotstime c \n" +
                "join telmed.consultationslots c2 on c.scheduleid = c2.id\n" +
                "join telmed.profiledirectory p on c2.profile = p.id \n" +
                "join telmed.consultationpurpose con on c2.purpose = con.id\n" +
                "join telmed.users u on c2.userid = u.id\n" +
                "join telmed.userplacework u2 on u2.userid = u.id \n" +
                "join telmed.usermedprofile u3 on u.id = u3.userid\n" +
                "join dpc.mis_sp_mu m on u2.placework = m.idmu \n" +
                "join telmed.accessroles a on u2.roleid = a.id\n" +
                "where m.namemu = 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and a.id = '103' and u.sname = 'БИЛАН' and c.starttime > '"+DateMethodWeek(DateMethod(TodayDate), pn)+" 00:00:00.000' \n" +
                "and c.starttime < '"+DateMethodWeek(DateMethod(TodayDate), vs)+" 23:59:00.000' \n" +
                "group by c.id, c.dayofweek, c.starttime, c.endtime, c.slotinterval, c.for_kid, u.fname, u.sname, u.mname, m.namemu, a.\"name\", p.\"name\", con.purpose) m ;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }

        System.out.println("Берём количество слотов на неделе из веба");
        list = driver.findElements(consultationSR.Slots);

        System.out.println("\n 2 проверка - сверяем количество отображаемых слотов на всей неделе");
        Assertions.assertEquals(Integer.valueOf(sql.value), list.size(), "Количество слотов отличается на вебе");
    }

    @Step("Метод смены формата даты")
    public String DateMethod(String str) {
        String year = str.substring(6);
        String month = str.substring(0, str.length() - 5).substring(3);
        String day = str.substring(0, str.length() - 8);
        String NewDateAll = "" + year + "-" + month + "-" + day + "";
        System.out.println(NewDateAll);
        return NewDateAll;
    }

    @Step("Метод смены формата даты, когда берём дату из дня недели")
    public String DateMethodWeek(String str, String dateWeek) {
        GetDate();

        String dateAll = "";
        String NewDateAll = "";
        switch (dateWeek) {
            case " 1": dateAll = "01";
                break;
            case " 2": dateAll = "02";
                break;
            case " 3": dateAll = "03";
                break;
            case " 4": dateAll = "04";
                break;
            case " 5": dateAll = "05";
                break;
            case " 6": dateAll = "06";
                break;
            case " 7": dateAll = "07";
                break;
            case " 8": dateAll = "08";
                break;
            case " 9": dateAll = "09";
                break;
            default:
                dateAll = dateWeek.substring(1);
        }

        // Вычитаем один месяц так как у нас в понедельник дата больше, значит это предыдущий месяц
        if (!dateTrue) {
            NewDateAll = "" + Year + "-" + SetDate(0, -1).substring(0, 2) + "-" + dateAll;
        } else {
            NewDateAll = "" + Year + "-" + Month + "-" + dateAll;
        }
        System.out.println(NewDateAll);
        return NewDateAll;
    }
}
