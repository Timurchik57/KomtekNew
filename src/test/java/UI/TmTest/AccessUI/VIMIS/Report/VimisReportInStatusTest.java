package UI.TmTest.AccessUI.VIMIS.Report;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.Report;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("Отчёты")
@Tag("Проверка_БД")
public class VimisReportInStatusTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    Report vimisReport;
    Integer NumberLine;
    SQL sql;
    java.util.Date date = new Date();
    SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
    String Date = formatForDateNow.format(date);
    private static String QuantitySQL;
    private static String IdSQL;

    @Issue(value = "TEL-378")
    @Link(name = "ТМС-1078", url = "https://team-1okm.testit.software/projects/5/tests/1078?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @Description("Авторизация и переход в ВИМИС - Отчёты - Отчёт по статусам отправки СМС. Проверяем корректное отображение, за выбранный период, проверяем соответствие с БД. А также по переданному СЭМД (vimis.cvd/akineo/prevention/sms) определить для СЭМД идентификатор ИС (centralized_unloading_systems_id), в справочнике ИС найти наименование (telmed.centralized_unloading_systems.name) по идентификатору. Проверяем наименование фильтра в отчетах \"Разработчик МИС изменено на \"Наименование ИС\" в данное поле возвращать наименования из поля telmed.centralized_unloading_systems.name")
    @DisplayName("Проверка отображения cведений о количестве переданных запросов в ВИМИС")
    @Test
    void VimisReport() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        vimisReport = new Report(driver);
        sql = new SQL();

        System.out.println("Авторизация  и переход в ВИМИС - Отчёты");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(vimisReport.ReportWait);
        actionElementAndClick(vimisReport.Report);
        WaitElement(vimisReport.ReportInStatusWait);
        actionElementAndClick(vimisReport.ReportInStatus);

        System.out.println("Выбор направления");
        WaitElement(vimisReport.StatusDirection);
        SelectClickMethod(vimisReport.StatusDirection, vimisReport.Oncology);

        System.out.println("Выбор периода");
        vimisReport.StatusPeriod.click();
        WaitElement(vimisReport.DateWait);
        WaitElement(vimisReport.DateOneWait);
        vimisReport.DateOne.click();
        Thread.sleep(2000);
        vimisReport.DateTwo.click();
        Thread.sleep(1000);

        System.out.println("Проверка ИС");
        ClickElement(vimisReport.IsWaitTwo);
        Thread.sleep(1000);
        List<String> Is = new ArrayList<String>();
        List<WebElement> IsWeb = driver.findElements(vimisReport.SelectIs);
        for (int i = 0; i < IsWeb.size(); i++) {
            Is.add(IsWeb.get(i).getAttribute("innerHTML"));
        }
        Collections.sort(Is);

        System.out.println("Берём все значение ИС из БД");
        List<String> SqlIs = new ArrayList<String>();
        sql.StartConnection("SELECT * FROM telmed.centralized_unloading_systems where isunloading is true;");
        while (sql.resultSet.next()) {
            String name = sql.resultSet.getString("name");
            SqlIs.add(name);
        }
        Collections.sort(SqlIs);
        assertListsEqualIgnoreOrder(Is, SqlIs, "Значения ИС на вебе и в БД не совпадают");

        System.out.println("Поиск");
        vimisReport.StatusSearch.click();
        WaitElement(vimisReport.SizeLine);
        /** Определение номера строки с нужным Структурным подразделением */
        for (int i = 1; i < 10; i++) {
            while (isElementVisibleTime(
                    By.xpath("//tbody[1]/tr[" + i + "]/td[contains(.,'Поликлиническое отделение Ткачей')]"), 1)) {
                NumberLine = i + 1;
                break;
            }
        }
        System.out.println(NumberLine);
        if (NumberLine == null) {
            System.out.println("Нет записей");
        } else {
            String Quantity = vimisReport.SMS27.getText();
            vimisReport.ColumnStructure.getText().equals("Структурное подразделение");

            /** Запрос в БД для Онкологии SMSV27, на количество записей */
            sql.StartConnection(
                    "SELECT count(id) FROM vimis.sms where create_date > '" + Date + " 00:00:00.346 +0500' and doctype = 'SMSV27';");
            while (sql.resultSet.next()) {
                QuantitySQL = sql.resultSet.getString("count");
            }

            /** Запрос в БД для Онкологии SMSV27, на название Структурного подразделения */
            sql.StartConnection("select m.depart_name  FROM vimis.additionalinfo a\n" +
                                        "join dpc.mo_subdivision_structure m on a.department_oid =  m.depart_oid \n" +
                                        "join vimis.sms s on a.smsid = s.id \n" +
                                        "WHERE s.create_date BETWEEN '" + Date + " 00:00:00.346 +0500' and '" + Date + " 23:59:59.346 +0500' and s.doctype = 'SMSV27' group by m.depart_name;");
            while (sql.resultSet.next()) {
                IdSQL = sql.resultSet.getString("depart_name");
            }
            Assertions.assertEquals(Quantity, QuantitySQL);

            System.out.println(
                    "Значение на интерфейсе для Онкологии SMS27 : " + Quantity + " и значение в БД: " + QuantitySQL + " совпадают");
            if (IdSQL == null) {
                Assertions.assertNull(IdSQL);
            } else {
                Assertions.assertEquals(IdSQL, "Поликлиническое отделение Ткачей");
            }

            System.out.println(
                    "Значение Структурного подразделения на интерфейсе для Онкологии SMS27 : " + IdSQL + " и значение в БД: Поликлиническое отделение Ткачей совпадают");
        }
    }
}
