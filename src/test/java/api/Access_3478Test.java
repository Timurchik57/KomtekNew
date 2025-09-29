package api;

import Base.BaseAPI;
import Base.SQL;
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
@Feature("Отправка xml с hash")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Дополнительные_параметры")
public class Access_3478Test extends BaseAPI {

    @Issue(value = "TEL-3478")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка xml с hash vmcl = 1")
    @Description("Отправить смс в ВИМИС и РЭМД сначала с hash, потом без него. После проверить в таблицах remd добавление данного значения в generator_hash")
    public void Access_3478Vmcl_1() throws IOException, SQLException, InterruptedException {
        Method_3478("SMS/SMS3.xml", "3", 1,  3, 1, 9, 18, 1, 57, 21, "vimis.sms");
    }

    @Test
    @DisplayName("Отправка xml с hash vmcl = 2")
    public void Access_3478Vmcl_2() throws IOException, SQLException, InterruptedException {
        Method_3478("SMS/SMS3.xml", "3", 2,    3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms");
    }

    @Test
    @DisplayName("Отправка xml с hash vmcl = 3")
    public void Access_3478Vmcl_3() throws IOException, SQLException, InterruptedException {
        Method_3478("SMS/SMS3.xml", "3", 3,   2, 1, 9, 18, 1, 57, 21, "vimis.akineosms");
    }

    @Test
    @DisplayName("Отправка xml с hash vmcl = 4")
    public void Access_3478Vmcl_4() throws IOException, SQLException, InterruptedException {
        Method_3478("SMS/SMS3.xml", "3", 4,  2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms");
    }

    @Test
    @DisplayName("Отправка xml с hash vmcl = 5")
    public void Access_3478Vmcl_5() throws IOException, SQLException, InterruptedException {
        Method_3478("SMS/SMS3.xml", "3", 5,  3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms");
    }

    @Test
    @DisplayName("Отправка xml с hash vmcl = 99")
    public void Access_3478Vmcl_99() throws IOException, SQLException, InterruptedException {
        Method_3478("SMS/SMS3.xml", "3", 99,  3, 1, 9, 18, 1, 57, 21, "vimis.remd_sent_result");
    }

    @Step("Отправка смс с id = {1} и vmcl = {2}")
    public void Method_3478 (String FileName, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms) throws IOException, InterruptedException, SQLException {
        sql = new SQL();
        String file = "";
        Integer numb = 0;

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                file = FileName;
                numb = 1;
            } else {
                file = "SMS/SMS3_hash.xml";
                numb = 2;
            }
            System.out.println("Отправляем запрос смс " + DocType + " с vmcl = " + vmcl + "");
            xml.ApiSmd(file, DocType, vmcl, numb, true, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);
            xml.ReplacementWordInFileBack(file);


            sql.StartConnection("Select * from " + sms + " where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("generator_hash");
                System.out.println(sql.value);
            }

            if (i == 0) {
                Assertions.assertEquals(sql.value, null, "Значение Hash должно отсутствовать");
            } else {
                Assertions.assertEquals(sql.value, "8ad85121d571cd1af3e66ac7ad358bd323864f598ae3a6a45c12dcdb2b602f64", "Значение Hash не совпадает");
            }
        }
    }
}