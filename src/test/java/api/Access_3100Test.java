package api;

import Base.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Добавление данных диспансерных больных в таблицу по смс 5 инфекция")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Дополнительные_параметры")
@Tag("Регистр_Инфекции")
@Tag("Типы_регистров")
public class Access_3100Test extends BaseAPI {

    public String body;
    public String content;
    public String encodedString;

    @Issue(value = "TEL-3100")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправить СЭМД смс5 vmcl = 5 с triggerPoint = 12, установить статус 1 в таблицу vimis.preventionlogs, проверить заполение теблицы vimis.infection_sms_v5_register")
    @Test
    @DisplayName("Добавление данных диспансерных больных в таблицу по смс 5 c triggerPoint = 12 по инфекции")
    public void Access_3100_12() throws IOException, InterruptedException, SQLException {
        Access_3100Method("SMS/SMS5-vmcl=5.xml", "5", 5, 12, 3, 6, 4, 18, 1, 57, 21, "vimis.infectionsms",
                "vimis.infectionlogs");
    }

    @Step("Отправляем смс = {1} и добавляем статус 1 в таблицу {11}")
    public void Access_3100Method(String FileName, String DocType, Integer vmcl, Integer triggerPoint, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String log) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        xml = new XML();
        date = new Date();

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Date = formatForDateNow.format(date);

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

        System.out.println("Запсываем полученный xml в файл");
        PrintWriter out = new PrintWriter("src/test/resources/ignored/1219_test.xml");
        out.println(modifiedJson);
        out.close();

        sql.StartConnection(
                "SELECT * FROM " + sms + "  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            System.out.println(sql.value);
            Assertions.assertNotEquals(sql.value, "NULL", "СМС не добавилось в таблицу " + sms + "");
        }

        System.out.println("Устанавливаем status = 1 в " + log + "");
        CollbekVimis("" + xml.uuid + "", "1", "Проверка 3100", sms, vmcl);

        if (ReadPropFile("className").contains("Access_3100Test")) {
            if (triggerPoint == 6) {
                InputPropFile("Local_uid_3100_tg6", String.valueOf(xml.uuid));
                InputPropFile("value_3100_Vmcl_5_tg6", sql.value);
            } else {
                InputPropFile("Local_uid_3100_tg12", String.valueOf(xml.uuid));
                InputPropFile("value_3100_Vmcl_5_tg12", sql.value);
            }
        }
    }

    @Step("Проверка добавления отправленной смс поле всех тестов в таблице = {1}")
    public void Access_3100After(String NameProp, String remd) throws IOException, SQLException {
        sql = new SQL();
        xml = new XML();

        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        System.out.println("Проверка добавления значения по заявке 3100");
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

            Assertions.assertEquals(dispensary_start_datetime.substring(0, 10), dateXml(getXml("SMS/SMS5-vmcl=5.xml",
                            "//code[@displayName='Начало диспансерного наблюдения']/following-sibling::effectiveTime/@value")),
                    "dispensary_start_datetime не совпадает");

            /** Данный параметр будет равен значению, который указан в документе, но если по данному пациенту уже есть в бд смс13 (смерть), то параметр будет равен этой же дате но из документа смс13 (1896)*/

            Assertions.assertEquals(dispensary_end_datetime.substring(0, 10), "2015-08-15",
                    "dispensary_start_datetime не совпадает");

            Assertions.assertEquals(diagnosis, getXml("SMS/SMS5-vmcl=5.xml",
                            "//code[@displayName='Диспансерное наблюдение']/following-sibling::entry[2]//value[@codeSystemName='Международная статистическая классификация болезней и проблем, связанных со здоровьем (10-й пересмотр)']/@code"),
                    "diagnosis не совпадает");

            /** Данный параметр будет равен значению, который указан в документе, но если по данному пациенту уже есть в бд смс13 (смерть), то параметр будет равен 4 (1896) */
            Assertions.assertEquals(dispensary_reason_removal_id, "4", "dispensary_reason_removal_id не совпадает");

            Assertions.assertEquals(visit_plan_datetime.substring(0, 10), dateXml(getXml("SMS/SMS5-vmcl=5.xml",
                            "//code[@displayName='Следующая явка']/following-sibling::effectiveTime/@value")),
                    "dispensary_start_datetime не совпадает");

            if (NameProp == "value_3100_Vmcl_5_tg6") {
                Assertions.assertEquals(local_uid, "" + props.getProperty("Local_uid_3100_tg6") + "",
                        "local_uid не совпадает");
            } else {
                Assertions.assertEquals(local_uid, "" + props.getProperty("Local_uid_3100_tg12") + "",
                        "local_uid не совпадает");
            }
        }
        System.out.println("Значение created_datetime по заявке 3100 добавились в таблицу " + remd + "");
    }

    @Step("Меняем формат даты")
    public String dateXml(String date) {
        String str = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
        return str;
    }
}
