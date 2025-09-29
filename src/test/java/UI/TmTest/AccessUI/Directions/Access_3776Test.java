package UI.TmTest.AccessUI.Directions;

import Base.BaseAPI;
import Base.TestListener;
import Base.TestListenerApi;
import Base.TestListenerChange;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.Directions.Kvots.DirectionsForQuotas;
import UI.TmTest.PageObject.Directions.Kvots.IncomingUnfinished;
import api.Before.Authorization;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.List;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@ExtendWith(TestListenerChange.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Тесты UI")
@Feature("Направления")
@Tag("Консультация_на_оборудование")
@Tag("Api_Направление_на_диагностику")
@Tag("Шаблоны_в_направлении")
@Tag("Проверка_БД")
@Tag("Основные")
public class Access_3776Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    DirectionsForQuotas directionsForQuotas;
    IncomingUnfinished incomingUnfinished;
    Authorization authorization;
    boolean favorite = false;
    boolean userTwo = false;

    // Решили, что не выбираем 38.2 без 38.1 (25.08.2025)

    @Issue(value = "TEL-3807")
    @Issue(value = "TEL-3808")
    @Issue(value = "TEL-3809")
    @Issue(value = "TEL-3811")
    @Issue(value = "TEL-3817")
    @Issue(value = "TEL-3819")
    @Issue(value = "TEL-3820")
    @Issue(value = "TEL-3821")
    @Issue(value = "TEL-3927")
    @Issue(value = "TEL-4010")
    @Issue(value = "TEL-4207")
    @Issue(value = "TEL-4208")
    @Owner(value = "Галиакберов Тимур")
    @Test
    @Order(1)
    @Link(name = "ТМС-2229", url = "https://team-1okm.testit.software/projects/5/tests/2229?isolatedSection=d72e058a-4718-4994-b342-ea7fad3fa259")
    @Story("Шаблоны")
    @DisplayName("Создание/редактирование/удаление шаблонов - все доступы")
    @Description("Создаём направление, переходим к этому направлению - записываем на слот - нажимаем выполнить исследование - проверяем фильтры шаблонов - создаём - добаляем в избранное - удаляем - также делаем это с чужим шаблоном ")
    public void Access_3776 () throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        authorization = new Authorization();

        Token = authorization.AuthorizationsAdd("1.2.643.5.1.13.13.12.2.86.8902", "21");

        xml.changes.put("$.TargetMedicalOid", POidMoRequest);
        xml.changes.put("$.ResearchCode", "HMP01");
        xml.changes.put("$.PatientGuid", PatientGuid);
        String modifiedJson = JsonMethod("SMS/Body/api_diagnostic.json", xml.changes, false, null);
        Api(HostAddress + "/api/diagnostic", "post", null, null, modifiedJson, 200, false);
        String id = Response.getString("Result.DirectionId");
        InputPropFile("Id_3776", id);

        directionsForQuotas.AddFile(PMORequest, id, PNameEquipment);

        System.out.println("Все доступы");
        AddRole(PRole, "Просмотр шаблонов Диагностического исследования", true);
        AddRole(PRole, "Создание, редактирование, удаление своих шаблонов Диагностического исследования", true);
        AddRole(PRole, "Создание, редактирование, удаление чужих шаблонов Диагностического исследования", true);

        System.out.println("1 проверка - Проверяем фильтры шаблонов");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.SortDesc);
        ClickElement(directionsForQuotas.SearchID(id));
        incomingUnfinished.WaitButton(
                new String[]{"Отменить направление", "Выполнить исследование", "Отправить на интерпретацию"});
        ClickElement(directionsForQuotas.Button("Выполнить исследование"));
        WaitElement(incomingUnfinished.PatternAll);

        SqlPatterns("Select count(*) from telmed.patterns;", "Все");

        ClickElement(incomingUnfinished.Filter);
        ClickElement(incomingUnfinished.FilterSet("Мои"));

        SqlPatterns("Select count(*) from telmed.patterns where authorid = '" + PId + "';", "Мои");

        ClickElement(incomingUnfinished.Filter);
        ClickElement(incomingUnfinished.FilterSet("Избранные"));

        SqlPatterns("Select count(*) from telmed.patterns pt\n" +
                "join telmed.patterns_favorites p on pt.id = p.patternid\n" +
                "where p.userid = '" + PId + "';", "Избранные");

        System.out.println("2 проверка - Проверяем поиск шаблонов");
        driver.navigate().refresh();
        Thread.sleep(1500);

        WaitElement(incomingUnfinished.PatternOne("1"));
        String name = driver.findElement(incomingUnfinished.PatternOneName("1")).getText();
        inputWord(driver.findElement(incomingUnfinished.SearchPattern), name + " ");
        WaitElement(incomingUnfinished.PatternOne("1"));

        System.out.println("3 проверка - Создание шаблона");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        ClickElement(directionsForQuotas.SortDesc);
        ClickElement(directionsForQuotas.SearchID(id));
        incomingUnfinished.WaitButton(
                new String[]{"Отменить направление", "Выполнить исследование", "Отправить на интерпретацию"});
        ClickElement(directionsForQuotas.Button("Выполнить исследование"));
        WaitElement(incomingUnfinished.PatternAll);
        ClickElement(incomingUnfinished.NewPattern);
        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "Название ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternStudy),
                "Сбор анамнеза и жалоб в дерматологии ");
        ClickElement(authorizationObject.Select("Сбор анамнеза и жалоб в дерматологии"));
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "Описание__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "Заключение ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "Рекомендации ");
        ClickElement(incomingUnfinished.SavePattern);
        Thread.sleep(2500);

        SqlSearch("Название", "Сбор анамнеза и жалоб в дерматологии", "Описание__", "Заключение", "Рекомендации");

        System.out.println("4 проверка - Редактирование своего шаблона");
        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Название ");
        ClickElement(incomingUnfinished.PatternName("Название"));
        ClickElement(incomingUnfinished.EditPattern("Редактировать шаблон"));

        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "Названиеsql ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "Описаниеsql__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "Заключениеsql ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "Рекомендацииsql ");
        ClickElement(incomingUnfinished.SavePattern);
        Thread.sleep(2500);

        SqlSearch("Названиеsql", "Сбор анамнеза и жалоб в дерматологии", "Описаниеsql__", "Заключениеsql",
                "Рекомендацииsql");

        System.out.println("5 проверка - Редактируем шаблон под другим пользователем - Всё ок");
        AuthoUser(PSnils_Rezerv);
        System.out.println(LOGIN_PAGE_URL);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + id + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);

        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Название ");
        ClickElement(incomingUnfinished.PatternName("Название"));
        ClickElement(incomingUnfinished.EditPattern("Редактировать шаблон"));

        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "НазваниеSQL ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "ОписаниеSQL__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "ЗаключениеSQL ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "РекомендацииSQL ");
        ClickElement(incomingUnfinished.SavePattern);
        Thread.sleep(2500);

        SqlSearch("НазваниеSQL", "Сбор анамнеза и жалоб в дерматологии", "ОписаниеSQL__", "ЗаключениеSQL",
                "РекомендацииSQL");

        System.out.println("6 проверка - Добавляем чужой шаблон в избранное");
        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "НазваниеSQL ");
        ClickElement(incomingUnfinished.PatternName("НазваниеSQL"));
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = true;
        userTwo = true;
        SqlSearch("НазваниеSQL", "Сбор анамнеза и жалоб в дерматологии", "ОписаниеSQL__", "ЗаключениеSQL",
                "РекомендацииSQL");

        System.out.println("7 проверка - убираем чужой шаблон из избранное - всё ок");
        Thread.sleep(1500);
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = false;
        userTwo = true;
        SqlSearch("НазваниеSQL", "Сбор анамнеза и жалоб в дерматологии", "ОписаниеSQL__", "ЗаключениеSQL",
                "РекомендацииSQL");

        System.out.println("8 проверка - Добавляем свой шаблон в Избранное");
        AuthoUser(PSnils);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + id + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);

        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "НазваниеSQL ");
        ClickElement(incomingUnfinished.PatternName("НазваниеSQL"));
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = true;
        userTwo = false;
        SqlSearch("НазваниеSQL", "Сбор анамнеза и жалоб в дерматологии", "ОписаниеSQL__", "ЗаключениеSQL",
                "РекомендацииSQL");

        System.out.println("9 проверка - Убираем свой шаблон из Избранное");
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = false;
        userTwo = false;
        SqlSearch("НазваниеSQL", "Сбор анамнеза и жалоб в дерматологии", "ОписаниеSQL__", "ЗаключениеSQL",
                "РекомендацииSQL");

        System.out.println("10 проверка - Удаляем свой шаблон");
        ClickElement(incomingUnfinished.EditPattern("Удалить шаблон"));
        ClickElement(incomingUnfinished.Button("Удалить"));
        Thread.sleep(1500);
        SqlNotWait("НазваниеSQL");

        System.out.println("11 проверка - Импортируем данные из шаблона");
        ClickElement(incomingUnfinished.NewPattern);
        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "Название ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternStudy),
                "Сбор анамнеза и жалоб в дерматологии ");
        ClickElement(authorizationObject.Select("Сбор анамнеза и жалоб в дерматологии"));
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "Описание__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "Заключение ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "Рекомендации ");
        ClickElement(incomingUnfinished.SavePattern);
        Thread.sleep(2500);

        favorite = false;
        userTwo = false;
        SqlSearch("Название", "Сбор анамнеза и жалоб в дерматологии", "Описание__", "Заключение", "Рекомендации");
        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Название ");
        ClickElement(incomingUnfinished.PatternName("Название"));
        ClickElement(incomingUnfinished.Import);
        Thread.sleep(1000);
        ClickElement(incomingUnfinished.SaveNot);
        Thread.sleep(1000);

        System.out.println("Проверяем заполнение данных в telmed.diagnosticresults");
        sql.StartConnection("SELECT d.*, mp.\"name\", s.\"name\" pathology FROM telmed.diagnosticresults d\n" +
                "left join dpc.medical_position mp on d.\"position\" = mp.id \n" +
                "left join telmed.diagnosticpathology dia on d.directionid = dia.directionid\n" +
                "left join telmed.s1mbegi1t7 s on dia.pathologyid = s.id\n" +
                "where d.directionid = '" + id + "';");
        while (sql.resultSet.next()) {
            String protocol = sql.resultSet.getString("protocol");
            String conclusion = sql.resultSet.getString("conclusion");
            String recommendations = sql.resultSet.getString("recommendations");
            String performersnils = sql.resultSet.getString("performersnils");
            String performername = sql.resultSet.getString("performername");
            String performerlname = sql.resultSet.getString("performerlname");
            String performermname = sql.resultSet.getString("performermname");

            Assertions.assertEquals(protocol, "Описание__", "Описание не совпадает");
            Assertions.assertEquals(conclusion, "Заключение", "Заключение не совпадает");
            Assertions.assertEquals(recommendations, "Рекомендации", "Рекомендации не совпадает");
            // Авторизуемся через Тестировщика, значит и данные его
            Assertions.assertEquals(performersnils, PSnils, "СНИЛС исполнителя не совпадает");
            Assertions.assertEquals(performername, PNameGlobal, "Имя не совпадает");
            Assertions.assertEquals(performerlname, PLastNameGlobal, "Фамилия не совпадает");
            Assertions.assertEquals(performermname, PMiddleNameGlobal, "Отчество не совпадает");
        }

        ClickElement(incomingUnfinished.EditPattern("Удалить шаблон"));
        ClickElement(incomingUnfinished.Button("Удалить"));
        Thread.sleep(1500);
        SqlNotWait("Название");

        /** Дополнительно проверяем сохранение данных в протоколе (4208) */
        driver.get(HostAddressWeb + "/direction/consultation/" + id + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);
        Assertions.assertEquals("Описание__", getShadow(driver.findElement(incomingUnfinished.Text("Описание"))), "Описание не заполнено");
        Assertions.assertEquals("Заключение", getShadow(driver.findElement(incomingUnfinished.Text("Заключение"))), "Описание не заполнено");
        Assertions.assertEquals("Рекомендации", getShadow(driver.findElement(incomingUnfinished.Text("Рекомендации"))), "Описание не заполнено");

        System.out.println("12 проверка - Удаляем чужой шаблон");
        ClickElement(incomingUnfinished.NewPattern);
        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "Название ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternStudy),
                "Сбор анамнеза и жалоб в дерматологии ");
        ClickElement(authorizationObject.Select("Сбор анамнеза и жалоб в дерматологии"));
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "Описание__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "Заключение ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "Рекомендации ");
        ClickElement(incomingUnfinished.SavePattern);
        Thread.sleep(2500);

        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Название ");
        ClickElement(incomingUnfinished.PatternName("Название"));
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        Thread.sleep(1500);

        AuthoUser(PSnils_Rezerv);
        System.out.println(LOGIN_PAGE_URL);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + id + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);

        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Название ");
        ClickElement(incomingUnfinished.PatternName("Название"));
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        Thread.sleep(1500);

        ClickElement(incomingUnfinished.EditPattern("Удалить шаблон"));
        ClickElement(incomingUnfinished.Button("Удалить"));
        Thread.sleep(1500);
        SqlNotWait("Название");

        sql.UpdateConnection("Delete from telmed.patterns where name = 'Название';");
        sql.UpdateConnection("Delete from telmed.patterns where name = 'Названиеsql';");
        sql.UpdateConnection("Delete from telmed.patterns where name = 'НазваниеSQL';");
    }

    @Test
    @Order(2)
    @Story("Шаблоны")
    @DisplayName("Создание/редактирование/удаление шаблонов с доступом 38 и 38.1")
    public void Access_3776_1 () throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        authorization = new Authorization();

        AddRole(PRole, "Просмотр шаблонов Диагностического исследования", true);
        AddRole(PRole, "Создание, редактирование, удаление своих шаблонов Диагностического исследования", true);
        AddRole(PRole, "Создание, редактирование, удаление чужих шаблонов Диагностического исследования", false);

        System.out.println("1 проверка - Создаём свой шаблон - всё ок");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);
        ClickElement(incomingUnfinished.NewPattern);
        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "Название ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternStudy),
                "Сбор анамнеза и жалоб в дерматологии ");
        ClickElement(authorizationObject.Select("Сбор анамнеза и жалоб в дерматологии"));
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "Описание__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "Заключение ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "Рекомендации ");
        ClickElement(incomingUnfinished.SavePattern);
        Thread.sleep(2500);

        System.out.println("2 проверка - Добавляем свой шаблон в избранное - всё ок");
        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Название ");
        ClickElement(incomingUnfinished.PatternName("Название"));
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = true;
        userTwo = false;
        SqlSearch("Название", "Сбор анамнеза и жалоб в дерматологии", "Описание__", "Заключение", "Рекомендации");

        System.out.println("3 проверка - убираем свой шаблон из избранное - всё ок");
        Thread.sleep(1500);
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = false;
        userTwo = false;
        SqlSearch("Название", "Сбор анамнеза и жалоб в дерматологии", "Описание__", "Заключение", "Рекомендации");

        System.out.println("4 проверка - Редактирование своего шаблона");
        ClickElement(incomingUnfinished.EditPattern("Редактировать шаблон"));

        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "Названиеsql ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "Описаниеsql__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "Заключениеsql ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "Рекомендацииsql ");
        ClickElement(incomingUnfinished.SavePattern);
        Thread.sleep(2500);
        favorite = false;
        userTwo = false;
        SqlSearch("Названиеsql", "Сбор анамнеза и жалоб в дерматологии", "Описаниеsql__", "Заключениеsql",
                "Рекомендацииsql");

        System.out.println("5 проверка - редактируем чужой шаблон - нет кнопки");
        AuthoUser(PSnils_Rezerv);
        System.out.println(LOGIN_PAGE_URL);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);

        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Названиеsql ");
        ClickElement(incomingUnfinished.PatternName("Названиеsql"));
        WaitNotElement(incomingUnfinished.EditPattern("Редактировать шаблон"));

        System.out.println("6 проверка - удаление чужого шаблона - нет кнопки");
        WaitNotElement(incomingUnfinished.EditPattern("Удалить шаблон"));

        System.out.println("7 проверка - добавляем чужой шаблон в избранное");
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = true;
        userTwo = true;
        SqlSearch("Названиеsql", "Сбор анамнеза и жалоб в дерматологии", "Описаниеsql__", "Заключениеsql",
                "Рекомендацииsql");

        System.out.println("8 проверка - убираем чужой шаблон из избранное");
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = false;
        userTwo = true;
        SqlSearch("Названиеsql", "Сбор анамнеза и жалоб в дерматологии", "Описаниеsql__", "Заключениеsql",
                "Рекомендацииsql");

        System.out.println("9 проверка - Удаляем свой шаблон");
        AuthoUser(PSnils);
        System.out.println(LOGIN_PAGE_URL);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);

        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Названиеsql ");
        ClickElement(incomingUnfinished.PatternName("Названиеsql"));
        ClickElement(incomingUnfinished.EditPattern("Удалить шаблон"));
        ClickElement(incomingUnfinished.Button("Удалить"));
        Thread.sleep(1500);
        SqlNotWait("Названиеsql");
    }

    @Test
    @Order(3)
    @Story("Шаблоны")
    @DisplayName("Создание/редактирование/удаление шаблонов с доступом 38 и 38.2")
    public void Access_3776_2 () throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        authorization = new Authorization();

        //Создадим шаблон с нужными доступами
        AddRole(PRole, "Просмотр шаблонов Диагностического исследования", true);
        AddRole(PRole, "Создание, редактирование, удаление своих шаблонов Диагностического исследования", true);
        AddRole(PRole, "Создание, редактирование, удаление чужих шаблонов Диагностического исследования", false);

        System.out.println("1 проверка - Создаём свой шаблон - всё ок");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);
        ClickElement(incomingUnfinished.NewPattern);
        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "Название ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternStudy),
                "Сбор анамнеза и жалоб в дерматологии ");
        ClickElement(authorizationObject.Select("Сбор анамнеза и жалоб в дерматологии"));
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "Описание__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "Заключение ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "Рекомендации ");
        ClickElement(incomingUnfinished.SavePattern);
        Thread.sleep(2500);

        AddRole(PRole, "Просмотр шаблонов Диагностического исследования", true);
        AddRole(PRole, "Создание, редактирование, удаление своих шаблонов Диагностического исследования", false);
        AddRole(PRole, "Создание, редактирование, удаление чужих шаблонов Диагностического исследования", true);

        System.out.println("1 проверка - Создаём свой шаблон - всё ок");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);
        ClickElement(incomingUnfinished.NewPattern);
        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "Название ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternStudy),
                "Сбор анамнеза и жалоб в дерматологии ");
        ClickElement(authorizationObject.Select("Сбор анамнеза и жалоб в дерматологии"));
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "Описание__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "Заключение ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "Рекомендации ");
        ClickElement(incomingUnfinished.SavePattern);
        Thread.sleep(2500);

        System.out.println("2 проверка - Добавляем свой шаблон в избранное - всё ок");
        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Название ");
        ClickElement(incomingUnfinished.PatternName("Название"));
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = true;
        userTwo = false;
        SqlSearch("Название", "Сбор анамнеза и жалоб в дерматологии", "Описание__", "Заключение", "Рекомендации");

        System.out.println("3 проверка - убираем свой шаблон из избранное - всё ок");
        Thread.sleep(1500);
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = false;
        userTwo = false;
        SqlSearch("Название", "Сбор анамнеза и жалоб в дерматологии", "Описание__", "Заключение", "Рекомендации");

        System.out.println("4 проверка - Редактирование своего шаблона - Недостаточно прав");
        ClickElement(incomingUnfinished.EditPattern("Редактировать шаблон"));

        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "Названиеsql ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "Описаниеsql__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "Заключениеsql ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "Рекомендацииsql ");
        ClickElement(incomingUnfinished.SavePattern);
        WaitElement(authorizationObject.AlertTrue("У вас недостаточно прав для редактирования данного шаблона"));
        Thread.sleep(2500);
        favorite = false;
        userTwo = false;
        SqlNotWait("Названиеsql");

        System.out.println("5 проверка - редактируем чужой шаблон - всё ок");
        AuthoUser(PSnils_Rezerv);
        System.out.println(LOGIN_PAGE_URL);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);

        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Название ");
        ClickElement(incomingUnfinished.PatternName("Название"));
        ClickElement(incomingUnfinished.EditPattern("Редактировать шаблон"));

        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "Названиеsql ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "Описаниеsql__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "Заключениеsql ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "Рекомендацииsql ");
        ClickElement(incomingUnfinished.SavePattern);

        favorite = false;
        userTwo = true;
        SqlSearch("Названиеsql", "Сбор анамнеза и жалоб в дерматологии", "Описаниеsql__", "Заключениеsql",
                "Рекомендацииsql");

        System.out.println("7 проверка - добавляем чужой шаблон в избранное");
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = true;
        userTwo = true;
        SqlSearch("Названиеsql", "Сбор анамнеза и жалоб в дерматологии", "Описаниеsql__", "Заключениеsql",
                "Рекомендацииsql");

        System.out.println("8 проверка - убираем чужой шаблон из избранное");
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = false;
        userTwo = true;
        SqlSearch("Названиеsql", "Сбор анамнеза и жалоб в дерматологии", "Описаниеsql__", "Заключениеsql",
                "Рекомендацииsql");

        System.out.println("9 проверка - Удаляем свой шаблон");
        AuthoUser(PSnils);
        System.out.println(LOGIN_PAGE_URL);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);

        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Названиеsql ");
        ClickElement(incomingUnfinished.PatternName("Названиеsql"));
        ClickElement(incomingUnfinished.EditPattern("Удалить шаблон"));
        ClickElement(incomingUnfinished.Button("Удалить"));
        Thread.sleep(1500);
        WaitElement(authorizationObject.AlertTrue("У вас недостаточно прав для удаления данного шаблона"));

        System.out.println("10 проверка - удаление чужого шаблона - всё ок");
        AuthoUser(PSnils_Rezerv);
        System.out.println(LOGIN_PAGE_URL);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);

        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Названиеsql ");
        ClickElement(incomingUnfinished.PatternName("Названиеsql"));
        ClickElement(incomingUnfinished.EditPattern("Удалить шаблон"));
        ClickElement(incomingUnfinished.Button("Удалить"));
        Thread.sleep(1500);
        SqlNotWait("Названиеsql");

    }

    @Test
    @Order(4)
    @Story("Шаблоны")
    @DisplayName("Создание/редактирование/удаление шаблонов с доступом 38")
    public void Access_3776_3 () throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        authorization = new Authorization();

        AddRole(PRole, "Просмотр шаблонов Диагностического исследования", true);
        AddRole(PRole, "Создание, редактирование, удаление своих шаблонов Диагностического исследования", true);
        AddRole(PRole, "Создание, редактирование, удаление чужих шаблонов Диагностического исследования", false);

        System.out.println("1 проверка - Создаём свой шаблон - всё ок");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);
        ClickElement(incomingUnfinished.NewPattern);
        inputWord(driver.findElement(incomingUnfinished.NewPatternString), "Название ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternStudy),
                "Сбор анамнеза и жалоб в дерматологии ");
        ClickElement(authorizationObject.Select("Сбор анамнеза и жалоб в дерматологии"));
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Описание")), "Описание__ ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Заключение")), "Заключение ");
        inputWord(driver.findElement(incomingUnfinished.NewPatternString("Рекомендации")), "Рекомендации ");
        ClickElement(incomingUnfinished.SavePattern);
        Thread.sleep(2500);

        AddRole(PRole, "Просмотр шаблонов Диагностического исследования", true);
        AddRole(PRole, "Создание, редактирование, удаление своих шаблонов Диагностического исследования", false);
        AddRole(PRole, "Создание, редактирование, удаление чужих шаблонов Диагностического исследования", false);

        System.out.println("1 проверка - Создаём свой шаблон - нет кнопки");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);
        WaitNotElement(incomingUnfinished.NewPattern);

        System.out.println("2 проверка - Добавляем свой шаблон в избранное - всё ок");
        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Название ");
        ClickElement(incomingUnfinished.PatternName("Название"));
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = true;
        userTwo = false;
        SqlSearch("Название", "Сбор анамнеза и жалоб в дерматологии", "Описание__", "Заключение", "Рекомендации");

        System.out.println("3 проверка - убираем свой шаблон из избранное - всё ок");
        Thread.sleep(1500);
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = false;
        userTwo = false;
        SqlSearch("Название", "Сбор анамнеза и жалоб в дерматологии", "Описание__", "Заключение", "Рекомендации");

        System.out.println("4 проверка - Редактирование своего шаблона - нет кнопки");
        WaitNotElement(incomingUnfinished.EditPattern("Редактировать шаблон"));

        System.out.println("5 проверка - редактируем чужой шаблон - нет кнопки");
        AuthoUser(PSnils_Rezerv);
        System.out.println(LOGIN_PAGE_URL);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);

        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Название ");
        ClickElement(incomingUnfinished.PatternName("Название"));
        WaitNotElement(incomingUnfinished.EditPattern("Редактировать шаблон"));

        System.out.println("6 проверка - удаление чужого шаблона - нет кнопки");
        WaitNotElement(incomingUnfinished.EditPattern("Удалить шаблон"));

        System.out.println("7 проверка - добавляем чужой шаблон в избранное - всё ок");
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = true;
        userTwo = true;
        SqlSearch("Название", "Сбор анамнеза и жалоб в дерматологии", "Описание__", "Заключение", "Рекомендации");

        System.out.println("8 проверка - убираем чужой шаблон из избранное - всё ок");
        ClickElement(incomingUnfinished.EditPattern("Добавить шаблон в избранное"));
        favorite = false;
        userTwo = true;
        SqlSearch("Название", "Сбор анамнеза и жалоб в дерматологии", "Описание__", "Заключение", "Рекомендации");

        System.out.println("9 проверка - Удаляем свой шаблон - нет кнопки");
        AuthoUser(PSnils);
        System.out.println(LOGIN_PAGE_URL);
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        WaitElement(incomingUnfinished.PatternAll);

        inputWord(driver.findElement(incomingUnfinished.SearchPattern), "Название ");
        ClickElement(incomingUnfinished.PatternName("Название"));
        WaitNotElement(incomingUnfinished.EditPattern("Удалить шаблон"));
    }

    @Test
    @Order(5)
    @Story("Шаблоны")
    @DisplayName("Создание/редактирование/удаление шаблонов с доступом 38.1 или 38.2 или без доступов или 38.1 и 38.2")
    public void Access_3776_4 () throws Exception {
        authorizationObject = new AuthorizationObject(driver);
        directionsForQuotas = new DirectionsForQuotas(driver);
        incomingUnfinished = new IncomingUnfinished(driver);
        authorization = new Authorization();

        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                AddRole(PRole, "Просмотр шаблонов Диагностического исследования", false);
                AddRole(PRole, "Создание, редактирование, удаление своих шаблонов Диагностического исследования",
                        true);
                AddRole(PRole, "Создание, редактирование, удаление чужих шаблонов Диагностического исследования",
                        false);
            }
            if (i == 1) {
                AddRole(PRole, "Просмотр шаблонов Диагностического исследования", false);
                AddRole(PRole, "Создание, редактирование, удаление своих шаблонов Диагностического исследования",
                        false);
                AddRole(PRole, "Создание, редактирование, удаление чужих шаблонов Диагностического исследования",
                        true);
            }
            if (i == 2) {
                AddRole(PRole, "Просмотр шаблонов Диагностического исследования", false);
                AddRole(PRole, "Создание, редактирование, удаление своих шаблонов Диагностического исследования",
                        true);
                AddRole(PRole, "Создание, редактирование, удаление чужих шаблонов Диагностического исследования",
                        true);
            }
            if (i == 3) {
                AddRole(PRole, "Просмотр шаблонов Диагностического исследования", false);
                AddRole(PRole, "Создание, редактирование, удаление своих шаблонов Диагностического исследования",
                        false);
                AddRole(PRole, "Создание, редактирование, удаление чужих шаблонов Диагностического исследования",
                        false);
            }
        }

        System.out.println("1 проверка - Нет шаблонов");
        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(directionsForQuotas.ConsultationWait);
        driver.get(HostAddressWeb + "/direction/consultation/" + ReadPropFile("Id_3776") + "/protocol");
        Thread.sleep(2000);
        WaitNotElement(incomingUnfinished.PatternAll);

        sql.UpdateConnection("Delete from telmed.patterns where name = 'Название';");
        sql.UpdateConnection("Delete from telmed.patterns where name = 'Названиеsql';");
        sql.UpdateConnection("Delete from telmed.patterns where name = 'НазваниеSQL';");
    }

    @Step("Метод для Проверки количества шаблонов")
    private void SqlPatterns (String SQL, String value) throws InterruptedException, SQLException {
        sql.StartConnection(SQL);
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        double result = Math.ceil(Integer.valueOf(sql.value) / 6);
        WaitElement(incomingUnfinished.PatternOne("last()"));

        System.out.println("Пролистываем вниз несколько раз, чтобы загрузить всё количество шаблонов");
        for (int i = 0; i < result; i++) {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].scrollIntoView(true);",
                    driver.findElement(incomingUnfinished.PatternOne("last()")));
            WaitElement(incomingUnfinished.PatternOne("last()"));
            Thread.sleep(2000);
        }

        List<WebElement> all = driver.findElements(incomingUnfinished.PatternAll);
        Assertions.assertEquals(Integer.valueOf(sql.value), all.size(), "Количество шаблонов не совпадает - " + value);
    }

    @Step("Метод для Проверки созданного шаблона")
    private void SqlSearch (String name, String studySql, String descriptionSql, String consultationSql, String recommendationsSql) throws SQLException {
        String NameFIO = null;
        String Name = null;
        String Study = null;
        String Description = null;
        String Conclusion = null;
        String Recommendations = null;
        String favorites = null;

        sql.StartConnection(
                "Select pt.*, ms.\"name\" as med, concat(u.fname, ' ', u.sname, ' ', u.mname) as author,  concat(uf.fname, ' ', uf.sname, ' ', uf.mname) as authorFavorite, ptx.*, p.userid from telmed.patterns pt\n" +
                        "left join telmed.patterns_favorites p on pt.id = p.patternid\n" +
                        "join telmed.patterns_text ptx on ptx.id = pt.id \n" +
                        "join dpc.medical_services ms on pt.medicalserviceid  = ms.id \n" +
                        "join telmed.users u on pt.authorid = u.id \n" +
                        "left join telmed.users uf on p.userid = uf.id \n" +
                        "where pt.\"name\" = '" + name + "';");
        while (sql.resultSet.next()) {
            Name = sql.resultSet.getString("name");
            Study = sql.resultSet.getString("med");
            Description = sql.resultSet.getString("description");
            Conclusion = sql.resultSet.getString("conclusion");
            Recommendations = sql.resultSet.getString("recommendation");
            favorites = sql.resultSet.getString("authorfavorite");
            NameFIO = sql.resultSet.getString("author");
        }

        Assertions.assertEquals(name, Name, "Название шаблона не совпдает");
        Assertions.assertEquals(studySql, Study, "Исследование шаблона не совпдает");
        Assertions.assertEquals(descriptionSql, Description, "Описание шаблона не совпдает");
        Assertions.assertEquals(consultationSql, Conclusion, "Заключение шаблона не совпдает");
        Assertions.assertEquals(recommendationsSql, Recommendations, "Рекомендации шаблона не совпдает");
        Assertions.assertEquals(NameFIO, PNameGlobal + " " + PLastNameGlobal + " " + PMiddleNameGlobal,
                "Автор шаблона не совпдает");

        if (favorite) {
            if (userTwo) {
                Assertions.assertEquals(favorites,
                        PNameGlobal_Rezerv + " " + PLastNameGlobal_Rezerv + " " + PMiddleNameGlobal_Rezerv,
                        "ФИО того, кто добавил в избранное не совпдает");
            } else {
                Assertions.assertEquals(favorites, PNameGlobal + " " + PLastNameGlobal + " " + PMiddleNameGlobal,
                        "ФИО того, кто добавил в избранное не совпдает");
            }
        } else {
            Assertions.assertNotEquals(favorites, null, "Не дольжно быть ФИО в избранном");
        }
    }

    @Step("Метод для проверки удалённого шаблона")
    private void SqlNotWait (String value) throws SQLException {
        sql.NotSQL(
                "Select count(*) from telmed.patterns pt\n" +
                        "left join telmed.patterns_favorites p on pt.id = p.patternid\n" +
                        "join telmed.patterns_text ptx on ptx.id = pt.id \n" +
                        "join dpc.medical_services ms on pt.medicalserviceid  = ms.id \n" +
                        "join telmed.users u on pt.authorid = u.id \n" +
                        "left join telmed.users uf on p.userid = uf.id \n" +
                        "where pt.\"name\" = '" + value + "';");
    }
}
