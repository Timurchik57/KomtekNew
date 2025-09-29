package UI.TmTest.AccessUI.Directions;

import Base.TestListener;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.RRP;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testng.asserts.SoftAssert;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Удалённая_консультация")
@Tag("Заполнение_карточки_пациента")
@Tag("РРП")
@Tag("Создание_пациента")
public class Access_1069Test extends BaseAPI {
    MedOrganization medOrganization;
    AuthorizationObject authorizationObject;
    Users users;
    DirectionsForQuotas directionsForQuotas;
    EquipmentSchedule equipmentSchedule;
    RRP rrp;
    private String snils;
    private String ENP;

    @Issue(value = "TEL-1069")
    @Link(name = "ТМС-1386", url = "https://team-1okm.testit.software/projects/5/tests/1386?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отображение данных по пациенту при создании консультации только из РРП")
    @Description("Проверяем пациента из Удкон в РРП и наоборот, также проверяем пациента в API РРП. Создаём пациента в Удкон проверяем, проверяем добавление его в РРП")
    public void Access_1069() throws Exception {
        equipmentSchedule = new EquipmentSchedule(driver);
        medOrganization = new MedOrganization(driver);
        authorizationObject = new AuthorizationObject(driver);
        users = new Users(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        rrp = new RRP(driver);
        RRPConnect();

        System.out.println("Авторизация и переход в пользователи");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);

        /** Переход в создание консультации  */
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);

        System.out.println("Создать консультацию - добавить пациента");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.CreateWait);
        directionsForQuotas.Create.click();
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.DistrictDiagnostic.click();
        directionsForQuotas.Next.click();
        ClickElement(directionsForQuotas.BigSearchWait);

        System.out.println("Ищем пациента");
        WaitElement(directionsForQuotas.LastNameWait);
        driver.findElement(directionsForQuotas.InputPatient("СНИЛС")).sendKeys(Snils);
        ClickElement(directionsForQuotas.SearchWait("2"));
        WaitElement(directionsForQuotas.listPatientFirstnils);

        snils = driver.findElement(directionsForQuotas.SnilsPatient("1", "5")).getText();
        ENP = driver.findElement(directionsForQuotas.SnilsPatient("1", "6")).getText();

        if (TextUtils.isEmpty(ENP)) {
            ENP = null;
        }

        System.out.println("Переходим в РРП и вводим снилс");
        driver.get(UrlRRPWeb);
        WaitElement(rrp.LoginWait);
        rrp.Login.sendKeys(LoginRRPWeb);
        rrp.Password.sendKeys(PasswordRRPWeb);
        rrp.Enter.click();
        WaitElement(rrp.SnilsWait);
        rrp.Snils.sendKeys(snils);
        rrp.Search.click();

        System.out.println("Берём значение Снилс и ЕНП");
        WaitElement(rrp.SearchSnilsWait);
        String SearchSnils = rrp.SearchSnils.getText();
        String SearchENP = rrp.SearchEnp.getText();
        // # Доделать 4124
        Assertions.assertEquals(formatSnils(snils), SearchSnils, "Снилс в Удконе и в РРП должен совпадать");
        Assertions.assertEquals(ENP, SearchENP, "ЕНП в Удконе и в РРП должен совпадать");

        System.out.println("Авторизуемся в РРП через АПИ");
        GetRRP("", snils);
        Assertions.assertEquals((Integer) Response.get("size"), 1);
        Assertions.assertEquals(Response.get("patients[0].snils"), snils);
        Assertions.assertEquals(Response.get("patients[0].LastName"), PLastNameGlobal);
        Assertions.assertEquals(Response.get("patients[0].Policy.ENP"), ENP);

        System.out.println("Генерируем новый снилс");
        authorizationObject.GenerateSnilsMethod();

        System.out.println("Авторизация и переход в пользователи");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);

        /** Переход в создание консультации  */
        WaitElement(directionsForQuotas.Unfinished);
        actionElementAndClick(directionsForQuotas.Consultation);

        System.out.println("Создать консультацию - добавить пациента");
        WaitElement(directionsForQuotas.Heading);
        WaitElement(directionsForQuotas.CreateWait);
        directionsForQuotas.Create.click();
        WaitElement(directionsForQuotas.TypeConsultationWait);
        directionsForQuotas.DistrictDiagnostic.click();
        directionsForQuotas.Next.click();
        ClickElement(directionsForQuotas.BigSearchWait);
        ClickElement(directionsForQuotas.CreatePatientWait);
        directionsForQuotas.CreatePatientMethod(authorizationObject.GenerationSnils);

        System.out.println("Заполнение информации о направившем враче");
        directionsForQuotas.DoctorMethod();

        System.out.println("Переходим в РРП и вводим снилс");
        driver.get(UrlRRPWeb);
        WaitElement(rrp.SnilsWait);
        rrp.Snils.sendKeys(authorizationObject.GenerationSnils);
        rrp.Search.click();

        System.out.println("Берём значение Снилс");
        WaitElement(rrp.SearchSnilsWait);

        System.out.println("Убираем пробелы из Снилс");
        String charsToRemove = " !";
        for (char c : charsToRemove.toCharArray()) {
            authorizationObject.GenerationSnils = authorizationObject.GenerationSnils.replace(String.valueOf(c),
                    "");
        }
        System.out.println("Ищем пациента в РРП через АПИ");
        GetRRP("", authorizationObject.GenerationSnils);
        Assertions.assertEquals((Integer)Response.get("size"), 1);
        Assertions.assertEquals(Response.get("patients[0].snils"), authorizationObject.GenerationSnils);
    }
}
