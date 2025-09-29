package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
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

import java.util.List;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Удалённая_консультация")
@Tag("Заполнение_карточки_пациента")
public class Access_1738Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;

    @Issue(value = "TEL-1738")
    @Link(name = "ТМС-1769", url = "https://team-1okm.testit.software/projects/5/tests/1769?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @Description("Создаём направление - проверяем валидацию ФИО пациента")
    @Test
    @DisplayName("Валидация ФИО при создании пациента на консультацию")
    public void Access_1738() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);

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
        authorizationObject.LoadingTime(10);
        ClickElement(directionsForQuotas.BigSearchWait);

        System.out.println("Создание пациента для консультации");
        WaitElement(directionsForQuotas.CreatePatientWait);
        directionsForQuotas.CreatePatient.click();
        WaitElement(directionsForQuotas.ReferralsRemoteConsultation);

        System.out.println("Первая проверка - ввод 39 символов");
        inputWord(directionsForQuotas.LastName, "фффффффффффффффффффффффффффффффффффффффф");
        WaitNotElement3(directionsForQuotas.Error40, 2);
        inputWord(directionsForQuotas.Name, "фффффффффффффффффффффффффффффффффффффффф");
        WaitNotElement3(directionsForQuotas.Error40, 2);
        inputWord(directionsForQuotas.MiddleName, "фффффффффффффффффффффффффффффффффффффффф");
        WaitNotElement3(directionsForQuotas.Error40, 2);

        System.out.println("Вторая проверка - ввод 40 символов");
        inputWord(directionsForQuotas.LastName, "ффффффффффффффффффффффффффффффффффффффффф");
        WaitNotElement3(directionsForQuotas.Error40, 2);
        inputWord(directionsForQuotas.Name, "ффффффффффффффффффффффффффффффффффффффффф");
        WaitNotElement3(directionsForQuotas.Error40, 2);
        inputWord(directionsForQuotas.MiddleName, "ффффффффффффффффффффффффффффффффффффффффф");
        WaitNotElement3(directionsForQuotas.Error40, 2);

        System.out.println("Третья проверка - ввод 41 символ");
        inputWord(directionsForQuotas.LastName, "фффффффффффффффффффффффффффффффффффффффффф");
        inputWord(directionsForQuotas.Name, "фффффффффффффффффффффффффффффффффффффффффф");
        inputWord(directionsForQuotas.MiddleName, "фффффффффффффффффффффффффффффффффффффффффф");
        Thread.sleep(1000);
        List<WebElement> list = driver.findElements(directionsForQuotas.Error40);
        Assertions.assertEquals(3, list.size(), "Отображается не 3 ошибки по ФИО");

        System.out.println("Четвёртая проверка - ввод латинских символов");
        inputWord(directionsForQuotas.LastName, "dfss");
        inputWord(directionsForQuotas.Name, "werw");
        inputWord(directionsForQuotas.MiddleName, "dfge");

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
        SelectClickMethod(directionsForQuotas.DoctorWait, directionsForQuotas.SelectDoctorSecond);
        if (KingNumber == 1 | KingNumber == 2) {
            SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMO);
        }
        if (KingNumber == 4) {
            SelectClickMethod(directionsForQuotas.MOWait, directionsForQuotas.SelectMOKon);
            Thread.sleep(1500);
        }
        SelectClickMethod(directionsForQuotas.ProfileWait, authorizationObject.SelectFirst);
        ClickElement(directionsForQuotas.Plan);
        SelectClickMethod(directionsForQuotas.Goal, directionsForQuotas.SelectCovid);
        inputWord(driver.findElement(directionsForQuotas.TypeConsul), "конс");
        ClickElement(authorizationObject.Select("Консультация детского уролога-андролога"));
        inputWord(directionsForQuotas.Diagnos, "AA");
        Thread.sleep(1000);
        ClickElement(authorizationObject.SelectFirst);
        if (isElementNotVisible(directionsForQuotas.Text("Жалобы пациента"))) {
            inputWord(driver.findElement(directionsForQuotas.Text("Жалобы пациента")), "йцуу");
            inputWord(driver.findElement(directionsForQuotas.Text("Анамнез")), "йцуу");
            inputWord(driver.findElement(directionsForQuotas.Text("Объективное состояние")), "йцуу");
        }
        ClickElement(directionsForQuotas.NextConsul);
        ClickElement(directionsForQuotas.CreateConsul);
        WaitElement(directionsForQuotas.ErrorName);
    }
}
