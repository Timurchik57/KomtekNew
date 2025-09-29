package api.REMD;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Хранение серии и номера документа из СЭМД")
@Tag("api/smd")
@Tag("Поиск_api/rremd")
@Tag("Проверка_БД")
@Tag("МИНИО")
@Tag("Проверка_info")
public class Access_1071Test extends BaseAPI {
    public String value1071;

    @Issue(value = "TEL-1071")
    @Issue(value = "TEL-1080")
    @Issue(value = "TEL-4187")
    @Issue(value = "TEL-4224")
    @Link(name = "ТМС-1365", url = "https://team-1okm.testit.software/projects/5/tests/1365?isolatedSection=7dd5e830-69f4-4c6c-95ea-93ca82de7f84")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Проверка таблицы info")
    @DisplayName("Хранение серии и номера документа из СЭМД для id=32 vmcl=99")
    @Description("Отправляем смс с document_series и documentNumber, после проверить методом /api/rremd отправку этих значений")
    public void Access_1071ID_32 () throws IOException, SQLException, InterruptedException {
        AddSms("SMS/id=32-vmcl=99.xml", "32", 99, 1, true, 1, 6, 4, 18, 1, 57, 21);
        CheckSQL("SMS/id=32-vmcl=99.xml", "vimis.remdadditionalinfo");
    }

    @Test
    @Story("Проверка таблицы info")
    @DisplayName("Хранение серии и номера документа из СЭМД для id=36 vmcl=99")
    public void Access_1071ID_36 () throws IOException, SQLException, InterruptedException {
        AddSms("SMS/id=36-vmcl=99.xml", "36", 99, 1, true, 1, 6, 4, 18, 1, 57, 21);
        CheckSQL("SMS/id=36-vmcl=99.xml", "vimis.remdadditionalinfo");
    }

    @Test
    @Story("Проверка таблицы info")
    @DisplayName("Проверяем хранение данныx в таблице info")
    public void Access_4224 () throws IOException, SQLException, InterruptedException {
        AddSms("SMS/id=15-vmcl=99.xml", "15", 99, 1, true, 1, 6, 4, 18, 1, 57, 21);
        CheckSQL_1("SMS/id=15-vmcl=99.xml", "vimis.remdadditionalinfo");
    }


    @Test
    @Story("Проверка таблицы info")
    @DisplayName("Проверяем хранение данныx в таблице info")
    public void Access_4187 () throws IOException, SQLException, InterruptedException {
        AddSms("SMS/id=150.xml", "150", 99, 1, true, 1, 6, 4, 18, 1, 57, 21);
        CheckSQL("SMS/id=150.xml", "vimis.remdadditionalinfo");
    }

    @Step("Метод проверки таблицы info для id=15")
    public void CheckSQL_1 (String Filename, String info) throws SQLException, IOException {
        String snils = getXml(Filename,
                "//associatedEntity[@classCode='PRS']/id/@extension");

        System.out.println("Проверяем добавление значений в таблице " + info + "");
        sql.StartConnection("Select * from " + info + " where smsid = '" + ReadPropFile("idDirectionSms") + "';");
        while (sql.resultSet.next()) {
            String snils_bd = sql.resultSet.getString("recipient_snils");

            Assertions.assertEquals(snils_bd, formatSnils_(snils), "Значение recipient_snils не совпадает");
        }
    }


    @Step("Метод проверки таблицы info для id=32/36")
    public void CheckSQL (String Filename, String info) throws SQLException, IOException {
        String series = null;
        String numbers = null;
        if (Filename != "SMS/id=150.xml") {
            series = getXml(Filename,
                    "//code[@displayName='Серия медицинского свидетельства о рождении']/following-sibling::text/following-sibling::value/text()");
            numbers = getXml(Filename,
                    "//code[@displayName='Номер медицинского свидетельства о рождении']/following-sibling::text/following-sibling::value/text()");
        } else {
            series = getXml(Filename,
                    "//code[@displayName='Серия рецепта']/following-sibling::value/text()");
            numbers = getXml(Filename,
                    "//code[@displayName='Номер рецепта']/following-sibling::value/text()");
        }

        System.out.println("Проверяем добавление значений в таблице " + info + "");
        sql.StartConnection("Select * from " + info + " where smsid = '" + ReadPropFile("idDirectionSms") + "';");
        while (sql.resultSet.next()) {
            String document_series = sql.resultSet.getString("document_series");
            String document_number = sql.resultSet.getString("document_number");

            Assertions.assertEquals(document_series, series, "Значение document_series не совпадает");
            Assertions.assertEquals(document_number, numbers, "Значение document_number не совпадает");
        }
        System.out.println("Отправляем запрос в КРЭМД");
        String params[] = {"LocalUid", String.valueOf(xml.uuid)};
        Api(URLKREMD, "get", params, null, "", 200, true);
        Assertions.assertEquals(Response.getString("result[0]." + DocumentDto + "documentNumber"),
                series + " " + numbers, "Не добавилось значение documentNumber в запросе к КРЭМД");
    }
}
