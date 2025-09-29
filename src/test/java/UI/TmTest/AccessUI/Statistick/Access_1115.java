package UI.TmTest.AccessUI.Statistick;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Disabled
public class Access_1115 extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;

    @Test
    @Description("Авторизация и переход в Детальный отчёт по консультациям. Проверяем корректное отображение, за выбранный период")
    @DisplayName("Проверка отображения детального отчёта по консультации")
    public void ConsultationStatistic() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        System.out.println("Берём все МО");
        Thread.sleep(2000);
        WaitElementTime(analyticsMO.ChooseMO, 60);
        ClickElement(analyticsMO.ChooseMO);
        Thread.sleep(1000);
        List<String> WebMO = new ArrayList<>();
        List<WebElement> MO = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < MO.size(); i++) {
            WebMO.add(MO.get(i).getAttribute("innerText"));
        }
        Collections.sort(WebMO);
        WaitElementTime(analyticsMO.ChooseMO, 60);
        ClickElement(analyticsMO.ChooseMO);

        System.out.println("Берём все Диагнозы");
        ClickElement(analyticsMO.ChooseDiagnosis);
        Thread.sleep(1000);
        List<String> WebDiagnosis = new ArrayList<>();
        List<WebElement> Diagnosis = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < Diagnosis.size(); i++) {
            WebDiagnosis.add(Diagnosis.get(i).getAttribute("innerText"));
        }
        Collections.sort(WebDiagnosis);

        System.out.println("Сверяем МО с БД");
        List<String> SQLMO = new ArrayList<>();
        sql.StartConnection("Select * from dpc.mis_sp_mu where oid is not null and destroydate is null and to_display is true and version = '1.0';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("namemu");
            SQLMO.add(sql.value);
        }
        Collections.sort(SQLMO);

        System.out.println("Сверяем Диагнозы с БД");
        List<String> SQLDiagnosis = new ArrayList<>();
        sql.StartConnection("Select * from dpc.mkb10 limit 50;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("mkb_code");
            String name = sql.resultSet.getString("mkb_name");
            SQLDiagnosis.add(sql.value + " " + name);
        }
        Collections.sort(SQLDiagnosis);

        assertListsEqualIgnoreOrder(WebMO, SQLMO, "МО не совпадают");
        assertListsEqualIgnoreOrder(WebDiagnosis, SQLDiagnosis, "МО не совпадают");
    }
}
