package UI.TmTest.AccessUI.Statistick;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
public class Access_1260Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;
    public Integer All = 0;
    public Integer Deviation = 0;
    public Integer Counter;
    public Integer CounterNegative;

    @Test
    @Issue(value = "TEL-1260")
    @Link(name = "ТМС-1465", url = "https://team-1okm.testit.software/projects/5/tests/1465?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Фильтрация по направлению на странице рейтинга МО")
    @Description("Переходим в Аналитика Мо по ОМП и проверяем отображения по направлениям")
    public void Access_1260() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        authorizationObject.LoadingTime(60);
        analyticsMO.QuantityStackMethod();

        System.out.println("По очереди выбираем направления и сверяем с бд все маршруты и просроченные с БД");
        QuantityRoutesDirectionsMethod(analyticsMO.Onko, "1", "Онкология");
        QuantityRoutesDirectionsMethod(analyticsMO.Prev, "2", "Профилактика");
        QuantityRoutesDirectionsMethod(analyticsMO.Akineo, "3", "Акинео");
        QuantityRoutesDirectionsMethod(analyticsMO.SSZ, "4", "ССЗ");
        QuantityRoutesDirectionsMethod(analyticsMO.Other, "99", "Иные профили");
    }

    /**
     * Метод для подсчета всех маршрутов и просроченных
     */
    @Step("Подсчет всех маршрутов и просроченных в блоке {1}")
    public void QuantityRoutestMethod(By Routes, String Name) {
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Считаем количество МО в " + Name + "");
        List<WebElement> Quantity = driver.findElements(Routes);

        System.out.println("Берём количество всех маршрутов и просроченных в " + Name + "");
        for (int i = 1; i < Quantity.size() + 1; i++) {
            String Alls = driver.findElement(By.xpath(
                    "//section/div/div/span[contains(.,'" + Name + "')]/following-sibling::div[@class='rating-mo-grid']/div[" + i + "]//div[@class='rating-mo-grid-info-line-body-counts']//span[@class='rating-mo-grid-info-line-body-counts-left-all']")).getText();
            String Deviations = driver.findElement(By.xpath(
                    "//section/div/div/span[contains(.,'" + Name + "')]/following-sibling::div[@class='rating-mo-grid']/div[" + i + "]//div[@class='rating-mo-grid-info-line-body-counts']//span[@class='rating-mo-grid-info-line-body-counts-left-deviation']")).getText();
            All += Integer.valueOf(Alls);
            Deviation += Integer.valueOf(Deviations);

        }
    }

    /**
     * Метод для выставления направления и проверки отображения МО
     */
    @Step("Выставление направления и проверки отображения МО в блоке {2}")
    public void QuantityRoutesDirectionsMethod(By Direction, String vmcl, String Name) throws SQLException, InterruptedException {
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();

        System.out.println("Выбираем направление - " + Name + "");
        SelectClickMethod(analyticsMO.Direction, Direction);
        ClickElement(analyticsMO.Apply);
        WaitElement(analyticsMO.Tall);
        Thread.sleep(1500);
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        authorizationObject.LoadingTime(60);
        QuantityRoutestMethod(analyticsMO.QuantityRoutestTall, "Высокий");
        QuantityRoutestMethod(analyticsMO.QuantityRoutestAverage, "Средний");
        QuantityRoutestMethod(analyticsMO.QuantityRoutestlow, "Низкий");
        System.out.println(All);
        System.out.println(Deviation);

        System.out.println("Сверяем количество всех маршрутов");
        sql.StartConnection("select  count(p.nosological_patient_id) from vimis.patients_routes_stages p\n" +
                "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                "where pp.is_current_point = true  and nos.vmcl = " + vmcl + ";");
        while (sql.resultSet.next()) {
            Counter = Integer.valueOf(sql.resultSet.getString("count"));
        }
        Assertions.assertEquals(Counter, All, "Количество всех маршрутов не совпадает");

        System.out.println("Сверяем количество просроченных  маршрутов");
        sql.StartConnection("select  count(p.nosological_patient_id) from vimis.patients_routes_stages p\n" +
                "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                "where pp.is_current_point = true  and nos.vmcl = " + vmcl + " and pp.deviation_count != 0;");
        while (sql.resultSet.next()) {
            Counter = Integer.valueOf(sql.resultSet.getString("count"));
        }
        Assertions.assertEquals(Counter, Deviation, "Количество просроченных маршрутов не совпадает");
        System.out.println();
        All = 0;
        Deviation = 0;
    }

    /**
     * Метод для проверки МО в нужном блоке в зависимости от направления
     */
    public void MORoutesDirectionsMethod(By Direction, String vmcl, String Name, By Routes) {
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();

        System.out.println("Берём первое МО в " + Name + "");
        if (isElementNotVisible(Routes)) {
            String NameMo = driver.findElement(Routes).getText();
        }
    }
}
