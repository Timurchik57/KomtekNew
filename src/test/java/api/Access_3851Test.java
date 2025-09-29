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

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Тесты API")
@Feature("Регистрация СЭМД с направлениями")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Прикрепление_Документов")
@Tag("Дополнительные_параметры")
@Tag("Основные")
public class Access_3851Test extends BaseAPI {

    String directionuuid;
    String directionid;

    @Issue(value = "TEL-3851")
    @Issue(value = "TEL-4188")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправляем смс и проверяем прикрепления документа, vmcl = 1")
    @Description("Отправить запрос api/smd с directionUid полем после проверяем БД telmed.directions на связь направления с этим документом, также смотрим смену статуса на 21")
    public void Access_3692_1() throws IOException, SQLException, InterruptedException {
        AddSEMD(1);
    }

    @Test
    @DisplayName("Отправляем смс и проверяем прикрепления документа, vmcl = 2")
    public void Access_3692_2() throws IOException, SQLException, InterruptedException {
        AddSEMD(2);
    }

    @Test
    @DisplayName("Отправляем смс и проверяем прикрепления документа, vmcl = 3")
    public void Access_3692_3() throws IOException, SQLException, InterruptedException {
        AddSEMD(3);
    }

    @Test
    @DisplayName("Отправляем смс и проверяем прикрепления документа, vmcl = 4")
    public void Access_3692_4() throws IOException, SQLException, InterruptedException {
        AddSEMD(4);
    }

    @Test
    @DisplayName("Отправляем смс и проверяем прикрепления документа, vmcl = 5")
    public void Access_3692_5() throws IOException, SQLException, InterruptedException {
        AddSEMD(5);
    }

    @Test
    @DisplayName("Отправляем смс и проверяем прикрепления документа, vmcl = 99")
    public void Access_3692_99() throws IOException, SQLException, InterruptedException {
        AddSEMD(99);
    }

    @Test
    @DisplayName("Отправляем смс с несуществующим directionUid и проверяем валидную ошибку")
    public void Access_3692_() throws IOException, SQLException, InterruptedException {
        directionuuid = "f4a0c7c8-d951-4fa9-9f1f-c02716f85665";

        for (int i = 1; i < 7; i++) {
            if (i == 6) {
                i = 99;
            }
            TableVmcl(i);
            xml.ReplacementWordInFile("SMS/SMS3.xml", "3", i, 2, true, 3, 1, 4, 18, 1, 57, 21);
            xml.addElementsFirst.put("$.directionUid", directionuuid);
            xml.ReplacementWordInFileBack("SMS/SMS3.xml");

            // Меняем в нужном файле параметры
            String modifiedJson = JsonMethod("src/test/resources/ignored/JsonBody.json", xml.changes, false, null);
            Api(HostAddress + "/api/smd", "post", null, null, modifiedJson, 200, true);
            Assertions.assertTrue(Response.getString("result[0].message").contains("Направление с идентификатором f4a0c7c8-d951-4fa9-9f1f-c02716f85665 не найдено в системе."));

            sql.StartConnection("Select * from "+smsBase+" where local_uid = '"+xml.uuid+"';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("direction_id");
            }

            Assertions.assertEquals(sql.value, null, "К СЭМД не должно прикрепиться направление");
        }
    }

    @Step("Метод отправки Сэмд с новым параметром directionUid и проверка БД")
    void AddSEMD (Integer vmcl) throws SQLException, IOException, InterruptedException {
        sql.StartConnection("Select * from telmed.directions where status != '21' order by id desc limit 1;");
        while (sql.resultSet.next()) {
            directionuuid = sql.resultSet.getString("directionguid");
            directionid = sql.resultSet.getString("id");
        }

        xml.ReplacementWordInFile("SMS/SMS3.xml", "3", vmcl, 2, true, 3, 1, 4, 18, 1, 57, 21);
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        xml.addElementsFirst.put("$.directionUid", directionuuid);

        // Меняем в нужном файле параметры
        String modifiedJson = JsonMethod("src/test/resources/ignored/JsonBody.json", xml.changes, false, null);
        Api(HostAddress + "/api/smd", "post", null, null, modifiedJson, 200, true);

        CheckBD(vmcl);
    }

    @Step("Метод проверки бд после отправки смс с параметром directionUid")
    void CheckBD (Integer vmcl) throws SQLException {
        TableVmcl(vmcl);
        sql.StartConnection("Select * from "+smsBase+" where local_uid = '"+xml.uuid+"';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("direction_id");
        }
        System.out.println(sql.value);

        Assertions.assertEquals(sql.value, directionid, "К СЭМД не прикрпилось направление");

        String dateexecute = null;
        sql.StartConnection("Select * from telmed.directions where id = '"+directionid+"';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("status");
            dateexecute = sql.resultSet.getString("dateexecute");
        }
        System.out.println(dateexecute);

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Date = formatForDateNow.format(date);

        Assertions.assertEquals(sql.value, "21", "У направления статус должен смениться на 21");
        // Добавляется текущая дата  при статусе 21(только методом api/smd) - 4188
        Assertions.assertEquals(dateexecute.substring(0, 10), Date, "У направления должна заполнится колонка dateexecute, текущей датой - " + Date);
    }
}
