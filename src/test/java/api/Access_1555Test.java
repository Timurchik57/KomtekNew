package api;

import Base.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Проверка добавления записи после успешного принятия документа ФРЭМД")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
public class Access_1555Test extends BaseAPI {
    public String URLRemd;
    public String LocalUid;
    public static String value_1555_vmcl_1;
    public static String value_1555_vmcl_2;
    public static String value_1555_vmcl_3;
    public static String value_1555_vmcl_4;
    public static String value_1555_vmcl_5;

    @Issue(value = "TEL-1555")
    @Link(name = "ТМС-1665", url = "https://team-1okm.testit.software/projects/5/tests/1665?isolatedSection=f07b5d61-7df7-4e90-9661-3fd312065912")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка добавления записи после успешного принятия документа ФРЭМД vmcl = 1")
    @Description("Отправляем смс в РЭМД, добавляем  fremd_status = '1'. После отправяем смс в ВИМИС и РЭМД с таким же Local_uid, добавляем статус 1 в logs и после проверяем, что смс не добавилась в таблицы remd")
    public void Access_1555Vmcl_1() throws IOException, SQLException, InterruptedException {
        Access_1555Method(
                "SMS/SMS3.xml", "3", 99, 1, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.documentlogs",
                "vimis.remd_sent_result", value_1555_vmcl_1
        );
    }

    @Test
    @DisplayName("Проверка добавления записи после успешного принятия документа ФРЭМД для vmcl = 2")
    public void Access_1555Vmcl_2() throws IOException, SQLException, InterruptedException {
        Access_1555Method(
                "SMS/SMS3.xml", "3", 99, 2, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms",
                "vimis.preventionlogs", "vimis.remd_sent_result", value_1555_vmcl_2
        );
    }

    @Test
    @DisplayName("Проверка добавления записи после успешного принятия документа ФРЭМД для vmcl = 3")
    public void Access_1555Vmcl_3() throws IOException, SQLException, InterruptedException {
        Access_1555Method(
                "SMS/SMS3.xml", "3", 99, 3, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.akineologs",
                "vimis.remd_sent_result", value_1555_vmcl_3
        );
    }

    @Test
    @DisplayName("Проверка добавления записи после успешного принятия документа ФРЭМД для vmcl = 4")
    public void Access_1555Vmcl_4() throws IOException, SQLException, InterruptedException {
        Access_1555Method(
                "SMS/SMS3.xml", "3", 99, 4, 1, true, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.cvdlogs",
                "vimis.remd_sent_result", value_1555_vmcl_4
        );
    }

    @Test
    @DisplayName("Проверка добавления записи после успешного принятия документа ФРЭМД для vmcl = 5")
    public void Access_1555Vmcl_5() throws IOException, SQLException, InterruptedException {
        Access_1555Method(
                "SMS/SMS3.xml", "3", 99, 5, 1, true, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms",
                "vimis.infectionlogs",
                "vimis.remd_sent_result", value_1555_vmcl_5
        );
    }

    public void Access_1555Method(
            String FileName, String DocType, Integer vmcl, Integer vmcl1, Integer number, Boolean RanLoc,
            Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1,
            Integer position1, Integer speciality1, String sms, String logs, String remd, String value
    ) throws IOException, InterruptedException, SQLException {
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {

            System.out.println("Отправляем запрос смс 3 с vmlc = " + vmcl + "");
            xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);
            LocalUid = String.valueOf(xml.uuid);
            xml.ReplacementWordInFileBack(FileName);
            sql.UpdateConnection(
                    "update " + remd + " set fremd_status = '1' where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "'");

            System.out.println("Отправляем запрос смс 3 с vmlc = " + vmcl1 + "");
            xml.uuid = UUID.fromString(LocalUid);
            System.out.println(xml.uuid);
            xml.ApiSmd(FileName, DocType, vmcl1, 2, false, docTypeVersion, Role, position, speciality, Role1, position1,
                    speciality1);

            sql.StartConnection(
                    "Select * from " + sms + " where create_date > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value = sql.resultSet.getString("id");
                System.out.println(value);
            }
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1555", sms, vmcl1);

            if (vmcl1 == 1) {
                InputPropFile("value_1555_vmcl_1", value);
            }
            if (vmcl1 == 2) {
                InputPropFile("value_1555_vmcl_2", value);
            }
            if (vmcl1 == 3) {
                InputPropFile("value_1555_vmcl_3", value);
            }
            if (vmcl1 == 4) {
                InputPropFile("value_1555_vmcl_4", value);
            }
            if (vmcl1 == 5) {
                InputPropFile("value_1555_vmcl_5", value);
            }
        }
    }

    public void Access_1555AfterMethod(String NameProp, String remd) throws IOException, SQLException {
        sql = new SQL();
        xml = new XML();
        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();
        System.out.println("Проверка, что значения не добавились по заявке 1555");
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Select count(sms_id) from " + remd + " where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
            sql.NotSQL("Select count(sms_id) from " + remd + " where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
        }
    }

}
