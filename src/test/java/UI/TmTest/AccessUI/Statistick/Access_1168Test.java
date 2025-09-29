package UI.TmTest.AccessUI.Statistick;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("РРП")
public class Access_1168Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;
    public String Code;
    public String CodeName;
    public String TokenRRP;

    @Test
    @Issue(value = "TEL-1168")
    @Link(name = "ТМС-1439", url = "https://team-1okm.testit.software/projects/5/tests/1439?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение данных по пациенту на странице Маршрутов - Пациент")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок где есть маршруты, открываем МО выбираем пациента и сверяем данные пациента с бд")
    public void Access_1168() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();

        if (KingNumber != 4) {
            System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
            AuthorizationMethod(authorizationObject.OKB);
            ClickElement(analyticsMO.Analytics);

            System.out.println("Проверяем где есть маршруты, во всех блоках");
            Thread.sleep(3000);
            analyticsMO.QuantityStackMethod();

            System.out.println("Переходим в первый попавшийся блок, у которого есть мо с этапами");
            if (AnalyticsMO.TallMO) {
                ClickElement(analyticsMO.NameMOTallFirst);
            } else {
                if (AnalyticsMO.AverageMO) {
                    ClickElement(analyticsMO.NameMOAverageFirst);
                } else {
                    ClickElement(analyticsMO.NameMOlowFirst);
                }
            }

            System.out.println("Выбираем первого пациента и переходим к нему");
            ClickElement(analyticsMO.FirstPatient);
            WaitElement(analyticsMO.Snils);
            Thread.sleep(1500);

            System.out.println("Берём данные пациента");
            String FIO = driver.findElement(analyticsMO.FIO).getText();
            String Date = driver.findElement(analyticsMO.Date).getText();
            String Phone = null;
            if (isElementNotVisible(analyticsMO.Phone)) {
                Phone = driver.findElement(analyticsMO.Phone).getText();
            }
            String Snils = driver.findElement(analyticsMO.Snils).getText();

            System.out.println("Изменяем формат даты (в бд отличается)");
            String Date1 = StringUtils.substring(Date, 0, 10);
            String dd = Date1.substring(0, Date1.length() - 8);
            String mm = Date1.substring(0, Date1.length() - 5);
            String mm1 = mm.substring(3);
            String yy = Date1.substring(6);
            String DateNew = yy + "-" + mm1 + "-" + dd;

            System.out.println("Берём диагнозы и их названия");
            WaitElement(analyticsMO.QuantityDiagnosis);
            List<String> DiagnosisWeb = new ArrayList<>();
            List<WebElement> QuaDiagnosis = driver.findElements(analyticsMO.QuantityDiagnosis);
            for (int i = 1; i < QuaDiagnosis.size() + 1; i++) {
                Code = driver.findElement(By.xpath(
                        "//li/div[contains(.,'Диспансерное наблюдение')]/following-sibling::div//ul/li[" + i + "]//span")).getText();
                CodeName = driver.findElement(
                        By.xpath(
                                "//li/div[contains(.,'Диспансерное наблюдение')]/following-sibling::div//ul/li[" + i + "]//p")).getText();
                DiagnosisWeb.add(Code + " " + CodeName);
            }
            Collections.sort(DiagnosisWeb);
            String PhoneSql = null;

            System.out.println("Авторизуемся в РРП и берём токен");
            GetRRP("", Snils);
            String DateRRP = Response.get("patients[0].Policy.ChangeDate");
            String DateRRPNew = StringUtils.substring(DateRRP, 0, 10);
            String LastName = Response.get("patients[0].LastName");
            String FirstName = Response.get("patients[0].FirstName");
            String MiddleName = Response.get("patients[0].MiddleName");
            String giud = Response.get("patients[0].guid");

            Assertions.assertEquals(FIO, LastName.toUpperCase() + " " + FirstName.toUpperCase() + " " + MiddleName.toUpperCase(), "ФИО не совпадает");

            System.out.println("Сравниваем Диагнозы с БД");
            List<String> DiagnosisSQL = new ArrayList<>();
            sql.StartConnection("WITH from_register AS (\n" +
                    "    SELECT a.diagnosis AS \"Code\", m.mkb_name AS \"Name\"\n" +
                    "    FROM vimis.akineo_sms_v5_register AS a\n" +
                    "    INNER JOIN dpc.mkb10 AS m ON a.diagnosis = m.mkb_code\n" +
                    "    LEFT JOIN dpc.pregnancyrisklevel AS p ON a.risklevel = p.id\n" +
                    "    WHERE a.patient_guid = '"+giud+"'\n" +
                    ")\n" +
                    "(\n" +
                    "    SELECT n.diagnosis AS \"Code\", m.mkb_name AS \"Name\"\n" +
                    "    FROM vimis.nosological_patients AS n \n" +
                    "    INNER JOIN dpc.mkb10 AS m ON n.diagnosis = m.mkb_code\n" +
                    "    WHERE n.patient_guid = '"+giud+"' AND n.diagnosis NOT IN (select \"Code\" from from_register)\n" +
                    ")\n" +
                    "UNION ALL \n" +
                    "(\n" +
                    "    select * from from_register\n" +
                    ");");
            while (sql.resultSet.next()) {
                String Code = sql.resultSet.getString("Code");
                String CodeName = sql.resultSet.getString("Name");
                DiagnosisSQL.add(Code + " " + CodeName);
            }
            Collections.sort(DiagnosisSQL);
            Assertions.assertEquals(DiagnosisWeb.size(), DiagnosisSQL.size(), "Диагнозы не совпадают");
        }
    }
}


