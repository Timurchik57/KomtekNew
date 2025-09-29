package api.Access_Notification;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@Feature("Уведомление с типом 18")
@Tag("Оповещение")
@Tag("СМП")
@Tag("Проверка_БД")
@Tag("Дополнительные_параметры")
@Tag("МО_прикрепления")
public class Access_3534_Type18_Test extends BaseAPI {
    String Mo;
    String StreetGuid;
    String PatientGuid_3534;
    String SystemId;
    String Password;
    String id;
    String birthDate;
    Boolean Old = false;
    Boolean notification = true;
    public static boolean OneNotification;

    @Test
    @Order(1)
    @DisplayName("Добавляем тип оповещения в зависимости от МО которая прикреплена к гуиду пациента")
    public void Access_3534_0() throws IOException, SQLException {
        if (KingNumber == 11) {
            Mo = "1.2.643.5.1.13.13.12.2.1.24";
            PatientGuid_3534 = "5847a980-78a6-4afa-80db-3898356c67cf";
            AddressNotification = "https://12345.requestcatcher.com/test";
            SystemId = "18";
            Password = "DE95D8FB9B519DC4AA3D94C11CBD8F72";

            sql.StartConnection("select count(*) from vimis.mis_address a\n" +
                    "join dpc.mis_sp_mu m on a.medicalidmu = m.medicalidmu  where address = '" + AddressNotification + "' and m.\"oid\"  = '" + Mo + "' and action_type_id != '12' and action_type_id != '16' and action_type_id = '18' and centralized_unloading_system_id != '43';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("count");
                System.out.println(sql.value);
            }
            if (sql.value.equals("0")) {
                Api(HostAddress + "/auth.svc", "post", null, null,
                        "{\n" +
                                "    \"Username\": \"" + Mo + "\",\n" +
                                "    \"SystemId\": " + SystemId + ",\n" +
                                "    \"Password\": \"" + Password + "\"\n" +
                                "}", 200, false);

                Token = Response.getString("Result.Value");
                Api(HostAddress + "/api/smd/misaddress", "post", null, null,
                        "{\n" +
                                "  \"address\": \"" + AddressNotification + "\",\n" +
                                "  \"actionTypeId\": \"18\"\n" +
                                "}", 200, false);
            }
        }
    }

    @Issue(value = "TEL-3534")
    @Issue(value = "TEL-3595")
    @Issue(value = "TEL-3971")
    @Issue(value = "TEL-4040")
    @Issue(value = "TEL-4041")
    @Issue(value = "TEL-4042")
    @Issue(value = "TEL-4043")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Order(2)
    @Link(name = "ТМС-1875", url = "https://team-1okm.testit.software/projects/5/tests/2152?isolatedSection=af882452-d09a-4051-af99-3d04bf599188")
    @DisplayName("Отправка неотложной помощи urgent-smp")
    @Description("Отправить нужную справку, проверить добавление значения в таблицы и уведомление")
    public void Access_3534_1() throws IOException, SQLException, InterruptedException {
        Old = true;
        Access_3534Method("urgent-smp");
    }

    @Test
    @Order(3)
    @DisplayName("Отправка неотложной помощи signal-smp старый вариант по заявке 3534")
    public void Access_3534_2() throws IOException, SQLException, InterruptedException {
        Old = true;
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(4)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = null, birthdate >= 18 - есть уведомление")
    public void Access_3534_3() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = "2000-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = null where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(5)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 1, birthdate >= 18 - нет уведомления")
    public void Access_3534_4() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        notification = false;
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = "2000-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '1' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(6)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 10, birthdate >= 18 - нет уведомления")
    public void Access_3534_5() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        notification = false;
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = "2000-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '10' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(7)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 0, birthdate >= 18 - нет уведомления")
    public void Access_3534_6() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        notification = false;
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = "2000-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '0' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(8)
    @Issue(value = "TEL-4102")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 2, birthdate >= 18 - есть уведомление")
    public void Access_4102__() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = "2000-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '2' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(9)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 1, birthdate < 18 - есть уведомление")
    public void Access_3534_7() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = "2020-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '1' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(10)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = null, birthdate < 18 - есть уведомление")
    public void Access_3534_8() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        // Заменя через StreetCode нужна для того, чтобы исключить уведомление по другой МО. Если не обновим нужное значение only_children у всех нужных street_code, то может получиться так, что уведомление не придёт только по нужной МО, а по другой придёт и тест упадёт
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = "2020-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = null where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(11)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 0, birthdate < 18 - нет уведомления")
    public void Access_3534_9() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        notification = false;
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = "2020-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '0' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(12)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 10, birthdate < 18 - нет уведомления")
    public void Access_4102_3() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        notification = false;
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = "2020-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '10' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(13)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 0, birthdate >= 18, У пациента есть Мо прикрепления - нет уведомления")
    public void Access_3534_10() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        notification = false;
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        PatientGuid = "e771fa4b-106c-4237-a18d-0f9e244e9ef1";
        birthDate = "2000-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '0' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(14)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 2, birthdate >= 18, У пациента есть Мо прикрепления - есть уведомление")
    public void Access_3534_11() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        PatientGuid = "e771fa4b-106c-4237-a18d-0f9e244e9ef1";
        birthDate = "2000-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '2' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(15)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = null, birthdate >= 18, У пациента есть Мо прикрепления - есть уведомление")
    public void Access_4102_4() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        PatientGuid = "e771fa4b-106c-4237-a18d-0f9e244e9ef1";
        birthDate = "2000-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = null where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(16)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 1, birthdate >= 18, У пациента есть Мо прикрепления - уведомления нет")
    public void Access_3534_12() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        notification = false;
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        PatientGuid = "e771fa4b-106c-4237-a18d-0f9e244e9ef1";
        birthDate = "2000-10-31";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '1' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(17)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 1, birthdate = 18 - уведомления нет")
    public void Access_3534_13() throws IOException, SQLException, InterruptedException {
        //высчитываем нужную дату - ровно 18 лет назад
        SetDate(0, -216);

        // Дополнительная логика по заявке 4049
        notification = false;
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = Year + "-" + SetDate(0, -216).substring(0,2) + "-" + SetDate(0, -216).substring(3);
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '1' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(18)
    @Issue(value = "TEL-4102")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 2, birthdate = 18 - уведомление есть")
    public void Access_4102_13() throws IOException, SQLException, InterruptedException {
        //высчитываем нужную дату - ровно 18 лет назад
        SetDate(0, -216);
        // Дополнительная логика по заявке 4049
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = Year + "-" + SetDate(0, -216).substring(0,2) + "-" + SetDate(0, -216).substring(3);
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '2' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(19)
    @Issue(value = "TEL-4102")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = null, birthdate = 18 - уведомление есть")
    public void Access_4102_13_() throws IOException, SQLException, InterruptedException {
        //высчитываем нужную дату - ровно 18 лет назад
        SetDate(0, -216);
        // Дополнительная логика по заявке 4049
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = Year + "-" + SetDate(0, -216).substring(0,2) + "-" + SetDate(0, -216).substring(3);
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = null where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(20)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 0, birthdate = 18 - уведомления нет")
    public void Access_3534_14() throws IOException, SQLException, InterruptedException {
        //высчитываем нужную дату - ровно 18 лет назад
        SetDate(0, -216);
        notification = false;
        // Дополнительная логика по заявке 4049
        sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
        while (sql.resultSet.next()) {
            Mo = sql.resultSet.getString("oid");
            StreetGuid = sql.resultSet.getString("street_code");
        }
        birthDate = Year + "-" + SetDate(0, -216).substring(0,2) + "-" + SetDate(0, -216).substring(3);
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '0' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(21)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 0, birthdate = ещё не родился - уведомления нет")
    public void Access_3534_15() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        notification = false;
        birthDate = "2100-05-10";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '0' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(22)
    @Issue(value = "TEL-4049")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 1, birthdate = ещё не родился - уведомления нет")
    public void Access_3534_16() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        notification = false;
        birthDate = "2100-05-10";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '1' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(23)
    @Issue(value = "TEL-4102")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = null, birthdate = ещё не родился - уведомления нет")
    public void Access_4102() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        notification = false;
        birthDate = "2100-05-10";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = null where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(25)
    @Issue(value = "TEL-4102")
    @DisplayName("Дополнительная логика уведомления для Отправка неотложной помощи signal-smp, only_children = 2, birthdate = ещё не родился - уведомления нет")
    public void Access_4102_5() throws IOException, SQLException, InterruptedException {
        // Дополнительная логика по заявке 4049
        notification = false;
        birthDate = "2100-05-10";
        sql.UpdateConnection("Update dpc.smp_addresses set only_children = '2' where street_code = '"+StreetGuid+"';");
        Access_3534Method("signal-smp");
    }

    @Test
    @Order(25)
    @DisplayName("Отправка неотложной помощи accompany-smp")
    public void Access_3534_40() throws IOException, SQLException, InterruptedException {
        Old = true;
        Access_3534Method("accompany-smp");
    }

    @Step("Метод отправки Справок")
    public void Access_3534Method(String help) throws InterruptedException, IOException, SQLException {

        System.out.println("Переходим на сайт для перехвата сообщений");
        driver.get(Notification);
        if (Old) {
            if (KingNumber == 11) {
                Mo = "1.2.643.5.1.13.13.12.2.1.24";
                PatientGuid_3534 = "5847a980-78a6-4afa-80db-3898356c67cf";
                Thread.sleep(1500);
            } else {
                Mo = "1.2.643.5.1.13.13.12.2.86.8902";
                PatientGuid_3534 = "2f1647ea-8c01-49f6-95ec-4d5cb6c8d1b6";
            }
            birthDate = "2015-10-31";
            StreetGuid = String.valueOf(UUID.randomUUID());
        } else {
            // Нужно для заявки 4043, сейчас для signal-smp оповещение приходит если в street.aoguid совпадает с dpc.smp_addresses.street_code, а МО в из dpc.smp_addresses.oid совпадает с Мо в оповещении
            PatientGuid_3534 = PatientGuid;
            sql.StartConnection("Select * from dpc.smp_addresses where oid = '"+MO+"';");
            while (sql.resultSet.next()) {
                Mo = sql.resultSet.getString("oid");
                StreetGuid = sql.resultSet.getString("street_code");
            }
        }
        Thread.sleep(1500);

        String bd = "";
        String bd_info = "";
        String body = "";
        String Type = "";

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);

        if (help.equals("urgent-smp")) {
            bd = "vimis.smp_urgent";
            Type = "1";
            System.out.println("Приём неотложной помощи");
            body = "{\n" +
                    "   \"cardUid\":\"" + uuid + "\",\n" +
                    "   \"CallDate\":\"2024-12-21\",\n" +
                    "   \"CardNumber\":\"555\",\n" +
                    "   \"patientGuid\":\"" + PatientGuid_3534 + "\",\n" +
                    "   \"patientComplaints\":\"Острая боль в животе\",\n" +
                    "   \"medicalHistory\":\"Подобная боль встречалась в раннем возрасте\",\n" +
                    "   \"anamnez\":{\n" +
                    "      \"workingPressure\":\"100 на 60\",\n" +
                    "      \"respirationRate\":\"учащенное\",\n" +
                    "      \"pulse\":\"100\",\n" +
                    "      \"pulseOximetry\":\"95\",\n" +
                    "      \"bloodPressure\":\"100 на 60\",\n" +
                    "      \"temperature\":\"36,7\",\n" +
                    "      \"heartRate\":\"10\"\n" +
                    "   },\n" +
                    "   \"localStatus\":\"тестовый статус\",\n" +
                    "   \"ECG\":{\n" +
                    "      \"ecgBefore\":{\n" +
                    "         \"ecgTime\":\"2024-10-31T15:45:24.874325\",\n" +
                    "         \"rhythm\":\"ритм\",\n" +
                    "         \"rhythmDisturbance\":\"нарушение ритма\",\n" +
                    "         \"conductionDisturbance\":\"нарушение проводимости\",\n" +
                    "         \"prongP\":\"зубец Р\",\n" +
                    "         \"prongT\":\"зубец Т\",\n" +
                    "         \"QRS\":\"QRS\",\n" +
                    "         \"heartRate\":\"ЧСС\",\n" +
                    "         \"segmentST\":\"сегмент ST\",\n" +
                    "         \"conclusion\":\"обнаружена дополнительная хорда\",\n" +
                    "         \"generalDiscription\":\"общее описание измерения ЭКГ\"\n" +
                    "      },\n" +
                    "      \"ecgAfter\":{\n" +
                    "         \"ecgTime\":\"2024-10-31T15:45:24.874325\",\n" +
                    "         \"rhythm\":\"ритм\",\n" +
                    "         \"rhythmDisturbance\":\"нарушение ритма\",\n" +
                    "         \"conductionDisturbance\":\"нарушение проводимости\",\n" +
                    "         \"prongP\":\"зубец Р\",\n" +
                    "         \"prongT\":\"зубец Т\",\n" +
                    "         \"QRS\":\"QRS\",\n" +
                    "         \"heartRate\":\"ЧСС\",\n" +
                    "         \"segmentST\":\"сегмент ST\",\n" +
                    "         \"conclusion\":\"обнаружена дополнительная хорда\",\n" +
                    "         \"generalDiscription\":\"общее описание измерения ЭКГ\"\n" +
                    "      }\n" +
                    "   },\n" +
                    "   \"patientDiagnoses\":{\n" +
                    "      \"code\":\"1\",\n" +
                    "      \"name\":\"Холера неуточненная\"\n" +
                    "   },\n" +
                    "   \"medications\":[\n" +
                    "      {\n" +
                    "         \"name\":\"парацетамол\",\n" +
                    "            \"quantity\":\"1\"\n" +
                    "      }\n" +
                    "   ],\n" +
                    "   \"openDate\":\"2024-10-31T15:45:24.874325\",\n" +
                    "   \"prescribedTreatment\":\"назначенное лечение\",\n" +
                    "   \"departureResult\":\"результат выезда\",\n" +
                    "   \"assistanseProvied\":\"Помощь оказанная на месте\"\n" +
                    "}";
        } else if (help.equals("signal-smp")) {

            bd = "vimis.smp_signal";
            bd_info = "vimis.smp_signal_patientinfo";
            Type = "2";
            System.out.println("Приём по сигнальному листу");
            body = "{\n" +
                    "   \"requestMoOid\":\"123\",\n" +
                    "   \"departOid\":\"123\",\n" +
                    "   \"CardNumber\":\"123\",\n" +
                    "   \"cardUid\":\""+uuid+"\",\n" +
                    "   \"smpDateTime\":\"2024-10-31T15:45:24.874325\",\n" +
                    "   \"patientInfo\":{\n" +
                    "      \"patientGuid\":\""+PatientGuid_3534+"\",\n" +
                    "      \"birthDate\":\""+birthDate+"\",\n" +
                    "      \"lastName\":\"Иванова\",\n" +
                    "      \"firstName\":\"Оксана\",\n" +
                    "      \"middleName\":\"Ивановна\",\n" +
                    "      \"age\":\"0\",\n" +
                    "      \"address\":\"г Москва, ул Жукова, д.8\",\n" +
                    "      \"telephone\":\"1\"\n" +
                    "   },\n" +
                    "   \"crewName\":\"123\",\n" +
                    "   \"doctor\":{\n" +
                    "      \"localId\":\"1234567896\",\n" +
                    "      \"lastName\":\"Мищенко\",\n" +
                    "      \"firstName\":\"Оксана\",\n" +
                    "      \"middleName\":\"Святославовна\"\n" +
                    "   },\n" +
                    "   \"patientComplaints\":\"Острая боль в животе\",\n" +
                    "   \"anamnez\":{\n" +
                    "      \"workingPressure\":\"100 на 60\",\n" +
                    "      \"respirationRate\":\"учащенное\",\n" +
                    "      \"pulse\":\"100\",\n" +
                    "      \"pulseOximetry\":\"95\",\n" +
                    "      \"bloodPressure\":\"100 на 60\",\n" +
                    "      \"temperature\":\"36,7\",\n" +
                    "      \"heartRate\":\"10\"\n" +
                    "   },\n" +
                    "   \"prelimDiagnoses\":{\n" +
                    "      \"code\":\"1\",\n" +
                    "      \"name\":\"1\"\n" +
                    "   },\n" +
                    "   \"helpDescription\":\"назначенное лечение\",\n" +
                    "   \"departureResult\":\"результат выезда\",\n" +
                    "   \"helpPlace\":\"г Москва, ул Жукова, д 8\",\n" +
                    "   \"targetMoOid\":\"12\",\n" +
                    "   \"stationaryDateTime\":\"2024-10-31T15:45:24.874325\",\n" +
                    "   \"helpCar\":\"Капельница\",\n" +
                    "   \"regionName\":\"Республика Адыгея\",\n" +
                    "   \"doctorVisit\":\"нет\",\n" +
                    "   \"addresses\": [\n" +
                    "    {\n" +
                    "      \"city\": {\n" +
                    "        \"text\": \"vvvvvvvvvvvvvvvvvvvvvvvvv\",\n" +
                    "        \"aoguid\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" +
                    "        \"formalname\": \"vvvvvvvvvvvvvvvvvvvvvvvvv\"\n" +
                    "      },\n" +
                    "      \"flat\": \"vvvvvvvvvvvvvvvvvvvvvvvvv\",\n" +
                    "      \"floor\": \"vvvvvvvvvvvvvvvvvvvvvvvvv\",\n" +
                    "      \"house\": \"vvvvvvvvvvvvvvvvvvvvvvvvv\",\n" +
                    "      \"porch\": \"vvvvvvvvvvvvvvvvvvvvvvvvv\",\n" +
                    "      \"region\": {\n" +
                    "        \"text\": \"vvvvvvvvvvvvvvvvvvvvvvvvv\",\n" +
                    "        \"aoguid\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" +
                    "        \"formalname\": \"vvvvvvvvvvvvvvvvvvvvvvvvv\"\n" +
                    "      },\n" +
                    "      \"street\": {\n" +
                    "        \"text\": \"string\",\n" +
                    "        \"aoguid\": \""+StreetGuid+"\",\n" +
                    "        \"formalname\": \"string\"\n" +
                    "      },\n" +
                    "      \"intercom\": \"string\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
        } else if (help.equals("accompany-smp")) {
            bd = "vimis.smp_accompany";
            bd_info = "vimis.smp_accompany_patientinfo";
            Type = "3";
            System.out.println("Приём по сопроводительному листу");
            body = "{\n" +
                    "  \"cardUid\": \"" + uuid + "\",\n" +
                    "  \"cardNumber\": \"134\",\n" +
                    "  \"HelpProvided\": \"134\",\n" +
                    "  \"patientInfo\": {\n" +
                    "    \"patientIdentity\": {\n" +
                    "    \"documentType\": \"123\",\n" +
                    "    \"documentTypeName\": \"789\",\n" +
                    "    \"documentSeries\": \"111\",\n" +
                    "    \"documentNumber\": \"Ивановна\"\n" +
                    "    },\n" +
                    "    \"patientGuid\": \"" + PatientGuid_3534 + "\",\n" +
                    "    \"lastName\": \"Иванова\",\n" +
                    "    \"firstName\": \"Оксана\",\n" +
                    "    \"middleName\": \"Ивановна\",\n" +
                    "    \"age\": \"26\",\n" +
                    "    \"sex\": \"1\",\n" +
                    "    \"birthdate\": \"2024-11-21\",\n" +
                    "    \"address\": \"г Москва, ул Жукова, д.8\",\n" +
                    "    \"telephone\": \"789\"\n" +
                    "  },\n" +
                    "  \"helpPlace\": \"г Москва, ул Жукова, д 8\",\n" +
                    "  \"patientDiagnoses\": {\n" +
                    "    \"code\": \"1\",\n" +
                    "    \"name\": \"Холера неуточненная\"\n" +
                    "  },\n" +
                    "  \"targetMoOid\": \"" + Mo + "\",\n" +
                    "  \"stationaryDateTime\": \"2024-10-31T15:45:24.874325\",\n" +
                    "  \"receievedDateTime\": \"2024-10-31T15:45:24.874325\",\n" +
                    "  \"helpDescription\": \"назначенное лечение\",\n" +
                    "  \"otherNote\": \"прочие\",\n" +
                    "  \"doctor\": {\n" +
                    "    \"localId\": \"1234567896\",\n" +
                    "    \"lastName\": \"Мищенко\",\n" +
                    "    \"firstName\": \"Оксана\",\n" +
                    "    \"middleName\": \"Святославовна\"\n" +
                    "  },\n" +
                    "  \"accidentFact\": \"обстоятельства несчастного случая\",\n" +
                    "  \"accidentDateTime\": \"2024-10-31T15:45:24.874325\",\n" +
                    "  \"helpCar\": \"Капельница\",\n" +
                    "  \"transportMethod\": \"Санавиация\",\n" +
                    "  \"regionName\": \"Республика Адыгея\"\n" +
                    "}";
        }
        Api(HostAddress + "/api/smd/" + help + "", "post", null, null, body, 200, true);
        Thread.sleep(1500);

        sql.SQL("SELECT count(*) FROM " + bd + " where carduid = '" + uuid + "';");
        sql.StartConnection("SELECT * FROM " + bd + "  where carduid = '" + uuid + "';");
        while (sql.resultSet.next()) {
            if (Type.equals("1")) {
                sql.value = sql.resultSet.getString("attachmooid");
                Assertions.assertEquals(sql.value, Mo, "МО не совпадает");
            }
            if (Type.equals("2"))  {
                sql.value = sql.resultSet.getString("attachmooid");
                if (Old) {
                    Assertions.assertEquals(sql.value, Mo, "МО не совпадает");
                } else {
                    Assertions.assertEquals(sql.value, null, "МО не совпадает");
                }
            }
            if (Type.equals("3")) {
                sql.value = sql.resultSet.getString("targetmooid");
                Assertions.assertEquals(sql.value, Mo, "МО не совпадает");
            }
            id = sql.resultSet.getString("id");
        }

        System.out.println("Проверяем тип данных у даты 3971");
        if (!Type.equals("1")) {
            sql.StartConnection("SELECT pg_typeof(sp.birthdate), * FROM " + bd + " s  " +
                    "join " + bd_info + " sp on s.patientinfo = sp.id  " +
                    "where carduid = '" + uuid + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("pg_typeof");
            }
            Assertions.assertEquals(sql.value, "date", "У поля дата рождения тип поля должен быть date");
        }

        System.out.println("Проверяем добавление данных в смежные таблицы");
        if (Type.equals("1")) {
            GetSqlUrgent(bd, String.valueOf(uuid));
        }
        if (Type.equals("2")) {
            GetSqlSignal(bd, String.valueOf(uuid));
        }
        if (Type.equals("3")) {
            GetSqlAccompany(bd, String.valueOf(uuid));
        }

        if (KingNumber == 11) {
            ClickElement(By.xpath("//div[@id='selector']/div[1]"));
            WaitElement(By.xpath("//div[@class='request']/pre"));
            TEXT = driver.findElement(By.xpath("//div[@class='request']/pre")).getText();
        } else {
            Thread.sleep(1500);
            ClickElement(By.xpath("//button[contains(.,'Обновить данные')]"));
            WaitElement(By.xpath("//div[@class='text-center']/div[1]/span"));
            if (Old | !notification) {
                OneNotification = true;
            } else {
                OneNotification = false;
            }
            NotificationsTrue(18, String.valueOf(uuid), 0);
        }
        System.out.println(TEXT);
        if (notification) {
            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение пришло");
            Assertions.assertTrue(TEXT.contains("\"CardUid\":\"" + uuid + "\""),
                    "Оповещение для " + help + " не добавилось");
            Assertions.assertTrue(TEXT.contains("\"PatientGuid\":\"" + PatientGuid_3534 + "\""),
                    "Оповещение для " + help + " не добавилось");
            Assertions.assertTrue(TEXT.contains("\"SmpType\":" + Type + ""),
                    "Оповещение для " + help + " не добавилось");
        } else {
            System.out.println("Переходим на сайт для перехвата сообщений и проверяем, что оповещение НЕ пришло");
            Assertions.assertFalse(TEXT.contains("\"CardUid\":\"" + uuid + "\""),
                    "Оповещение для " + help + " не должно добавиться");
        }
    }

    @Step("Метод проверки таблиц Urgent")
    void GetSqlUrgent (String Bd, String Uuid) throws SQLException {
        System.out.println("-----Таблица vimis.smp_urgent_medications");
        String medicationsname = "";
        String medicationsquantity = "";
        sql.StartConnection("SELECT * FROM " + Bd + " s  " +
                "join vimis.smp_urgent_medications sp on s.id = sp.smpurgentid  " +
                "where carduid = '" + Uuid + "';");
        while (sql.resultSet.next()) {
            medicationsname = sql.resultSet.getString("medicationsname");
            medicationsquantity = sql.resultSet.getString("medicationsquantity");
        }

        Assertions.assertEquals(medicationsname, "парацетамол", "Данные medicationsname не совпадают");
        Assertions.assertEquals(medicationsquantity, "1", "Данные medicationsquantity не совпадают");

        System.out.println("-----Таблица vimis.smp_urgent_electrocardiogram");
        String ecgtime = "";
        String rhythm = "";
        String rhythmdisturbance = "";
        String conductiondisturbance = "";
        String prongp = "";
        String prongt = "";
        String qrs = "";
        String heartrate = "";
        String segmentst = "";
        String conclusion = "";
        String generaldiscription = "";
        sql.StartConnection("SELECT * FROM " + Bd + " s  " +
                "join vimis.smp_urgent_electrocardiogram sp on s.ecgafter = sp.id  " +
                "where carduid = '" + Uuid + "';");
        while (sql.resultSet.next()) {
            ecgtime = sql.resultSet.getString("ecgtime");
            rhythm = sql.resultSet.getString("rhythm");
            rhythmdisturbance = sql.resultSet.getString("rhythmdisturbance");
            conductiondisturbance = sql.resultSet.getString("conductiondisturbance");
            prongp = sql.resultSet.getString("prongp");
            prongt = sql.resultSet.getString("prongt");
            qrs = sql.resultSet.getString("qrs");
            heartrate = sql.resultSet.getString("heartrate");
            segmentst = sql.resultSet.getString("segmentst");
            conclusion = sql.resultSet.getString("conclusion");
            generaldiscription = sql.resultSet.getString("generaldiscription");
        }

        Assertions.assertEquals(ecgtime.substring(0, 10), "2024-10-31", "Данные ecgtime не совпадают");
        Assertions.assertEquals(rhythm, "ритм", "Данные rhythm не совпадают");
        Assertions.assertEquals(rhythmdisturbance, "нарушение ритма", "Данные rhythmdisturbance не совпадают");
        Assertions.assertEquals(conductiondisturbance, "нарушение проводимости", "Данные conductiondisturbance не совпадают");
        Assertions.assertEquals(prongp, "зубец Р", "Данные prongp не совпадают");
        Assertions.assertEquals(prongt, "зубец Т", "Данные prongt не совпадают");
        Assertions.assertEquals(qrs, "QRS", "Данные qrs не совпадают");
        Assertions.assertEquals(heartrate, "ЧСС", "Данные heartrate не совпадают");
        Assertions.assertEquals(segmentst, "сегмент ST", "Данные segmentst не совпадают");
        Assertions.assertEquals(conclusion, "обнаружена дополнительная хорда", "Данные conclusion не совпадают");
        Assertions.assertEquals(generaldiscription, "общее описание измерения ЭКГ", "Данные generaldiscription не совпадают");

        System.out.println("-----Таблица vimis.smp_urgent_anamnez");
        String workingPressure = "";
        String respirationRate = "";
        String pulse = "";
        String pulseOximetry = "";
        String bloodPressure = "";
        String temperature = "";
        String heartRate = "";
        sql.StartConnection("SELECT * FROM " + Bd + " s  " +
                "join vimis.smp_urgent_anamnez sp on s.anamnezid = sp.id  " +
                "where carduid = '" + Uuid + "';");
        while (sql.resultSet.next()) {
            workingPressure = sql.resultSet.getString("workingpressure");
            respirationRate = sql.resultSet.getString("respirationrate");
            pulse = sql.resultSet.getString("pulse");
            pulseOximetry = sql.resultSet.getString("pulseoximetry");
            bloodPressure = sql.resultSet.getString("bloodpressure");
            temperature = sql.resultSet.getString("temperature");
            heartRate = sql.resultSet.getString("heartrate");
        }

        Assertions.assertEquals(workingPressure, "100 на 60", "Данные workingPressure не совпадают");
        Assertions.assertEquals(respirationRate, "учащенное", "Данные respirationRate не совпадают");
        Assertions.assertEquals(pulse, "100", "Данные pulse не совпадают");
        Assertions.assertEquals(pulseOximetry, "95", "Данные pulseOximetry не совпадают");
        Assertions.assertEquals(bloodPressure, "100 на 60", "Данные bloodPressure не совпадают");
        Assertions.assertEquals(temperature, "36,7", "Данные temperature не совпадают");
        Assertions.assertEquals(heartRate, "10", "Данные heartRate не совпадают");
    }

    @Step("Метод проверки таблиц Signal")
    void GetSqlSignal (String Bd, String Uuid) throws SQLException {
        System.out.println("-----Таблица vimis.smp_signal_anamnez");
        String workingPressure = "";
        String respirationRate = "";
        String pulse = "";
        String pulseOximetry = "";
        String bloodPressure = "";
        String temperature = "";
        String heartRate = "";
        sql.StartConnection("SELECT * FROM " + Bd + " s  " +
                "join vimis.smp_signal_anamnez sp on s.anamnez = sp.id  " +
                "where carduid = '" + Uuid + "';");
        while (sql.resultSet.next()) {
            workingPressure = sql.resultSet.getString("workingpressure");
            respirationRate = sql.resultSet.getString("respirationrate");
            pulse = sql.resultSet.getString("pulse");
            pulseOximetry = sql.resultSet.getString("pulseoximetry");
            bloodPressure = sql.resultSet.getString("bloodpressure");
            temperature = sql.resultSet.getString("temperature");
            heartRate = sql.resultSet.getString("heartrate");
        }

        Assertions.assertEquals(workingPressure, "100 на 60", "Данные workingPressure не совпадают");
        Assertions.assertEquals(respirationRate, "учащенное", "Данные respirationRate не совпадают");
        Assertions.assertEquals(pulse, "100", "Данные pulse не совпадают");
        Assertions.assertEquals(pulseOximetry, "95", "Данные pulseOximetry не совпадают");
        Assertions.assertEquals(bloodPressure, "100 на 60", "Данные bloodPressure не совпадают");
        Assertions.assertEquals(temperature, "36,7", "Данные temperature не совпадают");
        Assertions.assertEquals(heartRate, "10", "Данные heartRate не совпадают");

        System.out.println("-----Таблица vimis.smp_signal_doctor");
        String localid = "";
        String lastname = "";
        String firstname = "";
        String middlename = "";
        sql.StartConnection("SELECT * FROM " + Bd + " s  " +
                "join vimis.smp_signal_doctor sp on s.doctor = sp.id  " +
                "where carduid = '" + Uuid + "';");
        while (sql.resultSet.next()) {
            localid = sql.resultSet.getString("localid");
            lastname = sql.resultSet.getString("lastname");
            firstname = sql.resultSet.getString("firstname");
            middlename = sql.resultSet.getString("middlename");
        }

        Assertions.assertEquals(localid, "1234567896", "Данные localid не совпадают");
        Assertions.assertEquals(lastname, "Мищенко", "Данные lastname не совпадают");
        Assertions.assertEquals(firstname, "Оксана", "Данные firstname не совпадают");
        Assertions.assertEquals(middlename, "Святославовна", "Данные middlename не совпадают");

        System.out.println("-----Таблица vimis.smp_signal_patientinfo");
        String patientguid = "";
        lastname = "";
        firstname = "";
        middlename = "";
        String birthdate = "";
        String age = "";
        String address = "";
        String telephone = "";
        sql.StartConnection("SELECT * FROM " + Bd + " s  " +
                "join vimis.smp_signal_patientinfo sp on s.patientinfo = sp.id  " +
                "where carduid = '" + Uuid + "';");
        while (sql.resultSet.next()) {
            middlename = sql.resultSet.getString("middlename");
            lastname = sql.resultSet.getString("lastname");
            firstname = sql.resultSet.getString("firstname");
            patientguid = sql.resultSet.getString("patientguid");
            birthdate = sql.resultSet.getString("birthdate");
            age = sql.resultSet.getString("age");
            address = sql.resultSet.getString("address");
            telephone = sql.resultSet.getString("telephone");
        }

        Assertions.assertEquals(middlename, "Ивановна", "Данные middlename не совпадают");
        Assertions.assertEquals(lastname, "Иванова", "Данные lastname не совпадают");
        Assertions.assertEquals(firstname, "Оксана", "Данные firstname не совпадают");
        Assertions.assertEquals(patientguid, PatientGuid_3534, "Данные patientguid не совпадают");
        Assertions.assertEquals(birthdate, birthdate, "Данные birthdate не совпадают");
        Assertions.assertEquals(age, "0", "Данные age не совпадают");
        Assertions.assertEquals(address, "г Москва, ул Жукова, д.8", "Данные address не совпадают");
        Assertions.assertEquals(telephone, "1", "Данные telephone не совпадают");
    }

    @Step("Метод проверки таблиц Accompany")
    void GetSqlAccompany (String Bd, String Uuid) throws SQLException {
        System.out.println("-----Таблица vimis.smp_accompany_doctor");
        String localid = "";
        String lastname = "";
        String firstname = "";
        String middlename = "";
        sql.StartConnection("SELECT * FROM " + Bd + " s  " +
                "join vimis.smp_accompany_doctor sp on s.doctor = sp.id  " +
                "where carduid = '" + Uuid + "';");
        while (sql.resultSet.next()) {
            localid = sql.resultSet.getString("localid");
            lastname = sql.resultSet.getString("lastname");
            firstname = sql.resultSet.getString("firstname");
            middlename = sql.resultSet.getString("middlename");
        }

        Assertions.assertEquals(localid, "1234567896", "Данные localid не совпадают");
        Assertions.assertEquals(lastname, "Мищенко", "Данные lastname не совпадают");
        Assertions.assertEquals(firstname, "Оксана", "Данные firstname не совпадают");
        Assertions.assertEquals(middlename, "Святославовна", "Данные middlename не совпадают");

        System.out.println("-----Таблица vimis.smp_accompany_patientinfo");
        String patientguid = "";
        lastname = "";
        firstname = "";
        middlename = "";
        String birthdate = "";
        String age = "";
        String documenttype = "";
        String documenttypename = "";
        String documentseries = "";
        String address = "";
        String telephone = "";
        sql.StartConnection("SELECT * FROM " + Bd + " s  " +
                "join vimis.smp_accompany_patientinfo sp on s.patientinfo = sp.id  " +
                "where carduid = '" + Uuid + "';");
        while (sql.resultSet.next()) {
            middlename = sql.resultSet.getString("middlename");
            lastname = sql.resultSet.getString("lastname");
            firstname = sql.resultSet.getString("firstname");
            patientguid = sql.resultSet.getString("patientguid");
            birthdate = sql.resultSet.getString("birthdate");
            age = sql.resultSet.getString("age");
            documenttype = sql.resultSet.getString("documenttype");
            documenttypename = sql.resultSet.getString("documenttypename");
            documentseries = sql.resultSet.getString("documentseries");
            address = sql.resultSet.getString("address");
            telephone = sql.resultSet.getString("telephone");
        }

        Assertions.assertEquals(middlename, "Ивановна", "Данные middlename не совпадают");
        Assertions.assertEquals(lastname, "Иванова", "Данные lastname не совпадают");
        Assertions.assertEquals(firstname, "Оксана", "Данные firstname не совпадают");
        Assertions.assertEquals(patientguid, PatientGuid_3534, "Данные patientguid не совпадают");
        Assertions.assertEquals(birthdate, "2024-11-21", "Данные birthdate не совпадают");
        Assertions.assertEquals(age, "26", "Данные age не совпадают");
        Assertions.assertEquals(documenttype, "123", "Данные documenttype не совпадают");
        Assertions.assertEquals(documenttypename, "789", "Данные documenttypename не совпадают");
        Assertions.assertEquals(documentseries, "111", "Данные documentseries не совпадают");
        Assertions.assertEquals(address, "г Москва, ул Жукова, д.8", "Данные address не совпадают");
        Assertions.assertEquals(telephone, "789", "Данные telephone не совпадают");
    }
}
