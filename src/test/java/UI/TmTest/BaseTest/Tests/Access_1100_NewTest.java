package UI.TmTest.BaseTest.Tests;

import Base.*;
import UI.TmTest.BaseTest.BaseNew;
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
@Feature("Проверка отправки документа в РЭМД с отправкой только в ВИМИС")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Вимис_статус")
@Tag("Фоновый_сервис")
@Tag("Основные")
@Tag("Новый_контур")
public class Access_1100_NewTest extends BaseNew {
    SQL sql;
    String value_1100;

    @Issue(value = "TEL-1100")
    @Link(name = "ТМС-1427", url = "https://team-1okm.testit.software/projects/5/tests/1427?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС vmcl=1")
    @Description("Отправляем документ в ВИМИС, добавляем статус в Logs. Далее отправляем другой документ в ВИМИС и РЭМД, добавляем статус в Logs. После прохождения тестов смотрим, что документ, который только в ВИМИС не добавился, а документ в ВИМИС и РЭМД добавился в таблицы vimis.remd_onko_sent_result/ vimis.remd_cvd_sent_result/ vimis.remd_akineo_sent_result/ vimis.remd_prevention_sent_result")
    public void Access_1100Vmcl_1 () throws IOException, SQLException, InterruptedException {
        Access_1100Method("SMS/SMS2-id=42.xml", "42", 1, true, 3, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС vmcl=2")
    public void Access_1100Vmcl_2 () throws IOException, SQLException, InterruptedException {
        Access_1100Method("SMS/SMS2-id=42.xml", "42", 2, true, 3, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС vmcl=3")
    public void Access_1100Vmcl_3 () throws IOException, SQLException, InterruptedException {
        Access_1100Method("SMS/SMS2-id=42.xml", "42", 3, true, 2, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС vmcl=4")
    public void Access_1100Vmcl_4 () throws IOException, SQLException, InterruptedException {
        Access_1100Method("SMS/SMS2-id=42.xml", "42", 4, true, 2, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС vmcl=5")
    public void Access_1100Vmcl_5 () throws IOException, SQLException, InterruptedException {
        Access_1100Method("SMS/SMS2-id=42.xml", "42", 5, true, 3, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой в ВИМИС и РЭМД vmcl=1")
    public void Access_1100Vmcl_1Sms_3 () throws IOException, SQLException, InterruptedException {
        Access_1100Method("SMS/SMS3.xml", "3", 1, true, 3, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой в ВИМИС и РЭМД vmcl=2")
    public void Access_1100Vmcl_2Sms_3 () throws IOException, SQLException, InterruptedException {
        Access_1100Method("SMS/SMS3.xml", "3", 2, true, 3, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой в ВИМИС и РЭМД vmcl=3")
    public void Access_1100Vmcl_3Sms_3 () throws IOException, SQLException, InterruptedException {
        Access_1100Method("SMS/SMS3.xml", "3", 3, true, 3, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой в ВИМИС и РЭМД vmcl=4")
    public void Access_1100Vmcl_4Sms_3 () throws IOException, SQLException, InterruptedException {
        Access_1100Method("SMS/SMS3.xml", "3", 4, true, 3, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой в ВИМИС и РЭМД vmcl=5")
    public void Access_1100Vmcl_5Sms_3 () throws IOException, SQLException, InterruptedException {
        Access_1100Method("SMS/SMS3.xml", "3", 5, true, 3, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой в РЭМД vmcl=99")
    public void Access_1100Vmcl_99Sms_3 () throws IOException, SQLException, InterruptedException {
        Access_1100Method("SMS/SMS3.xml", "3", 99, true, 3, 1, 9, 18, 1, 57, 21);
    }

    @Step("Проверка, что документы, у которых отправка в ВИМИС не добавились в РЭМД")
    public void Access_1100AfterMethod (String NameProp, String sms, boolean remd) throws IOException, SQLException {

        sql = new SQL();
        xml = new XML();

        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        System.out.println("Проверка добавления значения по заявке 1100");
        if (remd) {
            if (NameProp.equals("value_1100_remd_99")) {
                sql.SQL("Select count(id) from " + sms + " where id = '" + props.getProperty(
                        "" + NameProp + "") + "';");

                sql.StartConnection("Select * from " + sms + " where id = '" + props.getProperty(
                        "" + NameProp + "") + "';");
            } else {
                sql.SQL("Select count(sms_id) from " + sms + " where sms_id = '" + props.getProperty(
                        "" + NameProp + "") + "';");
                sql.StartConnection("Select * from " + sms + " where sms_id = '" + props.getProperty(
                        "" + NameProp + "") + "';");
            }
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
            }
            Assertions.assertEquals(sql.value, "error", "Крэмд должен выставить статус = error");
        } else {
            sql.NotSQL("Select count(sms_id) from " + sms + " where sms_id = '" + props.getProperty(
                    "" + NameProp + "") + "';");
        }
    }

    public void Access_1100Method (String FileName, String DocType, Integer vmcl, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        TableVmcl(vmcl);

        System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(FileName, DocType, vmcl, 1, RanLoc, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);
        sql.StartConnection(
                "Select * from " + smsBase + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            value_1100 = sql.resultSet.getString("id");
            System.out.println(value_1100);
        }

        System.out.println("Устанавливаем status = 1 в " + logsBase + "");
        if (vmcl != 99) {
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1100", smsBase, vmcl);
        }
        // Для отправки в ВИМИС
        if (vmcl == 1 & FileName != "SMS/SMS3.xml") {
            InputPropFile("value_1100_vmcl_1", value_1100);
        }
        if (vmcl == 2 & FileName != "SMS/SMS3.xml") {
            InputPropFile("value_1100_vmcl_2", value_1100);
        }
        if (vmcl == 3 & FileName != "SMS/SMS3.xml") {
            InputPropFile("value_1100_vmcl_3", value_1100);
        }
        if (vmcl == 4 & FileName != "SMS/SMS3.xml") {
            InputPropFile("value_1100_vmcl_4", value_1100);
        }
        if (vmcl == 5 & FileName != "SMS/SMS3.xml") {
            InputPropFile("value_1100_vmcl_5", value_1100);
        }
        // Для отправки в РЭМД
        if (vmcl == 1 & FileName == "SMS/SMS3.xml") {
            InputPropFile("value_1100_remd_1", value_1100);
        }
        if (vmcl == 2 & FileName == "SMS/SMS3.xml") {
            InputPropFile("value_1100_remd_2", value_1100);
        }
        if (vmcl == 3 & FileName == "SMS/SMS3.xml") {
            InputPropFile("value_1100_remd_3", value_1100);
        }
        if (vmcl == 4 & FileName == "SMS/SMS3.xml") {
            InputPropFile("value_1100_remd_4", value_1100);
        }
        if (vmcl == 5 & FileName == "SMS/SMS3.xml") {
            InputPropFile("value_1100_remd_5", value_1100);
        }
        if (vmcl == 99 & FileName == "SMS/SMS3.xml") {
            InputPropFile("value_1100_remd_99", value_1100);
        }
    }
}
