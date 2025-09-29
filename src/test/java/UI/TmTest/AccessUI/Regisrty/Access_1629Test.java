package UI.TmTest.AccessUI.Regisrty;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.AccessUI.Administration.Access_1898Test;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import UI.TmTest.PageObject.VIMIS.SMS;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Регистры")
@Tag("Лк_врача")
public class Access_1629Test extends BaseAPI {
    AuthorizationObject authorizationObject;
    RegisterDispensaryPatients registerDB;
    TypeRegistr typeRegistr;
    AnalyticsMO analyticsMO;
    SMS sms;
    Access_1898Test access1898Test;
    MedOrganization medOrganization;

    @Issue(value = "TEL-1629")
    @Link(name = "ТМС-1716", url = "https://team-1okm.testit.software/projects/5/tests/1716?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Order(1)
    @Story("Проверка Лк Врача из разных частей удкон")
    @DisplayName("Перенести новый личный кабинет на ссылку старого ЛК врача")
    @Description("Переходим в ЛК Врача из регистров, Структурированных медицинских сведений")
    public void Access_1629_1() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        registerDB = new RegisterDispensaryPatients(driver);
        typeRegistr = new TypeRegistr(driver);
        analyticsMO = new AnalyticsMO(driver);
        sms = new SMS(driver);
        access1898Test = new Access_1898Test();
        medOrganization = new MedOrganization(driver);

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переход Регистр Диспансерных больных");
        ClickElement(registerDB.RegistrSelect("Профилактика"));
        ClickElement(registerDB.RegistrPrev);
        ClickElement(registerDB.FirstPatientLK);

        Thread.sleep(2000);
        WaitElement(analyticsMO.BlockFIO);
    }

    @Test
    @Order(2)
    @Story("Проверка Лк Врача из разных частей удкон")
    @DisplayName("Перенести новый личный кабинет на ссылку старого ЛК врача")
    @Description("Переходим в ЛК Врача из Структурированных медицинских сведений")
    public void Access_1629_2() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        sms = new SMS(driver);

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переход Вимис - СМС");
        Access_1629Method(sms.OncologyWait);
        Access_1629Method(sms.PreventionWait);
        Access_1629Method(sms.AkineoWait);
        Access_1629Method(sms.SSZWait);
        Access_1629Method(sms.OtherWait);
    }

    @Step("Выбираем направление")
    public void Access_1629Method(By District) throws InterruptedException {
        sms = new SMS(driver);

        ClickElement(sms.SMSWait);
        ClickElement(District);
        Thread.sleep(1000);

        System.out.println("Выбор Фильтров");
        sms.Filter.click();
        WaitElement(sms.FilterWait);

        System.out.println("Ввод PatientGuid");
        ClickElement(sms.Ident);
        WaitElement(sms.PatientGuid);
        if (KingNumber == 1) {
            inputWord(driver.findElement(sms.PatientGuid), "4743e15e-488a-44c6-af50-dff0778dd01a ");
        }
        if (KingNumber == 2) {
            inputWord(driver.findElement(sms.PatientGuid), "3791bfa4-c234-43d4-a83c-8d495bca5a55 ");
        }
        if (KingNumber == 4) {
            inputWord(driver.findElement(sms.PatientGuid), "3bd01e31-7a8e-41ef-901e-317a8ec1eff5 ");
        }
        System.out.println("Поиск");
        sms.Search.click();
        Thread.sleep(1500);
        WaitNotElement3(sms.Loading, 20);
        if (isElementNotVisible(sms.NotResultSearch)) {
            System.out.println("Нет данных");
        } else {
            ClickElement(sms.FirstLine);
            String link = driver.findElement(sms.FirstLineLKLink).getAttribute("href");
            driver.get(link);
            Thread.sleep(2000);
            WaitElement(analyticsMO.Snils);
        }
    }
}
