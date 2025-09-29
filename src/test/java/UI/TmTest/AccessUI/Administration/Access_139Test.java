package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.NSI.Equipments;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Мед_организации")
public class Access_139Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    MedOrganization medOrganization;
    Equipments equipments;

    @Test
    @Issue(value = "TEL-139")
    @Link(name = "ТМС-1527", url = "https://team-1okm.testit.software/projects/5/tests/1527?isolatedSection=aee82730-5a5f-42aa-a904-10b3057df4c4")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Написание пробела первым и последним символом в поле \"Пароль COVID\"")
    @Description("Переходим в администрирование - Мед. организации - выбираем любую организацию - редактировать, вводим в поле Пароль COVID пробел первым/последним символом, проверяем ошибку")
    public void Access_139() throws  InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        medOrganization = new MedOrganization(driver);
        equipments = new Equipments(driver);

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переход в Мед организации");
        ClickElement(medOrganization.OrganizationWait);
        ClickElement(medOrganization.EditFirst);

        System.out.println("Вводим пробел первым символом");
        inputWord(driver.findElement(medOrganization.Covid), "  ");
        WaitElement(medOrganization.Error);
        Thread.sleep(1500);

        System.out.println("Вводим пробел последним символом");
        inputWord(driver.findElement(medOrganization.Covid), "qwer  ");
        WaitElement(medOrganization.Error);
    }
}
