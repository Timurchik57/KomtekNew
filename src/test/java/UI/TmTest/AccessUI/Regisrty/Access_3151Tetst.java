package UI.TmTest.AccessUI.Regisrty;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
import io.qameta.allure.*;
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
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Регистры")
@Tag("Типы_регистров")
@Tag("Регистр_Акинео")
@Tag("Основные")
public class Access_3151Tetst extends BaseAPI {

    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    String DegreeRisk;
    String Patient_guid;
    List<String> DiagnosisWeb;
    List<WebElement> CountDiagnosis;

    @Issue(value = "TEL-3151")
    @Owner(value = "Галиакберов Тимур")
    @Description("Переходим в Лк Врача и проверяем отображение рисков рядом с диагнозами")
    @Test
    @DisplayName("Отображение данных рисков из таблицы vimis.akineo_sms_v5_register")
    public void Access_3151() throws SQLException, IOException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Переходим в Роли доступа");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(analyticsMO.Analytics);

        sql.StartConnection("select patient_guid, count(*) from vimis.akineo_sms_v5_register group by patient_guid order by count desc limit 1;");
        while (sql.resultSet.next()) {
            Patient_guid = sql.resultSet.getString("patient_guid");
        }

        driver.get(HostAddressWeb + "/registry/patient/"+Patient_guid+"/dashboard");
        WaitNotElement3(authorizationObject.LoadingTrue("3"), 60);
        WaitElement(analyticsMO.QuantityDiagnosis);
        Thread.sleep(2000);
        DiagnosisWeb = new ArrayList<>();
        CountDiagnosis = driver.findElements(analyticsMO.QuantityDiagnosis);
        for (int i = 1; i < CountDiagnosis.size(); i++) {
            DegreeRisk = driver.findElement(By.xpath("//li/div[contains(.,'Диспансерное наблюдение')]/following-sibling::div//ul/li["+i+"]//p")).getText();
            if (DegreeRisk.contains("Степень риска - Низкая") | DegreeRisk.contains("Степень риска - Средняя") | DegreeRisk.contains("Степень риска - Высокая") | DegreeRisk.contains("Степень риска - Не определена")) {
                DiagnosisWeb.add(DegreeRisk);
            }
        }

        sql.StartConnection("select count(*) from vimis.akineo_sms_v5_register a\n" +
                "join dpc.pregnancyrisklevel p on a.risklevel = p.id \n" +
                "where a.patient_guid = '"+Patient_guid+"'; ");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }

        System.out.println("1 Проверка - сравниваем общее количество записей с рисками");
        Assertions.assertEquals(DiagnosisWeb.size(), Integer.valueOf(sql.value), "Количество всех рисков не совпадает");
        DiagnosisWeb.clear();

        System.out.println("2 Проверка - сравниваем количество записей с риском Низкая");
        Degree("Низкая");
        Assertions.assertEquals(DiagnosisWeb.size(), Integer.valueOf(sql.value), "Количество с риском Низкая не совпадает");
        DiagnosisWeb.clear();

        System.out.println("3 Проверка - сравниваем количество записей с риском Средняя");
        Degree("Средняя");
        Assertions.assertEquals(DiagnosisWeb.size(), Integer.valueOf(sql.value), "Количество с риском Средняя не совпадает");
        DiagnosisWeb.clear();

        System.out.println("4 Проверка - сравниваем количество записей с риском Высокая");
        Degree("Высокая");
        Assertions.assertEquals(DiagnosisWeb.size(), Integer.valueOf(sql.value), "Количество с риском Высокая не совпадает");
        DiagnosisWeb.clear();

        System.out.println("5 Проверка - сравниваем количество записей с риском Не определена");
        Degree("Не определена");
        Assertions.assertEquals(DiagnosisWeb.size(), Integer.valueOf(sql.value), "Количество с риском Не определена не совпадает");
        DiagnosisWeb.clear();
    }

    @Step("Метод для подсчета степени риска на веб и бд")
    public void Degree(String degree) throws SQLException {
        sql.StartConnection("select count(*) from vimis.akineo_sms_v5_register a\n" +
                "join dpc.pregnancyrisklevel p on a.risklevel = p.id \n" +
                "where a.patient_guid = '"+Patient_guid+"' and p.name = '"+degree+"'; ");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        for (int i = 1; i < CountDiagnosis.size(); i++) {
            DegreeRisk = driver.findElement(By.xpath("//li/div[contains(.,'Диспансерное наблюдение')]/following-sibling::div//ul/li["+i+"]//p")).getText();
            if (DegreeRisk.contains("Степень риска - "+degree+"")) {
                DiagnosisWeb.add(DegreeRisk);
            }
        }
    }
}
