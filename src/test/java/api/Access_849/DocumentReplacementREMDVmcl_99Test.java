package api.Access_849;

import Base.SQL;
import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import Base.XML;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Замена документа направляемого в РЭМД, vmcl=99")
@Tag("api/smd")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
@Tag("Проверка_БД")
public class DocumentReplacementREMDVmcl_99Test extends BaseAPI {
    XML xml;
    SQL sql;
    private Integer value;
    private Integer IdSms;

    @Issue(value = "TEL-849")
    @Link(name = "ТМС-1243", url = "https://team-1okm.testit.software/projects/5/tests/1243?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Замена документа направляемого в РЭМД, vmcl=99")
    @Description("Замена одинаковых документов в бд, с присвоением новой версии документа")
    public void DocumentReplacementREMDVmcl_99() throws IOException, SQLException, InterruptedException {
        xml = new XML();
        sql = new SQL();
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {

            System.out.println("Отправляем смс с id = 15 и vmcl = 99");
            xml.ApiSmd("SMS/id=15-vmcl=99.xml", "15", 99, 1, true, 1, 6, 4, 18, 1, 57, 21);
            sql.StartConnection(
                    "SELECT * FROM vimis.remd_sent_result  where created_datetime > '" + Date + " 00:00:00.888 +0500' and local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                value = Integer.valueOf(sql.resultSet.getString("id"));
                System.out.println(value);
            }

            /** У данной записи устанавливаем status = error и проверяем, что статус установился */
            System.out.println("status = error в vimis.remd_sent_result");
            sql.UpdateConnection("update vimis.remd_sent_result set status = 'error' where id = '" + value + "';");
            sql.StartConnection("SELECT * FROM vimis.remd_sent_result  where id = '" + value + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("status");
            }

            System.out.println("Повторно отправляем смс с id = 15 и vmcl = 99");
            xml.ReplacementWordInFileBack("SMS/id=15-vmcl=99.xml");
            xml.ApiSmd("SMS/id=15-vmcl=99.xml", "15", 99, 2, false, 1, 6, 4, 18, 1, 57, 21);

            /** Берём в БД значение id данной записи (увеличилось на 1) и проверяем, что версия документа увеличилась на 1 */
            IdSms = value + 1;
            sql.StartConnection(
                    "SELECT * FROM vimis.remd_sent_result  where created_datetime > '" + Date + " 00:00:00.888 +0500' and id = '" + IdSms + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("version_doc");
                Assertions.assertEquals(sql.value, "2");
                System.out.println("status = error - ОК \n");
            }
            Thread.sleep(3000);

            /** У данной записи устанавливаем status = success и fremd_status = '0' и проверяем, что статус установился */
            System.out.println("status = success и fremd_status = '0' в vimis.remd_sent_result");
            sql.UpdateConnection(
                    "update vimis.remd_sent_result set status = 'success', fremd_status = '0' where id = '" + IdSms + "';");
            System.out.println("Повторно отправляем смс с id = 15 и vmcl = 99");
            xml.ReplacementWordInFileBack("SMS/id=15-vmcl=99.xml");
            xml.ApiSmd("SMS/id=15-vmcl=99.xml", "15", 99, 2, false, 1, 6, 4, 18, 1, 57, 21);

            /** Берём в БД значение id данной записи (увеличилось на 1) и проверяем, что версия документа увеличилась на 1 */
            IdSms = value + 2;
            sql.StartConnection(
                    "SELECT * FROM vimis.remd_sent_result  where created_datetime > '" + Date + " 00:00:00.888 +0500' and id = '" + IdSms + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("version_doc");
                Assertions.assertEquals(sql.value, "3");
                System.out.println("status = success и fremd_status = '0' - ОК \n");
            }
            Thread.sleep(3000);

            /** У данной записи устанавливаем status = success и fremd_status = '0'  но с другим Local_uid */
            System.out.println("status = success и fremd_status = '0' в vimis.remd_sent_result, но с другим Local_uid");
            sql.UpdateConnection(
                    "update vimis.remd_sent_result set status = 'success', fremd_status = '0' where id = '" + IdSms + "';");
            System.out.println("Повторно отправляем смс с id = 15 и vmcl = 99");
            xml.ReplacementWordInFileBack("SMS/id=15-vmcl=99.xml");
            xml.ApiSmd("SMS/id=15-vmcl=99.xml", "15", 99, 2, true, 1, 6, 4, 18, 1, 57, 21);

            /** Берём в БД значение id данной записи (увеличилось на 1) и проверяем, что версия документа увеличилась на 1 */
            IdSms = value + 3;
            sql.StartConnection(
                    "SELECT * FROM vimis.remd_sent_result  where created_datetime > '" + Date + " 00:00:00.888 +0500' and id = '" + IdSms + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("version_doc");
                Assertions.assertEquals(sql.value, "1");
                System.out.println("status = success и fremd_status = '0', но с другим Local_uid - ОК \n");
            }
            Thread.sleep(3000);

            /** У данной записи устанавливаем status = success и fremd_status = '1' и проверяем, что статус установился */
            System.out.println("status = success и fremd_status = '1' в vimis.remd_sent_result");
            sql.UpdateConnection(
                    "update vimis.remd_sent_result set status = 'success', fremd_status = '1' where id = '" + IdSms + "';");
            System.out.println("Повторно отправляем смс с id = 15 и vmcl = 99");
            xml.ReplacementWordInFileBack("SMS/id=15-vmcl=99.xml");
            xml.ReplacementWordInFile("SMS/id=15-vmcl=99.xml", "15", 99, 2, false, 1, 6, 4, 18, 1, 57, 21);
            String value = String.valueOf(given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(xml.body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("result[0].errorMessage"));
            Assertions.assertEquals(value, "Документ с идентификатором localUid=" + xml.uuid + " уже принят ФРЭМД");
            System.out.println("status = success и fremd_status = '1' - ОК \n");

            /** У данной записи устанавливаем status = success и fremd_status = 'null' и проверяем, что статус установился */
            System.out.println("status = success и fremd_status = 'null' в vimis.remd_sent_result");
            sql.UpdateConnection(
                    "update vimis.remd_sent_result set status = 'success', fremd_status = NULL where id = '" + IdSms + "';");

            System.out.println("Повторно отправляем смс с id = 15 и vmcl = 99");
            xml.ReplacementWordInFileBack("SMS/id=15-vmcl=99.xml");
            xml.ReplacementWordInFile("SMS/id=15-vmcl=99.xml", "15", 99, 2, false, 1, 6, 4, 18, 1, 57, 21);
            String value1 = String.valueOf(given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(xml.body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("result[0].errorMessage"));
            Assertions.assertEquals(
                    value1, "Документ с идентификатором localUid=" + xml.uuid + " уже направлен в ФРЭМД");
            System.out.println("status = success и fremd_status = 'null' - ОК \n");

            /** У данной записи устанавливаем status = null и fremd_status = 'null' и проверяем, что статус установился */
            System.out.println("status = null и fremd_status = 'null' в vimis.remd_sent_result");
            sql.UpdateConnection(
                    "update vimis.remd_sent_result set status = NULL, fremd_status = NULL where id = '" + IdSms + "';");
            System.out.println("Повторно отправляем смс с id = 15 и vmcl = 99");
            xml.ReplacementWordInFileBack("SMS/id=15-vmcl=99.xml");
            xml.ReplacementWordInFile("SMS/id=15-vmcl=99.xml", "15", 99, 2, false, 1, 6, 4, 18, 1, 57, 21);
            String value2 = String.valueOf(given()
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(xml.body)
                    .post(HostAddress + "/api/smd")
                    .prettyPeek()
                    .then()
                    .statusCode(200)
                    .extract().jsonPath().getString("result[0].errorMessage"));
            Assertions.assertEquals(
                    value2, "Документ с идентификатором localUid=" + xml.uuid + " уже направлен в РЭМД");
            System.out.println("status = null и fremd_status = 'null' - ОК \n");
        }
    }
}
