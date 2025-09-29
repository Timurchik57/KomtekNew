package api.Access_Notification;

import Base.SQL;
import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import Base.XML;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Уведомление с типом 14")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Медицинская_справка")
@Disabled
public class Access_3113_Type14_Test extends BaseAPI {
    
    boolean Repeated;

    // #Доделать
    @Issue(value = "TEL-3113")
    @Issue(value = "TEL-3114")
    @Issue(value = "TEL-3115")
    @Issue(value = "TEL-3116")
    @Issue(value = "TEL-3157")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Создание Мед справки для Vmcl = 1")
    @Story("Вимис и Рэмд")
    @Description("Отправляем смс по всем направлениям, добавляем медицинскую справку в таблицу vimis.patient_requests_for_doc - после фоновый сервис добавляет данные о последнем смс в таблицу vimis.patient_requests_for_doc")
    public void Access_3113_1() throws IOException, SQLException, InterruptedException {
        Access_3113Method("SMS/SMS3.xml", "3", 1, 1, 3, 1, 9, 18, 1, 57, 21, "vimis.sms", " vimis.remd_onko_sent_result");
    }

    @Test
    @Issue(value = "TEL-3209")
    @Story("Вимис и Рэмд")
    @DisplayName("Создание Мед справки для Vmcl = 2, отправляем 2 раза с одинаковым requestId")
    public void Access_3209() throws IOException, SQLException, InterruptedException {
        Repeated = true;
        Access_3113Method("SMS/SMS3.xml", "3", 2, 1, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.remd_prevention_sent_result");
        Repeated = false;
    }

    @Test
    @Story("Вимис и Рэмд")
    @DisplayName("Создание Мед справки для Vmcl = 2")
    public void Access_3113_2() throws IOException, SQLException, InterruptedException {
        Access_3113Method("SMS/SMS3.xml", "3", 2, 1, 3, 1, 9, 18, 1, 57, 21, "vimis.preventionsms", "vimis.remd_prevention_sent_result");
    }

    @Test
    @Story("Вимис и Рэмд")
    @DisplayName("Создание Мед справки для Vmcl = 3")
    public void Access_3113_3() throws IOException, SQLException, InterruptedException {
        Access_3113Method("SMS/SMS3.xml", "3", 3, 1, 2, 1, 9, 18, 1, 57, 21, "vimis.akineosms", "vimis.remd_akineo_sent_result");
    }

    @Test
    @Story("Вимис и Рэмд")
    @DisplayName("Создание Мед справки для Vmcl = 4")
    public void Access_3113_4() throws IOException, SQLException, InterruptedException {
        Access_3113Method("SMS/SMS3.xml", "3", 4, 1, 2, 1, 9, 18, 1, 57, 21, "vimis.cvdsms", "vimis.remd_cvd_sent_result");
    }

    @Test
    @Story("Вимис и Рэмд")
    @DisplayName("Создание Мед справки для Vmcl = 5")
    public void Access_3113_5() throws IOException, SQLException, InterruptedException {
        Access_3113Method("SMS/SMS3.xml", "3", 5, 1, 3, 1, 9, 18, 1, 57, 21, "vimis.infectionsms", "vimis.remd_infection_sent_result");
    }

    @Test
    @Story("Рэмд")
    @DisplayName("Создание Мед справки для Vmcl = 99")
    public void Access_3113_99() throws IOException, SQLException, InterruptedException {
        Access_3113Method("SMS/SMS3.xml", "3", 99, 1, 3, 1, 9, 18, 1, 57, 21, "", "vimis.remd_sent_result");
    }

    @Test
    @DisplayName("Создание Мед справки для Vmcl = 1")
    @Story("Вимис")
    public void Access_3113_1_f() throws IOException, SQLException, InterruptedException {
        Access_3113Method("SMS/SMS3-id=43.xml", "43", 1, 1, 3, 6, 4, 18, 1, 57, 21, "vimis.sms", "");
    }

    @Test
    @Story("Вимис")
    @DisplayName("Создание Мед справки для Vmcl = 2")
    public void Access_3113_2_f() throws IOException, SQLException, InterruptedException {
        Access_3113Method("SMS/SMS3-id=43.xml", "43", 2, 1, 3, 6, 4, 18, 1, 57, 21, "vimis.preventionsms", "");
    }

    @Test
    @Story("Вимис")
    @DisplayName("Создание Мед справки для Vmcl = 3")
    public void Access_3113_3_f() throws IOException, SQLException, InterruptedException {
        Access_3113Method("SMS/SMS3-id=43.xml", "43", 3, 1, 2, 6, 4, 18, 1, 57, 21, "vimis.akineosms", "");
    }

    @Test
    @Story("Вимис")
    @DisplayName("Создание Мед справки для Vmcl = 4")
    public void Access_3113_4_f() throws IOException, SQLException, InterruptedException {
        Access_3113Method("SMS/SMS3-id=43.xml", "43", 4, 1, 2, 6, 4, 18, 1, 57, 21, "vimis.cvdsms", "");
    }

    @Test
    @Story("Вимис")
    @DisplayName("Создание Мед справки для Vmcl = 5")
    public void Access_3113_5_f() throws IOException, SQLException, InterruptedException {
        Access_3113Method("SMS/SMS3-id=43.xml", "43", 5, 2, 3, 6, 4, 18, 1, 57, 21, "vimis.infectionsms", "");
    }

    @Step("Метод отправки смс и добавление мед справки в vimis.patient_requests_for_doc")
    public void Access_3113Method(String FileName, String DocType, Integer vmcl, Integer number, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1, String sms, String remd) throws IOException, InterruptedException, SQLException {

        System.out.println("Отправляем смс с id = " + DocType + " и vmcl = " + vmcl + "");
        xml.ApiSmd(FileName, DocType, vmcl, number, true, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);

        System.out.println("Используем колбэк ВИМИС");
        if (vmcl != 99 & !FileName.contains("SMS/SMS3-id=43.xml")) {
            CollbekVimis("" + xml.uuid + "", "1", "Проверка 3113", sms, vmcl);
        }
        if (!FileName.contains("SMS/SMS3-id=43.xml")) {
            WaitStatusKremd(remd, "" + xml.uuid + "");

            /** Устанавливаем fremd_status=1 чтобы по заявке 3157 можно было проверить скачивание мед справок*/
            CollbekKremd("" + xml.uuid + "", "success", "Проверка 3157", remd);
        }

        System.out.println("Отправляем запрос на создание справки в vimis.patient_requests_for_doc");
        String uuid = String.valueOf(UUID.randomUUID());

        String body = "{\n" +
                "  \"requestId\": \""+uuid+"\",\n" +
                "  \"patientGuid\": \""+PatientGuid+"\",\n" +
                "  \"moOid\": \""+MO+"\",\n" +
                "  \"docType\": \""+DocType+"\"\n" +
                "}";
        Api(ApiVimis+"/api/smd/patientrequestsfordoc","post", null, null, body, 200, true);

        if (Repeated) {
            body = "{\n" +
                    "  \"requestId\": \""+uuid+"\",\n" +
                    "  \"patientGuid\": \""+PatientGuid+"\",\n" +
                    "  \"moOid\": \""+MO+"\",\n" +
                    "  \"docType\": \""+DocType+"\"\n" +
                    "}";
            Api(ApiVimis+"/api/smd/patientrequestsfordoc","post", null, null, body, 400, true);
            Assertions.assertEquals(Response.getString("errorMessage"), "RequestId: Направленный идентификатор requestId не уникальный", "Должна быть ошибка, так как передаём requestId, который уже есть в БД");
        }


        sql.StartConnection("Select * from vimis.patient_requests_for_doc where request_id = '" + uuid + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        InputPropFile("ValueID_3113_"+vmcl+"_"+DocType+"", sql.value);
        InputPropFile("ValueLocalUid_3113_"+vmcl+"_"+DocType+"", "" + xml.uuid + "");
        InputPropFile("ValueRequestID_3113_"+vmcl+"_"+DocType+"", uuid);
    }

    @Step("Метод для просмотра уведомления по 14 типу")
    public void After_3113 (Integer vmcl, String id, String Request) throws InterruptedException, SQLException {
        sql = new SQL();
        xml = new XML();
        TableVmcl(vmcl);

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);

        String localuid = null;
        String oid = null;
        String patient_guid = null;

        sql.StartConnection("Select * from vimis.patient_requests_for_doc where id = '"+id+"';");
        while (sql.resultSet.next()) {
            localuid = sql.resultSet.getString("local_uid");
        }

        sql.StartConnection("Select s.*, m.oid from "+smsBase+" s " +
                "join dpc.mis_sp_mu m on s.medicalidmu = m.medicalidmu" +
                " where local_uid = '"+localuid+"';");
        while (sql.resultSet.next()) {
            localuid = sql.resultSet.getString("local_uid");
            oid = sql.resultSet.getString("oid");
            patient_guid = sql.resultSet.getString("patient_guid");
        }

        Assertions.assertEquals(oid, MO, "oid из таблицы dpc.mis_sp_mu не совпадает с "+MO+" для записи vimis.patient_requests_for_doc c id ="+id+"");
        Assertions.assertEquals(patient_guid, PatientGuid, "patient_guid из таблицы "+smsBase+" не совпадает с "+PatientGuid+" для записи vimis.patient_requests_for_doc c id ="+id+"");

        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        System.out.println(Request);
        System.out.println(localuid);

        for (int i = 1; i < 50; i++) {
            TEXT = driver.findElement(By.xpath("//div[@class='text-center']/div[" + i + "]/span")).getText();
            //Для типа уведомлений 14
            if (TEXT.contains("\"RequestId\":\""+Request+"\"") &
                    TEXT.contains("\"CreateDate\":") &
                    TEXT.contains("\"DocType\":") &
                    TEXT.contains("\"LocalUid\":\""+localuid+"\"") &
                    TEXT.contains("\"MessageId\":") == false) {
                System.out.println("Тип 14");
                break;
            }
        }
        System.out.println(TEXT);
        System.out.println(xml.uuid);
        Assertions.assertTrue(TEXT.contains("\"RequestId\":\""+Request+"\""),
                "RequestId для оповещения 14 для vmcl = " + vmcl + " не совпадает с "+Request+"");
        Assertions.assertTrue(TEXT.contains("\"LocalUid\":\""+localuid+"\""),
                "localuid для оповещения 14 для vmcl = " + vmcl + " не совпадает с "+localuid+"");
        Assertions.assertTrue(TEXT.contains("\"Vmcl\":"+vmcl+""),
                "Vmcl для оповещения 14 для vmcl = " + vmcl + " не совпадает с "+vmcl+"");
    }
}
