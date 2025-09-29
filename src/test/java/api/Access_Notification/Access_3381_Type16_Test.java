package api.Access_Notification;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Уведомление с типом 16")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("Вимис_статус")
@Tag("Крэмд_статус")
@Tag("Фрэмд_статус")
@Tag("Проверка_БД")
@Tag("МО_прикрепления")
public class Access_3381_Type16_Test extends BaseAPI {

    public String TransferId;
    public String Id;
    public String MORRP1;
    public String MORRP2;
    public String MORRP3;
    public Integer number = 0;

    /**
     Гуиды у которых есть МО прикрепления:
     2f1647ea-8c01-49f6-95ec-4d5cb6c8d1b6 (тип 1), мо - 1.2.643.5.1.13.13.12.2.86.8902
     c98bfe00-60fc-42ac-ab12-fbe5a6e780d8 (тип 4), мо - 1.2.643.5.1.13.13.12.2.86.8966
     Для типа 4 увведомления нет
     */

    @Issue(value = "TEL-3381")
    @Issue(value = "TEL-3886")
    @Link(name = "ТМС-2055", url = "https://team-1okm.testit.software/projects/5/tests/2055?isolatedSection=af882452-d09a-4051-af99-3d04bf599188")
    @Owner(value = "Галиакберов Тимур")
    @Description("Добавить уведомление с типом 16 для всех vmcl - отправть смс по всем направлениям - проверить уведомление")
    @Test
    @Story("Смс в ВИМИС и РЭМД")
    @DisplayName("Уведомление с типом 16 для vmcl = 1")
    public void Access_1() throws IOException, SQLException, InterruptedException {
        Access_3381Method("SMS/SMS3.xml", "3", 1, 3, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @Story("Смс в ВИМИС и РЭМД")
    @DisplayName("Уведомление с типом 16 для vmcl = 2")
    public void Access_2() throws IOException, SQLException, InterruptedException {
        Access_3381Method("SMS/SMS3.xml", "3", 2, 3, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @Story("Смс в ВИМИС и РЭМД")
    @DisplayName("Уведомление с типом 16 для vmcl = 3")
    public void Access_3() throws IOException, SQLException, InterruptedException {
        Access_3381Method("SMS/SMS3.xml", "3", 3, 2, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @Story("Смс в ВИМИС и РЭМД")
    @DisplayName("Уведомление с типом 16 для vmcl = 4")
    public void Access_4() throws IOException, SQLException, InterruptedException {
        Access_3381Method("SMS/SMS3.xml", "3", 4, 2, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @Story("Смс в ВИМИС и РЭМД")
    @DisplayName("Уведомление с типом 16 для vmcl = 5")
    public void Access_5() throws IOException, SQLException, InterruptedException {
        Access_3381Method("SMS/SMS3.xml", "3", 5, 3, 1, 9, 18, 1, 57, 21);
    }

    @Test
    @Story("Смс в ВИМИС и РЭМД")
    @DisplayName("Уведомление с типом 16 для vmcl = 99")
    public void Access_99() throws IOException, SQLException, InterruptedException {
        Access_3381Method("SMS/SMS3.xml", "3", 99, 3, 1, 9, 18, 1, 57, 21);
    }

    @Step("Метод отправки смс: {0}, vmcl = {3}")
    public void Access_3381Method(String FileName, String DocType, Integer vmcl, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws IOException, InterruptedException, SQLException {

        String File = null;
        String doctype = null;
        TableVmcl(vmcl);

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        Date = formatForDateNow.format(date);

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);

        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                /** Отправляем смс в ВИМИС и РЭМД, смотрим, что нет уведомления после успеха ВИМИС, но есть после успеха РЭМД */
                File = FileName;
                doctype = DocType;
                number = 1;
                PatientGuid = "2f1647ea-8c01-49f6-95ec-4d5cb6c8d1b6";
                System.out.println("\n 1 цикл + " + File + " PatientGuid - " + PatientGuid + " vmcl - " + vmcl);
            }
            if (i == 1) {
                /** Отправляем смс в ВИМИС, смотрим, что есть после успеха ВИМИС */
                File = "SMS/SMS2-id=42.xml";
                doctype = "42";
                number = 2;
                PatientGuid = "2f1647ea-8c01-49f6-95ec-4d5cb6c8d1b6";
                System.out.println("\n 2 цикл + " + File + " PatientGuid - " + PatientGuid + " vmcl - " + vmcl);
            }
            if (i == 2) {
                /** Отправляем смс в ВИМИС и РЭМД, но с гуидом, у которого нет прикрепления, смотрим, что нет уведомления после успеха ВИМИС и после успеха РЭМД */
                File = FileName;
                doctype = DocType;
                number = 2;
                PatientGuid = "4743e15e-488a-44c6-af50-dff0778dd01a";
                System.out.println("\n 3 цикл + " + File + " PatientGuid - " + PatientGuid + " vmcl - " + vmcl);
            }
            if (i == 3) {
                /** Отправляем смс в ВИМИС, но с гуидом, у которого нет прикрепления, смотрим, что нет уведомления после успеха ВИМИС и после успеха РЭМД */
                File = "SMS/SMS2-id=42.xml";
                doctype = "42";
                number = 2;
                PatientGuid = "4743e15e-488a-44c6-af50-dff0778dd01a";
                System.out.println("\n 4 цикл + " + File + " PatientGuid - " + PatientGuid + " vmcl - " + vmcl);
            }
            if (i == 4) {
                /** Отправляем смс в ВИМИС и РЭМД с гуидом у которого есть МО прикрепления = null (сейчас у него 2 прикрепления, одно без МО, другое с типом 4), смотрим, что не добавляются данные в таблицы */
                File = FileName;
                doctype = DocType;
                number = 2;
                PatientGuid = "4153EE1D-0C57-6694-E055-000000000001";
                System.out.println("\n 5 цикл + " + File + " PatientGuid - " + PatientGuid + " vmcl - " + vmcl);
            }
            if (i == 0 || i == 1 & vmcl != 99 ) {
                System.out.println("Отправляем смс с id = " + doctype + " и vmcl = " + vmcl + "");
                xml.ApiSmd(File, doctype, vmcl, number, true, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);

                /** Узнаём количество прикреплённых документов */
                Integer Count = SearchRRP(PatientGuid);

                sql.StartConnection("Select * from " + smsBase + " where local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    TransferId = sql.resultSet.getString("transfer_id");
                    System.out.println(sql.value);
                }

                List<String> list = new ArrayList<>();
                String str = null;
                sql.StartConnection("select * from vimis.mo_attachments a\n" +
                        "join " + attachmentsBase + " sm on a.id = sm.attachment_id\n" +
                        "where sm.sms_id = '" + sql.value + "' and patient_guid = '" + PatientGuid + "';");
                while (sql.resultSet.next()) {
                    str = sql.resultSet.getString("patient_guid");
                    list.add(str);
                }

                Assertions.assertEquals(Count, list.size(),
                            "Должно добавиться " + Count + " записи в " + attachmentsBase + "");

                if (vmcl != 99) {
                    CollbekVimis("" + xml.uuid + "", "1", "Проверка 3381", smsBase, vmcl);

                    if (File.equals("SMS/SMS3.xml")) {

                        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение не пришло");
                        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
                        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
                        NotificationsTrue(16, TransferId, vmcl);
                        System.out.println(TEXT);
                        Assertions.assertFalse(TEXT.contains("\"AttachmentType\"") &&
                                TEXT.contains("\"TransferId\":\"" + TransferId + "\""));

                        WaitStatusKremd(remdBase, "" + xml.uuid + "");
                        CollbekKremd("" + xml.uuid + "", "success", "Проверка уведомления 16 (3381)", remdBase);
                        accessType16(vmcl, File);
                    } else {
                        accessType16(vmcl, File);
                    }
                }

                if (File.equals("SMS/SMS3.xml") & vmcl == 99) {
                    WaitStatusKremd(remdBase, "" + xml.uuid + "");
                    CollbekKremd("" + xml.uuid + "", "success", "Проверка уведомления 16 (3381)", remdBase);
                    Thread.sleep(1500);
                    accessType16(vmcl, File);
                }
                xml.ReplacementWordInFileBack(File);
            }

            if (i == 2 || i == 3 & vmcl != 99 || i == 4 ) {
                System.out.println("Отправляем смс с id = " + doctype + " и vmcl = " + vmcl + "");
                xml.ApiSmd(File, doctype, vmcl, number, true, docTypeVersion, Role, position, speciality, Role1, position1, speciality1);

                /** Узнаём количество прикреплённых документов */
                Integer Count = SearchRRP(PatientGuid);

                sql.StartConnection("Select * from " + smsBase + " where local_uid = '" + xml.uuid + "';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("id");
                    TransferId = sql.resultSet.getString("transfer_id");
                    System.out.println(sql.value);
                }

                List<String> list = new ArrayList<>();
                String str = null;
                sql.StartConnection("select * from vimis.mo_attachments a\n" +
                        "join " + attachmentsBase + " sm on a.id = sm.attachment_id\n" +
                        "where sm.sms_id = '" + sql.value + "' and patient_guid = '" + PatientGuid + "';");
                while (sql.resultSet.next()) {
                    str = sql.resultSet.getString("patient_guid");
                    list.add(str);
                }

                Assertions.assertEquals(Count, list.size(),
                        "Должно добавиться " + Count + " записи в " + attachmentsBase + "");

                if (vmcl != 99) {
                    CollbekVimis("" + xml.uuid + "", "1", "Проверка 3381", smsBase, vmcl);

                    System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение не пришло");
                    ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
                    NotificationsTrue(16, TransferId, vmcl);
                    System.out.println(TEXT);
                    Assertions.assertFalse(TEXT.contains("\"AttachmentType\"") &&
                            TEXT.contains("\"TransferId\":\"" + TransferId + "\""));
                    if (File.equals("SMS/SMS3.xml")) {
                        WaitStatusKremd(remdBase, "" + xml.uuid + "");
                        CollbekKremd("" + xml.uuid + "", "success", "Проверка уведомления 16 (3381)", remdBase);

                        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение не пришло");
                        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
                        NotificationsTrue(16, TransferId, vmcl);
                        System.out.println(TEXT);
                        Assertions.assertFalse(TEXT.contains("\"AttachmentType\"") &&
                                TEXT.contains("\"TransferId\":\"" + TransferId + "\""));
                    }
                }

                if (File.equals("SMS/SMS3.xml") & vmcl == 99) {
                    WaitStatusKremd(remdBase, "" + xml.uuid + "");
                    CollbekKremd("" + xml.uuid + "", "success", "Проверка уведомления 16 (3381)", remdBase);

                    System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение не пришло");
                    ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
                    WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
                    NotificationsTrue(16, TransferId, vmcl);
                    System.out.println(TEXT);
                    Assertions.assertFalse(TEXT.contains("\"AttachmentType\"") &&
                            TEXT.contains("\"TransferId\":\"" + TransferId + "\""));
                }
                xml.ReplacementWordInFileBack(File);
            }
        }
    }

    @Step("Метод чтобы узнать количество прикреплённых документов в РРП")
    public Integer SearchRRP(String guid) throws IOException {

        System.out.println("Авторизуемся в РРП через АПИ");
        RRPConnect();
        String body = "{\n" +
                "    \"UserName\": \"" + LoginRRPApi + "\",\n" +
                "    \"Password\": \"" + PasswordRRPApi + "\"\n" +
                "}";
        Api(UrlRRPApi + "/api/authenticate", "post", null, null, body, 200, false);
        InputPropFile("TokenRRP", Response.getString("access_token"));
        Token = ReadPropFile("TokenRRP");

        String params[] = {
                "guid", "" + guid + "",
                "isActual", "true"};
        Api(UrlRRPApi + "/api/patient-attachment/", "get", params, null, "", 200, false);

        Token = TokenInit();
        Integer num = 0;
        String str = "str";
        String type = "";

        while (!TextUtils.isEmpty(str)) {
            str = Response.getString("[" + num + "].mo");
            type = Response.getString("[" + num + "].type");
            System.out.println("МО прикрепления - " + str);
            if (!TextUtils.isEmpty(str)) {
                num++;
                if (type.equals("1")) {
                    MORRP1 = str;
                }
                if (type.equals("2")) {
                    MORRP2 = str;
                }
                if (type.equals("3")) {
                    MORRP3 = str;
                }
            }
        }
        System.out.println(num);
        return num;
    }

    @Step("Проверки уведомления по типу 16")
    public void accessType16(Integer vmcl, String File) throws InterruptedException {
        Thread.sleep(2500);
        System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
        ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
        WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
        NotificationsTrue(16, TransferId, vmcl);
        System.out.println(TEXT);
        Assertions.assertTrue(TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                "Для оповещения vmcl = " + vmcl + " с типом 16 нет параметра LocalUid");
        Assertions.assertTrue(TEXT.contains("\"TransferId\":\"" + TransferId + "\""),
                "Для оповещения vmcl = \" + vmcl + \" с типом 16 нет параметра TransferId");
        Assertions.assertTrue(TEXT.contains("\"PatientGuid\":\"" + PatientGuid + "\""),
                "Для оповещения vmcl = \" + vmcl + \" с типом 16 нет параметра PatientGuid");
        Assertions.assertTrue(TEXT.contains("\"DocType\""),
                "Для оповещения vmcl = \" + vmcl + \" с типом 16 нет параметра DocType");

        if (TEXT.contains("\"AttachmentType\":1")) {
            Assertions.assertTrue(TEXT.contains("\"MoOid\":\"" + MORRP1 + ""),
                    "Для оповещения vmcl = " + vmcl + " с типом 16 нет параметра MoOid = " + MORRP1 + "");
        }
        if (TEXT.contains("\"AttachmentType\":2")) {
            Assertions.assertTrue(TEXT.contains("\"MoOid\":\"" + MORRP2 + ""),
                    "Для оповещения vmcl = " + vmcl + " с типом 16 нет параметра MoOid = " + MORRP2 + "");
        }
        if (TEXT.contains("\"AttachmentType\":3")) {
            Assertions.assertTrue(TEXT.contains("\"MoOid\":\"" + MORRP3 + ""),
                    "Для оповещения vmcl = " + vmcl + " с типом 16 нет параметра MoOid = " + MORRP3 + "");
        }

        if (File.equals("SMS/SMS2-id=42.xml")) {
            /** Берётся текущая дата, так как это колбэк ВИМИС */
            Assertions.assertTrue(TEXT.contains("\"ResultDate\":\"" + Date + ""),
                    "Для оповещения vmcl = \" + vmcl + \" с типом 16 нет параметра MoOid");
        } else {
            /** Дата берётся из Колбэка КРЭМД */
            Assertions.assertTrue(TEXT.contains("\"ResultDate\":\"2022-08-30T00:00:00\""),
                    "Для оповещения vmcl = \" + vmcl + \" с типом 16 нет параметра MoOid");
        }
    }
}