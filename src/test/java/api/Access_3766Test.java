package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Тесты API")
@Feature("Прикрепление документов")
@Tag("Проверка_БД")
@Tag("Основные")
@Tag("/api/direction")
@Tag("Прикрепление_Документов")
public class Access_3766Test extends BaseAPI {

    public String Direction_guid;

    @Issue(value = "TEL-3766")
    @Issue(value = "TEL-3767")
    @Issue(value = "TEL-3782")
    @Issue(value = "TEL-3783")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Order(1)
    @DisplayName("Отправляем смс и проверяем метод прикрепления документа")
    @Description("Отправить запрос api/smd после используем метод /api/direction/{directionGuid}/documents и проверяем в бд telmed.direction_documents")
    public void Access_3692() throws IOException, SQLException, InterruptedException {
        AddXml("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21, false);
        AddXml("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21, false);
        AddXml("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21, false);
        AddXml("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21, false);
        AddXml("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21, false);
        AddXml("SMS/SMS3.xml", "3", 99, 3, 1, 9, 18, 1, 57, 21, false);
    }

    @Test
    @Order(2)
    @DisplayName("Прикрепление Документа")
    public void Access_3692_POST() throws IOException, SQLException, InterruptedException {
        Document("1", ReadPropFile("localUid_3166_1"));
        Document("2", ReadPropFile("localUid_3166_2"));
        Document("3", ReadPropFile("localUid_3166_3"));
        Document("4", ReadPropFile("localUid_3166_4"));
        Document("5", ReadPropFile("localUid_3166_5"));
        Document("99", ReadPropFile("localUid_3166_99"));
    }

    @Test
    @Order(3)
    @DisplayName("Проверяем валидные ошибки при прикреплении документа")
    public void Access_3692_POST_Error() throws IOException, SQLException, InterruptedException {

        System.out.println("\n 1 Проверка - Указываем LocalUid, который не соответствует vmcl");
        sql.StartConnection("Select * from telmed.directions order by id desc limit 1");
        while (sql.resultSet.next()) {
            Direction_guid = sql.resultSet.getString("directionguid");
        }
        Api(HostAddress + "/api/direction/" + Direction_guid + "/documents", "post", null, null,
                "[\n" +
                        "  {\n" +
                        "    \"LocalUid\": \"" + ReadPropFile("localUid_3166_1") + "\",\n" +
                        "    \"VMCL\": 2\n" +
                        "  }\n" +
                        "]"
                , 200, true);
        Assertions.assertEquals(Response.getString("ErrorMessage"),
                "Указанный документ " + ReadPropFile("localUid_3166_1") + " не найден в ВИМИС/РЭМД", "Должна появится ошибка - Указанный документ не найден в ВИМИС/РЭМД");

        System.out.println("\n 2 Проверка - Указываем несуществующий LocalUid");
        sql.StartConnection("Select * from telmed.directions order by id desc limit 1");
        while (sql.resultSet.next()) {
            Direction_guid = sql.resultSet.getString("directionguid");
        }
        Api(HostAddress + "/api/direction/" + Direction_guid + "/documents", "post", null, null,
                "[\n" +
                        "  {\n" +
                        "    \"LocalUid\": \"7eca7459-af84-48c2-9fb9-b8bc2936b444\",\n" +
                        "    \"VMCL\": 1\n" +
                        "  }\n" +
                        "]"
                , 200, true);
        Assertions.assertEquals(Response.getString("ErrorMessage"),
                "Указанный документ 7eca7459-af84-48c2-9fb9-b8bc2936b444 не найден в ВИМИС/РЭМД", "Должна появится ошибка - Указанный документ не найден в ВИМИС/РЭМД");

        System.out.println("\n 3 Проверка - Прикрепляем сразу 2 документа");
        AddXml("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21, false);
        AddXml("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21, false);
        Api(HostAddress + "/api/direction/" + Direction_guid + "/documents", "post", null, null,
                "[\n" +
                        "  {\n" +
                        "    \"LocalUid\": \""+ReadPropFile("localUid_3166_1")+"\",\n" +
                        "    \"VMCL\": 1\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"LocalUid\": \""+ReadPropFile("localUid_3166_2")+"\",\n" +
                        "    \"VMCL\": 2\n" +
                        "  }\n" +
                        "]"
                , 200, true);
        CheckSql("1", ReadPropFile("localUid_3166_1"));
        DocumentGet(Direction_guid, "0", ReadPropFile("localUid_3166_1"), "1");
        DocumentDelete(Direction_guid, ReadPropFile("localUid_3166_1"));

        CheckSql("2", ReadPropFile("localUid_3166_2"));
        DocumentGet(Direction_guid, "0", ReadPropFile("localUid_3166_2"), "2");
        DocumentDelete(Direction_guid, ReadPropFile("localUid_3166_2"));
    }

    @Test
    @Order(4)
    @DisplayName("Отправляем смс В РЭМД")
    public void Access_3692_Remd() throws IOException, SQLException, InterruptedException {
        AddXml("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21, true);
        AddXml("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21, true);
        AddXml("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21, true);
        AddXml("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21, true);
        AddXml("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21, true);
        AddXml("SMS/SMS3.xml", "3", 99, 3, 1, 9, 18, 1, 57, 21, true);
    }

    @Test
    @Order(5)
    @DisplayName("Прикрепление Документа")
    public void Access_3692_POST_Remd() throws IOException, SQLException, InterruptedException {
        Document("1", ReadPropFile("localUid_3166_1"));
        Document("2", ReadPropFile("localUid_3166_2"));
        Document("3", ReadPropFile("localUid_3166_3"));
        Document("4", ReadPropFile("localUid_3166_4"));
        Document("5", ReadPropFile("localUid_3166_5"));
        Document("99", ReadPropFile("localUid_3166_99"));
    }

    @Step("Метод для отправки СМС в ВИМИС")
    public void AddXml(String FileName, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, boolean REMD) throws IOException, SQLException, InterruptedException {
        TableVmcl(vmcl);

        xml.ApiSmd(FileName, DocType, vmcl, 2, true, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);
        InputPropFile("localUid_3166_" + vmcl, String.valueOf(xml.uuid));
        xml.ReplacementWordInFileBack(FileName);
        if (REMD) {
            if (vmcl != 99) {
                CollbekVimis("" + xml.uuid + "", "0", "Проверка 3766", smsBase, vmcl);
            }
        }
    }

    @Step("Метод прикрепления документа")
    public void Document(String vmcl, String localUuid) throws IOException, SQLException, InterruptedException {
        sql.StartConnection("Select * from telmed.directions order by id desc limit 1");
        while (sql.resultSet.next()) {
            Direction_guid = sql.resultSet.getString("directionguid");
        }
        Api(HostAddress + "/api/direction/" + Direction_guid + "/documents", "post", null, null,
                "[\n" +
                        "  {\n" +
                        "    \"LocalUid\": \"" + localUuid+ "\",\n" +
                        "    \"VMCL\": " + vmcl + "\n" +
                        "  }\n" +
                        "]"
                , 200, true);
        CheckSql(vmcl, localUuid);
        DocumentGet(Direction_guid, "0", localUuid, vmcl);
        DocumentDelete(Direction_guid, localUuid);

    }

    @Step("Метод поиска прикреплённого документа")
    public void DocumentGet(String directionguid, String result, String localuid, String vmcl) throws IOException {
        Api(HostAddress + "/api/direction/" + directionguid + "/documents", "get", null, null,
                "", 200, true);
        Assertions.assertEquals(Response.getString("Result["+result+"].LocalUid"), localuid, "LocalUid не совпадает");
        Assertions.assertEquals(Response.getString("Result["+result+"].Vmcl"), vmcl, "vmcl не совпадает");
    }

    @Step("Метод удаления прикреплённого документа")
    public void DocumentDelete(String directionguid, String localuid) throws IOException, SQLException {
        Api(HostAddress + "/api/direction/" + directionguid + "/documents/" + localuid, "delete", null, null,
                "", 200, true);

        sql.NotSQL("Select count (*) from telmed.direction_documents where local_uid = '"+localuid+"' and direction_guid = '"+directionguid+"';");
    }

    @Step("Метод проверки БД прикреплённого документа")
    public void CheckSql (String vmcl, String localUuid) throws SQLException, IOException {
        String direction_guid = null;
        String local_uid = null;
        String document_name = null;
        String oid_mo = null;
        String date_created = null;
        String vmclBd = null;

        sql.StartConnection("Select * from telmed.direction_documents where local_uid = '"+localUuid+"';");
        while (sql.resultSet.next()) {
            direction_guid = sql.resultSet.getString("direction_guid");
            local_uid = sql.resultSet.getString("local_uid");
            document_name = sql.resultSet.getString("document_name");
            oid_mo = sql.resultSet.getString("oid_mo");
            date_created = sql.resultSet.getString("date_created");
            vmclBd = sql.resultSet.getString("vmcl");
        }

        softAssert.assertEquals(direction_guid, Direction_guid, "direction_guid не совпадает");
        softAssert.assertEquals(local_uid, ReadPropFile(
                "localUid_3166_" + vmcl + ""), "local_uid не совпадает");
        if (vmcl != "99") {
            softAssert.assertEquals(document_name, "Лабораторное исследование", "document_name не совпадает");
        } else {
            softAssert.assertEquals(document_name, "Протокол лабораторного исследования (CDA) Редакция 4", "document_name не совпадает");
        }
        softAssert.assertEquals(oid_mo, MO, "oid_mo не совпадает");
        softAssert.assertEquals(date_created.substring(0, 10), "2002-01-23", "date_created не совпадает");
        softAssert.assertEquals(vmclBd, vmcl, "vmcl не совпадает");
        softAssert.assertAll();
    }
}
