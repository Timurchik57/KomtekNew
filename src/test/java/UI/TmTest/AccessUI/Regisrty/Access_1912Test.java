package UI.TmTest.AccessUI.Regisrty;

import Base.BaseAPI;
import Base.TestListener;
import UI.TmTest.AccessUI.Administration.Access_1898Test;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Регистры")
@Tag("Типы_регистров")
@Tag("Регистр_Профилактики")
public class Access_1912Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    Access_1898Test access1898Test;
    MedOrganization medOrganization;
    RegisterDispensaryPatients registerDB;

    @Issue(value = "TEL-1912")
    @Link(name = "ТМС-1804", url = "https://team-1okm.testit.software/projects/5/tests/1804?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Скачивание регистра диспансерных больных")
    @Description("Переходим в любой регистр и скачиваем данные")
    public void Access_1912() throws InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        access1898Test = new Access_1898Test();
        medOrganization = new MedOrganization(driver);
        registerDB = new RegisterDispensaryPatients(driver);

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходим в Регистр диспансерных больных");
        ClickElement(registerDB.RegistrSelect("Профилактика"));
        ClickElement(registerDB.RegistrPrev);
        WaitElement(registerDB.FIOFirst);
        Thread.sleep(1500);
        WaitNotElement3(registerDB.DownloadDisabled, 30);
        ClickElement(registerDB.Download);
        Thread.sleep(2000);
        WaitNotElement(registerDB.DownloadError);
    }
}
