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

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Хранение информации по госпитализациям ССЗ")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Основные")
public class Access_927Test extends BaseAPI {
    XML xml;
    SQL sql;
    public static Integer value_927;

    @Issue(value = "TEL-927")
    @Link(name = "ТМС-1276", url = "https://team-1okm.testit.software/projects/5/tests/1276?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Хранение информации по госпитализациям ССЗ")
    @Description("Отправляем смс c vmcl = 4, добавляем status 1 в logs и проверяем, что добавилась в таблицу vimis.smsv8_discharging_cvd")
    public void Access_927Id_4() throws IOException, SQLException, InterruptedException {
        sql = new SQL();
        xml = new XML();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            xml.ApiSmd("SMS/SMS8.xml", "3", 4, 1, true, 3, 1, 4, 18, 1, 57, 21);
            sql.StartConnection(
                    "Select * from vimis.cvdsms where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value_927 = Integer.valueOf(sql.resultSet.getString("id"));
                System.out.println(value_927);
            }
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 927", "vimis.cvdsms", 4);
            InputPropFile("value_927", String.valueOf(value_927));
        }
    }

    public void Access_927Before(String NameProp) throws IOException, SQLException {
        sql = new SQL();
        xml = new XML();
        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        System.out.println("Проверка добавления значения по заявке 927");
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println(props.getProperty("" + NameProp + ""));
            sql.SQL("Select count(sms_id) from vimis.smsv8_discharging_cvd where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
            sql.StartConnection("Select * from vimis.smsv8_discharging_cvd where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
            while (sql.resultSet.next()) {
                String sms_id = sql.resultSet.getString("sms_id");
                String hospitalization_date = sql.resultSet.getString("hospitalization_date");
                String discharging_date = sql.resultSet.getString("discharging_date");
                String outcome = sql.resultSet.getString("outcome");
                Assertions.assertEquals(sms_id, "" + props.getProperty("" + NameProp + "") + "");
                Assertions.assertEquals(hospitalization_date, "2022-06-28 11:24:00");
                Assertions.assertEquals(discharging_date, "2022-07-08 09:07:00");
                Assertions.assertEquals(outcome, "1");

            }
            System.out.println("Значение по заявке 927 добавилось в таблицу vimis.smsv8_discharging_cvd");
        }
    }
}
