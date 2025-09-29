package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isOneOf;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Проверка TNM только у последней версии")
@Tag("Проверка_БД")
@Tag("api/smd")
public class Access_1012Test extends BaseAPI {
    public String refversion;
    public Integer Max;

    @Issue(value = "TEL-1012")
    @Link(name = "ТМС-1482", url = "https://team-1okm.testit.software/projects/5/tests/1482?isolatedSection=6be87226-c915-414a-b159-f0d65f786409")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка TNM только у последней версии")
    @Description("Отправить смс у которой есть 1.2.643.5.1.13.13.99.2.546, дублировать строку из таблицы dpc.tnm, установить в бд в строке с code, который отправили в смс refversion к примеру на 3.4, Отправить ту же смс. Далее отправить смс с id дублированной записи")
    public void Access_1012() throws IOException, SQLException, InterruptedException {
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {

            System.out.println("Отправляем смс с 1.2.643.5.1.13.13.99.2.546");
            xml.ApiSmd("SMS/SMS7-vmcl=1.xml", "7", 1, 1, true, 1, 6, 4, 18, 1, 57, 21);

            System.out.println("Берём значение версии из dpc.tnm для id=8780");
            sql.StartConnection("SELECT * FROM dpc.tnm  WHERE id = '8780'");
            while (sql.resultSet.next()) {
                refversion = sql.resultSet.getString("refversion");
            }

            System.out.println("Для старой записи устанавливаем значение refversion равное 3.4");
            sql.UpdateConnection("update dpc.tnm set refversion = '3.4' where id = '8780';");

            System.out.println("Берём максимальное значение id из dpc.tnm так как нет автоувеличения у таблицы dpc.tnm");
            sql.StartConnection("SELECT MAX(id) FROM dpc.tnm;");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("max");
            }
            Max = Integer.valueOf(sql.value) + 1;
            System.out.println(Max);

            System.out.println("Отправляем ту же смс и проверяем ошибку");
            xml.ReplacementWordInFileBack("SMS/SMS7-vmcl=1.xml");
            xml.ReplacementWordInFile("SMS/SMS7-vmcl=1.xml", "7", 1, 2, true, 1, 6, 4, 18, 1, 57, 21);
            JsonPath response = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(xml.body)
                    .post(HostAddress + "/api/smd")
                    .body()
                    .jsonPath();
            Assertions.assertEquals(
                    response.get("result[0].errorMessage"),
                    "Значение code не соответствует справочнику \"1.2.643.5.1.13.13.99.2.546\". Путь до элемента: //ClinicalDocument/component/structuredBody/component[2]/section/entry/act/entryRelationship/observation/value[8].",
                    "Ошибка Значение displayName не соответствует справочнику не вышла"
            );
            System.out.println("Добавляем запись в dpc.tnm с версией для id=8780");

            sql.UpdateConnection("insert into dpc.tnm (id, shortnameoftopography, icdotopography, morphology, stage, tumor, nodus, metastasis, addition, classification, version, refversion, id_tumor, id_nodus, id_metastasis, id_addition) values ('" + Max + "', 'Опухоли пищеварительной системы. Высокодифференцированные нейроэндокринные опухоли желудочно-кишечного тракта. Червеобразный отросток слепой кишки', 'C18.1', 'Карциноид; атипичная карциноидная опухоль', 'IV', 'T1', 'N0', 'M1', NULL, '1', '8', '5.15', '1439', '1444', '1448', NULL)");

            System.out.println("Отправляем ту же смс с исправленным code в 1.2.643.5.1.13.13.99.2.546");
            xml.ReplaceWord("SMS/SMS7-vmcl=1.xml", "8780", "" + Max + "");
            Thread.sleep(1500);
            xml.ReplacementWordInFileBack("SMS/SMS7-vmcl=1.xml");
            xml.ReplacementWordInFile("SMS/SMS7-vmcl=1.xml", "7", 1, 2, true, 1, 6, 4, 18, 1, 57, 21);
            JsonPath responses = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .when()
                    .body(xml.body)
                    .post(HostAddress + "/api/smd")
                    .body()
                    .jsonPath();
            assertThat(responses.get("result[0].message"),
                       isOneOf("СМС по направлению \"Онкология\" успешно опубликован в ЦУ РС ЕГИСЗ.",
                               "СМС по направлению \"Онкология\" успешно опубликован в РИЭМК.",
                               "СМС по направлению \"Онкология\" успешно опубликован в ПК РМИС."
                       )
            );
            System.out.println("Для старой записи устанавливаем значение refversion равное " + refversion + "");
            sql.UpdateConnection("update dpc.tnm set refversion = '5.15' where id = '8780';");
            System.out.println("Удаляем дублированную запись с id = " + Max + "");
            sql.UpdateConnection("Delete from dpc.tnm where id = '" + Max + "';");
            xml.ReplaceWord("SMS/SMS7-vmcl=1.xml", "" + Max + "", "8780");
            Thread.sleep(2000);
        }
    }
}
