package api.REMD;

import Base.SQL;
import Base.TestListener;
import Base.BaseAPI;
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
import static org.hamcrest.CoreMatchers.equalTo;

@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Передача информации о снилс получателя в КРЭМД")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Поиск_api/rremd")
@Tag("МИНИО")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Проверка_info")
public class Access_1097Test extends BaseAPI {

    SQL sql = new SQL();
    public String value1096;

    // Должны быть добавлены в  RemdMapping_RemdRecipientEmdTypes [ "113", "114", "118", "58", "75" ]
    // Если добавлены 57 и 75 то у всех будет отображаться этот блок

    @Issue(value = "TEL-1097")
    @Issue(value = "TEL-1080")
    @Issue(value = "TEL-1120")
    @Issue(value = "TEL-1668")
    @Link(name = "ТМС-1376", url = "https://team-1okm.testit.software/projects/5/tests/1376?isolatedSection=9fa13369-c95a-4df1-8d69-5b94a1692aa1")
    @Link(name = "ТМС-1731", url = "https://team-1okm.testit.software/projects/5/tests/1731?isolatedSection=a612d4c1-8453-43f3-b70f-551c6e8f4cd3")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Передача информации о снилс получателя в КРЭМД для vmcl = 1")
    @Description("Отправляем смс с блоком IRCP, после проверяем таблицу vimis.cvd/akineo/prevention/remd/additionalinfo.recipient_snils на заполненное поле. Для смс в ВИМИС устанавливаем статус 1 в logs и добавляем запись в remd, после используем запрос api/rremd и проверяем передалось ли значение snils. По 1120 заявке проверяем, что отображение блока recipient в запросе к КРЭМД будет только у смс с id=33/34/36")
    public void Access_1097Vmcl_1 () throws IOException, SQLException, InterruptedException {
        Access_1097Method("SMS/id=15-vmcl=99.xml", "15", 1, 1, true, 3, 6, 4, 18, 1, 57, 21);
    }

    @Test
    @DisplayName("Есть снилс, но поля recipient не будет для id = 15 vmcl = 99")
    public void Access_1097Vmcl_99 () throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/id=15-vmcl=99.xml", "15", 99, 1, true, 1, 6, 4, 18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Есть снилс, но поля recipient не будет для vmcl = 1 id = 3")
    public void Access_1097Vmcl_1Id_3 () throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/SMS3.xml", "3", 1, 1, true, 3, 1, 9, 18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Нет снилс, есть поле recipient для vmcl = 1 id = 33")
    public void Access_1097Vmcl_1Id_33 () throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/id=33-vmcl=1.xml", "33", 1, 1, true, 3, 6, 4, 18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Нет снилс, есть поле recipient для vmcl = 99 id = 33")
    public void Access_1097Vmcl_99Id_33 () throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/id=33-vmcl=1.xml", "33", 99, 1, true, 3, 6, 4, 18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Есть снилс, есть поле recipient для vmcl = 99 id = 34")
    public void Access_1097Vmcl_99Id_34 () throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/SMSV19-id=34.xml", "34", 99, 1, true, 3, 6, 4, 18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Есть снилс, есть поле recipient для vmcl = 3 id = 34")
    public void Access_1097Vmcl_3Id_34 () throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/SMSV19-id=34.xml", "34", 3, 1, true, 3, 6, 4, 18, 1, 57, 21
        );
    }

    @Test
    @DisplayName("Передача информации о снилс получателя в КРЭМД для vmcl = 99 id = 36")
    public void Access_1097Vmcl_99Id_36 () throws IOException, SQLException, InterruptedException {
        Access_1097Method(
                "SMS/id=36-vmcl=99.xml", "36", 99, 1, true, 3, 6, 4, 18, 1, 57, 21);
    }

    @Step("метод")
    public void Access_1097Method (String FileName, String DocType, Integer vmcl, Integer number, Boolean RanLoc, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException, SQLException {
        TableVmcl(vmcl);

        System.out.println("Отправляем смс с Doctype = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(FileName, DocType, vmcl, number, RanLoc, docTypeVersion, Role, position, speciality, Role1,
                position1, speciality1);

        sql.StartConnection(
                "Select * from " + smsBase + " where  local_uid = '" + xml.uuid + "';");
        while (sql.resultSet.next()) {
            value1096 = sql.resultSet.getString("id");
            System.out.println(value1096);
        }

        Thread.sleep(1500);

        System.out.println("Проверяем добавление значений в таблице " + infoBase + ".recipient_snils");
        sql.StartConnection("Select * from " + infoBase + " where smsid = '" + value1096 + "';");
        while (sql.resultSet.next()) {
            String recipient_snils = sql.resultSet.getString("recipient_snils");
            String recipient_mo = sql.resultSet.getString("recipient_mo");

            if (FileName == "SMS/id=15-vmcl=99.xml") {
                Assertions.assertEquals(recipient_snils, "19591054097", "Значение recipient_snils не совпадает");
                Assertions.assertEquals(recipient_mo, "1.2.643.5.1.13.13.12.1.77.87",
                        "Значение recipient_mo не совпадает");
            }
            if (FileName == "SMS/SMSV19-id=34.xml" | FileName == "SMS/id=36-vmcl=99.xml") {
                Assertions.assertEquals(recipient_snils, "15979025720", "Значение recipient_snils не совпадает");
                Assertions.assertEquals(recipient_mo, "1.2.643.5.1.13.13.12.1.77.87",
                        "Значение recipient_mo не совпадает");
            }
            if (FileName == "SMS/SMS3.xml" | FileName == "SMS/id=33-vmcl=1.xml") {
                Assertions.assertNull(recipient_snils, "Значение recipient_snils не совпадает");
                Assertions.assertEquals(recipient_mo, "1.2.643.5.1.13.13.12.1.77.87",
                        "Значение recipient_mo не совпадает");
            }
        }
        if (vmcl != 99) {
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 1097", smsBase, vmcl);
            WaitStatusKremd(remdBase, "" + xml.uuid + "");
            CollbekKremd("" + xml.uuid + "", "success", "Проверка 1097", remdBase);
        }

        Thread.sleep(1500);
        String params [] = {"LocalUid", String.valueOf(xml.uuid)};
        Api(URLKREMD, "get", params, null, "", 200, true);

        if (FileName == "SMS/id=15-vmcl=99.xml" | FileName == "SMS/SMS3.xml") {
            Assertions.assertNull(Response.get("result[0].recipient"), "recipient не соответствует");
        }
        if (FileName == "SMS/id=33-vmcl=1.xml") {
            Assertions.assertEquals(Response.get("result[0]." + DocumentDto + "recipient.kind.code"),
                    "MEDICAL_ORGANIZATION", "code не соответствует");
            Assertions.assertNull(Response.get("result[0]." + DocumentDto + "recipient.recipientKindPerson.snils"),
                    "snils не соответствует");
            Assertions.assertEquals(Response.get(
                            "result[0]." + DocumentDto + "recipient.recipientKindMedicalOrganization.organization.code"),
                    "1.2.643.5.1.13.13.12.1.77.87", "organization.code не соответствует");
        }
        if (FileName == "SMS/SMSV19-id=34.xml" | FileName == "SMS/id=36-id=99.xml") {
            Assertions.assertEquals(Response.get("result[0]." + DocumentDto + "recipient.kind.code"),
                    "PATIENT_REPRESENTATIVE", "code не соответствует");
            Assertions.assertEquals(Response.get("result[0]." + DocumentDto + "recipient.recipientKindPerson.snils"),
                    "15979025720", "snils не соответствует");
        }
    }
}
