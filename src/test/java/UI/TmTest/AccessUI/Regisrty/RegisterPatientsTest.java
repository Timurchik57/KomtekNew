package UI.TmTest.AccessUI.Regisrty;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Регистры")
@Tag("Типы_регистров")
@Disabled
public class RegisterPatientsTest extends BaseAPI {

    AuthorizationObject authorizationObject;
    RegisterDispensaryPatients registerDB;

    @Issue(value = "TEL-522")
    @Link(name = "ТМС-1156", url = "https://team-1okm.testit.software/projects/5/tests/1156?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @Description("Переход в Регистр диспансерных больных. Ввод диагнозов C00 —C96 и D00 —D09.0. Проверка отображения списка.")
    @DisplayName("Проверка Регистра диспансерных больных")
    @Test
    public void RegisterPatient() throws InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        registerDB = new RegisterDispensaryPatients(driver);

        if (KingNumber != 4) {

            AuthorizationMethod(authorizationObject.OKB);

            System.out.println("Переход в Регистр диспансерных больных");
            WaitElement(registerDB.RegistrDBWait);
            actionElementAndClick(registerDB.RegistrDB);
            WaitElement(registerDB.Header);
            WaitElement(registerDB.FiltersWait);

            System.out.println("Открытие фильтра и ввод значений");
            registerDB.Filters.click();
            while (!isElementNotVisible(registerDB.WindowFilters)) {
                registerDB.Filters.click();
                Thread.sleep(1500);
            }
            WaitElement(registerDB.AdditionalFiltersWait);
            registerDB.AdditionalFilters.click();
            WaitElement(registerDB.CodDiagnosisWait);
            registerDB.CodDiagnosis.click();
            WaitElement(registerDB.WindowCode);
            inputWord(registerDB.InputCod, "C000");
            WaitElement(registerDB.CodC00wait);
            registerDB.CodC00.click();
            inputWord(registerDB.InputCod, "C966");
            WaitElement(registerDB.CodC96wait);
            registerDB.CodC96.click();
            Thread.sleep(500);

            System.out.println("Поиск пациентов по параметру C00-C96");
            actionElementAndClick(registerDB.Search);

            System.out.println("Проверка, что нашлись пациенты по параметру C00-C96");
            WaitElement(registerDB.QuantityPatientsWait);
            String patient = registerDB.QuantityPatients.getText();
            Assertions.assertNotEquals("Пациентов: 0", patient);
            System.out.println(patient);
            Thread.sleep(1000);

            System.out.println("Открытие фильтра и ввод значений");
            registerDB.Filters.click();
            while (!isElementNotVisible(registerDB.WindowFilters)) {
                registerDB.Filters.click();
                Thread.sleep(1500);
            }
            WaitElement(registerDB.AdditionalFiltersWait);
            registerDB.AdditionalFilters.click();
            WaitElement(registerDB.CodDiagnosisWait);
            registerDB.CodDiagnosis.click();
            WaitElement(registerDB.WindowCode);
            registerDB.Reset.click();
            inputWord(registerDB.InputCod, "D000");
            WaitElement(registerDB.CodD00wait);
            registerDB.CodD00.click();
            inputWord(registerDB.InputCod, "D09.00");
            WaitElement(registerDB.CodD09wait);
            registerDB.CodD09.click();
            Thread.sleep(500);

            System.out.println("Поиск пациентов по параметру D00 —D09.0");
            actionElementAndClick(registerDB.Search);
            Thread.sleep(2000);
            authorizationObject.LoadingTime(10);

            System.out.println("Проверка, что нашлись пациенты по параметру D00 —D09.0");
            WaitElement(registerDB.QuantityPatientsWait);
            String patient1 = registerDB.QuantityPatients.getText();
            Assertions.assertNotEquals("Пациентов: 0", patient1);
            System.out.println(patient1);
        }
    }
}
