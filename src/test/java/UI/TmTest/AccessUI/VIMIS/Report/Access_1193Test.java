package UI.TmTest.AccessUI.VIMIS.Report;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.Report;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
@Tag("Основные")
public class Access_1193Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    Report report;

    String SuccessfullyUpSql;
    String SuccessfullyFederalSql;
    String RegionalSql;
    String FLKSql;
    String FederalTrueSql;
    String statusBefore;
    String statusAfter;
    String New;
    String SuccessfullyUp;
    String Regional;
    String SuccessfullyRegional;
    String FLK;
    String Queue;
    String FederalTrue;
    String FederalFalse;

    @Test
    @Order(1)
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @Issue(value = "TEL-1193")
    @Link(name = "ТМС-1444", url = "https://team-1okm.testit.software/projects/5/tests/1444?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 1")
    @Description("Переходим в ВИМИС - Отчёты - Сведения о количестве переданных запросов в ВИМИС и РЭМД - Заполняем параметры поиска. Отправляем смс и смотрим у неё статусы в колонках Успешно загруженные в регион/Отправленные на федеральный уровень/Успешно приняты на федеральном уровне/Не прошли ФЛК/В очереди отправки. Меняем данные в бд и смотрим изменения в этих столбцах")
    public void Access_1193_1() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);
        AddRole(PRole, "Доступ до аналитики ВИМИС в рамках разработчика МИС", false);
        Access_1193Method(1, 3, report.Oncology);
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 2")
    public void Access_1193_2() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);
        Access_1193Method(2, 3, report.Prevention);
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 3")
    public void Access_1193_3() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);
        Access_1193Method(3, 2, report.Akineo);
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 4")
    public void Access_1193_4() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);
        Access_1193Method(4, 2, report.SSZ);
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 5")
    public void Access_1193_5() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);
        Access_1193Method(5, 3, report.Infection);
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 99")
    public void Access_1193_99() throws SQLException, InterruptedException, IOException {
        report = new Report(driver);
        Access_1193Method(99, 1, report.Other);
    }

    @Test
    @Story("Сведения о количестве переданных запросов в ВИМИС и РЭМД")
    @DisplayName("Сведения о количестве переданных запросов в ВИМИС и РЭМД для vmcl = 99 для протокола ТМК")
    public void Access_1193_TMK() throws SQLException, InterruptedException {
        report = new Report(driver);
        Access_1193_TMK(report.Other);
    }

    @Step("Метод для сверки селектов с бд")
    public void WebSqlMethod(String Name, By Select, String Sql, String SqlName, Integer vmcl) throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);

        if (Name == "Мед организации") {
            driver.findElement(report.MOWait).sendKeys(Keys.SPACE);
        } else {
            ClickElement(Select);
        }
        Thread.sleep(1500);
        List<String> Web = new ArrayList<>();
        List<WebElement> list = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < list.size(); i++) {
            Web.add(list.get(i).getAttribute("innerText"));
        }
        Collections.sort(Web);
        if (Name == "Мед организации" | Name == "Информационная система") {
            List<String> SQL = new ArrayList<>();
            sql.StartConnection("" + Sql + "");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("" + SqlName + "");
                SQL.add(sql.value);
            }
            Collections.sort(SQL);
            Assertions.assertEquals(Web, SQL, "" + Name + " не совпадает с БД");
        } else {
            if (vmcl == 99) {
                String NameSMS;
                List<String> SQL = new ArrayList<>();
                sql.StartConnection("" + Sql + "");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("" + SqlName + "");
                    NameSMS = sql.resultSet.getString("oid");
                    SQL.add(NameSMS + " - " + sql.value);
                }
                Collections.sort(SQL);
                Assertions.assertEquals(Web, SQL, "" + Name + " не совпадает с БД");
            } else {
                String NameSMS;
                List<String> SQL = new ArrayList<>();
                sql.StartConnection("" + Sql + "");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("" + SqlName + "");
                    NameSMS = sql.resultSet.getString("id");
                    SQL.add(NameSMS + " - " + sql.value);
                }
                Collections.sort(SQL);
                Assertions.assertEquals(Web, SQL, "" + Name + " не совпадает с БД");
            }
        }
    }

    @Step("Метод для отправки смс с изменением статусов, с последующей проверкой изменений на UI")
    public void Access_1193Method(Integer vmcl, Integer docTypeVersion, By Directions) throws IOException, InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);

        TableVmcl(vmcl);

        System.out.println("Отправляем  смс c DocType 3 с vmlc=" + vmcl + "");
        xml.ApiSmd("SMS/SMS3.xml", "3", vmcl, 1, true, docTypeVersion, 1, 9, 18, 1, 57, 21);

        System.out.println("Переход в Отчёты");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(report.ReportWait);
        WaitElement(report.IntelligenceWait);

        System.out.println("Выбираем направление");
        SelectClickMethod(report.Direction, Directions);
        if (vmcl == 99) {
            System.out.println("Выбираем Период");
            ClickElement(report.PeriodWait);
            ClickElement(report.DateOneWait);
            Thread.sleep(1500);
            ClickElement(report.DateTwoWait);
        } else {
            System.out.println("Выбираем Период");
            ClickElement(report.PeriodWait);
            if (isElementNotVisible(report.DateOneWait)) {
                ClickElement(report.DateOneWait);
                Thread.sleep(1500);
                ClickElement(report.DateTwoWait);
            } else {
                ClickElement(report.DateTwoWait);
                Thread.sleep(1500);
                ClickElement(report.DateTwoWait);
            }
        }
        System.out.println("Выбираем Тип документа");
        if (vmcl == 99) {
            inputWord(driver.findElement(report.SMS), "755");
            ClickElement(report.SelectSms75);
        } else {
            ClickElement(report.SMS);
            ClickElement(report.SelectSms);
        }

        if (vmcl == 99) {
            WaitStatusKremd(smsBase, String.valueOf(xml.uuid));
        }
        report.Search.click();
        Thread.sleep(1500);

        if (vmcl == 99) {
            sql.StartConnection(
                    "SELECT id, doctype, status, fremd_status,emd_id FROM " + smsBase + " WHERE created_datetime > '" + Date + " 00:00:00.888 +0500' AND doctype = '75' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                statusBefore = sql.resultSet.getString("status");
            }
            System.out.println("Статус РРЭМД - " + statusBefore);
        }

        System.out.println("Успешно загруженные в регион"); // Просто добавленная смс
        WaitElement(report.SuccessfullyUploadedWait);
        SuccessfullyUp = report.SuccessfullyUploaded.getText();
        System.out.println(SuccessfullyUp);

        System.out.println("В очереди РРЭМД"); // status = null
        Regional = report.Regional.getText();
        System.out.println(Regional);

        System.out.println("Успешно приняты РРЭМД"); // status = success
        SuccessfullyRegional = report.SuccessfullyRegional.getText();
        System.out.println(SuccessfullyRegional);

        System.out.println("Не прошли ФЛК РРЭМД"); // status = error
        FLK = report.FLK.getText();
        System.out.println(FLK);

        System.out.println("В очереди отправки (Отправленные в ФРЭМД)");
        Queue = report.Queue.getText();
        System.out.println(Queue);

        if (vmcl == 99) {
            System.out.println("Успешно принятые в ФРЭМД"); // fremd_status = 1
            FederalTrue = report.FederalTrue.getText();
            System.out.println(FederalTrue);

            System.out.println("Не прошли ФЛК в ФРЭМД"); // fremd_status = 0
            FederalFalse = report.FederalFalse.getText();
            System.out.println(FederalFalse);
        }

        if (vmcl == 99) {
            System.out.println("1 проверка - status = error");
            report.Search.click();
            Thread.sleep(1500);

            AccessTable(0,0,0,0,0,0,0, vmcl);

            System.out.println("2 проверка - status на null - увеличилось значение в В очереди отправки");
            sql.UpdateConnection("update " + smsBase + " set status = NULL where local_uid = '" + xml.uuid + "';");
            report.Search.click();
            Thread.sleep(2000);

            AccessTable(0,1,0,-1,0,0,0, vmcl);

            System.out.println(
                    "3 проверка - status = success - уменьшилось значение В очереди РРЭМД и увеличилось Успешно приняты РРЭМД");
            sql.UpdateConnection("update " + smsBase + " set status = 'success' where local_uid = '" + xml.uuid + "';");
            report.Search.click();
            Thread.sleep(1500);

            AccessTable(0,0,1,-1,0,0,0, vmcl);

            System.out.println(
                    "4 проверка - fremd_status = 1 -  увеличилось значение Успешно приняты на федеральном уровне");
            sql.UpdateConnection("update " + smsBase + " set fremd_status = '1' where local_uid = '" + xml.uuid + "';");
            report.Search.click();
            Thread.sleep(1500);

            AccessTable(0,0,1,-1,0,1,0, vmcl);

            System.out.println(
                    "5 проверка - fremd_status = 0 - значение Успешно приняты на федеральном уровне осталось увеличенным, увеличилось Не прошли ФЛК");
            sql.UpdateConnection("update " + smsBase + " set fremd_status = '0' where local_uid = '" + xml.uuid + "';");
            report.Search.click();
            Thread.sleep(1500);

            AccessTable(0,0,1,-1,0,0,1, vmcl);
        } else {
            sql.StartConnection("select * from "+smsBase+" where local_uid = '" + xml.uuid + "'");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
            }

            System.out.println("1 проверка - is_sent = true - значение В очереди отправки и увеличилось Отправленные на федеральный уровень");
            sql.UpdateConnection("update " + smsBase + " set is_sent = 'true' where local_uid = '" + xml.uuid + "';");
            report.Search.click();
            Thread.sleep(1500);

            AccessTable(0,1,0,0,-1,0,0, vmcl);

            System.out.println("2 проверка - status в " + logsBase + " на 0 - увеличилось значение Не прошли ФЛК");
            CollbekVimis("" + xml.uuid + "", "0", "Проврека 1193", smsBase, vmcl);
            report.Search.click();
            Thread.sleep(1500);

            AccessTable(0,1,0,1,-1,0,0, vmcl);

            System.out.println("3 проверка - status = '1' -  увеличилось значение Успешно приняты на федеральном уровне");
            System.out.println("Берём эту же смс и меняем у неё status в " + logsBase + " на 1");
            sql.UpdateConnection("update " + logsBase + " set status = '1' where sms_id = '" + sql.value + "';");
            report.Search.click();
            Thread.sleep(1500);

            AccessTable(0,1,1,0,-1,0,0, vmcl);
        }
    }

    @Step("Метод проверки отображения данных в отчёте для ТМК")
    public void Access_1193_TMK(By Directions) throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);

        System.out.println("Переход в Отчёты");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(report.ReportWait);
        WaitElement(report.IntelligenceWait);

        System.out.println("Выбираем направление");
        SelectClickMethod(report.Direction, Directions);

        System.out.println("Выбираем Тип документа");
        inputWord(driver.findElement(report.SMS), "8 - Протокол ");
        ClickElement(report.Select("8 - Протокол телемедицинской консультации (PDF/A-1)"));
        ClickElement(report.SearchWait);
        Thread.sleep(1500);

        System.out.println("Успешно загруженные в регион");
        if (isElementVisibleTime(report.SuccessfullyUploadedWait, 8)) {
            String SuccessfullyUp = report.SuccessfullyUploaded.getText();
            sql.StartConnection(
                    "SELECT count(*)  FROM vimis.remd_sent_result where doctype = '8' and created_datetime between '" + Date.substring(
                            0, 4) + "-01-01 00:00:00.346 +0500' and '" + Date + " 00:00:00.346 +0500';");
            while (sql.resultSet.next()) {
                SuccessfullyUpSql = sql.resultSet.getString("count");
                Assertions.assertEquals(SuccessfullyUp, SuccessfullyUpSql, "Успешно загруженные в регион не совпадают");
            }

            System.out.println("Отправлены в РРЭМД");
            String Regional = report.Regional.getText();
            sql.StartConnection(
                    "SELECT count(*)  FROM vimis.remd_sent_result where doctype = '8' and status is null and created_datetime between '" + Date.substring(
                            0, 4) + "-01-01 00:00:00.346 +0500' and '" + Date + " 00:00:00.346 +0500';");
            while (sql.resultSet.next()) {
                RegionalSql = sql.resultSet.getString("count");
                Assertions.assertEquals(Regional, RegionalSql, "Очередь РРЭМД не совпадают");
            }

            System.out.println("Успешно приняты РРЭМД");
            String RegionalTrue = report.SuccessfullyRegional.getText();
            sql.StartConnection(
                    "SELECT count(*)  FROM vimis.remd_sent_result where doctype = '8' and status = 'success' and created_datetime between '" + Date.substring(
                            0, 4) + "-01-01 00:00:00.346 +0500' and '" + Date + " 00:00:00.346 +0500';");
            while (sql.resultSet.next()) {
                SuccessfullyFederalSql = sql.resultSet.getString("count");
                Assertions.assertEquals(RegionalTrue, SuccessfullyFederalSql, "Успешно приняты РРЭМД не совпадают");
            }

            System.out.println("Не прошли ФЛК");
            String FLK = report.FLK.getText();
            sql.StartConnection(
                    "SELECT count(*)  FROM vimis.remd_sent_result where doctype = '8' and status = 'error' and created_datetime between '" + Date.substring(
                            0, 4) + "-01-01 00:00:00.346 +0500' and '" + Date + " 00:00:00.346 +0500';");
            while (sql.resultSet.next()) {
                FLKSql = sql.resultSet.getString("count");
                Assertions.assertEquals(FLK, FLKSql, "Не прошли ФЛК РРЭМД не совпадают");
            }

            System.out.println("Успешно приняты ФРЭМД");
            String FederalTrue = report.FederalTrue.getText();
            sql.StartConnection(
                    "SELECT count(*)  FROM vimis.remd_sent_result where doctype = '8' and status = 'success' and fremd_status = 1 and created_datetime between '" + Date.substring(
                            0, 4) + "-01-01 00:00:00.346 +0500' and '" + Date + " 00:00:00.346 +0500';");
            while (sql.resultSet.next()) {
                FederalTrueSql = sql.resultSet.getString("count");
                Assertions.assertEquals(FederalTrue, FederalTrueSql, "Успешно приняты ФРЭМД не совпадают");
            }
        }
    }


    @Step("Метод проверки данных в таблице")
    public void AccessTable(Integer Remd, Integer OhRemd, Integer TrueRemd, Integer FalseRemd, Integer OhFremd, Integer TrueFremd, Integer FalseFemd, Integer vmcl) throws InterruptedException {
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);

        New = report.SuccessfullyUploaded.getText();
        System.out.println("Все - " + New  +" - " + SuccessfullyUp);
        Assertions.assertEquals(Integer.valueOf(New), Integer.valueOf(SuccessfullyUp) + Remd,
                "Значение Успешно загруженные в регион должно измениться на " + Remd);

        New = report.Regional.getText();
        if (vmcl == 99) {
            System.out.println("В очереди РРЭМД - " + New  +" - " + Regional);
            Assertions.assertEquals(Integer.valueOf(New), Integer.valueOf(Regional) + OhRemd,
                    "Значение В очереди РРЭМД должно измениться на " + OhRemd);
        } else {
            System.out.println("Отправленные на федеральный уровень - " + New  +" - " + Regional);
            Assertions.assertEquals(Integer.valueOf(New), Integer.valueOf(Regional) + OhRemd,
                    "Значение Отправленные на федеральный уровень должно измениться на " + OhRemd);
        }

        New = report.SuccessfullyRegional.getText();
        if (vmcl == 99) {
            System.out.println("Успешно приняты РРЭМД - " + New  +" - " + SuccessfullyRegional);
            Assertions.assertEquals(Integer.valueOf(New), Integer.valueOf(SuccessfullyRegional) + TrueRemd,
                    "Значение Успешно приняты РРЭМД должно измениться на " + TrueRemd);
        } else {
            System.out.println("Успешно приняты на федеральном уровне - " + New  +" - " + SuccessfullyRegional);
            Assertions.assertEquals(Integer.valueOf(New), Integer.valueOf(SuccessfullyRegional) + TrueRemd,
                    "Значение Успешно приняты на федеральном уровне должно измениться на " + TrueRemd);
        }


        New = report.FLK.getText();
        System.out.println("ФЛК - " + New  +" - " + FLK);
        Assertions.assertEquals(Integer.valueOf(New), Integer.valueOf(FLK) + FalseRemd,
                "Значение Не прошли ФЛК РРЭМД должно измениться на " + FalseRemd);

        New = report.Queue.getText();
        if (vmcl == 99) {
            System.out.println("Отправленные ФРЭМД - " + New  +" - " + Queue);
            Assertions.assertEquals(Integer.valueOf(New), Integer.valueOf(Queue) + OhFremd,
                    "Значение Отправленные в ФРЭМД должно измениться на " + OhFremd);
        } else {
            System.out.println("В очереди отправки - " + New  +" - " + Queue);
            Assertions.assertEquals(Integer.valueOf(New), Integer.valueOf(Queue) + OhFremd,
                    "Значение В очереди отправки должно измениться на " + OhFremd);
        }


        if (vmcl == 99) {
            New = report.FederalTrue.getText();
            System.out.println("Приняты ФРЭМД - " + New + " - " + FederalTrue);
            Assertions.assertEquals(Integer.valueOf(New), Integer.valueOf(FederalTrue) + TrueFremd,
                    "Значение Успешно принятые в ФРЭМД должно измениться на " + TrueFremd);

            New = report.FederalFalse.getText();
            System.out.println("ФЛК ФРЭМД - " + New + " - " + FederalFalse);
            Assertions.assertEquals(Integer.valueOf(New), Integer.valueOf(FederalFalse) + FalseFemd,
                    "Значение Не принятые в ФРЭМД должно измениться на " + FalseFemd);
        }
    }
}
