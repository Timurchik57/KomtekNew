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
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Хранение информации по пациентам ССЗ по скорой помощи")
@Tag("Проверка_БД")
@Tag("api/smd")
public class Access_937Test extends BaseAPI {
    XML xml;
    SQL sql;
    public static String value_937ID_107;
    public static String value_937ID_116;

    @Issue(value = "TEL-937")
    @Link(name = "ТМС-1285", url = "https://team-1okm.testit.software/projects/5/tests/1285?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Хранение информации по пациентам ССЗ по скорой помощи id=107")
    @Description("Отправляем смс, добавляем status 1 в logs и проверяем, что добавилась в таблицу vimis.cvd_call_an_ambulance")
    public void Access_937ID_107() throws IOException, SQLException, InterruptedException {
        Access_937Method("SMS/SMS18-id=107.xml", "107", 1, 18, 18, 1, 57, 21, value_937ID_107, "value_937ID_107");
    }

    @Issue(value = "TEL-937")
    @Link(name = "ТМС-1285", url = "https://team-1okm.testit.software/projects/5/tests/1285?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Хранение информации по пациентам ССЗ по скорой помощи id=116")
    @Description("Отправляем смс, добавляем status 1 в logs и проверяем, что добавилась в таблицу vimis.cvd_call_an_ambulance")
    public void Access_937ID_116() throws IOException, SQLException, InterruptedException {
        Access_937Method("SMS/SMS18-id=116.xml", "116", 6, 4, 18, 1, 57, 21, value_937ID_116, "value_937ID_116");
    }


    public void Access_937After(String NameFile) throws SQLException, IOException {
        sql = new SQL();
        xml = new XML();
        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        System.out.println("Проверка добавления значения по заявке 937");
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            sql.SQL("Select count(sms_id) from vimis.cvd_call_an_ambulance where sms_id = '" + props.getProperty(
                    "" + NameFile + "") + "';");
            sql.StartConnection("Select * from vimis.cvd_call_an_ambulance where sms_id = '" + props.getProperty(
                    "" + NameFile + "") + "';");
            while (sql.resultSet.next()) {
                String sms_id = sql.resultSet.getString("sms_id");
                String call_date = sql.resultSet.getString("call_date");
                String mo_arrive_date = sql.resultSet.getString("mo_arrive_date");
                String diagnos = sql.resultSet.getString("diagnos");
                String mo_arrived = sql.resultSet.getString("mo_arrived");
                String reason = sql.resultSet.getString("reason");
                Assertions.assertEquals(sms_id, "" + props.getProperty("" + NameFile + "") + "");
                if (NameFile == "value_937ID_107") {
                    Assertions.assertEquals(call_date, "2021-02-26 16:00:00");
                    Assertions.assertEquals(mo_arrive_date, "2021-02-26 20:00:00");
                }
                if (NameFile == "value_937ID_116") {
                    Assertions.assertEquals(call_date, "2020-09-25 17:00:00");
                    Assertions.assertEquals(mo_arrive_date, "2020-09-25 20:00:00");
                }
                Assertions.assertEquals(diagnos, "I21.0");
                Assertions.assertEquals(mo_arrived, "1.2.643.5.1.13.13.12.2.77.7994");
                Assertions.assertEquals(reason, "1");

            }
            System.out.println("Значения по заявке 937 добавились в таблицу vimis.cvd_call_an_ambulance");
        }
    }

    public void Access_937Method(String FileName, String DocType, Integer Role, Integer position, Integer speciality, Integer Role1,
            Integer position1, Integer speciality1, String Value, String NameProp) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        xml = new XML();

        System.out.println("Проверка по заявке 937 с смс = " + DocType + "");
        xml.ApiSmd(FileName, DocType, 4, 1, true, 2, Role, position, speciality, Role1, position1, speciality1);
        sql.StartConnection(
                "Select * from vimis.cvdsms where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            Value = sql.resultSet.getString("id");
            System.out.println(Value);
        }
        sql.UpdateConnection(
                "insert into vimis.cvdlogs (create_time, status, description, sms_id, msg_id) values ('" + Date + " 00:00:00.888 +0500', 1, 'ок', '" + Value + "', '" + UUID.randomUUID() + "')");
        InputPropFile(NameProp, Value);
    }
}
