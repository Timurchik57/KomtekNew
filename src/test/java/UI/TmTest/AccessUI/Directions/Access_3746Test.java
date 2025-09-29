package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
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
@Tag("Консультация_на_оборудование")
@Tag("Проверка_БД")
@Tag("Заполнение_карточки_пациента")
@Tag("РРП")
public class Access_3746Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOut;

    @Test
    @Issue(value = "TEL-3746")
    @Owner(value = "Галиакберов Тимур")
    @Description("Создаём напрвление изменяем данные о пациенте, после проверяем что данные изменились в iemk.op_patient_reg")
    @DisplayName("Изменение данных о пациенте в iemk.op_patient_reg")
    public void Access_3746() throws SQLException, InterruptedException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOut = new ConsultationOutgoingUnfinished(driver);

        System.out.println("Обновляем данные в iemk.op_patient_reg");
        sql.UpdateConnection("update iemk.op_patient_reg set middlename = 'тестик' where snils = '62137738890';");

        sql.StartConnection("Select * from iemk.op_patient_reg where snils = '62137738890';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("middlename");
        }
        Assertions.assertEquals(sql.value, "тестик", "middlename должно быть тестик");

        System.out.println("Создание консультации");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        directionsForQuotas.CreateRemoteConsul(false, "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"", "621 377 388 90", PName, "Женская консультация", "детской урологии-андрологии", "плановая", "Очное консультирование", "Паратиф A");
        ClickElement(directionsForQuotas.SendConsul);
        Thread.sleep(2000);

        sql.StartConnection("Select * from iemk.op_patient_reg where snils = '62137738890';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("middlename");
        }
        Assertions.assertEquals(sql.value, "Тестов", "middlename должно быть Тестов (такое установлено в РРП для этого пользователя)");
    }
}