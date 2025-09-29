package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
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
@Tag("Консультация_на_оборудование")
@Tag("Проверка_БД")
public class CreateDirectionsAccessSpecializationTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;

    @Test
    @Issue(value = "TEL-1015")
    @Link(name = "ТМС-1350", url = "https://team-1okm.testit.software/projects/5/tests/1350?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка специальности при создании консультации на диагностику")
    @Description("Переходим в Направления на квоты - Создать направление - Направление на диагностику - Ввести снилс созданного пользователя - Далее - нажимаем на специальность и сверяем с таблицей dpc.medical_and_pharmaceutical_specialties.name (только актуальные, т.е dateout = NULL)")
    public void CreateDirectionsAccessSpecialization() throws InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Авторизуемся и переходим в создание консультации");
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            AuthorizationMethod(authorizationObject.OKB);
        }
        if (KingNumber == 3) {
            AuthorizationMethod(authorizationObject.Seva);
        }

        directionsForQuotas.CreateConsultationEquipmentMethod();
        ClickElement(directionsForQuotas.Specialization);
        List<String> Select = new ArrayList<String>();
        WaitElement(authorizationObject.Xplacement);
        List<WebElement> Specialization = driver.findElements(directionsForQuotas.SelectSpecializationBottom);
        for (int i = 0; i < Specialization.size(); i++) {
            Select.add(Specialization.get(i).getText());
        }
        Collections.sort(Select);

        System.out.println("Берём значение специальности из бд");
        List<String> SelectSql = new ArrayList<String>();
        sql.StartConnection(
                "Select * from dpc.medical_and_pharmaceutical_specialties where dateout is null and parent is not null;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("name");
            SelectSql.add(sql.value);
        }
        Collections.sort(SelectSql);

        System.out.println("Проверяем совпадение значений");
        assertListsEqualIgnoreOrder(Select, SelectSql, "Значения специальности в вебе и бд не совпадают");
    }
}
