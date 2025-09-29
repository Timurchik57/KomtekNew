package UI.TmTest.AccessUI.RegisteredReferences;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import Base.TestListenerChange;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.RegisteredReferences.References;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListenerChange.class)
@Epic("Тесты UI")
@Feature("Заказанные справки")
@Tag("Медицинская_справка")
@Tag("Проверка_БД")
@Tag("Лк_врача")
@Tag("Основные")
public class Access_4161Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    References references;
    Access_3292Test access3292Test;
    List<String> list = new ArrayList<>();

    @Issue(value = "TEL-3473")
    @Issue(value = "TEL-3475")
    @Issue(value = "TEL-4161")
    @Issue(value = "TEL-4161")
    @Issue(value = "TEL-4168")
    @Issue(value = "TEL-4174")
    @Issue(value = "TEL-4166")
    @Issue(value = "TEL-4169")
    @Issue(value = "TEL-4172")
    @Issue(value = "TEL-4175")
    @Issue(value = "TEL-4176")
    @Issue(value = "TEL-4177")
    @Issue(value = "TEL-4165")
    @Issue(value = "TEL-4179")
    @Issue(value = "TEL-4180")
    @Issue(value = "TEL-4181")
    @Issue(value = "TEL-4183")
    @Issue(value = "TEL-4184")
    @Issue(value = "TEL-4192")
    @Issue(value = "TEL-4377")
    @Link(name = "ТМС-2189", url = "https://team-1okm.testit.software/projects/5/tests/2189?isolatedSection=af882452-d09a-4051-af99-3d04bf599188")
    @Link(name = "ТМС-2334", url = "https://team-1okm.testit.software/projects/5/tests/2344?isolatedSection=71cd14d9-46c4-4330-a433-f6755c9d1214")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Story("Добавление Медицинских справок")
    @DisplayName("Проверка взаимодействия с Медицинскими справками с типом - Электронный документ")
    @Description("Создаём медицинскую справку, проверяем отображение в разных разделах, меняем статус, добавляем файл, скачиваем и проверяем БД")
    public void Access_4161 () throws IOException, SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        references = new References(driver);
        access3292Test = new Access_3292Test();

        sql.UpdateConnection(
                "Delete from vimis.patient_requests_for_doc where patient_guid = '" + PGuid + "' and request_date < now();");

        AddRole("Тестовая роль", "Доступ к разделу заказанных справок", true);
        GetDate();
        String today = SetDate(0, 0).substring(3) + "." + SetDate(0, 0).substring(0, 2) + "." + Year;

        System.out.println("Переходим в Лк врача и создаём медицинскую справку");
        AuthorizationMethod(authorizationObject.OKB);
        WaitElement(references.ReeferenceNew);
        driver.get(HostAddressWeb + "/registry/patient/dashboard");
        ClickElement(references.AddMedHelp);
        SelectClickMethod(references.MedHelp("Наименование документа"),
                authorizationObject.Select("Выписки, копии карт из КДП, ЖК, ДП, ООЦ, ОФЦ"));
        ClickElement(references.MedHelp("Период формирования документа"));
        ClickElement(references.DateBackToday);
        ClickElement(references.DateToday);
        SelectClickMethod(references.MedHelp("Формат документа"), authorizationObject.Select("В электронном виде"));
        SelectClickMethod(references.MedHelp("Наименование МО"),
                authorizationObject.Select("БУ ХМАО-Югры \"Окружная клиническая больница\""));
        ClickElement(references.Button("Готово"));
        WaitElement(By.xpath("//div[text()='Услуга предоставляется в срок до 1 месяца']"));
        WaitElement(By.xpath(
                "//p[contains(.,'Максимальный срок выдачи медицинских документов (их копий) и выписок из них с момента регистрации в\n" +
                        "            медицинской организации запроса не должен превышать сроков, установленных требованиями законодательства\n" +
                        "            о порядке рассмотрения обращений граждан Российской Федерации.')]"));
        WaitElement(By.xpath(
                "//p[contains(.,'Федеральный закон от 2 мая 2006 г. N 59-Ф3 \"О порядке рассмотрения обращений граждан Российской\n" +
                        "            Федерации\" (Собрание законодательства Российской Федерации, 2006, N 19, ст. 2060).')]"));
        Thread.sleep(1500);
        ClickElement(references.Button2);
        SqlMed("1", "", "", "2", false, false);

        System.out.println("1 проверка - проверяем что документ ещё не готов - Ваша справка еще не готова");
        WaitElement(references.MedHelpTable(Date, PMORequest, "Отправлен", "В электронном виде", true));
        WebElement element = driver.findElement(
                references.MedHelpTable(Date, PMORequest, "Отправлен", "В электронном виде", true));
        actions.moveToElement(element);
        actions.perform();
        Thread.sleep(1200);
        WaitElement(references.Alert);

        System.out.println(
                "2 проверка - проверяем документ в Новых справках и добавляем файл - после отправки смотрим повторно на документ в Новых справках со статусом 3 (Готов)");
        GetId();
        GetDate();
        ClickElement(references.ReeferenceNew);
        WaitElement(references.SetDoc(PName, PSnils_, today, "В электронном виде", PMORequest, "Отправлен"));
        // Файл не получится добавить через веб интерфейс, поэтому добавляем через метод
        System.out.println("Добавляем файл");
        AddFile("3");
        SqlMed("3", "Текст ответа от МО", "Комментарий для сотрудников МО", "2", false, true);

        driver.navigate().refresh();
        WaitElement(references.SetDoc(PName, PSnils_, today, "В электронном виде", PMORequest, "Готов"));

        System.out.println(
                "3 проверка - проверяем что повторно отредактировать и отправить данные в справке нельзя - данные в бд соответственно не изменятся");
        ClickElement(references.ReeferenceNew);
        ClickElement(references.SetDoc(PName, PSnils_, today, "В электронном виде", PMORequest, "Готов"));
        WaitElement(references.InputComment("Комментарий для пациента"));
        inputWord(driver.findElement(references.InputComment("Комментарий для пациента")), "Любой текст");
        inputWord(driver.findElement(references.InputComment("Сообщение врачу")), "Любой текст");
        ClickElement(references.Button("Отправить"));
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);
        WaitElement(references.AlertError("Нельзя прикрепить файл повторно"));
        SqlMed("3", "Текст ответа от МО", "Комментарий для сотрудников МО", "2", false, true);

        System.out.println(
                "4 проверка - Нажимаем на скачать все документы - документы скачиваются - статус остается прежний");
        Thread.sleep(2500);
        //ClickElement(references.ErrorClosed);
        ClickElement(references.Button("Скачать"));
        authorizationObject.LoadingTime(10);
        WaitNotElement3(references.AlertError, 3);

        System.out.println(
                "5 проверка - переходим в ЛК пациента и смотрим оставленный коментарий + скачиваем документ (Устанавливается статус 5)");
        driver.get(HostAddressWeb + "/registry/patient/dashboard");
        ClickElement(references.MedHelpTable(Date, PMORequest, "Готов", "В электронном виде", false));
        WaitElement(references.AlertSms("Текст ответа от МО"));
        ClickElement(references.Button("Закрыть"));

        ClickElement(references.MedHelpTable(Date, PMORequest, "Готов", "В электронном виде", true));
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);
        WaitNotElement3(references.AlertError, 3);
        driver.navigate().refresh();
        Thread.sleep(1500);
        WaitElement(references.MedHelpTable(Date, PMORequest, "Выдан гражданину", "В электронном виде", false));
        SqlMed("5", "Текст ответа от МО", "Комментарий для сотрудников МО", "2", true, true);

        System.out.println(
                "6 проверка - переходим в закрытые справки, скачиваем один документ - статус остается тем же");
        ClickElement(references.ReeferenceClosed);
        ClickElement(
                references.SetDocClosed(PName, PSnils_, today, "В электронном виде", PMORequest, "Выдан гражданину",
                        today));
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);
        WaitNotElement3(references.AlertError, 3);
        SqlMed("5", "Текст ответа от МО", "Комментарий для сотрудников МО", "2", true, true);

        System.out.println("7 проверка - переходим в закрытые справки, скачиваем все документы");
        ClickElement(references.Button("Скачать"));
        authorizationObject.LoadingTime(10);
        WaitNotElement3(references.AlertError, 3);
        SqlMed("5", "Текст ответа от МО", "Комментарий для сотрудников МО", "2", true, true);

        sql.UpdateConnection(
                "Delete from vimis.patient_requests_for_doc where patient_guid = '" + PGuid + "' and request_date < now();");
    }

    @Test
    @Story("Добавление Медицинских справок")
    @DisplayName("Проверка взаимодействия с Медицинскими справками с типом - Бумажный формат")
    @Description("Создаём медицинскую справку, проверяем отображение в разных разделах, меняем статус, добавляем файл, скачиваем и проверяем БД")
    public void Access_4161_1 () throws IOException, SQLException, InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        references = new References(driver);
        access3292Test = new Access_3292Test();

        sql.UpdateConnection(
                "Delete from vimis.patient_requests_for_doc where patient_guid = '" + PGuid + "' and request_date < now();");

        AddRole("Тестовая роль", "Доступ к разделу заказанных справок", true);
        GetDate();
        String today = SetDate(0, 0).substring(3) + "." + SetDate(0, 0).substring(0, 2) + "." + Year;

        System.out.println("Создаём медицинскую справку через метод");
        AddApi("1");

        AuthorizationMethod(authorizationObject.OKB);
        System.out.println("1 проверка - проверяем что для бумажной формы нельзя добавить документ");
        GetId();
        GetDate();
        ClickElement(references.ReeferenceNew);

        System.out.println("Пробуем добавить файл");
        ClickElement(references.SetDoc(PName, PSnils_, today, "В бумажном виде", PMORequest, "Отправлен"));
        WaitNotElement3(references.AddFile, 3);
        WaitElement(references.InputComment("Комментарий для пациента"));
        inputWord(driver.findElement(references.InputComment("Комментарий для пациента")), "Текст ответа от МО ");
        inputWord(driver.findElement(references.InputComment("Сообщение врачу")), "Комментарий для сотрудников МО ");
        ClickElement(references.Button("Отправить"));
        Thread.sleep(1500);
        authorizationObject.LoadingTime(10);
        WaitNotElement(references.AlertError);
        SqlMed("1", "Текст ответа от МО", "Комментарий для сотрудников МО", "1", false, false);

        System.out.println("2 проверка - переходим в ЛК Врача и проверяем сообщение сообщение от МО + нет кнопки Скачать");
        CheckMedHelp(false,false, "Отправлен", "Отправлен",true, false,  true);

        System.out.println("3 проверка - устанавливаем статус Выдан гражданину + проверяем ЛК врача");
        // Ошибка при скачивании в Закрытых + Нет Скачать в ЛК
        ClickElement(references.ReeferenceNew);
        CheckMedHelp(false,true, "Отправлен", "Выдан гражданину",true, true,  true);
        SqlMed("5", "Текст ответа от МО", "Комментарий для сотрудников МО", "1", true, false);
    }

    @Test
    @Story("Добавление Медицинских справок")
    @DisplayName("Проверяем отображение мед справок с разными статусами для электронного вида")
    @Description("Создаём медицинскую справку, меняем ей статус и смотрим, где она расположена")
    public void Access_4161_2 () throws IOException, SQLException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        references = new References(driver);
        access3292Test = new Access_3292Test();

        sql.UpdateConnection(
                "Delete from vimis.patient_requests_for_doc where patient_guid = '" + PGuid + "' and request_date < now();");

        AddRole("Тестовая роль", "Доступ к разделу заказанных справок", true);
        GetDate();
        String today = SetDate(0, 0).substring(3) + "." + SetDate(0, 0).substring(0, 2) + "." + Year;

        System.out.println("Создаём медицинскую справку через метод");
        AddApi("2");

        // Проверяем 4377 - Документ нужно прикреплять при смене статуса Выдан гражданину и Готов к остальным не надо
        AuthorizationMethod(authorizationObject.OKB);
        System.out.println("1 проверка - проверяем что для электронного вида, нужно добавить файл прежде чем менять статус на Выдан гражданину");
        GetId();
        ClickElement(references.ReeferenceNew);
        CheckMedHelp(true, true, "Отправлен", "Выдан гражданину",false, false,  false);

//        System.out.println("2 проверка - проверяем что для электронного вида, нужно добавить файл прежде чем менять статус на Готов");
//        ClickElement(references.ReeferenceNew);
//        CheckMedHelp(true, true, "Отправлен", "Готов",false, false,  false);

        System.out.println("3 проверка - проверяем что для электронного вида, нужно добавить файл прежде чем менять статус на В работе");
        ClickElement(references.ReeferenceNew);
        CheckMedHelp(true, true, "Отправлен", "В работе",false, false,  false);

        System.out.println("4 проверка - проверяем что для электронного вида, нужно добавить файл прежде чем менять статус на Отклонен");
        ClickElement(references.ReeferenceNew);
        CheckMedHelp(true, true, "Отправлен", "Отклонен",false, false,  false);

        System.out.println("5 проверка - меняем у электронного вида статус отличный от 3 (Готов), например статус В работе (2)");
        // Нельзя сменить статус + нельзя скачать документ в ЛК
        System.out.println("Добавляем файл");
        AddFile("2");
        SqlMed("2", "Текст ответа от МО", "Комментарий для сотрудников МО", "2", false, true);
        driver.navigate().refresh();
        WaitElement(references.SetDoc(PName, PSnils_, today, "В электронном виде", PMORequest, "В работе"));
        CheckMedHelp(true,false, "", "В работе", false, false, true);

        System.out.println("6 проверка - Создаём справку и меняем статус на 4 (Отклонен) + после переходим в закрытые и скачиваем");
        // Можно скачать в закрытых, но нельзя скачать в ЛК
        AddApi("2");
        GetId();
        System.out.println("Добавляем файл");
        AddFile("4");
        SqlMed("4", "Текст ответа от МО", "Комментарий для сотрудников МО", "2", true, true);
        CheckMedHelp(true,false, "", "Отклонен", true, true, true);

        System.out.println("7 проверка - Создаём справку и меняем статус на 5 (Выдан гражданину) + после переходим в закрытые и скачиваем");
        // Можно скачать и в закрытых и в ЛК
        AddApi("2");
        GetId();
        System.out.println("Добавляем файл");
        AddFile("5");
        SqlMed("5", "Текст ответа от МО", "Комментарий для сотрудников МО", "2", true, true);
        CheckMedHelp(true,false, "", "Выдан гражданину", true, true, true);
    }

    @Test
    @Story("Добавление Медицинских справок")
    @DisplayName("Проверяем отображение мед справок с разными статусами для бумажного вида")
    @Description("Создаём медицинскую справку, меняем ей статус и смотрим, где она расположена")
    public void Access_4161_3 () throws IOException, SQLException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        references = new References(driver);
        access3292Test = new Access_3292Test();

        sql.UpdateConnection(
                "Delete from vimis.patient_requests_for_doc where patient_guid = '" + PGuid + "' and request_date < now();");

        AddRole("Тестовая роль", "Доступ к разделу заказанных справок", true);
        GetDate();
        String today = SetDate(0, 0).substring(3) + "." + SetDate(0, 0).substring(0, 2) + "." + Year;

        System.out.println("1 проверка - Создаём справку в бумажном виде меняем статус на 2 (В работе) + проверяем что нет кнопки скачать");
        AddApi("1");
        GetId();
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(references.ReeferenceNew);
        CheckMedHelp(false, true, "Отправлен", "В работе",true, false,  true);
        SqlMed("2", "Текст ответа от МО", "Комментарий для сотрудников МО", "1", false, false);

        System.out.println("2 проверка - У справки в бумажном виде меняем статус на 3 (Готов) + проверяем что нет кнопки скачать");
        GetId();
        ClickElement(references.ReeferenceNew);
        CheckMedHelp(false, true, "В работе", "Готов",true, false,  true);
        SqlMed("3", "Текст ответа от МО", "Комментарий для сотрудников МО", "1", false, false);

        System.out.println("3 проверка - У справки в бумажном виде меняем статус на 4 (Отклонен) + проверяем что нет кнопки скачать");
        GetId();
        ClickElement(references.ReeferenceNew);
        CheckMedHelp(false, true, "Готов", "Отклонен",true, true,  true);
        SqlMed("4", "Текст ответа от МО", "Комментарий для сотрудников МО", "1", true, false);
    }


    @Step("Метод для смены статуса и проверки в новых/закрытых справках и в ЛК")
    public void CheckMedHelp (boolean elect, boolean change, String ClickStatus, String NewStatus, boolean waitNew, boolean waitClosed, boolean waitLK) throws IOException, InterruptedException {
        String type = "";

        if (elect) {
            type = "В электронном виде";
        } else {
            type = "В бумажном виде";
        }

        String today = SetDate(0, 0).substring(3) + "." + SetDate(0, 0).substring(0, 2) + "." + Year;

        if (change) {
            ClickElement(references.SetDoc(PName, PSnils_, today, type, PMORequest, ClickStatus));
            WaitElement(references.InputComment("Комментарий для пациента"));
            SelectClickMethod(references.InputStatus("Статус"), authorizationObject.Select(NewStatus));
            if (NewStatus.equals("Отклонен")) {
                inputWord(driver.findElement(references.InputComment("Указать причину отказа")),
                        "Текст ответа от МО ");
            } else {
                inputWord(driver.findElement(references.InputComment("Комментарий для пациента")),
                        "Текст ответа от МО ");
            }
            inputWord(driver.findElement(references.InputComment("Сообщение врачу")),
                    "Комментарий для сотрудников МО ");
            ClickElement(references.Button("Отправить"));
            Thread.sleep(1500);
            authorizationObject.LoadingTime(10);
            if (elect && (NewStatus == "Выдан гражданину" || NewStatus == "Готов")) { // 4377
                System.out.println("11111111111111");
                WaitElement(references.AlertError("Прикрепление файла обязательно для запроса электронной справки"));
            } else {
                System.out.println("2222222222222222222222");
                WaitNotElement(references.AlertError("Прикрепление файла обязательно для запроса электронной справки"));
            }
        }

        if (waitNew) {
            ClickElement(references.ReeferenceNew);
            if (NewStatus.equals("Отклонен") | NewStatus.equals("Выдан гражданину")) {
                WaitNotElement3(references.SetDoc(PName, PSnils_, today, type, PMORequest, NewStatus),
                        3);
            }
            if (NewStatus.equals("Отправлен") | NewStatus.equals("В работе") | NewStatus.equals("Готов")) {
                WaitElement(references.SetDoc(PName, PSnils_, today, type, PMORequest, NewStatus));
            }
        }

        if (waitClosed) {
            ClickElement(references.ReeferenceClosed);
            ClickElement(references.SetDocClosed(PName, PSnils_, today, type, PMORequest, NewStatus,
                    today));
            Thread.sleep(1500);
            authorizationObject.LoadingTime(10);
            if (elect) {
                WaitNotElement3(references.AlertError, 2);
            } else {
                WaitElement(references.AlertError("Документ не найден"));
            }
        }

        if (waitLK) {
            driver.get(HostAddressWeb + "/registry/patient/dashboard");
            ClickElement(references.MedHelpTable(Date, PMORequest, NewStatus, type, false));
            WaitElement(references.AlertSms("Текст ответа от МО"));
            ClickElement(references.Button("Закрыть"));
            Thread.sleep(1500);

            if (elect) {
                WaitElement(references.MedHelpTable(Date, PMORequest, NewStatus, type, true));

                if (NewStatus.equals("В работе") | NewStatus.equals("Отклонен")) {
                    WebElement element = driver.findElement(
                            references.MedHelpTable(Date, PMORequest, NewStatus, type, true));
                    actions.moveToElement(element);
                    actions.perform();
                    Thread.sleep(1200);
                    WaitElement(references.Alert);
                }

                if (NewStatus.equals("Выдан гражданину")) {
                    ClickElement(references.MedHelpTable(Date, PMORequest, NewStatus, type, true));
                    Thread.sleep(1500);
                    authorizationObject.LoadingTime(10);
                    WaitNotElement3(references.AlertError, 3);
                }
            } else {
                WaitNotElement3(references.MedHelpTable(Date, PMORequest, NewStatus, type, true), 1);
            }
        }
    }

    @Step("Метод для создания мед справки через API")
    public void AddApi (String requestType) throws  IOException {
        GetDate();

        String yesterday = Year + "-" + SetDate(-1, 0).substring(0, 2) + "-" + SetDate(-1,
                0).substring(3);
        String todayNow = Year + "-" + SetDate(0, 0).substring(0, 2) + "-" + SetDate(0,
                0).substring(3);

        Api(WebVimis + "/patientrequestsfordoc", "post", null, null, "{\n" +
                "    \"requestId\": \"" + UUID.randomUUID() + "\",\n" +
                "    \"patientGuid\": \"" + PGuid + "\",\n" +
                "    \"moOid\": \"" + POidMoRequest + "\",\n" +
                "    \"docType\": \"1\",\n" +
                "    \"periodStart\": \"" + yesterday + "T09:58:37.890Z\",\n" +
                "    \"periodEnd\": \"" + todayNow + "T09:58:37.890Z\",\n" +
                "    \"phone\": \"9876543221\",\n" +
                "    \"requestType\": \"" + requestType + "\"\n" +
                "}", 200, false);
    }

    @Step("Метод для записи id созданной мед справки")
    public void GetId () throws SQLException, IOException {
        sql.StartConnection("select *from vimis.patient_requests_for_doc p\n" +
                "order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }

        InputPropFile("4164_Id", sql.value);
    }

    @Step("Метод проверки БД медицинских справок")
    public void SqlMed (String status, String text_Answer, String text_Local, String request_Type, boolean end, boolean minio) throws IOException, SQLException, InterruptedException {
        String moSql = null;
        String guid = null;
        String period_start = null;
        String period_end = null;
        String status_id = null;
        String text_answer = null;
        String text_local = null;
        String request_type = null;
        String end_date = null;
        String minio_name = null;
        if (text_Answer.isEmpty() | text_Local.isEmpty()) {
            text_Answer = null;
            text_Local = null;
        }
        Thread.sleep(1500);

        System.out.println(Year + "-" + Month + "-" + Day);

        sql.StartConnection("select p.*, m.namemu createmo from vimis.patient_requests_for_doc p\n" +
                "join dpc.mis_sp_mu m on p.сreator_medicalidmu = m.medicalidmu order by p.id desc limit 1;");
        while (sql.resultSet.next()) {
            moSql = sql.resultSet.getString("createmo");
            guid = sql.resultSet.getString("patient_guid");
            period_start = sql.resultSet.getString("period_start");
            period_end = sql.resultSet.getString("period_end");
            status_id = sql.resultSet.getString("status_id");
            text_answer = sql.resultSet.getString("text_answer");
            text_local = sql.resultSet.getString("text_local");
            request_type = sql.resultSet.getString("request_type");
            end_date = sql.resultSet.getString("end_date");
            minio_name = sql.resultSet.getString("minio_name");
        }

        GetDate();
        Assertions.assertEquals(moSql, PMORequest, "МО создания не совпадает");
        Assertions.assertEquals(guid, PGuid, "guid не совпадает");
        Assertions.assertEquals(period_start.substring(0, 10),
                Year + "-" + SetDate(-1, 0).substring(0, 2) + "-" + SetDate(-1, 0).substring(3),
                "period_start не совпадает");
        SetDate(+1, 0);
        Assertions.assertEquals(period_end.substring(0, 10),
                Year + "-" + SetDate(0, 0).substring(0, 2) + "-" + SetDate(0, 0).substring(3),
                "period_end не совпадает");
        Assertions.assertEquals(status_id, status, "status_id не совпадает");
        Assertions.assertEquals(text_answer, text_Answer, "text_answer не совпадает");
        Assertions.assertEquals(text_local, text_Local, "text_local не совпадает");
        Assertions.assertEquals(request_type, request_Type, "request_type не совпадает");

        if (end) {
            Assertions.assertEquals(end_date.substring(0, 10),
                    Year + "-" + SetDate(0, 0).substring(0, 2) + "-" + SetDate(0, 0).substring(3),
                    "end_date не совпадает");
        }
        if (minio) {
            Assertions.assertNotNull(minio_name, "minio_name Должна быть заполнена");
        }
    }

    @Step("Метод обавления файла к мед справке")
    public void AddFile (String status) throws IOException, InterruptedException {
        String content = new String(Files.readAllBytes(Paths.get("SMS/id=101-vmcl=99.txt")));

        cookies = driver.manage().getCookies();
        Api(HostAddressWeb + "/vimis/patientrequestsfordoc/" + ReadPropFile("4164_Id"), "put", null, null, "{\n" +
                "  \"textAnswer\": \"Текст ответа от МО\",\n" +
                "  \"textLocal\": \"Комментарий для сотрудников МО\",\n" +
                "  \"statusId\": " + status + ",\n" +
                "  \"docContent\": \"" + content + "\"\n" +
                "}", 200, false);
    }
}
