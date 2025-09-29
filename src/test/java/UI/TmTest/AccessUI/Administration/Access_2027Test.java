package UI.TmTest.AccessUI.Administration;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerChange;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListenerChange.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Уровень_мед_организаций")
@Tag("Типы_регистров")
@Tag("Роли_доступа")
@Tag("Курируемые_мо")
@Tag("Регистр_Акинео")
@Tag("Основные")
public class Access_2027Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;
    Access_1898Test access1898Test;
    MedOrganization medOrganization;
    RegisterDispensaryPatients registerDispensaryPatients;

    @Issue(value = "TEL-2027")
    @Issue(value = "TEL-3090")
    @Link(name = "ТМС-1866", url = "https://team-1okm.testit.software/projects/5/tests/1866?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Выставляем уровень 1 - убираем курируемую МО - проверяем Регистр Акинео - должно отображаться мало документов, так как отображаются только от авторизованной МО - после выставляем MoLevels = 0 и проверяем регистр Акинео - должно отображаться больше документов, от разных МО")
    @Test
    @DisplayName("Проверка отображения уровней МО при выключенной фиче")
    public void Access_2027 () throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        access1898Test = new Access_1898Test();
        medOrganization = new MedOrganization(driver);

        /*  - Доступ есть - 1 уровень - кур МО2 = МО1
            - Доступ есть - 1 уровень - кур нет = МО1
            - Доступ есть - 2 уровень - кур нет = МО1
            - Доступ есть - 2 уровень - кур МО2 = МО1 и МО2
            - Доступ есть - 3 уровень - кур МО2 = МО1 и МО2
            - Доступ есть - 3 уровень - кур нет = МО1
            - Доступ есть - 1 уровень + 2 уровень - кур МО2 = МО1 и МО2
            - Доступ есть - 1 уровень + 2 уровень - кур Нет = МО1
            - Доступ нет - все МО
         */

        System.out.println("Добавляем фичу - отображение уровней МО");
        sql.UpdateConnection("Update telmed.features set enabled = 1 where key = 'MoLevels';");

        /** Проверка по заявке 3090 */
        AddRole(PRole, "Доступ к разделу \"Нозологические регистры\" на СЭМД", true);
        AddRole(PRole, "Доступ к разделу \"Нозологические регистры\" по любой МО", false);
        AddRole(PRole, "ВИМИС \"Онкология\"", true);
        AddRole(PRole, "ВИМИС \"ССЗ\"", true);
        AddRole(PRole, "ВИМИС \"АкиНео\"", true);
        AddRole(PRole, "ВИМИС \"Профилактика\"", true);
        AddRole(PRole, "ВИМИС \"Инфекционные заболевания\"", true);
        AddRole(PRole, "ВИМИС \"Иные профили\"", true);

        System.out.println("Переходим в Типы регистров");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(typeRegistr.TypeRegistrWait);

        System.out.println("Проверяем создан ли нужный регистр - Профилактика");
        access1898Test.Access_1898Method("Регистр Профилактики", typeRegistr.SelectSourceDataPrev);

        System.out.println(
                "------------------------------------ 1 проверка - Доступ есть - 1 уровень - кур Нет = МО1 ------------------------------------");
        System.out.println("Переходим в Мед. Организации");
        SetMo();

        System.out.println("Оставляем только один уровень");
        WaitElement(medOrganization.AccessCount);
        Integer Count = driver.findElements(medOrganization.AccessCount).size();
        if (Count > 1) {
            for (int i = 1; i <= Count - 1; i++) {
                ClickElement(medOrganization.Delete("2"));
                ClickElement(medOrganization.DeleteYes);
                Thread.sleep(2000);
            }
        }
        System.out.println("Добавляем 1 уровень и убираем все курируемые МО");
        LevelUp("1 уровень", true);
        GetSql("where mo_oid in ('" + POidMoRequest + "')");

        System.out.println(
                "------------------------------------ 2 проверка - Доступ есть - 1 уровень - кур МО2 = МО1 ------------------------------------");
        System.out.println("Переходим в Мед. Организации");
        SetMo();

        System.out.println("Добавляем 1 уровень и добавляем курируемую МО");
        LevelUp("1 уровень", false);
        GetSql("where mo_oid in ('" + POidMoRequest + "')");

        System.out.println(
                "------------------------------------ 3 проверка - Доступ есть - 2 уровень - кур МО2 = МО1 и МО2 ------------------------------------");
        System.out.println("Переходим в Мед. Организации");
        SetMo();

        System.out.println("Добавляем 2 уровень и добавляем курируемую МО");
        LevelUp("2 уровень", false);
        GetSql("where mo_oid in ('" + POidMoRequest + "', '" + POidMoTarget2 + "')");

        System.out.println(
                "------------------------------------ 4 проверка - Доступ есть - 2 уровень - кур Нет = МО1 ------------------------------------");
        System.out.println("Переходим в Мед. Организации");
        SetMo();

        System.out.println("Добавляем 2 уровень и удаляем курируемую МО");
        LevelUp("2 уровень", true);
        GetSql("where mo_oid in ('" + POidMoRequest + "')");

        System.out.println(
                "------------------------------------ 5 проверка - Доступ есть - 3 уровень - кур Нет = МО1 ------------------------------------");
        System.out.println("Переходим в Мед. Организации");
        SetMo();

        System.out.println("Добавляем 3 уровень и удаляем курируемую МО");
        LevelUp("3 уровень", true);
        GetSql("where mo_oid in ('" + POidMoRequest + "')");

        System.out.println(
                "------------------------------------ 6 проверка - Доступ есть - 3 уровень - кур МО2 = МО1 и МО2 ------------------------------------");
        System.out.println("Переходим в Мед. Организации");
        SetMo();

        System.out.println("Добавляем 3 уровень и удаляем курируемую МО");
        LevelUp("3 уровень", false);
        GetSql("where mo_oid in ('" + POidMoRequest + "', '" + POidMoTarget2 + "')");

        System.out.println(
                "------------------------------------ 7 проверка - Доступ есть - 1 уровень + 2 уровень - кур МО2 = МО1 и МО2 ------------------------------------");
        System.out.println("Переходим в Мед. Организации");
        SetMo();

        System.out.println("Добавляем 1 уровень и удаляем курируемую МО");
        LevelUp("1 уровень", false);

        ClickElement(medOrganization.EditFirst);
        Thread.sleep(1500);
        ClickElement(medOrganization.AddAccess);
        ClickElement(medOrganization.DateStart);
        ClickElement(medOrganization.DateStart1);
        ClickElement(medOrganization.Level);
        ClickElement(medOrganization.Level("Второй уровень"));
        ClickElement(medOrganization.Profile);
        ClickElement(medOrganization.Profile1);
        ClickElement(medOrganization.AddAccess2);
        Thread.sleep(1500);
        ClickElement(medOrganization.UpdateWait);

        GetSql("where mo_oid in ('" + POidMoRequest + "', '" + POidMoTarget2 + "')");

        System.out.println(
                "------------------------------------ 8 проверка - Доступ есть - 1 уровень + 2 уровень - кур Нет = МО1 ------------------------------------");
        System.out.println("Переходим в Мед. Организации");
        SetMo();

        System.out.println("Добавляем 1 уровень и удаляем курируемую МО");
        LevelUp("1 уровень", true);
        GetSql("where mo_oid in ('" + POidMoRequest + "')");

        System.out.println(
                "------------------------------------ 9 проверка - Доступ Нет = Все МО ------------------------------------");
        sql.UpdateConnection("Update telmed.features set enabled = 0 where key = 'MoLevels';");
        Thread.sleep(3000);
        AuthorizationMethod(authorizationObject.OKB);
        System.out.println("Переходим в Мед. Организации");
        SetMo();

        System.out.println("Проверяем, что нет Настройки уровней");
        Integer number = 0;
        while (isElementNotVisible(medOrganization.SettingLevel) & number < 16) {
            driver.navigate().refresh();
            ClickElement(medOrganization.InputOrganizationWait);
            ClickElement(medOrganization.SelectMOOKB);
            ClickElement(medOrganization.SearchWait);
            ClickElement(medOrganization.EditFirst);
            Thread.sleep(1000);
            number++;
        }
        WaitNotElement(medOrganization.SettingLevel);
        WaitElement(medOrganization.SupervisedDisable);
        ClickElement(medOrganization.CloseTrue);
        GetSql("where mo_oid is not NULL");
    }

    public void LevelUp (String level, boolean delete) throws InterruptedException {
        String Level = "";
        if (level.equals("1 уровень")) {
            Level = "Первый уровень";
        }
        if (level.equals("2 уровень")) {
            Level = "Второй уровень";
        }
        if (level.equals("3 уровень")) {
            Level = "Третий уровень";
        }

        if (isElementVisibleTime(medOrganization.AccessCount, 1)) {
            if (!isElementVisibleTime(medOrganization.AccessCountTrue(level), 1)) {
                ClickElement(medOrganization.AccessFirstEdit);
                ClickElement(medOrganization.DateStart);
                ClickElement(medOrganization.DateStart1);
                ClickElement(medOrganization.Level);
                ClickElement(medOrganization.Level(Level));
                ClickElement(medOrganization.Profile);
                ClickElement(medOrganization.Profile1);
                ClickElement(medOrganization.UpdateAccess("2"));
            }
        }

        if (delete) {
            ClickElement(medOrganization.Supervised);
            WaitElement(medOrganization.MOAdd);
            if (isElementVisibleTime(medOrganization.MOCount, 1)) {
                Integer Count = driver.findElements(medOrganization.MOCount).size();
                if (Count > 0) {
                    for (int i = 0; i < Count; i++) {
                        ClickElement(medOrganization.MOTrueDelete("1"));
                        ClickElement(medOrganization.MOTrueDeleteYes);
                        ClickElement(medOrganization.UpdateWait);
                    }
                } else {
                    Thread.sleep(1500);
                    ClickElement(medOrganization.CloseTrue);
                }
            } else {
                Thread.sleep(1500);
                ClickElement(medOrganization.CloseTrue);
            }
        } else {
            System.out.println("Добавляем МО");
            ClickElement(medOrganization.Supervised);
            WaitElement(medOrganization.MOAdd);
            Thread.sleep(1500);
            if (!isElementVisibleTime(medOrganization.MOTrue, 1)) {
                ClickElement(medOrganization.MOAdd);
                ClickElement(authorizationObject.Select("БУ ХМАО-Югры \"Белоярская районная больница\""));
                ClickElement(medOrganization.MOAddEnter);
                Thread.sleep(1500);
                ClickElement(medOrganization.UpdateWait);
            } else {
                Thread.sleep(1500);
                ClickElement(medOrganization.CloseTrue);
            }
        }
    }

    public void SetMo () throws InterruptedException {
        ClickElement(medOrganization.OrganizationWait);
        ClickElement(medOrganization.InputOrganizationWait);
        ClickElement(medOrganization.SelectMOOKB);
        ClickElement(medOrganization.SearchWait);
        ClickElement(medOrganization.EditFirst);
        Thread.sleep(1500);
    }

    @Step("Метод сравнения отобржения количетсва записей на вебе")
    public void GetSql (String where) throws InterruptedException, SQLException {

        ClickElement(registerDispensaryPatients.RegistrSelect("Профилактика"));
        ClickElement(registerDispensaryPatients.RegistrPrev);
        WaitElement(registerDispensaryPatients.CountList);
        Thread.sleep(1500);
        String list = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);
        System.out.println("Количество в профилактике = " + list);

        sql.StartConnection("select count(*) from vimis.prevention_sms_v5_register " + where + " and mo_oid is not null;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }

        Assertions.assertEquals(list, sql.value,
                "Количество записей на вебе " + list + " не совпадает с бд " + sql.value + "");
    }
}
