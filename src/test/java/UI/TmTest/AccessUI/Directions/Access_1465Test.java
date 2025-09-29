package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Заполнение_карточки_пациента")
public class Access_1465Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;

    @Issue(value = "TEL-1465")
    @Link(name = "ТМС-1664", url = "https://team-1okm.testit.software/projects/5/tests/1664?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Валидация серии и номера документа при создании направления")
    @Description("Переходим в создание направления на диагностику/консультацию - выбираем пациента и проверяем у него, что поля серия и номер документа обязательны")
    public void Access_1465() throws  InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        equipmentSchedule = new EquipmentSchedule(driver);
        AuthorizationMethod(authorizationObject.OKB);

        ClickElement(directionsForQuotas.ConsultationWait);

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
        if (KingNumber == 4) {
            Thread.sleep(3000);
        }
        WaitElement(directionsForQuotas.PatientDataWait);
        WaitElement(directionsForQuotas.SerialWait);
        inputWord(directionsForQuotas.Serial, " ");
        inputWord(directionsForQuotas.Number, " ");
        WaitElement(directionsForQuotas.SerialError);
        WaitElement(directionsForQuotas.NumberError);
    }
}
