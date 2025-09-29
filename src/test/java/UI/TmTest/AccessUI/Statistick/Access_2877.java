package UI.TmTest.AccessUI.Statistick;

import Base.TestListener;
import UI.TmTest.AccessUI.Directions.Access_1219_1256;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Statistics.AnalyticsMO;
import UI.TmTest.PageObject.Statistics.RoutesTask;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Статистика")
@Tag("Аналитика_МО_ОМП")
@Tag("Задания")
@Tag("Протокол")
@Disabled
public class Access_2877 extends BaseAPI {

    AuthorizationObject authorizationObject;
    AnalyticsMO analyticsMO;
    Access_1219_1256 access_1219_1256;
    Access_1133 access_1133;
    RoutesTask routesTask;
    public String emd_oid;
    public String transfer_id_BD;
    public String transfer_id;
    public String sms_id;
    public String s_code;
    public String status;

    @Test
    @Issue(value = "TEL-2877")
    @Issue(value = "TEL-2932")
    @Issue(value = "TEL-2935")
    @Issue(value = "TEL-2942")
    @Link(name = "ТМС-2005", url = "https://team-1okm.testit.software/projects/5/tests/2005?isolatedSection=701d9777-a0eb-4d94-b711-0fe5d4f6bdbd")
    @Owner(value = "Галиакберов Тимур")
    @DisplayName("Проверка добавления transfer_id к заданию")
    @Description("Переходим в Аналитика Мо по ОМП, выбираем блок гдк есть маршруты, переходим к пациенту, у пациента смотрим на блок добавления заданий. Добавляем задание с определёнными Типами документов и Мед услугами. После потправляем смс с теми же типами и проверяем добавление transfer_id в vimis.routes_tasks_expected_result.transfer_id")
    public void Access_2877() throws InterruptedException, SQLException, IOException, AWTException {

        authorizationObject = new AuthorizationObject(driver);
        analyticsMO = new AnalyticsMO(driver);
        access_1219_1256 = new Access_1219_1256();
        access_1133 = new Access_1133();
        routesTask = new RoutesTask(driver);

        String name_header = "Проверка 2877";
        String Type1 = "Протокол инструментального исследования (CDA) Редакция 3";
        String Type2 = "Протокол лабораторного исследования (CDA) Редакция 4";
        String Type3 = "Медицинское свидетельство о смерти (CDA) Редакция 2";
        //TDU1.1.4
        String service1 = "Биохимический анализ крови (включая исследования уровня холестерина, уровня липопротеинов низкой плотности, С-реактивного белка, определение активности аланинаминотрансферазы в крови, определение активности аспартатаминотрансферазы в крови,определение активности лактатдегидрогеназы в крови, исследование уровня креатинина в крови)";
       // A26.08.027.008п
        String service2 = "Определение РНК коронавируса (SARS-CoV-2) с определением штаммов Omicron и Delta в мазках методом ПЦР";
        //A09.05.118.023
        String service3 = "Исследование уровня антител к антигенам растительного, животного и химическогопроисхождения в крови (пшеничная мука)";

        System.out.println("Меняем в документе "+Type2+" (id = 3) мед услугу");
        setXml("SMS/SMS3.xml", "(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@code", "A01.07.001.001");
        setXml("SMS/SMS3.xml", "(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@displayName", "Сбор анамнеза и жалоб при патологии полости рта, включая черепно-челюстно-лицевой области");

        System.out.println("Меняем в документе "+Type3+" (id = 3) мед услугу");
        setXml("SMS/SMS13-id=13.xml", "(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@code", "A01.07.001.001");
        setXml("SMS/SMS13-id=13.xml", "(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@displayName", "Сбор анамнеза и жалоб при патологии полости рта, включая черепно-челюстно-лицевой области");

        System.out.println("Удаляем запись, которую создадим далее (нужно, если тест упал и не дошёл да конца)");
        access_1133.deleteSql(name_header);

        /** Узнаем включена ли отправка протокола в заданиях */
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

        sql.StartConnection("SELECT * FROM iemk.op_patient_reg WHERE snils = '" + Snils + "';");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("guid");
        }
        System.out.println(sql.value);
        String patient_guid = sql.value;

        ClickElement(analyticsMO.Action);
        ClickElement(analyticsMO.ActionAddTask);
        WaitElement(analyticsMO.TaskPatient);
        Thread.sleep(2500);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 30);

        System.out.println("Создаём задание");
        Thread.sleep(1500);
        WaitNotElement3(analyticsMO.NewTaskDisabled, 30);
        ClickElement(analyticsMO.NewTask);
        Thread.sleep(2500);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 30);

        System.out.println("Заполняем поля");
        WaitElement(analyticsMO.Header);
        inputWord(analyticsMO.HeaderInput, name_header + " ");
        inputWord(analyticsMO.TextInput, name_header + " ");
        Thread.sleep(1500);
        ClickElement(analyticsMO.Data);
        ClickElement(analyticsMO.Day1);
        SelectClickMethod(analyticsMO.MO, analyticsMO.TrueMo("БУ ХМАО-Югры \"Белоярская районная больница\""));

        System.out.println("Заполняем Ожидаемый результат");
        inputWord(driver.findElement(analyticsMO.Margin("Тип документа", "1")), Type1 + " ");
        ClickElement(authorizationObject.Select(Type1));
        ClickElement(analyticsMO.ADD);

        inputWord(driver.findElement(analyticsMO.Margin("Тип документа", "2")), Type2 + " ");
        ClickElement(authorizationObject.Select(Type2));

        driver.findElement(analyticsMO.Margin("Медицинские услуги", "3")).sendKeys(" ");
        ClickElement(authorizationObject.Select(service1));
        ClickElement(analyticsMO.ADD);

        inputWord(driver.findElement(analyticsMO.Margin("Тип документа", "3")), Type3 + " ");
        ClickElement(authorizationObject.Select(Type3));
        ClickElement(analyticsMO.Margin("Медицинские услуги", "5"));
        ClickElement(authorizationObject.Select(service2));
        ClickElement(authorizationObject.Select(service3));

        if (Crypto) {
            System.out.println("Заполняем данные протокола");
            access_1133.ProtocolMethod(false, "Александр ", "Саферов ", "Николаевич ", "15748720095", "Врач", "Новый");
        } else {
            ClickElement(analyticsMO.Done);
        }

        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 120);

        System.out.println("Проверяем что запись появилась");
        WaitElement(analyticsMO.TaskHeaderTrue(name_header));

        System.out.println("Переходим в /stats/routes-tasks");
        driver.get(HostAddressWeb + "/stats/routes-tasks");
        Thread.sleep(1500);
        WaitNotElement3(authorizationObject.LoadingTrue("2"), 120);

        System.out.println("Проверяем, что все чек боксы Не активны (2935) ");
        WaitElement(routesTask.RoutsMedNoActive(name_header, Type1));
        Assertions.assertEquals(driver.findElement(routesTask.RoutsMedNoActive(name_header, Type1)).getCssValue("border-color").contains("#02b402"), false);
        Assertions.assertEquals(driver.findElement(routesTask.RoutsMedNoActive(name_header, Type2)).getCssValue("border-color").contains("#02b402"), false);
        Assertions.assertEquals(driver.findElement(routesTask.RoutsMedNoActive(name_header, Type3)).getCssValue("border-color").contains("#02b402"), false);
        Assertions.assertEquals(driver.findElement(routesTask.RoutsMedNoActive(name_header, service1)).getCssValue("border-color").contains("#02b402"), false);
        Assertions.assertEquals(driver.findElement(routesTask.RoutsMedNoActive(name_header, service2)).getCssValue("border-color").contains("#02b402"), false);
        Assertions.assertEquals(driver.findElement(routesTask.RoutsMedNoActive(name_header, service3)).getCssValue("border-color").contains("#02b402"), false);

        System.out.println("Проверяем, что статус Создано (2942)");
        Assertions.assertEquals(driver.findElement(routesTask.StatusTask(name_header)).getText().contains("Создано"), true);

        System.out.println("Узнаем идентификаторы Типа документа и Медицинской услуги и статус (2942)");
        sql.StartConnection("select * from vimis.routes_tasks order by id desc limit 1;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
            sms_id = sql.resultSet.getString("sms_id");
            status = sql.resultSet.getString("status");
        }

        /** Если не заполенно ни одно transfer_id, то статус должен быть Создано (2942)*/
        Assertions.assertEquals(status, "1", "Статус задания должен быть Создано");

        System.out.println("Узнаем emd_type Типов документов");
        sql.StartConnection("select e.id, e.emd_type, e.doctype, re.\"name\"  from dpc.emd_types e\n" +
                "join dpc.registered_emd re on e.emd_type = re.\"oid\" \n" +
                "where re.\"name\" = '"+Type1+"';");
        while (sql.resultSet.next()) {
            emd_oid = sql.resultSet.getString("emd_type");
        }

        System.out.println("\n------------------Отправляем смс с "+Type1+" (id = 2)");
        PatientGuid = patient_guid;
        //PatientGuid = "e3c3323e-1e05-4f59-b733-9abe7dfc88ce";
        xml.ReplacementWordInFile("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 1, 1, true, 3, 1, 9, 18, 1, 57, 21);
        Api(""+HostAddress+"/api/smd", "post", null, null, xml.body, 200, true);
        transfer_id = Response.getString("result[0].transferId");
        xml.ReplacementWordInFileBack("SMS/SMSV2-(id=2)-vmcl=1.xml");
        Thread.sleep(1500);

        System.out.println("1 проверка - по совпавшему типу документа нет никакой мед услуги = добавляется transfer_id");
        SQL_2877("where re.taskid  = '"+sql.value+"' and emd.name = '"+Type1+"' order by re.document_type desc;");
        while (sql.resultSet.next()) {
            transfer_id_BD = sql.resultSet.getString("transfer_id");
            status = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(transfer_id, transfer_id_BD, "transfer_id должен добавиться при serviceid = null");

        System.out.println("Проверяем, что для "+Type1+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, Type1));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, Type1)).getCssValue("border-color").contains("#02b402");

        /** Проверяем статус, при заполенном transfer_id, должен быть В работе (если есть ещё пустые transfer_id)*/
        Assertions.assertEquals(driver.findElement(routesTask.StatusTask(name_header)).getText().contains("В работе"), true, "Статус задания не сменился на В работе");
        Assertions.assertEquals(status, "2", "Статус задания должен быть В работе");

        System.out.println("\n--------Повторно отправляем смс с "+Type1+" (id = 2)");
        xml.ReplacementWordInFile("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 1, 2, true, 3, 1, 9, 18, 1, 57, 21);
        Api(""+HostAddress+"/api/smd", "post", null, null, xml.body, 200, true);
        xml.ReplacementWordInFileBack("SMS/SMSV2-(id=2)-vmcl=1.xml");

        System.out.println("2 проверка - по совпавшему типу документа нет никакой мед услуги + transfer_id уже заполнен = transfer_id остаёся прежним");
        SQL_2877("where re.taskid  = '"+sql.value+"' and emd.name = '"+Type1+"' order by re.document_type desc;");
        while (sql.resultSet.next()) {
            transfer_id_BD = sql.resultSet.getString("transfer_id");
            status = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(transfer_id, transfer_id_BD, "transfer_id не должен смениться при transfer_id уже заполнен");

        System.out.println("Проверяем, что для "+Type1+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, Type1));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, Type1)).getCssValue("border-color").contains("#02b402");

        /** Проверяем статус, при заполенном transfer_id, должен быть В работе (если есть ещё пустые transfer_id)*/
        Assertions.assertEquals(driver.findElement(routesTask.StatusTask(name_header)).getText().contains("В работе"), true, "Статус задания не сменился на В работе");
        Assertions.assertEquals(status, "2", "Статус задания должен быть В работе");

        System.out.println("Убиарем значения sms_id и transfer_id");
        sql.UpdateConnection("update vimis.routes_tasks set sms_id = NULL where id = '"+sql.value+"';");
        sql.UpdateConnection("update vimis.routes_tasks_expected_result set transfer_id = NULL where taskid = '"+sql.value+"' and document_type = '"+emd_oid+"';");

        System.out.println("\n---------Повторно отправляем смс с "+Type1+" (id = 2)");
        xml.ReplacementWordInFile("SMS/SMSV2-(id=2)-vmcl=1.xml", "2", 1, 2, true, 3, 1, 9, 18, 1, 57, 21);
        Api(""+HostAddress+"/api/smd", "post", null, null, xml.body, 200, true);
        transfer_id = Response.getString("result[0].transferId");
        xml.ReplacementWordInFileBack("SMS/SMSV2-(id=2)-vmcl=1.xml");
        Thread.sleep(1500);

        System.out.println("3 проверка - по совпавшему типу документа нет никакой мед услуги + sms_id null = добавляется transfer_id");
        SQL_2877("where re.taskid  = '"+sql.value+"' and emd.name = '"+Type1+"' order by re.document_type desc;");
        while (sql.resultSet.next()) {
            transfer_id_BD = sql.resultSet.getString("transfer_id");
            status = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(transfer_id, transfer_id_BD, "transfer_id должен добавиться при sms_id = null");

        System.out.println("Проверяем, что для "+Type1+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, Type1));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, Type1)).getCssValue("border-color").contains("#02b402");

        /** Проверяем статус, при заполенном transfer_id, должен быть В работе (если есть ещё пустые transfer_id)*/
        Assertions.assertEquals(driver.findElement(routesTask.StatusTask(name_header)).getText().contains("В работе"), true, "Статус задания не сменился на В работе");
        Assertions.assertEquals(status, "2", "Статус задания должен быть В работе");

        System.out.println("Добавляем обратно значение sms_id");
        sql.UpdateConnection("update vimis.routes_tasks set sms_id = '"+sms_id+"' where id = '"+sql.value+"';");

        System.out.println("\n--------Отправляем смс с "+Type2+" (id = 3)");
        xml.ReplacementWordInFile("SMS/SMS3.xml", "3", 1, 2, true, 3, 1, 9, 18, 1, 57, 21);
        Api(""+HostAddress+"/api/smd", "post", null, null, xml.body, 200, true);
        transfer_id = Response.getString("result[0].transferId");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(1500);

        System.out.println("4 проверка - по совпавшему типу документа + не совпадает мед услуга = не добавляется transfer_id");
        SQL_2877("where re.taskid  = '"+sql.value+"' and emd.name = '"+Type2+"' order by re.document_type desc;");
        while (sql.resultSet.next()) {
            transfer_id_BD = sql.resultSet.getString("transfer_id");
            s_code = sql.resultSet.getString("s_code");
            status = sql.resultSet.getString("status");
        }
        Assertions.assertNull(transfer_id_BD, "transfer_id не должен добавиться при не совпадает мед услуга");

        System.out.println("Проверяем, что для "+Type2+" остаётся без зелёной галочки");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, Type2));
        Assertions.assertEquals(driver.findElement(routesTask.RoutsMedNoActive(name_header, Type2)).getCssValue("border-color").contains("#02b402"), false);

        System.out.println("Проверяем, что для "+service1+" остаётся без зелёной галочки");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, service1));
        Assertions.assertEquals(driver.findElement(routesTask.RoutsMedNoActive(name_header, service1)).getCssValue("border-color").contains("#02b402"), false);

        /** Проверяем статус, при заполенном transfer_id, должен быть В работе (если есть ещё пустые transfer_id)*/
        Assertions.assertEquals(driver.findElement(routesTask.StatusTask(name_header)).getText().contains("В работе"), true, "Статус задания не сменился на В работе");
        Assertions.assertEquals(status, "2", "Статус задания должен быть В работе");

        System.out.println("Меняем в документе "+Type2+" (id = 3) мед услугу");
        setXml("SMS/SMS3.xml", "(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@code", s_code);
        setXml("SMS/SMS3.xml", "(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@displayName", service1);

        System.out.println("\n--------Повторно отправляем смс с "+Type2+" (id = 3)");
        xml.ReplacementWordInFile("SMS/SMS3.xml", "3", 1, 2, true, 3, 1, 9, 18, 1, 57, 21);
        Api(""+HostAddress+"/api/smd", "post", null, null, xml.body, 200, true);
        transfer_id = Response.getString("result[0].transferId");
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");
        Thread.sleep(1500);

        System.out.println("5 проверка - по совпавшему типу документа + совпадает мед услуга = добавляется transfer_id");
        SQL_2877("where re.taskid  = '"+sql.value+"' and emd.name = '"+Type2+"' order by re.document_type desc;");
        while (sql.resultSet.next()) {
            transfer_id_BD = sql.resultSet.getString("transfer_id");
            status = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(transfer_id, transfer_id_BD, "transfer_id должен добавиться при совпадает мед услуга");

        System.out.println("Проверяем, что для "+Type2+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, Type2));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, Type2)).getCssValue("border-color").contains("#02b402");

        /** Проверяем статус, при заполенном transfer_id, должен быть В работе (если есть ещё пустые transfer_id)*/
        Assertions.assertEquals(driver.findElement(routesTask.StatusTask(name_header)).getText().contains("В работе"), true, "Статус задания не сменился на В работе");
        Assertions.assertEquals(status, "2", "Статус задания должен быть В работе");

        System.out.println("Проверяем, что для "+service1+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, service1));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, service1)).getCssValue("border-color").contains("#02b402");

        System.out.println("\n--------Повторно отправляем смс с "+Type2+" (id = 3)");
        xml.ReplacementWordInFile("SMS/SMS3.xml", "3", 1, 2, true, 3, 1, 9, 18, 1, 57, 21);
        Api(""+HostAddress+"/api/smd", "post", null, null, xml.body, 200, true);
        xml.ReplacementWordInFileBack("SMS/SMS3.xml");

        System.out.println("6 проверка - по совпавшему типу документа + совпадает мед услуга + уже заполненный transfer_id = transfer_id остаёся прежним");
        SQL_2877("where re.taskid  = '"+sql.value+"' and emd.name = '"+Type2+"' order by re.document_type desc;");
        while (sql.resultSet.next()) {
            transfer_id_BD = sql.resultSet.getString("transfer_id");
            status = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(transfer_id, transfer_id_BD, "transfer_id должен остаться прежним при совпадает мед услуга + уже заполненный transfer_id");

        System.out.println("Проверяем, что для "+Type2+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, Type2));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, Type2)).getCssValue("border-color").contains("#02b402");

        System.out.println("Проверяем, что для "+service1+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, service1));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, service1)).getCssValue("border-color").contains("#02b402");

        /** Проверяем статус, при заполенном transfer_id, должен быть В работе (если есть ещё пустые transfer_id)*/
        Assertions.assertEquals(driver.findElement(routesTask.StatusTask(name_header)).getText().contains("В работе"), true, "Статус задания не сменился на В работе");
        Assertions.assertEquals(status, "2", "Статус задания должен быть В работе");

        System.out.println("\n--------Отправляем смс с "+Type3+" (id = 13)");
        System.out.println("Меняем в документе "+Type3+" (id = 3) мед услугу");
        SQL_2877("where re.taskid  = '"+sql.value+"' and emd.name = '"+Type3+"' order by re.document_type desc limit 1;");
        while (sql.resultSet.next()) {
            transfer_id_BD = sql.resultSet.getString("transfer_id");
            s_code = sql.resultSet.getString("s_code");
            status = sql.resultSet.getString("status");
        }

        setXml("SMS/SMS13-id=13.xml", "(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@code", s_code);
        setXml("SMS/SMS13-id=13.xml", "(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@displayName", service2);
        xml.ReplacementWordInFile("SMS/SMS13-id=13.xml", "13", 1, 2, true, 3, 6, 4, 18, 1, 57, 21);
        Api(""+HostAddress+"/api/smd", "post", null, null, xml.body, 200, true);
        transfer_id = Response.getString("result[0].transferId");
        xml.ReplacementWordInFileBack("SMS/SMS13-id=13.xml");
        Thread.sleep(1500);

        System.out.println("7 проверка - по совпавшему типу документа + одна из мед услуг совпадает = добавляется transfer_id");
        SQL_2877("where re.taskid  = '"+sql.value+"' and emd.name = '"+Type3+"' order by re.document_type desc limit 1;");
        while (sql.resultSet.next()) {
            transfer_id_BD = sql.resultSet.getString("transfer_id");
            s_code = sql.resultSet.getString("s_code");
            status = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(transfer_id, transfer_id_BD, "transfer_id должен добавиться при одна из мед услуг совпадает");

        System.out.println("Проверяем, что для "+Type3+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, Type3));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, Type3)).getCssValue("border-color").contains("#02b402");

        System.out.println("Проверяем, что для "+service2+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, service2));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, service2)).getCssValue("border-color").contains("#02b402");

        /** Проверяем статус, при заполенном transfer_id, должен быть В работе (если есть ещё пустые transfer_id)*/
        Assertions.assertEquals(driver.findElement(routesTask.StatusTask(name_header)).getText().contains("В работе"), true, "Статус задания не сменился на В работе");
        Assertions.assertEquals(status, "2", "Статус задания должен быть В работе");

        System.out.println("\n--------Повторно отправляем смс с "+Type3+" (id = 13)");
        xml.ReplacementWordInFile("SMS/SMS13-id=13.xml", "13", 1, 2, true, 3, 6, 4, 18, 1, 57, 21);
        Api(""+HostAddress+"/api/smd", "post", null, null, xml.body, 200, true);
        xml.ReplacementWordInFileBack("SMS/SMS13-id=13.xml");
        Thread.sleep(1500);

        System.out.println("8 проверка - по совпавшему типу документа + одна из мед услуг совпадает + уже заполненный transfer_id = transfer_id остаёся прежним");
        SQL_2877("where re.taskid  = '"+sql.value+"' and emd.name = '"+Type3+"' order by re.document_type desc limit 1;");
        while (sql.resultSet.next()) {
            transfer_id_BD = sql.resultSet.getString("transfer_id");
            status = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(transfer_id, transfer_id_BD, "transfer_id должен остаться прежним при одна из мед услуг совпадает + уже заполненный transfer_id");

        System.out.println("Проверяем, что для "+Type3+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, Type3));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, Type3)).getCssValue("border-color").contains("#02b402");

        System.out.println("Проверяем, что для "+service2+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, service2));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, service2)).getCssValue("border-color").contains("#02b402");

        /** Проверяем статус, при заполенном transfer_id, должен быть В работе (если есть ещё пустые transfer_id)*/
        Assertions.assertEquals(driver.findElement(routesTask.StatusTask(name_header)).getText().contains("В работе"), true, "Статус задания не сменился на В работе");
        Assertions.assertEquals(status, "2", "Статус задания должен быть В работе");

        System.out.println("\n--------Повторно отправляем смс с "+Type3+" (id = 13)");
        System.out.println("Меняем в документе "+Type3+" (id = 3) мед услугу");
        SQL_2877("where re.taskid  = '"+sql.value+"' and emd.name = '"+Type3+"' order by re.document_type desc limit 1 offset 1;");
        while (sql.resultSet.next()) {
            transfer_id_BD = sql.resultSet.getString("transfer_id");
            s_code = sql.resultSet.getString("s_code");
            status = sql.resultSet.getString("status");
        }

        setXml("SMS/SMS13-id=13.xml", "(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@code", s_code);
        setXml("SMS/SMS13-id=13.xml", "(//code[@codeSystem='1.2.643.5.1.13.13.11.1070'])[1]/@displayName", service3);
        xml.ReplacementWordInFile("SMS/SMS13-id=13.xml", "13", 1, 2, true, 3, 6, 4, 18, 1, 57, 21);
        Api(""+HostAddress+"/api/smd", "post", null, null, xml.body, 200, true);
        transfer_id = Response.getString("result[0].transferId");
        xml.ReplacementWordInFileBack("SMS/SMS13-id=13.xml");
        Thread.sleep(1500);

        System.out.println("9 проверка - по совпавшему типу документа + одна из мед услуг совпадает = добавляется transfer_id");
        SQL_2877("where re.taskid  = '"+sql.value+"' and emd.name = '"+Type3+"' order by re.document_type desc limit 1 offset 1;");
        while (sql.resultSet.next()) {
            transfer_id_BD = sql.resultSet.getString("transfer_id");
            status = sql.resultSet.getString("status");
        }
        Assertions.assertEquals(transfer_id, transfer_id_BD, "transfer_id должен добавиться при одна из мед услуг совпадает");

        System.out.println("Проверяем, что для "+Type3+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, Type3));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, Type3)).getCssValue("border-color").contains("#02b402");

        System.out.println("Проверяем, что для "+service3+" зелёная галочка");
        driver.navigate().refresh();
        WaitElement(routesTask.RoutsMedNoActive(name_header, service3));
        driver.findElement(routesTask.RoutsMedNoActive(name_header, service3)).getCssValue("border-color").contains("#02b402");

        /** Проверяем статус, при всех заполенных transfer_id, должен быть Выполнено */
        Assertions.assertEquals(driver.findElement(routesTask.StatusTask(name_header)).getText().contains("Выполнено"), true, "Статус задания не сменился на Выполнено");
        Assertions.assertEquals(status, "3", "Статус задания должен быть Выполнено");

        System.out.println("Удаляем запись в БД");
        access_1133.deleteSql(name_header);
    }

    @Step("Метод провреки таблицы с заданиями")
    public void SQL_2877 (String where) throws SQLException {
        sql.StartConnection("SELECT r.id, u.fname , u.sname, u.mname, r.task_header, r.task, r.status, r.create_date, ts.\"name\" status_task, m.namemu, r.sms_id, rsr.doctype, rsr.status, rsr.local_uid,  \n" +
                "re.document_type, emd.\"name\" type_name, re.serviceid, ms.s_code, ms.\"name\" serviceid_name, re.transfer_id FROM vimis.routes_tasks r\n" +
                "full outer join dpc.mis_sp_mu m on r.mo_recieve = m.idmu\n" +
                "full outer join telmed.users u on r.author_id = u.id\n" +
                "full outer join dpc.tasks_statuses ts on r.status = ts.id\n" +
                "full outer join vimis.remd_sent_result rsr on r.sms_id = rsr.id\n" +
                "full outer join vimis.routes_tasks_expected_result re on r.id = re.taskid\n" +
                "full outer join dpc.registered_emd emd on re.document_type = emd.oid \n" +
                "full outer join dpc.medical_services ms on re.serviceid = ms.id\n" +
                ""+where+"");
    }
}
