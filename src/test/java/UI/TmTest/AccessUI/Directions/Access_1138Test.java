package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.SQL;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
public class Access_1138Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    ConsultationUnfinished consultationUn;
    SQL sql;

    @Issue(value = "TEL-1138")
    @Link(name = "ТМС-1431", url = "https://team-1okm.testit.software/projects/5/tests/1431?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Номер телефона сотрудника запросивший консультацию")
    @Description("Перейти в Консультации - Создать направление - Направление на консультацию - Создаём направление, ищем номер пользователя, которого указали врачом, сверяем с UI")
    public void Access_1138() throws SQLException, InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        consultationUn = new ConsultationUnfinished(driver);
        sql = new SQL();

        System.out.println("Авторизуемся и переходим в создание удалённой консультации");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        directionsForQuotas.CreateRemoteConsul(false, "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"", "159 790 257 20", PName, "Женская консультация", "детской урологии-андрологии", "плановая", "Очное консультирование", "Паратиф A");
        ClickElement(directionsForQuotas.SendConsul);

        System.out.println("Авторизуемся под МО в которую направили консультацию");
        if (KingNumber == 1 | KingNumber == 2) {
            AuthorizationMethod(authorizationObject.YATCKIV);
        }
        if (KingNumber == 4) {
            AuthorizationMethod(authorizationObject.Kondinsk);
        }
        ClickElement(consultationUn.UnfinishedWait);
        ClickElement(consultationUn.DESK);
        Thread.sleep(1500);
        authorizationObject.LoadingTime(3);
        ClickElement(consultationUn.FirstWait);
        WaitElement(consultationUn.Phone);
        String Phone = driver.findElement(consultationUn.Phone).getText();
        sql.StartConnection("SELECT phone FROM telmed.users WHERE sname = '"+PLastNameGlobal+"'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("phone");
        }
        System.out.println("Сверяем номера телефонов");
        Assertions.assertEquals(Phone, sql.value, "Номер телефона не совпадает");
    }
}
