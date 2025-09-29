package UI.TmTest.AccessUI.NSI;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.AccessUI.Administration.Access_1898Test;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.NSI.Equipments;
import UI.TmTest.PageObject.NSI.StandardsLaboratoryResearch;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("НСИ")
@Tag("Оборудование")
@Tag("Роли_доступа")
public class Access_2006Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    Access_1898Test access1898Test;
    StandardsLaboratoryResearch standardsLaboratoryResearch;
    Equipments equipments;

    @Issue(value = "TEL-2006")
    @Issue(value = "TEL-4223")
    @Link(name = "ТМС-1869", url = "https://team-1okm.testit.software/projects/5/tests/1869?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Редактирование справочник оборудования своего МО")
    @Description("Добавляем новый допуск тестовому пользователю 8.2 8.3 и проверяем, что можно редактировать оборудование только своего МО/всех МО")
    public void Access_2006() throws InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        access1898Test = new Access_1898Test();
        standardsLaboratoryResearch = new StandardsLaboratoryResearch(driver);
        equipments = new Equipments(driver);

        System.out.println("1 проверка - не отображается раздел Оборудование");
        System.out.println("Редактирование роли - Добавляем Разрешено редактировать справочник оборудования своего МО");
        AddRole(PRole,"Разрешено редактировать справочник оборудования своего МО", false);
        AddRole(PRole,"Разрешено редактировать справочник оборудования любого МО", false);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        WaitElement(standardsLaboratoryResearch.StandardsLR);
        WaitNotElement(equipments.EquipmentWaitt);

        System.out.println("2 проверка - не отображается выбор МО в Оборудование");
        System.out.println("Редактирование роли - Добавляем Разрешено редактировать справочник оборудования своего МО");
        AddRole(PRole,"Разрешено редактировать справочник оборудования своего МО", true);

        System.out.println("Повторно Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(equipments.EquipmentWaitt);

        WaitElement(equipments.NameWait);
        WaitNotElement(equipments.InputMoWait);
        WaitElement(equipments.SearchNameWaitFirst);
        String NameMO = driver.findElement(equipments.SearchNameWaitFirst).getText();
        Assertions.assertEquals(NameMO, "БУ ХМАО-Югры \"Окружная клиническая больница\"", "Отображается иная МО");

        System.out.println("3 проверка - отображается выбор МО в Оборудование");
        AddRole(PRole,"Разрешено редактировать справочник оборудования своего МО", false);
        AddRole(PRole,"Разрешено редактировать справочник оборудования любого МО", true);

        System.out.println("Повторно Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(equipments.EquipmentWaitt);

        WaitElement(equipments.NameWait);
        WaitElement(equipments.InputMoWait);

        System.out.println("4 проверка - отображается выбор МО в Оборудование");
        AddRole(PRole,"Разрешено редактировать справочник оборудования своего МО", true);

        System.out.println("Повторно Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(equipments.EquipmentWaitt);

        WaitElement(equipments.NameWait);
        WaitElement(equipments.InputMoWait);
        WaitElement(equipments.FirstLineSet);
        // Отображается всегда тольк своё оборудование (4223)
        for (int i = 1; i < 20; i++) {
            String str = driver.findElement(equipments.SelectList(String.valueOf(i), "3")).getText();
            Assertions.assertEquals(str, PMORequest, "МО в оборудование отлично от - " + PMORequest);
        }

        AddRole(PRole,"Разрешено редактировать справочник оборудования своего МО", true);
        AddRole(PRole,"Разрешено редактировать справочник оборудования любого МО", true);
    }
}
