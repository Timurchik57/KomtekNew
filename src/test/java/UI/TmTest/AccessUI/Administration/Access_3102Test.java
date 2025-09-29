package UI.TmTest.AccessUI.Administration;

import Base.TestListener;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import api.Access_3100Test;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Типы_регистров")
@Tag("Роли_доступа")
@Tag("Уровень_мед_организаций")
@Tag("Регистр_Инфекции")
public class Access_3102Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;
    RegisterDispensaryPatients registerDispensaryPatients;
    public Integer number;
    Access_1898Test access1898Test;
    MedOrganization medOrganization;

    @Issue(value = "TEL-3102")
    @Owner(value = "Галиакберов Тимур")
    @Description("Создаём новый регистр с Инфекцией - переходим в данный регистр и проверяем добавленную запись из таблицы vimis.infection_sms_v5_register")
    @Test
    @DisplayName("Отображение данных из регистра Инфекции")
    public void Access_3102() throws SQLException, IOException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        Access_3100Test access_3100Test = new Access_3100Test();
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        number = 1;
        access1898Test = new Access_1898Test();
        medOrganization = new MedOrganization(driver);

        System.out.println("Редактирование роли - Добавляем все 6 доступа по СЭМД");
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" на СЭМД", true);
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" по любой МО", true);
        AddRole(PRole,"ВИМИС \"Онкология\"", true);
        AddRole(PRole,"ВИМИС \"ССЗ\"", true);
        AddRole(PRole,"ВИМИС \"АкиНео\"", true);
        AddRole(PRole,"ВИМИС \"Профилактика\"", true);
        AddRole(PRole,"ВИМИС \"Инфекционные заболевания\"", true);
        AddRole(PRole,"ВИМИС \"Иные профили\"", true);

        System.out.println("Переходим в Роли доступа");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Проверяем создан ли нужный регистр");
        ClickElement(typeRegistr.TypeRegistrWait);
        while (!isElementNotVisible(By.xpath("//table//tbody//tr//span[contains(.,'Регистр Инфекции')]"))) {
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
            inputWord(typeRegistr.InputNameRegistr, "Регистр Инфекциии");
            inputWord(typeRegistr.InputShortNameRegistr, "Регистр Инфекциии");
            SelectClickMethod(typeRegistr.SourceData, typeRegistr.SelectSourceDataPrev);
            typeRegistr.AddDiagnosis.click();
            WaitElement(typeRegistr.SelectAddDiagnosisWait);
            inputWord(typeRegistr.CodRegistr, "A000");
            ClickElement(typeRegistr.FirstCodRegistrWait);
            ClickElement(typeRegistr.AddCodRegistrWait);

            typeRegistr.AddDiagnosis.click();
            WaitElement(typeRegistr.SelectAddDiagnosisWait);
            inputWord(typeRegistr.CodRegistr, "J41.0 ");
            Thread.sleep(1500);
            ClickElement(typeRegistr.Section1);
            ClickElement(typeRegistr.FirstCodRegistrWait);
        }
        ClickElement(registerDispensaryPatients.RegistrSelect("Инфекционные болезни"));
        ClickElement(registerDispensaryPatients.RegistrSelect2("Регистр Инфекции"));
        WaitElement(registerDispensaryPatients.FIO);
        Thread.sleep(3000);
        String list1 = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);
        System.out.println(list1);

        InputPropFile("SizePatients_3102", list1);

        System.out.println("Отправляем нужную смс");
        access_3100Test.Access_3100Method("SMS/SMS5-vmcl=5.xml", "5", 5, 12, 3, 6, 4, 18, 1, 57, 21,
                "vimis.infectionsms", "vimis.infectionlogs");
    }

    @Step("Переходим к Регистр Инфекции и проверяем, что запись добавились")
    public void Access_3102After(String NameProp) throws IOException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        number = 1;
        medOrganization = new MedOrganization(driver);
        access1898Test = new Access_1898Test();

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходим в Регистр Профилактики");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(registerDispensaryPatients.RegistrSelect("Инфекционные болезни"));
        ClickElement(registerDispensaryPatients.RegistrSelect2("Регистр Инфекции"));
        Thread.sleep(1500);
        WaitElement(registerDispensaryPatients.FIO);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 20);
        String list2 = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);
        System.out.println("Количество после продолжительного времени - " + list2);
        System.out.println("Количество до того как отправили смс - " + ReadPropFile(NameProp));

        Assertions.assertNotEquals(Integer.valueOf(ReadPropFile(NameProp)), list2,
                "Количество пациентов не увеличилось");
    }
}
