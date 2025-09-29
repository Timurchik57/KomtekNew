package UI.TmTest.AccessUI.Administration;

import Base.TestListener;
import UI.TmTest.PageObject.Administration.Limits;
import UI.TmTest.PageObject.AuthorizationObject;
import Base.BaseAPI;
import Base.TestListenerApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@ExtendWith(TestListenerApi.class)
@Epic("Тесты UI")
@Feature("Администрирование")
@Tag("Проверка_БД")
@Tag("Лимиты")
@Tag("Основные")
public class Access_2193Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    Limits limits;
    public String LocalUid;
    Access_2166Test access2166Test;

    @Issue(value = "TEL-2193")
    @Issue(value = "TEL-2194")
    @Issue(value = "TEL-2195")
    @Link(name = "ТМС-1900", url = "https://team-1okm.testit.software/projects/5/tests/1900?isolatedSection=65a30e47-043a-4dd0-af4f-165d06d17427")
    @Owner(value = "Галиакберов Тимур")
    @Description("Редактирование лимитов + проверка изменения в БД")
    @Test
    @DisplayName("Создаём лимиты и проверяем, что их можно редактировать")
    public void Access_2166() throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        limits = new Limits(driver);
        access2166Test = new Access_2166Test();

        AuthorizationMethod(authorizationObject.OKB);

        System.out.println("Переходим в настройку Лимитов");
        ClickElement(limits.Limit);

        System.out.println("Удаляем Созданные тестами лимиты");
        access2166Test.DeleteLimit("99977766644", "2", "987", "5");
        access2166Test.DeleteLimit("32178945688", "2", "573", "5");

        System.out.println("Добавляем все типы лимитов");
        limits.Method_2166("Запроса СЭМД", "МИС \"Пациент\"", "Протокол лабораторного исследования (CDA) Редакция 4 ",
                true, "", "", "1", "месяц", "5733", "");

        limits.Method_2166("Доступности СЭМД", "МИС \"Пациент\"",
                "Протокол лабораторного исследования (CDA) Редакция 4 ", true, "321789456888",
                "БУ ХМАО-Югры \"Окружная клиническая больница\"", "2", "", "", "");

        limits.Method_2166("Запроса медицинских изображений", "МИС \"Пациент\"", "", true, "", "", "3", "месяц", "5733",
                "Аллергологические исследования");

        limits.Method_2166("Доступности медицинских изображений", "МИС \"Пациент\"", "", true, "321789456888",
                "БУ ХМАО-Югры \"Окружная клиническая больница\"", "3", "", "", "Аллергологические исследования");

        EditLimit("МИС \"Югра\"", true, "Протокол инструментального исследования (CDA) Редакция 2", "999777666444",
                "БУ ХМАО-Югры \"Няганская окружная больница\"", "месяц", "полгода", "9877", "");

        System.out.println("Удаляем Созданные тестами лимиты");
        access2166Test.DeleteLimit("32178945688", "2", "573", "5");
        access2166Test.DeleteLimit("99977766644", "2", "987", "5");
    }

    @Step("Метод с помощью которого редактируем созданные лимиты, снилс - 32178945688, количество - 573")
    public void EditLimit(String InfoSystem, boolean Nolimits, String Doc, String snils, String MO, String period, String period2, String count, String research) throws InterruptedException, SQLException {

        authorizationObject = new AuthorizationObject(driver);
        limits = new Limits(driver);

        System.out.println("Редактируем лимит");
        while (isElementVisibleTime(limits.LimitEdit("573", "5"), 1)) {
            ClickElement(limits.LimitEdit("573", "5"));
            limits.Method_2166_Edit(InfoSystem, Doc, Nolimits, snils, MO, period, period2, count, research);
        }

        while (isElementVisibleTime(limits.LimitEdit("32178945688", "2"), 3)) {
            ClickElement(limits.LimitEdit("32178945688", "2"));
            limits.Method_2166_Edit(InfoSystem, Doc, Nolimits, snils, MO, period, period2, count, research);
        }
    }
}
