package UI.TmTest.BaseTest;

import Base.*;
import UI.TmTest.PageObject.Administration.Users;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Consultation.ConsultationScheduleRemote;
import UI.TmTest.PageObject.Directions.Kvots.EquipmentSchedule;
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
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты API")
@ExtendWith(TestListener.class)
@Feature("Авторизация и методы перед тестами")
public class AuthorizationBase extends BaseNew {
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
    @Order(1)
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
    @Order(2)
    @DisplayName("Проверям есть ли нужный пользователь")
    @Step("Переходим в роли доступа у устанавливаем нужный доступ роли")
    public void AddUser () throws Exception {
        AuthorizationObject authorizationObject = new AuthorizationObject(driver);
        Users users = new Users(driver);

        sql.StartConnection("SELECT count(*) FROM telmed.userplacework x\n" +
                "join telmed.users u on x.userid = u.id \n" +
                "join dpc.mis_sp_mu m on x.placework = m.idmu \n" +
                "join telmed.accessroles a on x.roleid = a.id \n" +
                "left join telmed.userpositions up on up.userplaceworkid = x.id \n" +
                "left join dpc.med_worker_positions mwp on mwp.id = up.medworkerpositionsid \n" +
                "WHERE u.snils = '"+PSnils+"' and m.namemu in  ('БУ ХМАО-Югры \"Окружная клиническая больница\"', 'БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"', 'БУ ХМАО-Югры \"Белоярская районная больница\"') and a.\"name\" = 'Тестировщик' and x.quitdate is null and up.deletedate is null;");
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Integer countSql = Integer.valueOf(sql.value);
        if (countSql < 3) {
            System.out.println("Не хватает какой то МО и роли - добавляем");

            // Авторизация под ОКБ
            AuthoUser(PSnilsHak);
            AuthorizationMethod(authorizationObject.OKB);

            // Переход в раздел "Пользователи"
            ClickElement(users.UsersWait);
            WaitElement(users.HeaderUsersWait);

            // Поиск по СНИЛС
            ClickElement(users.AddUserWait);
            users.InputSnils.sendKeys(PSnils);
            Thread.sleep(1500);
            WaitNotElement3(users.LoadingSnils, 15);
            ClickElement(users.ButtonSearchWait);
            WaitElement(users.LastName);
            String surName = getShadow(driver.findElement(users.LastName));

            if (surName.isEmpty()) {
                System.out.println("Пользователя нет в ИЕЭМК, добавляю");
                WaitElement(users.LastName);
                inputWord(driver.findElement(users.LastName), PLastNameGlobal + "а");
                inputWord(driver.findElement(users.Name), PNameGlobal + "а");
                inputWord(driver.findElement(users.MiddleName), PMiddleNameGlobal + "а");
                if (isElementNotVisible(users.Login)) {
                    inputWord(driver.findElement(users.Login), "Тести");
                    inputWord(driver.findElement(users.Password), "Тести");
                }

                ClickElement(users.Date);
                ClickElement(users.DateYToday);
                driver.findElement(users.Number).sendKeys("+7 (111) 111-11-11");
                inputWord(users.Email, "testik@mail.ruu");

                // Отключаем
                if (isElementVisible(users.FRMR)) {
                    ClickElement(users.FRMR);
                }
            }

            System.out.println("Добавление места работы");
            List<WebElement> list = driver.findElements(users.CountWork);
            String str[] = {"БУ ХМАО-Югры \"Белоярская районная больница\"", "БУ ХМАО-Югры \"Нефтеюганская окружная клиническая больница имени В.И. Яцкив\"", "БУ ХМАО-Югры \"Окружная клиническая больница\""};



            for (int j = 0; j < 3; j++) {
                boolean work = false;
                System.out.println("Проверяем есть ли МО с ролью Тестировщик - " + str[j]);
                for (int i = 1; i < list.size() + 1; i++) {
                    if (isElementVisibleTime(users.Work("" + i + "", str[j], PRole), 1)) {
                        work = true;
                        System.out.println("Есть МО с ролью Тестировщик - " + str[j]);
                        break;
                    }
                }

                if (!work) {
                    System.out.println("Нет МО с ролью Тестировщик - " + str[j]);
                    ClickElement(users.AddWorkWait);
                    SelectClickMethod(users.SelectWork, authorizationObject.Select(str[j]));
                    Thread.sleep(1500);
                    SelectClickMethod(users.SelectDivision, authorizationObject.SelectFirst);
                    Thread.sleep(1500);
                    SelectClickMethod(users.SelectRoleUser, authorizationObject.Select(PRole));
                    ClickElement(users.AddWorkButtonWait);

                    System.out.println("Добавление профиля");
                    ClickElement(users.ProfileButtonWait);
                    WaitElement(users.HeaderProfileWait);
                    ClickElement(users.ProfileAddButton);
                    ClickElement(users.ProfileChoose);
                    ClickElement(authorizationObject.Select("авиационной и космической медицине"));
                    ClickElement(users.EditProfileApplyWait);
                    ClickElement(users.EditProfileCloseWait);
                    Thread.sleep(1500);
                }
            }
            if (surName.isEmpty()) {
                ClickElement(users.AddUsers);
            } else {
                ClickElement(users.UpdateWait);
            }
        } else {
            System.out.println("Нужный пользователь с МО и ролью - " + PRole + " уже добавлен");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Устанавливаем нужные Доступы")
    @Step("Переходим в роли доступа и устанавливаем нужный доступ роли")
    public void AddSMSMO () throws SQLException, IOException, InterruptedException {

        System.out.println("Проверяем существует ли роль, если нет, то добавляем роль и все доступы");
        AddRoleTrue(PRole);

        System.out.println(
                "Устанавливаем к роли " + PRole + " - Все нужные доступы");
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

        // Чтобы не вылезало уведомление при авторизации
        sql.UpdateConnection(
                "Update telmed.users set isreleasenotificationbeenviewed = '1', frmrcheck = '0' where snils = '" + PSnils + "';");
        sql.UpdateConnection(
                "Update telmed.users set isreleasenotificationbeenviewed = '1', frmrcheck = '0' where snils = '" + PSnils_Rezerv + "';");

        System.out.println("Убираем фичу - отображение уровней МО");
        sql.UpdateConnection("Update telmed.features set enabled = 0 where key = 'MoLevels';");

        System.out.println("Устанавливаем нужный формат, нужно для 388 теста");
        sql.UpdateConnection("Update dpc.registered_emd set format = '2' where oid = '58';");
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

        BodyEquipment(PidEquipment2, POidMoTarget, "43");
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
        AuthorizationMethod(authorizationObject.OKB);
        equipmentSchedule.AddResearch(
                PMORequest,
                PNameEquipment,
                "",
                str);

        AuthorizationMethod(authorizationObject.YATCKIV);
        equipmentSchedule.AddResearch(PMOTarget,
                PNameEquipment2,
                "",
                str);
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

        if (isOneHourDifference(Time, DateTime, 1)) {
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
