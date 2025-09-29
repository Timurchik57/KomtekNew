package UI.TmTest.AccessUI.Statistick;

import Base.SQL;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
import io.qameta.allure.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
public class Access_1108Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    SQL sql;
    AnalyticsMO analyticsMO;
    public Integer All = 0;
    public Integer Counter;
    public String URL;
    public Integer QuantityState;
    public String stageCode;
    public String stageName;
    public Integer patientsCount;
    public Integer CounterNegative;

    @Test
    @Issue(value = "TEL-1108")
    @Link(name = "ТМС-1452", url = "https://team-1okm.testit.software/projects/5/tests/1452?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение аналитики по этапам ОМП")
    @Description("Перейти в Статистика - Аналитика МО по ОМП. Проверить Отображение аналитики по этапам ОМП")
    public void Access_1108() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        sql = new SQL();
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Авторизуемся и переходим в Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        WaitElement(analyticsMO.StateHeader);
        WaitElement(analyticsMO.QuantityState);
        Thread.sleep(1500);

        System.out.println("Считаем количество всех этапов");
        List<WebElement> Quantity = driver.findElements(analyticsMO.QuantityState);
        QuantityState = Quantity.size();

        System.out.println("Берём количество всех всех этапов");
        for (int i = 1; i < Quantity.size() + 1; i++) {
            String Alls = driver.findElement(
                    By.xpath("(//ul[@class='stages-wmd__list']/li[" + i + "]//span)[1]")).getText();
            All += Integer.valueOf(Alls);
        }
        System.out.println(All);

        System.out.println("Сверяем количество всех этапов");
        sql.StartConnection("SELECT count(id) FROM vimis.patients_routes_stages;");
        while (sql.resultSet.next()) {
            Counter = Integer.valueOf(sql.resultSet.getString("count"));
        }
        Assertions.assertEquals(Counter, All, "Количество всех этапов не совпадает");
        QuantityStatMethod();
    }

    @Step("Подсчёт процента в Этапы ОМП")
    public void QuantityStatMethod() throws SQLException {
        System.out.println("Считаем количество этапов на UI");
        List<String> QuantitySQL = new ArrayList<>();

        sql.StartConnection(
                "SELECT stage_name, stage_code FROM vimis.patients_routes_stages group by stage_name, stage_code order by stage_code;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("stage_name");
            QuantitySQL.add(sql.value);
        }
        for (int i = 1; i < QuantitySQL.size() + 1; i++) {
            System.out.println("Берём процент отклонений на UI");
            String SumUI = driver.findElement(
                    By.xpath("(//ul[@class='stages-wmd__list']/li[" + i + "]//span)[2]")).getText();

            System.out.println("Берём stageName и stageCode у " + i + " записи");
            Integer OFFSET = i - 1;
            sql.StartConnection(
                    "SELECT stage_name, stage_code, count(stage_code) FROM vimis.patients_routes_stages group by stage_name, stage_code order by count desc limit 1 OFFSET " + OFFSET + ";");
            while (sql.resultSet.next()) {
                stageCode = sql.resultSet.getString("stage_code");
                stageName = sql.resultSet.getString("stage_name");
            }

            System.out.println("Берём количество отклонений в бд для " + stageName + " и " + stageCode + "");

            sql.StartConnection(
                    "SELECT count(*) FROM vimis.patients_routes_stages p\n" +
                            "join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                            "Where p.stage_name = '"+ stageName +"' and p.stage_code = '"+ stageCode +"' and pp.deviation_count != 0;");
            while (sql.resultSet.next()) {
                CounterNegative = Integer.valueOf(sql.resultSet.getString("count"));
                System.out.println(CounterNegative);
            }

            System.out.println("Берём количество всех этапов в бд для " + stageName + " и " + stageCode + "");
            sql.StartConnection(
                    "SELECT count(id) FROM vimis.patients_routes_stages Where stage_name = '" + stageName + "' and stage_code = '" + stageCode + "';");
            while (sql.resultSet.next()) {
                patientsCount = Integer.valueOf(sql.resultSet.getString("count"));
                System.out.println(patientsCount);
            }

            System.out.println("Делим количество отклонений на все этапы, чтобы узнать процент отклонений для данного этапа");
            if (CounterNegative == 0) {
                Assertions.assertEquals(CounterNegative + "%", SumUI, "Процент отклонений не совпадает");
                System.out.println("Процент отклонений равен 0");
            } else {
                double Sum = ((double) CounterNegative / patientsCount) * 100;
                StringUtils.substring(String.valueOf(Sum), 0, 5);
                System.out.println(Sum);
                int result = (int) Math.round(Sum);
                System.out.println(Sum);
                System.out.println(result);
                Assertions.assertEquals(result + "%", SumUI, "Процент отклонений не совпадает");
            }
        }
    }

}
