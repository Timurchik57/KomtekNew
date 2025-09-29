package UI.TmTest.AccessUI.Regisrty;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegistrPatients;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Регистры")
public class SearchDoubleLastNameTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    RegistrPatients registrPatients;

    @Test
    @Issue(value = "TEL-797")
    @Link(name = "ТМС-1213", url = "https://team-1okm.testit.software/projects/5/tests/1213?isolatedSection=ccb1fcf9-9e3b-44d1-9bad-7959d251a43d")
    @DisplayName("Поиск пациентов с двойной фамилией")
    @Description("Переход в регистр пациентов, ввод в поиск двойную фамилию")
    public void SearchDoubleLastName() throws InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        registrPatients = new RegistrPatients(driver);

        System.out.println("Авторизация и переход в Регистр пациентов");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(registrPatients.RegistryWait);
        actionElementAndClick(registrPatients.Registry);

        System.out.println("Ввод пациента с двойной фамилией");
        WaitElement(registrPatients.Header);
        WaitElement(registrPatients.SearchWait);
        inputWord(registrPatients.Search, "Гасанов Илгар Али Оглыы");
        registrPatients.SearchButton.click();

        System.out.println("Проверка отображения результата поиска");
        WaitElement(registrPatients.ResultSearch);
    }
}
