package UI.TmTest.AccessUI.Administration;

import Base.TestListener;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.List;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Типы_регистров")
@Tag("Роли_доступа")
@Tag("Уровень_мед_организаций")
@Tag("Регистр_Акинео")
@Tag("Основные")
public class Access_1893Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;
    RegisterDispensaryPatients registerDispensaryPatients;
    AcceessRoles acceessRoles;
    public Integer number = 1;
    Access_1898Test access1898Test;
    MedOrganization medOrganization;

    @Issue(value = "TEL-1893")
    @Link(name = "ТМС-1813", url = "https://team-1okm.testit.software/projects/5/tests/1813?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("В регистре Акинео убираем все диагнозы - переходим в регистр и проверяем, что все диагнозы всё равно отображаются")
    @Test
    @DisplayName("Отображать всех пациентов если не выбран диагноз в регистре Акинео")
    public void Access_1893() throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        acceessRoles = new AcceessRoles(driver);
        access1898Test = new Access_1898Test();
        medOrganization = new MedOrganization(driver);
        number = 1;

        System.out.println("Переходим в Типы регистров");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(typeRegistr.TypeRegistrWait);

        System.out.println("Проверяем создан ли нужный регистр - Акинео");
        access1898Test.Access_1898Method("Регистр Акинео", typeRegistr.SelectSourceDataAkineo);

        System.out.println("Редактирование роли - Добавляем все 6 доступа по СЭМД");
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" на СЭМД", true);
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" по любой МО", false);
        AddRole(PRole,"ВИМИС \"Онкология\"", true);
        AddRole(PRole,"ВИМИС \"ССЗ\"", true);
        AddRole(PRole,"ВИМИС \"АкиНео\"", true);
        AddRole(PRole,"ВИМИС \"Профилактика\"", true);
        AddRole(PRole,"ВИМИС \"Инфекционные заболевания\"", true);
        AddRole(PRole,"ВИМИС \"Иные профили\"", true);

        System.out.println("Переходим в Регистр Акинео и проверяем количество записей");
        Thread.sleep(1500);
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        ClickElement(registerDispensaryPatients.RegistrAkineo);
        WaitElement(registerDispensaryPatients.QuantityPatientsWait);
        Thread.sleep(3000);
        String list1 = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);
        System.out.println(list1);

        System.out.println("Убираем диагнозы из регистра Акинео");
        ClickElement(typeRegistr.TypeRegistrWait);
        while (isElementNotVisible(typeRegistr.EditAkineo) == false) {
            typeRegistr.Next.click();
        }
        ClickElement(typeRegistr.EditAkineo);
        WaitElement(typeRegistr.HeaderUpdate);
        if (isElementNotVisible(typeRegistr.DeleteDiagnosis) == true) {
            List<WebElement> list = driver.findElements(typeRegistr.DiagnosisCount);
            System.out.println(list.size());
            for (int i = 0; i < list.size(); i++) {
                ClickElement(typeRegistr.DeleteDiagnosis);
                ClickElement(typeRegistr.DeleteDiagnosistYes);
                Thread.sleep(1500);
            }
        }
        ClickElement(typeRegistr.UpdateWait);
        Thread.sleep(1500);

        System.out.println("Переходим в Регистр Акинео и проверяем, добавленную запись");
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        ClickElement(registerDispensaryPatients.RegistrAkineo);
        WaitElement(registerDispensaryPatients.QuantityPatientsWait);
        Thread.sleep(3000);
        String list2 = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);
        System.out.println(list2);

        Assertions.assertEquals(list1, list2, "Количество пациентов не должно меняться");
    }
}