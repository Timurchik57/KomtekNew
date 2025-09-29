package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Тесты API")
@Feature("Отпрвка сразу нескольких колбэков ФВИМИС")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("Вимис_статус")
public class Access_1957Test extends BaseAPI {

    private static Boolean str;

    @Issue(value = "TEL-1957")
    @Issue(value = "TEL-1958")
    @Link(name = "ТМС-1827", url = "https://team-1okm.testit.software/projects/5/tests/1827?isolatedSection=1f9b0804-847c-4b2c-8be6-2d2472e56a75")
    @Owner(value = "Галиакберов Тимур")
    @Order(1)
    @Test
    @DisplayName("Отправляем 25 смс")
    @Description("Отправить 25 смс, получить коллбэк от ФВИМИС - проверить, что все документы добавлись в таблицу remd")
    public void Access_1957() throws IOException, InterruptedException {
        for (int i = 0; i < 25; i++) {
            str = false;
            xml.ApiSmd("SMS/SMS3.xml", "3", 1, 2, true, 3, 1, 9, 18, 1, 57, 21);
            xml.ReplacementWordInFileBack("SMS/SMS3.xml");
            InputProp("src/test/resources/Many.properties", "many_" + i + "", "" + xml.uuid + "");
        }
        str = true;
    }

    @Test
    @Order(2)
    @DisplayName("Отправляем колбэки ФВИМИС")
    public void Access_1957_2() throws IOException, SQLException {
        if (str == true) {
            for (int i = 0; i < 25; i++) {
                FileInputStream in = new FileInputStream("src/test/resources/Many.properties");
                props = new Properties();
                props.load(in);
                in.close();
                CollbekVimis("" + props.getProperty("many_" + i + "") + "", "1", "Проверка Уведомлений по 1957/1958",
                        "vimis.sms", 1);
            }
            str = true;
        } else {
            str = false;
        }
    }

    @Test
    @Order(3)
    @DisplayName("Проверяем добавление данных в таблицу remd")
    public void Access_1957_3() throws IOException, SQLException, InterruptedException {
        Thread.sleep(5000);
        if (str == true) {
            for (int i = 0; i < 25; i++) {
                FileInputStream in = new FileInputStream("src/test/resources/Many.properties");
                props = new Properties();
                props.load(in);
                in.close();

                String error = "0";
                CountCollback = 1;
                while (error.contains("0") & CountCollback < 26) {
                    sql.PrintSQL = false;
                    sql.StartConnection(
                            "select count(*) from vimis.remd_onko_sent_result where local_uid = '" + props.getProperty(
                                    "many_" + i + "") + "';");
                    while (sql.resultSet.next()) {
                        error = sql.resultSet.getString("count");
                    }
                    CountCollback++;
                    if (!error.contains("0")) {
                        break;
                    }
                }
            }
        } else {
            str = false;
        }
        sql.PrintSQL = true;
    }
}
