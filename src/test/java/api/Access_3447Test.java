package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты API")
@Feature("Проверка сохранения данных в таблицах парсинга")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Парсинг")
public class Access_3447Test extends BaseAPI {

    @Issue(value = "TEL-3447")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправляем смс и проверяем таблицы парсинга, vmcl = 1")
    @Description("Отправить запрос api/smd с нужым документом, который добавляется в таблицы парсинга, после проверить добавление данных")
    public void Method_3447 () throws SQLException, IOException, InterruptedException {

        AddSms("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 1, 1, true, 3, 1, 9, 18, 1, 57, 21);
        InputPropFile("Vmcl_1_3447", String.valueOf(xml.uuid));

        TableVmcl(1);
        CollbekVimis(String.valueOf(xml.uuid), "1", "Проверка 3447", smsBase, 1);
    }

    @Test
    @DisplayName("Отправляем смс и проверяем таблицы парсинга, vmcl = 2")
    public void Method_3447_2 () throws SQLException, IOException, InterruptedException {
        AddSms("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 2, 2, true, 3, 1, 9, 18, 1, 57, 21);
        InputPropFile("Vmcl_2_3447", String.valueOf(xml.uuid));

        TableVmcl(2);
        CollbekVimis(String.valueOf(xml.uuid), "1", "Проверка 3447", smsBase, 2);
    }

    @Test
    @DisplayName("Отправляем смс и проверяем таблицы парсинга, vmcl = 3")
    public void Method_3447_3 () throws SQLException, IOException, InterruptedException {
        AddSms("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 3, 2, true, 2, 1, 9, 18, 1, 57, 21);
        InputPropFile("Vmcl_3_3447", String.valueOf(xml.uuid));

        TableVmcl(3);
        CollbekVimis(String.valueOf(xml.uuid), "1", "Проверка 3447", smsBase, 3);
    }

    @Test
    @DisplayName("Отправляем смс и проверяем таблицы парсинга, vmcl = 4")
    public void Method_3447_4 () throws SQLException, IOException, InterruptedException {
        AddSms("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 4, 2, true, 3, 1, 9, 18, 1, 57, 21);
        InputPropFile("Vmcl_4_3447", String.valueOf(xml.uuid));

        TableVmcl(4);
        CollbekVimis(String.valueOf(xml.uuid), "1", "Проверка 3447", smsBase, 4);
    }

    @Test
    @DisplayName("Отправляем смс и проверяем таблицы парсинга, vmcl = 5")
    public void Method_3447_5 () throws SQLException, IOException, InterruptedException {
        AddSms("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 5, 2, true, 3, 1, 9, 18, 1, 57, 21);
        InputPropFile("Vmcl_5_3447", String.valueOf(xml.uuid));

        TableVmcl(5);
        CollbekVimis(String.valueOf(xml.uuid), "1", "Проверка 3447", smsBase, 5);
    }

    // #Доделать - добавляется только после успеха от КРЭМД, но сначала долго нужно подождать чтобы получить ошибку
    @Test
    @Disabled
    @DisplayName("Отправляем смс и проверяем таблицы парсинга, vmcl = 99")
    public void Method_3447_99 () throws SQLException, IOException, InterruptedException {
        AddSms("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 99, 2, true, 0, 1, 9, 18, 1, 57, 21);
        InputPropFile("Vmcl_99_3447", String.valueOf(xml.uuid));
    }

    @Step("Отправка смс и проверка таблиц парцсинга")
    public void Method_3447_After (String FileName, Integer vmcl, String localuid) throws SQLException, IOException, InterruptedException {
        TableVmcl(vmcl);
        date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Date = formatForDateNow.format(date);

        System.out.println("Проверяем смс с vmcl = " + vmcl + " и localuid = " + localuid);

        String doctypeSms = null;
        String doctype_versionSms = null;
        String medicalidmuSms = null;
        String patient_snilsSms = null;
        String trigger_pointSms = null;
        String centralized_unloading_system_idSms = null;
        String registered_emd_oidSms = null;
        String transfer_idSms = null;

        String patient_guid = null;
        String patient_snils = null;
        String doctype = null;
        String doctype_version = null;
        String create_date = null;
        String medicalidmu = null;
        String case_idSms = null;
        String trigger_point = null;
        String case_id = null;
        String centralized_unloading_system_id = null;
        String registered_emd_oid = null;
        String version = null;
        String setid = null;
        String documentid = null;
        String effectivetime = null;
        String transfer_id = null;
        String versionSms = null;
        String setidSms = null;
        String documentidSms = null;

        String effect = getXml(FileName, "(//effectiveTime/@value)[1]");
        YearSql = effect.substring(0, 4);
        MonthSql = effect.substring(4, 6);
        DaySql = effect.substring(6, 8);

        sql.URL = "jdbc:postgresql://192.168.2.38:5432/postgres";
        sql.StartConnection("Select s.*, a.\"version\", a.setid, a.documentid from " + smsBase + " s " +
                "join " + infoBase + " a on s.id = a.smsid where local_uid = '" + localuid + "';");
        while (sql.resultSet.next()) {
            doctypeSms = sql.resultSet.getString("doctype");
            medicalidmuSms = sql.resultSet.getString("medicalidmu");
            case_idSms = sql.resultSet.getString("case_id");
            centralized_unloading_system_idSms = sql.resultSet.getString("centralized_unloading_system_id");
            transfer_idSms = sql.resultSet.getString("transfer_id");
            versionSms = sql.resultSet.getString("version");
            setidSms = sql.resultSet.getString("setid");
            documentidSms = sql.resultSet.getString("documentid");

            if (vmcl != 99) {
                doctype_versionSms = sql.resultSet.getString("doctype_version");
                patient_snilsSms = sql.resultSet.getString("patient_snils");
                trigger_pointSms = sql.resultSet.getString("trigger_point");
                registered_emd_oidSms = sql.resultSet.getString("registered_emd_oid");
            }
        }

        // Меняем БД чтобы проверить данные парсинга
        sql.URL = "jdbc:postgresql://192.168.2.38:5432/parsing_dev";
        Integer number = 0;
        while (patient_guid == null & number < 250) {
            if (number != 0) {
                sql.PrintSQL = false;
            }
            sql.StartConnection("Select * from " + common_infoBase + " where local_uid = '" + localuid + "';");
            number++;
            while (sql.resultSet.next()) {
                patient_guid = sql.resultSet.getString("patient_guid");
                patient_snils = sql.resultSet.getString("patient_snils");
                doctype = sql.resultSet.getString("doctype");
                doctype_version = sql.resultSet.getString("doctype_version");
                create_date = sql.resultSet.getString("create_date");
                medicalidmu = sql.resultSet.getString("medicalidmu");
                trigger_point = sql.resultSet.getString("trigger_point");
                case_id = sql.resultSet.getString("case_id");
                centralized_unloading_system_id = sql.resultSet.getString("centralized_unloading_system_id");
                registered_emd_oid = sql.resultSet.getString("registered_emd_oid");
                version = sql.resultSet.getString("version");
                setid = sql.resultSet.getString("setid");
                documentid = sql.resultSet.getString("documentid");
                effectivetime = sql.resultSet.getString("effectivetime");
                transfer_id = sql.resultSet.getString("transfer_id");
            }
        }
        System.out.println("В парсинг сходили " + number + " раз");

        Assertions.assertEquals(patient_guid, PatientGuid, "patient_guid Не совпадает");
        Assertions.assertEquals(doctype, doctypeSms, "doctype Не совпадает");
        Assertions.assertEquals(create_date.substring(0, 10), Date, "create_date Не совпадает");
        Assertions.assertEquals(medicalidmu, medicalidmuSms, "medicalidmu Не совпадает");
        Assertions.assertEquals(case_id, case_idSms, "case_id Не совпадает");
        Assertions.assertEquals(centralized_unloading_system_id, centralized_unloading_system_idSms,
                "centralized_unloading_system_id Не совпадает");
        Assertions.assertEquals(version, versionSms, "version Не совпадает");
        Assertions.assertEquals(setid, setidSms, "setid Не совпадает");
        Assertions.assertEquals(documentid, documentidSms, "documentid Не совпадает");
        Assertions.assertEquals(effectivetime.substring(0, 10), YearSql + "-" + MonthSql + "-" + DaySql,
                "effectivetime Не совпадает");
        Assertions.assertEquals(transfer_id, transfer_idSms, "transfer_id Не совпадает");

        if (vmcl != 99) {
            Assertions.assertEquals(doctype_version, doctype_versionSms, "doctype_version Не совпадает");
            Assertions.assertEquals(patient_snils, patient_snilsSms, "patient_snils Не совпадает");
            Assertions.assertEquals(trigger_point, trigger_pointSms, "trigger_point Не совпадает");
            Assertions.assertEquals(registered_emd_oid, registered_emd_oidSms, "registered_emd_oid Не совпадает");
        }
    }
}