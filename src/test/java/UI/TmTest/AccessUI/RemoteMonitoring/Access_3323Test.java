package UI.TmTest.AccessUI.RemoteMonitoring;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.RemoteMonitoring.RegisterObservations;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Дистанционный мониторинг")
@Tag("Регистр_наблюдений")
@Tag("Основные")
public class Access_3323Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    RegisterObservations registerObservations;

    @Test
    @Issue(value = "TEL-3323")
    @Owner(value = "Галиакберов Тимур")
    @Link(name = "ТМС-2046", url = "https://team-1okm.testit.software/projects/5/tests/2046?isolatedSection=af882452-d09a-4051-af99-3d04bf599188")
    @Description("Переходим в Регистр наблюдений и проверяем фильтр")
    @DisplayName("Фильтр в Регистр наблюдений")
    public void Access_3323() throws SQLException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        registerObservations = new RegisterObservations(driver);

        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(registerObservations.Register);

        System.out.println("1 проверка - Проверяем количество всех записей");
        GetSql("select count(*) from telmed.pmpattach;", "Количество всех записей не совпадает");

        System.out.println("2 проверка - Проверяем количество записей по фильтру 'Мед организация'");
        SetFilter("Медицинская организация",
                "БУ ХМАО-Югры МИАЦ",
                "select count(*) from telmed.pmpattach p\n" +
                        "join dpc.mis_sp_mu m on p.mooid = m.\"oid\" where m.namemu = 'БУ ХМАО-Югры МИАЦ';",
                "Количество записей с фильтром 'Мед организация' не совпадает");

        System.out.println("3 проверка - Проверяем количество записей по фильтру 'ФИО пациента'");
        SetFilter("ФИО пациента",
                "Тестовый Пациент Пациентов",
                "select count(*) from telmed.pmpattach p\n" +
                        "join dpc.mis_sp_mu m on p.mooid = m.\"oid\" where p.patientlastname = 'Тестовый';",
                "Количество записей с фильтром 'ФИО пациента' не совпадает");

        System.out.println("4 проверка - Проверяем количество записей по фильтру 'Серийный номер устройства'");
        SetFilter("Серийный номер устройства",
                "00037315",
                "select count(*) from telmed.pmpattach p\n" +
                        "join dpc.mis_sp_mu m on p.mooid = m.\"oid\" where p.serialnumber = '00037315';",
                "Количество записей с фильтром 'Серийный номер устройства' не совпадает");

        System.out.println("5 проверка - Проверяем количество записей по фильтру 'Поставлен на мониторинг'");
        SetFilter("Поставлен на мониторинг",
                "01.05.2023",
                "select count(*) from telmed.pmpattach p\n" +
                        "join dpc.mis_sp_mu m on p.mooid = m.\"oid\" where p.begindate >= '01.05.2023 00:00';",
                "Количество записей с фильтром 'Поставлен на мониторинг' не совпадает");

        System.out.println("6 проверка - Проверяем количество записей по фильтру 'Снят с мониторинга'");
        SetFilter("Снят с мониторинга",
                "17.06.2023",
                "select count(*) from telmed.pmpattach p\n" +
                        "join dpc.mis_sp_mu m on p.mooid = m.\"oid\" where p.enddate <= '17.06.2023 00:00';",
                "Количество записей с фильтром 'Снят с мониторинга' не совпадает");

        System.out.println("7 проверка - Проверяем количество записей по фильтру 'Вид устройства'");
        SetFilter("Вид устройства",
                "Тонометр",
                "select count(*) from telmed.pmpattach p\n" +
                        "join dpc.mis_sp_mu m on p.mooid = m.\"oid\" where p.type = '1';",
                "Количество записей с фильтром 'Вид устройства' не совпадает");

        System.out.println("8 проверка - Проверяем количество записей по фильтру 'Вид устройства'");
        SetFilter("Вид устройства",
                "Глюкометр",
                "select count(*) from telmed.pmpattach p\n" +
                        "join dpc.mis_sp_mu m on p.mooid = m.\"oid\" where p.type = '2';",
                "Количество записей с фильтром 'Вид устройства' не совпадает");

        System.out.println("9 проверка - Проверяем количество записей по фильтру 'Статус устройства'");
        SetFilter("Статус устройства",
                "Не активно",
                "select count(*) from telmed.pmpattach p\n" +
                        "join dpc.mis_sp_mu m on p.mooid = m.\"oid\" where p.active = '0';",
                "Количество записей с фильтром 'Статус устройства' не совпадает");

        System.out.println("10 проверка - Проверяем количество записей по фильтру 'Статус устройства'");
        SetFilter("Статус устройства",
                "Активно",
                "select count(*) from telmed.pmpattach p\n" +
                        "join dpc.mis_sp_mu m on p.mooid = m.\"oid\" where p.active = '1';",
                "Количество записей с фильтром 'Статус устройства' не совпадает");


    }

    @Step("Метод для выбора фильтра в Регистр наблюдений и равнения с БД")
    public void SetFilter(String filter, String selectFilter, String SQL, String mistake) throws SQLException, InterruptedException {
        ClickElement(registerObservations.Filters);
        ClickElement(registerObservations.Reset);

        ClickElement(registerObservations.Filters);
        //Нажимаем на доп фильтры
        if (isElementNotVisible(registerObservations.DopFilters)) {
            ClickElement(registerObservations.DopFilters);
        }
        WaitElement(registerObservations.Field(filter));
        if (filter.equals("Поставлен на мониторинг") | filter.equals("Снят с мониторинга")) {
            driver.findElement(registerObservations.Field(filter)).sendKeys(selectFilter);
            //Нажимаем чтобы скрыть календарь
            if (filter.equals("Снят с мониторинга")) {
                ClickElement(registerObservations.Field("Медицинская организация"));
            }
        } else {
            if(filter.equals("Статус устройства") | filter.equals("Вид устройства")) {
                ClickElement(registerObservations.Field(filter));
            } else {
                inputWord(driver.findElement(registerObservations.Field(filter)), selectFilter + " ");
            }
        }
        if(filter.equals("Медицинская организация") | filter.equals("Вид устройства") | filter.equals("Статус устройства")) {
            ClickElement(authorizationObject.Select(selectFilter));
        }
        ClickElement(registerObservations.Search);

        GetSql(SQL, mistake);
    }

    @Step("Метод для сравнения значений в Регистр наблюдений с БД")
    public void GetSql(String SQL, String str) throws SQLException, InterruptedException {
        authorizationObject.LoadingTime(10);
        Thread.sleep(2000);
        WaitElement(registerObservations.AllCount);
        String Count = driver.findElement(registerObservations.AllCount).getText().substring(6);

        sql.StartConnection(SQL);
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(Count, sql.value, str);
    }
}
