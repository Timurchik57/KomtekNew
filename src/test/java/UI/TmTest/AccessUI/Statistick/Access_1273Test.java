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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("Основные")
public class Access_1273Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    SQL sql;
    public String NameMo;
    public String Deviation;
    public String Id;
    public String Warning_message;
    public String Deviation_kind;
    public String MO;

    @Test
    @Issue(value = "TEL-1273")
    @Link(name = "ТМС-1468", url = "https://team-1okm.testit.software/projects/5/tests/1468?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение количества отклонений у конкретного пациента")
    @Description("Переходим в Аналитика Мо по ОМП, переходим во все МО и проверяем отображение количества отклонений у пациентов")
    public void Access_1273() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sql = new SQL();

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(analyticsMO.Analytics);
        Thread.sleep(2000);

        System.out.println("Проверяем где есть маршруты, во всех блоках");
        analyticsMO.QuantityStackMethod();

        System.out.println("Переходим во все блоки, у которых есть мо с этапами и сверяем количество пациентов");
        RoutestMethod(analyticsMO.QuantityRoutestTall, "Высокий");
        RoutestMethod(analyticsMO.QuantityRoutestAverage, "Средний");
        RoutestMethod(analyticsMO.QuantityRoutestlow, "Низкий");
    }

    /**
     * Метод с помощью которого переходим во все отклонения пациента и сверяем информацию
     */
    public void DeviationMethod() throws InterruptedException, SQLException {

        System.out.println("Переходим по порядку в отклонения каждого пациента");
        List<WebElement> Patient = driver.findElements(analyticsMO.PatientAll);
        for (int j = 1; j < Patient.size() + 1; j++) {

            System.out.println("Берём количество отклонений у " + j + " пациента и нажимаем на эти отклонения");
            Thread.sleep(1500);
            String CountDeviation = driver.findElement(By.xpath("//tbody/tr[" + j + "]/td[last()]//p")).getText();
            if (Integer.valueOf(CountDeviation) != 0) {
                driver.findElement(By.xpath("//tbody/tr[" + j + "]/td[last()]//p")).click();
                WaitElement(analyticsMO.DeviationHeader);

                System.out.println("Находим нужного пациента в бд, чтобы взять его id");
                int offset = j - 1;
                sql.StartConnection(
                        "select  p.nosological_patient_id, m.namemu, nos.diagnosis, pp.id , pr.patients_routes_point_id, count(pr.patients_routes_point_id), pp.deviation_count, pp.is_current_point  \n" +
                                "from vimis.patients_routes_stages p\n" +
                                "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                                "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                                "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                                "left join vimis.patients_routes_deviations pr on pp.id = pr.patients_routes_point_id\n" +
                                "where pp.is_current_point = true  and "+MO+" group by pr.patients_routes_point_id, p.nosological_patient_id, m.namemu,\n" +
                                "nos.diagnosis, pp.id , pp.deviation_count, pp.is_current_point order by id asc limit 1 offset " + offset + ";");
                while (sql.resultSet.next()) {
                    Id = sql.resultSet.getString("id");
                }

                System.out.println("Находим отклонения у пациента в бд");
                List<String> deviation_kind = new ArrayList<>();
                List<String> warning_message = new ArrayList<>();
                sql.StartConnection(
                        "select  pr.deviation_kind, pr.warning_message from vimis.patients_routes_stages p\n" +
                                "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                                "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                                "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                                "left join vimis.patients_routes_deviations pr on pp.id = pr.patients_routes_point_id\n" +
                                "where pp.is_current_point = true  and "+MO+" and pp.id =" + Id + ";");
                while (sql.resultSet.next()) {
                    Deviation_kind = sql.resultSet.getString("deviation_kind");
                    Warning_message = sql.resultSet.getString("warning_message");
                    if (Integer.valueOf(Deviation_kind) == 1) {
                        deviation_kind.add("Порядок ОМП");
                    }
                    if (Integer.valueOf(Deviation_kind) == 2) {
                        deviation_kind.add("ПГГ");
                    }
                    if (Integer.valueOf(Deviation_kind) == 3) {
                        deviation_kind.add("Клинические рекомендации");
                    }
                    warning_message.add(Warning_message);
                }
                Collections.sort(deviation_kind);
                Collections.sort(warning_message);

                System.out.println("Берём Тип отклонения на UI");
                List<String> Type = new ArrayList<>();
                List<WebElement> TypeCount = driver.findElements(analyticsMO.TypeDeviation);
                for (int i = 0; i < TypeCount.size(); i++) {
                    Type.add(TypeCount.get(i).getText());
                }
                Collections.sort(Type);

                System.out.println("Берём Отклонения на UI +\n");
                List<String> Deviation = new ArrayList<>();
                List<WebElement> Count = driver.findElements(analyticsMO.Deviation);
                for (int i = 0; i < Count.size(); i++) {
                    Deviation.add(Count.get(i).getText());
                }
                Collections.sort(Deviation);
                Assertions.assertEquals(deviation_kind, Type, "Значения Тип отклонения не совпадают");
                Assertions.assertEquals(warning_message, Deviation, "Значения Отклонения не совпадают");

                System.out.println("Считаем количество каждого типа отклонений");
                int OMP = Collections.frequency(deviation_kind, "Порядок ОМП");
                int PGG = Collections.frequency(deviation_kind, "ПГГ");
                int KR = Collections.frequency(deviation_kind, "Клинические рекомендации");
                String OMPUi = (driver.findElement(analyticsMO.OMP).getText()).substring(38);
                String PGGUi = (driver.findElement(analyticsMO.PGG).getText()).substring(30);
                String KRUi = (driver.findElement(analyticsMO.KR).getText()).substring(52);
                Assertions.assertEquals(
                        OMP, Integer.valueOf(OMPUi), "Количество отклонений по клиническим рекомендациям не совпадает");
                Assertions.assertEquals(PGG, Integer.valueOf(PGGUi), "Количество отклонений по ПГГ не совпадает");
                Assertions.assertEquals(KR, Integer.valueOf(KRUi), "Количество отклонений по Порядок ОМП не совпадает");
                ClickElement(analyticsMO.Close);
            } else {
                System.out.println("У пациента под номером " + j + " нет отклонений");
            }
        }
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
            ClickElement(By.xpath(
                    "//section/div/div/span[contains(.,'" + Name + "')]/following-sibling::div/div[" + i + "]//p"));
            Thread.sleep(2500);
            WaitElement(analyticsMO.FirstPatient);

            /** Проверяем значение МО = null (2836) */
            if (NameMo.contains("Неизвестно")) {
                MO = "m.namemu is null";
            } else {
                MO = "m.namemu = '" + NameMo + "'";
            }

            System.out.println("Берём все значения отклонений по порядку сверху вниз");
            List<WebElement> Patient = driver.findElements(analyticsMO.PatientAll);
            List<String> DeviationCount = new ArrayList<>();
            for (int j = 1; j < Patient.size() + 1; j++) {
                String QuantityDeviation = driver.findElement(
                        By.xpath("//tbody/tr[" + j + "]/td[last()]//p")).getText();
                DeviationCount.add(QuantityDeviation);
            }

            System.out.println("Берём все значения отклонений по порядку сверху вниз из БД");
            List<String> DeviationCountSQL = new ArrayList<>();
            for (int k = 0; k < Patient.size(); k++) {
                sql.StartConnection(
                        "select  p.nosological_patient_id, m.namemu, nos.diagnosis, pp.id , pr.patients_routes_point_id, count(pr.patients_routes_point_id), pp.deviation_count, pp.is_current_point  \n" +
                                "from vimis.patients_routes_stages p\n" +
                                "left join vimis.nosological_patients nos on p.nosological_patient_id = nos.id \n" +
                                "left join vimis.patients_routes_points pp on p.id = pp.patients_routes_stage_id\n" +
                                "left join dpc.mis_sp_mu m on pp.mo_oid = m.oid\n" +
                                "left join vimis.patients_routes_deviations pr on pp.id = pr.patients_routes_point_id\n" +
                                "where pp.is_current_point = true  and "+MO+" group by pr.patients_routes_point_id, p.nosological_patient_id, m.namemu,\n" +
                                "nos.diagnosis, pp.id , pp.deviation_count, pp.is_current_point order by id asc limit 1 offset " + k + ";");
                while (sql.resultSet.next()) {
                    Deviation = sql.resultSet.getString("deviation_count");
                }
                DeviationCountSQL.add(Deviation);
            }
            Assertions.assertEquals(
                    DeviationCount, DeviationCountSQL, "Количество отклонений у пациентов не совпадает с БД");
            DeviationMethod();
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
