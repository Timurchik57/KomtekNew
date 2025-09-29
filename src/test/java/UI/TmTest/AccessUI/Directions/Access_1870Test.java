package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
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

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Заполнение_карточки_пациента")
public class Access_1870Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOut;

    @Issue(value = "TEL-1870")
    @Link(name = "ТМС-1785", url = "https://team-1okm.testit.software/projects/5/tests/1785?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Создаём направление - вводим в фио по 2 символа - создаём консультацию")
    @Test
    @DisplayName("Создание пациента с ФИО менее 3 символов")
    public void Access_1870() throws InterruptedException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOut = new ConsultationOutgoingUnfinished(driver);

        System.out.println("Генерация снилса для добавления пользователя");
        authorizationObject.GenerateSnilsMethod();

        AuthorizationMethod(authorizationObject.OKB);

        /** Переход в создание консультации на оборудование */
        ClickElement(directionsForQuotas.ConsultationWait);

        System.out.println("Создание консультации");
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        WaitElement(directionsForQuotas.RemoteConsultationWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys(authorizationObject.GenerationSnils);
        Thread.sleep(2000);
        ClickElement(directionsForQuotas.BigSearchWait);

        System.out.println("Создание пациента для консультации");
        WaitElement(directionsForQuotas.CreatePatientWait);
        directionsForQuotas.CreatePatient.click();
        WaitElement(directionsForQuotas.ReferralsRemoteConsultation);

        System.out.println("Первая проверка - ввод 2 символов");
        inputWord(directionsForQuotas.LastName, "фф");
        WaitNotElement3(directionsForQuotas.Error40, 2);
        inputWord(directionsForQuotas.Name, "фф");
        WaitNotElement3(directionsForQuotas.Error40, 2);
        inputWord(directionsForQuotas.MiddleName, "фф");
        WaitNotElement3(directionsForQuotas.Error40, 2);

        System.out.println("Заполняем данные о пациенте для консультации");
        directionsForQuotas.AddSnils.sendKeys(authorizationObject.GenerationSnils);
        ClickElement(directionsForQuotas.DateWait);
        ClickElement(directionsForQuotas.SelectDate);
        ClickElement(directionsForQuotas.ManWait);
        SelectClickMethod(directionsForQuotas.TypeDocument, directionsForQuotas.SelectTypeDocument);
        inputWord(directionsForQuotas.Serial, "12344");
        inputWord(directionsForQuotas.Number, "1234566");
        actionElementAndClick(directionsForQuotas.Address);
        inputWord(directionsForQuotas.Address, "г Москва, ул Арбат, д 9АФ");
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.AddressHome);
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.NextPatient);

        System.out.println("Заполнение информации о направившем враче");
        directionsForQuotas.CreateRemoteConsul(false, "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"", "159 790 257 20", PName, "Женская консультация", "детской урологии-андрологии", "плановая", "Очное консультирование", "Паратиф A");

        System.out.println("Переходим в Консультации - Исходящие - проверяем, что создалась консультация");
        ClickElement(directionsForQuotas.SendConsul);
        Thread.sleep(2000);
        authorizationObject.LoadingTime(10);
        ClickElement(consultationOut.Consultation);
        WaitElement(consultationOut.LastName);
        String FIO = driver.findElement(consultationOut.LastName).getText();

        Assertions.assertEquals(FIO, "ф ф ф, 0", "Пациент с ФИО, где символов меньше 2 не создался");
    }
}
