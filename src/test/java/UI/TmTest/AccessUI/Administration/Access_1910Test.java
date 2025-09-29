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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Типы_регистров")
@Tag("Уровень_мед_организаций")
@Tag("Роли_доступа")
@Tag("Проверка_БД")
@Tag("Регистр_Акинео")
@Tag("Основные")
public class Access_1910Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;
    RegisterDispensaryPatients registerDispensaryPatients;
    AcceessRoles acceessRoles;
    Access_1898Test access1898Test;
    MedOrganization medOrganization;
    Access_1786Test access_1786Test;
    public Integer number = 1;
    public String licalUid;

    @Test
    @Issue(value = "TEL-1910")
    @Link(name = "ТМС-1807", url = "https://team-1okm.testit.software/projects/5/tests/1807?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Для регистра акинео отправляем смс5 с triggerpoint 19 и добавляем смс 22/29 с тем же пациентом, чтобы далее проверить фильтры с КАС и ВРТ")
    @DisplayName("Фильтр по КАС и ВРТ в Регистре диспансерных больных")
    public void Access_1910() throws SQLException, IOException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        acceessRoles = new AcceessRoles(driver);
        access1898Test = new Access_1898Test();
        medOrganization = new MedOrganization(driver);
        number = 1;
        access_1786Test = new Access_1786Test();

        System.out.println("Убираем дату в таблице registration_datetime, чтобы текущая была на 350 дней больше");
        PatientGuid = "4743e15e-488a-44c6-af50-dff0778dd01a";
        SQLAkineo(PatientGuid);

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
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        ClickElement(registerDispensaryPatients.RegistrAkineo);
        WaitElement(registerDispensaryPatients.QuantityPatientsWait);
        ClickElement(registerDispensaryPatients.FiltersWait);
        ClickElement(registerDispensaryPatients.AdditionalFiltersWait);
        ClickElement(registerDispensaryPatients.KAS);
        ClickElement(registerDispensaryPatients.BRT);
        ClickElement(registerDispensaryPatients.SearchWait);
        Thread.sleep(1500);
        CheckSql();

        System.out.println("Отправляем нужную смс 5 c trigger_point = 19");
        access_1786Test.Access_1786Method("SMS/SMS5-vmcl=3.xml", "5", 3, 19, 3, 6, 4, 18, 1, 57, 21,
                "vimis.akineosms", "vimis.akineologs");
        xml.ReplacementWordInFileBack("SMS/SMS5-vmcl=3.xml");

        sql.StartConnection("Select * from vimis.akineosms order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            licalUid = sql.resultSet.getString("local_uid");
        }
        sql.UpdateConnection(
                "insert into vimis.akineo_sms_v5_register (sms_id, patient_guid, registration_datetime, diagnosis, foetus_number,  version, local_uid) values ('" + sql.value + "', '" + PatientGuid + "', '1900-07-07 17:30:00.000 +0500', 'A00', '1', '" + VN + "', '" + licalUid + "');");

        System.out.println("Отправляем нужную смс 22/29 c тем же пациентом");
        xml.ApiSmd("SMS/SMS22-vmcl=3.xml", "24", 3, 2, true, 3, 6, 4, 18, 1, 57, 21);
        xml.ReplacementWordInFileBack("SMS/SMS22-vmcl=3.xml");

        xml.ApiSmd("SMS/SMS29-vmcl=3.xml", "111", 3, 2, true, 3, 6, 4, 18, 1, 57, 21);

        System.out.println("Переходим в Регистр Акинео и проверяем количество записей");
        ClickElement(registerDispensaryPatients.RegistrSelect("Акушерство и неонатология"));
        ClickElement(registerDispensaryPatients.RegistrAkineo);
        WaitElement(registerDispensaryPatients.QuantityPatientsWait);
        ClickElement(registerDispensaryPatients.FiltersWait);
        ClickElement(registerDispensaryPatients.AdditionalFiltersWait);
        ClickElement(registerDispensaryPatients.KAS);
        ClickElement(registerDispensaryPatients.BRT);
        ClickElement(registerDispensaryPatients.SearchWait);
        Thread.sleep(1500);
        CheckSql();
    }

    @Step("Метод проверки количества записей в БД")
    public void CheckSql () throws SQLException {
        String list = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);
        System.out.println("Количество записей в Акушерство и неонатология - " + list);

        sql.StartConnection("SELECT COUNT(*) FROM vimis.akineo_sms_v5_register p\n" +
                "WHERE EXISTS (\n" +
                "        SELECT 1 FROM vimis.akineosms a\n" +
                "        WHERE a.patient_guid = p.patient_guid AND a.doctype IN ('SMSV29', 'SMSV22'));");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }

        Assertions.assertEquals(list, sql.value,
                "Количество записей на вебе " + list + " не совпадает с бд " + sql.value + "");
    }
}