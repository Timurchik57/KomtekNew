package api;

import Base.*;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;


@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Добавление данных диспансерных больных в таблицу по смс5")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Регистр_Профилактики")
@Tag("Основные")
public class Access_1786Test extends BaseAPI {

    public String body;
    public String content;
    public String encodedString;

    @Issue(value = "TEL-1786")
    @Issue(value = "TEL-1800")
    @Issue(value = "TEL-1896")
    @Link(name = "ТМС-1767", url = "https://team-1okm.testit.software/projects/5/tests/1767?isolatedSection=623e281e-2190-42e3-913b-8beea1fbc57d")
    @Link(name = "ТМС-1941", url = "https://team-1okm.testit.software/projects/5/tests/1941")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправить СЭМД смс5 с triggerPoint = 6, установить статус 1 в таблицу vimis.preventionlogs, проверить заполение теблицы vimis.prevention_sms_v5_register")
    @Test
    @DisplayName("Добавление данных диспансерных больных в таблицу по смс5 c triggerPoint = 6")
    public void Access_1786_6() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS5-vmcl=2.xml", "5", 2, 6, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms",
                          "vimis.preventionlogs");
    }

    @Test
    @DisplayName("Добавление данных диспансерных больных в таблицу по смс5 c triggerPoint = 12")
    public void Access_1786_12() throws IOException, InterruptedException, SQLException {
        Access_1786Method("SMS/SMS5-vmcl=2.xml", "5", 2, 12, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms",
                          "vimis.preventionlogs");
    }

    @Step("Отправляем смс = {1} и добавляем статус 1 в таблицу {11}")
    public void Access_1786Method(String FileName, String DocType, Integer vmcl, Integer triggerPoint, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String log) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        xml = new XML();

        Token = TokenInit();

        xml.XmlStart(FileName, Departmen, MO, PatientGuid, NameMO, Snils, 1);
        xml.JsonChange(DocType, String.valueOf(vmcl), String.valueOf(triggerPoint), String.valueOf(docTypeVersion), String.valueOf(Role), String.valueOf(position), String.valueOf(speciality), String.valueOf(Role1), String.valueOf(position1), String.valueOf(speciality1));

        xml.changes.put("$.PatientGuid", PatientGuid);
        String[] pathDelete = {};
        if (vmcl == 99) {
            // Удаляем (если нужно) параметры
            pathDelete = new String[]{"$.VMCL[0].triggerPoint", "$.VMCL[0].docTypeVersion"};
        }
        // Меняем параметры в нужном файле
        String modifiedJson = JsonMethod("SMS/Body/body.json", xml.changes, true, pathDelete);

        System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
        Api(HostAddress + "/api/smd", "post", null, null, modifiedJson, 200, true);
        Thread.sleep(1500);

        sql.StartConnection(
                "SELECT * FROM " + sms + " where  local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            System.out.println(sql.value);
        }
        Assertions.assertNotNull(sql.value,  "СМС не добавилось в таблицу " + sms + "");

        System.out.println("Устанавливаем status = 1 в " + log + "");
        CollbekVimis("" + xml.uuid + "", "1", "Проверка 1786", sms, vmcl);

        if (ReadPropFile("className").contains("Access_1786Test")) {
            if (triggerPoint == 6) {
                InputPropFile("Local_uid_1786_tg6", String.valueOf(xml.uuid));
                InputPropFile("value_1786_Vmcl_2_tg6", sql.value);
            } else {
                InputPropFile("Local_uid_1786_tg12", String.valueOf(xml.uuid));
                InputPropFile("value_1786_Vmcl_2_tg12", sql.value);
            }
        }
    }

    @Step("Проверка добавления отправленной смс поле всех тестов в таблице = {1}")
    public void Access_1786After(String NameProp, String remd) throws IOException, SQLException {
        sql = new SQL();
        xml = new XML();

        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {

            System.out.println("Проверка добавления значения по заявке 1786");
            System.out.println(props.getProperty("" + NameProp + ""));
            sql.SQL("Select count(*) from " + remd + " where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
            sql.StartConnection(
                    "Select * from " + remd + " where sms_id = '" + props.getProperty("" + NameProp + "") + "';");
            while (sql.resultSet.next()) {
                String mo_oid = sql.resultSet.getString("mo_oid");
                String patient_guid = sql.resultSet.getString("patient_guid");
                String dispensary_start_datetime = sql.resultSet.getString("dispensary_start_datetime");
                String dispensary_end_datetime = sql.resultSet.getString("dispensary_end_datetime");
                String diagnosis = sql.resultSet.getString("diagnosis");
                String dispensary_reason_removal_id = sql.resultSet.getString("dispensary_reason_removal_id");
                String visit_plan_datetime = sql.resultSet.getString("visit_plan_datetime");
                String local_uid = sql.resultSet.getString("local_uid");

                Assertions.assertEquals(mo_oid, MO, "mo_oid не совпадает");
                Assertions.assertEquals(patient_guid, PatientGuid, "patient_guid не совпадает");

                Assertions.assertEquals(dispensary_start_datetime.substring(0, 10), dateXml(getXml("SMS/SMS5-vmcl=2.xml", "//code[@displayName='Начало диспансерного наблюдения']/following-sibling::effectiveTime/@value")), "dispensary_start_datetime не совпадает");

                /** Данный параметр будет равен значению, который указан в документе, но если по данному пациенту уже есть в бд смс13 (смерть), то параметр будет равен этой же дате но из документа смс13 (1896) */
                Assertions.assertEquals(dispensary_end_datetime.substring(0, 10), "2015-08-15", "dispensary_start_datetime не совпадает");

                Assertions.assertEquals(diagnosis, getXml("SMS/SMS5-vmcl=2.xml", "//code[@displayName='Диспансерное наблюдение']/following-sibling::entry[2]//value[@codeSystemName='Международная статистическая классификация болезней и проблем, связанных со здоровьем (10-й пересмотр)']/@code"), "diagnosis не совпадает");

                /** Данный параметр будет равен значению, который указан в документе, но если по данному пациенту уже есть в бд смс13 (смерть), то параметр будет равен 4 (1896) */
                Assertions.assertEquals(dispensary_reason_removal_id, "4", "dispensary_reason_removal_id не совпадает");

                Assertions.assertEquals(visit_plan_datetime.substring(0, 10), dateXml(getXml("SMS/SMS5-vmcl=2.xml", "//code[@displayName='Следующая явка']/following-sibling::effectiveTime/@value")), "dispensary_start_datetime не совпадает");

                if (NameProp == "value_1786_Vmcl_2_tg6") {
                    Assertions.assertEquals(local_uid, "" + props.getProperty("Local_uid_1786_tg6") + "",
                            "local_uid не совпадает");
                } else {
                    Assertions.assertEquals(local_uid, "" + props.getProperty("Local_uid_1786_tg12") + "",
                            "local_uid не совпадает");
                }
            }
            System.out.println("Значение created_datetime по заявке 1786 добавились в таблицу " + remd + "");
        }
    }

    @Step("Меняем формат даты")
    public String dateXml (String date) {
        String str = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
        return str;
    }
}
