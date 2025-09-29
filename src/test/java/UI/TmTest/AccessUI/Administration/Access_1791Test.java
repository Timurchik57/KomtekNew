package UI.TmTest.AccessUI.Administration;

import Base.*;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.Administration.TypeRegistr;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Registry.RegisterDispensaryPatients;
import api.Access_1786Test;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Типы_регистров")
@Tag("Роли_доступа")
@Tag("Уровень_мед_организаций")
@Tag("Регистр_Профилактики")
public class Access_1791Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    TypeRegistr typeRegistr;
    RegisterDispensaryPatients registerDispensaryPatients;
    public Integer number;
    Access_1898Test access1898Test;
    MedOrganization medOrganization;

    @Issue(value = "TEL-1791")
    @Link(name = "ТМС-1772", url = "https://team-1okm.testit.software/projects/5/tests/1772?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Owner(value = "Галиакберов Тимур")
    @Description("Создаём новый регистр с Профилактикой - переходим в данный регистр и проверяем добавленную запись из таблицы prevention_sms_v5_register")
    @Test
    @DisplayName("Отображение данных из регистра Профилактика")
    public void Access_1791() throws SQLException, IOException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        Access_1786Test access_1786Test = new Access_1786Test();
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        number = 1;
        access1898Test = new Access_1898Test();
        medOrganization = new MedOrganization(driver);

        System.out.println("Переходим в Роли доступа");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Редактирование роли - Добавляем все 6 доступа по СЭМД");
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" на СЭМД", true);
        AddRole(PRole,"Доступ к разделу \"Нозологические регистры\" по любой МО", false);

        System.out.println("Повторно авторизуеммся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Проверяем создан ли нужный регистр");
        ClickElement(typeRegistr.TypeRegistrWait);
        while (!isElementNotVisible(By.xpath("//table//tbody//tr//span[contains(.,'Регистр Профилактики')]"))) {
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
            inputWord(typeRegistr.InputNameRegistr, "Регистр Профилактикии");
            inputWord(typeRegistr.InputShortNameRegistr, "Регистр Профилактикии");
            SelectClickMethod(typeRegistr.SourceData, typeRegistr.SelectSourceDataPrev);
            typeRegistr.AddDiagnosis.click();
            WaitElement(typeRegistr.SelectAddDiagnosisWait);
            inputWord(typeRegistr.CodRegistr, "A000");
            ClickElement(typeRegistr.FirstCodRegistrWait);
            ClickElement(typeRegistr.AddCodRegistrWait);
            Thread.sleep(1500);
        }
        ClickElement(registerDispensaryPatients.RegistrSelect("Профилактика"));
        ClickElement(registerDispensaryPatients.RegistrPrev);
        WaitElement(registerDispensaryPatients.FIO);
        Thread.sleep(3000);
        String list1 = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);
        System.out.println(list1);

        InputPropFile("SizePatients_1791", list1);

        System.out.println("Отправляем нужную смс");
        access_1786Test.Access_1786Method("SMS/SMS5-vmcl=2.xml", "5", 2, 6, 3, 6, 4, 18, 1, 57, 21,
                "vimis.preventionsms", "vimis.preventionlogs");
        Thread.sleep(1500);
        InputPropFile("value_VN_1791", String.valueOf(VN));

        sql.StartConnection("Select * from vimis.preventionsms order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            InputPropFile("Id_1791", sql.value);

            sql.value = sql.resultSet.getString("local_uid");
            InputPropFile("localUid_1791", sql.value);
        }
    }

    @Step("Переходим к Регистр Профилактики и проверяем, что запись добавились")
    public void Access_1791After(String NameProp) throws IOException, InterruptedException, SQLException {
        authorizationObject = new AuthorizationObject(driver);
        typeRegistr = new TypeRegistr(driver);
        registerDispensaryPatients = new RegisterDispensaryPatients(driver);
        number = 1;
        medOrganization = new MedOrganization(driver);
        access1898Test = new Access_1898Test();

        String mo_oid = null;
        String patient_guid = null;
        String dispensary_start_datetime = null;
        String dispensary_end_datetime = null;
        String diagnosis = null;
        String dispensary_reason_removal_id = null;
        String visit_plan_datetime = null;
        String version = null;
        String local_uid = null;

        sql = new SQL();

        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        System.out.println("Переход в Типы регистров для добавления диагноза");
        typeRegistr.AddDiagnosis(getXml("SMS/SMS5-vmcl=2.xml","//code[@code='809']/following-sibling::value/@code"));

        System.out.println("Переходим в Регистр Профилактики");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(registerDispensaryPatients.RegistrSelect("Профилактика"));
        ClickElement(registerDispensaryPatients.RegistrPrev);
        Thread.sleep(1500);
        WaitElement(registerDispensaryPatients.FIO);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 20);
        String list2 = driver.findElement(registerDispensaryPatients.CountList).getText().substring(9);
        System.out.println(list2);

        System.out.println("Было количество записей в Регистр Профилактики - " + props.getProperty("" + NameProp + ""));
        System.out.println("Стало количество записей в Регистр Профилактики - " + list2);

        Assertions.assertNotEquals(props.getProperty("" + NameProp + ""), list2,
                "Количество пациентов не увеличилось");

        sql.StartConnection("SELECT * FROM vimis.prevention_sms_v5_register x\n" +
                "where x.sms_id = '"+props.getProperty("Id_1791")+"';");
        while (sql.resultSet.next()) {
            mo_oid = sql.resultSet.getString("mo_oid");
            patient_guid = sql.resultSet.getString("patient_guid");
            dispensary_start_datetime = sql.resultSet.getString("dispensary_start_datetime");
            dispensary_end_datetime = sql.resultSet.getString("dispensary_end_datetime");
            diagnosis = sql.resultSet.getString("diagnosis");
            dispensary_reason_removal_id = sql.resultSet.getString("dispensary_reason_removal_id");
            visit_plan_datetime = sql.resultSet.getString("visit_plan_datetime");
            version = sql.resultSet.getString("version");
            local_uid = sql.resultSet.getString("local_uid");
        }

        Assertions.assertEquals(mo_oid, MO, "mo_oid не совпадает из таблицы vimis.prevention_sms_v5_register");
        Assertions.assertEquals(patient_guid, PatientGuid, "patient_guid не совпадает из таблицы vimis.prevention_sms_v5_register");

        ChangeDate(dispensary_start_datetime);
        Assertions.assertEquals(YearSql + MonthSql + DaySql, getXml("SMS/SMS5-vmcl=2.xml", "//code[@code='7002']/following-sibling::effectiveTime/@value"), "dispensary_start_datetime не совпадает из таблицы vimis.prevention_sms_v5_register");

        /** Данный параметр будет равен значению, который указан в документе, но если по данному пациенту уже есть в бд смс13 (смерть), то параметр будет равен этой же дате но из документа смс13 (1896) */
        ChangeDate(dispensary_end_datetime);
        Assertions.assertEquals(YearSql + MonthSql + DaySql, "20150815", "dispensary_end_datetime не совпадает из таблицы vimis.prevention_sms_v5_register");

        Assertions.assertEquals(diagnosis, getXml("SMS/SMS5-vmcl=2.xml","//code[@code='809']/following-sibling::value/@code"), "diagnosis не совпадает из таблицы vimis.prevention_sms_v5_register");

        /** Данный параметр будет равен значению, который указан в документе, но если по данному пациенту уже есть в бд смс13 (смерть), то параметр будет равен 4 (1896) */
        Assertions.assertEquals(dispensary_reason_removal_id, "4", "dispensary_reason_removal_id не совпадает из таблицы vimis.prevention_sms_v5_register");

        ChangeDate(visit_plan_datetime);
        Assertions.assertEquals(YearSql + MonthSql + DaySql, getXml("SMS/SMS5-vmcl=2.xml", "//code[@code='7005']/following-sibling::effectiveTime/@value"), "visit_plan_datetime не совпадает из таблицы vimis.prevention_sms_v5_register");
        Assertions.assertEquals(version, props.getProperty("value_VN_1791"), "version не совпадает из таблицы vimis.prevention_sms_v5_register");
        Assertions.assertEquals(local_uid, props.getProperty("localUid_1791"), "local_uid не совпадает из таблицы vimis.prevention_sms_v5_register");
    }
}
