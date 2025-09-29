package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Чек бокс на определение отправки смс в РЭМД")
@Tag("Проверка_БД")
@Tag("api/smd")
public class Access_1923Test extends BaseAPI {

    public void Access_1923Method(String FileName, String DocType, Integer vmcl, Integer number, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, Boolean Remd) throws IOException, InterruptedException, SQLException {

        xml.ApiSmd(FileName, DocType, vmcl, number, true, docTypeVersion, Role, position, speciality, Role1,
                position1, speciality1);

        sql.StartConnection("Select * from " + sms + " where local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("sending_to_remd");
        }
        if (Remd == true) {
            Assertions.assertEquals(sql.value, "t", "Не отображается sending_to_remd = true");
        } else {
            Assertions.assertNotEquals(sql.value, "t", "Не отображается sending_to_remd = false");
        }
    }

    @Issue(value = "TEL-1923")
    @Link(name = "ТМС-1876", url = "https://team-1okm.testit.software/projects/5/tests/1876?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Чек бокс на определение отправки смс в РЭМД для Vmcl = 1")
    @Story("Вимис и Рэмд")
    @Description("Отправляем смс и смотрим в таблице, стоит ли чек бокс sending_to_remd")
    public void Access_1923_1() throws IOException, SQLException, InterruptedException {
        Access_1923Method("SMS/SMS3.xml", "3", 1, 1, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", true);
    }

    @Test
    @Story("Вимис и Рэмд")
    @DisplayName("Чек бокс на определение отправки смс в РЭМД для Vmcl = 2")
    public void Access_1923_2() throws IOException, SQLException, InterruptedException {
        Access_1923Method("SMS/SMS3.xml", "3", 2, 1, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", true);
    }

    @Test
    @Story("Вимис и Рэмд")
    @DisplayName("Чек бокс на определение отправки смс в РЭМД для Vmcl = 3")
    public void Access_1923_3() throws IOException, SQLException, InterruptedException {
        Access_1923Method("SMS/SMS3.xml", "3", 3, 1, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", true);
    }

    @Test
    @Story("Вимис и Рэмд")
    @DisplayName("Чек бокс на определение отправки смс в РЭМД для Vmcl = 4")
    public void Access_1923_4() throws IOException, SQLException, InterruptedException {
        Access_1923Method("SMS/SMS3.xml", "3", 4, 1, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", true);
    }

    @Test
    @Story("Вимис и Рэмд")
    @DisplayName("Чек бокс на определение отправки смс в РЭМД для Vmcl = 5")
    public void Access_1923_5() throws IOException, SQLException, InterruptedException {
        Access_1923Method("SMS/SMS3.xml", "3", 5, 1, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", true);
    }

    @Test
    @DisplayName("Чек бокс на определение отправки смс в РЭМД для Vmcl = 1")
    @Story("Вимис")
    public void Access_1923_1_f() throws IOException, SQLException, InterruptedException {
        Access_1923Method("SMS/SMS3-id=43.xml", "43", 1, 1, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", false);
    }

    @Test
    @Story("Вимис")
    @DisplayName("Чек бокс на определение отправки смс в РЭМД для Vmcl = 2")
    public void Access_1923_2_f() throws IOException, SQLException, InterruptedException {
        Access_1923Method("SMS/SMS3-id=43.xml", "43", 2, 1, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms", false);
    }

    @Test
    @Story("Вимис")
    @DisplayName("Чек бокс на определение отправки смс в РЭМД для Vmcl = 3")
    public void Access_1923_3_f() throws IOException, SQLException, InterruptedException {
        Access_1923Method("SMS/SMS3-id=43.xml", "43", 3, 1, 2, 6, 4, 18, 1, 57, 21, "vimis.akineosms", false);
    }

    @Test
    @Story("Вимис")
    @DisplayName("Чек бокс на определение отправки смс в РЭМД для Vmcl = 4")
    public void Access_1923_4_f() throws IOException, SQLException, InterruptedException {
        Access_1923Method("SMS/SMS3-id=43.xml", "43", 4, 1, 2, 6, 4, 18, 1, 57, 21, "vimis.cvdsms", false);
    }

    @Test
    @Story("Вимис")
    @DisplayName("Чек бокс на определение отправки смс в РЭМД для Vmcl = 5")
    public void Access_1923_5_f() throws IOException, SQLException, InterruptedException {
        Access_1923Method("SMS/SMS3-id=43.xml", "43", 5, 2, 3, 6, 4, 18, 1, 57, 21, "vimis.infectionsms", false);
    }
}
