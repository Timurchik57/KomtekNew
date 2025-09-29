package UI.TmTest.AccessUI.VIMIS.Report;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.Report;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("Отчёты")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Основные")
public class Access_1423Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    Report report;
    Integer NumberAfter;

    @Test
    @Order(1)
    @Issue(value = "TEL-1423")
    @Link(name = "ТМС-1505", url = "https://team-1okm.testit.software/projects/5/tests/1505?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение Отчёт по статусам отправки СМС для vmcl = 1")
    @Story("Отчёт по статусам отправки СМС")
    @Description("Переходим в ВИМИС - Отчёты - Отчёт по статусам отправки СМС - Заполняем параметры поиска -Выбираем в фильтрах различные статусы и смотрим отображение в таблице, сравниваем с БД")
    public void Access_1423_1() throws SQLException, InterruptedException, IOException {
        AddRole(PRole, "Доступ до аналитики ВИМИС в рамках разработчика МИС", false);
        Access_1423Method("Онкология", 1, 3);
    }


    @Test
    @Story("Отчёт по статусам отправки СМС")
    @DisplayName("Отображение Отчёт по статусам отправки СМС для vmcl = 2")
    public void Access_1423_2() throws SQLException, InterruptedException, IOException {
        Access_1423Method("Профилактика", 2, 3);
    }

    @Test
    @Story("Отчёт по статусам отправки СМС")
    @DisplayName("Отображение Отчёт по статусам отправки СМС для vmcl = 3")
    public void Access_1423_3() throws SQLException, InterruptedException, IOException {
        Access_1423Method("Акушерство", 3, 2);
    }

    @Test
    @Story("Отчёт по статусам отправки СМС")
    @DisplayName("Отображение Отчёт по статусам отправки СМС для vmcl = 4")
    public void Access_1423_4() throws SQLException, InterruptedException, IOException {
        Access_1423Method("ССЗ", 4, 2);
    }

    @Test
    @Story("Отчёт по статусам отправки СМС")
    @DisplayName("Отображение Отчёт по статусам отправки СМС для vmcl = 5")
    public void Access_1423_5() throws SQLException, InterruptedException, IOException {
        Access_1423Method("Инфекционные болезни", 5, 3);
    }

//    @Test
//    @DisplayName("Отображение Отчёт по статусам отправки СМС для vmcl = 99")
//    public void Access_1423_99() throws SQLException, InterruptedException, IOException {
//        Access_1423Method("Иные профили", 99, 0);
//    }

    @Step("Метод для сравнения списка с бд, сравнение списка после отправки смс с определённым статусом")
    public void AddSmsAssertSms(String Name, Integer vmcl, Integer docTypeVersion, String Status) throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        report = new Report(driver);

        TableVmcl(vmcl);

        /** Узнаём текущую дату */
        GetDate();

        List<String> SMS = new ArrayList<>();
        List<WebElement> list;
        if (isElementNotVisible(report.NotData)) {
            if (vmcl == 1) {
                SMS = new ArrayList<String>(Collections.nCopies(21, "0"));
                System.out.println(SMS.size());
            }
            if (vmcl == 2 | vmcl == 4) {
                SMS = new ArrayList<String>(Collections.nCopies(12, "0"));
                System.out.println(SMS.size());
            }
            if (vmcl == 3) {
                SMS = new ArrayList<String>(Collections.nCopies(17, "0"));
                System.out.println(SMS.size());
            }
            if (vmcl == 5) {
                SMS = new ArrayList<String>(Collections.nCopies(9, "0"));
                System.out.println(SMS.size());
            }
            sql.StartConnection("SELECT s.doctype, count(doctype) FROM " + smsBase + " s\n" +
                    "left join " + logsBase + " d on s.id = d.sms_id  \n" +
                    "where create_date > '" + Date + " 00:00:00.346 +0500' and create_date < '" + Year +"-"+ SetDate(1, 0).substring(0, 2) +"-"+ SetDate(1, 0).substring(3) + " 00:00:00.346 +0500' and d.status = " + Status + " group by doctype;");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("count");
                Assertions.assertNull(sql.value, "В БД для " + Name + " есть смс со статусом " + Status + ", а на UI нет");
            }
        } else {
            Integer Quantity = 0;
            SMS = new ArrayList<>();
            WaitElement(report.Sum);
            list = driver.findElements(report.Sum);
            System.out.println(list.size());
            for (int i = 1; i < list.size(); i++) {
                SMS.add(list.get(i).getText());
            }
            for (int j = 0; j < SMS.size(); j++) {
                if (Integer.valueOf(SMS.get(j)) != 0) {
                    Quantity++;
                }
            }
            System.out.println(SMS);
            sql.StartConnection("Select count(*) from (select s.doctype , count(s.doctype) FROM " + smsBase + " s\n" +
                    "left join " + logsBase + " d on s.id = d.sms_id  \n" +
                    "where create_date > '" + Date + " 00:00:00.346 +0500' and create_date < '" + Year +"-"+ SetDate(1, 0).substring(0, 2) +"-"+ SetDate(1, 0).substring(3) + " 00:00:00.346 +0500' and d.status = " + Status + " group by doctype) as Count;");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("Count");
                Assertions.assertEquals(
                        sql.value, String.valueOf(Quantity),
                        "Количество СМС у которых есть значения отличные от 0 не совпадает с БД"
                );
            }
        }

        System.out.println("Отправляем  смс c DocType 3 с vmlc = " + vmcl + " и устанавливаем статус в " + logsBase + " " + Status + "");
        if (Status == "1") {
            xml.ApiSmd("SMS/SMS3.xml", "3", vmcl, 1, true, docTypeVersion, 1, 9, 18, 1, 57, 21);
        } else {
            xml.ApiSmd("SMS/SMS3.xml", "3", vmcl, 2, true, docTypeVersion, 1, 9, 18, 1, 57, 21);
        }
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        sql.StartConnection("select * from " + smsBase + " where local_uid = '" + xml.uuid + "'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }
        System.out.println("Берём отправленную смс и устанавливаем у неё в " + logsBase + " status = " + Status + "");
        sql.UpdateConnection(
                "insert into " + logsBase + "(create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', " + Status + ", 'ок', " + sql.value + ", '"+ UUID.randomUUID()+"');");
        if (vmcl == 1 | vmcl == 3 | vmcl == 4) {
            NumberAfter = Integer.valueOf(SMS.get(2));
            NumberAfter++;
            SMS.set(2, String.valueOf(NumberAfter));
        }
        if (vmcl == 2 | vmcl == 5) {
            NumberAfter = Integer.valueOf(SMS.get(1));
            NumberAfter++;
            SMS.set(1, String.valueOf(NumberAfter));
        }
        ClickElement(report.StatusSearchWait);
        Thread.sleep(2500);
        authorizationObject.LoadingTime(20);
        WaitElementTime(report.Sum, 30);
        List<String> SMSAfter = new ArrayList<>();
        list = driver.findElements(report.Sum);
        for (int i = 1; i < list.size(); i++) {
            SMSAfter.add(list.get(i).getText());
        }
        Assertions.assertEquals(
                SMS, SMSAfter, "Значение не добавилось в Статус Успешно принятые в ВИМИС к " + Name + "");
    }

    @Step("Метод для выставления Направления, выбора периода, поиск смс, сравнение списка с бд, сравнение списка после отправки смс с определённым статусом")
    public void  Access_1423Method(String Name, Integer vmcl, Integer docTypeVersion) throws IOException, InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        report = new Report(driver);

        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(report.ReportWait);
        ClickElement(report.ReportInStatusWait);

        System.out.println("Выбираем направление");
        if (vmcl == 1) {
            SelectClickMethod(report.StatusDirection, report.Onko);
        }
        if (vmcl == 2) {
            SelectClickMethod(report.StatusDirection, report.Prev);
        }
        if (vmcl == 3) {
            SelectClickMethod(report.StatusDirection, report.akineo);
        }
        if (vmcl == 4) {
            SelectClickMethod(report.StatusDirection, report.ssz);
        }
        if (vmcl == 5) {
            SelectClickMethod(report.StatusDirection, report.infection);
        }
        if (vmcl == 99) {
            SelectClickMethod(report.StatusDirection, report.other);
        }
        System.out.println("Выбираем Период");
        ClickElement(report.StatusPeriodWait);
        if (isElementNotVisible(report.PeriodEnd)) {
            ClickElement(report.PeriodEnd);
            Thread.sleep(1500);
            if (isElementNotVisible(report.PeriodStartEnd)) {
                ClickElement(report.PeriodStartEnd);
            } else {
                ClickElement(report.PeriodStart);
            }
        } else {
            ClickElement(report.PeriodStartEnd);
            Thread.sleep(1500);
            if (isElementNotVisible(report.PeriodStartEnd)) {
                ClickElement(report.PeriodStartEnd);
            } else {
                ClickElement(report.PeriodStart);
            }
        }
        System.out.println("Выбираем Статус Успешно принятые в ВИМИС");
        SelectClickMethod(report.Status, report.SelectStatusSuccess);
        ClickElement(report.StatusSearchWait);
        Thread.sleep(2000);
        WaitNotElement3(report.LoadingSMS, 20);
        AddSmsAssertSms(Name, vmcl, docTypeVersion, "1");

        System.out.println("Выбираем Не прошли проверку ФЛК ВИМИС");
        SelectClickMethod(report.Status, report.SelectStatusFalse);
        ClickElement(report.StatusSearchWait);
        Thread.sleep(2000);
        WaitNotElement3(report.LoadingSMS, 20);
        AddSmsAssertSms(Name, vmcl, docTypeVersion, "0");

        System.out.println("Выбираем Принято на региональном уровне");
        SelectClickMethod(report.Status, report.SelectStatusTrue);
        ClickElement(report.StatusSearchWait);
        Thread.sleep(2000);
        WaitNotElement3(report.LoadingSMS, 20);
        AddSmsAssertSms(Name, vmcl, docTypeVersion, "3");
    }
}
