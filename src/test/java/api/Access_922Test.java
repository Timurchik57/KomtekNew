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
@Feature("Формирование даты создания документа")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Основные")
public class Access_922Test extends BaseAPI {
    XML xml;
    SQL sql;
    public static String value_922_Vmcl;

    @Issue(value = "TEL-922")
    @Link(name = "ТМС-1271", url = "https://team-1okm.testit.software/projects/5/tests/1271?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование даты создания документа vmcl=1")
    @Description("Отправить СЭМД, у которого идёт отправка и в ВИМИС и РЭМД, указать добавленному СЭМД статус 1 в documentlogs/cvdlogs/akineologs/preventionlogs, перейти в таблицу vimis.remd_onko_sent_result/vimis.remd_prevention_sent_result/vimis.remd_akineo_sent_result/vimis.remd_cvd_sent_result и проверить, что в колонке created_datetime указана валидная дата - \"2021-10-22 13:54:18.321\"")
    public void Access_922Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_922Method(
                "SMS/id=15-vmcl=99.xml", "15", 1, 3, "vimis.sms", "vimis.documentlogs", 6, 4, 18, 1, 57, 21,
                "value_922_Vmcl_1"
        );
    }

    @Test
    @DisplayName("Формирование даты создания документа vmcl=2")
    public void Access_922Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_922Method(
                "SMS/id=15-vmcl=99.xml", "15", 2, 3, "vimis.preventionsms", "vimis.preventionlogs", 6, 4, 18, 1, 57, 21,
                "value_922_Vmcl_2"
        );
    }

    @Test
    @DisplayName("Формирование даты создания документа vmcl=3")
    public void Access_922Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_922Method(
                "SMS/id=15-vmcl=99.xml", "15", 3, 2, "vimis.akineosms", "vimis.akineologs", 6, 4, 18, 1, 57, 21,
                "value_922_Vmcl_3"
        );
    }

    @Test
    @DisplayName("Формирование даты создания документа vmcl=4")
    public void Access_922Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_922Method(
                "SMS/id=15-vmcl=99.xml", "15", 4, 2, "vimis.cvdsms", "vimis.cvdlogs", 6, 4, 18, 1, 57, 21,
                "value_922_Vmcl_4"
        );
    }

    @Test
    @DisplayName("Формирование даты создания документа vmcl=5")
    public void Access_922Vmcl_5() throws IOException, SQLException, InterruptedException {
        Access_922Method(
                "SMS/id=15-vmcl=99.xml", "15", 5, 3, "vimis.infectionsms", "vimis.infectionlogs", 6, 4, 18, 1, 57, 21,
                "value_922_Vmcl_5"
        );
    }

    @Step("Проверяем добавление данный фоновым сервисом КРЭМД")
    public void Access_922MethodAfter(String remd, Integer Vmcl,
            String NameProp) throws IOException, SQLException {
        sql = new SQL();
        xml = new XML();
        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            if (Vmcl == 1) {
                System.out.println("Проверка добавления значения по заявке 922");
                System.out.println(props.getProperty("" + NameProp + ""));
                sql.SQL("Select count(id) from " + remd + " where sms_id = '" + props.getProperty(
                        "" + NameProp + "") + "';");
                sql.StartConnection(
                        "Select * from " + remd + " where sms_id = '" + props.getProperty("" + NameProp + "") + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("created_datetime");
                    System.out.println(sql.value);
                    Assertions.assertNotNull(0, sql.value);
                }
            }
            System.out.println("Значение created_datetime по заявке 922 добавились в таблицу " + remd + "");
        }
    }

    public void Access_922Method(String FileName, String Doctype, Integer Vmlc, Integer docTypeVersion, String sms,
            String logs,
            Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1,
            String NameProp) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        xml = new XML();

        xml.ApiSmd(FileName, Doctype, Vmlc, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);
        sql.StartConnection(
                "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            value_922_Vmcl = sql.resultSet.getString("id");
            System.out.println(value_922_Vmcl);
        }
        CollbekVimis("" + xml.uuid + "", "1", "Проверка 922", sms, Vmlc);
        InputPropFile(NameProp, value_922_Vmcl);
        Thread.sleep(1500);
    }
}
