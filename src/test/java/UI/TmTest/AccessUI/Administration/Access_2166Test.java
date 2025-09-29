package UI.TmTest.AccessUI.Administration;

import Base.TestListener;
import UI.TmTest.PageObject.Administration.Limits;
import UI.TmTest.PageObject.AuthorizationObject;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Лимиты")
@Tag("Проверка_БД")
@Tag("Основные")
public class Access_2166Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    Limits limits;
    public String LocalUid;

    @Issue(value = "TEL-2166")
    @Issue(value = "TEL-2167 ")
    @Issue(value = "TEL-2168")
    @Issue(value = "TEL-2169")
    @Link(name = "ТМС-1874", url = "https://team-1okm.testit.software/projects/5/tests/1874?isolatedSection=65a30e47-043a-4dd0-af4f-165d06d17427")
    @Owner(value = "Галиакберов Тимур")
    @Description("Настраиваем лимиты смс и смотрим добавление их в БД")
    @Test
    @DisplayName("Сохранение добавленных лимитов на странице \"Настройка лимитов\"")
    public void Access_2166() throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        limits = new Limits(driver);
        String mo = "";

        System.out.println("Авторизуемся");
        if (KingNumber == 12) {
            AuthorizationLoginPassword(LOGIN_PAGE_URL, LoginBase, PasswordBase, "ГБУЗ \"Чукотская окружная больница\" г. Анадырь");
            mo = "ГБУЗ \"Чукотская окружная больница\" г. Анадырь";
        } else {
            AuthorizationMethod(authorizationObject.OKB);
            mo = "БУ ХМАО-Югры \"Окружная клиническая больница\"";
        }

        System.out.println("Переходим в настройку Лимитов");
        ClickElement(limits.Limit);

        System.out.println("Удаляем Созданные тестами лимиты");
        DeleteLimit("32178945688", "2", "573", "5");

        ClickElement(limits.AddLimit);

        System.out.println("\n 1 проверка - обязательность полей");
        SelectClickMethod(limits.Select("Тип лимита", false), authorizationObject.Select("Запроса СЭМД"));
        ClickElement(limits.Add);
        WaitElement(limits.SelectError("Информационная система"));
        WaitElement(limits.SelectError("Количество"));
        WaitElement(limits.SelectError("Период потребления информации"));

        System.out.println("\n 2 проверка - добавление данных с лимитом Запроса СЭМД");
        SelectClickMethod(limits.Select("Информационная система", false),
                authorizationObject.Select("МИС \"Пациент\""));
        inputWord(driver.findElement(limits.Select("Вид документа", true)),
                "Протокол лабораторного исследования (CDA) Редакция 4 ");
        Thread.sleep(1000);
        ClickElement(authorizationObject.SelectFirst);
        ClickElement(limits.NotLimits);
        inputWord(driver.findElement(limits.Select("Количество", true)), "5733");
        ClickElement(limits.Period("месяц"));
        ClickElement(limits.Add);
        Thread.sleep(2000);

        sql.StartConnection(
                "SELECT l.id, l.\"type\", t.fullname, l.doctype, e.doctype, l.count, l.nolimit, l.\"period\", l.doctorsnils, l.medicalidmu, l.research, l.createdate FROM telmed.limit_settings l\n" +
                        "left join telmed.centralized_unloading_systems t on l.centralizedunloadingsystemid = t.id\n" +
                        "left join dpc.emd_types e on l.doctype = e.id where l.\"type\" = '1' order by l.id desc limit 1;");
        while (sql.resultSet.next()) {
            Assertions.assertEquals(sql.resultSet.getString("type"), "1");
            Assertions.assertEquals(sql.resultSet.getString("fullname"), "МИС \"Пациент\"");
            Assertions.assertEquals(sql.resultSet.getString("doctype"), "3");
            Assertions.assertEquals(sql.resultSet.getString("count"), "573");
            Assertions.assertEquals(sql.resultSet.getString("nolimit"), "1");
            Assertions.assertEquals(sql.resultSet.getString("period"), "1");
            Assertions.assertEquals(sql.resultSet.getString("doctorsnils"), null);
            Assertions.assertEquals(sql.resultSet.getString("medicalidmu"), null);
            Assertions.assertEquals(sql.resultSet.getString("research"), null);

            String date = sql.resultSet.getString("createdate");
            Assertions.assertEquals(date.substring(0, 10), Date);
        }

        System.out.println("\n 3 проверка - добавление данных с лимитом Доступности СЭМД");
        limits.Method_2166("Доступности СЭМД", "МИС \"Пациент\"", "Протокол лабораторного исследования (CDA) Редакция 4 ", true, "32178945688 ", mo, "2", "", "", "");

        System.out.println("\n 4 проверка - добавление данных с лимитом Запроса медицинских изображений");
        limits.Method_2166("Запроса медицинских изображений", "МИС \"Пациент\"", "", true, "", "", "3", "месяц", "5733", "Аллергологические исследования");

        System.out.println("\n 5 проверка - добавление данных с лимитом Доступности медицинских изображений");
        limits.Method_2166("Доступности медицинских изображений", "МИС \"Пациент\"", "", true, "32178945688 ", mo, "3", "", "", "Аллергологические исследования");

        System.out.println("Удаляем Созданные тестами лимиты");
        DeleteLimit("32178945688", "2", "573", "5");
    }

    @Step("Метод с помощью которого удаляем созданные лимиты, снилс - 32178945688, количество - 573")
    public void DeleteLimit (String snils, String column, String count, String column2) throws InterruptedException {
        authorizationObject = new AuthorizationObject(driver);
        limits = new Limits(driver);

        while (isElementVisibleTime(limits.LimitDelete(snils, column), 1) == true) {
            ClickElement(limits.LimitDelete(snils, column));
            ClickElement(limits.LimitDeleteYes);
            Thread.sleep(1000);
        }
        while (isElementVisibleTime(limits.LimitDelete(count, column2), 1) == true) {
            ClickElement(limits.LimitDelete(count, column2));
            ClickElement(limits.LimitDeleteYes);
            Thread.sleep(1000);
        }

        while (isElementVisibleTime(authorizationObject.NextDisable, 1) == false) {
            ClickElement(authorizationObject.Next);

            while (isElementVisibleTime(limits.LimitDelete(snils, column), 1) == true) {
                ClickElement(limits.LimitDelete(snils, column));
                ClickElement(limits.LimitDeleteYes);
                Thread.sleep(1000);
            }
            while (isElementVisibleTime(limits.LimitDelete(count, column2), 1) == true) {
                ClickElement(limits.LimitDelete(count, column2));
                ClickElement(limits.LimitDeleteYes);
                Thread.sleep(1000);
            }
        }
        Thread.sleep(1500);
        if (isElementVisible(limits.LimitDeleteAlert)) {
            ClickElement(limits.LimitDeleteAlert);
        }
    }
}
