package api.Access_1444;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
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
@Feature("Проверка добавленной информации по смс при отправке SmdToMinio =1")
@Tag("МИНИО")
@Tag("Проверка_БД")
@Tag("Проверка_info")
public class Access_1470 extends BaseAPI {
    public String SMSID;

    @Step("Проверяем таблицы info по отправленным смс с Local_Uid = {3} в таблице {2}")
    public void Access_1470Method(
            String sms, Integer vmcl, String info, String Local_Uid
    ) throws IOException, SQLException {

        FileInputStream in = new FileInputStream("src/test/java/api/Access_1444/1444.properties");
        props = new Properties();
        props.load(in);
        in.close();

        System.out.println(props.getProperty("" + Local_Uid + ""));
        sql.StartConnection("SELECT * FROM " + sms + "  where local_uid = '" + props.getProperty("" + Local_Uid + "") + "';");
        while (sql.resultSet.next()) {
            SMSID = sql.resultSet.getString("id");
            System.out.println(SMSID);
        }

        System.out.println("Находим запись по id в таблице " + info + "");
        sql.StartConnection("Select * from " + info + " where smsid = '" + SMSID + "'");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("version");
            Assertions.assertNotNull(sql.value, "version не заполнено");
            sql.value = sql.resultSet.getString("setid");
            Assertions.assertNotNull(sql.value, "setid не заполнено");
            sql.value = sql.resultSet.getString("documentid");
            Assertions.assertNotNull(sql.value, "documentid не заполнено");
        }
    }

    @Issue(value = "TEL-1470")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка добавленной информации по смс vmcl = 1 Вимис")
    @Description("Переходим в таблицы info и проверяем, что добавились значения при отправке смс с minio")
    public void Access_1470Vimis1() throws IOException, SQLException, InterruptedException {
        Access_1470Method("vimis.sms", 1, "vimis.additionalinfo", "SmsIdVimis1");
    }

    @Test
    @DisplayName("Проверка добавленной информации по смс vmcl = 2 Вимис")
    public void Access_1470Vimis2() throws IOException, SQLException, InterruptedException {
        Access_1470Method("vimis.preventionsms", 2, "vimis.preventionadditionalinfo", "SmsIdVimis2");
    }

    @Test
    @DisplayName("Проверка добавленной информации по смс vmcl = 3 Вимис")
    public void Access_1470Vimis3() throws IOException, SQLException, InterruptedException {
        Access_1470Method("vimis.akineosms", 3, "vimis.akineoadditionalinfo", "SmsIdVimis3");
    }

    @Test
    @DisplayName("Проверка добавленной информации по смс vmcl = 4 Вимис")
    public void Access_1470Vimis4() throws IOException, SQLException, InterruptedException {
        Access_1470Method("vimis.cvdsms", 4, "vimis.cvdadditionalinfo", "SmsIdVimis4");
    }

    @Test
    @DisplayName("Проверка добавленной информации по смс vmcl = 5 Вимис")
    public void Access_1470Vimis5() throws IOException, SQLException, InterruptedException {
        Access_1470Method("vimis.infectionsms", 5, "vimis.infectionadditionalinfo", "SmsIdVimis5");
    }

    @Test
    @DisplayName("Проверка добавленной информации по смс vmcl = 1 Вимис и Ремд")
    public void Access_1470VimisRemd1() throws IOException, SQLException, InterruptedException {
        Access_1470Method("vimis.sms", 1, "vimis.additionalinfo", "SmsIdVimisRemd1");
    }

    @Test
    @DisplayName("Проверка добавленной информации по смс vmcl = 2 Вимис Ремд")
    public void Access_1470VimisRemd2() throws IOException, SQLException, InterruptedException {
        Access_1470Method("vimis.preventionsms", 2, "vimis.preventionadditionalinfo", "SmsIdVimisRemd2");
    }

    @Test
    @DisplayName("Проверка добавленной информации по смс vmcl = 3 Вимис Ремд")
    public void Access_1470VimisRemd3() throws IOException, SQLException, InterruptedException {
        Access_1470Method("vimis.akineosms", 3, "vimis.akineoadditionalinfo", "SmsIdVimisRemd3");
    }

    @Test
    @DisplayName("Проверка добавленной информации по смс vmcl = 4 Вимис Ремд")
    public void Access_1470VimisRemd4() throws IOException, SQLException, InterruptedException {
        Access_1470Method("vimis.cvdsms", 4, "vimis.cvdadditionalinfo", "SmsIdVimisRemd4");
    }

    @Test
    @DisplayName("Проверка добавленной информации по смс vmcl = 5 Вимис Ремд")
    public void Access_1470VimisRemd5() throws IOException, SQLException, InterruptedException {
        Access_1470Method("vimis.infectionsms", 5, "vimis.infectionadditionalinfo", "SmsIdVimisRemd5");
    }

    @Test
    @DisplayName("Проверка добавленной информации по смс vmcl = 99 Ремд")
    public void Access_1470Remd99() throws IOException, SQLException, InterruptedException {
        Access_1470Method("vimis.remd_sent_result", 99, "vimis.remdadditionalinfo", "SmsIdRemd");
    }
}
