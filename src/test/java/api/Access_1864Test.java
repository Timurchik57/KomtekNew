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
@Feature("Проверка записи ошибок без \\")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
public class Access_1864Test extends BaseAPI {

    @Issue(value = "TEL-1864")
    @Link(name = "ТМС-1792", url = "https://team-1okm.testit.software/projects/5/tests/1792?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Description("Отправить СЭМД - установить статус 1 в logs, проверить запись ошибки в remd без /. Сменить статус со своим текстом ошибки, также нет /")
    @Test
    @DisplayName("Проверка записи ошибок без /")
    public void Access_1864() throws IOException, SQLException, InterruptedException {
        Access_1864Method("SMS/SMS3.xml", "3", 1, 1, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.remd_onko_sent_result", false);
    }

    @Test
    @DisplayName("Проверка записи ошибок без / со сменой текста ошибки колбэком")
    public void Access_1864_1() throws IOException, SQLException, InterruptedException {
        Access_1864Method("SMS/SMS3.xml", "3", 1, 1, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", "vimis.remd_onko_sent_result", true);
    }

    @Step("Отправляем смс = {1} c vmcl = {2} и используем коллбэк от ФВИМИС и КРЭМД")
    public void Access_1864Method(String File, String DocType, Integer vmcl, Integer number, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String remd, Boolean kremd) throws IOException, InterruptedException, SQLException {

        xml.ApiSmd(File, DocType, vmcl, number, true, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);

        System.out.println("Отправляем колбек от ФВИМИС");
        CollbekVimis("" + xml.uuid + "", "1", "Проверка 1864", sms, vmcl);
        WaitStatusKremd(remd, "" + xml.uuid + "");
        String error = null;
        while (error == null & CountCollback < 150) {
            sql.StartConnection("Select * from " + remd + " where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                error = sql.resultSet.getString("status");
            }
            CountCollback++;
        }
        Assertions.assertEquals(error.contains("\\"), false, "В сообщении ошибки есть символы \\");

        if (kremd == true) {
            CollbekKremd("" + xml.uuid + "", "success", "Проверка 1864", remd);
            WaitStatusKremd(remd, "" + xml.uuid + "");

            error = null;
            while (error == null & CountCollback < 150) {
                sql.StartConnection("Select * from " + remd + " where local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    error = sql.resultSet.getString("status");
                }
                CountCollback++;
            }

            System.out.println(sql.value);
            Assertions.assertEquals(error.contains("\\"), false, "В сообщении ошибки есть символы \\");
        }
    }
}
