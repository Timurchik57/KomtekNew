package UI.TmTest.AccessUI.VIMIS;

import Base.BaseAPI;
import Base.TestListener;
import Base.BaseTest;
import UI.TmTest.PageObject.AuthorizationObject;
import UI.TmTest.PageObject.VIMIS.StandardsProvision;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

@ExtendWith(TestListener.class)
@Epic("Тесты UI")
@Feature("Вимис")
@Tag("Стандарты_оказания_МП")
public class Access_3330Test extends BaseAPI {

    AuthorizationObject authorizationObject;
    StandardsProvision standardsProvision;

    @Test
    @Issue(value = "TEL-3330")
    @Owner(value = "Галиакберов Тимур")
    @Link(name = "ТМС-2049", url = "https://team-1okm.testit.software/projects/5/tests/2049?isolatedSection=af882452-d09a-4051-af99-3d04bf599188")
    @Description("Переходим в Вимис - Стандарты оказания МП и проверяем Поиск, создание, удаление, изменение")
    @DisplayName("Поиск, создание, удаление, изменение в Стандарты оказания МП")
    public void Access_3330() throws SQLException, InterruptedException {

        authorizationObject = new AuthorizationObject(driver);
        standardsProvision = new StandardsProvision(driver);

        AuthorizationMethod(authorizationObject.OKB);
        ClickElement(standardsProvision.StandardsProv);

        System.out.println("1 проверка - Проверяем количество всех записей");
        GetSql("select count (*) from telmed.medical_care_standards;", "Количество всех записей не совпадает");

        System.out.println("2 проверка - Проверяем количество записей по 'Перечень диагнозов'");
        SetFilter("Перечень диагнозов",
                "J06 ",
                "J06.0 Острый ларингофарингит",
                "select count (*) from telmed.medical_care_standards mcs where diagnosiscode = 'J06.0';",
                "Количество записей по 'Перечень диагнозов' не совпадает");

        System.out.println("3 проверка - Проверяем количество записей по 'Возраст пациента'");
        SetFilter("Возраст пациента",
                "",
                "Взрослые",
                "select count (*) from telmed.medical_care_standards mcs where age = '1';",
                "Количество записей по 'Возраст пациента' не совпадает");

        System.out.println("4 проверка - Проверяем количество записей по 'Пол пациента'");
        SetFilter("Пол пациента",
                "",
                "Женский",
                "select count (*) from telmed.medical_care_standards mcs where sex = '2';",
                "Количество записей по 'Пол пациента' не совпадает");

        System.out.println("5 проверка - Проверяем количество записей по 'Назначаемая услуга'");
        SetFilter("Назначаемая услуга",
                "A.12.09.001.08",
                "A.12.09.001.08 HMP30",
                "select count (*) from telmed.medical_care_standards mcs where servicecode = 'A.12.09.001.08';",
                "Количество записей по 'Назначаемая услуга' не совпадает");

        ClickElement(standardsProvision.Reset);
        authorizationObject.LoadingTime(10);

        System.out.println("6 проверка - Проверяем Создание записи");
        ClickElement(standardsProvision.Add("1"));
        WaitElement(standardsProvision.SetField("Перечень диагнозов"));
        driver.findElement(standardsProvision.SetField("Перечень диагнозов")).sendKeys("A37.1");
        ClickElement(authorizationObject.Select("A37.1 Коклюш, вызванный Bordetella parapertussis"));
        SelectClickMethod(standardsProvision.SetField("Возраст пациента"), authorizationObject.Select("Дети"));
        SelectClickMethod(standardsProvision.SetField("Пол пациента"), authorizationObject.Select("Мужской"));
        driver.findElement(standardsProvision.SetField("Назначаемая услуга")).sendKeys("A16.07.026");
        Thread.sleep(2000);
        ClickElement(authorizationObject.SelectFirst);
        inputWord(driver.findElement(standardsProvision.SetField("Частота услуги")), "36 ");
        inputWord(driver.findElement(standardsProvision.SetField("Кратность услуги")), "36 ");
        ClickElement(standardsProvision.Add("2"));
        authorizationObject.LoadingTime(10);
        Thread.sleep(2000);

        sql.SQL("select count(*) from telmed.medical_care_standards mcs \n" +
                "where diagnosiscode = 'A37.1' and age = '2' and sex = '1' and servicecode = 'A16.07.026' and frequency = '36' and multiplicity = '36';");
        driver.navigate().refresh();

        System.out.println("7 проверка - Проверяем редактирование записи");
        /** age - Возрастная группа пациентов (int, обязательный. Может принимать значения:
        1 - Взрослые
        2 - Дети
        3 - Любой

        sex - Пол пациента (int, обязательный. Может принимать значения:
        1 - Мужской
        2 - Женский
        3 - Любой */
        ClickElement(standardsProvision.Edit);
        WaitElement(standardsProvision.SetField("Перечень диагнозов"));
        driver.findElement(standardsProvision.SetField("Перечень диагнозов")).sendKeys("J06.9");
        ClickElement(authorizationObject.Select("J06.9 Острая инфекция верхних дыхательных путей неуточненная"));
        SelectClickMethod(standardsProvision.SetField("Возраст пациента"), authorizationObject.Select("Дети"));
        SelectClickMethod(standardsProvision.SetField("Пол пациента"), authorizationObject.Select("Мужской"));
        driver.findElement(standardsProvision.SetField("Назначаемая услуга")).sendKeys("A16.07.026");
        Thread.sleep(2000);
        ClickElement(authorizationObject.SelectFirst);
        inputWord(driver.findElement(standardsProvision.SetField("Частота услуги")), "96 ");
        inputWord(driver.findElement(standardsProvision.SetField("Кратность услуги")), "96 ");
        ClickElement(standardsProvision.Update);
        authorizationObject.LoadingTime(10);
        Thread.sleep(2000);

        sql.SQL("select count(*) from telmed.medical_care_standards mcs \n" +
                "where diagnosiscode = 'J06.9' and age = '2' and sex = '1' and servicecode = 'A16.07.026' and frequency = '96' and multiplicity = '96';");

        System.out.println("8 проверка - Проверяем Удаление записи");
        int number = 0;
        while (!isElementNotVisible(standardsProvision.frequencyDelete("96")) & number < 10) {
            number++;
            ClickElement(standardsProvision.Next);
        }
        Thread.sleep(2000);
        ClickElement(standardsProvision.frequencyDelete("36"));
        ClickElement(standardsProvision.YesDelete);
        authorizationObject.LoadingTime(10);
        Thread.sleep(2000);
        sql.NotSQL("select count(*) from telmed.medical_care_standards mcs \n" +
                "where diagnosiscode = 'J06.9' and age = '2' and sex = '1' and servicecode = 'A16.07.026' and frequency = '36' and multiplicity = '96';");
    }

    @Step("Метод для выбора фильтра в Регистр наблюдений и равнения с БД")
    public void SetFilter(String filter, String input, String selectFilter, String SQL, String mistake) throws SQLException, InterruptedException {
        ClickElement(standardsProvision.Reset);
        authorizationObject.LoadingTime(10);
        ClickElement(standardsProvision.GetField(filter));
        if (filter.equals("Перечень диагнозов") | filter.equals("Назначаемая услуга") ) {
            inputWord(driver.findElement(standardsProvision.GetField(filter)), input);
            authorizationObject.LoadingTime(10);
        }
        ClickElement(authorizationObject.Select(selectFilter));
        ClickElement(standardsProvision.Search);
        authorizationObject.LoadingTime(10);
        Thread.sleep(2000);

        GetSql(SQL, mistake);
    }

    @Step("Метод для сравнения значений в Регистр наблюдений с БД")
    public void GetSql(String SQL, String str) throws SQLException, InterruptedException {
        authorizationObject.LoadingTime(10);
        Thread.sleep(2000);
        WaitElement(standardsProvision.AllCount);
        String Count = driver.findElement(standardsProvision.AllCount).getText().substring(6);

        sql.StartConnection(SQL);
        while (sql.resultSet.next()) {
            sql.value = sql.resultSet.getString("count");
        }
        Assertions.assertEquals(Count, sql.value, str);
    }
}