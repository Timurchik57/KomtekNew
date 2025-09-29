package api.Soap;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Отправка Soap с несуществующим guid в РРП")
@Tag("Soap")
@Tag("Проверка_БД")
public class Access_2621Test extends BaseAPI {

    @Issue(value = "TEL-2621")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка Soap с несуществующим guid в РРП vmcl = 1")
    @Description("Отправляем Soap запрос с несуществующим guid в РРП после проверяем таблицу на добавление универсального guid и отсутствие ошибок")
    public void Access_2621() throws IOException, SQLException, InterruptedException {
        SoapSignatureMethod("SMS/SMS5.xml", SoapSystemId, 1, "5", 3);
        SoapBodyMethod("5", 1, 3, "vimis.sms");
    }

    @Test
    @DisplayName("Отправка Soap с несуществующим guid в РРП vmcl = 2")
    public void Access_2621_2() throws IOException, SQLException, InterruptedException {
        SoapSignatureMethod("SMS/SMS5-vmcl=2.xml", SoapSystemId, 2, "5", 3);
        SoapBodyMethod("5", 2, 3, "vimis.preventionsms");
    }

    @Test
    @DisplayName("Отправка Soap с несуществующим guid в РРП vmcl = 3")
    public void Access_2621_3() throws IOException, SQLException, InterruptedException {
        SoapSignatureMethod("SMS/SMS5-vmcl=3(v1.2).xml", SoapSystemId, 3, "5", 2);
        SoapBodyMethod("5", 3, 2, "vimis.akineosms");
    }

    @Test
    @DisplayName("Отправка Soap с несуществующим guid в РРП vmcl = 4")
    public void Access_2621_4() throws IOException, SQLException, InterruptedException {
        SoapSignatureMethod("SMS/SMS5-vmcl=4.xml", SoapSystemId, 4, "6", 3);
        SoapBodyMethod("6", 4, 3, "vimis.cvdsms");
    }

    @Test
    @DisplayName("Отправка Soap с несуществующим guid в РРП vmcl = 5")
    public void Access_2621_5() throws IOException, SQLException, InterruptedException {
        SoapSignatureMethod("SMS/SMS5-vmcl=3(v1.2).xml", SoapSystemId, 5, "5", 3);
        SoapBodyMethod("5", 5, 3, "vimis.infectionsms");
    }
}
