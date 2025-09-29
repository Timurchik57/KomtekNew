package UI.TmTest.AccessUI.Administration;

import Base.SQL;
import Base.TestListener;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import api.Access_1786Test;
import Base.BaseAPI;
import Base.TestListenerApi;
import Base.XML;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Типы_регистров")
@Tag("Роли_доступа")
@Tag("Уровень_мед_организаций")
@Tag("Проверка_БД")
@Tag("Регистр_Акинео")
public class Access_1898Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;
    RegisterDispensaryPatients registerDispensaryPatients;
    AcceessRoles acceessRoles;
    MedOrganization medOrganization;
    public Integer number = 1;
    public String licalUid;

    /* Уровень 1 - отображаются свои документы в регистрах
           Уровень 2 и 3 - отображаются документы, которые указаны в Курируемые МО
            Если MoLevels = 0, то отображаются свои документы
         */

    @Issue(value = "TEL-1898")
    @Issue(value = "TEL-2173")
    @Issue(value = "TEL-1979")
    @Link(name = "ТМС-1798", url = "https://team-1okm.testit.software/projects/5/tests/1798?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Link(name = "ТМС-1870", url = "https://team-1okm.testit.software/projects/5/tests/1870?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @Description("Добавляем новый допуск тестовому пользователю 5885 и проверяем отображение")
    @Test
    @Order(1)
    @Story("Проверка отображения регистров при определённых настройках")
    @DisplayName("Допуски в Роли доступа")
    public void Access_1898() throws InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        acceessRoles = new AcceessRoles(driver);
        number = 1;

        System.out.println("Редактирование роли - Добавляем все 6 доступа по СЭМД");
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" на СЭМД", true);
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" по любой МО", false);
        AddRole(PRole,"ВИМИС \"Онкология\"", true);
        AddRole(PRole,"ВИМИС \"ССЗ\"", true);
        AddRole(PRole,"ВИМИС \"АкиНео\"", true);
        AddRole(PRole,"ВИМИС \"Профилактика\"", true);
        AddRole(PRole,"ВИМИС \"Инфекционные заболевания\"", true);
        AddRole(PRole,"ВИМИС \"Иные профили\"", true);

        System.out.println("Переходим в Типы регистров");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(typeRegistr.TypeRegistrWait);

        System.out.println("Проверяем создан ли нужный регистр");
        Access_1898Method("Регистр Онко", typeRegistr.SelectSourceDataOnko);
        Access_1898Method("Регистр Профилактики", typeRegistr.SelectSourceDataPrev);
        Access_1898Method("Регистр Акинео", typeRegistr.SelectSourceDataAkineo);
        Access_1898Method("Регистр ССЗ", typeRegistr.SelectSSZ);
        Access_1898Method("Регистр Инфекции", typeRegistr.SelectInfection);
        Access_1898Method("Регистр Иные Профили", typeRegistr.SelectOther);

        System.out.println("Повторно Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(registerDispensaryPatients.RegistrDBWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Онкология"));
        WaitElement(registerDispensaryPatients.RegistrOnkoWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Профилактика"));
        WaitElement(registerDispensaryPatients.RegistrPrev);
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        WaitElement(registerDispensaryPatients.RegistrAkineo);
        ClickElement(registerDispensaryPatients.RegistrSelect("Сердечно-сосудистые"));
        WaitElement(registerDispensaryPatients.RegistrSSZ);
        ClickElement(registerDispensaryPatients.RegistrSelect("Инфекционные болезни"));
        WaitElement(registerDispensaryPatients.RegistrInfection);
        ClickElement(registerDispensaryPatients.RegistrSelect("Иные профили"));
        WaitElement(registerDispensaryPatients.RegistrOther);

        System.out.println("Редактирование роли - оставить только 5 доступа");
        AddRole(PRole,"ВИМИС \"Онкология\"", false);

        System.out.println("Повторно Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(registerDispensaryPatients.RegistrDBWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Онкология"));
        WaitNotElement(registerDispensaryPatients.RegistrOnkoWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Профилактика"));
        WaitElement(registerDispensaryPatients.RegistrPrev);
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        WaitElement(registerDispensaryPatients.RegistrAkineo);
        ClickElement(registerDispensaryPatients.RegistrSelect("Сердечно-сосудистые"));
        WaitElement(registerDispensaryPatients.RegistrSSZ);
        ClickElement(registerDispensaryPatients.RegistrSelect("Инфекционные болезни"));
        WaitElement(registerDispensaryPatients.RegistrInfection);
        ClickElement(registerDispensaryPatients.RegistrSelect("Иные профили"));
        WaitElement(registerDispensaryPatients.RegistrOther);

        System.out.println("Редактирование роли - оставить только 4 доступа");
        AddRole(PRole,"ВИМИС \"ССЗ\"", false);

        System.out.println("Повторно Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(registerDispensaryPatients.RegistrDBWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Онкология"));
        WaitNotElement(registerDispensaryPatients.RegistrOnkoWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Профилактика"));
        WaitElement(registerDispensaryPatients.RegistrPrev);
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        WaitElement(registerDispensaryPatients.RegistrAkineo);
        ClickElement(registerDispensaryPatients.RegistrSelect("Сердечно-сосудистые"));
        WaitNotElement(registerDispensaryPatients.RegistrSSZ);
        ClickElement(registerDispensaryPatients.RegistrSelect("Инфекционные болезни"));
        WaitElement(registerDispensaryPatients.RegistrInfection);
        ClickElement(registerDispensaryPatients.RegistrSelect("Иные профили"));
        WaitElement(registerDispensaryPatients.RegistrOther);

        System.out.println("Редактирование роли - оставить только 3 доступа");
        AddRole(PRole,"ВИМИС \"АкиНео\"", false);

        System.out.println("Повторно Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(registerDispensaryPatients.RegistrDBWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Онкология"));
        WaitNotElement(registerDispensaryPatients.RegistrOnkoWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Профилактика"));
        WaitElement(registerDispensaryPatients.RegistrPrev);
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        WaitNotElement(registerDispensaryPatients.RegistrAkineo);
        ClickElement(registerDispensaryPatients.RegistrSelect("Сердечно-сосудистые"));
        WaitNotElement(registerDispensaryPatients.RegistrSSZ);
        ClickElement(registerDispensaryPatients.RegistrSelect("Инфекционные болезни"));
        WaitElement(registerDispensaryPatients.RegistrInfection);
        ClickElement(registerDispensaryPatients.RegistrSelect("Иные профили"));
        WaitElement(registerDispensaryPatients.RegistrOther);

        System.out.println("Редактирование роли - оставить только 2 доступа");
        AddRole(PRole,"ВИМИС \"Профилактика\"", false);

        System.out.println("Повторно Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(registerDispensaryPatients.RegistrDBWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Онкология"));
        WaitNotElement(registerDispensaryPatients.RegistrOnkoWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Профилактика"));
        WaitNotElement(registerDispensaryPatients.RegistrPrev);
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        WaitNotElement(registerDispensaryPatients.RegistrAkineo);
        ClickElement(registerDispensaryPatients.RegistrSelect("Сердечно-сосудистые"));
        WaitNotElement(registerDispensaryPatients.RegistrSSZ);
        ClickElement(registerDispensaryPatients.RegistrSelect("Инфекционные болезни"));
        WaitElement(registerDispensaryPatients.RegistrInfection);
        ClickElement(registerDispensaryPatients.RegistrSelect("Иные профили"));
        WaitElement(registerDispensaryPatients.RegistrOther);

        System.out.println("Редактирование роли - оставить только 1 доступа");
        AddRole(PRole,"ВИМИС \"Инфекционные заболевания\"", false);

        System.out.println("Повторно Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(registerDispensaryPatients.RegistrDBWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Онкология"));
        WaitNotElement(registerDispensaryPatients.RegistrOnkoWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Профилактика"));
        WaitNotElement(registerDispensaryPatients.RegistrPrev);
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        WaitNotElement(registerDispensaryPatients.RegistrAkineo);
        ClickElement(registerDispensaryPatients.RegistrSelect("Сердечно-сосудистые"));
        WaitNotElement(registerDispensaryPatients.RegistrSSZ);
        ClickElement(registerDispensaryPatients.RegistrSelect("Инфекционные болезни"));
        WaitNotElement(registerDispensaryPatients.RegistrInfection);
        ClickElement(registerDispensaryPatients.RegistrSelect("Иные профили"));
        WaitElement(registerDispensaryPatients.RegistrOther);

        System.out.println("Редактирование роли - убрать все");
        AddRole(PRole,"ВИМИС \"Иные профили\"", false);

        System.out.println("Повторно Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(registerDispensaryPatients.RegistrDBWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Онкология"));
        WaitNotElement(registerDispensaryPatients.RegistrOnkoWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Профилактика"));
        WaitNotElement(registerDispensaryPatients.RegistrPrev);
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        WaitNotElement(registerDispensaryPatients.RegistrAkineo);
        ClickElement(registerDispensaryPatients.RegistrSelect("Сердечно-сосудистые"));
        WaitNotElement(registerDispensaryPatients.RegistrSSZ);
        ClickElement(registerDispensaryPatients.RegistrSelect("Инфекционные болезни"));
        WaitNotElement(registerDispensaryPatients.RegistrInfection);
        ClickElement(registerDispensaryPatients.RegistrSelect("Иные профили"));
        WaitNotElement(registerDispensaryPatients.RegistrOther);
    }

    @Test
    @Order(2)
    @Story("Проверка отображения регистров при определённых настройках")
    @DisplayName("Уровень Мед. организации")
    @Description("MoLevels = 1, добавлена курируемая МО, добавлен уровень 2 - отправляем смс - проверяем Регистр Акинео, после убираем курируемую Мо и проверяем, что добавленная СМС не отображается")
    public void Access_1898AddSMS() throws InterruptedException, IOException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        acceessRoles = new AcceessRoles(driver);
        medOrganization = new MedOrganization(driver);

        System.out.println("Редактирование роли - Добавляем все 6 доступа по СЭМД");
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" на СЭМД", true);
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" по любой МО", false);
        AddRole(PRole,"ВИМИС \"Онкология\"", true);
        AddRole(PRole,"ВИМИС \"ССЗ\"", true);
        AddRole(PRole,"ВИМИС \"АкиНео\"", true);
        AddRole(PRole,"ВИМИС \"Профилактика\"", true);
        AddRole(PRole,"ВИМИС \"Инфекционные заболевания\"", true);
        AddRole(PRole,"ВИМИС \"Иные профили\"", true);

        System.out.println("Авторизуемся и добавляем нужные доступы");
        AuthorizationMethod(authorizationObject.OKB);

        Access_1898MOMethod(medOrganization.SelectMOOKB, medOrganization.Level2, medOrganization.Profile1, medOrganization.MOAddBRB, medOrganization.MOTrue, true);

        System.out.println("Переходим в Регистр Акинео и проверяем количество записей");
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        ClickElement(registerDispensaryPatients.RegistrAkineo);
        WaitElement(registerDispensaryPatients.QuantityPatientsWait);
        Thread.sleep(1500);
        String list = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);

        System.out.println(
                "Отправляем смс и добавляем запись в таблицу vimis.akineo_sms_v5_register, чтобы проверить отображение в регистре");

        xml.ApiSmd("SMS/SMS3.xml", "3", 3, 1, true, 2, 1, 9, 18, 1, 57, 21);

        sql.StartConnection("Select * from vimis.akineosms where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        sql.UpdateConnection(
                "insert into vimis.akineo_sms_v5_register (sms_id, patient_guid, registration_datetime, diagnosis, foetus_number,  version, local_uid) values ('" + sql.value + "', '" + PatientGuid + "', '1900-07-07 17:30:00.000 +0500', 'A00', '1', '" + VN + "', '" + xml.uuid + "');");

        System.out.println("Переходим в Регистр Акинео и проверяем, добавленную запись");
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        ClickElement(registerDispensaryPatients.RegistrAkineo);
        WaitElement(registerDispensaryPatients.QuantityPatientsWait);
        Thread.sleep(1500);
        String list2 = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);

        Assertions.assertNotEquals(list, list2, "Количество пациентов не отличается");

        System.out.println("Снова переходим в Курируемые МО и убираем нужную МО, с которой отправляли смс");
        ClickElement(medOrganization.OrganizationWait);
        ClickElement(medOrganization.InputOrganizationWait);
        ClickElement(medOrganization.SelectMOOKB);
        ClickElement(medOrganization.SearchWait);
        ClickElement(medOrganization.EditFirst);

        ClickElement(medOrganization.Supervised);
        System.out.println("Удаляем нужную МО");
        ClickElement(medOrganization.MOTrueDelete);
        ClickElement(medOrganization.MOTrueDeleteYes);
        Thread.sleep(1500);
        ClickElement(medOrganization.UpdateWait);

        System.out.println("Переходим в Регистр Акинео и проверяем, что записи не отображаются");
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        ClickElement(registerDispensaryPatients.RegistrAkineo);
        WaitElement(registerDispensaryPatients.QuantityPatientsWait);
        Thread.sleep(1500);
        String list3 = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);

        Assertions.assertNotEquals(list2, list3, "Количество пациентов не отличается");

        System.out.println("Переходим в Мед. Организации и добавляем нужный уровень обратно");
        Access_1898MOMethod(medOrganization.SelectMOOKB, medOrganization.Level2, medOrganization.Profile1, medOrganization.MOAddBRB, medOrganization.MOTrue, true);
    }

    @Issue(value = "TEL-1895")
    @Link(name = "ТМС-1902", url = "https://team-1okm.testit.software/projects/5/tests/1902?isolatedSection=65a30e47-043a-4dd0-af4f-165d06d17427")
    @Order(3)
    @Test
    @Story("Проверка отображения регистров при определённых настройках")
    @DisplayName("Данные в таблице Регистр Акинео")
    public void Access_1895() throws InterruptedException, IOException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        acceessRoles = new AcceessRoles(driver);
        medOrganization = new MedOrganization(driver);
        Access_1786Test access_1786Test = new Access_1786Test();

        System.out.println("Убираем дату в таблице registration_datetime, чтобы текущая была на 350 дней больше");
        PatientGuid = "abf7d542-0783-431e-8f93-73feac8f9e74";
        SQLAkineo(PatientGuid);

        System.out.println("Переходим в Типы регистров");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(typeRegistr.TypeRegistrWait);

        System.out.println("Проверяем создан ли нужный регистр");
        Access_1898Method("Регистр Акинео", typeRegistr.SelectSourceDataAkineo);

        System.out.println("Редактирование роли - Добавляем все 6 доступа по СЭМД");
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" на СЭМД", true);
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" по любой МО", false);
        AddRole(PRole,"ВИМИС \"Онкология\"", true);
        AddRole(PRole,"ВИМИС \"ССЗ\"", true);
        AddRole(PRole,"ВИМИС \"АкиНео\"", true);
        AddRole(PRole,"ВИМИС \"Профилактика\"", true);
        AddRole(PRole,"ВИМИС \"Инфекционные заболевания\"", true);
        AddRole(PRole,"ВИМИС \"Иные профили\"", true);

        System.out.println("Авторизуемся и добавляем нужные доступы");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(registerDispensaryPatients.RegistrDBWait);
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        WaitElement(registerDispensaryPatients.RegistrAkineo);

        System.out.println("Переходим в Регистр Акинео и проверяем количество записей");
        ClickElement(registerDispensaryPatients.RegistrAkineo);
        WaitElement(registerDispensaryPatients.QuantityPatientsWait);
        Thread.sleep(3000);
        WaitElement(registerDispensaryPatients.CountList);
        String list = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);

        InputPropFile("List_1895", list);

        System.out.println("Отправляем нужную смс 5 c trigger_point = 19");
        access_1786Test.Access_1786Method("SMS/SMS5-vmcl=3.xml", "5", 3, 19, 3, 6, 4, 18, 1, 57, 21,
                "vimis.akineosms", "vimis.akineologs");
        xml.ReplacementWordInFileBack("SMS/SMS5-vmcl=3.xml");

        sql.StartConnection("Select * from vimis.akineosms order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            licalUid = sql.resultSet.getString("local_uid");
        }
        InputPropFile("Id_1895", sql.value);
        InputPropFile("localUid_1895", licalUid);
    }

    @Step("Смотрим, есть ли нужный регистр = {0}, если нет, то дабавляем")
    public void Access_1898Method(String NameRegistr, By Direction) throws InterruptedException {
        number = 1;
        typeRegistr = new TypeRegistr(driver);
        Integer count = 0;

        Thread.sleep(1500);
        while (!isElementNotVisible(By.xpath("//table//tbody//tr//span[contains(.,'" + NameRegistr + "')]")) & count < 6) {
            if (isElementNotVisible(typeRegistr.NextDisabled) == true) {
                number = 0;
                break;
            } else {
                count++;
                typeRegistr.Next.click();
            }
        }
        if (number == 0) {
            System.out.println("Добавление регистра");
            ClickElement(typeRegistr.AddRegistrWait);
            WaitElement(typeRegistr.HeaderAddRegistrWait);
            inputWord(typeRegistr.InputNameRegistr, "" + NameRegistr + "п");
            inputWord(typeRegistr.InputShortNameRegistr, "" + NameRegistr + "п");
            SelectClickMethod(typeRegistr.SourceData, Direction);
            typeRegistr.AddDiagnosis.click();
            WaitElement(typeRegistr.SelectAddDiagnosisWait);
            inputWord(typeRegistr.CodRegistr, "A000");
            Thread.sleep(1500);
            ClickElement(typeRegistr.Section1);
            ClickElement(typeRegistr.FirstCodRegistrWait);

            typeRegistr.AddDiagnosis.click();
            WaitElement(typeRegistr.SelectAddDiagnosisWait);
            inputWord(typeRegistr.CodRegistr, "O30.0 ");
            Thread.sleep(1500);
            ClickElement(typeRegistr.Section1);
            ClickElement(typeRegistr.FirstCodRegistrWait);

            typeRegistr.AddDiagnosis.click();
            WaitElement(typeRegistr.SelectAddDiagnosisWait);
            inputWord(typeRegistr.CodRegistr, "I11.9 ");
            Thread.sleep(1500);
            ClickElement(typeRegistr.Section1);
            ClickElement(typeRegistr.FirstCodRegistrWait);

            ClickElement(typeRegistr.AddCodRegistrWait);
            Thread.sleep(2000);
        }
    }

    @Step("Устанавливаем нужный доступ для роли {0}")
    public void Access_1898RoleMethod(String Role, Boolean Add) throws InterruptedException {
        acceessRoles = new AcceessRoles(driver);
        authorizationObject = new AuthorizationObject(driver);
        System.out.println("Устанавливаем доступ - " + Role);
        int number = 0;

        ClickElement(acceessRoles.RolesWait1);
        WaitElement(acceessRoles.HeaderRoles);
        while (!isElementNotVisible(acceessRoles.RoleTester) & number < 10) {
            acceessRoles.Next.click();
            number++;
        }
        actionElementAndClick(acceessRoles.EditPolny);
        WaitElement(acceessRoles.EditRole);
        WaitElement(acceessRoles.InputWordWait);
        inputWord(acceessRoles.InputWord, Role + " ");
        WaitNotElement3(authorizationObject.LoadingTrue("3"), 20);
        WaitElement(By.xpath("//section//div[@class='el-tree-node__content']//span[contains(.,'" + Role + "')]"));
        String Class = String.valueOf(driver.findElement((By.xpath(
                "//section//div[@class='el-tree-node__content']//span[contains(.,'" + Role + "')]/preceding-sibling::label"))).getAttribute("class"));
        if (Add & !Class.equals("el-checkbox is-checked")) {
            ClickElement(By.xpath("//section//div[@class='el-tree-node__content']//span[contains(.,'" + Role + "')]/preceding-sibling::label[@class='el-checkbox']"));
        }
        if (!Add & Class.equals("el-checkbox is-checked")) {
            ClickElement(By.xpath(
                    "//section//div[@class='el-tree-node__content']//span[contains(.,'" + Role + "')]/preceding-sibling::label[@class='el-checkbox is-checked']"));
        }
        ClickElement(acceessRoles.UpdateWait);
        Thread.sleep(1500);
    }

    @Step("Переходим в Мед организации и добавляем нужный уровень выбранной МО и добавляем курируемые МО")
    public void Access_1898MOMethod(By MO, By level, By Profile, By KMo, By KMoTrue, boolean organization) throws InterruptedException {
        medOrganization = new MedOrganization(driver);

        if (organization) {
            System.out.println("Переходим в Мед. Организации и добавляем нужный уровень");
            ClickElement(medOrganization.OrganizationWait);
            ClickElement(medOrganization.InputOrganizationWait);
            ClickElement(MO);
            ClickElement(medOrganization.SearchWait);
            ClickElement(medOrganization.EditFirst);
        }

        System.out.println("Пероверяем есть ли уровени, если нет - добавляем, если есть - обновляем");
        if (isElementNotVisible(medOrganization.AccessCount) == false) {
            ClickElement(medOrganization.AddAccess);
            ClickElement(medOrganization.DateStart);
            ClickElement(medOrganization.DateStart1);
            ClickElement(medOrganization.Level);
            ClickElement(level);
            ClickElement(medOrganization.Profile);
            ClickElement(Profile);
            ClickElement(medOrganization.AddAccess2);
        } else {
            ClickElement(medOrganization.AccessFirstEdit);
            ClickElement(medOrganization.DateStart);
            ClickElement(medOrganization.DateStart1);
            ClickElement(medOrganization.Level);
            ClickElement(level);
            ClickElement(medOrganization.Profile);
            ClickElement(Profile);
            ClickElement(medOrganization.UpdateAccess("2"));
        }

        System.out.println("Переходим в Курируемые МО");
        ClickElement(medOrganization.Supervised);
        System.out.println("Проверяем отображение нужной МО, если нет, то дабавляем");
        if (isElementNotVisible(medOrganization.MOCount) == false) {
            ClickElement(medOrganization.MOAdd);
            ClickElement(KMo);
            ClickElement(medOrganization.MOAddEnter);
            Thread.sleep(2500);
            ClickElement(medOrganization.UpdateWait);
        } else {
            if (isElementNotVisible(KMoTrue) == false) {
                ClickElement(medOrganization.MOAdd);
                ClickElement(KMo);
                ClickElement(medOrganization.MOAddEnter);
                Thread.sleep(2500);
                ClickElement(medOrganization.UpdateWait);
            } else {
                ClickElement(medOrganization.CloseWait);
            }
        }
        Thread.sleep(1500);
        if (isElementVisibleTime(medOrganization.Close2("2"), 3)) {
            ClickElement(medOrganization.Close2("2"));
        }
    }

    @Step("Метод смены формата даты")
    public String DateMethod(String str) {
        String year = str.substring(0, str.length() - 4);
        String month = str.substring(0, str.length() - 2).substring(4);
        String day = str.substring(6);
        String NewDateAll = "" + year + "-" + month + "-" + day + "";
        System.out.println(NewDateAll);
        return NewDateAll;
    }

    @Step("Переходим после тестов в Регистр Акинео и проверяем добавленную смс")
    public void After_1895(String List, String Id, String LocalUid) throws IOException, InterruptedException, SQLException{

        sql = new SQL();
        xml = new XML();

        authorizationObject = new AuthorizationObject(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        String foetus_number = null;
        String pregnancy_outcome_id= null;
        String pregnancy_outcome_name= null;
        String registration_datetime= null;
        String localUid= null;

        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходим в Регистр Акинео и проверяем количество записей");
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        ClickElement(registerDispensaryPatients.RegistrAkineo);
        WaitElement(registerDispensaryPatients.QuantityPatientsWait);
        Thread.sleep(3000);
        WaitElement(registerDispensaryPatients.CountList);
        String list = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);

        System.out.println("Какое было количество записей - " + props.getProperty("" + List + ""));
        System.out.println("Какое стало количество записей - " + list);

        Assertions.assertNotEquals(props.getProperty("" + List + ""), list, "Количество записей не увеличилось");

        sql.StartConnection("SELECT * FROM vimis.akineo_sms_v5_register x\n" +
                "join vimis.akineo_sms_v5_register_pregnancy_outcomes a on x.sms_id = a.akineo_sms_v5_register_id\n" +
                        "join dpc.pregnancy_outcome o on a.pregnancy_outcome_id = o.id\n" +
                "where x.sms_id = '"+props.getProperty("" + Id + "")+"' limit 1;");
        while (sql.resultSet.next()) {
            foetus_number = sql.resultSet.getString("foetus_number");
            pregnancy_outcome_id = sql.resultSet.getString("pregnancy_outcome_id");
            pregnancy_outcome_name = sql.resultSet.getString("name");
            registration_datetime = sql.resultSet.getString("registration_datetime");
            localUid = sql.resultSet.getString("local_uid");

        }

        String foetus_numberXml = getXml("SMS/SMS5-vmcl=3.xml", "//code[@code='6078']/following-sibling::value/@value");
        String pregnancy_outcome_idXml = getXml("SMS/SMS5-vmcl=3.xml", "(//entryRelationship//code[@codeSystemName='Исходы беременности']/@code)[1]");
        String pregnancy_outcome_nameXml = getXml("SMS/SMS5-vmcl=3.xml", "(//entryRelationship//code[@codeSystemName='Исходы беременности']/@displayName)[1]");
        String registration_datetimeXml = getXml("SMS/SMS5-vmcl=3.xml", "//observation/code[@codeSystemName='Сроки постановки на учет по поводу беременности']/following-sibling::effectiveTime/@value");

        Assertions.assertEquals(foetus_number, foetus_numberXml, "foetus_number не совпадает");
        Assertions.assertEquals(pregnancy_outcome_id, pregnancy_outcome_idXml, "pregnancy_outcome_id не совпадает");
        Assertions.assertEquals(pregnancy_outcome_name, pregnancy_outcome_nameXml, "pregnancy_outcome_name не совпадает");
        Assertions.assertEquals(registration_datetime, ""+DateMethod(registration_datetimeXml)+" 05:00:00+05", "registration_datetime не совпадает");
        Assertions.assertEquals(localUid, ""+props.getProperty("" + LocalUid + "")+"", "localUid не совпадает");
    }
}
