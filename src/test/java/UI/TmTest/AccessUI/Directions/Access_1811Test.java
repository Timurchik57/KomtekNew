package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
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
@Tag("Удалённая_консультация")
@Tag("Заполнение_карточки_пациента")
public class Access_1811Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;

    @Issue(value = "TEL-1811")
    @Issue(value = "TEL-2024")
    @Link(name = "ТМС-1816", url = "https://team-1okm.testit.software/projects/5/tests/1816?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Создаём направление - выбираем тип документа Свидетельство о рождении - проверяем ввод по шаблону серии и номера")
    @Test
    @DisplayName("Тип документа при создании/обновлении пациента \"Свидетельство о рождении\"")
    public void Access_1811() throws InterruptedException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

        System.out.println("Генерация снилса для добавления пользователя");
        authorizationObject.GenerateSnilsMethod();

        System.out.println("Переход в создание консультации на оборудование");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);

        System.out.println("Создание консультации");
        ClickElement(directionsForQuotas.CreateWait);
        ClickElement(directionsForQuotas.RemoteConsultationWait);
        ClickElement(directionsForQuotas.NextWait);
        WaitElement(directionsForQuotas.SnilsWait);
        ClickElement(directionsForQuotas.BigSearchWait);
        WaitElement(directionsForQuotas.CreatePatientWait);
        directionsForQuotas.CreatePatient.click();
        WaitElement(directionsForQuotas.ReferralsRemoteConsultation);
        WaitElement(directionsForQuotas.PatientDataWait);
        directionsForQuotas.AddSnils.sendKeys(authorizationObject.GenerationSnils);
        inputWord(directionsForQuotas.LastName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        inputWord(directionsForQuotas.Name, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        inputWord(directionsForQuotas.MiddleName, getRandomWord(6, "абвгдеёжзийклмнопрстуфхцщшьъыэюя"));
        System.out.println("Заполняем данные о пациенте для консультации");
        SelectClickMethod(directionsForQuotas.DateWait, directionsForQuotas.SelectDate);
        ClickElement(directionsForQuotas.ManWait);
        SelectClickMethod(directionsForQuotas.TypeDocument, directionsForQuotas.SelectTypeDocumentSR);

        System.out.println("1 Проверка серия - R-ББ");
        inputWord(directionsForQuotas.Serial, "R-ББ ");
        WaitElement(directionsForQuotas.SerialError);

        System.out.println("2 Проверка серия - V-БV");
        inputWord(directionsForQuotas.Serial, "V-БV ");
        WaitElement(directionsForQuotas.SerialError);

        System.out.println("3 Проверка серия - V-Б9");
        inputWord(directionsForQuotas.Serial, "V-Б9 ");
        WaitElement(directionsForQuotas.SerialError);

        System.out.println("4 Проверка серия - Б-БП");
        inputWord(directionsForQuotas.Serial, "Б-БП ");
        WaitElement(directionsForQuotas.SerialError);

        System.out.println("5 Проверка серия - V БП");
        inputWord(directionsForQuotas.Serial, "V БП ");
        WaitElement(directionsForQuotas.SerialError);

        System.out.println("6 Проверка серия - V-БП");
        inputWord(directionsForQuotas.Serial, "V-БП ");
        WaitNotElement(directionsForQuotas.SerialError);

        System.out.println("8 Проверка номер - 1234567");
        inputWord(directionsForQuotas.Number, "1234567 ");
        WaitElement(directionsForQuotas.NumberError);

        System.out.println("9 Проверка номер - 12345");
        inputWord(directionsForQuotas.Number, "12345 ");
        WaitElement(directionsForQuotas.NumberError);

        System.out.println("10 Проверка номер - 12345д");
        inputWord(directionsForQuotas.Number, "12345д ");
        WaitElement(directionsForQuotas.NumberError);

        System.out.println("11 Проверка номер - 12345 ");
        inputWord(directionsForQuotas.Number, "12345  ");
        WaitElement(directionsForQuotas.NumberError);

        System.out.println("12 Проверка номер - 12345");
        inputWord(directionsForQuotas.Number, " 12345 ");
        WaitElement(directionsForQuotas.NumberError);

        System.out.println("13 Проверка номер - 12-345");
        inputWord(directionsForQuotas.Number, "12-345 ");
        WaitElement(directionsForQuotas.NumberError);

        System.out.println("14 Проверка номер - 123456");
        inputWord(directionsForQuotas.Number, "123456 ");
        WaitNotElement(directionsForQuotas.NumberError);

        actionElementAndClick(directionsForQuotas.Address);
        inputWord(directionsForQuotas.Address, "г Москва, ул Арбат, д 9АФ");
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.AddressHome);
        ClickElement(directionsForQuotas.Select);
        actionElementAndClick(directionsForQuotas.NextPatient);

        System.out.println("Заполнение информации о направившем враче");
        directionsForQuotas.CreateRemoteConsul(false, "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"", "159 790 257 20", PName, "Женская консультация", "детской урологии-андрологии", "плановая", "Очное консультирование", "Паратиф A");
    }
}
