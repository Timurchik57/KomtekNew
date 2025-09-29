package api.Access_Notification;

import Base.TestListener;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Уведомление с типом 17")
@Tag("Оповещение")
@Tag("api/smd")
@Tag("Проверка_БД")
@Tag("Дополнительные_параметры")
@Tag("МО_прикрепления")
public class Access_3412_Type17_Test extends BaseAPI {

    public String body;
    public String content;
    public String encodedString;
    public String activeCall;
    public boolean guidAttachment;
    String Guid;
    /** Гуиды у которых есть прикрепелённые МО
     * @2f1647ea-8c01-49f6-95ec-4d5cb6c8d1b6 - 1.2.643.5.1.13.13.12.2.86.8902
     * @c98bfe00-60fc-42ac-ab12-fbe5a6e780d8 - 1.2.643.5.1.13.13.12.2.86.8902, 1.2.643.5.1.13.13.12.2.86.8966
     * */

    @Test
    @Order(1)
    @DisplayName("Добавляем тип оповещения в зависимости от МО которая прикреплена к гуиду пациента")
    public void Access_3534_0() throws IOException, SQLException {
        if (KingNumber == 11) {
            MO = "1.2.643.5.1.13.13.12.2.1.24";
            Guid = "5847a980-78a6-4afa-80db-3898356c67cf";
            AddressNotification = "https://12345.requestcatcher.com/test";
            SystemId = "18";
            Password = "DE95D8FB9B519DC4AA3D94C11CBD8F72";

            for (int i = 0; i < 2; i++) {
                /** Добавляем 2 МО, чтобы проверить что уведомление приходит, только по 1 */
                if (i == 1) {
                    MO = "1.2.643.5.1.13.13.12.2.86.8986";
                }
                sql.StartConnection("select count(*) from vimis.mis_address a\n" +
                        "join dpc.mis_sp_mu m on a.medicalidmu = m.medicalidmu  where address = '" + AddressNotification + "' and m.\"oid\"  = '" + MO + "' and action_type_id != '12' and action_type_id != '16' and action_type_id = '17' and centralized_unloading_system_id != '43';");
                while (sql.resultSet.next()) {
                    sql.value = sql.resultSet.getString("count");
                    System.out.println(sql.value);
                }
                if (sql.value.equals("0")) {
                    Api(HostAddress + "/auth.svc", "post", null, null,
                            "{\n" +
                                    "    \"Username\": \"" + MO + "\",\n" +
                                    "    \"SystemId\": " + SystemId + ",\n" +
                                    "    \"Password\": \"" + Password + "\"\n" +
                                    "}", 200, false);

                    Token = Response.getString("Result.Value");
                    Api(HostAddress + "/api/smd/misaddress", "post", null, null,
                            "{\n" +
                                    "  \"address\": \"" + AddressNotification + "\",\n" +
                                    "  \"actionTypeId\": \"17\"\n" +
                                    "}", 200, false);
                }
            }
        }
    }

    @Issue(value = "TEL-3412")
    @Issue(value = "TEL-3413")
    @Issue(value = "TEL-3638")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @DisplayName("Отправка смс с activeCall = true id = 107 vmcl = 99")
    @Description("Отправить смс с activeCall, проверить добавление значения в таблицы и уведомление")
    public void Access_3412_true() throws IOException, SQLException, InterruptedException {
        Access_3412Method("SMS/SMS18-id=107.xml", "107", 99, 0, 0, 1, 9,
                18, 1, 57, 21);
    }

    @Test
    @DisplayName("Отправка смс с activeCall = true id = 3 vmcl = 1")
    public void Access_3412_3_1() throws IOException, SQLException, InterruptedException {
        Access_3412Method("SMS/SMS3.xml", "3", 1, 2, 3, 1, 9,
                18, 1, 57, 21);
    }

    @Test
    @DisplayName("Отправка смс с activeCall = true id = 3 vmcl = 2")
    public void Access_3412_3_2() throws IOException, SQLException, InterruptedException {
        Access_3412Method("SMS/SMS3.xml", "3", 2, 2, 3, 1, 9,
                18, 1, 57, 21);
    }

    @Test
    @DisplayName("Отправка смс с activeCall = true id = 3 vmcl = 3")
    public void Access_3412_3_3() throws IOException, SQLException, InterruptedException {
        Access_3412Method("SMS/SMS3.xml", "3", 3, 2, 2, 1, 9,
                18, 1, 57, 21);
    }

    @Test
    @DisplayName("Отправка смс с activeCall = true id = 3 vmcl = 4")
    public void Access_3412_3_4() throws IOException, SQLException, InterruptedException {
        Access_3412Method("SMS/SMS3.xml", "3", 4, 2, 2, 1, 9,
                18, 1, 57, 21);
    }

    @Test
    @DisplayName("Отправка смс с activeCall = true id = 3 vmcl = 5")
    public void Access_3412_3_5() throws IOException, SQLException, InterruptedException {
        Access_3412Method("SMS/SMS3.xml", "3", 5, 2, 3, 1, 9,
                18, 1, 57, 21);
    }

    @Step("Метод отправки смс с activeCall")
    public void Access_3412Method(String FileName, String DocType, Integer vmcl, Integer triggerPoint, Integer docTypeVersion, Integer Role, Integer position, Integer speciality, Integer Role1, Integer position1, Integer speciality1) throws InterruptedException, IOException, SQLException {

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        Thread.sleep(1500);
        Integer num = 0;

        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                System.out.println("\n1 заход");
                PatientGuid = "4743e15e-488a-44c6-af50-dff0778dd01a"; //Нет МО прикрепления
                guidAttachment = false;
                activeCall = "true";
                num = 1;
            }
            if (i == 1) {
                System.out.println("\n2 заход");
                PatientGuid = "4743e15e-488a-44c6-af50-dff0778dd01a";//Нет МО прикрепления
                guidAttachment = false;
                activeCall = "false";
                num = 2;
            }
            if (i == 2)  {
                System.out.println("\n3 заход");
                PatientGuid = "2f1647ea-8c01-49f6-95ec-4d5cb6c8d1b6";//Есть МО прикрепления
                guidAttachment = true;
                activeCall = "true";
                num = 2;
            }
            if (i == 3)  {
                System.out.println("\n4 заход");
                PatientGuid = "2f1647ea-8c01-49f6-95ec-4d5cb6c8d1b6";//Есть МО прикрепления
                guidAttachment = true;
                activeCall = "null";
                num = 2;
            }
            xml.XmlStart(FileName, Departmen, MO, PatientGuid, NameMO, Snils, num);

            String textVmxl;
            if (vmcl != 99) {
                textVmxl = "{\n" +
                        "                    \"vmcl\": " + vmcl + ",\n" +
                        "                    \"triggerPoint\": " + triggerPoint + ",\n" +
                        "                    \"docTypeVersion\": " + docTypeVersion + "\n" +
                        "                    \n" +
                        "                }";
            } else {
                textVmxl = "{\n" +
                        "                    \"vmcl\": " + vmcl + "\n" +
                        "                }";
            }
            body = "{\n" +
                    "    \"PatientGuid\":\"" + PatientGuid + "\",\n" +
                    "    \"DocType\":\"" + DocType + "\",\n" +
                    "    \"activeCall\": " + activeCall + ",\n" +
                    "    \"DocContent\":\n" +
                    "    {\n" +
                    "        \"Document\":\"" + xml.encodedString + "\",\n" +
                    "        \"CheckSum\":" + xml.CheckSum + "\n" +
                    "        },\n" +
                    "        \"LocalUid\":\"" + xml.uuid + "\",\n" +
                    "        \"Payment\":1,\n" +
                    "        \"ReasonForAbsenceIdcase\":\n" +
                    "        {\n" +
                    "            \"CodeSystemVersion\":\"1.1\",\n" +
                    "            \"Code\":10,\n" +
                    "            \"CodeSystem\":\"1.2.643.5.1.13.13.99.2.286\"\n" +
                    "        },\n" +
                    "            \"VMCL\":\n" +
                    "            [" + textVmxl + "],\n" +
                    "            \"OrgSignature\":\n" +
                    "            {\n" +
                    "                \"Data\":\"" + xml.Signatures + "\",\n" +
                    "                \"CheckSum\":" + xml.CheckSumSign + "\n" +
                    "                },\n" +
                    "                \"PersonalSignatures\":\n" +
                    "                [\n" +
                    "                    {\n" +
                    "                        \"Signer\":\n" +
                    "                        {\n" +
                    "                            \"LocalId\":\"0001510378\",\n" +
                    "                            \"Role\":{\"$\":\"" + Role + "\",\"@version\":\"2.4\"},\n" +
                    "                            \"LastName\":\"Коситченков\",\n" +
                    "                            \"FirstName\":\"Андрей\",\n" +
                    "                            \"MiddleName\":\"Александрович\",\n" +
                    "                            \"Snils\":\"18259202174\",\n" +
                    "                            \"Position\":{\"$\":\"" + position + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                            \"Speciality\":{\"$\":" + speciality + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                            \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                        },\n" +
                    "                            \"Signature\":\n" +
                    "                            {\n" +
                    "                                \"Data\":\"" + xml.Signatures + "\",\n" +
                    "                                  \"CheckSum\":" + xml.CheckSumSign + "\n" +
                    "                            },\n" +
                    "                            \"Description\":\"Подпись сотрудника\"\n" +
                    "                            },\n" +
                    "                            {\n" +
                    "                                \"Signer\":\n" +
                    "                                {\n" +
                    "                                    \"LocalId\":\"0003948083\",\n" +
                    "                                    \"Role\":{\"$\":\"" + Role1 + "\",\"@version\":\"2.4\"},\n" +
                    "                                    \"LastName\":\"Стрекнев\",\n" +
                    "                                    \"FirstName\":\"Денис\",\n" +
                    "                                    \"MiddleName\":\"Юрьевич\",\n" +
                    "                                    \"Snils\":\"18287265004\",\n" +
                    "                                    \"Position\":{\"$\":\"" + position1 + "\",\"@version\":\"" + Position + "\"},\n" +
                    "                                    \"Speciality\":{\"$\":" + speciality1 + ",\"@version\":\"" + Speciality + "\"},\n" +
                    "                                    \"Department\":\"1.2.643.5.1.13.13.12.2.86.8986.0.536268\"\n" +
                    "                                },\n" +
                    "                                    \"Signature\":\n" +
                    "                                    {\n" +
                    "                                        \"Data\":\"" + xml.Signatures + "\",\n" +
                    "                                       \"CheckSum\":" + xml.CheckSumSign + "\n" +
                    "                                        },\n" +
                    "                                        \"Description\":\"Подпись сотрудника\"\n" +
                    "                                    }\n" +
                    "        ]\n" +
                    "    }";

            Api("" + HostAddress + "/api/smd", "post", null, null, body, 200, true);
            Response.getString("result[0].message").contains("успешно опубликован");
            String TransferId = Response.getString("result[0].transferId");

            TableVmcl(vmcl);

            sql.StartConnection("SELECT * FROM " + smsBase + "  where local_uid = '" + xml.uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("active_call");
            }

            if (guidAttachment) {
                System.out.println(
                        "Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            } else {
                System.out.println(
                        "Переходим на сайт для перехвата сообщений и проверяем, что оповещение не пиршло");
            }
            Thread.sleep(1500);
            ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
            NotificationsTrue(17, TransferId, vmcl);
            System.out.println(xml.uuid);
            System.out.println(TEXT);

            if (activeCall.equals("true")) {
                Assertions.assertEquals(sql.value, "t",
                        "Параметр active_call не добавлился в таблицу " + smsBase + " со значением 1");

                // Оповещение придёт если у patientguid есть мо прикрепления, и это МО совпадает с МО, указанное в уведомлении
                if (guidAttachment) {
                    Assertions.assertTrue(TEXT.contains("\"PatientGuid\":\"" + PatientGuid.toLowerCase() + "\""),
                            "Оповещение для vmcl = " + vmcl + " не добавилось");
                    Assertions.assertTrue(TEXT.contains("\"LocalUid\":\"" + xml.uuid + "\""),
                            "Оповещение для vmcl = " + vmcl + " не добавилось");
                    Assertions.assertTrue(TEXT.contains("\"CallDate\":"),
                            "Оповещение для vmcl = " + vmcl + " не добавилось");
                } else {
                    System.out.println("Не будет уведомления, так как нет МО прикрепления");
                }
            } else if (activeCall.equals("false")) {
                Assertions.assertEquals(sql.value, "f", "Параметр active_call не добавлился в таблицу " + smsBase + " со значением 0");
            } else {
                Assertions.assertNull(sql.value, "Параметр active_call в таблице " + smsBase + " должен быть равен null");
            }
            xml.ReplacementWordInFileBack(FileName);
        }
    }
}
