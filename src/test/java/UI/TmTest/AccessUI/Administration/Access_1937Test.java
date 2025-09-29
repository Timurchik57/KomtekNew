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
public class Access_1937Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    MedOrganization medOrganization;
    Equipments equipments;

    @Issue(value = "TEL-1937")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Добавление доступа до оборудования в собственную МО")
    @Description("Добавление организации, для которой будет доступно оборудование для своего МО")
    public void AddEquipment() throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        medOrganization = new MedOrganization(driver);
        equipments = new Equipments(driver);

        System.out.println("Добавление оборудования");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переход в добавление МО, получающей консультацию");
        WaitElement(medOrganization.OrganizationWait);
        actionElementAndClick(medOrganization.Organization);

        System.out.println("Выбор организации для редактирования");
        WaitElement(medOrganization.HeaderOrganizationWait);
        WaitElement(medOrganization.InputOrganizationWait);
        SelectClickMethod(medOrganization.InputOrganizationWait, medOrganization.SelectMOOKB);
        medOrganization.Search.click();

        System.out.println("Редактирование организации");
        ClickElement(medOrganization.Edit);
        WaitElement(medOrganization.HeaderMOWait);
        if (KingNumber == 4) {
            Thread.sleep(5000);
        }
        ClickElement(medOrganization.RecordingWait);

        System.out.println("Добавление организации для которой будет доступно оборудование");
        WaitElement(medOrganization.GammaCameraWait);
        AddEquipmentsMethod(medOrganization.GammaCameraWait, medOrganization.OKB, medOrganization.OKBTrue);
        WaitElement(medOrganization.KT);
        AddEquipmentsMethod(medOrganization.KT, medOrganization.OKB, medOrganization.OKBTrue);
        WaitElement(medOrganization.MRT);
        AddEquipmentsMethod(medOrganization.MRT, medOrganization.OKB, medOrganization.OKBTrue);
        Thread.sleep(1000);
        WaitElement(medOrganization.UpdateWait);
        actionElementAndClick(medOrganization.Update);
        Thread.sleep(2000);
        if (isElementNotVisible(medOrganization.HeaderMOWait)) {
            ClickElement(medOrganization.Close2("3"));
        }

        System.out.println("МО получающая консультацию добавлена");
        if (KingNumber == 4 | KingNumber == 2) {
            Thread.sleep(5000);
        }
    }
}
