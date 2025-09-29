package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationOutgoingUnfinished;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Заполнение_карточки_пациента")
@Tag("Удалённая_консультация")
public class Access_1872Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    ConsultationOutgoingUnfinished consultationOut;

    @Issue(value = "TEL-1872")
    @Link(name = "ТМС-1819", url = "https://team-1okm.testit.software/projects/5/tests/1819?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Создаём направление - создаём нового пациента - выбираем мать без снилс - заполняем все обязательные поля - создаём консультацию - проверяем что нет ошибок")
    @Test
    @DisplayName("Создание ребёнка до 2 месяцев с матерью без снилса")
    public void Access_1872() throws InterruptedException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        consultationOut = new ConsultationOutgoingUnfinished(driver);

        System.out.println("Создание консультации");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        WaitElement(directionsForQuotas.RemoteConsultationWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        ClickElement(directionsForQuotas.BigSearchWait);

        System.out.println("Создание пациента для консультации");
        WaitElement(directionsForQuotas.CreatePatientWait);
        directionsForQuotas.CreatePatient.click();
        WaitElement(directionsForQuotas.ReferralsRemoteConsultation);

        System.out.println("Выбираем причину отсутствия СНИЛС");
        SelectClickMethod(directionsForQuotas.NotSnils, directionsForQuotas.NotSnils2Mouth);
        ClickElement(directionsForQuotas.MatherSnils);
        WaitElement(directionsForQuotas.BigLastName);
        inputWord(driver.findElement(directionsForQuotas.BigLastName), "Мать ");
        ClickElement(directionsForQuotas.SearchWait("3"));
        ClickElement(directionsForQuotas.SearchMother);
        ClickElement(directionsForQuotas.Choose("2"));

        ClickElement(directionsForQuotas.AddMother);

        System.out.println("Заполняем данные о пациенте");
        WaitElement(directionsForQuotas.ReferralsRemoteConsultation);
        inputWord(directionsForQuotas.LastName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        inputWord(directionsForQuotas.Name, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        inputWord(directionsForQuotas.MiddleName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        ClickElement(directionsForQuotas.DateWait);
        ClickElement(directionsForQuotas.SelectDate);
        ClickElement(directionsForQuotas.Time);
        inputWord(driver.findElement(directionsForQuotas.SerialBorn), getRandomWord(5, "0123456789"));
        inputWord(driver.findElement(directionsForQuotas.NumberBorn), getRandomWord(7, "0123456789"));
        inputWord(driver.findElement(directionsForQuotas.NumberChild), "12");
        ClickElement(directionsForQuotas.ManWait);
        actionElementAndClick(directionsForQuotas.Address);
        inputWord(directionsForQuotas.Address, "г Москва, ул Арбат, д 9АФ");
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.AddressHome);
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.NextPatient);

        System.out.println("Заполнение информации о направившем враче");
        directionsForQuotas.CreateRemoteConsul(false, "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"", "159 790 257 20", PName, "Женская консультация", "детской урологии-андрологии", "плановая", "Очное консультирование", "Паратиф A");
        ClickElement(directionsForQuotas.SendConsul);
        Thread.sleep(2000);
    }
}
