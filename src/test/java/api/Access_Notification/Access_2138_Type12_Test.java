package api.Access_Notification;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Тесты API")
@Feature("Уведомление с типом 12")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Api_Направление_на_диагностику")
public class Access_2138_Type12_Test extends BaseAPI {

    Authorization authorization;
    public String TransferId;
    public static String DirectionGuid;

    @Test
    @Order(1)
    public void BeforeDirections () throws IOException, SQLException {
        authorization = new Authorization();

        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        xml.changes.put("$.TargetMedicalOid", POidMoRequest);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        DirectionGuid = Response.getString("Result.Guid");
        InputPropFile("DirectionGuid_2138", DirectionGuid);
        System.out.println(DirectionGuid);

    }

    @Issue(value = "TEL-2138")
    @Issue(value = "TEL-4131")
    @Issue(value = "TEL-4307")
    @Link(name = "ТМС-1875", url = "https://team-1okm.testit.software/projects/5/tests/1875?isolatedSection=65a30e47-043a-4dd0-af4f-165d06d17427")
    @Owner(value = "Галиакберов Тимур")
    @Description("Добавить уведомление с типом 12 для всех vmcl - отправть смс по всем направлениям - проверить уведомление")
    @Test
    @DisplayName("Уведомление с типом 12 для vmcl = 1")
    public void Access_2138_1 () throws IOException, SQLException, InterruptedException {
        System.out.println(DirectionGuid);
        Access_2138Method("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21, "vimis.sms");
    }

    @Test
    @DisplayName("Уведомление с типом 12 для vmcl = 2")
    public void Access_2138_2 () throws IOException, SQLException, InterruptedException {
        Access_2138Method("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms");
    }

    @Test
    @DisplayName("Уведомление с типом 12 для vmcl = 3")
    public void Access_2138_3 () throws IOException, SQLException, InterruptedException {
        Access_2138Method("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms");
    }

    @Test
    @DisplayName("Уведомление с типом 12 для vmcl = 4")
    public void Access_2138_4 () throws IOException, SQLException, InterruptedException {
        Access_2138Method("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms");
    }

    @Test
    @DisplayName("Уведомление с типом 12 для vmcl = 5")
    public void Access_2138_5 () throws IOException, SQLException, InterruptedException {
        Access_2138Method("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms");
    }

    @Test
    @DisplayName("Уведомление с типом 12 для vmcl = 99")
    public void Access_2138_99 () throws IOException, SQLException, InterruptedException {
        Access_2138Method("SMS/SMS3.xml", "3", 99, 3, 1, 9, 18, 1, 57, 21, "vimis.remd_sent_result");
    }

    @Test
    @DisplayName("Уведомление с типом 12 для vmcl = 99 региональный")
    public void Access_2138_99_regional () throws IOException, SQLException, InterruptedException {
        Access_2138Method("SMS/id=101-vmcl=99.txt", "101", 99, 3, 1, 9, 18, 1, 57, 21,
                "vimis.certificate_remd_sent_result");
    }

    @Step("Метод отправки смс: {0}, vmcl = {2}")
    public void Access_2138Method (String FileName, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms) throws IOException, InterruptedException, SQLException {

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);

        xml.XmlStart(FileName, Departmen, MO, PatientGuid, NameMO, Snils, 1);
        xml.JsonChange(DocType, String.valueOf(vmcl), "2", String.valueOf(docTypeVersion), String.valueOf(Role), String.valueOf(position), String.valueOf(speciality), String.valueOf(Role1), String.valueOf(position1), String.valueOf(speciality1));

        xml.changes.put("$.PatientGuid", PatientGuid);
        xml.changes.put("$.directionUid", ReadPropFile("DirectionGuid_2138"));
        String[] pathDelete = {};
        if (vmcl == 99) {
            // Удаляем (если нужно) параметры
            pathDelete = new String[]{"$.VMCL[0].triggerPoint", "$.VMCL[0].docTypeVersion"};
        }
        // Меняем параметры в нужном файле
        String modifiedJson = JsonMethod("SMS/Body/body.json", xml.changes, true, pathDelete);

        System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
        Api(HostAddress + "/api/smd", "post", null, null, modifiedJson, 200, true);

        sql.StartConnection("Select * from " + sms + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            TransferId = sql.resultSet.getString("transfer_id");
            System.out.println(sql.value);
        }

        Thread.sleep(2500);

        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(12, TransferId, vmcl);
        System.out.println(TEXT);
        System.out.println(xml.uuid);
        Assertions.assertTrue(TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                "Оповещение для vmcl = " + vmcl + " с типом 12 не добавилось");
        Assertions.assertTrue(TEXT.contains("\"TransferId\":\"" + TransferId + "\""),
                "Оповещение для vmcl = " + vmcl + " с типом 12 не добавилось");
        Assertions.assertTrue(TEXT.contains("\"PatientGuid\":\"" + PatientGuid + "\""),
                "Оповещение для vmcl = " + vmcl + " с типом 12 не добавилось");

        /** Проверка по заявке (4131) */
        Assertions.assertTrue(TEXT.contains("\"DirectionGuid\":\"" + ReadPropFile("DirectionGuid_2138") + "\""), "DirectionGuid для vmcl = " + vmcl + " с типом 12 не добавилось");

        /** Проверка по заявке (4307) */
        Assertions.assertTrue(TEXT.contains("\"DirectionReqMoOid\":\"" + POidMoRequest + "\""), "DirectionReqMoOid для vmcl = " + vmcl + " с типом 12 не добавилось");

        /** Проверка по заявке (3403) */
        Assertions.assertTrue(TEXT.contains("\"MoOid\":\"" + MO + "\""), "MoOid для vmcl = " + vmcl + " с типом 12 не добавилось");
        if (vmcl != 99) {
            Assertions.assertTrue(TEXT.contains("\"DocType\":\"SMSV3\""),
                    "DocType для vmcl = " + vmcl + " с типом 12 не добавилось");
        } else {
            if (FileName.equals("SMS/id=101-vmcl=99.txt")) {
                Assertions.assertTrue(TEXT.contains("\"DocType\":\"101\""),
                        "DocType для vmcl = " + vmcl + " с типом 12 не добавилось");
            } else {
                Assertions.assertTrue(TEXT.contains("\"DocType\":\"75\""),
                        "DocType для vmcl = " + vmcl + " с типом 12 не добавилось");
            }
        }
    }
}
