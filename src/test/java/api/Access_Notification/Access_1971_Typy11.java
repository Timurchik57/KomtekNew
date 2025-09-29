package api.Access_Notification;

import Base.TestListener;
import UI.TmTest.AccessUI.Directions.Access_1219_1256;
import UI.TmTest.AccessUI.Statistick.Access_1133;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Уведомление с типом 11")
@Tag("Оповещение")
@Tag("Проверка_БД")
@Tag("Аналитика_МО_ОМП")
@Tag("Задания")
@Tag("РРП")
@Tag("Протокол")
public class Access_1971_Typy11 extends BaseAPI {

    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    Access_1219_1256 access_1219_1256;
    Access_1133 access_1133;

    @Test
    @Issue(value = "TEL-1971")
    @Issue(value = "TEL-2857")
    @Issue(value = "TEL-2906")
    @Link(name = "ТМС-1871", url = "https://team-1okm.testit.software/projects/5/tests/1871?isolatedSection=767caae3-1d53-4e9c-a277-e3122b6d6370")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Добавление задания для 11 типа уведомления")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок гдк есть маршруты, переходим к пациенту, у пациента смотрим на блок добавления заданий. Добавляем задание с МО, которое указано при добавлении 11 типа уведомлений и проверяем уведомление")
    public void Access_1971() throws InterruptedException, SQLException, AWTException, IOException {

        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        access_1219_1256 = new Access_1219_1256();
        access_1133 = new Access_1133();

        String name_header = "Проверка 1971";
        String Type1 = "Медицинская справка в бассейн (CDA) Редакция 3";
        String service1 = "Определение антител класса М к вирусу SARS-CoV-2 в сыворотке венозной крови (ИФА)";
        String service2 = "Исследование уровня антител к антигенам растительного, животного и химическогопроисхождения в крови (пшеничная мука)";

        System.out.println("Удаляем запись, которую создадим далее (нужно, если тест упал и не дошёл да конца)");
        access_1133.deleteSql(name_header);

        GetCookies();
        Api(HostAddressWeb + "/vimis/pmc/routes/tasks/settings/needsign", "get", null, null, "", 200, true);
        if (Response.get("result").equals(true)) {
            Crypto = true;
            System.out.println("Добавляем сертификаты");
            access_1219_1256.CryptoProMethod();
        }

        System.out.println("Авторизуемся и переходим в Статистика - Аналитика МО по ОМП");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(analyticsMO.Analytics);
        //Специальный пациент для проверки ЛК, небольшое количество СЭМД
        driver.get(HostAddressWeb + "/registry/patient/2ed97c3b-5f30-4fda-bcdc-39d930c6c224/dashboard");
        WaitElement(analyticsMO.Snils);
        String Snils = driver.findElement(analyticsMO.Snils).getText();

        GetRRP("", Snils);
        String patient_guid = Response.getString("patients[0].guid");
        System.out.println(patient_guid);

        ClickElement(analyticsMO.Action);
        ClickElement(analyticsMO.ActionAddTask);
        WaitElement(analyticsMO.TaskPatient);
        Thread.sleep(2500);
        WaitNotElement3(analyticsMO.TaskPatientLoading, 30);

        System.out.println("Создаём задание");
        Thread.sleep(2500);
        WaitNotElement3(analyticsMO.NewTaskLoading, 30);
        ClickElement(analyticsMO.NewTask);

        System.out.println("Заполняем поля");
        WaitElement(analyticsMO.Header);
        inputWord(analyticsMO.HeaderInput, name_header + " ");
        inputWord(analyticsMO.TextInput, name_header + " ");
        Thread.sleep(1500);
        ClickElement(analyticsMO.Data);
        ClickElement(analyticsMO.Day1);
        SelectClickMethod(analyticsMO.MO, analyticsMO.TrueMo("БУ ХМАО-Югры \"Белоярская районная больница\""));

        System.out.println("Заполняем Ожидаемый результат");
        inputWord(driver.findElement(analyticsMO.Margin("Тип документа", "1")), "Медицинская справка в бассейн (CDA) Редакция ");
        ClickElement(authorizationObject.Select(Type1));

        driver.findElement(analyticsMO.Margin("Медицинские услуги", "1")).sendKeys(" ");
        ClickElement(authorizationObject.Select(service1));
        ClickElement(authorizationObject.Select(service2));
        ClickElement(analyticsMO.Header);

        if (Crypto) {
            System.out.println("Заполняем данные протокола");
            access_1133.ProtocolMethod(false, "Александр ", "Саферов ", "Николаевич ", "15748720095", "Врач", "Новый");
        } else {
            ClickElement(analyticsMO.Done);
        }

        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 120);

        System.out.println("Проверяем что запись появилась");
        WaitElement(analyticsMO.TaskHeaderTrue("Проверка 1971"));

        System.out.println("Узнаем идентификаторы Типа документа и Медицинской услуги (2857)");
        sql.StartConnection("select * from vimis.routes_tasks order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        String type_id = null;
        String list_serviceid = null;
        List<String> list_serviceid_idBD = new ArrayList<>();

        sql.StartConnection(
                "SELECT r.id, u.fname , u.sname, u.mname, r.task_header, r.task, r.create_date, ts.\"name\" status_task, m.namemu, r.sms_id, rsr.doctype, rsr.status, rsr.local_uid,  \n" +
                        "re.document_type, emd.\"name\" type_name, re.serviceid, ms.\"name\" serviceid_name FROM vimis.routes_tasks r\n" +
                        "full outer join dpc.mis_sp_mu m on r.mo_recieve = m.idmu\n" +
                        "full outer join telmed.users u on r.author_id = u.id\n" +
                        "full outer join dpc.tasks_statuses ts on r.status = ts.id\n" +
                        "full outer join vimis.remd_sent_result rsr on r.sms_id = rsr.id\n" +
                        "full outer join vimis.routes_tasks_expected_result re on r.id = re.taskid\n" +
                        "full outer join dpc.registered_emd emd on re.document_type = emd.oid \n" +
                        "full outer join dpc.medical_services ms on re.serviceid = ms.id\n" +
                        "where re.taskid  = '" + sql.value + "' order by re.serviceid asc;");
        while (sql.resultSet.next()) {
            type_id = sql.resultSet.getString("document_type");
            list_serviceid = sql.resultSet.getString("serviceid");

            list_serviceid_idBD.add(list_serviceid);
        }

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);

        System.out.println("Проверяем, что оповещение пришло");
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(11, "", 0);
        System.out.println(TEXT);
        Assertions.assertTrue(TEXT.contains("\"TaskHeader\":\"Проверка 1971\""),
                "Оповещение для 11 типа уведомлений не добавилось");
        Assertions.assertTrue(TEXT.contains("\"Task\":\"Проверка 1971\""),
                "Оповещение для 11 типа уведомлений не добавилось");

        /** Проверяем (2857) */
        String medicalServices = list_serviceid_idBD.toString();
        System.out.println(
                "ExpectedResult\":[{\"DocumentType\":\"" + type_id + "\",\"MedicalServices\":" + medicalServices.replaceAll(
                        ", ", ",") + "}]");
        Assertions.assertTrue(TEXT.contains(
                "\"ExpectedResult\":[{\"DocumentType\":\"" + type_id + "\",\"MedicalServices\":" + medicalServices.replaceAll(
                        ", ", ",") + "}]"), "Тип документа и Медицинские услуги для 11 типа уведомлений не добавилось");

        /** Проверяем (2906) */
        Assertions.assertTrue(TEXT.contains("\"PatientGuid\":\"" + patient_guid + "\""),
                "Не отображается PatientGuid для 11 типа уведомлений");

        access_1133.deleteSql(name_header);
    }
}
