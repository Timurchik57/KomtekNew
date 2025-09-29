package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Проверка_БД")
public class Access_1141Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    SQL sql;

    @Test
    @Issue(value = "TEL-1141")
    @Link(name = "ТМС-1429", url = "https://team-1okm.testit.software/projects/5/tests/1429?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Отображение анатомических областей при создании направлений на квоту")
    @Description("Перейти в Консультации - создать консультацию на диагностику, На странице заполнения данных о враче - нажать на Анатомические области, сравнить с данными в БД")
    public void Access_1141() throws SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        sql = new SQL();

        System.out.println("Авторизация и переход в создание консультации");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);

        System.out.println("Создать консультацию - добавить пациента");
        WaitElement(directionsForQuotas.Heading);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.DistrictDiagnosticWait);
        ClickElement(directionsForQuotas.NextWait);
        directionsForQuotas.Snils.sendKeys("15979025720");
        WaitElement(directionsForQuotas.PatientDataWait);
        WaitElement(directionsForQuotas.NextWait);
        Thread.sleep(1000);
        actionElementAndClick(directionsForQuotas.Next);

        System.out.println("Заполнение информации о направившем враче");
        WaitElement(directionsForQuotas.InfoDoctorWait);
        ClickElement(directionsForQuotas.AnatomicalAreas);
        driver.findElement(directionsForQuotas.AnatomicalAreas).sendKeys("Лучевая");
        Thread.sleep(1000);
        List<String> WebAnatomic = new ArrayList<>();
        List<WebElement> Web = driver.findElements(authorizationObject.SelectALL);
        for (int i = 0; i < Web.size(); i++) {
            WebAnatomic.add(Web.get(i).getText());
        }

        System.out.println("Берём значение в БД");
        List<String> SQLAnatomic = new ArrayList<>();
        sql.StartConnection("Select name from dpc.anatom_localation where name like '%Лучевая%';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("name");
            SQLAnatomic.add(sql.value);
        }

        assertListsEqualIgnoreOrder(WebAnatomic,SQLAnatomic, "Данные на вебе по Анатомическим областям не совпадают с БД");
    }
}
