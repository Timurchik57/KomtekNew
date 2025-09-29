package UI.TmTest.AccessUI.Administration;

import Base.TestListener;
import UI.TmTest.PageObject.Administration.AcceessRoles;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import api.Access_1786Test;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Типы_регистров")
@Tag("Уровень_мед_организаций")
@Tag("Роли_доступа")
@Tag("Регистр_Акинео")
public class Access_1918Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;
    RegisterDispensaryPatients registerDispensaryPatients;
    AcceessRoles acceessRoles;
    Access_1898Test access1898Test;
    MedOrganization medOrganization;
    Access_1786Test access_1786Test;
    public Integer number = 1;

    @Issue(value = "TEL-1918")
    @Link(name = "ТМС-1808", url = "https://team-1okm.testit.software/projects/5/tests/1808?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Переходим в регистр диспансерных больных и проверяем работу поска по ФИО КАС")
    @Test
    @DisplayName("Фильтр в Регистр диспансерных больных")
    public void Access_1918() throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        acceessRoles = new AcceessRoles(driver);
        access1898Test = new Access_1898Test();
        medOrganization = new MedOrganization(driver);
        number = 1;
        access_1786Test = new Access_1786Test();

        System.out.println("Добавляем фичу - отображение уровней МО");
        sql.UpdateConnection("Update telmed.features set enabled = 1 where key = 'MoLevels';");

        System.out.println("Редактирование роли - Добавляем все 6 доступа по СЭМД");
        AddRole(PRole, "Доступ к разделу \"Нозологические регистры\" на СЭМД", true);
        AddRole(PRole, "Доступ к разделу \"Нозологические регистры\" по любой МО", true);
        AddRole(PRole, "ВИМИС \"Онкология\"", true);
        AddRole(PRole, "ВИМИС \"ССЗ\"", true);
        AddRole(PRole, "ВИМИС \"АкиНео\"", true);
        AddRole(PRole, "ВИМИС \"Профилактика\"", true);
        AddRole(PRole, "ВИМИС \"Инфекционные заболевания\"", true);
        AddRole(PRole, "ВИМИС \"Иные профили\"", true);

        System.out.println("Переходим в Типы регистров");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(typeRegistr.TypeRegistrWait);

        System.out.println("Проверяем создан ли нужный регистр - Акинео");
        access1898Test.Access_1898Method("Регистр Акинео", typeRegistr.SelectSourceDataAkineo);

        System.out.println("Переходим в Регистр Акинео и берём ФИО Врач создавший извещения КАС");
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        ClickElement(registerDispensaryPatients.RegistrAkineo);
        WaitElement(registerDispensaryPatients.QuantityPatientsWait);
        ClickElement(registerDispensaryPatients.FiltersWait);
        ClickElement(registerDispensaryPatients.AdditionalFiltersWait);
        ClickElement(registerDispensaryPatients.KAS);
        ClickElement(registerDispensaryPatients.BRT);
        ClickElement(registerDispensaryPatients.SearchWait);
        Thread.sleep(1500);
        WaitElement(registerDispensaryPatients.CountList);
        Integer number = 1;
        Integer numberCount = 0;
        String Fio = "";
        while (number < 21 && numberCount < 21) {
            if (isElementVisibleTime(registerDispensaryPatients.SelectPatient("" + number + "", false), 2)) {
                ClickElement(registerDispensaryPatients.SelectPatient("" + number + "", false));
                WaitElement(registerDispensaryPatients.DoctorKas);
                Fio = driver.findElement(registerDispensaryPatients.DoctorKas).getText();

                if (!TextUtils.isEmpty(Fio)) {
                    break;
                } else {
                    ClickElement(registerDispensaryPatients.SelectPatient("" + number + "", true));
                    number++;
                }
            } else if (!isElementVisibleTime(registerDispensaryPatients.SelectPatient("" + number + "", false), 2) & !isElementVisibleTime(registerDispensaryPatients.NextDisable, 2)) {
                number = 1;
                numberCount = 1;
                ClickElement(registerDispensaryPatients.Next);
                Thread.sleep(1500);
                WaitElement(registerDispensaryPatients.SelectPatient("" + number + "", false));
            } else {
                throw new IllegalArgumentException("Нет Врача создавшего извещение Кас");
            }

            numberCount++;
            System.out.println(numberCount);
        }
        Thread.sleep(1500);
        ClickElement(registerDispensaryPatients.FiltersWait);
        ClickElement(registerDispensaryPatients.AdditionalFiltersWait);
        WaitElement(registerDispensaryPatients.DoctorAddKas);
        System.out.println(Fio);

        if (!TextUtils.isEmpty(Fio)) {
            System.out.println("Первая проверка - вводим часть ФИО в нижнем регистре Врач создавший извещения КАС ФИО");
            inputWord(driver.findElement(registerDispensaryPatients.DoctorAddKas), Fio.substring(2).toLowerCase());
            ClickElement(registerDispensaryPatients.SearchWait);
            Thread.sleep(1500);
            ClickElement(registerDispensaryPatients.SelectPatient("" + 1 + "", false));
            WaitElement(registerDispensaryPatients.DoctorKas);
            String Fio2 = driver.findElement(registerDispensaryPatients.DoctorKas).getText();
            Assertions.assertEquals(Fio, Fio2, "Поиск по части ФИО работает не корректно Врач создавший извещения КАС");

            ClickElement(registerDispensaryPatients.FiltersWait);
            ClickElement(registerDispensaryPatients.ResetWait);
            Thread.sleep(1500);

            number = 1;
            Fio = "";
            numberCount = 0;

            System.out.println("Берём ФИО Врач обработавший КАС");
            while (number < 21 && numberCount < 21) {
                if (isElementVisibleTime(registerDispensaryPatients.SelectPatient("" + number + "", false), 2)) {
                    ClickElement(registerDispensaryPatients.SelectPatient("" + number + "", false));
                    WaitElement(registerDispensaryPatients.DoctorKasObr);
                    Fio = driver.findElement(registerDispensaryPatients.DoctorKasObr).getText();

                    if (!TextUtils.isEmpty(Fio)) {
                        break;
                    } else {
                        ClickElement(registerDispensaryPatients.SelectPatient("" + number + "", true));
                        number++;
                    }
                } else if (!isElementVisibleTime(registerDispensaryPatients.SelectPatient("" + number + "", false), 2) & !isElementVisibleTime(registerDispensaryPatients.NextDisable, 2)) {
                    number = 1;
                    numberCount = 1;
                    ClickElement(registerDispensaryPatients.Next);
                    Thread.sleep(1500);
                    WaitElement(registerDispensaryPatients.SelectPatient("" + number + "", false));
                }  else {
                    throw new IllegalArgumentException("Нет Врача обработавшего извещение Кас");
                }
            }

            Thread.sleep(1500);
            ClickElement(registerDispensaryPatients.FiltersWait);
            ClickElement(registerDispensaryPatients.AdditionalFiltersWait);
            WaitElement(registerDispensaryPatients.DoctorUpdateAddKas);

            System.out.println("Вторая проверка - вводим часть ФИО в нижнем регистре Врач обработавший КАС");
            inputWord(driver.findElement(registerDispensaryPatients.DoctorUpdateAddKas),
                    Fio.substring(2).toLowerCase());
            ClickElement(registerDispensaryPatients.SearchWait);
            Thread.sleep(1500);
            ClickElement(registerDispensaryPatients.SelectPatient("" + 1 + "", false));
            WaitElement(registerDispensaryPatients.DoctorKasObr);
            Fio2 = driver.findElement(registerDispensaryPatients.DoctorKasObr).getText();
            Assertions.assertEquals(Fio, Fio2, "Поиск по части ФИО работает не корректно Врач обработавший КАС");
        }
    }
}