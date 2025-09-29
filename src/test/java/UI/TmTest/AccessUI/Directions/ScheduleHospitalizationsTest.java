package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.HospitalizationSchedule;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Проверка_БД")
public class ScheduleHospitalizationsTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    HospitalizationSchedule hospitalizationS;
    List<String> Department;
    List<String> SQL;

    @Test
    @Issue(value = "TEL-986")
    @Link(name = "ТМС-1325", url = "https://team-1okm.testit.software/projects/5/tests/1325?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение подразделения и отделения в Расписании госпитализаций")
    @Description("Перейти в Расписание госпитализаций - Настройка расписания госпитализаций - Добавить расписание. Выбрать подразделение у которого есть наименование отделения")
    public void ScheduleHospitalizations() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        hospitalizationS = new HospitalizationSchedule(driver);

        System.out.println("Авторизуемся и переходим в Расписание госпитализаций - Настройка расписания госпитализаций - Добавить расписание");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(hospitalizationS.HospitalizationWait);
        ClickElement(hospitalizationS.Settings);
        ClickElement(hospitalizationS.Add);
        ClickElement(hospitalizationS.FirstPeriodWait);

        System.out.println("Выбираем период и подразделение - Детская поликлиника");
        ClickElement(hospitalizationS.Today);
        Thread.sleep(1000);
        ClickElement(hospitalizationS.TodayTwo);
        Thread.sleep(1000);
        SelectClickMethod(hospitalizationS.DivisionWait, hospitalizationS.SelectDivisionDP);

        System.out.println("Нажимаем на отделение и берём все значения");
        Thread.sleep(1500);
        ClickElement(hospitalizationS.DepartmentWait);
        Thread.sleep(1000);
        ScheduleHospitalizationsMethod("Детская поликлиника", "ambulance_subdivision_name", 1);

        System.out.println("Выбираем период и подразделение - Рентгенологическое отделение");
        SelectClickMethod(hospitalizationS.DivisionWait, hospitalizationS.SelectDivisionRO);
        Thread.sleep(1500);

        System.out.println("Нажимаем на отделение и берём все значения");
        ClickElement(hospitalizationS.DepartmentWait);
        Thread.sleep(1000);
        ScheduleHospitalizationsMethod("Рентгенологическое отделение", "oid", 2);
    }

    @Step("Метод для выбора подразделения и последующая проверка отделения с БД")
    public void ScheduleHospitalizationsMethod(String NameDivision, String NamePole, Integer number) throws SQLException {
        Department = new ArrayList<String>();
        List<WebElement> department = driver.findElements(hospitalizationS.SelectDepartmentList);
        for (int i = 0; i < department.size(); i++) {
            Department.add(department.get(i).getText());
        }
        Collections.sort(Department);

        System.out.println("Берём значения отделения из БД");
        SQL = new ArrayList<String>();
        sql.StartConnection(
                "SELECT msm.namemu, m.depart_name, m.ambulance_subdivision_name, m.\"oid\"  FROM dpc.mo_branches m \n" +
                        "left join dpc.mo_subdivision_structure ms on m.depart_oid = ms.depart_oid \n" +
                        "left join dpc.mis_sp_mu msm on ms.mo_oid  = msm.\"oid\" \n" +
                        "where msm.namemu = 'БУ ХМАО-Югры \"Окружная клиническая больница\"' and m.depart_name = '" + NameDivision + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("" + NamePole + "");
            if (number == 1) {
                SQL.add(sql.value);
            }
            if (number == 2) {
                SQL.add("- (" + sql.value + ")");
            }
        }
        Collections.sort(SQL);

        System.out.println("Проверяем совпадение значений");
        Assertions.assertEquals(Department, SQL, "Значение отделения нв Web не соответствует БД");
    }
}
