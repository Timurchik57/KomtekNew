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

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Отправка документа 1 раз в КРЭМД")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
public class Access_1880Test extends BaseAPI {

    public String ID_1880;

    @Issue(value = "TEL-1880")
    @Link(name = "ТМС-1793", url = "https://team-1okm.testit.software/projects/5/tests/1793?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправить СЭМД - установить статус 1 в logs, дождаться смены статуса. Сменить статус колбэком КРЭМД, после проверить, что статус не смнился обратно фоновым сервисом")
    @Test
    @DisplayName("Отправка смс с vmcl = 1")
    public void Access_1880_1() throws IOException, SQLException, InterruptedException {
        Access_1880Method("SMS/SMS3.xml", "3", 1, 1, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.remd_onko_sent_result", "value_1880_1");
    }

    @Test
    @DisplayName("Отправка смс с vmcl = 2")
    public void Access_1880_2() throws IOException, SQLException, InterruptedException {
        Access_1880Method("SMS/SMS3.xml", "3", 2, 1, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.remd_prevention_sent_result", "value_1880_2");
    }

    @Test
    @DisplayName("Отправка смс с vmcl = 3")
    public void Access_1880_3() throws IOException, SQLException, InterruptedException {
        Access_1880Method("SMS/SMS3.xml", "3", 3, 1, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.remd_akineo_sent_result", "value_1880_3");
    }

    @Test
    @DisplayName("Отправка смс с vmcl = 4")
    public void Access_1880_4() throws IOException, SQLException, InterruptedException {
        Access_1880Method("SMS/SMS3.xml", "3", 4, 1, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.remd_cvd_sent_result", "value_1880_4");
    }

    @Test
    @DisplayName("Отправка смс с vmcl = 99")
    public void Access_1880_99() throws IOException, SQLException, InterruptedException {
        Access_1880Method("SMS/SMS3.xml", "3", 99, 1, 3, 1, 9, 18, 1, 57, 21, "", "vimis.remd_sent_result", "value_1880_99");
    }

    @Step("Отправляем смс = {1} c vmcl = {2} и используем коллбэк от ФВИМИС и КРЭМД")
    public void Access_1880Method(String File, String DocType, Integer vmcl, Integer number, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String remd, String value) throws IOException, InterruptedException, SQLException {
        xml.ApiSmd(File, DocType, vmcl, number, true, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);

        if (vmcl != 99) {
            sql.StartConnection("Select * from " + sms + " where local_uid = '" + xml.uuid + "'");
            while (sql.resultSet.next()) {
                ID_1880 = sql.resultSet.getString("id");
            }

            System.out.println("Отправляем колбек от ФВИМИС");
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1880", sms, vmcl);
            WaitStatusKremd(remd, "" + xml.uuid + "");
        } else {
            sql.StartConnection("Select * from " + remd + " where local_uid = '" + xml.uuid + "'");
            while (sql.resultSet.next()) {
                ID_1880 = sql.resultSet.getString("id");
            }
            WaitStatusKremd(remd, "" + xml.uuid + "");
        }

        System.out.println("Отправляем колбек от РЭМД");
        CollbekKremd("" + xml.uuid + "", "success", "Проверка 1880", remd);

        InputPropFile(value, ID_1880);
    }

    @Step("Проверяем статус смс в таблице {0}")
    public void Access_1880After(String remd, String NameProp) throws IOException, SQLException {

        sql = new SQL();
        xml = new XML();

        FileInputStream in = new FileInputStream("src/test/resources/ignored/my.properties");
        props = new Properties();
        props.load(in);
        in.close();

        if (remd == "vimis.remd_sent_result") {
            sql.StartConnection("Select * from " + remd + " where id = '" + props.getProperty("" + NameProp + "") + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
            }
        } else {
            sql.StartConnection("Select * from " + remd + " where sms_id = '" + props.getProperty("" + NameProp + "") + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
            }
        }
        System.out.println(sql.value);

        Assertions.assertEquals(sql.value, "success", "Смс сменила статус 2 раза фоновым сервисом");
    }
}
