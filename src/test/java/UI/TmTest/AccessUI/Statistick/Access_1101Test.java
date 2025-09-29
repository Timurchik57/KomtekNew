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
public class Access_1101Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    SQL sql;
    AnalyticsMO analyticsMO;
    public Integer All = 0;
    public Integer Deviation = 0;
    public Integer Counter;
    public Integer CounterNegative;
    public String MO;

    @Test
    @Issue(value = "TEL-1101")
    @Issue(value = "TEL-2836")
    @Link(name = "ТМС-1433", url = "https://team-1okm.testit.software/projects/5/tests/1433?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение рейтинга медицинских организаций по числу пациентов с просроченными сроками ОМП")
    @Description("Перейти в Статистика - Аналитика МО по ОМП. Проверить отображения Рейтинга, проверить все маршруты + просроченные")
    public void Access_1101() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        sql = new SQL();
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Авторизуемся и переходим в Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        WaitElement(analyticsMO.Tall);
        QuantityRoutestMethod(analyticsMO.QuantityRoutestTall, "Высокий");
        QuantityRoutestMethod(analyticsMO.QuantityRoutestAverage, "Средний");
        QuantityRoutestMethod(analyticsMO.QuantityRoutestlow, "Низкий");
        System.out.println(All);
        System.out.println(Deviation);

        System.out.println("Сверяем количество всех маршрутов");
        sql.StartConnection("select  count(p.nosological_patient_id)  from vimis.patients_routes_stages p\n" +
                                    "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                                    "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                                    "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                                    "where pp.is_current_point = true;");
        while (sql.resultSet.next()) {
            Counter = Integer.valueOf(sql.resultSet.getString("count"));
        }
        Assertions.assertEquals(Counter, All, "Количество всех маршрутов не совпадает");

        System.out.println("Сверяем количество просроченных  маршрутов");
        sql.StartConnection("select  count(p.nosological_patient_id)  from vimis.patients_routes_stages p\n" +
                                    "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                                    "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                                    "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                                    "where pp.is_current_point = true and pp.deviation_count != '0';");
        while (sql.resultSet.next()) {
            Counter = Integer.valueOf(sql.resultSet.getString("count"));
        }
        Assertions.assertEquals(Counter, Deviation, "Количество просроченных маршрутов не совпадает");
        PercentRoutestMethod(analyticsMO.QuantityRoutestTall, "Высокий");
        PercentRoutestMethod(analyticsMO.QuantityRoutestAverage, "Средний");
        PercentRoutestMethod(analyticsMO.QuantityRoutestlow, "Низкий");

        System.out.println("Берём первое название маршрута");
        FirstRoutestMethod(analyticsMO.NameMOTallFirst, "Высокий");
        FirstRoutestMethod(analyticsMO.NameMOAverageFirst, "Средний");
        FirstRoutestMethod(analyticsMO.NameMOlowFirst, "Низкий");
    }

    /**
     * Метод для подсчета всех маршрутов и просроченных
     **/
    @Step("Подсчёт всех маршрутов и просроченных")
    public void QuantityRoutestMethod(By Routes, String Name) {
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Считаем количество МО в " + Name + "");
        List<WebElement> Quantity = driver.findElements(Routes);

        System.out.println("Берём количество всех маршрутов и просроченных в " + Name + "");
        System.out.println(Quantity.size());
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
     * Метод для подсчета процента просроченных
     */
    @Step("Подсчёт процента просроченных")
    public void PercentRoutestMethod(By Routes, String Name) {
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Считаем процент просроченных МО в " + Name + "");
        List<WebElement> Quantity = driver.findElements(Routes);

        System.out.println("Берём количество просроченных маршрутов и делим на количество всех в " + Name + "");
        for (int i = 1; i < Quantity.size() + 1; i++) {
            String Alls = driver.findElement(By.xpath(
                    "//section/div/div/span[contains(.,'" + Name + "')]/following-sibling::div[@class='rating-mo-grid']/div[" + i + "]//div[@class='rating-mo-grid-info-line-body-counts']//span[@class='rating-mo-grid-info-line-body-counts-left-all']")).getText();
            String Deviations = driver.findElement(By.xpath(
                    "//section/div/div/span[contains(.,'" + Name + "')]/following-sibling::div[@class='rating-mo-grid']/div[" + i + "]//div[@class='rating-mo-grid-info-line-body-counts']//span[@class='rating-mo-grid-info-line-body-counts-left-deviation']")).getText();
            All = Integer.valueOf(Alls);
            Deviation = Integer.valueOf(Deviations);
            double Sum = (double) Deviation / All;
            if (Name == "Высокий") {
                boolean b = Sum >= 0.6;
                Assertions.assertTrue(b, "Значение не верно расположено в колонке " + Name + "");
            }
            if (Name == "Средний") {
                boolean b = Sum < 0.6 && Sum > 0.3;
                Assertions.assertTrue(b, "Значение не верно расположено в колонке " + Name + "");
            }
            if (Name == "Низкий") {
                boolean b = Sum <= 0.3;
                Assertions.assertTrue(b, "Значение не верно расположено в колонке " + Name + "");
            }
        }
    }

    /**
     * Метод для смены количества просроченных и проверка изменения блоков на ui
     */
    @Step("Смена количества просроченных и проверка изменения блоков на ui")
    public void FirstRoutestMethod(By Routes, String Name) throws SQLException {
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();
        String Deviation_count = null;

        System.out.println("Берём первое МО в " + Name + "");
        if (isElementNotVisible(Routes)) {
            String NameMo = driver.findElement(Routes).getText();

            /** Проверяем значение МО = null (2836) */
            if (NameMo.contains("Неизвестно")) {
                MO = "m.namemu is null";
            } else {
                MO = "m.namemu = '" + NameMo + "'";
            }

            System.out.println("Берём первое МО в " + MO + " и используем запрос в БД");
            sql.StartConnection(
                    "select  p.nosological_patient_id, pp.id, m.namemu, nos.diagnosis, pp.deviation_count, pp.is_current_point  \n" +
                            "from vimis.patients_routes_stages p\n" +
                            "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                            "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                            "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                            "where pp.is_current_point = true and pp.deviation_count != '0' and "+MO+" order by p.nosological_patient_id desc limit 1;");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
                System.out.println("айди записи = " + sql.value);
                Deviation_count = sql.resultSet.getString("deviation_count");
            }

            System.out.println(
                    "У выбранного маршрута меняем значение deviation_count на 0 (таким образом уменьшаем количество просроченных маршрутов)");
            sql.UpdateConnection(
                    "update vimis.patients_routes_points set deviation_count = '0' where id = '" + sql.value + "';");
            System.out.println("Перезагружаем страницу");
            driver.navigate().refresh();
            System.out.println("Берём общее количество маршрутов по выбранной МО = " + MO + "");
            sql.StartConnection("select count(p.nosological_patient_id)  \n" +
                    "from vimis.patients_routes_stages p\n" +
                    "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                    "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                    "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                    "where pp.is_current_point = true  and "+MO+";");
            while (sql.resultSet.next()) {
                Counter = Integer.valueOf(sql.resultSet.getString("count"));
            }

            System.out.println("Берём количество просроченных маршрутов по выбранной МО = " + MO + "");
            sql.StartConnection("select count(p.nosological_patient_id)  \n" +
                    "from vimis.patients_routes_stages p\n" +
                    "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                    "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                    "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                    "where pp.is_current_point = true and pp.deviation_count != '0' and "+MO+";");
            while (sql.resultSet.next()) {
                CounterNegative = Integer.valueOf(sql.resultSet.getString("count"));
            }

            System.out.println("Делим полученные результаты, чтобы узнать в каком блоке расположена МО теперь");
            double Sum = (double) CounterNegative / Counter;
            if ((Sum >= 0.6)) {
                WaitElement(By.xpath(
                        "//section/div/div/span[contains(.,'Высокий')]/following-sibling::div/div//p[contains(.,'" + NameMo + "')]"));
                String Alls = driver.findElement(By.xpath(
                        "//section/div/div/span[contains(.,'Высокий')]/following-sibling::div/div//p[contains(.,'" + NameMo + "')]/following-sibling::div//span[@class='rating-mo-grid-info-line-body-counts-left-all']")).getText();
                String Deviations = driver.findElement(By.xpath(
                        "//section/div/div/span[contains(.,'Высокий')]/following-sibling::div/div//p[contains(.,'" + NameMo + "')]/following-sibling::div//span[@class='rating-mo-grid-info-line-body-counts-left-deviation']")).getText();
                All = Integer.valueOf(Alls);
                Deviation = Integer.valueOf(Deviations);
                Assertions.assertEquals(All, Counter, "Количество всех маршрутов не совпадает");
                Assertions.assertEquals(Deviation, CounterNegative, "Количество просроченных маршрутов не совпадает");
            }
            if ((Sum < 0.6 && Sum > 0.3)) {
                WaitElement(By.xpath(
                        "//section/div/div/span[contains(.,'Средний')]/following-sibling::div/div//p[contains(.,'" + NameMo + "')]"));
                String Alls = driver.findElement(By.xpath(
                        "//section/div/div/span[contains(.,'Средний')]/following-sibling::div/div//p[contains(.,'" + NameMo + "')]/following-sibling::div//span[@class='rating-mo-grid-info-line-body-counts-left-all']")).getText();
                String Deviations = driver.findElement(By.xpath(
                        "//section/div/div/span[contains(.,'Средний')]/following-sibling::div/div//p[contains(.,'" + NameMo + "')]/following-sibling::div//span[@class='rating-mo-grid-info-line-body-counts-left-deviation']")).getText();
                All = Integer.valueOf(Alls);
                Deviation = Integer.valueOf(Deviations);
                Assertions.assertEquals(All, Counter, "Количество всех маршрутов не совпадает");
                Assertions.assertEquals(Deviation, CounterNegative, "Количество просроченных маршрутов не совпадает");
            }
            if ((Sum <= 0.3)) {
                WaitElement(By.xpath(
                        "//section/div/div/span[contains(.,'Низкий')]/following-sibling::div/div//p[contains(.,'" + NameMo + "')]"));
                String Alls = driver.findElement(By.xpath(
                        "//section/div/div/span[contains(.,'Низкий')]/following-sibling::div/div//p[contains(.,'" + NameMo + "')]/following-sibling::div//span[@class='rating-mo-grid-info-line-body-counts-left-all']")).getText();
                String Deviations = driver.findElement(By.xpath(
                        "//section/div/div/span[contains(.,'Низкий')]/following-sibling::div/div//p[contains(.,'" + NameMo + "')]/following-sibling::div//span[@class='rating-mo-grid-info-line-body-counts-left-deviation']")).getText();
                All = Integer.valueOf(Alls);
                Deviation = Integer.valueOf(Deviations);
                Assertions.assertEquals(All, Counter, "Количество всех маршрутов не совпадает");
                Assertions.assertEquals(Deviation, CounterNegative, "Количество просроченных маршрутов не совпадает");
            }
            System.out.println("Возвращаем прежнее количество маршрутов у выбранной МО");
            sql.UpdateConnection(
                    "update vimis.patients_routes_points set deviation_count = '" + Deviation_count + "' where id = '" + sql.value + "';");
            System.out.println("Перезагружаем страницу");
            driver.navigate().refresh();
        } else {
            System.out.println("В блоке " + Name + " нет записей");
        }
    }
}
