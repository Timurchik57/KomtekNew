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
public class Access_1270Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;
    public String NameMo;
    public String MO;
    public Integer PatientAll;


    @Test
    @Issue(value = "TEL-1270")
    @Link(name = "ТМС-1466", url = "https://team-1okm.testit.software/projects/5/tests/1466?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Фильтрация по наличию отклонений в списке пациентов ОМП")
    @Description("Переходим в Аналитика Мо по ОМП, переходим во все МО и проверяем отображение с чек боксами \"с отклонениями\" ")
    public void Access_1270() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        Thread.sleep(3000);

        System.out.println("Проверяем где есть маршруты, во всех блоках");
        analyticsMO.QuantityStackMethod();

        System.out.println("Переходим во все блоки, у которых есть мо с этапами и сверяем количество пациентов");
        RoutestMethod(analyticsMO.QuantityRoutestTall, "Высокий");
        RoutestMethod(analyticsMO.QuantityRoutestAverage, "Средний");
        RoutestMethod(analyticsMO.QuantityRoutestlow, "Низкий");
    }

    /**
     * Метод, который считает количество МО в каждом блоке, переходит в каждую МО и сверяет количество пациентов
     */
    @Step("Считаем количество МО в блоке {1}, переходим в каждую МО и сверяем количество пациентов")
    public void QuantityPatientMethod(By Routes, String Name) throws InterruptedException, SQLException {
        analyticsMO = new AnalyticsMO(driver);

        System.out.println("Считаем МО в " + Name + "");
        List<WebElement> Quantity = driver.findElements(Routes);
        for (int i = 1; i < Quantity.size() + 1; i++) {
            Thread.sleep(1500);
            NameMo = driver.findElement(By.xpath(
                    "//section/div/div/span[contains(.,'" + Name + "')]/following-sibling::div/div[" + i + "]//p")).getText();

            /** Проверяем значение МО = null (2836) */
            if (NameMo.contains("Неизвестно")) {
                MO = "m.namemu is null";
            } else {
                MO = "m.namemu = '" + NameMo + "'";
            }
            ClickElement(By.xpath(
                    "//section/div/div/span[contains(.,'" + Name + "')]/following-sibling::div/div[" + i + "]//p"));

            System.out.println("Выбираем флаг С отклонениями");
            Thread.sleep(1500);
            ClickElement(analyticsMO.CheckBez);
            Thread.sleep(1500);
            ClickElement(analyticsMO.Apply);
            Thread.sleep(1500);
            WaitNotElement3(analyticsMO.ApplyLoading, 30);
            List<WebElement> Patient = driver.findElements(analyticsMO.PatientAll);
            sql.StartConnection(
                    "select count(distinct (pr.patients_routes_point_id)) from vimis.patients_routes_stages p\n" +
                            "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                            "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                            "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                            "left join vimis.patients_routes_deviations pr on pp.id = pr.patients_routes_point_id\n" +
                            "where pp.is_current_point = true and "+MO+" and pp.deviation_count > '0';");
            while (sql.resultSet.next()) {
                PatientAll = Integer.valueOf(sql.resultSet.getString("count"));
            }
            if (PatientAll == null) {
                PatientAll = 0;
            }
            Assertions.assertEquals(Patient.size(), PatientAll, "Количество пациентов не совпадает");

            System.out.println("Выбираем флаг Без отклонений");
            Thread.sleep(1500);
            ClickElement(analyticsMO.CheckBez);
            ClickElement(analyticsMO.CheckS);
            Thread.sleep(1500);
            ClickElement(analyticsMO.Apply);
            Thread.sleep(1500);
            WaitNotElement3(analyticsMO.ApplyLoading, 30);
            Patient = driver.findElements(analyticsMO.PatientAll);
            sql.StartConnection("select count(*) from vimis.patients_routes_stages p\n" +
                    "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                    "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                    "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                    "left join vimis.patients_routes_deviations pr on pp.id = pr.patients_routes_point_id\n" +
                    "where pp.is_current_point = true and "+MO+" and pp.deviation_count = '0' or \n" +
                    "pp.is_current_point = true and "+MO+" and pp.deviation_count is null;");
            while (sql.resultSet.next()) {
                PatientAll = Integer.valueOf(sql.resultSet.getString("count"));
            }
            if (PatientAll == null) {
                PatientAll = 0;
            }
            Assertions.assertEquals(Patient.size(), PatientAll, "Количество пациентов не совпадает");
            driver.navigate().back();
        }
    }

    /**
     * Метод, который выбирает МО в определённом блоке
     */
    public void RoutestMethod(By Routes, String Name) throws InterruptedException, SQLException {
        analyticsMO = new AnalyticsMO(driver);
        if (Name == "Высокий" & AnalyticsMO.TallMO) {
            QuantityPatientMethod(Routes, Name);
        }
        if (Name == "Высокий" & !analyticsMO.TallMO) {
            System.out.println("В блоке " + Name + " нет МО");
        }
        if (Name == "Средний" & AnalyticsMO.AverageMO) {
            QuantityPatientMethod(Routes, Name);
        }
        if (Name == "Средний" & !analyticsMO.AverageMO) {
            System.out.println("В блоке " + Name + " нет МО");
        }
        if (Name == "Низкий" & AnalyticsMO.lowMO) {
            QuantityPatientMethod(Routes, Name);
        }
        if (Name == "Низкий" & !analyticsMO.lowMO) {
            System.out.println("В блоке " + Name + " нет МО");
        }
    }
}
