package api.REMD;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Передача Association В СЭМД и проверка remd_associations")
@Tag("api/smd")
@Tag("МИНИО")
@Tag("Поиск_api/rremd")
@Tag("Основные")
public class Access_1484Test extends BaseAPI {

    public String Access_1103;
    public String AssociationTarget;
    public String AssociationType;
    public String AssociationTarget_1;
    public String AssociationType_1;
    public String body;
    public String content;
    public String encodedString;
    public String textVmxl;
    public UUID uuid;

    @Issue(value = "TEL-1484")
    @Link(name = "ТМС-1566", url = "https://team-1okm.testit.software/projects/5/tests/1566?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 99")
    @Description("Отправляем смс с блоком Association, где передаётся массив значений в теле запроса, после проверяем таблицу onko/cvd/akineo/prevention/ remd_sent_result. Проверяем новые таблицы1- remd_onko_associations ,2-remd_cvd_associations,3-remd_akineo_associations,4-remd_prevention_associations, 99 -remd_associations на заполненность данными. Для смс в ВИМИС устанавливаем статус 1 в logs и добавляем запись в remd, после используем запрос api/rremd и проверяем, что передались все значения")
    public void Access_1484Vmcl_99() throws IOException, SQLException, InterruptedException {
        Access_1484Method(
                "SMS/id=15-vmcl=99.xml", "15", 99, 1, true, 1, 6, 4, 18, 1, 57, 21, "", "vimis.remd_sent_result", "",
                "vimis.remd_associations", ""
        );
    }

    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 1")
    public void Access_1484Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1484Method(
                "SMS/id=15-vmcl=99.xml", "15", 1, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.sms",
                "vimis.remd_onko_sent_result", "vimis.documentlogs", "", "vimis.remd_onko_associations"
        );
    }

    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 2")
    public void Access_1484Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1484Method(
                "SMS/id=15-vmcl=99.xml", "15", 2, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms",
                "vimis.remd_prevention_sent_result", "vimis.preventionlogs", "", "vimis.remd_prevention_associations"
        );
    }

    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 3")
    public void Access_1484Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1484Method(
                "SMS/id=15-vmcl=99.xml", "15", 3, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.akineosms",
                "vimis.remd_akineo_sent_result", "vimis.akineologs", "", "vimis.remd_akineo_associations"
        );
    }

    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 4")
    public void Access_1484Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1484Method(
                "SMS/id=15-vmcl=99.xml", "15", 4, 1, true, 2, 6, 4, 18, 1, 57, 21, "vimis.cvdsms",
                "vimis.remd_cvd_sent_result", "vimis.cvdlogs", "", "vimis.remd_cvd_associations"
        );
    }

    @Test
    @DisplayName("Передача Association В СЭМД для vmcl = 5")
    public void Access_1484Vmcl_5() throws IOException, SQLException, InterruptedException {
        Access_1484Method(
                "SMS/id=15-vmcl=99.xml", "15", 5, 1, true, 3, 6, 4, 18, 1, 57, 21, "vimis.infectionsms",
                "vimis.remd_infection_sent_result", "vimis.infectionlogs", "", "vimis.remd_infection_associations"
        );
    }

    @Test
    @DisplayName("Передача Association В СЭМД для id = 101")
    public void Access_1484Id_101() throws IOException, SQLException, InterruptedException {
        Access_1484Method("SMS/id=101-vmcl=99.txt", "101", 99, 1, true, 2, 1, 57, 18, 1, 57, 21, "", "", "", "", "");
    }

    public void Access_1484Method(String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String remd, String logs, String AsRemd, String AsSms) throws IOException, InterruptedException, SQLException {

        xml.XmlStart(FileName, Departmen, MO, PatientGuid, NameMO, Snils, 1);
        xml.JsonChange(DocType, String.valueOf(vmcl), "2", String.valueOf(docTypeVersion), String.valueOf(Role), String.valueOf(position), String.valueOf(speciality), String.valueOf(Role1), String.valueOf(position1), String.valueOf(speciality1));

        /* Добавляем новое поле
        * "association":[
            {
                "target":"1484",
                "type":"1484"
            },
            {
                "target":"1484_1",
                "type":"1484_1"
            }
           ] */
        // Добавление первого объекта
        Map<String, Object> firstObject = new HashMap<>();
        firstObject.put("target", "1484");
        firstObject.put("type", "1484");
        xml.associationList.add(firstObject);

        // Добавление второго объекта
        Map<String, Object> secondObject = new HashMap<>();
        secondObject.put("target", "1484_1");
        secondObject.put("type", "1484_1");
        xml.associationList.add(secondObject);

        // Добавим associationList в addElements под нужным путём
        xml.addElements.put("$.association", xml.associationList);

        String[] pathDelete = {};
        if (vmcl == 99) {
            // Удаляем (если нужно) параметры
            pathDelete = new String[]{"$.VMCL[0].triggerPoint", "$.VMCL[0].docTypeVersion"};
        }
        // Меняем параметры в нужном файле
        String modifiedJson = JsonMethod("SMS/Body/body.json", xml.changes, true, pathDelete);

        Api(HostAddress + "/api/smd", "post", null, null, modifiedJson, 200, true);

        if (FileName == "SMS/id=101-vmcl=99.txt") {
            Assertions.assertEquals(
                    Response.get("result[0].errorMessage"),
                    "Association: При отправке региональных справок недопустима передача идентификаторов связанных документов РЭМД в блоке association",
                    "Не появилась ошибка - При отправке региональных справок недопустима передача идентификаторов связанных документов РЭМД в блоке association"
            );
        } else {
            if (vmcl == 99) {
                sql.StartConnection(
                        "Select * from " + remd + " where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    Access_1103 = sql.resultSet.getString("id");
                    System.out.println(Access_1103);
                }
                sql.StartConnection(
                        "Select * from " + AsRemd + " where sms_id = '" + Access_1103 + "' limit 1 offset 0;");
                while (sql.resultSet.next()) {
                    AssociationTarget = sql.resultSet.getString("target");
                    AssociationType = sql.resultSet.getString("type");
                    System.out.println(AssociationTarget);
                    System.out.println(AssociationType);
                }
                sql.StartConnection(
                        "Select * from " + AsRemd + " where sms_id = '" + Access_1103 + "' limit 1 offset 1;");
                while (sql.resultSet.next()) {
                    AssociationTarget_1 = sql.resultSet.getString("target");
                    AssociationType_1 = sql.resultSet.getString("type");
                }
            } else {
                sql.StartConnection(
                        "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    Access_1103 = sql.resultSet.getString("id");
                    System.out.println(Access_1103);
                }
                sql.StartConnection(
                        "Select * from " + AsSms + " where sms_id = '" + Access_1103 + "' limit 1 offset 0;");
                while (sql.resultSet.next()) {
                    AssociationTarget = sql.resultSet.getString("target");
                    AssociationType = sql.resultSet.getString("type");
                }
                sql.StartConnection(
                        "Select * from " + AsSms + " where sms_id = '" + Access_1103 + "' limit 1 offset 1;");
                while (sql.resultSet.next()) {
                    AssociationTarget_1 = sql.resultSet.getString("target");
                    AssociationType_1 = sql.resultSet.getString("type");
                }
                System.out.println("Устанавливаем status = 1 в " + logs + "");
                sql.UpdateConnection(
                        "insert into " + logs + " (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + Access_1103 + "', '" + UUID.randomUUID() + "')");
                System.out.println("Устанавливаем status = 1 в " + remd + "");
                sql.UpdateConnection(
                        "insert into " + remd + " (sms_id, local_uid, status, created_datetime, fremd_status) values (" + Access_1103 + ", '" + xml.uuid + "', 'success', '" + Date + " 00:00:00.888 +0500', NULL);");
            }

            JsonPath response = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .param("LocalUid", xml.uuid)
                    .when()
                    .get(URLKREMD)
                    .body()
                    .jsonPath();
            if (SmdToMinio == 1) {
                Assertions.assertEquals(
                        AssociationType, response.get("result[0].documentDto.associations[0].type"),
                        "type не совпадает с отправляемым значением"
                );
                Assertions.assertEquals(
                        AssociationTarget, response.get("result[0].documentDto.associations[0].target"),
                        "target не совпадает с отправляемым значением"
                );
                Assertions.assertEquals(
                        AssociationType_1, response.get("result[0].documentDto.associations[1].type"),
                        "type не совпадает с отправляемым значением"
                );
                Assertions.assertEquals(
                        AssociationTarget_1, response.get("result[0].documentDto.associations[1].target"),
                        "target не совпадает с отправляемым значением"
                );
            } else {
                Assertions.assertEquals(
                        AssociationType, response.get("result[0].associations[0].type"),
                        "type не совпадает с отправляемым значением"
                );
                Assertions.assertEquals(
                        AssociationTarget, response.get("result[0].associations[0].target"),
                        "target не совпадает с отправляемым значением"
                );
                Assertions.assertEquals(
                        AssociationType_1, response.get("result[0].associations[1].type"),
                        "type не совпадает с отправляемым значением"
                );
                Assertions.assertEquals(
                        AssociationTarget_1, response.get("result[0].associations[1].target"),
                        "target не совпадает с отправляемым значением"
                );
            }
        }
    }
}
