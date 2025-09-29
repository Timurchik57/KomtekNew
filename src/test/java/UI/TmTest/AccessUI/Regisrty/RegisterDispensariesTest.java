package UI.TmTest.AccessUI.Regisrty;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerChange;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerChange.class)
@Epic("Тесты UI")
@Feature("Регистры")
@Tag("Типы_регистров")
@Tag("Регистр_Профилактики")
@Tag("Основные")
public class RegisterDispensariesTest extends BaseAPI {
    AuthorizationObject authorizationObject;
    RegisterDispensaryPatients registerDB;
    TypeRegistr typeRegistr;

    @Issue(value = "TEL-614")
    @Link(name = "ТМС-1183", url = "https://team-1okm.testit.software/projects/5/tests/1183?isolatedSection=3f797ff4-168c-4eff-b708-5d08ab80a28e")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование регистра диспансерных больных")
    @Description("Формирование регистра диспансерных больных, выбор любого пациента и его диагноза. Переходим в Типы регистров и убираем данный диагноз. Проверяем, что при формировании регистра диспансерных больных, диагноз не отображается")
    public void RegisterDispensaries() throws InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        registerDB = new RegisterDispensaryPatients(driver);
        typeRegistr = new TypeRegistr(driver);

        System.out.println("Редактирование роли - Добавляем все 6 доступа по СЭМД");
        AddRole(PRole, "Доступ к разделу \"Нозологические регистры\" на СЭМД", true);
        AddRole(PRole, "Доступ к разделу \"Нозологические регистры\" по любой МО", true);
        AddRole(PRole, "ВИМИС \"Онкология\"", true);
        AddRole(PRole, "ВИМИС \"ССЗ\"", true);
        AddRole(PRole, "ВИМИС \"АкиНео\"", true);
        AddRole(PRole, "ВИМИС \"Профилактика\"", true);
        AddRole(PRole, "ВИМИС \"Инфекционные заболевания\"", true);
        AddRole(PRole, "ВИМИС \"Иные профили\"", true);

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переход Регистр Диспансерных больных - Профилактика");
        ClickElement(registerDB.RegistrSelect("Профилактика"));
        ClickElement(registerDB.RegistrPrev);

        System.out.println("Выбор диагноза из списка");
        Thread.sleep(1500);
        WaitNotElement3(registerDB.Loading, 30);
        WaitElement(registerDB.RegistrDBDiagnosisSSZ);
        List<String> GetDiagnosis = new ArrayList<String>();
        List<WebElement> Diagnosis = driver.findElements(registerDB.RegistrDBDiagnosisSSZ);
        for (int i = 0; i < Diagnosis.size(); i++) {
            GetDiagnosis.add(Diagnosis.get(i).getText());
        }
        System.out.println("Выбираем диагноз " + GetDiagnosis.get(0));
        typeRegistr.NameDiagnosis = GetDiagnosis.get(0);
        System.out.println(typeRegistr.NameDiagnosis);

        System.out.println("Переход в Типы регистров");
        ClickElement(typeRegistr.TypeRegistrWait);

        Thread.sleep(1500);
        while (!isElementNotVisible(By.xpath("//table//tbody//tr//span[contains(.,'Регистр Профилактики')]"))) {
            if (isElementNotVisible(typeRegistr.NextDisabled) == true) {
                break;
            } else {
                typeRegistr.Next.click();
            }
        }
        ClickElement(typeRegistr.SearchEdit("Регистр Профилактики"));

        System.out.println("Ищем нужное название диагноза и удаляем");
        Thread.sleep(2500);
        WaitElement(typeRegistr.ListDiagnosis);
        List<WebElement> ListRegistr = driver.findElements(typeRegistr.ListDiagnosis);
        for (int i = 0; i < ListRegistr.size(); i++) {
            if ((typeRegistr.NameDiagnosis.contains(ListRegistr.get(i).getText()))) {
                typeRegistr.TrueNumber = i + 1;
                typeRegistr.SelectedDiagnosis = ListRegistr.get(i).getText();
            }
        }

        System.out.println(typeRegistr.TrueNumber);
        System.out.println(typeRegistr.SelectedDiagnosis);
        System.out.println("Удаляем нужный диагноз");
        ClickElement(By.xpath(
                "(//div[@class='margin-bottom-3']["+typeRegistr.TrueNumber+"]//span)[3]"));
        ClickElement(typeRegistr.AlertDeleteDiagnosisWait);
        ClickElement(typeRegistr.UpdateWait);
        Thread.sleep(1500);

        System.out.println("Переход Регистр Диспансерных больных - Профилактика");
        ClickElement(registerDB.RegistrSelect("Профилактика"));
        ClickElement(registerDB.RegistrPrev);

        System.out.println("Проверяем, что нет удалённого диагноза");
        Thread.sleep(1500);
        List<WebElement> DiagnosisUpdate = driver.findElements(registerDB.RegistrDBDiagnosisSSZ);
        for (int i = 0; i < DiagnosisUpdate.size(); i++) {
            Assertions.assertNotEquals(DiagnosisUpdate.get(i).getText(), typeRegistr.NameDiagnosis);
        }

        System.out.println("Переход в Типы регистров для обратного добавления удалённого диагноза");
        ClickElement(typeRegistr.TypeRegistrWait);

        Thread.sleep(1500);
        while (!isElementNotVisible(By.xpath("//table//tbody//tr//span[contains(.,'Регистр Профилактики')]"))) {
            if (isElementNotVisible(typeRegistr.NextDisabled) == true) {
                break;
            } else {
                typeRegistr.Next.click();
            }
        }
        ClickElement(typeRegistr.SearchEdit("Регистр Профилактики"));

        System.out.println("Добавляем диагноз ");
        ClickElement(typeRegistr.AddDiagnosisWait);
        WaitElement(typeRegistr.SelectAddDiagnosisWait);
        inputWord(typeRegistr.CodRegistr, typeRegistr.SelectedDiagnosis + "1");
        Thread.sleep(2500);
        typeRegistr.FirstCodRegistr.click();
        WaitNotElement(typeRegistr.SelectAddDiagnosisWait);
        ClickElement(typeRegistr.UpdateWait);
        Thread.sleep(1500);
    }
}
