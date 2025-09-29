package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import Base.XML;
import api.Before.Authorization;
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
@Epic("Тесты API")
@Feature("Добавление данных в бд по беременным")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Проверка_info")
@Tag("Основные")
public class Access_1889Test extends BaseAPI {

    public String body;
    public String content;
    public String encodedString;
    public String guestation_age_registated;
    public String pregnancy_registration_date;
    public String pregnancy_number;
    public String pregnancy_outcome;
    public String abortion;
    public String childbirth_term;

    @Issue(value = "TEL-1889")
    @Issue(value = "TEL-1894")
    @Link(name = "ТМС-1791", url = "https://team-1okm.testit.software/projects/5/tests/1790?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Добавление данных в бд по беременным для смс 5")
    @Description("Отправить смс 5/17/32 c vmcl = 3, перейти в таблицу vimis.akineoadditionalinfo и проверить добавление данных по регистру беременных")
    public void Access_1889_5 () throws IOException, SQLException, InterruptedException {
        Access_1889Method("SMS/SMS5-vmcl=3(v1.2).xml", "5", 3, 2, 2, 6, 4,
                18, 1, 57, 21);
    }

    @Test
    @DisplayName("Добавление данных в бд по беременным для смс 17")
    public void Access_1889_17 () throws IOException, SQLException, InterruptedException {
        Access_1889Method("SMS/SMS17-vmcl=3.xml", "19", 3, 2, 2, 6, 4,
                18, 1, 57, 21);
    }

    @Test
    @DisplayName("Добавление данных в бд по беременным для смс 8")
    public void Access_1889_8 () throws IOException, SQLException, InterruptedException {
        Access_1889Method("SMS/SMS8-vmcl=3(v1.2).xml", "8", 3, 1, 2, 6, 4,
                18, 1, 57, 21);
    }

    @Test
    @DisplayName("Добавление данных в бд по беременным для смс 22")
    public void Access_1889_22 () throws IOException, SQLException, InterruptedException {
        Access_1889Method("SMS/SMS22-vmcl=3.xml", "24", 3, 5, 3, 6, 4,
                18, 1, 57, 21);
    }

    @Test
    @DisplayName("Добавление данных в бд по беременным для смс 30")
    public void Access_1889_30 () throws IOException, SQLException, InterruptedException {
        Access_1889Method("SMS/SMS30-vmcl=3.xml", "112", 3, 2, 3, 6, 4,
                18, 1, 57, 21);
    }

    @Step("Метод отправки смс: {0}, vmcl = {3}, и проверки данных в таблице vimis.akineoadditionalinfo ")
    public void Access_1889Method (String FileName, String DocType, Integer vmcl, Integer triggerPoint, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException, SQLException {

        Token = TokenInit();
        XML.Type = FileName;

        xml.XmlStart(FileName, Departmen, MO, PatientGuid, NameMO, Snils, 1);
        xml.JsonChange(DocType, String.valueOf(vmcl), String.valueOf(triggerPoint), String.valueOf(docTypeVersion), String.valueOf(Role), String.valueOf(position), String.valueOf(speciality), String.valueOf(Role1), String.valueOf(position1), String.valueOf(speciality1));

        xml.changes.put("$.PatientGuid", PatientGuid);
        String[] pathDelete = {};
        // Меняем параметры в нужном файле
        String modifiedJson = JsonMethod("SMS/Body/body.json", xml.changes, true, pathDelete);

        System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
        Api(HostAddress + "/api/smd", "post", null, null, modifiedJson, 200, true);
        Thread.sleep(1500);

        sql.StartConnection(
                "SELECT * FROM vimis.akineosms  where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            Assertions.assertNotEquals(sql.value, "NULL", "СМС не добавилось в таблицу vimis.akineosms");
            System.out.println(sql.value);
        }

        sql.StartConnection(
                "SELECT * FROM vimis.akineoadditionalinfo  where smsid = '" + sql.value + "';");
        while (sql.resultSet.next()) {
            guestation_age_registated = sql.resultSet.getString("guestation_age_registated");
            pregnancy_registration_date = sql.resultSet.getString("pregnancy_registration_date");
            pregnancy_number = sql.resultSet.getString("pregnancy_number");
            pregnancy_outcome = sql.resultSet.getString("pregnancy_outcome");
            abortion = sql.resultSet.getString("abortion");
            childbirth_term = sql.resultSet.getString("childbirth_term");
        }

        if (DocType == "5") {
            Assertions.assertEquals(guestation_age_registated, "1",
                    "guestation_age_registated не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_registration_date, "2020-05-25 17:30:00+05",
                    "pregnancy_registration_date не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_number, "2", "pregnancy_number не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_outcome, "{8,8}", "pregnancy_outcome не совпадает с нужным значением");
            Assertions.assertEquals(abortion, "14", "abortion не совпадает с нужным значением");
            Assertions.assertEquals(childbirth_term, "37", "childbirth_term не совпадает с нужным значением");
        }
        if (DocType == "17") {
            Assertions.assertEquals(guestation_age_registated, "1",
                    "guestation_age_registated не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_registration_date, "2020-05-25 17:30:00+05",
                    "pregnancy_registration_date не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_number, "2", "pregnancy_number не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_outcome, "{8,8}", "pregnancy_outcome не совпадает с нужным значением");
            Assertions.assertEquals(abortion, null, "abortion не совпадает с нужным значением");
            Assertions.assertEquals(childbirth_term, "39", "childbirth_term не совпадает с нужным значением");
        }
        if (DocType == "8") {
            Assertions.assertEquals(guestation_age_registated, null,
                    "guestation_age_registated не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_registration_date, null,
                    "pregnancy_registration_date не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_number, "2", "pregnancy_number не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_outcome, "{}", "pregnancy_outcome не совпадает с нужным значением");
            Assertions.assertEquals(abortion, "14", "abortion не совпадает с нужным значением");
            Assertions.assertEquals(childbirth_term, "37", "childbirth_term не совпадает с нужным значением");
        }
        if (DocType == "24") {
            Assertions.assertEquals(guestation_age_registated, null,
                    "guestation_age_registated не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_registration_date, null,
                    "pregnancy_registration_date не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_number, null, "pregnancy_number не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_outcome, "{8,8}", "pregnancy_outcome не совпадает с нужным значением");
            Assertions.assertEquals(abortion, "14", "abortion не совпадает с нужным значением");
            Assertions.assertEquals(childbirth_term, "23", "childbirth_term не совпадает с нужным значением");
        }
        if (DocType == "112") {
            Assertions.assertEquals(guestation_age_registated, null,
                    "guestation_age_registated не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_registration_date, null,
                    "pregnancy_registration_date не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_number, null, "pregnancy_number не совпадает с нужным значением");
            Assertions.assertEquals(pregnancy_outcome, "{}", "pregnancy_outcome не совпадает с нужным значением");
            Assertions.assertEquals(abortion, null, "abortion не совпадает с нужным значением");
            Assertions.assertEquals(childbirth_term, null, "childbirth_term не совпадает с нужным значением");
        }
        if (DocType == "24") {
            if (KingNumber == 1) {
                xml.ReplaceWord(FileName, "1.2.643.5.1.13.13.12.2.86.9006", "${ksomo}");
                xml.ReplaceWord(FileName, "2022", "${ksoyear}");
                xml.ReplaceWord(FileName, "22-86-00002", "${ksonumber}");
            }
            if (KingNumber == 2) {
                xml.ReplaceWord(FileName, "1.2.643.5.1.13.13.12.2.86.9006", "${ksomo}");
                xml.ReplaceWord(FileName, "2021", "${ksoyear}");
                xml.ReplaceWord(FileName, "21-86-05295", "${ksonumber}");
            }
            if (KingNumber == 4) {
                xml.ReplaceWord(FileName, "1.2.643.5.1.13.13.12.2.86.9006", "${ksomo}");
                xml.ReplaceWord(FileName, "2021", "${ksoyear}");
                xml.ReplaceWord(FileName, "21-77-65842", "${ksonumber}");
            }
        }
    }
}
