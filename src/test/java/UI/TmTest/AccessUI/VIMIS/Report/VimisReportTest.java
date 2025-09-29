package UI.TmTest.AccessUI.VIMIS.Report;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.Report;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("Отчёты")
@Tag("Проверка_БД")
@Tag("Основные")
public class VimisReportTest extends BaseAPI {

    private static String Successfull;
    private static String SuccessfullDoctype;

    AuthorizationObject authorizationObject;
    Report vimisReport;
    Date date = new Date();
    SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
    String Date = formatForDateNow.format(date);

    @Test
    @Order(1)
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    public void Before() throws SQLException, IOException, InterruptedException {
        AddRole(PRole, "Доступ до аналитики ВИМИС в рамках разработчика МИС", false);
        AddXmlAll();
    }

    @Description("Авторизация и переход в Вимис отчёт - Сведения о количестве переданных запросов в ВИМИС и РЭМД. Проверяем корректное отображение, за выбранный период, проверяем соответствие с БД")
    @DisplayName("Проверка отображения cведений о количестве переданных запросов в ВИМИС для vmcl = 1")
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @Test
    public void VimisReport_1() throws InterruptedException, SQLException {
        vimisReport = new Report(driver);
        VimisReportethod("Онкология", vimisReport.DirectionBy("1 - Онкология"), 1);
    }

    @DisplayName("Проверка отображения cведений о количестве переданных запросов в ВИМИС для vmcl = 2")
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @Test
    public void VimisReport_2() throws InterruptedException, SQLException {
        vimisReport = new Report(driver);
        VimisReportethod("Профилактика", vimisReport.DirectionBy("2 - Профилактика"), 2);
    }

    @DisplayName("Проверка отображения cведений о количестве переданных запросов в ВИМИС для vmcl = 3")
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @Test
    public void VimisReport_3() throws InterruptedException, SQLException {
        vimisReport = new Report(driver);
        VimisReportethod("Акушерство и неонатология", vimisReport.DirectionBy("3 - Акушерство и неонатология"), 3);
    }

    @DisplayName("Проверка отображения cведений о количестве переданных запросов в ВИМИС для vmcl = 4")
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @Test
    public void VimisReport_4() throws InterruptedException, SQLException {
        vimisReport = new Report(driver);
        VimisReportethod("Сердечно-сосудистые заболевания",
                vimisReport.DirectionBy("4 - Сердечно-сосудистые заболевания"), 4);
    }

    @DisplayName("Проверка отображения cведений о количестве переданных запросов в ВИМИС для vmcl = 5")
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @Test
    public void VimisReport_5() throws InterruptedException, SQLException {
        vimisReport = new Report(driver);
        VimisReportethod("Инфекционные болезни", vimisReport.DirectionBy("5 - Инфекционные болезни"), 5);
    }

    @DisplayName("Проверка отображения cведений о количестве переданных запросов в ВИМИС для vmcl = 99")
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @Test
    public void VimisReport_99() throws InterruptedException, SQLException {
        vimisReport = new Report(driver);
        VimisReportethod("Иные профили", vimisReport.DirectionBy("99 - Иные профили"), 99);
    }

    public void VimisReportethod(String Name, By directions, Integer vmcl) throws InterruptedException, SQLException {

        String dateSql = "";
        String Quantity = null;
        TableVmcl(vmcl);
        authorizationObject = new AuthorizationObject(driver);

        System.out.println("Авторизация  и переход в ВИМИС - Отчёты");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(vimisReport.ReportWait);
        ClickElement(vimisReport.ReportWait);

        System.out.println("Выбор Направления - " + Name + "");
        ClickElement(vimisReport.Delete);
        WaitElement(vimisReport.IntelligenceWait);
        SelectClickMethod(vimisReport.Direction, directions);

        System.out.println("Выбор Периода");
        WaitElement(vimisReport.IntelligenceWait);
        vimisReport.Period.click();
        ClickElement(vimisReport.DateOneWait);
        Thread.sleep(1500);
        ClickElement(vimisReport.DateTwoWait);
        Thread.sleep(1000);

        System.out.println("Поиск");
        vimisReport.Search.click();
        Thread.sleep(1500);
        authorizationObject.LoadingTime(20);
        if (isElementNotVisible(vimisReport.TableSearch)) {
            /** Взять значения стобца "Наименование ИС" */
            String is = driver.findElement(vimisReport.IS).getAttribute("innerText");

            ClickElement(vimisReport.IsWait);
            ClickElement(authorizationObject.Select(is));
            ClickElement(vimisReport.SearchWait);
            Thread.sleep(1500);
            authorizationObject.LoadingTime(20);

            /** Взяли значение "Успешно загруженные в регион" */
            WaitElement(vimisReport.TableSearch);
            Successfull = driver.findElement(vimisReport.TableSearch).getText();
            System.out.println("Колличество успешно загруженных в регион для " + Name + ": " + Successfull);

            /** Взять значения стобца "Тип СМС/Документа" */
            SuccessfullDoctype = driver.findElement(vimisReport.OneDoctype).getText();

            if (vmcl != 99) {
                dateSql = "create_date";
            } else {
                dateSql = "created_datetime";
            }

            System.out.println("Запрос в БД для " + Name + "");
            sql.StartConnection("SELECT count(a.id) FROM " + smsBase + " a\n" +
                    "join telmed.centralized_unloading_systems cus on a.centralized_unloading_system_id = cus.id \n" +
                    "where " + dateSql + " BETWEEN '" + Date + " 00:00:00.346 +0500' and '" + Date + " 23:59:59.346 +0500' and cus.name = '" + is + "';");
            while (sql.resultSet.next()) {
                Quantity = sql.resultSet.getString("count");
            }
            String Doctype = "";
            if (vmcl != 99) {
                sql.StartConnection("SELECT doctype FROM " + smsBase + " a \n" +
                        "join telmed.centralized_unloading_systems cus on a.centralized_unloading_system_id = cus.id \n" +
                        "where " + dateSql + " BETWEEN '" + Date + " 00:00:00.888 +0500' and '" + Date + " 23:59:59.346 +0500' and cus.name = '" + is + "'\n" +
                        "group by doctype order by doctype");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("doctype");
                }
            } else {
                sql.StartConnection("SELECT  r.\"name\"  FROM " + smsBase + " a \n" +
                        "join telmed.centralized_unloading_systems cus on a.centralized_unloading_system_id = cus.id \n" +
                        "join dpc.registered_emd r on a.doctype = r.\"oid\" \n" +
                        "where " + dateSql + " BETWEEN '" + Date + " 00:00:00.888 +0500' and '" + Date + " 23:59:59.346 +0500' and cus.name = '" + is + "'\n" +
                        "group by r.\"name\" order by r.\"name\"");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("name");
                }
            }
            Doctype = sql.value;

            Assertions.assertEquals(SuccessfullDoctype, Doctype, "Не совпадают названия СМС");
            System.out.println(
                    "Значение \"Тип СМС \" на интерфейсе для "+Name+": " + SuccessfullDoctype + " и значение в БД: " + Doctype + " совпадают");
            System.out.println();
        } else {
            Successfull = "0";
            Quantity = "0";

            System.out.println("Нет записей для " + Name + "");
        }
        System.out.println("Колличество успешно загруженных в регион для " + Name + ": " + Successfull);
        Assertions.assertEquals(Successfull, Quantity);
        System.out.println(
                "Значение на интерфейсе для "+Name+": " + Successfull + " и значение в БД: " + Quantity + " совпадают");
    }
}
