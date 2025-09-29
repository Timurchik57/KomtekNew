package api.Before;

import Base.SQL;
import Base.TestListener;
import UI.TmTest.AccessUI.Administration.Access_1898Test;
import UI.TmTest.PageObject.Administration.MedOrganization;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
import UI.TmTest.PageObject.NSI.Equipments;
import Base.BaseAPI;
import Base.TestListenerApi;
import Base.XML;
import UI.TmTest.PageObject.VIMIS.RestrictionsIS;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.hc.core5.util.TextUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Авторизация и методы перед тестами")
public class Authorization extends BaseAPI {
    XML xml;
    String BODY;
    String id;
    String SerialNumber;
    String InventoryNumber;
    String Mo;
    String Name;
    String Type;
    String MoName;

    /**
     *  Команды для запуска тестов через контейнер
     *  allure generate target/allure-results --clean
     *  docker run --rm -d -p 4445:4444 -v /dev/shm:/dev/shm selenium/standalone-chrome
     *  mvn clean test -Dtest=Authorization#Authorizations -DUrlChrome=http://192.168.1.109:4445/wd/hub -DContour=2
     *  mvn clean test -Dtest=Authorization#ClearFile -DUrlChrome=http://192.168.1.109:4445/wd/hub -DContour=2
     *
     *  mvn clean test -Dtest=Authorization#Authorizations -DUrlChrome=http://192.168.2.126:4445/wd/hub -DContour=2
     *  */

    /**
     Должны быть добавлены:
     @Уведомления
     @* address = HostAddressWeb/TmkCallback, medicalidmu = 21126, system_id = 43
     @ИС
     @* mo = 99 (1.2.643.5.1.13.13.12.2.86.8902), system_id = 43, password = 333
     @* mo = 1605 (1.2.643.5.1.13.13.12.2.86.9003), system_id = 43, password = 333
     @* (Для Хмао) mo = 56 (1.2.643.5.1.13.13.12.2.86.9003), system_id = 43, password = 333
     @* mo = 99 (1.2.643.5.1.13.13.12.2.86.8902), system_id = 21, password = 123
     @* mo = 1 (1.2.643.5.1.13.13.12.2.86.8986), system_id = 15, password = 321
     @* mo = 1 (1.2.643.5.1.13.13.12.2.86.8986), system_id = 21, password = 123
     */

    @Test
    @Disabled
    @DisplayName("тестов")
    public void StartFiled () throws IOException {
        System.out.println("agaaaaaaaaaa");
        driver.get(
                "https://tm-dev.pkzdrav.ru/auth/bysnils?snils=159-790-257 20&key=8E3D10EC-9596-444D-BFDE-C101D5F7AE91");
        Assertions.assertEquals(1, 2);
    }

    @Test
    @Disabled
    @DisplayName("тестов")
    public void StartFiled2 () {
        driver.get("https://www.jetbrains.com/idea/");
        Assertions.assertEquals(1, 2);
    }

    @Test
    @Order(1)
    @DisplayName("Очищаем файл FiledTests.sh или FiledTests.bat для записи в него упавших тестов")
    public void ClearFile () throws IOException {
        String str = "";
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                str = "ignored/FiledTests.bat";
            } else {
                str = "ignored/FiledTests.sh";
            }
            new FileWriter("src/test/resources/" + str + "", false).close();
        }
    }

    @Test
    @Order(2)
    @DisplayName("Авторизация и получение токена авторизации")
    public void Authorizations () throws IOException {
        xml = new XML();
        Token = given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .log().body()
                .body(BodyAuthorisation)
                .when()
                .post(HostAddress + "/auth.svc")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("Result.Value");
        if (KingNumber == 1) {
            InputPropFile("Token_TmTest", Token);
        } else if (KingNumber == 2) {
            InputPropFile("Token_TmDev", Token);
        } else if (KingNumber == 3) {
            InputPropFile("Token_TmSev", Token);
        } else if (KingNumber == 4) {
            InputPropFile("Token_TmXmao", Token);
        } else if (KingNumber == 5) {
            InputPropFile("Token_TmChao", Token);
        } else if (KingNumber == 9) {
            InputPropFile("Token_TmAltai", Token);
        } else if (KingNumber == 10) {
            InputPropFile("Token_DevSevNev", Token);
        } else if (KingNumber == 11) {
            InputPropFile("Token_TmAdygeya", Token);
        } else if (KingNumber == 12) {
            InputPropFile("Token_TmChaoNew", Token);
        }
    }

    @Test
    @Order(3)
    @DisplayName("Устанавливаем все нужные ИС и пароли")
    @Disabled
    @Step("Переходим в telmed.centralized_unloading_system_accesses, проверяем все ли ИС установлены, если нет, то дабавляем")
    public void IsAdd () throws SQLException {
        System.out.println("Устанавливаем нужные ИС и пароли");
        String value = null;

        /** Нужно для уведомления 2 по протоколу для ОКБ */
        SQLAddMethod(value, "43", "1.2.643.5.1.13.13.12.2.86.8902", "333");
        value = null;

        /** Нужно для уведомления 4 по для Белоярской */
        SQLAddMethod(value, "15", "1.2.643.5.1.13.13.12.2.86.8986", "321");
        value = null;

        /** Нужно для Белоярской */
        SQLAddMethod(value, "21", "1.2.643.5.1.13.13.12.2.86.8986", "123");
        value = null;

        /** Нужно для уведомления 2 по протоколу для Яцкив */
        SQLAddMethod(value, "43", "1.2.643.5.1.13.13.12.2.86.9003", "333");

        value = null;

        /** Нужно для роверки 16 типа уведомления, Добавляем ИС для БУ ХМАО-Югры "Окружная клиническая больница" (3381) */
        SQLAddMethod(value, "21", "1.2.643.5.1.13.13.12.2.86.8902", "7FB035C746E16B6FC72AA29F80CB9CCD");
        value = null;

        if (KingNumber == 4) {
            /** Нужно для АУ ХМАО-Югры \"Кондинская районная стоматологическая поликлиника\"*/
            SQLAddMethod(value, "43", "1.2.643.5.1.13.13.12.2.86.8987", "333");
        }
    }

    @Test
    @Order(4)
    @DisplayName("Устанавливаем все типы опевещений")
    @Step("Переходим в vimis.mis_address, проверяем все ли уведомления установлены, если нет, то дабавляем")
    public void Notifications () throws SQLException, IOException {
        System.out.println("Устанавливаем нужные адреса для уведомлений");

        // Для уведомления типа 2 для протокола
        AddNotification(HostAddress + "/api/smd/misaddress", HostAddressWeb + "/TmkCallback", "21126", 2, 43, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", HostAddressWeb + "/TmkCallback", "13094", 2, 43, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 2, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 4, 15, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 8, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 9, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 11, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 13, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 14, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 15, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 17, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 18, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "21126", 18, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 19, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "21126", 19, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "21126", 20, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "21126", 21, 21, 0, 0);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "21126", 22, 21, 0, 0);
        // Soap уведомление
        AddNotification(HostAddress + "/api/smd/misaddress/soap", "https://12345.requestcatcher.com/test", "21126", 10,
                21, 0, 0);
        // 12 тип уведомления
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 12, 21, 1, 3);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 12, 21, 2, 3);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 12, 21, 3, 3);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 12, 21, 4, 3);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 12, 21, 5, 3);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 12, 21, 99, 3);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 12, 21, 99, 101);
        // 16 тип уведомления
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 16, 21, 1, 3);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 16, 21, 2, 3);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 16, 21, 3, 3);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 16, 21, 4, 3);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 16, 21, 5, 3);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "1001", 16, 21, 99, 3);
        // 16 тип уведомления только вимис
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "21126", 16, 21, 1, 42);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "21126", 16, 21, 2, 42);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "21126", 16, 21, 3, 42);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "21126", 16, 21, 4, 42);
        AddNotification(HostAddress + "/api/smd/misaddress", AddressNotification, "21126", 16, 21, 5, 42);
        // Без типа, уведомление при записи на слот
        AddNotification(HostAddress + "/api/mis_integrations/address", AddressNotification, "21126", 0, 21, 0, 0);
    }

    @Test
    @Order(5)
    @DisplayName("Устанавливаем нужные Доступы")
    @Step("Переходим в роли доступа у устанавливаем нужный доступ роли")
    public void AddSMSMO () throws SQLException, IOException, InterruptedException {

        // Чтобы не вылезало уведомление при авторизации
        sql.UpdateConnection(
                "Update telmed.users set isreleasenotificationbeenviewed = '1', frmrcheck = '0' where snils = '" + PSnils + "';");
        sql.UpdateConnection(
                "Update telmed.users set isreleasenotificationbeenviewed = '1', frmrcheck = '0' where snils = '" + PSnils_Rezerv + "';");

        // Удаляем не нужные данные из таблицы telmed.mis_integrations
        sql.UpdateConnection(
                "Delete from telmed.mis_integrations where medicalidmu != '" + PIdmuMoRequest + "';");

        System.out.println("Проверяем существует ли роль, если нет, то добавляем роль и все доступы");
        AddRoleTrue(PRole);

        System.out.println(
                "Устанавливаем к нужной роли - Все нужные доступы");
        AddRole(PRole, "Доступ к структурированным медицинским сведениям по всем МО", true);
        AddRole(PRole, "Разрешено редактировать справочник оборудования своего МО", true);
        AddRole(PRole, "Разрешено редактировать справочник оборудования любого МО", true);
        AddRole(PRole, "Доступ к просмотру \"Диагнозы для консультаций\"", true);
        AddRole(PRole, "Доступ к разделу \"Расписание консультаций\"", true);
        AddRole(PRole, "Доступ к созданию/редактированию графиков консультаций в \"Расписание консультаций\"", true);
        AddRole(PRole, "Доступ к просмотру графиков консультаций в \"Расписание консультаций\"", true);
        AddRole(PRole, "Доступ к записи на консультацию в \"Расписание консультаций\"", true);
        AddRole(PRole, "Доступ к Аналитике МО по ОМП", true);
        AddRole(PRole, "Доступ до аналитики ВИМИС в рамках разработчика МИС", false);
        AddRole(PRole,
                "Разрешено выбирать врача, который отклонил/завершил консультацию, запросил доп.информацию", true);
        AddRole(PRole, "Доступ к санитарным бортам на уровне региона", true);
        AddRole(PRole, "Доступ к разделу \"Дистанционный мониторинг\"", true);
        AddRole(PRole, "Доступ к разделу \"Клинические рекомендации\"", true);
        AddRole(PRole, "Доступ к разделу заказанных справок", true);
        AddRole(PRole, "Доступ к разделу \"Ограничения на инструментальные исследования\"", true);
        AddRole(PRole, "Доступ к разделу \"Стандарты оказания МП\"", true);
        AddRole(PRole, "Доступ ко всем направлениям на квоты", true);
        AddRole(PRole, "Доступ ко всем консультациям", true);
        AddRole(PRole, "ВИМИС \"Онкология\"", true);
        AddRole(PRole, "ВИМИС \"ССЗ\"", true);
        AddRole(PRole, "ВИМИС \"АкиНео\"", true);
        AddRole(PRole, "ВИМИС \"Профилактика\"", true);
        AddRole(PRole, "ВИМИС \"Инфекционные заболевания\"", true);
        AddRole(PRole, "ВИМИС \"Иные профили\"", true);

    }

    @Test
    @Order(6)
    @DisplayName("Настройка контура для тестов, установка нужных значений")
    @Step("Настраиваем контура под тесты")
    public void BeforeData () throws SQLException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        Equipments equipments = new Equipments(driver);

        SQL sql = new SQL();

        System.out.println("Добавляем фичу - отображение уровней МО");
        sql.UpdateConnection("Update telmed.features set enabled = 1 where key = 'MoLevels';");

        System.out.println("Устанавливаем нужный формат, нужно для 388 теста");
        sql.UpdateConnection("Update dpc.registered_emd set format = '2' where oid = '58';");

        if (KingNumber == 1) {
            BODY = "{\"Username\": \"1.2.643.5.1.13.13.12.2.86.9003\",\n" +
                    "                   \"SystemId\": 22,\n" +
                    "                    \"Password\": \"612D11DB39CE0E0C434CCA701855CDDC\"}";
            sql.UpdateConnection(
                    "update telmed.accessroles set isavailable = '1' where name = 'Тестовая роль 213'");
            sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = '" + PRole + "'");
            sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = 'Консультант'");

            id = "14037";
            SerialNumber = "313141";
            InventoryNumber = "1010404095";
            Mo = "1.2.643.5.1.13.13.12.2.86.9003";
            Name = "X-OMAT";
            Type = "54";
            MoName = "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"";
        }

        if (KingNumber == 2) {
            BODY = "{\"Username\": \"1.2.643.5.1.13.13.12.2.86.9003\",\n" +
                    "                   \"SystemId\": 22,\n" +
                    "                    \"Password\": \"561D9DF2BBAD98CF327DE72BEB3FB33C\"}";
            sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = 'Тестовая роль'");
            sql.UpdateConnection("update telmed.accessroles set isavailable = '0' where name = 'Тестовая роль 2'");
            sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = '" + PRole + "'");
            sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = 'Консультант'");

            id = "17478";
            SerialNumber = "313141";
            InventoryNumber = "1010404095";
            Mo = "1.2.643.5.1.13.13.12.2.86.9003";
            Name = "X-OMAT";
            Type = "54";
            MoName = "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"";
        }
        if (KingNumber == 4) {
            BODY = "{\"Username\": \"1.2.643.5.1.13.13.12.2.86.8970\",\n" +
                    "\"SystemId\": 18,\n" +
                    "\"Password\": \"CBB8EB03AF36A823C6EAD68A6B048CA6\"}";
            sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = 'Тестовая 999'");
            sql.UpdateConnection("update telmed.accessroles set isavailable = '1' where name = '" + PRole + "'");
            sql.UpdateConnection(
                    "update telmed.accessroles set isavailable = '1' where name = 'ВИМИС для разработчика МИС'");

            id = "48434";
            SerialNumber = "000000003819";
            InventoryNumber = "000000000000977";
            Mo = "1.2.643.5.1.13.13.12.2.86.8970";
            Name = "CDR Kit";
            Type = "55";
            MoName = "АУ ХМАО-Югры \"Кондинская районная стоматологическая поликлиника\"";
        }

        System.out.println("У нужного оборудования удаляем дату списания");
        System.out.println(HostAddress + "/auth.svc");
        String Token = given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(BODY)
                .when()
                .post(HostAddress + "/auth.svc")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("Result.Value");

        String result = given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + Token)
                .body("{\n" +
                        "  \"Id\": " + id + ",\n" +
                        "  \"Name\": \"" + Name + "\",\n" +
                        "  \"Type\": " + Type + ",\n" +
                        "  \"Modality\": 1,\n" +
                        "  \"MedicalOid\": \"" + Mo + "\",\n" +
                        "  \"PatientMaxWeight\": 50.0,\n" +
                        "  \"IsAvailableForOtherMo\": true,\n" +
                        "  \"DateAnnulment\": \"\",\n" +
                        "  \"SerialNumber\": \"" + SerialNumber + "\",\n" +
                        "  \"InventoryNumber\": \"" + InventoryNumber + "\",\n" +
                        "  \"Description\": \"123\"\n" +
                        "}")
                .when()
                .put(HostAddress + "/api/equipment/update")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("Result.Id");
        if (KingNumber == 1) {
            Assertions.assertEquals(result, "14037", "Дата списания не удалилась");
        }
        if (KingNumber == 2) {
            Assertions.assertEquals(result, "17478", "Дата списания не удалилась");
        }
        if (KingNumber == 4) {
            Assertions.assertEquals(result, "48434", "Дата списания не удалилась");
        }
    }

    @Test
    @Order(7)
    @DisplayName("Добавляем расписание оборудованию")
    public void AddEquipment () throws IOException, SQLException {
        GetDate();

        System.out.println(Year + "-" + SetDate(0, 0).substring(0, 2) + "-" + SetDate(1, 0).substring(3));
        System.out.println(Year + "-" + SetDate(0, 1).substring(0, 2) + "-01");

        System.out.println("Метод создания расписания оборудования");
        /**
         Тм-тест
         14037 - X-OMAT,  БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"
         87929 - Vivid 7, БУ ХМАО-Югры \"Окружная клиническая больница\"

         Тм-дев
         17478 - X-OMAT, БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"
         87929 -Vivid 7, БУ ХМАО-Югры \"Окружная клиническая больница\"

         Тест Хмао
         48434 - CDR Kit, АУ ХМАО-Югры \"Кондинская районная стоматологическая поликлиника\"
         2750 -Gyroscan ACS-NT томограф тест МосмедИИ,  БУ ХМАО-Югры \"Окружная клиническая больница\"
         */
        System.out.println("1");
        BodyEquipment(PidEquipment2, POidMoTarget, "43");
        System.out.println("2");
        BodyEquipment(PidEquipment, POidMoRequest, "21");

        // Если не создалось, то пробуем создать только на завтрашнее число
        System.out.println("3");
        BodyEquipmentDate(1, 1, PidEquipment2, POidMoTarget, "43");

        // Если не создалось, то пробуем создать только на послезавтрашнее число
        System.out.println("4");
        BodyEquipmentDate(2, 2, PidEquipment2, POidMoTarget, "43");
    }

    @Test
    @Order(8)
    @DisplayName("Добавляем исследования оборудованию")
    public void AddResearchMethod () throws InterruptedException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        EquipmentSchedule equipmentSchedule = new EquipmentSchedule(driver);

        String str[] = {"HMP01", "Компьютерная томография органов брюшной полости", "Магнитно-резонансная томография головного мозга", "Маммография"};

        AuthorizationMethod(authorizationObject.Select(PMOTarget));
        equipmentSchedule.AddResearch(
                PMOTarget,
                PNameEquipment2,
                "",
                str);

        AuthorizationMethod(authorizationObject.Select(PMORequest));
        equipmentSchedule.AddResearch(PMORequest,
                PNameEquipment,
                "",
                str);
    }

    @Test
    @Order(9)
    @DisplayName("Настройка уровня МО и курируемы МО")
    public void LevelMO () throws InterruptedException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        Access_1898Test access1898Test = new Access_1898Test();
        MedOrganization medOrganization = new MedOrganization(driver);

        System.out.println("Переходим в Роли доступа");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходим в Мед. Организации и добавляем нужный уровень");
        access1898Test.Access_1898MOMethod(medOrganization.SelectMOOKB, medOrganization.Level2,
                medOrganization.Profile1, medOrganization.MOAddBRB, medOrganization.MOTrue, true);
    }

    @Test
    @Order(10)
    @DisplayName("Добавляем расписание консультаций для Окуржной клинической больницы, Врач - Зотин Андрей Владимирович")
    public void AddConsultation () throws InterruptedException, IOException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        ConsultationScheduleRemote consultationSR = new ConsultationScheduleRemote(driver);

        System.out.println("Авторизуемся");
        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Открываем Расписание консультаций - График консультаций");
        ClickElement(consultationSR.ConsultationScheduleremote);

        System.out.println("Проверяем есть ли свободные слоты у Консультанта");
        if (consultationSR.CheckConsulRemote("Зотин") == false) {
            System.out.println("Открываем Расписание консультаций - График консультаций - добавляем расписание");
            ClickElement(consultationSR.ConsultationScheduleremote);
            ClickElement(consultationSR.ConsultationSchedule);
            consultationSR.AddConsul("Зотин Андрей Владимирович", "детской урологии-андрологии",
                    "Подозрение на COVID-19",
                    consultationSR.Tomorrow, consultationSR.DateLastMonth, "6:00", "21:00", "70");
            Thread.sleep(2000);
        }
    }

    @Test
    @Order(11)
    @DisplayName("Удаляем ограничения по инструментальным исследованиям, чтобы не было проблем с созданием направления на диагностику")
    public void DeleteRestrictions () throws InterruptedException {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        RestrictionsIS restrictionsIS = new RestrictionsIS(driver);

        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(restrictionsIS.RestrictionIS);

        Integer number = 0;
        while (isElementNotVisible(restrictionsIS.frequencyDelete(
                "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"")) & number < 40) {
            ClickElement(restrictionsIS.frequencyDelete(
                    "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\""));
            ClickElement(restrictionsIS.YesDelete);
            number++;
            Thread.sleep(1500);
        }
        while (!isElementNotVisible(restrictionsIS.NextDisable)) {
            ClickElement(restrictionsIS.NextDisable);
            number = 0;
            while (isElementNotVisible(restrictionsIS.frequencyDelete(
                    "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"")) & number < 40) {
                ClickElement(restrictionsIS.frequencyDelete(
                        "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\""));
                ClickElement(restrictionsIS.YesDelete);
                number++;
                Thread.sleep(1500);
            }
        }
    }

    @Test
    @Order(12)
    @DisplayName("Удаляем стандарты оказания МП, чтобы не было проблем с созданием направления на диагностику")
    public void DeleteProvision () throws SQLException {
        sql.UpdateConnection("delete from telmed.medical_care_standards m where m.diagnosiscode = 'A00.0';");
    }

    @Step("Метод для авторизации и получения токена")
    public String AuthorizationsAdd (String mo, String is) throws SQLException, IOException {
        SQL sql = new SQL();
        String Tokenn = null;

        // Узнаём текущую дату и время - 2025-06-04T14:25:02.988853400
        LocalDateTime currentDateTime = LocalDateTime.now();
        String DateTime = String.valueOf(currentDateTime).substring(0, 19);
        System.out.println(DateTime);

        String Time = ReadPropFile("TokenTime_" + mo.substring(mo.length() - 4) + "_" + is);

        if (isOneHourDifference(Time, DateTime, 1) |
                !isOneHourDifference(Time, DateTime, 1) & ReadPropFile("Token_" + mo.substring(mo.length() - 4) + "_" + is) == null) {
            System.out.println("Генерируем токен для мо - " + mo + " и ИС - " + is);

            // Узнаём фактический пароль
            sql.StartConnection(
                    "SELECT c.* FROM telmed.centralized_unloading_system_accesses c \n" +
                            "join telmed.mo_permissions p on c.mopermissionid = p.id \n" +
                            "join dpc.mis_sp_mu m on p.idmo = m.idmu \n" +
                            "where c.centralizedunloadingsystemid = " + is + " and m.\"oid\" = '" + mo + "';");
            while (sql.resultSet.next()) {
                sql.value = sql.resultSet.getString("password");
            }

            // Записываем дату и время получения Токена, чтобы можно было определить, нужно ли повторно авторизовываться
            InputPropFile("TokenTime_" + mo.substring(mo.length() - 4) + "_" + is, DateTime);

            Tokenn = given()
                    .filter(new AllureRestAssured())
                    .contentType(ContentType.JSON)
                    .body("{\n" +
                            "    \"Username\": \"" + mo + "\",\n" +
                            "    \"SystemId\": " + is + ",\n" +
                            "    \"Password\": \"" + sql.value + "\"\n" +
                            "}")
                    .when()
                    .post(HostAddress + "/auth.svc")
                    .prettyPeek()
                    .then()
                    .extract().jsonPath().getString("Result.Value");
            if (Tokenn != null) {
                InputPropFile("Token_" + mo.substring(mo.length() - 4) + "_" + is, Tokenn);
            }
        } else {
            System.out.println("Токен уже есть, берём готовый для мо - " + mo + " и ИС - " + is);
            Tokenn = ReadPropFile("Token_" + mo.substring(mo.length() - 4) + "_" + is);
        }
        return Tokenn;
    }

    @Step("Метод для сравнения дат и вывод true, если даты различаются более чем на {2} часов")
    public static boolean isOneHourDifference (String date1Str, String date2Str, Integer time) {
        if (date1Str != null) {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            // Парсим строки в объекты LocalDateTime
            LocalDateTime date1 = LocalDateTime.parse(date1Str, formatter);
            LocalDateTime date2 = LocalDateTime.parse(date2Str, formatter);

            // Вычисляем разницу в часах
            long hoursDifference = ChronoUnit.HOURS.between(date1, date2);

            // Проверяем, равна ли разница
            return Math.abs(hoursDifference) > time;
        } else {
            return true;
        }
    }

    @Step("Метод для добавления расписания оборудованию")
    public void BodyEquipment (String value, String mo, String Is) throws IOException, SQLException {
        GetDate();
        String body = "{\n" +
                "    \"DateFrom\": \"" + Year + "-" + SetDate(0, 0).substring(0, 2) + "-" + SetDate(1, 0).substring(
                3) + "\",\n" +
                "    \"DateTo\": \"" + Year + "-" + SetDate(0, 1).substring(0, 2) + "-01" + "\",\n" +
                "    \"EquipmentId\": " + value + ",\n" +
                "    \"TimeInterval\": {\n" +
                "        \"DaysOfWeek\": [1,2,3,4,5,6,7],\n" +
                "        \"StartHour\":6,\n" +
                "        \"StartMinutes\": 0,\n" +
                "        \"EndHour\": 21,\n" +
                "        \"EndMinutes\": 0,\n" +
                "        \"Interval\":30\n" +
                "    }\n" +
                "}";

        Token = AuthorizationsAdd(mo, Is);
        Api(HostAddress + "/api/schedule/add", "post", null, null, body, 200, false);
    }

    @Step("Метод для добавления расписания оборудованию в нужную дату")
    public void BodyEquipmentDate (Integer DayAdd, Integer DayAdd2, String value, String mo, String Is) throws IOException, SQLException {
        GetDate();
        String body = "{\n" +
                "    \"DateFrom\": \"" + Year + "-" + SetDate(DayAdd, 0).substring(0, 2) + "-" + SetDate(DayAdd,
                0).substring(
                3) + "\",\n" +
                "    \"DateTo\": \"" + Year + "-" + SetDate(DayAdd2, 0).substring(0, 2) + "-" + SetDate(DayAdd2,
                0).substring(
                3) + "\",\n" +
                "    \"EquipmentId\": " + value + ",\n" +
                "    \"TimeInterval\": {\n" +
                "        \"DaysOfWeek\": [1,2,3,4,5,6,7],\n" +
                "        \"StartHour\":6,\n" +
                "        \"StartMinutes\": 0,\n" +
                "        \"EndHour\": 21,\n" +
                "        \"EndMinutes\": 0,\n" +
                "        \"Interval\":30\n" +
                "    }\n" +
                "}";

        Token = AuthorizationsAdd(mo, Is);
        Api(HostAddress + "/api/schedule/add", "post", null, null, body, 200, false);
    }

    @Step("Метод для обновлениния исследований у оборудования")
    public void EquipmentResearch (String value, String mo, String Is) throws IOException, SQLException {
        String body = "{\n" +
                "    \"DateFrom\": \"" + Year + "-" + SetDate(0, 0).substring(0, 2) + "-" + SetDate(1, 0).substring(
                3) + "\",\n" +
                "    \"DateTo\": \"" + Year + "-" + SetDate(0, 1).substring(0, 2) + "-01" + "\",\n" +
                "    \"EquipmentId\": " + value + ",\n" +
                "    \"TimeInterval\": {\n" +
                "        \"DaysOfWeek\": [1,2,3,4,5,6,7],\n" +
                "        \"StartHour\":6,\n" +
                "        \"StartMinutes\": 0,\n" +
                "        \"EndHour\": 21,\n" +
                "        \"EndMinutes\": 0,\n" +
                "        \"Interval\":30\n" +
                "    }\n" +
                "}";

        Token = AuthorizationsAdd(mo, Is);
        Api(HostAddress + "/api/schedule/add", "post", null, null, body, 200, false);
    }

    @Step("Метод для добавления уведомлений")
    public void AddNotification (String HostAddress, String accessAdress, String mo, Integer actionType, Integer systemId, Integer vmcl, Integer doctype) throws SQLException, IOException {

        String addressNotification = null;
        String body = null;
        boolean mis_integrations = false;

        if (HostAddress.equals(BaseAPI.HostAddress + "/api/mis_integrations/address")) {
            mis_integrations = true;
            sql.StartConnection(
                    "select * from telmed.mis_integrations where medicalidmu = '" + mo + "' and address = '" + accessAdress + "';");
        } else {
            if (actionType == 10) {
                sql.StartConnection(
                        "select * from vimis.mis_address where address = '" + accessAdress + "' and action_type_id = '" + actionType + "' and centralized_unloading_system_id = '" + systemId + "';");
            } else if (actionType == 12 | actionType == 16) {
                sql.StartConnection(
                        "select * from vimis.mis_address where address = '" + accessAdress + "' and medicalidmu = '" + mo + "' and action_type_id = '" + actionType + "' and centralized_unloading_system_id = '" + systemId + "' and vmcl = '" + vmcl + "' and doctype = '" + doctype + "';");
            } else {
                sql.StartConnection(
                        "select * from vimis.mis_address where address = '" + accessAdress + "' and medicalidmu = '" + mo + "' and action_type_id = '" + actionType + "' and centralized_unloading_system_id = '" + systemId + "';");
            }
        }
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("id");
        }
        System.out.println(sql.value);

        if (sql.value == null) {

            addressNotification = accessAdress;
            if (KingNumber == 4 & accessAdress.equals(HostAddressWeb + "/TmkCallback")) {
                addressNotification = "http://10.86.6.131:1100/TmkCallback";
            }

            if (mo.equals("21126")) {
                Token = AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "" + systemId + "");
                System.out.println("1");
            }
            if (mo.equals("13094")) {
                Token = AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.9003", "" + systemId + "");
                System.out.println("2");
            }
            if (mo.equals("1001")) {
                Token = AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8986", "" + systemId + "");
                System.out.println("2");
            }

            System.out.println("ЭТО токен - " + Token);
            if (actionType == 12 | actionType == 16) {
                body = "{\n" +
                        "  \"address\": \"" + addressNotification + "\",\n" +
                        "  \"actionTypeId\": \"" + actionType + "\",\n" +
                        "  \"DocType\": " + doctype + ",\n" +
                        "  \"vmcl\": " + vmcl + " \n" +
                        "}";
            } else if (mis_integrations) {
                body = "{\n" +
                        "  \"Address\": \"" + addressNotification + "\"\n" +
                        "}";
            } else {
                body = "{\n" +
                        "  \"address\": \"" + addressNotification + "\",\n" +
                        "  \"actionTypeId\": \"" + actionType + "\"\n" +
                        "}";
            }

            System.out.println(
                    "\nОтправляем запрос на добавление адреса - " + addressNotification + " для типа оповещения - " + actionType + "");
            JsonPath response = given()
                    .header("Authorization", "Bearer " + Token)
                    .contentType(ContentType.JSON)
                    .log().uri()
                    .log().body()
                    .body(body)
                    .when()
                    .post(HostAddress)
                    .prettyPeek()
                    .body()
                    .jsonPath();

            if (!TextUtils.isEmpty(response.get("errorMessage")) || !TextUtils.isEmpty(response.get("ErrorMessage"))) {
                System.out.println("\nОбновляем адрес");
                JsonPath respons = given()
                        .header("Authorization", "Bearer " + Token)
                        .log().uri()
                        .log().body()
                        .contentType(ContentType.JSON)
                        .body(body)
                        .when()
                        .put(HostAddress)
                        .prettyPeek()
                        .body()
                        .jsonPath();
                if (respons.get("statusCode") != null) {
                    Assertions.assertEquals((Integer) respons.get("statusCode"), 200,
                            "Адресс уведомлений не обновился");
                } else {
                    Assertions.assertEquals((Integer) respons.get("StatusCode"), 200,
                            "Адресс уведомлений не обновился");
                }
            }
        } else {
            sql.value = null;
        }
    }

    @Step("Метод, который проверяет нужную ИС с паролем и МО и если её нет, то добавляет")
    public void SQLAddMethod (String str, String central, String mo, String password) throws SQLException {
        String value = null;

        sql.StartConnection(
                "SELECT p.* FROM telmed.mo_permissions p \n" +
                        "join dpc.mis_sp_mu m on p.idmo = m.idmu \n" +
                        "where  m.\"oid\" = '" + mo + "';");
        while (sql.resultSet.next()) {
            value = sql.resultSet.getString("id");
        }

        sql.StartConnection(
                "SELECT c.* FROM telmed.centralized_unloading_system_accesses c \n" +
                        "join telmed.mo_permissions p on c.mopermissionid = p.id \n" +
                        "join dpc.mis_sp_mu m on p.idmo = m.idmu \n" +
                        "where c.centralizedunloadingsystemid = " + central + " and m.\"oid\" = '" + mo + "' and c.\"password\" = '" + password + "';");
        while (sql.resultSet.next()) {
            str = sql.resultSet.getString("id");
            value = sql.resultSet.getString("mopermissionid");
        }
        if (str == null) {
            sql.UpdateConnection(
                    "INSERT INTO telmed.centralized_unloading_system_accesses (centralizedunloadingsystemid, mopermissionid, \"password\") \n" +
                            "VALUES ('" + central + "', '" + value + "', '" + password + "')\n" +
                            "ON CONFLICT (mopermissionid, centralizedunloadingsystemid) \n" +
                            "DO UPDATE SET \"password\" = EXCLUDED.\"password\";");
        }
    }
}
