package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
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
@Tag("Консультация_на_оборудование")
@Tag("Проверка_БД")
public class AccessDivisionTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    SQL sql;

    @Test
    @DisplayName("Проверка отображения отделений у выбранного подразделения")
    @Description("Проверка отображения отделений у выбранного подразделения при создании направления на оборудование")
    public void AccessDivision() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        sql = new SQL();
        System.out.println("Создание консультации на оборудование");

        System.out.println("Авторизация  и переход в создание направления");
        AuthorizationMethod(authorizationObject.YATCKIV);

        /** Переход в создание консультации на оборудование */
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);

        System.out.println("Создание консультации");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.CreateWait);
        directionsForQuotas.Create.click();
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.DistrictDiagnostic.click();
        WaitElement(directionsForQuotas.DistrictDiagnosticWait);
        directionsForQuotas.Next.click();
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys("159 790 257 20");
        WaitElement(directionsForQuotas.PatientDataWait);
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);

        System.out.println("Заполнение информации о направившем враче");
        WaitElement(directionsForQuotas.InfoDoctorWait);
        WaitElement(directionsForQuotas.Division);
        SelectClickMethod(directionsForQuotas.Division, directionsForQuotas.SelectDivision);
        Thread.sleep(1000);
        WaitElement(directionsForQuotas.DepartmentWait);
        Thread.sleep(4000);
        directionsForQuotas.Department.click();
        Thread.sleep(1000);
        List<String> getDepartment = new ArrayList<String>();
        List<WebElement> department = driver.findElements(directionsForQuotas.SelectDepartment);
        for (int i = 0; i < department.size(); i++) {
            getDepartment.add(department.get(i).getText());
        }
        Collections.sort(getDepartment);
        List<String> getBd = new ArrayList<String>();

        System.out.println("Проверяем значения в БД");
        sql.StartConnection("SELECT m.namemu, depart_name, ambulance_subdivision_name  FROM dpc.mo_branches x\n" +
                                    "join mis_sp_mu m on m.\"oid\" = x.mo_oid \n" +
                                    "WHERE m.namemu  IN ('БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"') and depart_name in ('Женская консультация');");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("ambulance_subdivision_name");
            getBd.add(sql.value);
        }
        Collections.sort(getBd);
        Assertions.assertEquals(getDepartment, getBd);
        System.out.println("Значения " + getDepartment + " и " + getBd + " совпадают ");
    }
}
