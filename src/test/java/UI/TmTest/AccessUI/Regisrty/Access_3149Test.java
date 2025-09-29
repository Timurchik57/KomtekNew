package UI.TmTest.AccessUI.Regisrty;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.AccessUI.Administration.Access_1898Test;
import UI.TmTest.PageObject.Administration.MedOrganization;
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

import java.sql.SQLException;
import java.util.List;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Регистры")
@Tag("Типы_регистров")
@Tag("Регистр_Акинео")
public class Access_3149Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    Access_1898Test access1898Test;
    MedOrganization medOrganization;
    TypeRegistr typeRegistr;
    RegisterDispensaryPatients registerDispensaryPatients;
    String list;

    @Issue(value = "TEL-3149")
    @Owner(value = "Галиакберов Тимур")
    @Description("Переходим в Регистр Акинео и проверяем фильтр отображения рисков")
    @Test
    @DisplayName("Отображение данных из регистра Акинео")
    public void Access_3149() throws SQLException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        access1898Test = new Access_1898Test();
        medOrganization = new MedOrganization(driver);
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);

        System.out.println("Редактирование роли - Добавляем все 6 доступа по СЭМД");
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" на СЭМД", true);
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" по любой МО", true);

        System.out.println("Переходим в Роли доступа");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Проверяем создан ли нужный регистр");
        ClickElement(typeRegistr.TypeRegistrWait);
        Integer number = 1;
        while (!isElementNotVisible(By.xpath("//table//tbody//tr//span[contains(.,'Регистр Акинео')]"))) {
            if (isElementNotVisible(typeRegistr.NextDisabled) == true) {
                number = 0;
                break;
            } else {
                typeRegistr.Next.click();
            }
        }
        if (number == 0) {
            System.out.println("Добавление регистра");
            ClickElement(typeRegistr.AddRegistrWait);
            WaitElement(typeRegistr.HeaderAddRegistrWait);
            inputWord(typeRegistr.InputNameRegistr, "Регистр Акинео");
            inputWord(typeRegistr.InputShortNameRegistr, "Регистр Акинео");
            SelectClickMethod(typeRegistr.SourceData, typeRegistr.SelectSourceDataAkineo);

            /** Удаляем конкретные диагнозы, чтобы были доступны все */
            if (isElementNotVisible(typeRegistr.DeleteDiagnosis) == true) {
                List<WebElement> list = driver.findElements(typeRegistr.DiagnosisCount);
                System.out.println(list.size());
                for (int i = 0; i < list.size(); i++) {
                    ClickElement(typeRegistr.DeleteDiagnosis);
                    ClickElement(typeRegistr.DeleteDiagnosistYes);
                    Thread.sleep(1500);
                }
            }
            ClickElement(typeRegistr.AddCodRegistrWait);
            Thread.sleep(1500);
        }
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        ClickElement(registerDispensaryPatients.RegistrSelect2("Регистр Акинео"));

        WaitNotElement3(authorizationObject.LoadingTrue("2"), 60);
        WaitElement(registerDispensaryPatients.CountList);
        list = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);

        sql.StartConnection("select count(*) from vimis.akineo_sms_v5_register;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(list, sql.value, "Без фильтров, количество записей должно соответствовать числу из vimis.akineo_sms_v5_register");

        SelectStep("Низкая");
        SelectStep("Средняя");
        SelectStep("Высокая");
        SelectStep("Не определена");
    }

    @Step("Метод выбора нужной степени риска")
    public void SelectStep(String Step) throws InterruptedException, SQLException {
        ClickElement(registerDispensaryPatients.FiltersWait);
        SelectClickMethod(registerDispensaryPatients.FilterAll("Степень риска"), authorizationObject.Select(Step));
        ClickElement(registerDispensaryPatients.SearchWait);
        Thread.sleep(1500);

        WaitNotElement3(authorizationObject.LoadingTrue("2"), 20);
        WaitElement(registerDispensaryPatients.CountList);
        list = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);

        sql.StartConnection("select count(*) from vimis.akineo_sms_v5_register a\n" +
                "join dpc.pregnancyrisklevel p on a.risklevel = p.id \n" +
                "where p.\"name\" = '"+Step+"';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(list, sql.value, "С фильтром "+Step+", количество записей должно соответствовать числу из vimis.akineo_sms_v5_register");
    }
}
