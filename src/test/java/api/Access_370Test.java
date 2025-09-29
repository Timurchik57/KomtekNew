package api;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.collection.IsIn;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isOneOf;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Feature("Проверка валидных ошибок при одинаковых данных")
@Tag("Проверка_БД")
@Tag("api/smd")
@Tag("ФЛК")
@Tag("Проверки_уникальных_идентификаторов")
@Tag("Основные")
public class Access_370Test extends BaseAPI {
    public String value388;

    @Issue(value = "TEL-370")
    @Link(name = "ТМС-901", url = "https://team-1okm.testit.software/projects/5/tests/901?isolatedSection=827ef86d-406f-4fec-a839-c7939a1a4497")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Order(1)
    @DisplayName("Валидный СЭМД с DocType = 3 vmcl= 1")
    @Description("Отправляем валидный СЭМД, после отправляем с одинаковым SetID, ID, VersionNumber и смотрим валидные ошибки")
    public void Access_370ID_3() throws IOException, InterruptedException {

        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем смс с Doctype = 3");
            xml.ApiSmd("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21);
            System.out.println(ID);
            System.out.println(SetID);
            System.out.println(VN);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Id, который уже есть СЭМД с DocType = 43 vmcl= 1")
    public void Access_370ID_5() throws IOException, SQLException, InterruptedException {

        String file = "SMS/SMSV2-(id=2)-vmcl=1.xml";

        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            VN = (int) Math.floor(timestamp.getTime() / 1000) + 4;
            InputPropFile("value_VN", String.valueOf(VN));
            xml.ReplaceWord(file, "${iD}", props.getProperty("value_ID"));
            xml.ReplaceWord(file, "${setId}", props.getProperty("value_SetID"));
            xml.ReplaceWord(file, "${versionNumber}", String.valueOf(VN));
            xml.ReplaceWord(file, "${mo}", MO);
            xml.ReplaceWord(file, "${guid}", PatientGuid);
            xml.ReplaceWord(file, "${namemo}", NameMO);
            xml.ReplacementWordInFile(file, "2", 1, 0, true, 3, 1, 9, 18, 1, 57, 21);
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
            assertThat(
                    value,
                    isOneOf("Уникальный идентификатор набора версий документа совпадает с идентификатором уже загруженного документа (Тип ранее загруженного документа = \"3\")")
            );
        }
    }

    @Test
    @Order(3)
    @DisplayName("Валидный СЭМД 2 с DocType = 3 vmcl= 1")
    public void Access_370ID() throws IOException, SQLException, InterruptedException {

        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            System.out.println("Отправляем смс с Doctype = 3");
            xml.ApiSmd("SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21);
            System.out.println(props.getProperty("value_ID"));
            System.out.println(props.getProperty("value_SetID"));
            System.out.println(props.getProperty("value_VN"));
        }
    }

    @Test
    @Order(4)
    @DisplayName("versionNumber, который уже есть СЭМД с DocType = 3 vmcl= 1")
    public void Access_370ID_5_SetID() throws IOException, SQLException, InterruptedException {

        String file = "SMS/SMS3.xml";
        if (KingNumber == 1 || KingNumber == 2 || KingNumber == 4) {
            ID = (int) Math.floor(timestamp.getTime() / 1000) + 4;
            InputPropFile("value_ID", String.valueOf(ID));
            xml.ReplaceWord(file, "${iD}", String.valueOf(ID));
            xml.ReplaceWord(file, "${setId}", props.getProperty("value_SetID"));
            xml.ReplaceWord(file, "${versionNumber}", props.getProperty("value_VN"));
            xml.ReplaceWord(file, "${mo}", MO);
            xml.ReplaceWord(file, "${guid}", PatientGuid);
            xml.ReplaceWord(file, "${namemo}", NameMO);
            System.out.println(props.getProperty("value_ID"));
            System.out.println(props.getProperty("value_SetID"));
            System.out.println(props.getProperty("value_VN"));
            xml.ReplacementWordInFile(file, "3", 1, 0, true, 3, 1, 9, 18, 1, 57, 21);
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
            assertThat(
                    value,
                    isOneOf("Версия загружаемого документа с указанными реквизитами SetID совпадает (или меньше) с ранее загруженным документом")
            );
        }
    }

    @Test
    @Order(5)
    @Issue(value = "TEL-2593")
    @DisplayName("Проверяем отправку смс с уже зарегистрированным Local_uid vmcl = 1")
    public void Access_2593_vmcl_1() throws IOException, SQLException, InterruptedException {
        Method_2593("SMS/SMS3.xml", "3", 1, 3, "vimis.sms", 1, 9, 12, 1, 57, 21, "vimis.remd_onko_sent_result");
    }

    @Test
    @Order(6)
    @DisplayName("Проверяем отправку смс с уже зарегистрированным Local_uid vmcl = 2")
    public void Access_2593_vmcl_2() throws IOException, SQLException, InterruptedException {
        Method_2593("SMS/SMS3.xml", "3", 2, 3, "vimis.preventionsms", 1, 9, 12, 1, 57, 21, "vimis.remd_prevention_sent_result");
    }

    @Test
    @Order(7)
    @DisplayName("Проверяем отправку смс с уже зарегистрированным Local_uid vmcl = 3")
    public void Access_2593_vmcl_3() throws IOException, SQLException, InterruptedException {
        Method_2593("SMS/SMS3.xml", "3", 3, 2, "vimis.akineosms", 1, 9, 12, 1, 57, 21, "vimis.remd_akineo_sent_result");
    }

    @Test
    @Order(8)
    @DisplayName("Проверяем отправку смс с уже зарегистрированным Local_uid vmcl = 4")
    public void Access_2593_vmcl_4() throws IOException, SQLException, InterruptedException {
        Method_2593("SMS/SMS3.xml", "3", 4, 2, "vimis.cvdsms", 1, 9, 12, 1, 57, 21, "vimis.remd_cvd_sent_result");
    }

    @Test
    @Order(9)
    @DisplayName("Проверяем отправку смс с уже зарегистрированным Local_uid vmcl = 5")
    public void Access_2593_vmcl_5() throws IOException, SQLException, InterruptedException {
        Method_2593("SMS/SMS3.xml", "3", 5, 3, "vimis.infectionsms", 1, 9, 12, 1, 57, 21, "vimis.remd_infection_sent_result");
    }

    @Test
    @Order(10)
    @DisplayName("Проверяем отправку смс с уже зарегистрированным Local_uid vmcl = 99")
    public void Access_2593_vmcl_99() throws IOException, SQLException, InterruptedException {
        Method_2593("SMS/SMS3.xml", "3", 99, 3, "", 1, 9, 12, 1, 57, 21, "vimis.remd_sent_result");
    }

    @Test
    @Order(11)
    @DisplayName("Проверяем отправку смс с уже зарегистрированным Local_uid vmcl = 99")
    public void Access_2593_vmcl_99_129() throws IOException, SQLException, InterruptedException {
        Method_2593("SMS/SMS3.xml", "3", 99, 3, "", 1, 9, 12, 1, 57, 21, "vimis.remd_sent_result");
    }

    public void Method_2593(String FileName, String Doctype, Integer vmcl, Integer docTypeVersion, String sms,  Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String remd) throws IOException, InterruptedException, SQLException {

        xml.ApiSmd(FileName, Doctype, vmcl, 1, true, docTypeVersion, Role, position, speciality, Role1, position1,
                speciality1);
        String uuid = String.valueOf(xml.uuid);

        if (vmcl != 99) {
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 2593", sms, vmcl);
        }
        WaitStatusKremd(remd, "" + xml.uuid + "");
        CollbekKremd("" + xml.uuid + "", "success", "Проверка 2593", remd);

        System.out.println("Повторно отправляем смс с тем же Local_uid, status = success. fremd_status = 1");
        xml.ReplacementWordInFile(FileName, Doctype, vmcl, 1, false, docTypeVersion, Role, position, speciality, Role1,
                position1, speciality1);
        Api(""+ HostAddress + "/api/smd","post", null, null, xml.body, 200, true);
        Assertions.assertEquals(Response.getString("result[0].errorMessage"), "Документ с идентификатором localUid="+xml.uuid+" уже принят ФРЭМД");

        if (vmcl != 99) {
            sql.StartConnection("select s.* from " + sms + " s\n" +
                    "join " + remd + " r on s.id = r.sms_id\n" +
                    "where s.local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("id");
            }
            sql.UpdateConnection("update " + remd + " set fremd_status = '0' where sms_id = '" + sql.value + "';");
        } else {
            sql.UpdateConnection("update " + remd + " set fremd_status = '0' where local_uid = '" + xml.uuid + "';");
        }

        System.out.println("Повторно отправляем смс с тем же Local_uid, status = success. fremd_status = 0");

        xml.ReplacementWordInFile(FileName, Doctype, vmcl, 1, false, docTypeVersion, Role, position, speciality, Role1,
                position1, speciality1);
        Api(""+ HostAddress + "/api/smd","post", null, null, xml.body, 200, true);
        assertThat(Response.getString("result[0].message"), IsIn.isOneOf(
                        "СМС по направлению \"Онкология\" успешно опубликован в ЦУ РС ЕГИСЗ.",
                        "СМС по направлению \"Онкология\" успешно опубликован в РИЭМК.",
                        "СМС по направлению \"Онкология\" успешно опубликован в ПК РМИС.",
                "СМС по направлению \"Профилактика\" успешно опубликован в ЦУ РС ЕГИСЗ.",
                "СМС по направлению \"АкиНео\" успешно опубликован в ЦУ РС ЕГИСЗ.",
                "СМС по направлению \"ССЗ\" успешно опубликован в ЦУ РС ЕГИСЗ.",
                "СМС по направлению \"Инфекционные болезни\" успешно опубликован в ЦУ РС ЕГИСЗ.",
                "СМС предназначенная только для передачи в РЭМД успешно опубликована в ЦУ РС ЕГИСЗ."));

    }
}
