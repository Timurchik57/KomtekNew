package api.After;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import UI.TmTest.AccessUI.Administration.Access_1791Test;
import UI.TmTest.AccessUI.Administration.Access_1898Test;
import UI.TmTest.AccessUI.Administration.Access_3102Test;
import UI.TmTest.AccessUI.Statistick.Access_4311Test;
import api.*;
import api.Access_Notification.Access_3113_Type14_Test;
import api.Before.Authorization;
import api.REMD.Access_1100Test;
import io.qameta.allure.*;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.annotation.Order;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Проверка бд после тестов")
public class After extends BaseAPI {

    @Order(1)
    @Issue(value = "TEL-904")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Хранение информации о смерти пациентов из СМС13")
    @Description("Проверка заявки 904, на добавление в таблицу vimis.cvd_death_cause")
    public void AfterTests904 () throws IOException, SQLException, InterruptedException {
        Access_904Test access_904Test = new Access_904Test();
        access_904Test.StorageInformationAfter("value_904");
    }

    @Order(2)
    @Issue(value = "TEL-922")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование даты создания документа")
    @Description("Проверка заявки 922, на добавление в таблицы vimis.remd_onko_sent_result/vimis.remd_prevention_sent_result/vimis.remd_akineo_sent_result/vimis.remd_cvd_sent_result.")
    public void AfterTests922 () throws IOException, SQLException {
        Access_922Test access_922Test = new Access_922Test();
        access_922Test.Access_922MethodAfter("vimis.remd_onko_sent_result", 1, "value_922_Vmcl_1");
        access_922Test.Access_922MethodAfter("vimis.remd_prevention_sent_result", 2, "value_922_Vmcl_2");
        access_922Test.Access_922MethodAfter("vimis.remd_akineo_sent_result", 3, "value_922_Vmcl_3");
        access_922Test.Access_922MethodAfter("vimis.remd_cvd_sent_result", 4, "value_922_Vmcl_4");
        access_922Test.Access_922MethodAfter("vimis.remd_infection_sent_result", 5, "value_922_Vmcl_5");
    }

    @Order(3)
    @Issue(value = "TEL-937")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Хранение информации по пациентам ССЗ по скорой помощи")
    @Description("Проверка 937, на добавление в таблицу vimis.cvd_call_an_ambulance")
    public void AfterTests937 () throws IOException, SQLException, InterruptedException {
        Access_937Test access_937Test = new Access_937Test();
        access_937Test.Access_937After("value_937ID_107");
        access_937Test.Access_937After("value_937ID_116");
    }

    @Order(4)
    @Issue(value = "TEL-1100")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка отправки документа в РЭМД с отправкой только в ВИМИС")
    @Description("После прохождения тестов смотрим, что документ, который только в ВИМИС не добавился, а документ в ВИМИС и РЭМД добавился в таблицы vimis.remd_onko_sent_result/ vimis.remd_cvd_sent_result/ vimis.remd_akineo_sent_result/ vimis.remd_prevention_sent_result")
    public void AfterTests1100 () throws IOException, SQLException {
        Access_1100Test access_1100Test = new Access_1100Test();
        access_1100Test.Access_1100AfterMethod("value_1100_vmcl_1", "vimis.remd_onko_sent_result", false);
        access_1100Test.Access_1100AfterMethod("value_1100_vmcl_2", "vimis.remd_prevention_sent_result", false);
        access_1100Test.Access_1100AfterMethod("value_1100_vmcl_3", "vimis.remd_akineo_sent_result", false);
        access_1100Test.Access_1100AfterMethod("value_1100_vmcl_4", "vimis.remd_cvd_sent_result", false);
        access_1100Test.Access_1100AfterMethod("value_1100_vmcl_5", "vimis.remd_infection_sent_result", false);

        access_1100Test.Access_1100AfterMethod("value_1100_remd_1", "vimis.remd_onko_sent_result", true);
        access_1100Test.Access_1100AfterMethod("value_1100_remd_2", "vimis.remd_prevention_sent_result", true);
        access_1100Test.Access_1100AfterMethod("value_1100_remd_3", "vimis.remd_akineo_sent_result", true);
        access_1100Test.Access_1100AfterMethod("value_1100_remd_4", "vimis.remd_cvd_sent_result", true);
        access_1100Test.Access_1100AfterMethod("value_1100_remd_5", "vimis.remd_infection_sent_result", true);
        access_1100Test.Access_1100AfterMethod("value_1100_remd_99", "vimis.remd_sent_result", true);
    }

    @Order(5)
    @Issue(value = "TEL-1272")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Формирование запроса в КРЭМД только одно направления при отправке сразу нескольких vmcl")
    @Description("Отправляем смс по нескольким направлениям и проверяем, что for_send = true только у первого стоящего vmcl, и отправка в РЭМД идёт только у него")
    public void AfterTests1272 () throws IOException, SQLException, InterruptedException {
        Access_1272Test access_1272Test = new Access_1272Test();
        access_1272Test.AfterAccess_1272("vimis.remd_onko_sent_result", "value_1272_vmcl_1");
        access_1272Test.AfterAccess_1272("vimis.remd_prevention_sent_result", "value_1272_vmcl_2");
        access_1272Test.AfterAccess_1272("vimis.remd_akineo_sent_result", "value_1272_vmcl_3");
        access_1272Test.AfterAccess_1272("vimis.remd_cvd_sent_result", "value_1272_vmcl_4");
        access_1272Test.AfterAccess_1272("vimis.remd_infection_sent_result", "value_1272_vmcl_5");
    }

    @Order(6)
    @Issue(value = "TEL-1555")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Проверка добавления записи после успешного принятия документа ФРЭМД")
    @Description("Отправляем смс в РЭМД, добавляем  fremd_status = '1'. После отправяем смс в ВИМИС и РЭМД с таким же Local_uid, добавляем статус 1 в logs и после проверяем, что смс не добавилась в таблицы remd")
    public void AfterTests1555 () throws IOException, SQLException {
        Access_1555Test access_1555Test = new Access_1555Test();
        access_1555Test.Access_1555AfterMethod("value_1555_vmcl_1", "vimis.remd_onko_sent_result");
        access_1555Test.Access_1555AfterMethod("value_1555_vmcl_2", "vimis.remd_prevention_sent_result");
        access_1555Test.Access_1555AfterMethod("value_1555_vmcl_3", "vimis.remd_akineo_sent_result");
        access_1555Test.Access_1555AfterMethod("value_1555_vmcl_4", "vimis.remd_cvd_sent_result");
    }

    @Order(7)
    @Issue(value = "TEL-1616")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Повторная отправка смс с успешно отправленным local_uid в РЭМ")
    @Description("Отправляем смс - добавляем статус 1 в logs и fremd_status = 1 в remd, после отправляем смс с тем же local_uid и проверяем, что смс добавилось в таблицу logs, но не добавилась в таблицу remd")
    public void AfterTests1616 () throws IOException, SQLException, InterruptedException {
        Access_1616Test access_1616Test = new Access_1616Test();
        access_1616Test.Access_1616AfterMethod("value_1616_vmcl_1", "vimis.remd_onko_sent_result");
        access_1616Test.Access_1616AfterMethod("value_1616_vmcl_2", "vimis.remd_prevention_sent_result");
        access_1616Test.Access_1616AfterMethod("value_1616_vmcl_3", "vimis.remd_akineo_sent_result");
        access_1616Test.Access_1616AfterMethod("value_1616_vmcl_4", "vimis.remd_cvd_sent_result");
    }

    @Order(8)
    @Issue(value = "TEL-1786")
    @Issue(value = "TEL-1800")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Добавление данных диспансерных больных в таблицу по смс5")
    @Description("Отправить СЭМД смс5 с triggerPoint = 6, установить статус 1 в таблицу vimis.preventionlogs, проверить заполение теблицы vimis.prevention_sms_v5_register")
    public void AfterTests1786 () throws IOException, SQLException {
        Access_1786Test access_1786Test = new Access_1786Test();
        access_1786Test.Access_1786After("value_1786_Vmcl_2_tg6", "vimis.prevention_sms_v5_register");
        access_1786Test.Access_1786After("value_1786_Vmcl_2_tg12", "vimis.prevention_sms_v5_register");
    }

    @Order(9)
    @Issue(value = "TEL-1791")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отображение данных из регистра Профилактика")
    @Description("Создаём новый регистр с Профилактикой - переходим в данный регистр и проверяем добавленную запись из таблицы prevention_sms_v5_register")
    public void AfterTests1791 () throws IOException, InterruptedException, SQLException {
        Access_1791Test access_1791Test = new Access_1791Test();
        access_1791Test.Access_1791After("SizePatients_1791");
    }

    @Order(10)
    @Issue(value = "TEL-1880")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка документа 1 раз в КРЭМД")
    @Description("Отправить СЭМД - установить статус 1 в logs, дождаться смены статуса. Сменить статус колбэком КРЭМД, после проверить, что статус не смнился обратно фоновым сервисом")
    public void AfterTests1880 () throws IOException, SQLException, InterruptedException {
        Access_1880Test access_1880Test = new Access_1880Test();
        access_1880Test.Access_1880After("vimis.remd_onko_sent_result", "value_1880_1");
        access_1880Test.Access_1880After("vimis.remd_prevention_sent_result", "value_1880_2");
        access_1880Test.Access_1880After("vimis.remd_akineo_sent_result", "value_1880_3");
        access_1880Test.Access_1880After("vimis.remd_cvd_sent_result", "value_1880_4");
        access_1880Test.Access_1880After("vimis.remd_sent_result", "value_1880_99");
    }

    @Order(11)
    @Test
    @DisplayName("Удаляем записи от колбэка ВИМИС на тесте ХМАО")
    public void DeleteVimis () throws SQLException {
        if (KingNumber == 40) {
            String text = "'ок', 'Проверяем отправку смс 1', 'Проверка 603', 'Проверка уведомления 667', 'Проверяем отправку по заявке 811', 'Проверка тип уведомления 8 (847)', 'Проверка 904', 'Проверка 922', 'Проверка 927', 'Проверка 1009', 'Проверка 1076', 'Проверка 1097', 'Проверка 1100', 'Проверка 1103', 'Проверка Уведомлений по РЭМД (1156, 1525)', 'Проверка 1193', 'Проверка 1313', 'Проверка 1343', 'Проверка 1437', 'Проверка 1444', 'Проверка 1464', 'Проверка 1473', 'Проверка 1555', 'Проверка 1591', 'Проверка 1595', 'Проверка 1616', 'Проверка 1619', 'Проверка 1663', 'Проверка 1727', 'Проверка 1761', 'Проверка 1786', 'Проверка 1793', 'Проверка 1864', 'Проверка 1880', 'Проверка 1887', 'Проверка Уведомлений по 1957/1958'";
            sql.UpdateConnection("Delete from vimis.documentlogs where description in (" + text + ");");
            sql.UpdateConnection("Delete from vimis.preventionlogs where description in (" + text + ");");
            sql.UpdateConnection("Delete from vimis.akineologs where description in (" + text + ");");
            sql.UpdateConnection("Delete from vimis.cvdlogs where description in (" + text + ");");
            sql.UpdateConnection("Delete from vimis.infectionlogs where description in (" + text + ");");
        }
    }

    @Order(12)
    @Issue(value = "TEL-3100")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Добавление данных диспансерных больных в таблицу по смс 5 c triggerPoint = 12 по инфекции")
    @Description("Отправить СЭМД смс 5 vmcl = 5 с triggerPoint = 12, установить статус 1 в таблицу vimis.preventionlogs, проверить заполение теблицы vimis.infection_sms_v5_register")
    public void AfterTests3100 () throws IOException, SQLException {
        Access_3100Test access_3100Test = new Access_3100Test();
        access_3100Test.Access_3100After("value_3100_Vmcl_5_tg12", "vimis.infection_sms_v5_register");
    }

    @Order(13)
    @Issue(value = "TEL-3102")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Description("Создаём новый регистр с Инфекцией - переходим в данный регистр и проверяем добавленную запись из таблицы vimis.infection_sms_v5_register")
    @DisplayName("Отображение данных из регистра Инфекции")
    public void AfterTests3102 () throws IOException, InterruptedException {
        Access_3102Test access_3102Test = new Access_3102Test();
        access_3102Test.Access_3102After("SizePatients_3102");
    }

    @Order(14)
    @Issue(value = "TEL-3113")
    @Issue(value = "TEL-3114")
    @Issue(value = "TEL-3115")
    @Issue(value = "TEL-3116")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Disabled
    @Description("Отправляем смс по всем направлениям, добавляем медицинскую справку в таблицу vimis.patient_requests_for_doc - после фоновый сервис ")
    @DisplayName("Создание Мед справки")
    public void AfterTests3113 () throws InterruptedException, IOException, SQLException {
        Access_3113_Type14_Test access3113Type14Test = new Access_3113_Type14_Test();
        // Уведомелния приходят на последнее добавленные СЭМД, спустя како то время, и добавляется не тот который отправили, поэтому по уедомлению сложно определить нужное уведомление
        for (int i = 1; i < 6; i++) {
            access3113Type14Test.After_3113(i, ReadPropFile("ValueID_3113_" + i + "_3"),
                    ReadPropFile("ValueRequestID_3113_" + i + "_3"));
        }
        access3113Type14Test.After_3113(1, ReadPropFile("ValueID_3113_99_3"), ReadPropFile("ValueRequestID_3113_99_3"));

        for (int i = 1; i < 6; i++) {
            access3113Type14Test.After_3113(i, ReadPropFile("ValueID_3113_" + i + "_43"),
                    ReadPropFile("ValueRequestID_3113_" + i + "_43"));
        }
    }

    @Order(15)
    @Test
    @DisplayName("Данные в таблице Регистр Акинео")
    public void AfterTests1895 () throws SQLException, IOException, InterruptedException {
        Access_1898Test access1898Test = new Access_1898Test();
        access1898Test.After_1895("List_1895", "Id_1895", "localUid_1895");
    }

    @Order(16)
    @Test
    @DisplayName("Проверка добавления данных в таблицы парсинга")
    public void AfterTests3447 () throws SQLException, IOException, InterruptedException {
        Access_3447Test access3447Test = new Access_3447Test();
        for (int i = 1; i < 6; i++) {
            access3447Test.Method_3447_After("SMS/SMSV2-(id=2)-vmcl=1.xml", i, ReadPropFile("Vmcl_" + i + "_3447"));
        }
    }

    @Order(17)
    @Test
    @DisplayName("Проверка что блок вимис не отображается в Лк Врача, а также не отображаеются документы")
    public void AfterTests_4111 () throws InterruptedException {
        Access_4311Test access4311Test = new Access_4311Test();
        access4311Test.After_4311();
    }

    @Order(49)
    @Test
    @DisplayName("Повторно запускаем настройку контура")
    public void AfterSettings () throws IOException, SQLException, InterruptedException {
        Authorization authorization = new Authorization();
        authorization.BeforeData();
    }

    @Order(50)
    @Test
    @DisplayName("Метод нужен для того, чтобы из нескольких файлов с упавшими тестами взять тесты и поместить в один")
    public void AfterFiledCopyTest () throws IOException {
        String path = "";
        if (TextUtils.isEmpty(remote_url_chrome)) {
            path = "ignored/FiledTests.bat";
        } else {
            path = "ignored/FiledTests.sh";
        }
        String content = null;
        try {
            for (int i = 1; i < 15; i++) {
                content = new String(Files.readAllBytes(Paths.get("src/test/resources/test" + i + ".txt")));
                FileWriter writer = new FileWriter("src/test/resources/" + path + "", true);
                BufferedWriter bufferWriter = new BufferedWriter(writer);
                bufferWriter.write(content);
                bufferWriter.close();
            }
        } catch (NoSuchFileException e) {
            throw new IllegalStateException("Ошибочка - " + e.getMessage());
        }
    }

    @Order(51)
    @Test
    @DisplayName("Редактируем файл запуска упавших тестов")
    public void AfterFiledTest () throws IOException {
        String path = "";
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                path = "ignored/FiledTests.bat";
            } else {
                path = "ignored/FiledTests.sh";
            }

            String str = new String(
                    Files.readAllBytes(Paths.get("src/test/resources/" + path + ""))).replace("mvn test -Dtest=, ", "");

            String modifiedText = Arrays.stream(str.split(" "))
                    .reduce("", (result, word) -> result.contains(word) ? result : result + " " + word);

            String modifiedText1 = modifiedText.replace("mvn test -Dtest= ", "mvn test -Dtest=").substring(
                    1);

            new FileWriter("src/test/resources/" + path + "", false).close();

            FileWriter writer = new FileWriter("src/test/resources/" + path + "", true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            if (TextUtils.isEmpty(remote_url_chrome)) {
                bufferWriter.write("mvn test -Dtest=\"" + modifiedText1 + "\"");
            } else {
                bufferWriter.write(
                        "mvn test -Dtest=\"" + modifiedText1 + "\" -DUrlChrome=" + remote_url_chrome + " -DContour=" + KingNumber + "");
            }
            bufferWriter.close();
        }
        String text1 = new String(Files.readAllBytes(Paths.get("src/test/resources/" + path + "")));
        System.out.println("Данные из файла с упавшими тестами - " + text1);
    }
}
