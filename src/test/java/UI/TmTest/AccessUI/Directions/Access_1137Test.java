package UI.TmTest.AccessUI.Directions;

import Base.TestListener;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import Base.BaseAPI;
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
@Tag("Консультация_на_оборудование")
@Tag("Удалённая_консультация")
@Tag("Заполнение_карточки_пациента")
@Tag("РРП")
public class Access_1137Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    public String LastName;
    public String FirstName;
    public String MiddleName;
    public String SnilsOld;
    public String SnilsNew;

    @Issue(value = "TEL-1137")
    @Link(name = "ТМС-1443", url = "https://team-1okm.testit.software/projects/5/tests/1443?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Обновление данных в РРП при сочетании ФИО+Дата рождения")
    @Description("Перейти в Консультации - Создать направление - Направление на диагностику/консультацию - Создать пациента. Далее создаём ещё одного пациента но с ФИО + Дата рождения первого. В РРП должен сохраниться только второй СНИЛС")
    public void Access_1137 () throws InterruptedException, IOException {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        RRPConnect();

        System.out.println("Генерация снилса для добавления пользователя");
        authorizationObject.GenerateSnilsMethod();
        SnilsOld = authorizationObject.GenerationSnils.replaceAll(" ", "");

        System.out.println("Авторизация и переход в пользователи");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);

        /** Переход в создание консультации  */
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);

        System.out.println("Создать консультацию - добавить пациента");
        WaitElement(directionsForQuotas.Heading);
        ClickElement(directionsForQuotas.CreateWait);
        WaitElement(directionsForQuotas.TypeConsultationWait);
        ClickElement(directionsForQuotas.DistrictDiagnosticWait);
        ClickElement(directionsForQuotas.NextWait);
        ClickElement(directionsForQuotas.BigSearchWait);
        ClickElement(directionsForQuotas.CreatePatientWait);
        directionsForQuotas.CreatePatientMethod(SnilsOld);
        directionsForQuotas.DoctorMethod();

        System.out.println("Авторизуемся в РРП через АПИ");
        GetRRP("", SnilsOld);
        LastName = Response.get("patients[0].LastName");
        FirstName = Response.get("patients[0].FirstName");
        MiddleName = Response.get("patients[0].MiddleName");

        System.out.println("Создаём нового пациента заново");
        authorizationObject.GenerateSnilsMethod();

        System.out.println("Авторизация и переход в пользователи");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);
        SnilsNew = authorizationObject.GenerationSnils.replaceAll(" ", "");

        /** Переход в создание консультации  */
        WaitElement(directionsForQuotas.Unfinished);
        ClickElement(directionsForQuotas.ConsultationWait);

        System.out.println("Создать консультацию - добавить пациента");
        WaitElement(directionsForQuotas.Heading);
        ClickElement(directionsForQuotas.CreateWait);
        WaitElement(directionsForQuotas.TypeConsultationWait);
        ClickElement(directionsForQuotas.DistrictDiagnosticWait);
        ClickElement(directionsForQuotas.NextWait);
        ClickElement(directionsForQuotas.BigSearchWait);
        ClickElement(directionsForQuotas.CreatePatientWait);

        WaitElement(directionsForQuotas.SnilsWait);
        directionsForQuotas.Snils.sendKeys(SnilsNew);
        inputWord(directionsForQuotas.LastName, LastName + "у");
        inputWord(directionsForQuotas.Name, FirstName + "у");
        inputWord(directionsForQuotas.MiddleName, MiddleName + "у");

        System.out.println("Заполняем данные о пациенте для консультации");
        SelectClickMethod(directionsForQuotas.DateWait, directionsForQuotas.SelectDate);
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
        directionsForQuotas.DoctorMethod();

        // Проверить после 4148 #Доделать
        System.out.println("Ищем первого пациента в РРП через АПИ");
        GetRRP("", SnilsOld);
        Assertions.assertNotEquals(
                Response.get("ErrorText"), "По указанным параметрам пациенты не найдены.", "Старый снилс должен остаться");

        System.out.println("Ищем второго пациента в РРП через АПИ");
        GetRRP("", SnilsNew);
        Assertions.assertEquals(
                Response.get("patients[0].snils"), SnilsNew, "Новый снилс должен добавиться");
    }
}
